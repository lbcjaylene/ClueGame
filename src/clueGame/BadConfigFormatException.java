/*
 * BadConfigFormatException
 * 
 * Custom exceptions used when the configuration file contains
 * invalid formatting, inconsistent columns, or undefined room initials
 * 
 * @author Jaylene Khourn, Trisha Varadaraj
 * 
 */

package clueGame;

public class BadConfigFormatException extends Exception {

    // default constructor when no error msg is provided
    public BadConfigFormatException() {
        super("Invalid configuration file format.");
    }

    // constructor that accepts an error message
    public BadConfigFormatException(String message) {
        super(message);
    }
}
