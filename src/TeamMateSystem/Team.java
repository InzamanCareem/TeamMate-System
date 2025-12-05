package TeamMateSystem;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The {@code Team} class represents a group of participants within the
 * TeamMateSystem. Teams store participants and provide methods to add members
 * and display team information.
 *
 * <p>A {@link CopyOnWriteArrayList} is used to safely handle concurrent
 * modifications when participants are added in parallel.</p>
 */
public class Team {

    /** Unique identifier for the team. */
    private int id;

    /** Name of the team. */
    private final String name;

    /** List of participants in this team (thread-safe). */
    private final List<Participant> team = new CopyOnWriteArrayList<>();

    /**
     * Constructs a new {@code Team} with the given ID and name.
     *
     * @param id   The unique team identifier.
     * @param name The team name.
     */
    public Team(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Adds a participant to this team.
     *
     * @param participant The participant to add.
     */
    public void addParticipant(Participant participant){
        team.add(participant);
    }

    /**
     * Returns the list of participants in this team.
     *
     * @return A thread-safe list of participants.
     */
    public List<Participant> getParticipants(){
        return team;
    }

    /**
     * Displays the team details and all its participants to the console.
     */
    public void displayTeam(){
        System.out.println(this);
        for (Participant participant: this.team){
            System.out.println(participant.details());
        }
    }

    /**
     * Returns a string representation of the team (name and ID).
     *
     * @return A string in the format: "TeamName ID:".
     */
    @Override
    public String toString() {
        return this.name + " " + this.id + ":";
    }
}
