package com.saja.skillmatch.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications",
       uniqueConstraints = @UniqueConstraint(columnNames = {"candidate_id", "job_id"}))
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "candidate_id")
    private User candidate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(nullable = false)
    private String status = "APPLIED";

    @Column(nullable = false)
    private LocalDateTime appliedAt = LocalDateTime.now();

    public Application() {}

    public Application(User candidate, Job job) {
        this.candidate = candidate;
        this.job = job;
    }

    public Long getId() { return id; }
    public User getCandidate() { return candidate; }
    public Job getJob() { return job; }
    public String getStatus() { return status; }
    public LocalDateTime getAppliedAt() { return appliedAt; }

    public void setId(Long id) { this.id = id; }
    public void setCandidate(User candidate) { this.candidate = candidate; }
    public void setJob(Job job) { this.job = job; }
    public void setStatus(String status) { this.status = status; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
}
