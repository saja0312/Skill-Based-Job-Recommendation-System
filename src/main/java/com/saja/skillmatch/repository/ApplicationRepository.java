package com.saja.skillmatch.repository;

import com.saja.skillmatch.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByCandidateId(Long candidateId);
    List<Application> findByJobRecruiterId(Long recruiterId);
    Optional<Application> findByCandidateIdAndJobId(Long candidateId, Long jobId);
}
