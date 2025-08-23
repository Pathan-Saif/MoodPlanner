package net.engineer.moodPlanner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "schedules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
//    @Id
//    private String id;
//    private String userId;
//    private String mood;
//    private List<Task> tasks;

//    public void setMood(String mood) {
//        this.mood = mood;
//    }


    @Id
    private String id;
    private String username;
    private String mood;
    private List<Task> tasks;

}

