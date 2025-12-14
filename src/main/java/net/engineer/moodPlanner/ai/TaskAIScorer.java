package net.engineer.moodPlanner.ai;

import net.engineer.moodPlanner.model.Task;
import net.engineer.moodPlanner.model.User;

import java.util.*;
import java.util.stream.Collectors;

public class TaskAIScorer {

    private static final Random RANDOM = new Random();

    public static List<Task> rankTasks(User user, List<Task> tasks, int limit) {

        if (user == null || tasks == null || tasks.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Task, Integer> scoreMap = new HashMap<>();

        for (Task task : tasks) {

            int score = 0;
            String title = task.getTitle() != null ? task.getTitle().toLowerCase() : "";

            /* ======================
               1. MOOD SCORING
               ====================== */
            if (user.getMood() != null) {
                switch (user.getMood().toLowerCase()) {

                    case "happy":
                        if (containsAny(title,
                                "affirmation", "healthy breakfast", "stretch",
                                "talk", "evening walk", "music",
                                "gratitude", "hobby", "meditation", "read")) {
                            score += 40;
                        }
                        break;

                    case "sad":
                        if (containsAny(title,
                                "gentle stretching", "comfort breakfast",
                                "call", "short walk", "uplifting",
                                "journal", "meditation", "cook",
                                "self-care", "positive")) {
                            score += 40;
                        }
                        break;

                    case "anxious":
                        if (containsAny(title,
                                "deep breathing", "exercise", "grounding",
                                "mindful", "walk outside", "meditation",
                                "limit screen", "worries", "calming", "sleep early")) {
                            score += 45;
                        }
                        break;

                    case "focused":
                        if (containsAny(title,
                                "planning", "deep work", "project review",
                                "wrap-up", "plan tomorrow",
                                "technical", "reflect")) {
                            score += 45;
                        }
                        break;

                    case "tired":
                        if (containsAny(title,
                                "hydrate", "nap", "light breakfast",
                                "gentle yoga", "relaxing tea",
                                "calming music", "no screens",
                                "sleep early")) {
                            score += 45;
                        }
                        break;

                    default:
                        score += 5;
                }
            }

            /* ======================
               2. OCCUPATION SCORING
               ====================== */
            if (user.getOccupation() != null) {
                switch (user.getOccupation().toLowerCase()) {

                    case "engineer":
                        if (containsAny(title,
                                "technical", "problem solving",
                                "debug", "project planning", "tech news")) {
                            score += 35;
                        }
                        break;

                    case "businessman":
                        if (containsAny(title,
                                "market news", "networking",
                                "business planning", "investor",
                                "finance")) {
                            score += 35;
                        }
                        break;

                    case "lawyer":
                        if (containsAny(title,
                                "case review", "court",
                                "client meeting", "legal",
                                "document")) {
                            score += 35;
                        }
                        break;

                    case "teacher":
                        if (containsAny(title,
                                "lesson prep", "class",
                                "student", "homework",
                                "material research")) {
                            score += 35;
                        }
                        break;

                    case "singer":
                        if (containsAny(title,
                                "vocal", "music practice",
                                "songwriting", "studio",
                                "performance")) {
                            score += 40;
                        }
                        break;

                    default:
                        score += 5;
                }
            }

            /* ======================
               3. AGE GROUP SCORING
               ====================== */
            if (user.getAgeGroup() != null) {
                String age = user.getAgeGroup().toLowerCase();

                if (age.contains("10") || age.contains("17")) {
                    if (containsAny(title, "play", "homework", "games", "school", "family")) {
                        score += 30;
                    }
                } else if (age.contains("18") || age.contains("25")) {
                    if (containsAny(title, "skill", "internship", "job", "networking", "workout")) {
                        score += 35;
                    }
                } else if (age.contains("26") || age.contains("40")) {
                    if (containsAny(title, "career", "workout", "project", "walk", "plan")) {
                        score += 30;
                    }
                } else if (age.contains("41") || age.contains("60")) {
                    if (containsAny(title, "exercise", "health", "gardening", "family")) {
                        score += 30;
                    }
                } else if (age.contains("60")) {
                    if (containsAny(title, "prayer", "walk", "light exercise", "sleep")) {
                        score += 35;
                    }
                }
            }

            /* ======================
               4. WORK TIME SCORING
               ====================== */
            if (user.getWorkTime() != null) {
                switch (user.getWorkTime().toLowerCase()) {

                    case "early_shift":
                        if (containsAny(title, "early", "morning", "focus", "wind-down")) {
                            score += 25;
                        }
                        break;

                    case "regular_shift":
                        if (containsAny(title, "balanced", "wrap-up", "refresh", "review")) {
                            score += 25;
                        }
                        break;

                    case "late_shift":
                        if (containsAny(title, "slow morning", "cool-down", "late", "unwind")) {
                            score += 25;
                        }
                        break;
                }
            }

            /* ======================
               5. GENDER SCORING
               ====================== */
            if (user.getGender() != null) {
                switch (user.getGender().toLowerCase()) {

                    case "male":
                        if (containsAny(title, "strength", "sports", "run")) {
                            score += 15;
                        }
                        break;

                    case "female":
                        if (containsAny(title, "dance", "zumba", "skin care", "jog")) {
                            score += 15;
                        }
                        break;

                    case "other":
                        score += 10;
                        break;
                }
            }

            /* ======================
               6. USER PREFERENCES
               ====================== */
            if (user.getPreferences() != null) {
                for (String pref : user.getPreferences()) {
                    if (pref != null && title.contains(pref.toLowerCase())) {
                        score += 20;
                    }
                }
            }

            /* ======================
               7. AI RANDOMNESS
               ====================== */
            score += RANDOM.nextInt(5);

            scoreMap.put(task, score);
        }

        return scoreMap.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static boolean containsAny(String source, String... keywords) {
        for (String k : keywords) {
            if (source.contains(k)) {
                return true;
            }
        }
        return false;
    }
}
