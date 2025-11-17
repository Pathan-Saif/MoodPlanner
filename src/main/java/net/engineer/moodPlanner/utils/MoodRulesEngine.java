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

    public static final Map<String, List<Task>> moodTaskPool = new HashMap<>();
    public static final Map<String, List<Task>> occupationTaskPool = new HashMap<>();
    public static final Map<String, List<Task>> ageGroupTaskPool = new HashMap<>();
    public static final Map<String, List<Task>> workTimeTaskPool = new HashMap<>();
    public static final Map<String, List<Task>> genderTaskPool = new HashMap<>();

    public static List<Task> generateSchedule(User user) {
        List<Task> tasks = new ArrayList<>();

        // 1. Mood pool (10 tasks → pick 5)
//        Map<String, List<Task>> moodTaskPool = new HashMap<>();

        moodTaskPool.put("happy", Arrays.asList(
                new Task("Positive affirmations", "7:00 AM", Arrays.asList(
                        "Stand in front of mirror",
                        "Take a deep breath",
                        "Smile gently",
                        "Say 3 positive lines",
                        "Repeat for 2 minutes"
                )),
                new Task("Healthy breakfast", "7:30 AM", Arrays.asList(
                        "Prepare oats or fruits",
                        "Avoid oily food",
                        "Drink 1 glass of water",
                        "Eat slowly",
                        "Sit near sunlight"
                )),
                new Task("Stretch breaks", "10:30 AM", Arrays.asList(
                        "Raise hands up",
                        "Neck rotation",
                        "Shoulder rolls",
                        "Deep breathing",
                        "Relax 2 mins"
                )),
                new Task("Talk to a friend", "1:00 PM", Arrays.asList(
                        "Choose a close friend",
                        "Call for 10 minutes",
                        "Share positive things",
                        "Ask about them",
                        "End with gratitude"
                )),
                new Task("Evening walk", "6:30 PM", Arrays.asList(
                        "Wear comfortable shoes",
                        "Walk 20 min",
                        "Avoid phone",
                        "Maintain normal pace",
                        "Deep breathing"
                )),
                new Task("Listen to music", "5:00 PM", Arrays.asList(
                        "Choose calm playlist",
                        "Sit comfortably",
                        "Close eyes",
                        "Focus on rhythm",
                        "Relax mind"
                )),
                new Task("Journal gratitude", "9:00 PM", Arrays.asList(
                        "Open notebook",
                        "Write 3 happy things",
                        "Write 1 improvement",
                        "Reflect 2 min",
                        "Close book"
                )),
                new Task("Do a hobby", "8:00 PM", Arrays.asList(
                        "Pick your hobby",
                        "Set 20 min timer",
                        "Remove distractions",
                        "Enjoy fully",
                        "Wrap up slowly"
                )),
                new Task("Quick meditation", "12:00 PM", Arrays.asList(
                        "Sit straight",
                        "Close eyes",
                        "Deep inhale/exhale",
                        "Focus on breath",
                        "Relax gently"
                )),
                new Task("Read a book", "9:30 PM", Arrays.asList(
                        "Choose light content",
                        "Read 3–4 pages",
                        "Avoid phone",
                        "Sit comfortably",
                        "Reflect before sleep"
                ))
        ));


        moodTaskPool.put("sad", Arrays.asList(
                new Task("Gentle stretching", "7:00 AM", Arrays.asList(
                        "Raise arms slowly",
                        "Move shoulders",
                        "Neck left-right",
                        "Slow breathing",
                        "Relax 1 min"
                )),
                new Task("Comfort breakfast", "8:00 AM", Arrays.asList(
                        "Warm tea/coffee",
                        "Light meal",
                        "Sit calmly",
                        "Eat slowly",
                        "Deep breath"
                )),
                new Task("Call a loved one", "12:30 PM", Arrays.asList(
                        "Choose trusted person",
                        "Call for 10 mins",
                        "Talk openly",
                        "Share feelings",
                        "End positively"
                )),
                new Task("Short walk", "5:00 PM", Arrays.asList(
                        "Wear slippers/shoes",
                        "Walk 10 min",
                        "Look at sky",
                        "Calm breathing",
                        "Return slowly"
                )),
                new Task("Watch uplifting movie", "7:30 PM", Arrays.asList(
                        "Select positive movie",
                        "Sit comfortably",
                        "Avoid phone",
                        "Have light snack",
                        "Feel emotions freely"
                )),
                new Task("Write in journal", "9:00 PM", Arrays.asList(
                        "Open notebook",
                        "Write feelings",
                        "Don’t judge yourself",
                        "Slow breathing",
                        "Close journal"
                )),
                new Task("Meditation", "10:00 AM", Arrays.asList(
                        "Sit comfortably",
                        "Close eyes",
                        "Deep inhale/exhale",
                        "Focus on calm",
                        "Relax gently"
                )),
                new Task("Cook favorite meal", "1:30 PM", Arrays.asList(
                        "Choose light recipe",
                        "Prepare ingredients",
                        "Cook slowly",
                        "Enjoy aroma",
                        "Eat calmly"
                )),
                new Task("Evening self-care", "8:30 PM", Arrays.asList(
                        "Wash face",
                        "Apply lotion",
                        "Wear comfortable clothes",
                        "Sit quietly",
                        "Relax mind"
                )),
                new Task("Read something positive", "9:30 PM", Arrays.asList(
                        "Pick inspirational page",
                        "Read slowly",
                        "Reflect 1 min",
                        "Close book",
                        "Go to bed"
                ))
        ));


        moodTaskPool.put("anxious", Arrays.asList(
                new Task("Deep breathing", "7:15 AM", Arrays.asList(
                        "Sit straight",
                        "Inhale 4 sec",
                        "Hold 2 sec",
                        "Exhale 4 sec",
                        "Repeat 5 times"
                )),
                new Task("Morning yoga", "8:00 AM", Arrays.asList(
                        "Basic stretches",
                        "Cat-cow pose",
                        "Child pose",
                        "Slow breathing",
                        "Relax muscles"
                )),
                new Task("Grounding exercise", "11:00 AM", Arrays.asList(
                        "Name 5 things you see",
                        "4 you can touch",
                        "3 you hear",
                        "2 you smell",
                        "1 you feel"
                )),
                new Task("Mindful lunch", "1:00 PM", Arrays.asList(
                        "Sit quietly",
                        "Chew slowly",
                        "Avoid phone",
                        "Feel taste",
                        "Deep breath"
                )),
                new Task("Walk outside", "4:00 PM", Arrays.asList(
                        "Go outside",
                        "Walk 10 mins",
                        "Observe nature",
                        "Deep breathing",
                        "Slow pace"
                )),
                new Task("Meditation", "6:00 PM", Arrays.asList(
                        "Sit upright",
                        "Close eyes",
                        "Focus on breath",
                        "Calm mind",
                        "Relax 5 mins"
                )),
                new Task("Limit screen time", "8:00 PM", Arrays.asList(
                        "Put phone aside",
                        "Dim lights",
                        "Focus on silence",
                        "Relax body",
                        "Stay offline 15 mins"
                )),
                new Task("Write worries down", "9:00 PM", Arrays.asList(
                        "Take notebook",
                        "List worries",
                        "Write positives",
                        "Close journal",
                        "Deep breath"
                )),
                new Task("Read calming book", "9:30 PM", Arrays.asList(
                        "Pick calm story",
                        "Read 5 pages",
                        "Slow breathing",
                        "Relax mind",
                        "Prepare for sleep"
                )),
                new Task("Sleep early", "10:00 PM", Arrays.asList(
                        "Switch off screens",
                        "Dim lights",
                        "Lie down",
                        "Relax body",
                        "Sleep peacefully"
                ))
        ));


        moodTaskPool.put("focused", Arrays.asList(
                new Task("Morning planning", "7:00 AM", Arrays.asList(
                        "Open notebook",
                        "Write tasks",
                        "Mark priorities",
                        "Set deadlines",
                        "Review briefly"
                )),
                new Task("Healthy breakfast", "7:30 AM", Arrays.asList(
                        "Prepare protein meal",
                        "Drink water",
                        "Avoid heavy food",
                        "Eat slowly",
                        "Start day strong"
                )),
                new Task("Deep work session", "9:00 AM", Arrays.asList(
                        "Close distractions",
                        "Set 45-min timer",
                        "Focus fully",
                        "Short break",
                        "Repeat cycle"
                )),
                new Task("Quick stretch", "11:00 AM", Arrays.asList(
                        "Stand straight",
                        "Shoulder rolls",
                        "Neck stretch",
                        "Deep breathing",
                        "Relax 1 min"
                )),
                new Task("Focused lunch", "1:00 PM", Arrays.asList(
                        "Eat balanced meal",
                        "Avoid screen",
                        "Sit upright",
                        "Drink water",
                        "Rest 5 mins"
                )),
                new Task("Project review", "3:00 PM", Arrays.asList(
                        "Open project notes",
                        "Check progress",
                        "Update tasks",
                        "Write next steps",
                        "Organize files"
                )),
                new Task("Evening wrap-up", "6:00 PM", Arrays.asList(
                        "Close pending tasks",
                        "Reply important messages",
                        "Review goals",
                        "Shutdown workspace",
                        "Relax 5 mins"
                )),
                new Task("Plan tomorrow", "8:00 PM", Arrays.asList(
                        "Write next-day tasks",
                        "Estimate time",
                        "Organize list",
                        "Set priorities",
                        "Close notebook"
                )),
                new Task("Read technical book", "9:00 PM", Arrays.asList(
                        "Pick topic",
                        "Read 5 pages",
                        "Make short notes",
                        "Understand concepts",
                        "Close book"
                )),
                new Task("Reflect on goals", "9:30 PM", Arrays.asList(
                        "Check weekly goals",
                        "Rate progress",
                        "Update tasks",
                        "Reflect 2 min",
                        "Prepare for sleep"
                ))
        ));


        moodTaskPool.put("tired", Arrays.asList(
                new Task("Hydrate", "7:00 AM", Arrays.asList(
                        "Drink 1 glass water",
                        "Stretch arms",
                        "Deep breathing",
                        "Sit calmly",
                        "Relax mind"
                )),
                new Task("Light breakfast", "7:30 AM", Arrays.asList(
                        "Eat fruits",
                        "Avoid oily food",
                        "Drink tea if needed",
                        "Sit near sunlight",
                        "Relax quietly"
                )),
                new Task("Nap", "2:00 PM", Arrays.asList(
                        "Lie comfortably",
                        "Close eyes",
                        "Slow breathing",
                        "Relax body",
                        "20-min only"
                )),
                new Task("Short walk", "4:00 PM", Arrays.asList(
                        "Wear sandals",
                        "Walk slowly",
                        "Look around",
                        "Deep breathing",
                        "Return relaxed"
                )),
                new Task("Gentle yoga", "6:00 PM", Arrays.asList(
                        "Sit calmly",
                        "Light stretches",
                        "Deep inhaling",
                        "Relax body",
                        "Slow movements"
                )),
                new Task("Relaxing tea", "7:00 PM", Arrays.asList(
                        "Prepare warm tea",
                        "Sit comfortably",
                        "Sip slowly",
                        "Calm your mind",
                        "Deep breath"
                )),
                new Task("Listen calming music", "8:00 PM", Arrays.asList(
                        "Pick calm track",
                        "Close eyes",
                        "Deep breath",
                        "Relax body",
                        "Focus on sound"
                )),
                new Task("No screens", "9:00 PM", Arrays.asList(
                        "Switch off phone",
                        "Dim lights",
                        "Sit quietly",
                        "Relax mind",
                        "Prepare for sleep"
                )),
                new Task("Gratitude journal", "9:30 PM", Arrays.asList(
                        "Write 3 good things",
                        "Reflect 1 min",
                        "Deep breath",
                        "Close journal",
                        "Feel thankful"
                )),
                new Task("Sleep early", "10:00 PM", Arrays.asList(
                        "Lie down",
                        "Avoid thoughts",
                        "Calm breathing",
                        "Relax body",
                        "Sleep peacefully"
                ))
        ));


        moodTaskPool.put("default", Arrays.asList(
                new Task("Morning hydration", "7:00 AM", Arrays.asList(
                        "Drink water",
                        "Deep breathing",
                        "Stretch arms",
                        "Sit calmly",
                        "Start day fresh"
                )),
                new Task("Balanced breakfast", "7:30 AM", Arrays.asList(
                        "Eat nutritious meal",
                        "Avoid phone",
                        "Drink water",
                        "Chew slowly",
                        "Stay calm"
                )),
                new Task("Work session", "9:00 AM", Arrays.asList(
                        "Open workspace",
                        "Check tasks",
                        "Start top priority",
                        "Focus 45 mins",
                        "Take short break"
                )),
                new Task("Mid-day stretch", "12:00 PM", Arrays.asList(
                        "Stand straight",
                        "Neck stretch",
                        "Shoulder movement",
                        "Deep breath",
                        "Relax"
                )),
                new Task("Nutritious lunch", "1:00 PM", Arrays.asList(
                        "Eat balanced meal",
                        "Avoid heavy food",
                        "Drink water",
                        "Sit quietly",
                        "Walk 2 mins"
                )),
                new Task("Afternoon work", "3:00 PM", Arrays.asList(
                        "Open pending task",
                        "Work 30 mins",
                        "Focus mode",
                        "Avoid phone",
                        "Complete target"
                )),
                new Task("Evening relaxation", "6:00 PM", Arrays.asList(
                        "Sit comfortably",
                        "Deep inhale/exhale",
                        "Stretch lightly",
                        "Calm mind",
                        "Relax body"
                )),
                new Task("Family time", "8:00 PM", Arrays.asList(
                        "Sit with family",
                        "Talk positively",
                        "Share updates",
                        "Laugh together",
                        "Feel connected"
                )),
                new Task("Read book", "9:00 PM", Arrays.asList(
                        "Choose topic",
                        "Read 5 pages",
                        "Avoid screen",
                        "Stay calm",
                        "Close book"
                )),
                new Task("Prepare for tomorrow", "9:30 PM", Arrays.asList(
                        "Review tasks",
                        "Plan next day",
                        "Set priorities",
                        "Relax 1 min",
                        "Sleep peacefully"
                ))
        ));


        String moodKey = user.getMood() != null ? user.getMood().toLowerCase() : "default";
        List<Task> moodTasks = moodTaskPool.getOrDefault(moodKey, moodTaskPool.get("default"));
        tasks.addAll(pickRandom(moodTasks, 5));

        // 2. Occupation pool (5 → 2)

        occupationTaskPool.put("engineer", Arrays.asList(
                new Task("Review technical notes", "8:00 PM", Arrays.asList(
                        "Open your notebook or docs",
                        "Scan key concepts",
                        "Highlight important lines",
                        "Update missing points",
                        "Close with a quick recap"
                )),
                new Task("Problem solving", "4:00 PM", Arrays.asList(
                        "Pick one coding problem",
                        "Read requirements clearly",
                        "Plan simple approach",
                        "Write solution calmly",
                        "Verify edge cases"
                )),
                new Task("Tech news read", "9:00 AM", Arrays.asList(
                        "Open trusted tech site",
                        "Skim major headlines",
                        "Read one useful article",
                        "Note key takeaway",
                        "Close distractions"
                )),
                new Task("Debug practice", "11:00 AM", Arrays.asList(
                        "Open previous code",
                        "Run and observe output",
                        "Identify error area",
                        "Apply small fixes",
                        "Re-run and confirm"
                )),
                new Task("Project planning", "2:00 PM", Arrays.asList(
                        "Open task board",
                        "Check deadlines",
                        "Break into small tasks",
                        "Assign time blocks",
                        "Review final checklist"
                ))
        ));

        occupationTaskPool.put("businessman", Arrays.asList(
                new Task("Market news review", "8:30 AM", Arrays.asList(
                        "Open financial news portal",
                        "Scan market trends",
                        "Check stock updates",
                        "Note major shifts",
                        "Plan quick action items"
                )),
                new Task("Networking call", "11:00 AM", Arrays.asList(
                        "Prepare short agenda",
                        "Call business contact",
                        "Discuss collaboration points",
                        "Note important highlights",
                        "End with next steps"
                )),
                new Task("Business planning", "2:00 PM", Arrays.asList(
                        "Open business dashboard",
                        "Review performance metrics",
                        "Set short-term goals",
                        "Update strategy list",
                        "Finalize priority items"
                )),
                new Task("Investor updates", "4:00 PM", Arrays.asList(
                        "Check investor notes",
                        "Write quick summary",
                        "Send email update",
                        "Attach required documents",
                        "Review feedback later"
                )),
                new Task("Finance review", "8:00 PM", Arrays.asList(
                        "Open expense sheet",
                        "Check transactions",
                        "Evaluate profits & loss",
                        "Note correction points",
                        "Plan next day finances"
                ))
        ));

        occupationTaskPool.put("lawyer", Arrays.asList(
                new Task("Case review", "9:00 AM", Arrays.asList(
                        "Open case file",
                        "Read recent updates",
                        "Mark key points",
                        "Check hearing date",
                        "Plan next steps"
                )),
                new Task("Court prep", "11:00 AM", Arrays.asList(
                        "Organize documents",
                        "Review arguments",
                        "Prepare evidence list",
                        "Practice statements",
                        "Confirm timings"
                )),
                new Task("Client meeting", "3:00 PM", Arrays.asList(
                        "Review client notes",
                        "Discuss case progress",
                        "Clarify legal doubts",
                        "Record important details",
                        "Plan future actions"
                )),
                new Task("Legal reading", "6:00 PM", Arrays.asList(
                        "Open law journal",
                        "Read new amendments",
                        "Highlight key sections",
                        "Note useful references",
                        "Save for future use"
                )),
                new Task("Document drafting", "8:00 PM", Arrays.asList(
                        "Open drafting template",
                        "Write important sections",
                        "Proofread carefully",
                        "Add legal citations",
                        "Save final version"
                ))
        ));

        occupationTaskPool.put("teacher", Arrays.asList(
                new Task("Lesson prep", "7:30 AM", Arrays.asList(
                        "Review topic",
                        "Prepare examples",
                        "Create notes",
                        "Plan activities",
                        "Organize material"
                )),
                new Task("Class session", "10:00 AM", Arrays.asList(
                        "Enter classroom calmly",
                        "Teach main topic",
                        "Engage students",
                        "Ask questions",
                        "Conclude clearly"
                )),
                new Task("Student check-in", "1:00 PM", Arrays.asList(
                        "Talk to students",
                        "Ask about progress",
                        "Identify weak areas",
                        "Give guidance",
                        "Motivate gently"
                )),
                new Task("Homework review", "5:00 PM", Arrays.asList(
                        "Check assignments",
                        "Mark corrections",
                        "Give feedback",
                        "Record grades",
                        "Prepare remarks"
                )),
                new Task("Material research", "8:00 PM", Arrays.asList(
                        "Search new content",
                        "Read reference material",
                        "Collect examples",
                        "Update lesson notes",
                        "Save everything neatly"
                ))
        ));

        occupationTaskPool.put("singer", Arrays.asList(
                new Task("Vocal warm-up", "9:00 AM", Arrays.asList(
                        "Drink warm water",
                        "Start humming",
                        "Practice scales",
                        "Focus on breath control",
                        "Relax throat muscles"
                )),
                new Task("Music practice", "11:00 AM", Arrays.asList(
                        "Choose your song",
                        "Practice sections slowly",
                        "Correct pitch issues",
                        "Repeat tricky parts",
                        "Record short sample"
                )),
                new Task("Songwriting", "2:00 PM", Arrays.asList(
                        "Sit with notebook",
                        "Think of theme",
                        "Write rough lines",
                        "Fix rhythm & flow",
                        "Save draft version"
                )),
                new Task("Studio recording", "5:00 PM", Arrays.asList(
                        "Check mic setup",
                        "Do quick vocal test",
                        "Record main track",
                        "Review mistakes",
                        "Save clean take"
                )),
                new Task("Evening performance", "8:00 PM", Arrays.asList(
                        "Prepare outfit",
                        "Warm up vocals again",
                        "Check sound system",
                        "Perform confidently",
                        "End with gratitude"
                ))
        ));

        occupationTaskPool.put("default", Arrays.asList(
                new Task("Skill practice", "10:00 AM", Arrays.asList(
                        "Choose one skill",
                        "Prepare quick notes",
                        "Practice 20 minutes",
                        "Review mistakes",
                        "Track improvement"
                )),
                new Task("Personal project", "4:00 PM", Arrays.asList(
                        "Open project files",
                        "Set small goal",
                        "Work without distraction",
                        "Test results",
                        "Save progress"
                )),
                new Task("Networking", "6:00 PM", Arrays.asList(
                        "Open LinkedIn or contacts",
                        "Message one person",
                        "Share quick update",
                        "Discuss opportunities",
                        "Maintain connection"
                )),
                new Task("Read industry news", "8:00 PM", Arrays.asList(
                        "Open news source",
                        "Skim important topics",
                        "Read one full article",
                        "Note key learning",
                        "Plan how to use it"
                )),
                new Task("Reflect on progress", "9:00 PM", Arrays.asList(
                        "Sit quietly",
                        "Think about your day",
                        "Note achievements",
                        "Write improvements",
                        "Close journal calmly"
                ))
        ));


        String occKey = user.getOccupation() != null ? user.getOccupation().toLowerCase() : "default";
        List<Task> occTasks = occupationTaskPool.getOrDefault(occKey, occupationTaskPool.get("default"));
        tasks.addAll(pickRandom(occTasks, 2));

        // 3. AgeGroup pool (5 → 2)

        ageGroupTaskPool.put("10 to 17", Arrays.asList(
                new Task("Play outdoor sports", "5:00 PM", Arrays.asList(
                        "Pick your favorite sport",
                        "Call friends or teammates",
                        "Warm up properly",
                        "Play actively for 30 mins",
                        "Cool down before leaving"
                )),
                new Task("Do homework", "7:00 PM", Arrays.asList(
                        "Open school notebook",
                        "Read instructions carefully",
                        "Finish subject-wise tasks",
                        "Double-check answers",
                        "Prepare for the next day"
                )),
                new Task("Evening games", "6:30 PM", Arrays.asList(
                        "Choose indoor/outdoor game",
                        "Set up quickly",
                        "Play with full fun",
                        "Avoid distractions",
                        "Close game neatly"
                )),
                new Task("Family dinner", "8:30 PM", Arrays.asList(
                        "Wash hands",
                        "Sit with family",
                        "Eat slowly",
                        "Talk about your day",
                        "Help clean up"
                )),
                new Task("Morning school prep", "6:30 AM", Arrays.asList(
                        "Wake up peacefully",
                        "Pack school bag",
                        "Get ready properly",
                        "Check timetable",
                        "Leave on time"
                ))
        ));

        ageGroupTaskPool.put("18 to 25", Arrays.asList(
                new Task("Online skill course", "8:00 PM", Arrays.asList(
                        "Open course platform",
                        "Start new lecture",
                        "Take quick notes",
                        "Complete small quiz",
                        "Save progress"
                )),
                new Task("Internship work", "11:00 AM", Arrays.asList(
                        "Check assigned tasks",
                        "Write short plan",
                        "Work with focus",
                        "Update mentor",
                        "Submit work"
                )),
                new Task("Job prep reading", "9:30 PM", Arrays.asList(
                        "Open study material",
                        "Read one chapter",
                        "Highlight important points",
                        "Take small notes",
                        "Close with summary"
                )),
                new Task("Networking event", "6:00 PM", Arrays.asList(
                        "Dress professionally",
                        "Reach event location",
                        "Introduce yourself",
                        "Talk to new people",
                        "Save contacts"
                )),
                new Task("Early workout", "6:00 AM", Arrays.asList(
                        "Drink water",
                        "Warm up 5 mins",
                        "Do main exercises",
                        "Stretch properly",
                        "Cool down"
                ))
        ));

        ageGroupTaskPool.put("26 to 40", Arrays.asList(
                new Task("Career development", "7:30 PM", Arrays.asList(
                        "Check skill roadmap",
                        "Pick one career topic",
                        "Learn for 20 mins",
                        "Apply concept somewhere",
                        "Record progress"
                )),
                new Task("Morning workout", "6:30 AM", Arrays.asList(
                        "Wear workout clothes",
                        "Do light warm-up",
                        "Perform full routine",
                        "Stretch at end",
                        "Drink water"
                )),
                new Task("Project brainstorming", "3:00 PM", Arrays.asList(
                        "Open project plan",
                        "Write new ideas",
                        "Analyze feasibility",
                        "Pick two best ideas",
                        "Note action steps"
                )),
                new Task("Evening walk", "7:00 PM", Arrays.asList(
                        "Wear comfortable shoes",
                        "Start slow walk",
                        "Increase pace gradually",
                        "Enjoy fresh air",
                        "Return calmly"
                )),
                new Task("Plan next day", "10:00 PM", Arrays.asList(
                        "Open planner",
                        "Write tomorrow’s tasks",
                        "Set priorities",
                        "Estimate time",
                        "Sleep peacefully"
                ))
        ));

        ageGroupTaskPool.put("41 to 60", Arrays.asList(
                new Task("Yoga", "6:00 AM", Arrays.asList(
                        "Spread yoga mat",
                        "Start with breathing",
                        "Do simple asanas",
                        "Relax body slowly",
                        "End with meditation"
                )),
                new Task("Health checkup", "11:00 AM", Arrays.asList(
                        "Schedule appointment",
                        "Carry medical records",
                        "Meet doctor calmly",
                        "Discuss key issues",
                        "Follow instructions"
                )),
                new Task("Gardening", "5:30 PM", Arrays.asList(
                        "Wear gloves",
                        "Check plant soil",
                        "Water plants gently",
                        "Trim dry leaves",
                        "Clean surrounding"
                )),
                new Task("Evening news", "7:30 PM", Arrays.asList(
                        "Sit comfortably",
                        "Turn on news channel",
                        "Watch top headlines",
                        "Note important updates",
                        "End screen time"
                )),
                new Task("Family time", "8:30 PM", Arrays.asList(
                        "Sit with family",
                        "Share experiences",
                        "Discuss plans",
                        "Enjoy calm moments",
                        "End on positive note"
                ))
        ));

        ageGroupTaskPool.put("60+", Arrays.asList(
                new Task("Morning prayers", "7:00 AM", Arrays.asList(
                        "Sit peacefully",
                        "Close eyes gently",
                        "Chant or meditate",
                        "Focus on calmness",
                        "End softly"
                )),
                new Task("Morning walk", "7:30 AM", Arrays.asList(
                        "Wear comfortable shoes",
                        "Start slow pace",
                        "Enjoy sunlight",
                        "Walk 15–20 mins",
                        "Return and rest"
                )),
                new Task("Light exercise", "10:00 AM", Arrays.asList(
                        "Stand upright",
                        "Do light stretches",
                        "Move joints slowly",
                        "Breathe calmly",
                        "Avoid strain"
                )),
                new Task("Story telling with grandkids", "6:00 PM", Arrays.asList(
                        "Call grandkids",
                        "Pick short story",
                        "Tell with expressions",
                        "Engage them happily",
                        "End with a lesson"
                )),
                new Task("Early sleep", "9:00 PM", Arrays.asList(
                        "Wash face",
                        "Turn off lights",
                        "Lie down comfortably",
                        "Relax mind",
                        "Sleep early"
                ))
        ));


        if (ageGroupTaskPool.containsKey(user.getAgeGroup().toLowerCase())) {
            tasks.addAll(pickRandom(ageGroupTaskPool.get(user.getAgeGroup().toLowerCase()), 2));
        }

        // 4. WorkTime pool (5 → 2)
//        String workTime = user.getWorkTime();
//        if (workTime != null && workTime.contains("to")) {
//            try {
//                String[] times = workTime.split(" to ");
//                int startHour = Integer.parseInt(times[0].trim());
//                int endHour = Integer.parseInt(times[1].replaceAll("[^0-9]", "").trim());
//
//                // Adjust morning tasks based on work start time
//                if (startHour > 9) {
//                    tasks.add(new Task(
//                            "Leisurely morning routine",
//                            "7:30 AM - 8:30 AM",
//                            List.of(
//                                    "Wake up peacefully",
//                                    "Drink warm water",
//                                    "Light stretching for 5–10 min",
//                                    "Enjoy a slow healthy breakfast",
//                                    "Plan the day's priorities"
//                            )
//                    ));
//                } else if (startHour < 8) {
//                    tasks.add(new Task(
//                            "Quick morning preparation",
//                            "6:00 AM - 6:30 AM",
//                            List.of(
//                                    "Wake up quickly",
//                                    "Splash water on face",
//                                    "Prepare a simple breakfast",
//                                    "Get ready for work",
//                                    "Review today’s tasks"
//                            )
//                    ));
//                }
//
//                // Adjust evening tasks based on work end time
//                if (endHour > 17) {
//                    tasks.add(new Task(
//                            "Relaxing evening activity",
//                            "8:00 PM - 9:00 PM",
//                            List.of(
//                                    "Take a warm shower",
//                                    "Relax with soft music",
//                                    "Light dinner",
//                                    "10 minutes meditation",
//                                    "Prepare for sleep"
//                            )
//                    ));
//                } else if (endHour < 16) {
//                    tasks.add(new Task(
//                            "Afternoon personal project",
//                            "4:00 PM - 5:00 PM",
//                            List.of(
//                                    "Set project goal",
//                                    "Research or outline tasks",
//                                    "Execute planned work",
//                                    "Take notes of progress",
//                                    "Plan next steps"
//                            )
//                    ));
//                }
//
//            } catch (NumberFormatException e) {
//                tasks.add(new Task(
//                        "Work-life balance time",
//                        "6:00 PM - 7:00 PM",
//                        List.of(
//                                "Relax for 10 minutes",
//                                "Organize workspace",
//                                "Quick walk",
//                                "Light snack",
//                                "Prepare for tomorrow"
//                        )
//                ));
//            }
//        }


//        Map<String, List<Task>> workTimeTaskPool = new HashMap<>();

        workTimeTaskPool.put("early_shift", Arrays.asList(
                new Task("Early morning focus session", "5:30 AM", Arrays.asList(
                        "Wake up calmly",
                        "Drink a glass of water",
                        "Do 5-minute stretch",
                        "Review today’s goals",
                        "Begin light mental warm-up"
                )),
                new Task("Post-work relaxation", "4:30 PM", Arrays.asList(
                        "Take a short walk",
                        "Disconnect from work apps",
                        "Do deep breathing",
                        "Listen to calm music",
                        "Plan a relaxing activity"
                )),
                new Task("Healthy breakfast routine", "6:00 AM", Arrays.asList(
                        "Prepare a simple meal",
                        "Avoid heavy foods",
                        "Drink something warm",
                        "Sit peacefully while eating",
                        "Clean up neatly"
                )),
                new Task("Evening personal learning", "7:30 PM", Arrays.asList(
                        "Pick one topic",
                        "Read for 15 minutes",
                        "Watch one short tutorial",
                        "Note key learning",
                        "Plan next session"
                )),
                new Task("Night wind-down", "9:30 PM", Arrays.asList(
                        "Turn off screens",
                        "Dim room lights",
                        "Relax your shoulders",
                        "Slow breathing exercise",
                        "Sleep without stress"
                ))
        ));

        workTimeTaskPool.put("regular_shift", Arrays.asList(
                new Task("Balanced morning routine", "7:00 AM", Arrays.asList(
                        "Wake up slowly",
                        "Brush & freshen up",
                        "Eat a healthy breakfast",
                        "Check work schedule",
                        "Leave calmly"
                )),
                new Task("Efficient work wrap-up", "6:00 PM", Arrays.asList(
                        "Close all tasks",
                        "Write next-day notes",
                        "Reply to pending messages",
                        "Turn off work mode",
                        "Step away from the desk"
                )),
                new Task("Mid-day refresh", "1:00 PM", Arrays.asList(
                        "Take a short walk",
                        "Drink water",
                        "Relax your shoulders",
                        "Avoid heavy lunch",
                        "Return with fresh focus"
                )),
                new Task("Evening fitness", "7:00 PM", Arrays.asList(
                        "Warm up 5 minutes",
                        "Do light exercise",
                        "Stretch body muscles",
                        "Cool down properly",
                        "Track workout progress"
                )),
                new Task("Night review session", "10:00 PM", Arrays.asList(
                        "Sit comfortably",
                        "Recall your day",
                        "Write down improvements",
                        "Appreciate small wins",
                        "Sleep peacefully"
                ))
        ));

        workTimeTaskPool.put("late_shift", Arrays.asList(
                new Task("Slow morning start", "9:00 AM", Arrays.asList(
                        "Wake up without rush",
                        "Drink warm water",
                        "Light stretching",
                        "Plan your late shift",
                        "Have an easy breakfast"
                )),
                new Task("Post-work cool-down", "11:00 PM", Arrays.asList(
                        "Sit quietly",
                        "Relax your eyes",
                        "Avoid using bright screens",
                        "Drink warm water",
                        "Prepare for sleep"
                )),
                new Task("Afternoon energy boost", "3:00 PM", Arrays.asList(
                        "Drink light juice/tea",
                        "Walk for 5 minutes",
                        "Take deep breaths",
                        "Organize mid-shift tasks",
                        "Stay focused"
                )),
                new Task("Late evening meal", "9:00 PM", Arrays.asList(
                        "Choose light food",
                        "Avoid fried snacks",
                        "Eat slowly",
                        "Drink little water after",
                        "Sit comfortably for 10 mins"
                )),
                new Task("Late-night unwind", "12:00 AM", Arrays.asList(
                        "Turn off notifications",
                        "Do breathing exercise",
                        "Keep room dim",
                        "Avoid heavy thoughts",
                        "Sleep naturally"
                ))
        ));

        if (workTimeTaskPool.containsKey(user.getWorkTime().toLowerCase())) {
            tasks.addAll(pickRandom(workTimeTaskPool.get(user.getWorkTime().toLowerCase()), 2));
        }



        // 5. Gender pool (5 → 2)

        genderTaskPool.put("male", Arrays.asList(
                new Task("Strength workout", "6:30 AM", Arrays.asList(
                        "Warm-up stretches for 5 minutes",
                        "Push-ups and squats routine",
                        "Dumbbell or bodyweight exercises",
                        "Cool-down walk for 5 minutes",
                        "Hydration and light snack"
                )),
                new Task("Sports", "5:30 PM", Arrays.asList(
                        "Choose your sport (cricket/football/badminton)",
                        "10-minute warm-up jog",
                        "Main gameplay session",
                        "Short break with hydration",
                        "Cool-down and stretching"
                )),
                new Task("Evening walk", "7:30 PM", Arrays.asList(
                        "Wear comfortable walking shoes",
                        "Walk at a moderate pace",
                        "Take deep breaths during walk",
                        "Stop midway for stretching",
                        "Return home slowly, cool down"
                )),
                new Task("Music session", "9:00 PM", Arrays.asList(
                        "Choose playlist or instrument",
                        "Set relaxed lighting",
                        "Play/sing for 15 minutes",
                        "Reflect and practice improvements",
                        "Wrap up and relax"
                )),
                new Task("Morning run", "7:00 AM", Arrays.asList(
                        "Light stretching before starting",
                        "Start jogging slowly",
                        "Increase pace gradually",
                        "Maintain breathing pattern",
                        "Cool down at the end"
                ))
        ));

        genderTaskPool.put("female", Arrays.asList(
                new Task("Yoga", "6:30 AM", Arrays.asList(
                        "Deep breathing warm-up",
                        "Basic yoga poses (sun salutation)",
                        "Balance posture practice",
                        "Meditation for 5 minutes",
                        "Cool-down stretching"
                )),
                new Task("Dance/Zumba", "7:00 PM", Arrays.asList(
                        "Quick warm-up",
                        "Energetic zumba routine",
                        "Short hydration break",
                        "Continue high-energy steps",
                        "Cool-down dance/stretch"
                )),
                new Task("Meditation", "9:30 PM", Arrays.asList(
                        "Sit comfortably in a quiet place",
                        "Focus on breathing rhythm",
                        "Detach from distracting thoughts",
                        "Stay mindful for 10 minutes",
                        "Gently open your eyes"
                )),
                new Task("Skin care routine", "10:00 PM", Arrays.asList(
                        "Wash face with cleanser",
                        "Apply toner",
                        "Use moisturizer",
                        "Apply serum or night cream",
                        "Massage gently for relaxation"
                )),
                new Task("Morning jog", "7:00 AM", Arrays.asList(
                        "Wear comfortable jogging shoes",
                        "Start walking slowly",
                        "Transition to a light jog",
                        "Maintain steady rhythm",
                        "Cool-down walk and stretching"
                ))
        ));

        genderTaskPool.put("other", Arrays.asList(
                new Task("Jogging", "6:30 AM", Arrays.asList(
                        "Light stretches before jogging",
                        "Start with slow-paced jogging",
                        "Maintain steady pace",
                        "Hydrate midway",
                        "Cool down and stretch"
                )),
                new Task("Art practice", "5:30 PM", Arrays.asList(
                        "Prepare your workspace",
                        "Sketch basic outlines",
                        "Work on detailing",
                        "Add shading or colors",
                        "Review and refine"
                )),
                new Task("Community meet", "7:30 PM", Arrays.asList(
                        "Prepare talking points",
                        "Reach meeting spot on time",
                        "Engage in discussion",
                        "Network with members",
                        "Take feedback and notes"
                )),
                new Task("Meditation", "9:00 PM", Arrays.asList(
                        "Find a quiet comfortable place",
                        "Start with slow breathing",
                        "Focus on inner calmness",
                        "Avoid distractions",
                        "End with gratitude reflection"
                )),
                new Task("Music session", "8:00 PM", Arrays.asList(
                        "Select music instrument/playlist",
                        "Start with warm-up notes",
                        "Practice main sequence",
                        "Record or reflect",
                        "End session peacefully"
                ))
        ));


        if (genderTaskPool.containsKey(user.getGender().toLowerCase())) {
            tasks.addAll(pickRandom(genderTaskPool.get(user.getGender().toLowerCase()), 2));
        }

        // --- Remove conflicts & sort ---
        List<Task> finalSchedule = resolveConflicts(tasks);

        return finalSchedule;
    }

    public static List<Task> getAllTasks() {
        List<Task> all = new ArrayList<>();
        moodTaskPool.values().forEach(all::addAll);
        occupationTaskPool.values().forEach(all::addAll);
        ageGroupTaskPool.values().forEach(all::addAll);
        workTimeTaskPool.values().forEach(all::addAll);
        genderTaskPool.values().forEach(all::addAll);

        return all;
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
