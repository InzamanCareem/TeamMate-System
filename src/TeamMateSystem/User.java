package TeamMateSystem;

import java.util.List;

/**
 * Abstract class representing a system user.
 * Both {@link Participant} and {@link Organizer} extend this class.
 */
public abstract class User {

    /**
     * Returns the list of teams accessible to the user.
     *
     * @param teams List of all teams in the system.
     * @return List of {@link Team} objects that the user can view.
     */
    public abstract List<Team> viewTeams(List<Team> teams);
}
