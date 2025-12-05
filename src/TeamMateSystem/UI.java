package TeamMateSystem;

import java.util.List;
import java.util.Scanner;

public class UI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TeamMateController teamMateController = new TeamMateController();

        outer: while (true){
            System.out.println("1. Login (if Organizer)\n2. Fill Survey\n3. View Teams\n4. Exit");
            System.out.print("Enter the command: ");

            while (!scanner.hasNextInt()){
                System.out.println(Color.RED + "Invalid command" + Color.RESET);
                scanner.nextLine();
                System.out.print("Enter the command: ");
            }

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
                        System.out.println(Color.BLUE + "You are logged in as an organizer" + Color.RESET);
                    }
                    else{
                        System.out.println(Color.RED + "Incorrect username or password" + Color.RESET);
                    }
                }

                case 2 -> {
                    // Using Threads and concurrency

                    int noOfParticipants = 10;

                    Message message = teamMateController.startSurveyTasks(noOfParticipants);
                    System.out.println(message.getMessage());
                }

                case 3 -> {
                    System.out.print("Enter participant id: ");
                    String id = scanner.nextLine();

                    List<Team> teams = teamMateController.viewTeamsForParticipant(id);

                    if (teams != null){
                        System.out.println("This is your team: ");
                        for (Team team: teams){
                            team.displayTeam();
                        }
                    }
                    else{
                        System.out.println(Color.RED + "Incorrect participant id" + Color.RESET);
                    }
                }

                case 4 -> {
                    break outer;
                }

                default -> System.out.println(Color.RED + "Invalid command" + Color.RESET);
            }

            while (organizer){
                System.out.println("1. Upload CSV File\n2. Initiate Team Formation\n3. Save Formed Teams\n4. View Teams\n5. Logout\n6. Exit");
                System.out.print("Enter the command: ");

                while (!scanner.hasNextInt()){
                    System.out.println(Color.RED + "Invalid command" + Color.RESET);
                    scanner.nextLine();
                    System.out.print("Enter the command: ");
                }

                int orgCommand = scanner.nextInt();

                scanner.nextLine();

                switch (orgCommand){
                    case 1 -> {
                        Message message = teamMateController.uploadCsvFile();
                        System.out.println(message.getMessage());
                    }

                    case 2 -> {
                        System.out.print("Enter the team size: ");

                        while (!scanner.hasNextInt()){
                            System.out.println(Color.RED + "Team size should be a number!" + Color.RESET);
                            scanner.nextLine();
                            System.out.print("Enter the command: ");
                        }

                        int teamSize = scanner.nextInt();

                        Message message = teamMateController.initiateTeamFormation(teamSize);
                        System.out.println(message.getMessage());
                    }

                    case 3 -> {
                        Message message = teamMateController.saveFormedTeams();
                        System.out.println(message.getMessage());
                    }

                    case 4 -> {
                        List<Team> teams = teamMateController.viewTeamsForOrganizer();
                        if (!teams.isEmpty()){
                            for (Team team: teams){
                                team.displayTeam();
                                System.out.println();
                            }
                        }
                        else{
                            System.out.println(Color.RED + "Teams were not formed!" + Color.RESET);
                        }
                    }

                    case 5 -> organizer = false;

                    case 6 -> {
                        break outer;
                    }

                    default -> System.out.println(Color.RED + "Invalid command" + Color.RESET);
                }
            }
        }

        scanner.close();
    }
}
