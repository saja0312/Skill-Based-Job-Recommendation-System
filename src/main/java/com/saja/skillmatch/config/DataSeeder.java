package com.saja.skillmatch.config;

import com.saja.skillmatch.model.Job;
import com.saja.skillmatch.model.Role;
import com.saja.skillmatch.model.User;
import com.saja.skillmatch.repository.JobRepository;
import com.saja.skillmatch.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(UserRepository users, JobRepository jobs, PasswordEncoder encoder) {
        return args -> {
            User candidate = users.findByEmail("candidate@skillmatch.com").orElseGet(() -> {
                User u = new User("candidate@skillmatch.com", encoder.encode("candidate123"), "Demo Candidate", Role.CANDIDATE);
                u.setSkills("Java, Spring Boot, SQL, HTML, CSS, JavaScript, Git, REST API");
                u.setExperienceYears(1);
                u.setPreferredRole("Backend Developer");
                u.setLocation("Chennai");
                u.setRemotePreferred(true);
                return users.save(u);
            });

            User recruiter = users.findByEmail("recruiter@skillmatch.com").orElseGet(() ->
                users.save(new User("recruiter@skillmatch.com", encoder.encode("recruiter123"), "Demo Recruiter", Role.RECRUITER)));

            users.findByEmail("admin@skillmatch.com").orElseGet(() ->
                users.save(new User("admin@skillmatch.com", encoder.encode("admin123"), "Admin", Role.ADMIN)));

            Object[][] data = {
                {"Java Backend Developer", "Build scalable REST APIs and backend services for enterprise products.", "Java, Spring Boot, SQL, REST API, Git", 0, "Chennai", true, "TechNova"},
                {"Full Stack Developer", "Develop modern full-stack applications used by thousands of customers.", "Java, Spring Boot, JavaScript, HTML, CSS, React, SQL", 1, "Bangalore", true, "CodeWorks"},
                {"Junior Software Engineer", "Join a product engineering team and build reliable software features.", "Java, SQL, Git, HTML, CSS", 0, "Chennai", false, "NextGen Systems"},
                {"Spring Boot Developer", "Create production-ready microservices and REST APIs.", "Java, Spring Boot, REST API, MySQL, Git", 1, "Chennai", true, "CloudOrbit"},
                {"Java Microservices Engineer", "Design and maintain distributed microservices.", "Java, Spring Boot, Microservices, Docker, SQL", 2, "Bangalore", true, "FinEdge"},
                {"Backend Developer", "Work on secure APIs and high-volume backend services.", "Java, Spring Boot, SQL, REST API, AWS", 1, "Hyderabad", true, "ByteCraft"},
                {"Software Developer - Java", "Build business applications with Java and relational databases.", "Java, Spring, SQL, Git, Maven", 0, "Coimbatore", false, "DevSphere"},
                {"Associate Software Engineer", "Develop and test web applications with a collaborative engineering team.", "Java, SQL, JavaScript, Git, HTML", 0, "Chennai", true, "InnoLabs"},
                {"Java API Developer", "Implement clean REST endpoints and integrations.", "Java, Spring Boot, REST API, JSON, SQL", 1, "Pune", true, "APIWorks"},
                {"Web Application Developer", "Create responsive web applications and backend integrations.", "Java, HTML, CSS, JavaScript, SQL", 0, "Chennai", true, "WebNest"},
                {"React + Java Full Stack Developer", "Build user-facing products with React and Spring Boot.", "Java, Spring Boot, React, JavaScript, SQL, Git", 1, "Bangalore", true, "PixelForge"},
                {"Software Engineer - Backend", "Develop scalable backend modules and automated tests.", "Java, Spring Boot, JUnit, SQL, Git", 1, "Chennai", false, "StackLabs"},
                {"Graduate Software Engineer", "Entry-level role for developers passionate about building software.", "Java, SQL, Git, OOP, HTML", 0, "Chennai", false, "LaunchPad Tech"},
                {"Cloud Java Developer", "Build cloud-native Java services and deployment pipelines.", "Java, Spring Boot, AWS, Docker, SQL", 2, "Hyderabad", true, "CloudNova"},
                {"Java Developer - FinTech", "Develop transaction and payment platform services.", "Java, Spring Boot, SQL, REST API, Microservices", 1, "Mumbai", false, "PayGrid"},
                {"Backend Engineer - SaaS", "Develop APIs for a growing SaaS platform.", "Java, Spring Boot, PostgreSQL, REST API, Docker", 1, "Chennai", true, "SaaSly"},
                {"Full Stack Engineer", "Own features across frontend, backend and database layers.", "Java, Spring Boot, React, JavaScript, PostgreSQL", 2, "Bangalore", true, "BuildFlow"},
                {"Java Developer - E Commerce", "Build catalog, order and payment services.", "Java, Spring Boot, MySQL, REST API, Git", 1, "Chennai", true, "ShopStack"},
                {"Application Support Engineer", "Troubleshoot and improve enterprise Java applications.", "Java, SQL, Linux, Git, REST API", 0, "Chennai", false, "EnterpriseOne"},
                {"DevOps Java Engineer", "Automate builds and deployments for Java services.", "Java, Spring Boot, Docker, Jenkins, AWS, Git", 2, "Pune", true, "DeployHub"},
                {"Java Test Automation Engineer", "Create automated tests for backend and web applications.", "Java, Selenium, JUnit, SQL, Git", 1, "Chennai", true, "QualityWorks"},
                {"Android Java Developer", "Develop and maintain Android applications.", "Java, Android, REST API, Git, SQL", 1, "Bangalore", false, "MobileMint"},
                {"Data Platform Engineer", "Build data services and backend pipelines.", "Java, SQL, Python, Spring Boot, AWS", 2, "Hyderabad", true, "DataPulse"},
                {"Security Software Engineer", "Develop secure application components and API services.", "Java, Spring Boot, SQL, REST API, Security", 1, "Chennai", true, "SecureStack"},
                {"Technical Graduate Trainee", "Learn modern Java development while shipping real product features.", "Java, SQL, Git, HTML, CSS, OOP", 0, "Chennai", true, "CareerForge"},
                {"Node + Java Full Stack Engineer", "Build integrations across Java services and JavaScript applications.", "Java, Spring Boot, JavaScript, Node.js, React, SQL", 1, "Bangalore", true, "FusionTech"},
                {"Cloud Support Developer", "Maintain cloud-hosted applications and backend integrations.", "Java, SQL, AWS, Linux, REST API", 0, "Chennai", true, "CloudDesk"},
                {"Enterprise Java Engineer", "Work on large-scale enterprise systems and integrations.", "Java, Spring Boot, SQL, Maven, Microservices", 2, "Chennai", false, "CoreSystems"}
            };

            for (Object[] d : data) {
                String title = (String) d[0];
                if (jobs.findAll().stream().noneMatch(existing -> existing.getTitle().equalsIgnoreCase(title))) {
                    Job j = new Job();
                    j.setTitle(title);
                    j.setDescription((String) d[1]);
                    j.setSkills((String) d[2]);
                    j.setMinExperience((Integer) d[3]);
                    j.setLocation((String) d[4]);
                    j.setRemote((Boolean) d[5]);
                    j.setCompany((String) d[6]);
                    j.setRecruiter(recruiter);
                    jobs.save(j);
                }
            }
        };
    }
}
