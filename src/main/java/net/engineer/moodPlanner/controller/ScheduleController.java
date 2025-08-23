package net.engineer.moodPlanner.controller;

import lombok.RequiredArgsConstructor;
import net.engineer.moodPlanner.model.Schedule;
import net.engineer.moodPlanner.model.User;
import net.engineer.moodPlanner.repository.ScheduleRepository;
import net.engineer.moodPlanner.repository.UserRepository;
import net.engineer.moodPlanner.security.JwtUtil;
import net.engineer.moodPlanner.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

//    @Autowired
//    private ScheduleService scheduleService;
//
//    @Autowired
//    private ScheduleRepository scheduleRepository;
//
//    @PostMapping
//    public ResponseEntity<Schedule> createSchedule(@RequestBody User user) {
//        Schedule schedule = scheduleService.createSchedule(user);
//        return ResponseEntity.ok(schedule);
//    }
//
//
//    @GetMapping("/all")
//    public ResponseEntity<List<Schedule>> getAllSchedules(){
//        List<Schedule> schedules = scheduleService.getAllSchedules();
//        return ResponseEntity.ok(schedules);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Schedule> getScheduleById(@PathVariable String id) {
//        Schedule schedule = scheduleService.getScheduleById(id);
//        if (schedule == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(schedule);
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Schedule>> getSchedules(@PathVariable String userId) {
//        List<Schedule> schedules = scheduleService.getSchedulesByUserId(userId);
//        return ResponseEntity.ok(schedules);
//    }
//
//    @DeleteMapping("/user/{userId}")
//    public ResponseEntity<?> deleteUserById(@PathVariable String userId){
//        scheduleRepository.deleteByUserId(userId);
//        return ResponseEntity.ok("All schedules for userId " + userId + " deleted successfully");
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteScheduleById(@PathVariable String id){
//        if(scheduleRepository.existsById(id)){
//            scheduleRepository.deleteById(id);
//            return ResponseEntity.ok("Schedule deleted successfully");
//        }
//        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Schedule not found");
//    }



    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

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


    // 🔹 Step 1: User apna mood update kare
    @PutMapping("/mood")
    public ResponseEntity<String> updateMood(
            @RequestHeader("Authorization") String token,
            @RequestParam String mood) {

        String username = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setMood(mood.toLowerCase());
        userRepository.save(user);

        return ResponseEntity.ok("Mood updated to: " + mood);
    }


}
