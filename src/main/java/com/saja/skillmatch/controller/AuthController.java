package com.saja.skillmatch.controller;

import com.saja.skillmatch.model.Role;
import com.saja.skillmatch.model.User;
import com.saja.skillmatch.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest input) {
        if (input.email() == null || input.email().isBlank()
                || input.password() == null || input.password().isBlank()
                || input.name() == null || input.name().isBlank()) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "Name, email and password are required");
        }

        if (users.existsByEmail(input.email().trim())) {
            throw new AuthException(HttpStatus.CONFLICT, "Email already registered");
        }

        User u = new User(
                input.email().trim(),
                encoder.encode(input.password()),
                input.name().trim(),
                Role.CANDIDATE
        );
        u.setSkills(input.skills());
        u.setExperienceYears(input.experienceYears() == null ? 0 : input.experienceYears());
        u.setPreferredRole(input.preferredRole());
        u.setLocation(input.location());
        u.setRemotePreferred(Boolean.TRUE.equals(input.remotePreferred()));

        User saved = users.save(u);
        return new LoginResponse(saved.getId(), saved.getEmail(), saved.getName(), saved.getRole().name());
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        if (req.email() == null || req.password() == null) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "Email and password are required");
        }

        User u = users.findByEmail(req.email().trim())
                .orElseThrow(() -> new AuthException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!encoder.matches(req.password(), u.getPassword())) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return new LoginResponse(u.getId(), u.getEmail(), u.getName(), u.getRole().name());
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus
    public ErrorResponse handleAuthException(AuthException ex) {
        return new ErrorResponse(ex.getStatus().value(), ex.getMessage());
    }

    public record LoginRequest(String email, String password) {}

    public record RegisterRequest(
            String name,
            String email,
            String password,
            String skills,
            Integer experienceYears,
            String preferredRole,
            String location,
            Boolean remotePreferred
    ) {}

    public record LoginResponse(Long id, String email, String name, String role) {}

    public record ErrorResponse(int status, String message) {}

    public static class AuthException extends RuntimeException {
        private final HttpStatus status;

        public AuthException(HttpStatus status, String message) {
            super(message);
            this.status = status;
        }

        public HttpStatus getStatus() {
            return status;
        }
    }
}
