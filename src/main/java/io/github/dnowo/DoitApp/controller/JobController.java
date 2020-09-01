package io.github.dnowo.DoitApp.controller;

import io.github.dnowo.DoitApp.model.Job;
import io.github.dnowo.DoitApp.model.User;
import io.github.dnowo.DoitApp.repository.UserRepository;
import io.github.dnowo.DoitApp.service.JobService;
import io.github.dnowo.DoitApp.verify.DateVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
    private final UserRepository userRepository;


    @GetMapping("/api/job/all")
    public List<Job> getJobs(@RequestParam(required = false) int page,
                             @AuthenticationPrincipal UsernamePasswordAuthenticationToken userAuthenticated){
        int pageNumber = page >= 0 ? page : 0;
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
