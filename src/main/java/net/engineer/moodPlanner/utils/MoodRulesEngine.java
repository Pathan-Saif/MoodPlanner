package net.engineer.moodPlanner.utils;

import net.engineer.moodPlanner.model.Task;
import net.engineer.moodPlanner.model.User;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoodRulesEngine {

    private static final Pattern timePattern =
            Pattern.compile("(1[0-2]|0?[1-9]):([0-5][0-9])\\s*(AM|PM|am|pm)");

    private static final Random random = new Random();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    public static List<Task> generateSchedule(User user) {
        List<Task> tasks = new ArrayList<>();

        // 1. Mood pool (10 tasks → pick 5)
        Map<String, List<Task>> moodTaskPool = new HashMap<>();
        moodTaskPool.put("happy", Arrays.asList(
                new Task("Positive affirmations", "7:00 AM"),
                new Task("Healthy breakfast", "7:30 AM"),
                new Task("Stretch breaks", "10:30 AM"),
                new Task("Talk to a friend", "1:00 PM"),
                new Task("Evening walk", "6:30 PM"),
                new Task("Listen to music", "5:00 PM"),
                new Task("Journal gratitude", "9:00 PM"),
                new Task("Do a hobby", "8:00 PM"),
                new Task("Quick meditation", "12:00 PM"),
                new Task("Read a book", "9:30 PM")
        ));
        moodTaskPool.put("sad", Arrays.asList(
                new Task("Gentle stretching", "7:00 AM"),
                new Task("Comfort breakfast", "8:00 AM"),
                new Task("Call a loved one", "12:30 PM"),
                new Task("Short walk", "5:00 PM"),
                new Task("Watch uplifting movie", "7:30 PM"),
                new Task("Write in journal", "9:00 PM"),
                new Task("Meditation", "10:00 AM"),
                new Task("Cook favorite meal", "1:30 PM"),
                new Task("Evening self-care", "8:30 PM"),
                new Task("Read something positive", "9:30 PM")
        ));
        moodTaskPool.put("anxious", Arrays.asList(
                new Task("Deep breathing", "7:15 AM"),
                new Task("Morning yoga", "8:00 AM"),
                new Task("Grounding exercise", "11:00 AM"),
                new Task("Mindful lunch", "1:00 PM"),
                new Task("Walk outside", "4:00 PM"),
                new Task("Meditation", "6:00 PM"),
                new Task("Limit screen time", "8:00 PM"),
                new Task("Write worries down", "9:00 PM"),
                new Task("Read calming book", "9:30 PM"),
                new Task("Sleep early", "10:00 PM")
        ));
        moodTaskPool.put("focused", Arrays.asList(
                new Task("Morning planning", "7:00 AM"),
                new Task("Healthy breakfast", "7:30 AM"),
                new Task("Deep work session", "9:00 AM"),
                new Task("Quick stretch", "11:00 AM"),
                new Task("Focused lunch", "1:00 PM"),
                new Task("Project review", "3:00 PM"),
                new Task("Evening wrap-up", "6:00 PM"),
                new Task("Plan tomorrow", "8:00 PM"),
                new Task("Read technical book", "9:00 PM"),
                new Task("Reflect on goals", "9:30 PM")
        ));
        moodTaskPool.put("tired", Arrays.asList(
                new Task("Hydrate", "7:00 AM"),
                new Task("Light breakfast", "7:30 AM"),
                new Task("Nap", "2:00 PM"),
                new Task("Short walk", "4:00 PM"),
                new Task("Gentle yoga", "6:00 PM"),
                new Task("Relaxing tea", "7:00 PM"),
                new Task("Listen calming music", "8:00 PM"),
                new Task("No screens", "9:00 PM"),
                new Task("Gratitude journal", "9:30 PM"),
                new Task("Sleep early", "10:00 PM")
        ));
        moodTaskPool.put("default", Arrays.asList(
                new Task("Morning hydration", "7:00 AM"),
                new Task("Balanced breakfast", "7:30 AM"),
                new Task("Work session", "9:00 AM"),
                new Task("Mid-day stretch", "12:00 PM"),
                new Task("Nutritious lunch", "1:00 PM"),
                new Task("Afternoon work", "3:00 PM"),
                new Task("Evening relaxation", "6:00 PM"),
                new Task("Family time", "8:00 PM"),
                new Task("Read book", "9:00 PM"),
                new Task("Prepare for tomorrow", "9:30 PM")
        ));

        String moodKey = user.getMood() != null ? user.getMood().toLowerCase() : "default";
        List<Task> moodTasks = moodTaskPool.getOrDefault(moodKey, moodTaskPool.get("default"));
        tasks.addAll(pickRandom(moodTasks, 5));

        // 2. Occupation pool (5 → 2)
        Map<String, List<Task>> occupationTaskPool = new HashMap<>();
        occupationTaskPool.put("engineer", Arrays.asList(
                new Task("Review technical notes", "8:00 PM"),
                new Task("Problem solving", "4:00 PM"),
                new Task("Tech news read", "9:00 AM"),
                new Task("Debug practice", "11:00 AM"),
                new Task("Project planning", "2:00 PM")
        ));
        occupationTaskPool.put("businessman", Arrays.asList(
                new Task("Market news review", "8:30 AM"),
                new Task("Networking call", "11:00 AM"),
                new Task("Business planning", "2:00 PM"),
                new Task("Investor updates", "4:00 PM"),
                new Task("Finance review", "8:00 PM")
        ));
        occupationTaskPool.put("lawyer", Arrays.asList(
                new Task("Case review", "9:00 AM"),
                new Task("Court prep", "11:00 AM"),
                new Task("Client meeting", "3:00 PM"),
                new Task("Legal reading", "6:00 PM"),
                new Task("Document drafting", "8:00 PM")
        ));
        occupationTaskPool.put("teacher", Arrays.asList(
                new Task("Lesson prep", "7:30 AM"),
                new Task("Class session", "10:00 AM"),
                new Task("Student check-in", "1:00 PM"),
                new Task("Homework review", "5:00 PM"),
                new Task("Material research", "8:00 PM")
        ));
        occupationTaskPool.put("singer", Arrays.asList(
                new Task("Vocal warm-up", "9:00 AM"),
                new Task("Music practice", "11:00 AM"),
                new Task("Songwriting", "2:00 PM"),
                new Task("Studio recording", "5:00 PM"),
                new Task("Evening performance", "8:00 PM")
        ));
        occupationTaskPool.put("default", Arrays.asList(
                new Task("Skill practice", "10:00 AM"),
                new Task("Personal project", "4:00 PM"),
                new Task("Networking", "6:00 PM"),
                new Task("Read industry news", "8:00 PM"),
                new Task("Reflect on progress", "9:00 PM")
        ));

        String occKey = user.getOccupation() != null ? user.getOccupation().toLowerCase() : "default";
        List<Task> occTasks = occupationTaskPool.getOrDefault(occKey, occupationTaskPool.get("default"));
        tasks.addAll(pickRandom(occTasks, 2));

        // 3. AgeGroup pool (5 → 2)
        Map<String, List<Task>> ageGroupTaskPool = new HashMap<>();
        ageGroupTaskPool.put("10 to 17", Arrays.asList(
                new Task("Play outdoor sports", "5:00 PM"),
                new Task("Do homework", "7:00 PM"),
                new Task("Evening games", "6:30 PM"),
                new Task("Family dinner", "8:30 PM"),
                new Task("Morning school prep", "6:30 AM")
        ));
        ageGroupTaskPool.put("18 to 25", Arrays.asList(
                new Task("Online skill course", "8:00 PM"),
                new Task("Internship work", "11:00 AM"),
                new Task("Job prep reading", "9:30 PM"),
                new Task("Networking event", "6:00 PM"),
                new Task("Early workout", "6:00 AM")
        ));
        ageGroupTaskPool.put("26 to 40", Arrays.asList(
                new Task("Career development", "7:30 PM"),
                new Task("Morning workout", "6:30 AM"),
                new Task("Project brainstorming", "3:00 PM"),
                new Task("Evening walk", "7:00 PM"),
                new Task("Plan next day", "10:00 PM")
        ));
        ageGroupTaskPool.put("41 to 60", Arrays.asList(
                new Task("Yoga", "6:00 AM"),
                new Task("Health checkup", "11:00 AM"),
                new Task("Gardening", "5:30 PM"),
                new Task("Evening news", "7:30 PM"),
                new Task("Family time", "8:30 PM")
        ));
        ageGroupTaskPool.put("60+", Arrays.asList(
                new Task("Morning prayers", "7:00 AM"),
                new Task("Morning walk", "7:30 AM"),
                new Task("Light exercise", "10:00 AM"),
                new Task("Story telling with grandkids", "6:00 PM"),
                new Task("Early sleep", "9:00 PM")
        ));

        if (ageGroupTaskPool.containsKey(user.getAgeGroup().toLowerCase())) {
            tasks.addAll(pickRandom(ageGroupTaskPool.get(user.getAgeGroup().toLowerCase()), 2));
        }

        // 4. WorkTime pool (5 → 2)
        String workTime = user.getWorkTime();
        if (workTime != null && workTime.contains("to")) {
            try {
                String[] times = workTime.split(" to ");
                int startHour = Integer.parseInt(times[0].trim());
                int endHour = Integer.parseInt(times[1].replaceAll("[^0-9]", "").trim());

                // Adjust morning tasks based on work start time
                if (startHour > 9) {
                    tasks.add(new Task("Leisurely morning routine", "7:30 AM - 8:30 AM"));
                } else if (startHour < 8) {
                    tasks.add(new Task("Quick morning preparation", "6:00 AM - 6:30 AM"));
                }

                // Adjust evening tasks based on work end time
                if (endHour > 17) {
                    tasks.add(new Task("Relaxing evening activity", "8:00 PM - 9:00 PM"));
                } else if (endHour < 16) {
                    tasks.add(new Task("Afternoon personal project", "4:00 PM - 5:00 PM"));
                }
            } catch (NumberFormatException e) {
                // If parsing fails, use default adjustments
                tasks.add(new Task("Work-life balance time", "6:00 PM - 7:00 PM"));
            }
        }

        // 5. Gender pool (5 → 2)
        Map<String, List<Task>> genderTaskPool = new HashMap<>();
        genderTaskPool.put("male", Arrays.asList(
                new Task("Strength workout", "6:30 AM"),
                new Task("Sports", "5:30 PM"),
                new Task("Evening walk", "7:30 PM"),
                new Task("Music session", "9:00 PM"),
                new Task("Morning run", "7:00 AM")
        ));
        genderTaskPool.put("female", Arrays.asList(
                new Task("Yoga", "6:30 AM"),
                new Task("Dance/zumba", "7:00 PM"),
                new Task("Meditation", "9:30 PM"),
                new Task("Skin care routine", "10:00 PM"),
                new Task("Morning jog", "7:00 AM")
        ));
        genderTaskPool.put("other", Arrays.asList(
                new Task("Jogging", "6:30 AM"),
                new Task("Art practice", "5:30 PM"),
                new Task("Community meet", "7:30 PM"),
                new Task("Meditation", "9:00 PM"),
                new Task("Music session", "8:00 PM")
        ));

        if (genderTaskPool.containsKey(user.getGender().toLowerCase())) {
            tasks.addAll(pickRandom(genderTaskPool.get(user.getGender().toLowerCase()), 2));
        }

        // --- Remove conflicts & sort ---
        List<Task> finalSchedule = resolveConflicts(tasks);

        return finalSchedule;
    }

    // Helper: pick N random tasks
    private static List<Task> pickRandom(List<Task> pool, int n) {
        Collections.shuffle(pool);
        return new ArrayList<>(pool.subList(0, Math.min(n, pool.size())));
    }

    // Helper: resolve conflicts and sort
    private static List<Task> resolveConflicts(List<Task> tasks) {
        if (tasks == null) return new ArrayList<>();

        // Sort tasks by parsed time (safe with fallback)
        Collections.sort(tasks, new Comparator<Task>() {
            public int compare(Task t1, Task t2) {
                LocalTime lt1 = parseTime(t1.getTimeOfDay());
                LocalTime lt2 = parseTime(t2.getTimeOfDay());

                // fallback to NOON if null
                if (lt1 == null) lt1 = LocalTime.NOON;
                if (lt2 == null) lt2 = LocalTime.NOON;

                return lt1.compareTo(lt2);
            }
        });

        List<Task> resolved = new ArrayList<>();
        Set<LocalTime> used = new HashSet<>();

        for (Task task : tasks) {
            LocalTime time = parseTime(task.getTimeOfDay());

            // fallback for invalid/null time
            if (time == null) time = LocalTime.NOON;

            // find next free 15-min slot
            while (used.contains(time)) {
                time = time.plusMinutes(15);
            }

            // set back formatted string
            task.setTimeOfDay(time.format(formatter));
            used.add(time);
            resolved.add(task);
        }
        return resolved;
    }

    // safer parse method
    private static LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) return null;

        try {
            Matcher m = timePattern.matcher(timeStr);
            if (m.find()) {
                String txt = m.group(0).toUpperCase().replaceAll("\\s+", " ");
                return LocalTime.parse(txt, formatter);
            }
            return LocalTime.parse(timeStr.trim(), formatter);
        } catch (Exception ignored) {
            return null;
        }
    }
}
