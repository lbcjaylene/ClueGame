/**
 * TestBoardCell Class
 *
 * Represents an individual cell on the Clue game board
 * Stores the cell's row and column, adjacent cells,
 * and whether the cell is a room or occupied.
 *
 * @author Jaylene Khourn, Trisha Varadaraj
 * 
 */

package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {

    private int row;
    private int col;

    // Flags for special cell types
    private boolean rooms;
    private boolean occupied;

    private Set<TestBoardCell> neighbors;

    public TestBoardCell(int rowNum, int colNum) {
        this.row = rowNum;
        this.col = colNum;
        this.rooms = false;
        this.occupied = false;
        this.neighbors = new HashSet<>();
    }

    // Add a neighboring cell to the adjacency list
    public void addAdjacency(TestBoardCell cell) {
        neighbors.add(cell);
    }

    // returns the set of adjacency cells
    public Set<TestBoardCell> getAdjList() {
        return neighbors;
    }
    
    // sets whether this cell is a room
    public void setRoom(boolean isRoom) {
        this.rooms = isRoom;
    }

    // Check if room .    
    public boolean isRoom() {
        return rooms;
    }

    // Set whether this cell has another player on it .    
    public void setOccupied(boolean isOccupied) {
        this.occupied = isOccupied;
    }

    // Check if cell is occupied .             
    public boolean getOccupied() {
        return occupied;
    }

    // Getter for row coordinate
    public int getRow() {
        return row;
    }

    // Getter for column coordinate
    public int getCol() {
        return col;
    }
}