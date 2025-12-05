package TeamMateSystem;

/**
 * The {@code Color} class provides ANSI escape codes for coloring text output
 * in terminals that support ANSI color formatting. These constants can be used
 * to add color to console messages and reset the color formatting afterward for
 * useful debugging purposes.
 *
 * <p>Example usage:</p>
 * <pre>
 *     System.out.println(Color.RED + "message" + Color.RESET);
 * </pre>
 */
public class Color {
    /** ANSI escape code to reset text formatting to default. */
    public static final String RESET  = "\u001B[0m";

    /** ANSI escape code for red-colored text. */
    public static final String RED    = "\u001B[31m";

    /** ANSI escape code for green-colored text. */
    public static final String GREEN  = "\u001B[32m";

    /** ANSI escape code for blue-colored text. */
    public static final String BLUE   = "\u001B[34m";
}
