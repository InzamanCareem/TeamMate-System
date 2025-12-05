package TeamMateSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code Survey} class represents the survey system in TeamMateSystem,
 * storing survey questions and the responses submitted by participants.
 *
 * <p>This class supports concurrent access so multiple participants can
 * fill out the survey simultaneously without corrupting data.</p>
 */
public class Survey {

    /** List of survey questions. */
    private List<String> questions = new ArrayList<>();

    /** Stores personality questions related responses per participant ID. */
    private ConcurrentHashMap<String, Map<Integer, String>> personalityResponses = new ConcurrentHashMap<>();

    /** Stores interest questions related responses per participant ID. */
    private ConcurrentHashMap<String, Map<Integer, String>> interestResponses = new ConcurrentHashMap<>();

    /**
     * Constructs a new {@code Survey} and initializes default questions.
     */
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

    /**
     * Returns a copy of the survey questions to prevent external modification.
     *
     * @return An unmodifiable list of survey questions.
     */
    public List<String> getQuestions() {
        // returning an unmodifiable copy
        return List.copyOf(questions);
    }

    /**
     * Adds a personality questions related answer for a participant.
     * This method is thread-safe and can be called concurrently.
     *
     * @param participantId The unique ID of the participant.
     * @param questionNo    The question number being answered.
     * @param answer        The participant's response.
     */
    public void addPersonalityAnswer(String participantId, int questionNo, String answer){
        personalityResponses.putIfAbsent(participantId, new ConcurrentHashMap<>());
        personalityResponses.get(participantId).put(questionNo, answer);
    }

    /**
     * Adds an interest questions related answer for a participant.
     * This method is thread-safe and can be called concurrently.
     *
     * @param participantId The unique ID of the participant.
     * @param questionNo    The question number being answered.
     * @param answer        The participant's response.
     */
    public void addInterestAnswer(String participantId, int questionNo, String answer){
        interestResponses.putIfAbsent(participantId, new ConcurrentHashMap<>());
        interestResponses.get(participantId).put(questionNo, answer);
    }

    /**
     * Returns the interest-related responses of all participants.
     *
     * @return A thread-safe map containing participant IDs and their interest responses.
     */
    public ConcurrentHashMap<String, Map<Integer, String>> getInterestResponses() {
        return interestResponses;
    }
}
