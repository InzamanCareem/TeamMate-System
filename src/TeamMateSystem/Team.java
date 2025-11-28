package TeamMateSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Team {
    private int id;
    private final String name;
    private final List<Participant> team = new CopyOnWriteArrayList<>();

    public Team(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addParticipant(Participant participant){
        team.add(participant);
    }

    public List<Participant> getParticipants(){
        return team;
    }

    public void displayTeam(){
        System.out.println(this);
        for (Participant participant: this.team){
            System.out.println(participant.details());
        }
    }

    @Override
    public String toString() {
        return this.name + " " + this.id + ":";
    }
}
