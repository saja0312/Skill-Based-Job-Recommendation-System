package com.saja.skillmatch.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String skills;
    private Integer experienceYears;
    private String preferredRole;
    private String location;
    private Boolean remotePreferred;

    public User() {}

    public User(String email, String password, String name, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public Role getRole() { return role; }
    public String getSkills() { return skills; }
    public Integer getExperienceYears() { return experienceYears; }
    public String getPreferredRole() { return preferredRole; }
    public String getLocation() { return location; }
    public Boolean getRemotePreferred() { return remotePreferred; }

    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
    public void setRole(Role role) { this.role = role; }
    public void setSkills(String skills) { this.skills = skills; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    public void setPreferredRole(String preferredRole) { this.preferredRole = preferredRole; }
    public void setLocation(String location) { this.location = location; }
    public void setRemotePreferred(Boolean remotePreferred) { this.remotePreferred = remotePreferred; }
}
