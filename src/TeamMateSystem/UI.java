package TeamMateSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class UI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TeamMateController teamMateController = new TeamMateController();

        outer: while (true){
            System.out.println("1. Login (if Organizer)\n2. Fill Survey\n3. View Teams\n4. Exit");
            System.out.print("Enter the command: ");
            int command = scanner.nextInt();

            scanner.nextLine();

            boolean organizer = false;  // default participant

            switch (command){
                case 1 -> {
                    System.out.print("Enter the username: ");
                    String username = scanner.nextLine();

                    System.out.print("Enter the password: ");
                    String password = scanner.nextLine();

                    boolean loggedIn = teamMateController.organizerLogin(username, password);

                    if (loggedIn){
                        organizer = true;
                        System.out.println("You are logged in as an organizer");
                    }
                    else{
                        System.out.println("Incorrect username or password");
                    }
                }

                case 2 -> {
//                    System.out.print("Enter the participant name: ");
//                    String name = scanner.nextLine();
//
//                    System.out.print("Enter the participant email: ");
//                    String email = scanner.nextLine();
//
//                    Participant participant = new Participant(name, email);
//
//                    boolean participantAdded = teamMateController.addParticipant(participant);
//
//                    if (participantAdded){
//                        System.out.println("Your participant id is: " + participant.getId());
//                        int questionNo = 1;
//                        for (String question: teamMateController.getSurveyQuestions()){
//                            boolean accepted = true;
//                            while (accepted){
//                                System.out.print(question);
//                                String answer = scanner.next();
//                                Message message = teamMateController.fillSurvey(participant.getId(), questionNo, answer);
//                                accepted = !message.isSuccess();
//                                if (accepted) {
//                                    System.out.println(message.getMessage());
//                                }
//                            }
//                            questionNo++;
//                        }
//                        teamMateController.setParticipantValues(participant.getId());
//                        System.out.println("Your personality: " + teamMateController.viewPersonalityType(participant.getId()));
//
//                        Message message = teamMateController.storeParticipantDetails(participant);
//                        if (!message.isSuccess()){
//                            System.out.println(message.getMessage());
//                        }
//                    }
//                    else{
//                        System.out.println("Participant already exists");
//                    }

                    // Using Threads and concurrency

                    int noOfParticipants = 100;

                    ExecutorService executorService = Executors.newFixedThreadPool(10);

                    for (int i = 1; i <= noOfParticipants; i++) {
                        Participant p = new Participant("Participant_" + i, "user" + i + "@university.edu");
                        FillSurveyWorker fillSurveyWorker = new FillSurveyWorker(p, teamMateController);
                        fillSurveyWorker.fillAnswers();
                        executorService.submit(fillSurveyWorker);
                    }

                    executorService.shutdown();
                    try {
                        if (executorService.awaitTermination(5, TimeUnit.MINUTES)) {
                            System.out.println("All survey tasks completed!");
                        } else {
                            System.out.println("Timeout reached before all tasks finished.");
                        }
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted while waiting for survey tasks.");
                    }
                }

                case 3 -> {
                    System.out.print("Enter participant id: ");
                    String id = scanner.nextLine();

                    List<Team> teams = teamMateController.viewTeamsForParticipant(id);

                    if (teams != null){
                        for (Team team: teams){
                            team.displayTeam();
                        }
                    }
                    else{
                        System.out.println("Incorrect participant id");
                    }
                }

                case 4 -> {
                    break outer;
                }

                default -> System.out.println("Invalid command");
            }



//            boolean organizer = true;

            while (organizer){
                System.out.println("1. Upload CSV File\n2. Initiate Team Formation\n3. Save Formed Teams\n4. View Teams\n5. Logout\n6. Exit");
                System.out.print("Enter the command: ");
                int orgCommand = scanner.nextInt();

                scanner.nextLine();

                switch (orgCommand){
                    case 1 -> {
                        Message message = teamMateController.uploadCsvFile();
                        System.out.println(message.getMessage());
                    }

                    case 2 -> {
                        System.out.print("Enter the team size: ");
                        int teamSize = scanner.nextInt();

                        teamMateController.initiateTeamFormation(teamSize);
                    }

                    case 3 -> {
                        Message message = teamMateController.saveFormedTeams();
                        System.out.println(message.getMessage());
                    }

                    case 4 -> {
                        for (Team team: teamMateController.viewTeamsForOrganizer()){
                            team.displayTeam();
                            System.out.println();
                        }
                    }

                    case 5 -> organizer = false;

                    case 6 -> {
                        break outer;
                    }

                    default -> System.out.println("Invalid command");
                }
            }
        }

        scanner.close();
    }
}
