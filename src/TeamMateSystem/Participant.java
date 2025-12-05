package TeamMateSystem;

import java.util.*;

/**
 * The {@code Participant} class represents an individual user who takes part in
 * the TeamMateSystem. Participants can fill survey answers, receive personality
 * classifications, and be assigned to teams.
 *
 * <p>Participants store their information, survey responses, personality scores,
 * and role preferences, and they extend the {@link User} class to allow viewing
 * of their assigned team.</p>
 */
public class Participant extends User {

    /** Auto-incrementing counter used to generate unique participant IDs. */
    private static int counter = 1;

    /** Unique identifier for the participant (e.g., P1, P2...). */
    private String id;

    /** Participant's full name. */
    private String name;

    /** Participant's contact email. */
    private String email;

    /** The participant's preferred game. */
    private String preferredGame;

    /** The participant's skill level (from 1 to 10). */
    private int skillLevel;

    /** The participant's preferred team role (e.g., Strategist, Attacker, etc.). */
    private String preferredRole;

    /** Calculated personality score from survey answers. */
    private int personalityScore;

    /** Personality type (e.g., Leader, Balanced, Thinker). */
    private String personalityType;

    /** Stores survey responses by question number and answer. */
    private Map<Integer, String> responses = new HashMap<>();

    /** Personality classifier used to calculate and classify personality score and type. */
    private PersonalityClassifier classifier;

    /**
     * Creates a new participant with auto-generated ID.
     *
     * @param name  The participant's name.
     * @param email The participant's email.
     */
    public Participant(String name, String email) {
        this.id = "P" + counter++;
        this.name = name;
        this.email = email;
    }

    /**
     * Creates a participant with fully specified attributes. Used when reading from CSV.
     */
    public Participant(String id, String name, String email, String preferredGame, int skillLevel, String preferredRole,
                       int personalityScore, String personalityType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.preferredGame = preferredGame;
        this.skillLevel = skillLevel;
        this.preferredRole = preferredRole;
        this.personalityScore = personalityScore;
        this.personalityType = personalityType;
    }

    /** @return The participant's unique ID. */
    public String getId() {
        return id;
    }

    /** @return The participant's skill level. */
    public int getSkillLevel() {
        return skillLevel;
    }

    /** @return The participant's personality type. */
    public String getPersonalityType() {
        return personalityType;
    }

    /** sets the participant's preferred game. */
    public void setPreferredGame(String preferredGame) {
        this.preferredGame = preferredGame;
    }

    /** sets the participant's skill level. */
    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    /** sets the participant's preferred role. */
    public void setPreferredRole(String preferredRole) {
        this.preferredRole = preferredRole;
    }

    /**
     * Records an answer for a specific survey question.
     *
     * @param questionNo The question number being answered.
     * @param answer     The participant's response.
     */
    public void fillSurvey(int questionNo, String answer){
        responses.put(questionNo, answer);
    }

    /**
     * Generates the participant's personality score and type based on the
     * survey responses using a {@link PersonalityClassifier} class.
     */
    public void viewPersonalityType(){
        classifier = new PersonalityClassifier(new ArrayList<>(responses.values()));
        personalityScore = classifier.calculateScore();
        personalityType = classifier.classify();
    }

    /**
     * Converts a CSV row into a {@code Participant} object.
     *
     * @param row A {@code String[]} representing one CSV row.
     * @return A {@code Participant} populated with CSV data.
     */
    public static Participant covertRow(String[] row){
        return new Participant(row[0], row[1], row[2], row[3], Integer.parseInt(row[4]), row[5], Integer.parseInt(row[6]), row[7]);
    }

    /**
     * Returns a simple formatted details string for the participant. Used when saving teams.
     *
     * @return A readable summary including ID, name, and email.
     */
    public String details(){
        return "ID: " + this.id + ", Name: " + this.name + ", Email: " + this.email;
    }

    /**
     * Allows the participant to view the team they belong to.
     *
     * @param teams The list of all teams in the system.
     * @return A list containing this participant's team, or {@code null} if not assigned.
     */
    @Override
    public List<Team> viewTeams(List<Team> teams) {
        List<Team> thisTeam = new ArrayList<>();
        for (Team team: teams){
            if (team.getParticipants().contains(this)){
                thisTeam.add(team);
                return thisTeam;
            }
        }
        return null;
    }

    /**
     * Converts the participant's data into a CSV-formatted string.
     *
     * @return A comma-separated representation of the participant.
     */
    @Override
    public String toString() {
        return id + "," + name + "," + email + "," + preferredGame + "," + skillLevel + "," + preferredRole + "," + personalityScore + "," + personalityType;
    }
}
