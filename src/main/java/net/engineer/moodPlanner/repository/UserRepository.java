package net.engineer.moodPlanner.repository;

import net.engineer.moodPlanner.model.User;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface UserRepository extends MongoRepository<User, String> {
//    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByVerificationToken(String verificationToken);

    //    boolean existsByUserName(String userName);
}
