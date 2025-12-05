package TeamMateSystem;

/**
 * The {@code Message} class represents a response result used throughout the
 * TeamMateSystem to indicate whether an operation succeeded and to provide
 * an associated message explaining the outcome.
 *
 * <p>This class is used for operations such as form validation,
 * survey submission, and controller feedback.</p>
 *
 * <p>Example:</p>
 * <pre>
 *     Message msg = controller.fillSurvey(id, questionNo, answer);
 *     if (msg.isSuccess()) {
 *         System.out.println("Answer accepted!");
 *     } else {
 *         System.out.println("Error: " + msg.getMessage());
 *     }
 * </pre>
 */
public class Message {

    /** Indicates whether a certain operation was successful. */
    private final boolean success;

    /** A descriptive message explaining the result of the operation. */
    private final String message;

    /**
     * Constructs a new {@code Message} object.
     *
     * @param success Whether the operation succeeded.
     * @param message A descriptive message providing details about the result.
     */
    public Message(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Returns whether the operation succeeded.
     *
     * @return {@code true} if successful, {@code false} otherwise.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the message describing the result of the operation.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }
}
