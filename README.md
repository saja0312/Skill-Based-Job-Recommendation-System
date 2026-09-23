# SkillMatch Pro

Skill-Based Job Recommendation System built with Java 17, Spring Boot, MySQL, Spring Data JPA and Spring Security/JWT.

## Requirements
- JDK 17+ (JDK 25 is also fine)
- Maven 3.9+
- MySQL 8+
- VS Code / IntelliJ / Eclipse

## 1. Create database
Open MySQL and run:
```sql
CREATE DATABASE skillmatch_db;
```

If your MySQL root account has a password, edit:
`src/main/resources/application.properties`

Set:
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

## 2. Build
Open terminal in this project folder:
```powershell
mvn clean install
```

## 3. Run
```powershell
mvn spring-boot:run
```

Then open:
http://localhost:8080

## Demo accounts
Candidate:
candidate@skillmatch.com / candidate123

Recruiter:
recruiter@skillmatch.com / recruiter123

Admin:
admin@skillmatch.com / admin123

## Important
Apache/XAMPP Apache is NOT required. Spring Boot uses its embedded Tomcat server.
MySQL must be running before starting the application.


## Recruiter job creation JSON
When creating a job with `POST /api/jobs`, include `recruiterId`:
```json
{
  "recruiterId": 2,
  "title": "Java Backend Developer",
  "description": "Build REST APIs and backend services.",
  "skills": "Java, Spring Boot, SQL, REST API",
  "minExperience": 0,
  "location": "Chennai",
  "remote": true,
  "company": "TechNova"
}
```


## UI upgrade
The dashboard now includes a modern responsive layout, personalized recommendation cards, match-score rings, filters, search, application tracking and 25+ seeded job opportunities.
