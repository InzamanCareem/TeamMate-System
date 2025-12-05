package TeamMateSystem;

import java.io.*;
import java.util.*;

/**
 * The {@code CsvFileHandler} class provides utility methods for reading and writing
 * CSV files used in the TeamMateSystem. It supports:
 *
 * <ul>
 *     <li>Reading CSV files and converting each line into a {@code String[]}.</li>
 *     <li>Appending participant details to a CSV file (with header creation if file does not initially exist).</li>
 *     <li>Saving formed teams and their participants to a CSV file.</li>
 * </ul>
 *
 */
public class CsvFileHandler {

    /**
     * Reads a CSV file and returns its contents as a list of string arrays.
     * Each array represents one line split by commas.
     *
     * <p>The first line (header) is skipped automatically.</p>
     *
     * @param filePath The path to the CSV file.
     * @return A list of rows, each row stored as a {@code String[]} split by commas.
     * @throws IOException If the file cannot be opened or read.
     */
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

    /**
     * Appends a participant's data to the specified CSV file.
     * If the file does not exist, this method will create it and write a header row first.
     *
     * <p>This method is synchronized to prevent multiple threads from writing
     * simultaneously and corrupting the file.</p>
     *
     * @param filePath    The path to the CSV file.
     * @param participant The participant to write.
     * @throws IOException If the file cannot be opened or written to.
     */
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

    /**
     * Saves all formed teams into a CSV file named {@code formed_teams.csv}.
     * This includes:
     *
     * <ul>
     *     <li>The team's name and id</li>
     *     <li>Each participant's id, name and email</li>
     * </ul>
     *
     * A blank line is written between teams for readability.
     *
     * @param teams The list of teams to save.
     * @throws IOException If writing to the file fails.
     */
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
