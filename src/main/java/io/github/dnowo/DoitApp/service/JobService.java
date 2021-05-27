package io.github.dnowo.DoitApp.service;

import io.github.dnowo.DoitApp.model.Job;
import io.github.dnowo.DoitApp.model.User;
import io.github.dnowo.DoitApp.model.dto.JobPagesDto;
import io.github.dnowo.DoitApp.repository.JobRepository;
import io.github.dnowo.DoitApp.verify.DateVerifier;
import io.github.dnowo.DoitApp.verify.RepeatVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {
    private static final int PAGE_SIZE = 6;
    private final JobRepository jobRepository;
    private final DateVerifier dateVerifier;
    private final RepeatVerifier repeatVerifier;

    public JobPagesDto getJobs(int page, User user) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE,
                Sort.by(Sort.Order.desc("deadline"),
                        Sort.Order.asc("priority")));

        Page<Job> jobWithPages = jobRepository.findAllByUserId(user.getId(), pageable);
        List<Job> forUser = jobWithPages.getContent();

        forUser = dateVerifier.verifyJobTime(forUser);
        forUser = repeatVerifier.verifyRepeatableJobs(forUser);

        JobPagesDto jobPagesDto = new JobPagesDto();
        jobPagesDto.setJobs(forUser);
        jobPagesDto.setCurrentPage(Long.valueOf(jobWithPages.getNumber() + ""));
        jobPagesDto.setTotalItems(jobWithPages.getTotalElements());
        jobPagesDto.setTotalPages(Long.valueOf(jobWithPages.getTotalPages() + ""));

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
