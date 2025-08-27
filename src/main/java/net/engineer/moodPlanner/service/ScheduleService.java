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
        schedule.setUsername(user.getUserName());
        schedule.setMood(user.getMood());
        schedule.setTasks(tasks);

        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getSchedulesForUser(String username) {
        return scheduleRepository.findByUsername(username);
    }

    public Schedule getScheduleByIdForUser(String id, String username) {
        return scheduleRepository.findById(id)
                .filter(s -> s.getUsername().equals(username))
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    public Schedule updateScheduleByIdForUser(String id, User user) {
        Schedule existing = getScheduleByIdForUser(id, user.getUserName());
        List<Task> tasks = MoodRulesEngine.generateSchedule(user);
        existing.setMood(user.getMood());
        existing.setTasks(tasks);
        return scheduleRepository.save(existing);
    }

    public boolean deleteScheduleByIdForUser(String id, String username) {
        Optional<Schedule> opt = scheduleRepository.findById(id);
        if (opt.isPresent() && opt.get().getUsername().equals(username)) {
            scheduleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteAllSchedulesForUser(String username) {
        scheduleRepository.deleteByUsername(username);
    }

}
