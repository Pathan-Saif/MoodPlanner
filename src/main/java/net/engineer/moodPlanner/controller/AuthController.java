package net.engineer.moodPlanner.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.Valid;
import net.engineer.moodPlanner.dto.AuthDtos;
import net.engineer.moodPlanner.model.Role;
import net.engineer.moodPlanner.model.Schedule;
import net.engineer.moodPlanner.model.User;
import net.engineer.moodPlanner.repository.ScheduleRepository;
import net.engineer.moodPlanner.repository.UserRepository;
import net.engineer.moodPlanner.security.JwtUtil;
import net.engineer.moodPlanner.service.AuthService;
import net.engineer.moodPlanner.service.ScheduleService;
import net.engineer.moodPlanner.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserRepository repo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/auth/verify")
    public ResponseEntity<Void> verifyUser(@RequestParam String token) {
        try {
            DecodedJWT decoded = tokenService.verifyToken(token);
            String email = decoded.getClaim("email").asString();

            // If user already exists, redirect to login (or show message)
            if (repo.existsByEmail(email)) {
                String frontendLogin = System.getenv("FRONTEND_BASE_URL");
                if (frontendLogin == null) frontendLogin = "http://localhost:3000/login";
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(frontendLogin))
                        .build();
            }

            // Extract data and create user
            String username = decoded.getClaim("username").asString();
            String passwordHash = decoded.getClaim("passwordHash").asString();
            String rolesStr = decoded.getClaim("roles").asString();
            String mood = decoded.getClaim("mood").asString();
            String occupation = decoded.getClaim("occupation").asString();
            String ageGroup = decoded.getClaim("ageGroup").asString();
            String workTime = decoded.getClaim("workTime").asString();
            String gender = decoded.getClaim("gender").asString();

            User u = new User();
            u.setUserName(username);
            u.setEmail(email);
            u.setPassword(passwordHash); // already hashed
            Set<Role> roles = new HashSet<>();
            roles.add("ADMIN".equals(rolesStr) ? Role.ADMIN : Role.USER);
            u.setRoles(roles);
            u.setMood(mood);
            u.setOccupation(occupation);
            u.setAgeGroup(ageGroup);
            u.setWorkTime(workTime);
            u.setGender(gender);

            u.setVerificationToken(null);
            u.setVerified(true);
            repo.save(u);

            // Redirect user to frontend login page
            String frontendLogin = System.getenv("FRONTEND_BASE_URL");
            if (frontendLogin == null) frontendLogin = "http://localhost:3000/login";
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(frontendLogin))
                    .build();

        } catch (TokenExpiredException ex) {
            // token expired - redirect to frontend page with message or show error
            String frontendErr = System.getenv("FRONTEND_BASE_URL");
            if (frontendErr == null) frontendErr = "http://localhost:3000/login";
            // Optionally append ?tokenExpired or show error page
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(frontendErr + "?token=expired"))
                    .build();
        } catch (Exception ex) {
            // invalid token or other error
            String frontendErr = System.getenv("FRONTEND_BASE_URL");
            if (frontendErr == null) frontendErr = "http://localhost:3000/login";
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(frontendErr + "?token=invalid"))
                    .build();
        }
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthDtos.RegisterRequest req) {
        authService.register(
                req.getUsername(),
                req.getEmail(),
                req.getPassword(),
                req.isAdmin(),
                req.getMood(),
                req.getOccupation(),
                req.getAgeGroup(),
                req.getWorkTime(),
                req.getGender()

        );

        return ResponseEntity.ok("Registered");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable String userId) {
        Map<String, String> profile = authService.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable String userId,
            @RequestBody Map<String, String> updatedData) {
        authService.updateUserProfile(userId, updatedData);
        return ResponseEntity.ok("Profile updated successfully");
    }


//    @GetMapping("/verify")
//    public ResponseEntity<String> verifyUser(@RequestParam String token) {
//        Optional<User> userOpt = repo.findByVerificationToken(token);
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            user.setVerified(true);
//            user.setVerificationToken(null);
//            repo.save(user);
//            return ResponseEntity.ok("Email verified successfully!");
//        }
//        return ResponseEntity.badRequest().body("Invalid or expired token!");
//    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        authService.sendPasswordResetOtp(email);
        return ResponseEntity.ok("OTP sent to registered email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");
        String newPassword = body.get("newPassword");

        authService.resetPassword(email, otp, newPassword);
        return ResponseEntity.ok("Password reset successful");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.TokenResponse> login(@RequestBody AuthDtos.LoginRequest req) {
        String token = authService.login(req.getEmail(), req.getPassword()); // ab login email se hoga
        return ResponseEntity.ok(new AuthDtos.TokenResponse(token));
    }

    @GetMapping("/me")
    public ResponseEntity<String> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth.getName());
    }




    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMyAccount(@RequestHeader("Authorization") String token) {

        String email = jwtUtil.extractUsername(token.substring(7)); // ab token se email nikalega

        User user = authService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        authService.deleteAllSchedulesForUser(user.getId());
        authService.deleteUser(user);

        return ResponseEntity.ok("Your account and all related data have been permanently deleted.");
    }
}

















