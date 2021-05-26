package io.github.dnowo.DoitApp.verify;

import io.github.dnowo.DoitApp.model.Job;
import io.github.dnowo.DoitApp.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class RepeatVerifier {
    private final JobRepository jobRepository;

    @Autowired
    public RepeatVerifier(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> verifyRepeatableJobs(List<Job> toVerify) {
        for (Job j : toVerify) {
            if (j.getRepeatable() && LocalDateTime.now().isAfter(j.getDeadline())) {
                saveDate(j);
            }
        }
        return toVerify;
    }

    @Transactional
    private void saveDate(Job job){
        Job editedJob = jobRepository.findById(job.getId()).orElseThrow();
        editedJob.setDeadline(job.getDeadline().plusDays(1));
        jobRepository.save(editedJob);
    }
}
