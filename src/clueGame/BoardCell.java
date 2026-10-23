/*
 * BoardCell class
 * 
 * Represents an individual cell on the board. This stores the cell's
 * row, column, initial, doorway direction, and info about the room
 * centers, labels, and secret passages
 * 
 * @author Jaylene Khourn, Trisha Varadaraj
 * 
 */

package clueGame;
import java.util.Set;

public class BoardCell {

	private int row;
	private int col;
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private Set<BoardCell> adjList;
	
	// constructor 
	public BoardCell(int row, int col, char initial) {
		this.row = row;
		this.col = col;
		this.initial = initial;
		
		// default values
		this.doorDirection = DoorDirection.NONE;
		this.roomLabel = false;
		this.roomCenter = false;
		this.secretPassage = ' ';
	}
	
	// getter for the initial of the room
	public char getInitial() {
		return initial;
	}
	
	// checks if the cell contains a doorway
	public boolean isDoorway() {
	    return doorDirection != DoorDirection.NONE;
	}
	
	// returns the direction of the doorway
	public DoorDirection getDoorDirection() {
	    return doorDirection;
	}
	
	// sets the direction of the doorway
	public void setDoorDirection(DoorDirection direction) {
	    this.doorDirection = direction;
	}
	
	// checks if the cell is the center of a room
	public boolean isRoomCenter() {
	    return roomCenter;
	}

	// sets whether the cell is a room center
	public void setRoomCenter(boolean roomCenter) {
	    this.roomCenter = roomCenter;
	}
	
	// checks if the cell contains a room label
	public boolean isRoomLabel() {
	    return roomLabel;
	}
	
	// returns if the cell is a room label for the 306 tests
	public boolean isLabel() {
		return roomLabel;
	}

	// sets whether the cell contains a room label
	public void setRoomLabel(boolean roomLabel) {
	    this.roomLabel = roomLabel;
	}
	
	// returns the destination initial of a secret passage
	public char getSecretPassage() {
	    return secretPassage;
	}
	
	// sets the destination initial of a secret passage
	public void setSecretPassage(char secretPassage) {
	    this.secretPassage = secretPassage;
	}
	
}

