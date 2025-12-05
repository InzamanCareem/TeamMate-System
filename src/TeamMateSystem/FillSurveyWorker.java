package TeamMateSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The {@code FillSurveyWorker} class represents a background worker that simulates
 * a participant filling out a survey. It is designed to run as a separate thread,
 * allowing multiple participants to complete surveys concurrently.
 *
 * <p>This worker performs the following actions:</p>
 * <ol>
 *     <li>Adds the participant to the system.</li>
 *     <li>Randomly generates survey answers.</li>
 *     <li>Submits survey responses to the controller.</li>
 *     <li>Triggers value calculation (personality score and type).</li>
 *     <li>Stores the participant's details.</li>
 * </ol>
 *
 * This class is written as to show that surveys can be filled parallelly by many participants.
 * This is primarily used for automated testing, simulation, and multi-threading
 * demonstrations in the TeamMateSystem.
 */
public class FillSurveyWorker implements Runnable {

    /** The participant that this worker will simulate completing a survey. */
    private final Participant participant;

    /** Controller providing access to survey logic and participant management. */
    private final TeamMateController controller;

    /** List of randomly generated answers for the survey questions. */
    private final List<String> answers = new ArrayList<>();

    /** Random number generator used to generate fake survey answers. */
    private final Random random = new Random();

    /** List of available games retrieved from the controller. */
    private final List<String> games;

    /** List of available roles retrieved from the controller. */
    private final List<String> roles;

    /**
     * Constructs a worker that fills a survey for a given participant.
     *
     * @param participant The participant whose survey will be filled.
     * @param controller  The controller managing participants and survey logic.
     */
    public FillSurveyWorker(Participant participant, TeamMateController controller) {
        this.participant = participant;
        this.controller = controller;
        this.games = controller.getGAMES();
        this.roles = controller.getROLES();
    }

    /**
     * Generates the random answers for the participant's survey.
     * <p>
     * The survey answers include:
     * <ul>
     *     <li>Five personality-answers from 1 to 5 inclusive.</li>
     *     <li>A preferred game.</li>
     *     <li>Skill level from 1 to 10 inclusive.</li>
     *     <li>A preferred role.</li>
     * </ul>
     */
    public void fillAnswers(){

        // Can change the value range to simulate incorrect answers for these questions

        // Answers for five personality questions (1–5)
        answers.add(Integer.toString(random.nextInt(1, 6)));
        answers.add(Integer.toString(random.nextInt(1, 6)));
        answers.add(Integer.toString(random.nextInt(1, 6)));
        answers.add(Integer.toString(random.nextInt(1, 6)));
        answers.add(Integer.toString(random.nextInt(1, 6)));

        // Random game, skill level, and preferred role
        answers.add(games.get(random.nextInt(games.size())));
        answers.add(Integer.toString(random.nextInt(1, 11)));
        answers.add(roles.get(random.nextInt(roles.size())));
    }

    /**
     * Executes the full survey-filling routine:
     * <ol>
     *     <li>Add the participant (if not already present).</li>
     *     <li>Submit answers to each survey question and validate the answers.</li>
     *     <li>Calculate participant's personality score and personality type.</li>
     *     <li>Save participant information.</li>
     * </ol>
     *
     * Errors are caught and handled gracefully, and colored status output
     * is printed to the console.
     */
    @Override
    public void run() {
        try {
            // Add participant to the system
            boolean added = controller.addParticipant(participant);
            if (!added) {
                System.out.println("Participant already exists: " + participant.getId());
                return;
            }

            // Fill survey for each question
            for (int qNo = 1; qNo <= controller.getSurveyQuestions().size(); qNo++) {
                boolean accepted = true;
                while (accepted) {
                    String answer = answers.get(qNo - 1);
                    Message msg = controller.fillSurvey(participant.getId(), qNo, answer);

                    // accepted = false means success — exit loop
                    accepted = !msg.isSuccess();

                    if (!msg.isSuccess()){
                        controller.removeParticipant(participant.getId());
                        break;
                    }
                }
            }

            // Set participant values and store details
            controller.setParticipantValues(participant.getId());

            // After survey completion and filling details, calculate all scores for the participant
            String personality = controller.viewPersonalityType(participant.getId());
            
            // Participants with personality type null will be excluded from the system
            if (personality == null){
                controller.removeParticipant(participant.getId());
            }

            // Retrieve personality type and store participant details to file
            controller.storeParticipantDetails(participant);

            // Console output with thread info
            System.out.println(Color.GREEN + "Participant: " + participant.getId() + " | Personality: " + personality
            + " [Thread: " + Thread.currentThread().getName() + "]" + Color.RESET);

        } catch (Exception e) {
            System.out.println(Color.RED + "Invalid input for participant: " + participant.getId() + Color.RESET);
        }
    }
}
