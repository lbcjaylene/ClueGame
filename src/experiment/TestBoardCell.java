package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {

    private int cellRows;
    private int CellColumn;

    // Flags for special cell types
    private boolean rooms;
    private boolean occupied;

    private Set<TestBoardCell> neighbors;

    public TestBoardCell(int rowNum, int colNum) {
        this.cellRows = rowNum;
        this.CellColumn = colNum;
        this.rooms = false;
        this.occupied = false;
        this.neighbors = new HashSet<>();
    }

    // Add a neighbor cell to cell's adjacency set
    public void addAdjacency(TestBoardCell cell) {
        neighbors.add(cell);
    }

    
    public Set<TestBoardCell> getAdjList() {
        return neighbors;
    }

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
        return cellRows;
    }

    // Getter for column coordinate
    public int getCol() {
        return CellColumn;
    }
}