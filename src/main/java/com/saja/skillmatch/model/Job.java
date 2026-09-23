package com.saja.skillmatch.model;

import jakarta.persistence.*;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    private String skills;
    private Integer minExperience;
    private String location;
    private Boolean remote;
    private String company;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    public Job() {}

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSkills() { return skills; }
    public Integer getMinExperience() { return minExperience; }
    public String getLocation() { return location; }
    public Boolean getRemote() { return remote; }
    public String getCompany() { return company; }
    public User getRecruiter() { return recruiter; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setSkills(String skills) { this.skills = skills; }
    public void setMinExperience(Integer minExperience) { this.minExperience = minExperience; }
    public void setLocation(String location) { this.location = location; }
    public void setRemote(Boolean remote) { this.remote = remote; }
    public void setCompany(String company) { this.company = company; }
    public void setRecruiter(User recruiter) { this.recruiter = recruiter; }
}
