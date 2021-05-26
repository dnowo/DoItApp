package io.github.dnowo.DoitApp.service;

import io.github.dnowo.DoitApp.model.Job;
import io.github.dnowo.DoitApp.model.User;
import io.github.dnowo.DoitApp.model.dto.JobPagesDto;
import io.github.dnowo.DoitApp.repository.JobRepository;
import io.github.dnowo.DoitApp.verify.DateVerifier;
import io.github.dnowo.DoitApp.verify.RepeatVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {
    private static final int PAGE_SIZE = 6;
    private final JobRepository jobRepository;
    private final DateVerifier dateVerifier;
    private final RepeatVerifier repeatVerifier;

    public JobPagesDto getJobs(int page, User user) {
        System.out.println("page " + page);
        long totalItems = jobRepository.findAll().stream().filter(job ->
                job.getUser().getId().equals(user.getId())).count();

        Pageable pageable = PageRequest.of(page, PAGE_SIZE,
                Sort.by(Sort.Order.desc("deadline"),
                        Sort.Order.asc("priority")));

        List<Job> forUser = jobRepository.findAll(pageable)
                .stream()
                .filter(job ->
                        job.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());

        forUser = dateVerifier.verifyJobTime(forUser);
        forUser = repeatVerifier.verifyRepeatableJobs(forUser);


        long totalPages = (long) (Math.ceil((double) totalItems / (double) PAGE_SIZE)) - 1L;
        if (totalPages < 1)
            totalPages = 0;

        JobPagesDto jobPagesDto = new JobPagesDto();
        jobPagesDto.setJobs(forUser);
        jobPagesDto.setCurrentPage(Long.parseLong("" + page));
        jobPagesDto.setTotalItems(totalItems);
        jobPagesDto.setTotalPages(totalPages);

        log.info(jobPagesDto.toString());
        log.info(user.toString());

        return jobPagesDto;
    }

    @Transactional
    public Job edit(Job job) {
        Job jobToEdit = jobRepository.findById(job.getId()).orElseThrow();
        jobToEdit.setEnded(job.getEnded());
        jobToEdit.setDeadline(job.getDeadline());
        jobToEdit.setDescription(job.getDescription());
        jobToEdit.setRepeatable(job.getRepeatable());
        jobToEdit.setPriority(job.getPriority());
        jobToEdit.setTitle(job.getTitle());

        return jobToEdit;
    }

    public Job getJobById(Long id) {
        return jobRepository.getJobById(id);
    }

    public List<Job> findAllUnsorted() {
        return dateVerifier.verifyJobTime(jobRepository.findAll());
    }

    public Job addJob(Job job) {
        job.setEnded(false);
        System.out.println(job.toString());
        return jobRepository.save(job);
    }

    @Transactional
    public void deleteJob(Long id, User user) {
        Job job = jobRepository.findById(id).orElseThrow();
        if (user.getId().equals(job.getUser().getId())) {
            jobRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cannot delete this job!");
        }
    }

}
