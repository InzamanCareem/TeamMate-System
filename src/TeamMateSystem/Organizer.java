package TeamMateSystem;

import java.io.IOException;
import java.util.List;

/**
 * The {@code Organizer} class represents a system user with privileges to log in to the system, manage
 * participants, upload CSV files, initiate team formation, view teams, and save formed teams.
 * <p>
 * Organizers can:
 * </p>
 * <ul>
 *     <li>Log into the system using their account credentials</li>
 *     <li>Upload participant data from CSV files</li>
 *     <li>Trigger team formation using the {@link TeamBuilder} class</li>
 *     <li>Save formed teams to a file</li>
 *     <li>View existing teams</li>
 * </ul>
 *
 * This class extends {@link User}, allowing organizers to interact with teams
 * within the system.
 */
public class Organizer extends User {

    /** The organizer's account containing username and password credentials. */
    private final Account account;

    /** Strategy pattern: builds teams based on skill levels (default strategy). */
    private final TeamBuilder teamBuilder = new SkillBasedTeamBuilder();

    /**
     * Constructs an Organizer with the given username and password.
     *
     * @param username The organizer's login username.
     * @param password The organizer's login password.
     */
    public Organizer(String username, String password) {
        this.account = new Account(username, password);
    }

    /**
     * Verifies that the provided username and password match this organizer's credentials.
     *
     * @param username The username to validate.
     * @param password The password to validate.
     * @return {@code true} if the credentials are valid; {@code false} otherwise.
     */
    private boolean detailsValid(String username, String password){
        return account.getUsername().equals(username) && account.checkPassword(password);
    }

    /**
     * Attempts to log in using the provided credentials.
     *
     * @param username The username submitted by the user.
     * @param password The password submitted by the user.
     * @return {@code true} if login is successful; {@code false} otherwise.
     */
    public boolean login(String username, String password){
        return detailsValid(username, password);
    }

    /**
     * Uploads and reads a CSV file containing participant data.
     *
     * @param filePath        The path of the CSV file to read.
     * @param csvFileHandler  The handler responsible for parsing CSV files.
     * @return A list of rows, where each row is represented as a {@code String[]}.
     * @throws IOException If the file cannot be opened or read.
     */
    public List<String[]> uploadCsvFile(String filePath, CsvFileHandler csvFileHandler) throws IOException {
        return csvFileHandler.readFile(filePath);
    }

    /**
     * Initiates the formation of teams using the constructed {@link TeamBuilder} class.
     *
     * @param participants The list of participants to divide into teams.
     * @param teamSize     The desired number of participants per team.
     * @return A list of teams created by the team builder.
     */
    public List<Team> initiateTeamFormation(List<Participant> participants, int teamSize){
        return teamBuilder.buildTeam(participants, teamSize);
    }

    /**
     * Saves the formed teams to a CSV file using the specified file handler.
     *
     * @param teams          The teams to save.
     * @param csvFileHandler The handler responsible for saving CSV files.
     * @throws IOException If writing to the file fails.
     */
    public void saveFormedTeams(List<Team> teams, CsvFileHandler csvFileHandler) throws IOException{
        csvFileHandler.saveFile(teams);
    }

    /**
     * Allows the organizer to view existing teams.
     *
     * @param teams A list of current teams.
     * @return The same list of teams passed to the method.
     */
    @Override
    public List<Team> viewTeams(List<Team> teams) {
        return teams;
    }
}
