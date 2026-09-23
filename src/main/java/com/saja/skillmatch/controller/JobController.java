package com.saja.skillmatch.controller;

import com.saja.skillmatch.model.Job;
import com.saja.skillmatch.model.User;
import com.saja.skillmatch.repository.JobRepository;
import com.saja.skillmatch.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobRepository jobs;
    private final UserRepository users;

    public JobController(JobRepository jobs, UserRepository users) {
        this.jobs = jobs;
        this.users = users;
    }

    @GetMapping
    public List<Job> all(@RequestParam(required = false) String q) {
        return q == null || q.isBlank()
            ? jobs.findAll()
            : jobs.findByTitleContainingIgnoreCaseOrSkillsContainingIgnoreCase(q, q);
    }

    @PostMapping
    public Job create(@RequestBody JobRequest req) {
        User recruiter = users.findById(req.recruiterId()).orElseThrow();
        Job j = new Job();
        j.setTitle(req.title());
        j.setDescription(req.description());
        j.setSkills(req.skills());
        j.setMinExperience(req.minExperience());
        j.setLocation(req.location());
        j.setRemote(req.remote());
        j.setCompany(req.company());
        j.setRecruiter(recruiter);
        return jobs.save(j);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        jobs.deleteById(id);
    }

    public record JobRequest(Long recruiterId, String title, String description, String skills,
                              Integer minExperience, String location, Boolean remote, String company) {}
}
