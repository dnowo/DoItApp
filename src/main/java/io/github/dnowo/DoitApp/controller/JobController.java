package io.github.dnowo.DoitApp.controller;

import io.github.dnowo.DoitApp.model.Job;
import io.github.dnowo.DoitApp.model.User;
import io.github.dnowo.DoitApp.repository.UserRepository;
import io.github.dnowo.DoitApp.service.JobService;
import io.github.dnowo.DoitApp.verify.DateVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
    private final UserRepository jobRepository;
    private final DateVerifier dateVerifier;

    @GetMapping("/api/job/all")
    public List<Job> getJobs(@RequestParam(required = false) int page, @AuthenticationPrincipal UsernamePasswordAuthenticationToken user){
        int pageNumber = page >= 0 ? page : 0;
        List<Job> jobList = jobService.getJobs(pageNumber);
        dateVerifier.verifyJobTime(jobList);

        User userFound = jobRepository.findByUsername(user.getPrincipal().toString());
        List<Job> forUser = jobList
                .stream()
                .filter(job ->
                        job.getUser().getId().equals(userFound.getId()))
                .collect(Collectors.toList());
        return forUser;
    }

    @GetMapping("/api/job/unsorted")
    public List<Job> finAllUnsorted(){
        List<Job> jobList = jobService.findAllUnsorted();
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

    @DeleteMapping("/api/job/delete/{id}")
    public void deleteJob(@PathVariable Long id){
        jobService.deleteJob(id);
    }

    @PutMapping("/api/job/edit")
    public Job editJob(@RequestBody Job job){
        return jobService.edit(job);
    }

}
