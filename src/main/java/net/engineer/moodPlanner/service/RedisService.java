package net.engineer.moodPlanner.service;

import net.engineer.moodPlanner.model.Schedule;
import net.engineer.moodPlanner.model.User;
import net.engineer.moodPlanner.repository.ScheduleRepository;
import net.engineer.moodPlanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIX = "schedule:";  // key prefix in Redis

    public Schedule getScheduleByMood(String mood) {
        String key = PREFIX + mood.toLowerCase();

        // 1. Check cache
        Schedule cachedSchedule = (Schedule) redisTemplate.opsForValue().get(key);
        if (cachedSchedule != null) {
            System.out.println("Fetching from cache...");
            return cachedSchedule;
        }

        // 2. If not in cache → get from DB
        Schedule schedule = scheduleRepository.findByMood(mood)
                .orElseThrow(() -> new RuntimeException("No schedule found for mood: " + mood));

        // 3. Save in cache for next time
        redisTemplate.opsForValue().set(key, schedule, 10, TimeUnit.MINUTES);
        // 10 min expiry (customize)

        System.out.println("Fetching from DB and caching...");
        return schedule;
    }

}
