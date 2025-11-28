package TeamMateSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Survey {
    private List<String> questions = new ArrayList<>();
    private ConcurrentHashMap<String, Map<Integer, String>> personalityResponses = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Map<Integer, String>> interestResponses = new ConcurrentHashMap<>();

    public Survey(){
        questions.add("1. I enjoy taking the lead and guiding others during group activities: ");
        questions.add("2. I prefer analyzing situations and coming up with strategic solutions: ");
        questions.add("3. I work well with others and enjoy collaborative teamwork: ");
        questions.add("4. I am calm under pressure and can help maintain team morale: ");
        questions.add("5. I like making quick decisions and adapting in dynamic situations: ");
        questions.add("6. What is you preferred game: ");
        questions.add("7. What is you skill level: ");
        questions.add("8. What is you preferred role: ");
    }

    public List<String> getQuestions() {
        // returning an unmodifiable copy
        return List.copyOf(questions);
    }

    // Many Participants can call this
    public void addPersonalityAnswer(String participantId, int questionNo, String answer){
        personalityResponses.putIfAbsent(participantId, new ConcurrentHashMap<>());
        personalityResponses.get(participantId).put(questionNo, answer);
    }

    public void addInterestAnswer(String participantId, int questionNo, String answer){
        interestResponses.putIfAbsent(participantId, new ConcurrentHashMap<>());
        interestResponses.get(participantId).put(questionNo, answer);
    }

    public ConcurrentHashMap<String, Map<Integer, String>> getInterestResponses() {
        return interestResponses;
    }
}
