package net.engineer.moodPlanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
//    @Id
//    private String id;
//    private String name;
//    private String description;
//    private String time;
//
//    public Task(String name, String time) {
//        this.name = name;
//        this.time = time;
//    }

    private String title;
    private String timeOfDay;
}

