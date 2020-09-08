package io.github.dnowo.DoitApp.service;

import io.github.dnowo.DoitApp.model.Job;
import io.github.dnowo.DoitApp.model.User;
import io.github.dnowo.DoitApp.repository.JobRepository;
import io.github.dnowo.DoitApp.verify.DateVerifier;
import io.github.dnowo.DoitApp.verify.RepeatVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {
    private static final int PAGE_SIZE = 6;
    private final JobRepository jobRepository;
    private final DateVerifier dateVerifier;
    private final RepeatVerifier repeatVerifier;

    public List<Job> getJobs(int page, User user){
        List<Job> forUser = jobRepository.findAll(PageRequest.of(page,
                        PAGE_SIZE,
                        Sort.by(Sort.Order.asc("ended"),
                                Sort.Order.desc("deadline"),
                                Sort.Order.asc("priority"))))
                .stream()
                .filter(job ->
                        job.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
        forUser = dateVerifier.verifyJobTime(forUser);
        forUser = repeatVerifier.verifyRepeatableJobs(forUser);
        return forUser;
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

    public List<Job> findAllUnsorted() { return dateVerifier.verifyJobTime(jobRepository.findAll()); }

    public Job addJob(Job job) {
        return jobRepository.save(job);
    }

    public void deleteJob(Long id, User user) {
        Job job = jobRepository.findById(id).orElseThrow();
        if (user.getId() == job.getUser().getId()) {
            jobRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cannot delete this job!");
        }
    }

}
