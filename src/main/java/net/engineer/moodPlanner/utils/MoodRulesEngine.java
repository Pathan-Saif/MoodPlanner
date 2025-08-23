package net.engineer.moodPlanner.utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.engineer.moodPlanner.model.Task;
import net.engineer.moodPlanner.model.User;

import java.util.ArrayList;
import java.util.List;

public class MoodRulesEngine {

    public static List<Task> generateSchedule(User user) {
        String mood = user.getMood();
        if(mood == null) mood = "neutral";

        List<String> preferences = user.getPreferences();
        List<Task> tasks = new ArrayList<>();

        switch (mood.toLowerCase()) {
            case "happy":
                tasks.add(new Task("Go for a walk", "Morning"));
                tasks.add(new Task("Listen to your favorite music", "Afternoon"));
                tasks.add(new Task("Hangout with friends", "Evening"));
                break;

            case "sad":
                tasks.add(new Task("Try journaling", "Morning"));
                tasks.add(new Task("Watch a comforting movie", "Evening"));
                tasks.add(new Task("Call a loved one", "Night"));
                break;

            case "anxious":
                tasks.add(new Task("Do breathing exercises", "Morning"));
                tasks.add(new Task("Read a book", "Afternoon"));
                tasks.add(new Task("Meditation", "Evening"));
                break;

            case "focused":
                tasks.add(new Task("Deep work session", "Morning"));
                tasks.add(new Task("Study/Project work", "Afternoon"));
                tasks.add(new Task("Review goals", "Night"));
                break;
            default:
                tasks.add(new Task("Take a break", "Anytime"));
                break;
        }

        // Add custom preferences if any
        if (preferences != null && !preferences.isEmpty()) {
            for (String pref : preferences) {
                tasks.add(new Task(pref, "Custom"));
            }
        }

        return tasks;
    }
}
