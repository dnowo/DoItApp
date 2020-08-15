package io.github.dnowo.DoitApp.controller;

import io.github.dnowo.DoitApp.model.Job;
import io.github.dnowo.DoitApp.service.JobService;
import io.github.dnowo.DoitApp.verify.DateVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
    private final DateVerifier dateVerifier;

    @GetMapping("/api/job/all")
    public List<Job> getJobs(){
        List<Job> jobList = jobService.getJobs();
        dateVerifier.verifyJobTime(jobList);
        return jobList;
    }

    @GetMapping("api/job/{id}")
    public Job getJobById(@PathVariable Long id){
        return jobService.getJobById(id);
    }

    @PostMapping("/api/job/add")
    public Job addJob(@RequestBody Job job){
        return jobService.addJob(job);
    }

    @DeleteMapping("/api/job/{id}/delete")
    public void deleteJob(@PathVariable Long id){
        jobService.deleteJob(id);
    }

    @PutMapping("/api/job/edit")
    public Job editJob(@RequestBody Job job){
        return jobService.edit(job);
    }

}
