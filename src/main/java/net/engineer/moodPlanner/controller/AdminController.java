package net.engineer.moodPlanner.controller;

import net.engineer.moodPlanner.model.Schedule;
import net.engineer.moodPlanner.model.User;
import net.engineer.moodPlanner.repository.ScheduleRepository;
import net.engineer.moodPlanner.repository.UserRepository;
import net.engineer.moodPlanner.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private AuthService authService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    if (updatedUser.getUserName() != null) {
                        user.setUserName(updatedUser.getUserName());
                    }
                    if (updatedUser.getPassword() != null) {
                        user.setPassword(updatedUser.getPassword());
                    }
                    if (updatedUser.getRoles() != null) {
                        user.setRoles(updatedUser.getRoles());
                    }
                    if (updatedUser.getMood() != null) {
                        user.setMood(updatedUser.getMood());
                    }
                    if (updatedUser.getPreferences() != null) {
                        user.setPreferences(updatedUser.getPreferences());
                    }
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<String> deleteUserAccountByAdmin(@PathVariable String username) {

        User user = authService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        authService.deleteAllSchedulesForUser(username);

        authService.deleteUser(user);

        return ResponseEntity.ok("User '" + username + "' and all related data have been permanently deleted by admin.");
    }

    @GetMapping("/schedules")
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @GetMapping("/schedules/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable String id) {
        return scheduleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/schedules")
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @PutMapping("/schedules/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable String id, @RequestBody Schedule scheduleDetails) {
        return scheduleRepository.findById(id).map(schedule -> {
            schedule.setUsername(scheduleDetails.getUsername());
            schedule.setMood(scheduleDetails.getMood());
            schedule.setTasks(scheduleDetails.getTasks());
            return ResponseEntity.ok(scheduleRepository.save(schedule));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String id) {
        return scheduleRepository.findById(id).map(schedule -> {
            scheduleRepository.delete(schedule);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
