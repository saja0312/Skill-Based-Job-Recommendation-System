package com.saja.skillmatch.repository;

import com.saja.skillmatch.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByTitleContainingIgnoreCaseOrSkillsContainingIgnoreCase(String title, String skills);
}
