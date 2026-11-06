/*
 * ComputerPlayer class
 * 
 * Represents a computer controlled player in the game. Inherits 
 * player info and functionality from Player. It handles the computer
 * player decisions and movements
 * 
 * @author Jaylene Khourn, Trisha Varadaraj
 * 
 */

package clueGame;

public class ComputerPlayer extends Player {
	
	// constructor passes player info to the parent class
	public ComputerPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}
}
