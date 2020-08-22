package io.github.dnowo.DoitApp.repository;

import io.github.dnowo.DoitApp.model.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j FROM Job j ORDER BY j.ended ASC, j.deadline ASC, j.priority ASC")
    List<Job> findAllJobSorted(Pageable page);

    Job getJobById(Long id);

    @Query("SELECT j FROM Job j")
    List<Job> findAll();

    @Query("SELECT j FROM Job j WHERE j.ended=0 GROUP BY j.ended, j.id ORDER BY j.deadline asc")
    List<Job> findAllNotEndedNearest();
}
