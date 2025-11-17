//package net.engineer.moodPlanner.controller;
//
//import net.engineer.moodPlanner.model.Schedule;
//import net.engineer.moodPlanner.model.User;
//import net.engineer.moodPlanner.security.JwtUtil;
//import net.engineer.moodPlanner.service.RedisService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/schedules")
//public class RedisController {
//
//    @Autowired
//    private RedisService redisService;
//
//    @GetMapping("/{mood}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<Schedule> getSchedule(@PathVariable String mood, Authentication authentication) {
//
//        String username = authentication.getName();
//        Schedule schedule = redisService.getScheduleByMood(mood);
//        return ResponseEntity.ok(schedule);
//    }
//}
