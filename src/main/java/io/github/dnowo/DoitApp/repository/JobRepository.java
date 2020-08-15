package io.github.dnowo.DoitApp.repository;

import io.github.dnowo.DoitApp.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findAll();

    Job getJobById(Long id);

}
