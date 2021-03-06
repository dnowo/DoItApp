package io.github.dnowo.DoitApp.controller;

import io.github.dnowo.DoitApp.model.Job;
import io.github.dnowo.DoitApp.model.User;
import io.github.dnowo.DoitApp.model.dto.JobPagesDto;
import io.github.dnowo.DoitApp.repository.UserRepository;
import io.github.dnowo.DoitApp.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
    private final UserRepository userRepository;

    @GetMapping("/api/job/all")
    public JobPagesDto getJobs(@RequestParam(defaultValue = "0") int page,
                               @AuthenticationPrincipal UsernamePasswordAuthenticationToken userAuthenticated){
        User user = userRepository.findByUsername(userAuthenticated.getPrincipal().toString());

        return jobService.getJobs(page, user);
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
