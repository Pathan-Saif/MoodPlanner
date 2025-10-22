package net.engineer.moodPlanner.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.io.Serializable;
import java.util.List;
import java.util.Set;
@Data
@Document(collection = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String userName;

    private String password;

    private String mood;
    private String occupation;
    private String workTime;
    private String gender;
    private String ageGroup;

    // Roles (e.g. USER, ADMIN)
    private Set<Role> roles;

    // Tasks created by user
    private List<Task> tasks;

    // Preferences, tags, etc.
    private List<String> preferences;

    private boolean verified = false;

    private String verificationToken;

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }


}
