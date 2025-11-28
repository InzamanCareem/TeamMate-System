package TeamMateSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TeamMateController {
    private final ConcurrentHashMap<String, Participant> participants = new ConcurrentHashMap<>();
    private final Survey survey = new Survey();
    private final Organizer organizer = new Organizer("admin", "123");
    private final List<String> GAMES = new ArrayList<>(Arrays.asList("chess", "fifa", "basketball", "cs:go", "dota 2", "valorant"));
    private final List<String> ROLES = new ArrayList<>(Arrays.asList("strategist", "attacker", "defender", "supporter", "coordinator"));
    private final List<Team> teams = new ArrayList<>();
    private final CsvFileHandler csvFileHandler = new CsvFileHandler();
    private final String filePath = "C:\\Users\\HP\\IdeaProjects\\TeamMateSystem\\participants.csv";

    // Fill Survey

    public Message fillSurvey(String participantId, int questionNo, String answer){
        Participant participant = participants.get(participantId);

        // For Personality Test
        if (questionNo >= 1 && questionNo <= 5){
            if (!isValidAnswer(answer)){
                return new Message(false, "Invalid Answer");
            }
            survey.addPersonalityAnswer(participantId, questionNo, answer);
        }
        // For Interest Survey
        else if (questionNo >= 6 && questionNo <= 8) {
            if (questionNo == 6 && !isValidGame(answer)){
                return new Message(false, "Invalid Answer\nPlease choose a game from this list:" + GAMES);
            }
            else if (questionNo == 7 && !isValidSkillLevel(answer)){
                return new Message(false, "Invalid Answer");
            }
            else if (questionNo == 8 && !isValidRole(answer)) {
                return new Message(false, "Invalid Answer\nPlease choose a role from this list: " + ROLES);
            }
            survey.addInterestAnswer(participantId, questionNo, answer);
        }
        else {
            return new Message(false, "");
        }

        participant.fillSurvey(questionNo, answer);
        return new Message(true, "");
    }

    public boolean addParticipant(Participant participant){
        return participants.putIfAbsent(participant.getId(), participant) == null;
    }

    public List<String> getSurveyQuestions(){
        return survey.getQuestions();
    }

    private boolean isValidAnswer(String answer){
        try{
            int answerInt = Integer.parseInt(answer);
            return answerInt >= 1 && answerInt <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidSkillLevel(String answer){
        try{
            int answerInt = Integer.parseInt(answer);
            return answerInt >= 1 && answerInt <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidGame(String answer){
        return GAMES.contains(answer.toLowerCase());
    }

    private boolean isValidRole(String answer){
        return ROLES.contains(answer.toLowerCase());
    }

    public void setParticipantValues(String participantId){
        Participant participant = participants.get(participantId);
        Map<Integer, String> response = survey.getInterestResponses().get(participantId);

        participant.setPreferredGame(response.get(6).toLowerCase());
        participant.setSkillLevel(Integer.parseInt(response.get(7)));
        participant.setPreferredRole(response.get(8).toLowerCase());
    }

    public Message storeParticipantDetails(Participant participant) {
        try{
            csvFileHandler.writeFile(filePath, participant);
            return new Message(true, "");
        } catch (IOException e) {
            return new Message(false, "Details saving failed\nError writing file: " + e.getMessage());
        }
    }

    public String viewPersonalityType(String participantId){
        Participant participant = participants.get(participantId);
        participant.viewPersonalityType();
        return participant.getPersonalityType();
    }

    public List<String> getGAMES() {
        return GAMES;
    }

    public List<String> getROLES() {
        return ROLES;
    }


    // Login

    public boolean organizerLogin(String username, String password) {
        return organizer.login(username, password);
    }


    // Upload CSV FIle

    public Message uploadCsvFile(){
        try{
            List<String[]> fileParticipants = organizer.uploadCsvFile(filePath, csvFileHandler);
            addParticipantsFromCsv(fileParticipants);
            return new Message(true, "CSV file uploaded successfully\n" + fileParticipants.size() + " participants has been processed.");
        } catch (IOException e) {
            return new Message(false, "File upload failed\nError reading file: " + e.getMessage());
        }
    }

    public void addParticipantsFromCsv(List<String[]> fileParticipants){
        for (String[] row: fileParticipants){
            addParticipant(Participant.covertRow(row));
        }
    }



    // Initiate Team Formation

    public void initiateTeamFormation(int teamSize){
        teams.clear();
        teams.addAll(organizer.initiateTeamFormation(new ArrayList<>(participants.values()), teamSize));
    }



    // Save Formed Teams

    public Message saveFormedTeams() {
        try{
            organizer.saveFormedTeams(teams, csvFileHandler);
            return new Message(true, "formed_teams.csv file saved successfully\n" + teams.size() + " teams has been saved.");
        } catch (IOException e) {
            return new Message(false, "File saving failed\nError writing file: " + e.getMessage());
        }
    }



    // View Teams

    public List<Team> viewTeamsForParticipant(String participantId) {
        Participant participant = participants.get(participantId);
        if (participant != null){
            return participant.viewTeams(teams);
        }
        return null;
    }

    public List<Team> viewTeamsForOrganizer() {
        return organizer.viewTeams(teams);
    }


}
