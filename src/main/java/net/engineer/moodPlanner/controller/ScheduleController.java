package net.engineer.moodPlanner.controller;

import lombok.RequiredArgsConstructor;
import net.engineer.moodPlanner.model.Schedule;
import net.engineer.moodPlanner.model.Task;
import net.engineer.moodPlanner.model.User;
import net.engineer.moodPlanner.repository.ScheduleRepository;
import net.engineer.moodPlanner.repository.UserRepository;
import net.engineer.moodPlanner.security.JwtUtil;
import net.engineer.moodPlanner.service.ScheduleService;
import net.engineer.moodPlanner.utils.MoodRulesEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @PostMapping
    public ResponseEntity<Schedule> generateSchedule(
            @RequestHeader("Authorization") String token) {

        String username = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Schedule saved = scheduleService.createSchedule(user);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Schedule>> getSchedules(
            @RequestHeader("Authorization") String token) {

        String username = jwtUtil.extractUsername(token.substring(7));
        return ResponseEntity.ok(scheduleService.getSchedulesForUser(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getById(@RequestHeader("Authorization") String auth,
                                            @PathVariable String id) {
        String username = jwtUtil.extractUsername(auth.substring(7));
        Schedule s = scheduleService.getScheduleByIdForUser(id, username);
        return ResponseEntity.ok(s);
    }


    // 🔹 Step 1: User apna mood update kare
    @PutMapping("/{userId}")
    public ResponseEntity<List<Schedule>> updateMoodOrUsername(
            @RequestHeader("Authorization") String token,
            @PathVariable String userId,
            @RequestBody Map<String, String> body) {

        String originalUsername = jwtUtil.extractUsername(token.substring(7));

        User user = userRepository.findByUserName(originalUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getId().equals(userId)) {
            throw new RuntimeException("Unauthorized update attempt!");
        }

        String newUsername = body.get("username");
        String newMood = body.get("mood");
        String newOccupation = body.get("occupation");
        String newWorkTime = body.get("workTime");
        String newGender = body.get("gender");
        String newAgeGroup = body.get("ageGroup");

        if (newUsername != null && !newUsername.isEmpty()) {
            user.setUserName(newUsername);
        }

        if (newMood != null && !newMood.isEmpty()) {
            user.setMood(newMood.toLowerCase());
        }

        if (newOccupation != null && !newOccupation.isEmpty()) {
            user.setOccupation(newOccupation.toLowerCase());
        }

        if (newWorkTime != null && !newWorkTime.isEmpty()) {
            user.setWorkTime(newWorkTime.toLowerCase());
        }

        if (newGender != null && !newGender.isEmpty()) {
            user.setGender(newGender.toLowerCase());
        }

        if (newAgeGroup != null && !newAgeGroup.isEmpty()) {
            user.setAgeGroup(newAgeGroup.toLowerCase());
        }

        userRepository.save(user);

        List<Schedule> schedules = scheduleRepository.findByUserId(userId);

        for (Schedule schedule : schedules) {
            if (newMood != null && !newMood.isEmpty()) {
                schedule.setMood(newMood.toLowerCase());
                schedule.setTasks(MoodRulesEngine.generateSchedule(user));
            }
            if (newOccupation != null && !newOccupation.isEmpty()) {
                schedule.setOccupation(newOccupation.toLowerCase());
            }
            if (newWorkTime != null && !newWorkTime.isEmpty()) {
                schedule.setWorkTime(newWorkTime.toLowerCase());
            }
            if (newGender != null && !newGender.isEmpty()) {
                schedule.setGender(newGender.toLowerCase());
            }
            if (newAgeGroup != null && !newAgeGroup.isEmpty()) {
                schedule.setAgeGroup(newAgeGroup.toLowerCase());
            }
        }

        scheduleRepository.saveAll(schedules);

        return ResponseEntity.ok(schedules);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOne(@RequestHeader("Authorization") String auth,
                                       @PathVariable String id) {
        String username = jwtUtil.extractUsername(auth.substring(7));
        boolean removed = scheduleService.deleteScheduleByIdForUser(id, username);
        if (removed) return ResponseEntity.ok("Deleted");
        return ResponseEntity.status(404).body("Not found or not yours");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestHeader("Authorization") String auth) {
        String username = jwtUtil.extractUsername(auth.substring(7));
        scheduleService.deleteAllSchedulesForUser(username);
        return ResponseEntity.ok("All schedules removed for " + username);
    }

}
