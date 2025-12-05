package TeamMateSystem;

/**
 * The Account class represents an organizer account with a username and a password.
 * It is used for managing user authentication details within the system.
 */
public class Account {
    /** The username of the account. */
    private final String username;
    /** The password of the account. */
    private final String password;

    /**
     * Constructs an Account object with a specified username and password.
     *
     * @param username The username for the account.
     * @param password The password for the account.
     */
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Retrieves the username of this account.
     * @return The username of the account.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Checks whether the provided password matches the stored password.
     * @param inputPassword The password entered by the user.
     * @return true if the password is correct, false otherwise.
     */
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}
