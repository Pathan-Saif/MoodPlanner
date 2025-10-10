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

    @Id
    private String id;
    private String userId;
    private  String userName;
    private String mood;
    private String occupation;
    private String workTime;
    private String gender;
    private String ageGroup;
    private List<Task> tasks;

}

