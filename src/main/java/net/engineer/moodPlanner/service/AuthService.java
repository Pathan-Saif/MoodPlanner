package net.engineer.moodPlanner.service;

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
        if (admin) {
            roles.add(Role.ADMIN);
        } else {
            roles.add(Role.USER);
        }
        u.setRoles(roles);
        u.setMood(mood);
        u.setOccupation(occupation);
        u.setAgeGroup(ageGroup);
        u.setWorkTime(workTime);
        u.setGender(gender);

        String token = UUID.randomUUID().toString();
        u.setVerificationToken(token);
        u.setVerified(false);

        repo.save(u);
        emailService.sendVerificationEmail(email, token);

    }


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






