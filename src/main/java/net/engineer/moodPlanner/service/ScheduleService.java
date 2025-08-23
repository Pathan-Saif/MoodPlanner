package net.engineer.moodPlanner.service;

import net.engineer.moodPlanner.model.Schedule;
import net.engineer.moodPlanner.model.Task;
import net.engineer.moodPlanner.model.User;
import net.engineer.moodPlanner.repository.ScheduleRepository;
import net.engineer.moodPlanner.utils.MoodRulesEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

//    @Autowired
//    private ScheduleRepository scheduleRepository;
//
//    public Schedule createSchedule(User user) {
//        List<Task> tasks = MoodRulesEngine.generateSchedule(user);
//        Schedule schedule = new Schedule();
//        schedule.setUserId(user.getId());
//        schedule.setMood(user.getMood());
//        schedule.setTasks(tasks);
//        return scheduleRepository.save(schedule);
//    }
//
//    public List<Schedule> getAllSchedules() {
//        return scheduleRepository.findAll();
//    }
//
//    public Schedule getScheduleById(String id) {
//        Optional<Schedule> scheduleOptional = scheduleRepository.findById(id);
//        return scheduleOptional.orElse(null);
//    }
//
//
//    public List<Schedule> getSchedulesByUserId(String userId) {
//        List<Schedule> schedules = scheduleRepository.findByUserId(userId);
//        if (schedules.isEmpty()) {
//            throw new RuntimeException("No schedules found for userId: " + userId);
//        }
//        return schedules;
//    }


    @Autowired
    private ScheduleRepository scheduleRepository;

    public Schedule createSchedule(User user) {
        List<Task> tasks = MoodRulesEngine.generateSchedule(user);

        Schedule schedule = new Schedule();
        schedule.setUsername(user.getUserName());
        schedule.setMood(user.getMood());
        schedule.setTasks(tasks);

        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getSchedulesForUser(String username) {
        return scheduleRepository.findByUsername(username);
    }



}
