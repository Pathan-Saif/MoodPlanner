package net.engineer.moodPlanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private String title;
    private String timeOfDay;
    private List<String> steps;
}

