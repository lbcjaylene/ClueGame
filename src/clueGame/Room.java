package clueGame;

public class Room {
	
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	
	// constructor to create a room with a name
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

