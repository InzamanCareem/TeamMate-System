package TeamMateSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code PersonalityClassifier} class calculates a personality score
 * based on survey responses and classifies participants into personality types.
 * <p>
 * It is primarily used to evaluate participants in the TeamMateSystem
 * and assign them to a team based on their survey responses.
 * </p>
 */
public class PersonalityClassifier {

    /** List of participant survey responses as strings. */
    private List<String> result;

    /** The total score calculated from survey responses. */
    private int total;

    /**
     * Constructs a new {@code PersonalityClassifier}.
     *
     * @param result An {@link ArrayList} of survey answers (as strings).
     */
    public PersonalityClassifier(ArrayList<String> result) {
        this.result = result;
    }

    /**
     * Calculates the personality score by summing the first five survey answers
     * and multiplying the sum by 4 (for easier grouping).
     *
     * @return The calculated total personality score.
     */
    public int calculateScore(){
        total = 0;
        for (int i = 0; i < 5; i++) {
            total += Integer.parseInt(result.get(i));
        }
        total *= 4;  // Scale the score
        return total;
    }

    /**
     * Classifies the participant's personality type based on the total score.
     *
     * <ul>
     *     <li>90 or above: "Leader"</li>
     *     <li>70–89: "Balanced"</li>
     *     <li>50–69: "Thinker"</li>
     *     <li>Below 50: {@code null} (no classification)</li>
     * </ul>
     *
     * @return The personality type as a string, or {@code null} if no type matches.
     */
    public String classify(){
        if (total >= 90){
            return "Leader";
        }
        else if (total >= 70){
            return "Balanced";
        }
        else if (total >= 50){
            return "Thinker";
        }
        else{
            return null;
        }
    }
}
