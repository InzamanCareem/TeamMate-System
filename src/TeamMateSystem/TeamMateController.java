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

/**
 * The {@code TeamMateController} class acts as the main controller for the
 * TeamMateSystem. It manages participants, surveys, teams, and interactions
 * with the organizer. It also handles concurrent survey filling and team formation.
 */
public class TeamMateController {

    /** Stores all participants by their unique ID. */
    private final ConcurrentHashMap<String, Participant> participants = new ConcurrentHashMap<>();

    /** Survey object that contains questions and stores participant responses. */
    private final Survey survey = new Survey();

    /** Organizer account for managing CSV uploads and team formation. */
    private final Organizer organizer = new Organizer("admin", "123");

    /** Predefined list of games participants can choose from. */
    private final List<String> GAMES = new ArrayList<>(Arrays.asList("chess", "fifa", "basketball", "cs:go", "dota 2", "valorant"));

    /** Predefined list of roles participants can choose from. */
    private final List<String> ROLES = new ArrayList<>(Arrays.asList("strategist", "attacker", "defender", "supporter", "coordinator"));

    /** Stores the teams formed after team formation. */
    private final List<Team> teams = new ArrayList<>();

    /** CSV file handler to read/write participant and team data. */
    private final CsvFileHandler csvFileHandler = new CsvFileHandler();

    /** Default file path for participants.csv file. */
    private final String filePath = "C:\\Users\\HP\\IdeaProjects\\TeamMateSystem\\participants.csv";

    /**
     * Starts concurrent survey tasks for a given number of participants.
     *
     * @param noOfParticipants Number of participants to simulate survey filling.
     * @return A {@link Message} indicating success or failure.
     */
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

    /**
     * Fills a survey answer for a participant.
     *
     * @param participantId Participant's unique ID.
     * @param questionNo    Question number being answered.
     * @param answer        Answer provided by the participant.
     * @return {@link Message} indicating whether the answer was valid and accepted.
     */
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

    /**
     * Adds a participant to the system if they don't already exist.
     *
     * @param participant The participant to add.
     * @return true if added successfully, false if participant already exists.
     */
    public boolean addParticipant(Participant participant){
        return participants.putIfAbsent(participant.getId(), participant) == null;
    }

    /**
     * Removes a participant from the system.
     *
     * @param participantId The participant to remove.
     */
    public void removeParticipant(String participantId){
        participants.remove(participantId);
    }

    /**
     * Returns the list of survey questions.
     *
     * @return List of survey questions.
     */
    public List<String> getSurveyQuestions(){
        return survey.getQuestions();
    }

