package TeamMateSystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class TeamBuilder {

    public final List<Team> buildTeam(List<Participant> participants, int teamSize){
        List<Participant> participantList = collectData(participants);
        List<Team> teams = applyMatchingStrategies(participantList, teamSize);
        return sendFormedTeams(teams);
    }

    protected List<Participant> collectData(List<Participant> participants){
        List<Participant> sortedParticipants = new ArrayList<>(participants);
        sortedParticipants.sort(Comparator.comparing(Participant::getSkillLevel));
        return sortedParticipants;
    }

    protected abstract List<Team> applyMatchingStrategies(List<Participant> participants, int teamSize);

    protected List<Team> sendFormedTeams(List<Team> teams){
        return teams;
    }
}
