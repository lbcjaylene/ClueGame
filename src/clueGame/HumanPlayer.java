/*
 * HumanPlayer Class
 * 
 * Represents the human controlled player in the game. Inherits player
 * info and functionality from Player
 * 
 * @author Jaylene Khourn, Trisha Varadaraj
 * 
 */

package clueGame;

public class HumanPlayer extends Player {
	
	// constructor passes player info to the parent class
	public HumanPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}
}
