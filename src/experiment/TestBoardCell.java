/**
 * TestBoardCell Class
 * 
 * Represents an individual cell on a 4x4 Clue game board. Each cell stores its row/col 
 * coordinates and tracks whether it is a room or occupied.
 * 
 * @authors Jaylene Khourn, Trisha Varadaraj
 * 
 */

package experiment;
import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {
	
	// constructor 
	private int row;
	private int col;
	private boolean isRoom;
	private boolean isOccupied;
	
	public TestBoardCell(int row, int col) {  // accepts row/column coordinates .    
		this.row =row;  // Assigns row parameter to instance variable row .    
		this.col = col;
	}
	
	// setter to add a cell 
	
	public void addAdjacency(TestBoardCell cell) {
		
	}
	
	// NEW
	
	public Set<TestBoardCell> getAdjList(){
		return new HashSet<>();
	}
	public void setRoom(boolean room) {
        this.isRoom= room;
    }

    public boolean isRoom() {
        return isRoom;
    }

    public void setOccupied(boolean occupied) {  // To mark occupied or empty .    
        this.isOccupied = occupied;
    }

    public boolean getOccupied(){
        return isOccupied;
    }
}
	

