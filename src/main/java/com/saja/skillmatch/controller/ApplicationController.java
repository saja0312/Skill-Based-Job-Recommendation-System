package com.saja.skillmatch.controller;

import com.saja.skillmatch.model.Application;
import com.saja.skillmatch.model.Job;
import com.saja.skillmatch.model.User;
import com.saja.skillmatch.repository.ApplicationRepository;
import com.saja.skillmatch.repository.JobRepository;
import com.saja.skillmatch.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationRepository applications;
    private final UserRepository users;
    private final JobRepository jobs;

    public ApplicationController(ApplicationRepository applications, UserRepository users, JobRepository jobs) {
        this.applications = applications;
        this.users = users;
        this.jobs = jobs;
    }

    @PostMapping
    public Application apply(@RequestParam Long candidateId, @RequestParam Long jobId) {
        if (applications.findByCandidateIdAndJobId(candidateId, jobId).isPresent()) {
            throw new IllegalArgumentException("Already applied for this job");
        }
        User candidate = users.findById(candidateId).orElseThrow();
        Job job = jobs.findById(jobId).orElseThrow();
        return applications.save(new Application(candidate, job));
    }

    @GetMapping("/candidate/{candidateId}")
    public List<Application> candidateApplications(@PathVariable Long candidateId) {
        return applications.findByCandidateId(candidateId);
    }

    @GetMapping("/recruiter/{recruiterId}")
    public List<Application> recruiterApplications(@PathVariable Long recruiterId) {
        return applications.findByJobRecruiterId(recruiterId);
    }

    @PatchMapping("/{id}/status")
    public Application updateStatus(@PathVariable Long id, @RequestParam String status) {
        Application a = applications.findById(id).orElseThrow();
        a.setStatus(status);
        return applications.save(a);
    }
}