    /**
     * Validates answer.
     *
     * @param answer the answer to validate.
     * @return true if answer correct, false if answer incorrect.
     */
    private boolean isValidAnswer(String answer){
        try{
            int answerInt = Integer.parseInt(answer);
            return answerInt >= 1 && answerInt <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates skill level.
     *
     * @param answer the answer to validate.
     * @return true if answer correct, false if answer incorrect.
     */
    private boolean isValidSkillLevel(String answer){
        try{
            int answerInt = Integer.parseInt(answer);
            return answerInt >= 1 && answerInt <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates game.
     *
     * @param answer the answer to validate.
     * @return true if answer correct, false if answer incorrect.
     */
    private boolean isValidGame(String answer){
        return GAMES.contains(answer.toLowerCase());
    }

    /**
     * Validates role.
     *
     * @param answer the answer to validate.
     * @return true if answer correct, false if answer incorrect.
     */
    private boolean isValidRole(String answer){
        return ROLES.contains(answer.toLowerCase());
    }

    /**
     * Sets the participant's game, skill level, and preferred role based on survey responses.
     *
     * @param participantId Participant's unique ID.
     */
    public void setParticipantValues(String participantId){
        Participant participant = participants.get(participantId);
        Map<Integer, String> response = survey.getInterestResponses().get(participantId);

        participant.setPreferredGame(response.get(6).toLowerCase());
        participant.setSkillLevel(Integer.parseInt(response.get(7)));
        participant.setPreferredRole(response.get(8).toLowerCase());
    }

    /**
     * Stores participant details into the CSV file.
     *
     * @param participant Participant to store.
     * @return {@link Message} indicating success or failure.
     */
    public Message storeParticipantDetails(Participant participant) {
        try{
            csvFileHandler.writeFile(filePath, participant);
            return new Message(true, "");
        } catch (IOException e) {
            return new Message(false, Color.RED + "Details saving failed\nError writing file: " + e.getMessage() + Color.RESET);
        }
    }

    /**
     * Calculates and returns the personality type of a participant.
     *
     * @param participantId Participant's unique ID.
     * @return Personality type as a string.
     */
    public String viewPersonalityType(String participantId){
        Participant participant = participants.get(participantId);
        participant.viewPersonalityType();
        return participant.getPersonalityType();
    }

    /**
     * Returns the list of available games.
     *
     * @return List of available games.
     */
    public List<String> getGAMES() {
        return GAMES;
    }

    /**
     * Returns the list of available roles.
     *
     * @return List of available roles.
     */
    public List<String> getROLES() {
        return ROLES;
    }

    /**
     * Logs in the organizer with the given credentials.
     *
     * @param username Organizer username.
     * @param password Organizer password.
     * @return true if login successful; false otherwise.
     */
    public boolean organizerLogin(String username, String password) {
        return organizer.login(username, password);
    }

    /**
     * Uploads participants from a CSV file and adds them to the system.
     *
     * @return {@link Message} indicating success or failure of CSV upload.
     */
    public Message uploadCsvFile(){
        try{
            List<String[]> fileParticipants = organizer.uploadCsvFile(filePath, csvFileHandler);
            addParticipantsFromCsv(fileParticipants);
            return new Message(true, Color.GREEN + "CSV file uploaded successfully\n" + fileParticipants.size() + " participants has been processed." + Color.RESET);
        } catch (IOException e) {
            return new Message(false, Color.RED + "File upload failed\nError reading file: " + e.getMessage() + Color.RESET);
        }
    }

    /**
     * Adds participants from a list of CSV rows.
     *
     * @param fileParticipants List of CSV rows representing participants.
     */
    public void addParticipantsFromCsv(List<String[]> fileParticipants){
        for (String[] row: fileParticipants){
            addParticipant(Participant.covertRow(row));
        }
    }

    /**
     * Initiates team formation based on the current participants.
     *
     * @param teamSize Size of each team.
     * @return {@link Message} indicating success or failure.
     */
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

    /**
     * Checks if a team size is valid for the number of participants.
     *
     * @param teamSize        Proposed team size.
     * @param noOfParticipants Number of participants available.
     * @return {@link Message} indicating validity of the team size.
     */
    public Message checkTeamSize(int teamSize, int noOfParticipants){
        if (teamSize <= 1){
            return new Message(false, Color.RED + "Teams size cannot be less than 2!" + Color.RESET);
        }
        else if (teamSize >= noOfParticipants){
            return new Message(false, Color.RED + "Team size cannot be greater than the no of participants!" + Color.RESET);
        }
        return new Message(true, "");
    }

    /**
     * Saves all formed teams to a CSV file.
     *
     * @return {@link Message} indicating success or failure.
     */
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

    /**
     * Returns the team a participant belongs to.
     *
     * @param participantId Participant's unique ID.
     * @return List of {@link Team} objects or null if participant not found.
     */
    public List<Team> viewTeamsForParticipant(String participantId) {
        Participant participant = participants.get(participantId);
        if (participant != null){
            return participant.viewTeams(teams);
        }
        return null;
    }

    /**
     * Returns all formed teams for the organizer.
     *
     * @return List of all {@link Team} objects.
     */
    public List<Team> viewTeamsForOrganizer() {
        return organizer.viewTeams(teams);
    }
}
