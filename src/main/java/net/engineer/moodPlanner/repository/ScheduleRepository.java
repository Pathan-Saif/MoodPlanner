package net.engineer.moodPlanner.repository;

import net.engineer.moodPlanner.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ScheduleRepository  extends MongoRepository<Schedule, String> {
    List<Schedule> findByUsername(String username);

//    void deleteByUserId(String userId);
//    void findByUserName(String userName);
//    void deleteById(String id);
}
