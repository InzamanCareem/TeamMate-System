package TeamMateSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SkillBasedTeamBuilder extends TeamBuilder{

    @Override
    protected List<Team> applyMatchingStrategies(List<Participant> participants, int teamSize) {
        List<Team> teams = new ArrayList<>();

        int noOfTeams = (int) Math.ceil((double) participants.size() / teamSize);

        for (int i = 0; i < noOfTeams; i++) {
            teams.add(new Team(i+1, "Team"));
        }

        // Precomputing which participant goes to which team, so we can safely parallelize it without corrupting the order.
        List<List<Participant>> teamAssignments = new ArrayList<>();
        for (int i = 0; i < noOfTeams; i++) {
            teamAssignments.add(new ArrayList<>());
        }

        int lastMinChosen = 0;
        int lastMaxChosen = participants.size() - 1;

        // Assign participants to teams first
        for (int i = 0; i < noOfTeams; i++) {
            int count = 1;
            while (count <= teamSize && lastMinChosen <= lastMaxChosen) {
                teamAssignments.get(i).add(participants.get(lastMinChosen++));
                count++;
                if (count <= teamSize && lastMinChosen <= lastMaxChosen) {
                    teamAssignments.get(i).add(participants.get(lastMaxChosen--));
                    count++;
                }
            }
        }

        // Populating the teams in parallel
        int numThreads = Math.min(noOfTeams, Runtime.getRuntime().availableProcessors());
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < noOfTeams; i++) {
            final int index = i;
            executorService.submit(() -> {
                for (Participant participant: teamAssignments.get(index)){
                    teams.get(index).addParticipant(participant);
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            return null;
        }

        return teams;
    }
}
