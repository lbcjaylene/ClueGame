/*
 * Player Class
 * 
 * Abstract parent class for the human and computer players.
 * Stores player info such as their name, color, location, and the
 * cards that are held by the player
 * 
 * @author Jaylene Khourn, Trisha Varadaraj
 * 
 */

package clueGame;

public abstract class Player {
	private String name;
	private String color;
	private int row;
	private int column;
	
	// constructor initializes the player's info
	public Player(String name, String color, int row, int column) {
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
	}
	
	// returns the player's name
	public String getName() {
		return name;
	}
	
	// returns the player's color 
	public String getColor() {
		return color;
	}
	
	// returns the player's current row
	public int getRow() {
		return row;
	}
	
	// returns the player's current column
	public int getColumn() {
		return column;
	}
}
