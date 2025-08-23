package net.engineer.moodPlanner.repository;

import net.engineer.moodPlanner.model.User;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUserName(String userName);

    boolean existsByUserName(String userName);
}
