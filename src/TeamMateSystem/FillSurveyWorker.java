package TeamMateSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FillSurveyWorker implements Runnable {

    private final Participant participant;
    private final TeamMateController controller;
    private final List<String> answers = new ArrayList<>();
    private final Random random = new Random();
    private final List<String> games;
    private final List<String> roles;

    public FillSurveyWorker(Participant participant, TeamMateController controller) {
        this.participant = participant;
        this.controller = controller;
        this.games = controller.getGAMES();
        this.roles = controller.getROLES();
    }

    public void fillAnswers(){
        answers.add(Integer.toString(random.nextInt(1, 6)));
        answers.add(Integer.toString(random.nextInt(1, 6)));
        answers.add(Integer.toString(random.nextInt(1, 6)));
        answers.add(Integer.toString(random.nextInt(1, 6)));
        answers.add(Integer.toString(random.nextInt(1, 6)));
        answers.add(games.get(random.nextInt(games.size())));
        answers.add(Integer.toString(random.nextInt(1, 11)));
        answers.add(roles.get(random.nextInt(roles.size())));
    }

    @Override
    public void run() {
        try {
            // Add participant
            boolean added = controller.addParticipant(participant);
            if (!added) {
                System.out.println("Participant already exists: " + participant.getId());
                return;
            }

            // Fill survey
            int qNo = 1;
            for (String question : controller.getSurveyQuestions()) {
                boolean accepted = true;
                while (accepted) {
                    String answer = answers.get(qNo - 1);
                    Message msg = controller.fillSurvey(participant.getId(), qNo, answer);
                    accepted = !msg.isSuccess();
                }
                qNo++;
            }

            // Set participant values and store details
            controller.setParticipantValues(participant.getId());
            String personality = controller.viewPersonalityType(participant.getId());
            controller.storeParticipantDetails(participant);

//            System.out.println("Participant: " + participant.getId() + " | Personality: " + personality
//            + " [Thread: " + Thread.currentThread().getName() + "]");

        } catch (Exception e) {
            System.out.println("Error in participant " + participant.getId() + ": " + e.getMessage());
        }
    }
}
