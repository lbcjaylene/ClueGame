package clueGame;

public class BadConfigFormatException extends Exception {

    // Default constructor
    public BadConfigFormatException() {
        super("Invalid configuration file format.");
    }

    // Constructor that accepts an error message
    public BadConfigFormatException(String message) {
        super(message);
    }
}
