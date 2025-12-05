package TeamMateSystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The {@code TeamBuilder} abstract class provides a framework for forming teams
 * from a list of participants. It defines the core workflow for collecting data,
 * applying matching strategies, and returning the formed teams.
 * This uses the template design pattern.
 *
 * <p>Concrete subclasses must implement {@link #applyMatchingStrategies(List, int)}
 * to define their own team formation logic (e.g., skill-based, random, or role-based).</p>
 */
public abstract class TeamBuilder {

    /**
     * Builds teams from a list of participants using a defined workflow:
     * <ol>
     *     <li>Collect and sort participant data.</li>
     *     <li>Apply a specific matching strategy.</li>
     *     <li>Return the formed teams.</li>
     * </ol>
     *
     * @param participants The list of participants to form into teams.
     * @param teamSize     The desired number of participants per team.
     * @return A list of formed {@link Team} objects.
     */
    public final List<Team> buildTeam(List<Participant> participants, int teamSize){
        List<Participant> participantList = collectData(participants);
        List<Team> teams = applyMatchingStrategies(participantList, teamSize);
        return sendFormedTeams(teams);
    }

    /**
     * Collects participant data and performs pre-processing.
     * By default, it sorts participants by skill level in ascending order.
     * Subclasses may override if additional pre-processing is needed.
     *
     * @param participants The original list of participants.
     * @return A sorted list of participants.
     */
    protected List<Participant> collectData(List<Participant> participants){
        List<Participant> sortedParticipants = new ArrayList<>(participants);
        sortedParticipants.sort(Comparator.comparing(Participant::getSkillLevel));
        return sortedParticipants;
    }

    /**
     * Abstract method to apply a team formation strategy.
     * Subclasses must implement this method to define how participants
     * are assigned to teams.
     *
     * @param participants The sorted list of participants.
     * @param teamSize     The desired number of participants per team.
     * @return A list of {@link Team} objects after applying the strategy.
     */
    protected abstract List<Team> applyMatchingStrategies(List<Participant> participants, int teamSize);

    /**
     * Sends or returns the formed teams. By default, this method simply returns
     * the teams as-is, but subclasses could override it to perform additional
     * processing.
     *
     * @param teams The formed list of teams.
     * @return The final list of teams.
     */
    protected List<Team> sendFormedTeams(List<Team> teams){
        return teams;
    }
}
