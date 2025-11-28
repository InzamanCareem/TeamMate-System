package TeamMateSystem;

import java.util.*;

public class Participant extends User{
    private static int counter = 1;
    private String id;
    private String name;
    private String email;
    private String preferredGame;
    private int skillLevel;
    private String preferredRole;
    private int personalityScore;
    private String personalityType;

    private Map<Integer, String> responses = new HashMap<>();
    private PersonalityClassifier classifier;

    public Participant(String name, String email) {
        this.id = "P" + counter++;
        this.name = name;
        this.email = email;
    }

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

    public String getId() {
        return id;
    }

    public String getPersonalityType() {
        return personalityType;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setPreferredGame(String preferredGame) {
        this.preferredGame = preferredGame;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public void setPreferredRole(String preferredRole) {
        this.preferredRole = preferredRole;
    }

    public void fillSurvey(int questionNo, String answer){
        responses.put(questionNo, answer);
    }

    public void viewPersonalityType(){
        classifier = new PersonalityClassifier(new ArrayList<>(responses.values()));
        personalityScore = classifier.calculateScore();
        personalityType = classifier.classify();
    }

    public static Participant covertRow(String[] row){
        return new Participant(row[0], row[1], row[2], row[3], Integer.parseInt(row[4]), row[5], Integer.parseInt(row[6]), row[7]);
    }

    public String details(){
        return "ID: " + this.id + ", Name: " + this.name + ", Email: " + this.email;
    }

    @Override
    public List<Team> viewTeams(List<Team> teams) {
        List<Team> thisTeam = new ArrayList<>();
        for (Team team: teams){
            if (team.getParticipants().contains(this)){
                thisTeam.add(team);
                return thisTeam;
            };
        }
        return null;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + email + "," + preferredGame + "," + skillLevel + "," + preferredRole + "," + personalityScore + "," + personalityType;
    }
}
