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
    @Autowired
    private ScheduleRepository scheduleRepository;

    public Schedule createSchedule(User user) {
        List<Task> tasks = MoodRulesEngine.generateSchedule(user);

        Schedule schedule = new Schedule();
        schedule.setUserId(user.getId());
        schedule.setMood(user.getMood());
        schedule.setOccupation(user.getOccupation());
        schedule.setGender(user.getGender());
        schedule.setAgeGroup(user.getAgeGroup());
        schedule.setWorkTime(user.getWorkTime());
        schedule.setTasks(tasks);

        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getSchedulesForUser(String userId) {
        return scheduleRepository.findByUserId(userId);
    }

    public Schedule getScheduleByIdForUser(String id, String userId) {
        return scheduleRepository.findById(id)
                .filter(s -> s.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    public Schedule updateScheduleByIdForUser(String id, User user) {
        Schedule existing = getScheduleByIdForUser(id, user.getId());
        List<Task> tasks = MoodRulesEngine.generateSchedule(user);
        existing.setMood(user.getMood());
        existing.setTasks(tasks);
        existing.setOccupation(user.getOccupation());
        existing.setGender(user.getGender());
        existing.setAgeGroup(user.getAgeGroup());
        existing.setWorkTime(user.getWorkTime());
        return scheduleRepository.save(existing);
    }

    public boolean deleteScheduleByIdForUser(String id, String userId) {
        Optional<Schedule> opt = scheduleRepository.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(userId)) {
            scheduleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteAllSchedulesForUser(String userId) {
        scheduleRepository.deleteByUserId(userId);
    }

}
