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

import java.util.HashSet;
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
    private boolean occupied = false;
    private Card roomCard; // Used for testing target selection

    // Main constructor used by Board layout loader
    public BoardCell(int row, int col, char initial) {
        this.row = row;
        this.col = col;
        this.initial = initial;

        this.doorDirection = DoorDirection.NONE;
        this.roomLabel = false;
        this.roomCenter = false;
        this.secretPassage = ' ';
        this.adjList = new HashSet<>();
    }

    // Secondary constructor for standalone AI unit testing
    public BoardCell(int row, int col, Card roomCard) {
        this(row, col, 'R');
        this.roomCard = roomCard;
        this.roomCenter = true;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getInitial() {
        return initial;
    }

    public boolean isDoorway() {
        return doorDirection != DoorDirection.NONE;
    }

    public DoorDirection getDoorDirection() {
        return doorDirection;
    }

    public void setDoorDirection(DoorDirection direction) {
        this.doorDirection = direction;
    }

    public boolean isRoomCenter() {
        return roomCenter;
    }

    public void setRoomCenter(boolean roomCenter) {
        this.roomCenter = roomCenter;
    }

    public boolean isRoomLabel() {
        return roomLabel;
    }

    public boolean isLabel() {
        return roomLabel;
    }

    public void setRoomLabel(boolean roomLabel) {
        this.roomLabel = roomLabel;
    }

    public char getSecretPassage() {
        return secretPassage;
    }

    public void setSecretPassage(char secretPassage) {
        this.secretPassage = secretPassage;
    }

    public void addAdjacency(BoardCell cell) {
        adjList.add(cell);
    }

    public Set<BoardCell> getAdjList() {
        return adjList;
    }

    public boolean isWalkway() {
        return initial == 'W';
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isRoom() {
        return roomCard != null || (initial != 'W' && initial != 'X');
    }

    public Card getRoomCard() {
        return roomCard;
    }
    
    public void setRoomCard(Card card) {
        this.roomCard = card;
    }
}