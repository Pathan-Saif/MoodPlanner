package net.engineer.moodPlanner.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import net.engineer.moodPlanner.model.Role;
import net.engineer.moodPlanner.model.User;
import net.engineer.moodPlanner.repository.ScheduleRepository;
import net.engineer.moodPlanner.repository.UserRepository;
import net.engineer.moodPlanner.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwt;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;


//    public void register(String username, String email, String rawPassword, boolean admin,
//                         String mood, String occupation, String ageGroup, String workTime, String gender) {
//
//        if (repo.existsByEmail(email)) {
//            throw new RuntimeException("Email already exists");
//        }
//
//        // Hash the password
//        String passwordHash = encoder.encode(rawPassword);
//
//        // roles string
//        String rolesStr = admin ? "ADMIN" : "USER";
//
//        // SAVE USER IN DB WITH VERIFIED = FALSE
//        User user = new User();
//        user.setUserName(username);
//        user.setEmail(email);
//        user.setPassword(passwordHash);
//        user.setRoles(Set.of(admin ? Role.ADMIN : Role.USER));
//        user.setMood(mood);
//        user.setOccupation(occupation);
//        user.setAgeGroup(ageGroup);
//        user.setWorkTime(workTime);
//        user.setGender(gender);
//        user.setVerified(false);
//        repo.save(user);
//
//        // Create verification token (can include user id/email)
//        String token = tokenService.createVerificationToken(username, email, passwordHash,
//                rolesStr, mood, occupation, ageGroup, workTime, gender);
//
//        // Build verification link
//        String backendVerifyUrl = System.getenv("BACKEND_BASE_URL");
//        if (backendVerifyUrl == null) backendVerifyUrl = "https://moodplanner.onrender.com";
//
//        String verifyLink = backendVerifyUrl + "/auth/verify?token=" + token;
//
//
//
//
//        // Send email. If fails, optionally mark user deleted or keep unverified
//        try {
//            emailService.sendVerificationEmail(email, verifyLink);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            // Optionally: repo.delete(user); // undo save if email fails
//            throw new RuntimeException("Failed to send verification email", ex);
//        }
//
//        // SUCCESS: user saved, email sent
//    }




    public void register(String username, String email, String rawPassword, boolean admin,
                         String mood, String occupation, String ageGroup, String workTime, String gender) {

        if (repo.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User u = new User();
        u.setUserName(username);
        u.setEmail(email);
        u.setPassword(encoder.encode(rawPassword));

        Set<Role> roles = new HashSet<>();
        roles.add(admin ? Role.ADMIN : Role.USER);
        u.setRoles(roles);

        u.setMood(mood);
        u.setOccupation(occupation);
        u.setAgeGroup(ageGroup);
        u.setWorkTime(workTime);
        u.setGender(gender);

        // 🔹 Create verification token
        String token = UUID.randomUUID().toString();
        u.setVerificationToken(token);
        u.setVerified(false);

        try {
            // 🔹 Send verification email
            emailService.sendVerificationEmail(email, token);

            // 🔹 Save user only if email sent successfully
            repo.save(u);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email. Registration aborted.", e);
        }
    }




    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${SPRING_MAIL_FROM}")
    private String fromEmail;

    private final Map<String, String> otpStore = new HashMap<>();

    public void sendPasswordResetOtp(String email) {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        otpStore.put(email, otp);

        try {
            String sendGridUrl = "https://api.sendgrid.com/v3/mail/send";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(sendGridApiKey);

            // Email body as HTML
            String htmlContent = "<p>Your OTP for password reset is: <b>" + otp + "</b></p>";

            Map<String, Object> body = Map.of(
                    "personalizations", new Object[]{
                            Map.of("to", new Object[]{Map.of("email", email)})
                    },
                    "from", Map.of("email", fromEmail),
                    "subject", "Password Reset OTP - MoodPlanner",
                    "content", new Object[]{
                            Map.of("type", "text/html", "value", htmlContent)
                    }
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(sendGridUrl, HttpMethod.POST, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("SendGrid API failed: " + response.getStatusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send OTP via SendGrid", e);
        }
    }


    public boolean verifyOtp(String email, String otp) {
        return otp.equals(otpStore.get(email));
    }

    public void resetPassword(String email, String otp, String newPassword) {
        if (!verifyOtp(email, otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(encoder.encode(newPassword));
        repo.save(user);
        // System.out.println("Encoder type: " + encoder.getClass().getName());
        // System.out.println("Encoded Password: " + encoder.encode(newPassword));
        otpStore.remove(email); // clear used OTP
    }


    public String login(String email, String password) {

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            throw new RuntimeException("Please verify your email before logging in.");
        }

        try {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(email, password);
            authManager.authenticate(token);
            return jwt.generateToken(email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid credentials");
        }
    }


    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email);
    }


    public void deleteAllSchedulesForUser(String userID) {
        scheduleRepository.deleteByUserId(userID);
    }

    public void deleteUser(User user) {
        repo.delete(user);
    }

    public Map<String, String> getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, String> profile = new HashMap<>();
        profile.put("username", user.getUserName());
        profile.put("mood", user.getMood());
        profile.put("occupation", user.getOccupation());
        profile.put("workTime", user.getWorkTime());
        profile.put("gender", user.getGender());
        profile.put("ageGroup", user.getAgeGroup());

        return profile;
    }

    public void updateUserProfile(String userId, Map<String, String> updatedData) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserName(updatedData.get("username"));
        user.setMood(updatedData.get("mood"));
        user.setOccupation(updatedData.get("occupation"));
        user.setWorkTime(updatedData.get("workTime"));
        user.setGender(updatedData.get("gender"));
        user.setAgeGroup(updatedData.get("ageGroup"));

        userRepository.save(user);
    }
}






