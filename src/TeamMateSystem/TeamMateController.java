package TeamMateSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    public Message startSurveyTasks(int noOfParticipants){
        int numThreads = Math.min(noOfParticipants, Runtime.getRuntime().availableProcessors());
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int i = 1; i <= noOfParticipants; i++) {
            Participant p = new Participant("Participant_" + i, "user" + i + "@university.edu");
            FillSurveyWorker fillSurveyWorker = new FillSurveyWorker(p, this);
            fillSurveyWorker.fillAnswers();
            executorService.submit(fillSurveyWorker);
        }

        executorService.shutdown();
        try {
            if (executorService.awaitTermination(5, TimeUnit.MINUTES)) {
                return new Message(true, Color.GREEN + "All survey tasks completed!" + Color.RESET);
            } else {
                return new Message(false, Color.RED + "Timeout reached before all tasks finished." + Color.RESET);
            }
        } catch (InterruptedException e) {
            return new Message(false, Color.RED + "Thread interrupted while waiting for survey tasks!" + Color.RESET);
        }
    }

    public Message fillSurvey(String participantId, int questionNo, String answer){
        Participant participant = participants.get(participantId);

        // For Personality Test
        if (questionNo >= 1 && questionNo <= 5){
            if (!isValidAnswer(answer)){
                return new Message(false, Color.RED + "Invalid Answer!" + Color.RESET);
            }
            survey.addPersonalityAnswer(participantId, questionNo, answer);
        }
        // For Interest Survey
        else if (questionNo >= 6 && questionNo <= 8) {
            if (questionNo == 6 && !isValidGame(answer)){
                return new Message(false, Color.RED + "Invalid Answer\nPlease choose a game from this list: " + Color.RESET + GAMES);
            }
            else if (questionNo == 7 && !isValidSkillLevel(answer)){
                return new Message(false, Color.RED + "Invalid Answer" + Color.RESET);
            }
            else if (questionNo == 8 && !isValidRole(answer)) {
                return new Message(false, Color.RED + "Invalid Answer\nPlease choose a role from this list: " + Color.RESET + ROLES);
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
            return new Message(false, Color.RED + "Details saving failed\nError writing file: " + e.getMessage() + Color.RESET);
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
            return new Message(true, Color.GREEN + "CSV file uploaded successfully\n" + fileParticipants.size() + " participants has been processed." + Color.RESET);
        } catch (IOException e) {
            return new Message(false, Color.RED + "File upload failed\nError reading file: " + e.getMessage() + Color.RESET);
        }
    }

    public void addParticipantsFromCsv(List<String[]> fileParticipants){
        for (String[] row: fileParticipants){
            addParticipant(Participant.covertRow(row));
        }
    }



    // Initiate Team Formation

    public Message initiateTeamFormation(int teamSize){
        if (!participants.isEmpty()){
            Message message = checkTeamSize(teamSize, participants.size());
            if (message.isSuccess()){
                teams.clear();
                teams.addAll(organizer.initiateTeamFormation(new ArrayList<>(participants.values()), teamSize));
                return new Message(true, Color.GREEN + teams.size() + " Teams Formed!" + Color.RESET);
            }
            return message;
        }
        return new Message(false, Color.RED + "No participants available to form teams!" + Color.RESET);
    }

    public Message checkTeamSize(int teamSize, int noOfParticipants){
        if (teamSize <= 1){
            return new Message(false, Color.RED + "Teams size cannot be less than 2!" + Color.RESET);
        }
        else if (teamSize >= noOfParticipants){
            return new Message(false, Color.RED + "Team size cannot be greater than the no of participants!" + Color.RESET);
        }
        return new Message(true, "");
    }



    // Save Formed Teams

    public Message saveFormedTeams() {
        if (!teams.isEmpty()){
            try{
                organizer.saveFormedTeams(teams, csvFileHandler);
                return new Message(true, Color.GREEN + "formed_teams.csv file saved successfully\n" + teams.size() + " teams has been saved." + Color.RESET);
            } catch (IOException e) {
                return new Message(false, Color.RED + "File saving failed\nError writing file: " + e.getMessage() + Color.RESET);
            }
        }
        return new Message(false, Color.RED + "No teams were formed!" + Color.RESET);
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
