package io.github.dnowo.DoitApp.repository;

import io.github.dnowo.DoitApp.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findAll(Pageable page);
    Job getJobById(Long id);
    Optional<Job> findById(Long id);
}
