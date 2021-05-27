package io.github.dnowo.DoitApp.verify;

import io.github.dnowo.DoitApp.model.Job;
import io.github.dnowo.DoitApp.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RepeatVerifier {
    private final JobRepository jobRepository;

    @Transactional
    public List<Job> verifyRepeatableJobs(List<Job> toVerify) {
        LocalDateTime now = LocalDateTime.now();

        toVerify.forEach(j -> {
            if (j.getRepeatable() && now.isAfter(j.getDeadline())) {
                saveDate(j);
            }
        });
        System.out.println(now);
        System.out.println(now);
        System.out.println(now);
        System.out.println(now);
        System.out.println(now);
        return toVerify;
    }

    private void saveDate(Job job){
        Job editedJob = jobRepository.findById(job.getId()).orElseThrow();
        editedJob.setDeadline(job.getDeadline().plusDays(1));
    }
}
