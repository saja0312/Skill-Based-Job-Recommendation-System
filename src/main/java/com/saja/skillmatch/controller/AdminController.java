package com.saja.skillmatch.controller;

import com.saja.skillmatch.repository.ApplicationRepository;
import com.saja.skillmatch.repository.JobRepository;
import com.saja.skillmatch.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository users;
    private final JobRepository jobs;
    private final ApplicationRepository applications;

    public AdminController(UserRepository users, JobRepository jobs, ApplicationRepository applications) {
        this.users = users;
        this.jobs = jobs;
        this.applications = applications;
    }

    @GetMapping("/stats")
    public Stats stats() {
        return new Stats(users.count(), jobs.count(), applications.count());
    }

    public record Stats(long users, long jobs, long applications) {}
}
