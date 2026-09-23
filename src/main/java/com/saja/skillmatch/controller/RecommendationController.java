package com.saja.skillmatch.controller;

import com.saja.skillmatch.model.Job;
import com.saja.skillmatch.model.User;
import com.saja.skillmatch.repository.JobRepository;
import com.saja.skillmatch.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final UserRepository users;
    private final JobRepository jobs;

    public RecommendationController(UserRepository users, JobRepository jobs) {
        this.users = users;
        this.jobs = jobs;
    }

    @GetMapping("/{candidateId}")
    public List<Recommendation> recommend(@PathVariable Long candidateId) {
        User c = users.findById(candidateId).orElseThrow();
        Set<String> candidateSkills = split(c.getSkills());

        return jobs.findAll().stream()
            .map(j -> score(c, candidateSkills, j))
            .sorted(Comparator.comparingInt(Recommendation::matchPercentage).reversed())
            .toList();
    }

    private Recommendation score(User c, Set<String> candidateSkills, Job j) {
        Set<String> jobSkills = split(j.getSkills());
        Set<String> matched = new TreeSet<>();
        Set<String> missing = new TreeSet<>(jobSkills);

        for (String s : candidateSkills) {
            if (jobSkills.contains(s)) {
                matched.add(s);
                missing.remove(s);
            }
        }

        int skillScore = jobSkills.isEmpty() ? 0 : (int) Math.round(matched.size() * 70.0 / jobSkills.size());
        int experienceScore = c.getExperienceYears() != null && j.getMinExperience() != null
            && c.getExperienceYears() >= j.getMinExperience() ? 15 : 0;
        int roleScore = c.getPreferredRole() != null && j.getTitle() != null
            && j.getTitle().toLowerCase().contains(c.getPreferredRole().toLowerCase().replace("developer", "").trim()) ? 10 : 0;
        int locationScore = Boolean.TRUE.equals(j.getRemote()) || (c.getLocation() != null && c.getLocation().equalsIgnoreCase(j.getLocation())) ? 5 : 0;

        int total = Math.min(100, skillScore + experienceScore + roleScore + locationScore);
        return new Recommendation(j, total, matched, missing);
    }

    private Set<String> split(String s) {
        if (s == null || s.isBlank()) return Set.of();
        return Arrays.stream(s.split(","))
            .map(String::trim)
            .map(String::toLowerCase)
            .filter(x -> !x.isBlank())
            .collect(Collectors.toCollection(TreeSet::new));
    }

    public record Recommendation(Job job, int matchPercentage, Set<String> matchedSkills, Set<String> missingSkills) {}
}
