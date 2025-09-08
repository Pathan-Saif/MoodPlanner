package net.engineer.moodPlanner.service;

import net.engineer.moodPlanner.model.Role;
import net.engineer.moodPlanner.model.User;
import net.engineer.moodPlanner.repository.ScheduleRepository;
import net.engineer.moodPlanner.repository.UserRepository;
import net.engineer.moodPlanner.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
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

    public void register(String username, String rawPassword, boolean admin, String mood, String occupation, String ageGroup, String workTime, String gender) {
        if (repo.existsByUserName(username)) {
            throw new RuntimeException("Username already exists");

        }
        User u = new User();
        u.setUserName(username);
        u.setPassword(encoder.encode(rawPassword));
        Set<Role> roles = new HashSet<>();
        if (admin) {
            roles.add(Role.ADMIN);
        }
        else roles.add(Role.USER);
        u.setRoles(roles);
        u.setMood(mood);
        u.setOccupation(occupation);
        u.setAgeGroup(ageGroup);
        u.setWorkTime(workTime);
        u.setGender(gender);
        repo.save(u);
    }

    public String login(String username, String password) {
        try{
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(username, password);
            authManager.authenticate(token);
            return jwt.generateToken(username);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Invalid credentials");
        }
    }

    public Optional<User> findByUsername(String username) {
        return repo.findByUserName(username);
    }

    public void deleteAllSchedulesForUser(String userID) {
        scheduleRepository.deleteByUserId(userID);
    }

    public void deleteUser(User user) {
        repo.delete(user);
    }
}
