package io.github.dnowo.DoitApp.controller;

import io.github.dnowo.DoitApp.model.Job;
import io.github.dnowo.DoitApp.model.User;
import io.github.dnowo.DoitApp.repository.UserRepository;
import io.github.dnowo.DoitApp.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JobController {
    private final JobService jobService;
    private final UserRepository userRepository;

    @Autowired
    public JobController(JobService jobService, UserRepository userRepository) {
        this.jobService = jobService;
        this.userRepository = userRepository;
    }

    @GetMapping("/api/job/all")
    public List<Job> getJobs(@RequestParam(required = false) int page,
                             @AuthenticationPrincipal UsernamePasswordAuthenticationToken userAuthenticated){
        int pageNumber = page >= 0 ? page : 1;
        User user = userRepository.findByUsername(userAuthenticated.getPrincipal().toString());

        return jobService.getJobs(pageNumber, user);
    }

    @GetMapping("/api/job/unsorted")
    public List<Job> finAllUnsorted(){
        return jobService.findAllUnsorted();
    }

    @GetMapping("api/job/{id}")
    public Job getJobById(@PathVariable Long id){
        return jobService.getJobById(id);
    }

    @PostMapping("/api/job/add")
    public Job addJob(@RequestBody Job job,
                      @AuthenticationPrincipal UsernamePasswordAuthenticationToken userAuthenticated) {
        User user = userRepository.findByUsername(userAuthenticated.getPrincipal().toString());
        job.setUser(user);
        return jobService.addJob(job);
    }

    @DeleteMapping("/api/job/delete/{id}")
    public void deleteJob(@PathVariable Long id,
                          @AuthenticationPrincipal UsernamePasswordAuthenticationToken userAuthenticated) {
        User user = userRepository.findByUsername(userAuthenticated.getPrincipal().toString());
        jobService.deleteJob(id, user);
    }

    @PutMapping("/api/job/edit")
    public Job editJob(@RequestBody Job job){
        return jobService.edit(job);
    }

}
