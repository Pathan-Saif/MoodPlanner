package net.engineer.moodPlanner.repository;

import net.engineer.moodPlanner.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface ScheduleRepository  extends MongoRepository<Schedule, String> {
    List<Schedule> findByUserId(String userId);
    Optional<Schedule> findByMood(String mood);

    void deleteByUserId(String userId);

}
