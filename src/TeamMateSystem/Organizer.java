package TeamMateSystem;

import java.io.IOException;
import java.util.List;

public class Organizer extends User{
    private final Account account;
    private final TeamBuilder teamBuilder = new SkillBasedTeamBuilder();

    public Organizer(String username, String password) {
        this.account = new Account(username, password);
    }

    private boolean detailsValid(String username, String password){
        return account.getUsername().equals(username) && account.getPassword().equals(password);
    }

    public boolean login(String username, String password){
        return detailsValid(username, password);
    }

    public List<String[]> uploadCsvFile(String filePath, CsvFileHandler csvFileHandler) throws IOException {
        return csvFileHandler.readFile(filePath);
    }

    public List<Team> initiateTeamFormation(List<Participant> participants, int teamSize){
        return teamBuilder.buildTeam(participants, teamSize);
    }

    public void saveFormedTeams(List<Team> teams, CsvFileHandler csvFileHandler) throws IOException{
        csvFileHandler.saveFile(teams);
    }

    @Override
    public List<Team> viewTeams(List<Team> teams) {
        return teams;
    }
}
