package TeamMateSystem;

import java.io.*;
import java.util.*;

public class CsvFileHandler {
    public List<String[]> readFile(String filePath) throws IOException{
        List<String[]> file = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                file.add(line.split(","));
            }
        }
        return file;
    }

    public synchronized void writeFile(String filePath, Participant participant) throws IOException{
        File file = new File(filePath);
        boolean participantsFileExist = file.exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            if (!participantsFileExist){
                writer.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType");
                writer.newLine();
            }
            writer.write(participant.toString());
            writer.newLine();
        }
    }

    public void saveFile(List<Team> teams) throws IOException{
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("formed_teams.csv"))) {
            for (Team team: teams){
                writer.write(team.toString());
                writer.newLine();
                for (Participant participant: team.getParticipants()){
                    writer.write(participant.details());
                    writer.newLine();
                }
                writer.newLine();
            }
        }
    }
}
