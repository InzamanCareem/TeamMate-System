package TeamMateSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The {@code SkillBasedTeamBuilder} class is a concrete implementation of {@link TeamBuilder} class
 * that creates teams based on participant ordering, alternating between the lowest
 * and highest skill levels to balance teams.
 *
 * <p>Teams are populated in parallel using multiple threads to improve performance
 * when adding participants to each team.</p>
 */
public class SkillBasedTeamBuilder extends TeamBuilder {

    /**
     * Applies the skill-based matching strategy to form balanced teams.
     *
     * <p>The strategy works as follows:</p>
     * <ol>
     *     <li>Calculate the number of teams based on participant count and team size.</li>
     *     <li>Create empty team objects.</li>
     *     <li>Precompute team assignments by alternating lowest and highest skill participants.</li>
     *     <li>Populate the teams in parallel using an ExecutorService.</li>
     * </ol>
     *
     * @param participants The list of participants to assign to teams.
     * @param teamSize     The desired number of participants per team.
     * @return A list of {@link Team} objects with assigned participants.
     */
    @Override
    protected List<Team> applyMatchingStrategies(List<Participant> participants, int teamSize) {
        List<Team> teams = new ArrayList<>();

        // Calculate the number of teams required
        int noOfTeams = (int) Math.ceil((double) participants.size() / teamSize);

        // Create empty teams
        for (int i = 0; i < noOfTeams; i++) {
            teams.add(new Team(i+1, "Team"));
        }

        // Precomputing which participant goes to which team, so we can safely parallelize it without corrupting the order.
        List<List<Participant>> teamAssignments = new ArrayList<>();
        for (int i = 0; i < noOfTeams; i++) {
            teamAssignments.add(new ArrayList<>());
        }

        int lastMinChosen = 0;  // Pointer to lowest-skill participant
        int lastMaxChosen = participants.size() - 1;  // Pointer to highest-skill participant

        // Assign participants to each team alternating between lowest and highest
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

        // Populating the teams in parallel using a thread pool
        int numThreads = Math.min(noOfTeams, Runtime.getRuntime().availableProcessors());
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < noOfTeams; i++) {
            final int index = i;  // Needed because lambda captures final variables only
            executorService.submit(() -> {
                for (Participant participant: teamAssignments.get(index)){
                    teams.get(index).addParticipant(participant);
                }
            });
        }

        // Shutdown executor and wait for the tasks to finish
        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            return null;
        }

        return teams;
    }
}
