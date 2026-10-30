/*
 * Room Class
 * 
 * Represents a room on the board. It stores the room's name 
 * and references to the center of the room and label cells
 * 
 * @author Jaylene Khourn, Trisha Varadaraj
 * 
 */

package clueGame;

public class Room {

	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;

	// constructor
	public Room(String name) {
		this.name = name;
	}

	// return the name of the room
	public String getName() {
		return name;
	}

	// returns the center cell of the room
	public BoardCell getCenterCell() {
		return centerCell;
	}

	// sets the center cell of the room
	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	// returns the label cell of the room
	public BoardCell getLabelCell() {
		return labelCell;
	}

	// sets the label cell of the room
	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}
}
