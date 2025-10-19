package net.engineer.moodPlanner.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import net.engineer.moodPlanner.model.Role;
import net.engineer.moodPlanner.model.User;
import net.engineer.moodPlanner.repository.ScheduleRepository;
import net.engineer.moodPlanner.repository.UserRepository;
import net.engineer.moodPlanner.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
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


    public void register(String username, String email, String rawPassword, boolean admin,
                         String mood, String occupation, String ageGroup, String workTime, String gender) {

        if (repo.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        // Hash the password now (store hash in token, NOT raw password)
        String passwordHash = encoder.encode(rawPassword);

        // roles string
        String rolesStr = admin ? "ADMIN" : "USER";

        // Create a signed token containing the user data (expires automatically)
        String token = tokenService.createVerificationToken(
                username, email, passwordHash, rolesStr, mood, occupation, ageGroup, workTime, gender
        );

        // Build verification link (use your backend host)
        String backendVerifyUrl = System.getenv("BACKEND_BASE_URL"); // e.g., https://api.yoursite.com
        if (backendVerifyUrl == null) backendVerifyUrl = "http://localhost:8080";
        String verifyLink = backendVerifyUrl + "/auth/verify?token=" + token;

        // Send email. If email sending fails, registration aborted and user not saved.
        try {
            emailService.sendVerificationEmail(email, verifyLink);
        } catch (Exception ex) {
            // log and rethrow (do not save user)
            ex.printStackTrace();
            throw new RuntimeException("Failed to send verification email, registration aborted", ex);
        }

        // SUCCESS: email sent and user not saved yet (we wait for that /verify call)
    }



//    public void register(String username, String email, String rawPassword, boolean admin,
//                         String mood, String occupation, String ageGroup, String workTime, String gender) {
//
//        if (repo.existsByEmail(email)) {
//            throw new RuntimeException("Email already exists");
//        }
//
//        User u = new User();
//        u.setUserName(username);
//        u.setEmail(email);
//        u.setPassword(encoder.encode(rawPassword));
//        Set<Role> roles = new HashSet<>();
//        roles.add(admin ? Role.ADMIN : Role.USER);
//        u.setRoles(roles);
//        u.setMood(mood);
//        u.setOccupation(occupation);
//        u.setAgeGroup(ageGroup);
//        u.setWorkTime(workTime);
//        u.setGender(gender);
//
//        String token = UUID.randomUUID().toString();
//        u.setVerificationToken(token);
//        u.setVerified(false);
//
//        // Send email first, save only if sending succeeds
//        try {
//            emailService.sendVerificationEmail(email, token); // throws MessagingException on failure
//            repo.save(u);  // only save after successful send
//        } catch (MessagingException e) {
//            // log and rethrow or return a meaningful response to client
//            // do not save the user
//            throw new RuntimeException("Failed to send verification email. Registration aborted.", e);
//        }
//    }



    private final Map<String, String> otpStore = new HashMap<>();
    private final JavaMailSender mailSender;

    public void sendPasswordResetOtp(String email) {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        otpStore.put(email, otp);

        // send email
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Password Reset OTP");
        msg.setText("Your OTP for password reset is: " + otp);
        mailSender.send(msg);
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






