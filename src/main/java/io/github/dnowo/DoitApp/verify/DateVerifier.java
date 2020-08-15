package io.github.dnowo.DoitApp.verify;

import io.github.dnowo.DoitApp.Constants;
import io.github.dnowo.DoitApp.model.Job;
import io.github.dnowo.DoitApp.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DateVerifier {
    private final JobService jobService;

    public void verifyJobTime(List<Job> jobsFromDatabase){

        LocalDateTime dateTimeNow = LocalDateTime.now();
        String time = dateTimeNow.format(Constants.DATE_FORMAT);
        LocalDateTime localDateTimeNow = LocalDateTime.parse(time);

        for(Job j : jobsFromDatabase){
            LocalDateTime actualJobDateTime = j.getDeadline();

            if(localDateTimeNow.isAfter(actualJobDateTime)){
                jobService.changeEndedState(j);
            }
        }
    }
}
