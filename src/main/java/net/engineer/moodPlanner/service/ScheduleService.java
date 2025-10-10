package net.engineer.moodPlanner.service;

import lombok.RequiredArgsConstructor;
import net.engineer.moodPlanner.model.Schedule;
import net.engineer.moodPlanner.model.Task;
import net.engineer.moodPlanner.model.User;
import net.engineer.moodPlanner.repository.ScheduleRepository;
import net.engineer.moodPlanner.utils.MoodRulesEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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


    public Schedule updateTask(String userId, String oldTitle, Task updatedTask) {
        // user ka schedule nikaalna
        Optional<Schedule> scheduleOpt = scheduleRepository.findByUserId(userId)
                .stream().findFirst();

        if (scheduleOpt.isEmpty()) {
            throw new RuntimeException("Schedule not found for user: " + userId);
        }

        Schedule schedule = scheduleOpt.get();
        List<Task> tasks = schedule.getTasks();

        // specific task find karna (title se identify kar rahe hain)
        boolean found = false;
        for (Task t : tasks) {
            if (t.getTitle().equalsIgnoreCase(oldTitle)) {
                t.setTitle(updatedTask.getTitle());
                t.setTimeOfDay(updatedTask.getTimeOfDay());
                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("Task not found with title: " + oldTitle);
        }

        // save updated schedule
        schedule.setTasks(tasks);
        return scheduleRepository.save(schedule);
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

    public boolean deleteTaskForUser(String userId, String taskTitle) {
        // user ke schedules fetch karo
        Optional<Schedule> scheduleOpt = scheduleRepository.findByUserId(userId)
                .stream().findFirst(); // ya multiple schedules handle karna ho to adjust kar sakte ho

        if (scheduleOpt.isPresent()) {
            Schedule schedule = scheduleOpt.get();

            boolean removed = schedule.getTasks().removeIf(t -> t.getTitle().equals(taskTitle));

            if (removed) {
                scheduleRepository.save(schedule);
                return true;
            }
        }

        return false;
    }


    public Schedule addTaskForUser(String userId, Task newTask) {
        Optional<Schedule> scheduleOpt = scheduleRepository.findByUserId(userId)
                .stream().findFirst();

        Schedule schedule;
        if (scheduleOpt.isPresent()) {
            schedule = scheduleOpt.get();
            // Null check for tasks list
            if (schedule.getTasks() == null) {
                schedule.setTasks(new ArrayList<>());
            }
            schedule.getTasks().add(newTask);
        } else {
            // If no schedule exists, create a new one
            schedule = new Schedule();
            schedule.setUserId(userId);
            schedule.setTasks(new ArrayList<>());
            schedule.getTasks().add(newTask);
        }

        return scheduleRepository.save(schedule);
    }




}
