package io.github.dnowo.DoitApp.verify;

import io.github.dnowo.DoitApp.Constants;
import io.github.dnowo.DoitApp.model.Job;
import io.github.dnowo.DoitApp.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DateVerifier {
    private final JobRepository jobRepository;

    @Transactional
    public List<Job> verifyJobTime(List<Job> jobsFromDatabase) {
        LocalDateTime dateTimeNow = LocalDateTime.now().plusHours(2);
        String time = dateTimeNow.format(Constants.DATE_FORMAT);
        LocalDateTime localDateTimeNow = LocalDateTime.parse(time);

        jobsFromDatabase.forEach(j -> {
            LocalDateTime actualJobDateTime = j.getDeadline();
            if (localDateTimeNow.isAfter(actualJobDateTime)) {
                changeEndedState(j);
            }
        });

        return jobsFromDatabase;
    }

    private void changeEndedState(Job job) {
        Job editedJob = jobRepository.findById(job.getId()).orElseThrow();
        editedJob.setEnded(true);
    }
}
