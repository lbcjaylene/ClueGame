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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
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
    private Card roomCard; // used for testing target selection

    // main constructor used by Board layout loader
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

    // secondary constructor for standalone AI unit testing
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

    // returns the room card used for computer movement tests
    public Card getRoomCard() {
        return roomCard;
    }
    
    public void setRoomCard(Card card) {
        this.roomCard = card;
    }
    
    // draws an individual board cell
    public void draw(Graphics g, int cellSize, int xOffset, int yOffset) {
        int x = xOffset + col * cellSize;
        int y = yOffset + row * cellSize;

        // set the cell color based on its type
        if (isWalkway()) {
            g.setColor(Color.LIGHT_GRAY);
        } else if (initial == 'X') {
            g.setColor(Color.DARK_GRAY);
        } else {
            g.setColor(new Color(180, 220, 180));
        }

        // draw the cell
        g.fillRect(x, y, cellSize, cellSize);

        // draw grid lines for walkways
        if (isWalkway()) {
            g.setColor(Color.BLACK);
            g.drawRect(x, y, cellSize, cellSize);
        }

        // draw doorways using a thick blue line
        if (isDoorway()) {
            g.setColor(Color.BLUE);
            switch (doorDirection) {
                case UP:
                    g.fillRect(x, y, cellSize, 4);
                    break;
                case DOWN:
                    g.fillRect(x, y + cellSize - 4, cellSize, 4);
                    break;
                case LEFT:
                    g.fillRect(x, y, 4, cellSize);
                    break;
                case RIGHT:
                    g.fillRect(x + cellSize - 4, y, 4, cellSize);
                    break;

                default:
                    break;
            }
        }
    }

 // Draws the room name inside its room boundaries
    public void drawName(Graphics g, int cellSize, int xOffset, int yOffset) {
        if (!roomLabel || cellSize <= 0) {
            return;
        }

        Board board = Board.getInstance();
        Room room = board.getRoom(initial);

        if (room == null) {
            return;
        }

        String roomName = room.getName();

        // Find the room boundaries along the label row
        int leftCol = col;
        int rightCol = col;

        while (leftCol > 0 &&
               board.getCell(row, leftCol - 1).getInitial() == initial) {
            leftCol--;
        }

        while (rightCol < board.getNumColumns() - 1 &&
               board.getCell(row, rightCol + 1).getInitial() == initial) {
            rightCol++;
        }

        // Calculate available space inside the room
        int leftX = xOffset + leftCol * cellSize;
        int roomWidth = (rightCol - leftCol + 1) * cellSize;
        int availableWidth = Math.max(1, roomWidth - 4);

        // Start with a font proportional to the cell size
        int fontSize = Math.max(1, cellSize / 3);
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g.setFont(font);

        FontMetrics metrics = g.getFontMetrics();

        // Shrink the font until the name fits inside the room
        while (metrics.stringWidth(roomName) > availableWidth
                && fontSize > 1) {
            fontSize--;
            font = new Font("Arial", Font.BOLD, fontSize);
            g.setFont(font);
            metrics = g.getFontMetrics();
        }

        // Center the name within the room boundaries
        int textWidth = metrics.stringWidth(roomName);
        int x = leftX + (roomWidth - textWidth) / 2;

        // Center the text vertically in the label cell
        int centerY = yOffset + row * cellSize + cellSize / 2;
        int y = centerY +
                (metrics.getAscent() - metrics.getDescent()) / 2;

        // Draw the room name
        g.setColor(Color.BLACK);
        g.drawString(roomName, x, y);
    }
    
    
    
}



