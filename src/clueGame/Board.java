/*
 * Board Class
 * 
 * Represents the board using the singleton design patterns.
 * Reads the setup and layout configuration files, creates the board
 * cells, store information about each room, doorway, 
 * centers, labels, and the secret passages
 * 
 * @author Jaylene Khourn, Trisha Varadaraj
 * 
 */

package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
//import clueGame.Room; // NEW
public class Board {

	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private static Board theInstance = new Board();
	private Set<BoardCell> targets = new HashSet<>();
	private Set<BoardCell> visited = new HashSet<>();
	private List<Player> players = new ArrayList<>();
	
	// List to store weapon names from setup file
	private List<String> weaponList = new ArrayList<>();
	
	// Deck of all cards in the game and solution envelope
	private List<Card> deck = new ArrayList<>();
	private Solution solution;
	
	// private constructor prevents creating multiple boards
	private Board() {
	}
	
	// returns the single instance of Board
	public static Board getInstance() {
		return theInstance;
	}
	
	// sets the config file names
	public void setConfigFiles(String layoutFile, String setupFile) {
		layoutConfigFile = layoutFile;
		setupConfigFile = setupFile;
	}
	
	// initializes the board
	public void initialize() { 
		// NEW 
		
		// loads the room info before we create the board layout
		try {
			loadSetupConfig();
			loadLayoutConfig();
			calcAdjacencies(); // NEW 
		}
		// displays an error if either config files cannot be loaded
		catch (FileNotFoundException | BadConfigFormatException e) {
			System.err.println("Error initializing board: " + e.getMessage());
		}
		
	}

	// calculates the adjacency list for every cell on the board
	public void calcAdjacencies() {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				grid[row][col].getAdjList().clear();
			}
		}
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				calculateCellAdjacency(row, col);
			}
		}
	}
	
	// HELPER FUNCTIONS:

	// connects a room center to its secret passage destination
	private void addSecretPassage(BoardCell center) {

	    // search the board for secret passages belonging to this room
	    for (int row = 0; row < numRows; row++) {
	        for (int col = 0; col < numColumns; col++) {
	            BoardCell cell = grid[row][col];

	            // check that the passage belongs to this room
	            if (cell.getInitial() == center.getInitial() && cell.getSecretPassage() != ' ') {
	                char destination = cell.getSecretPassage();
	                Room targetRoom = roomMap.get(destination);

	                // connect the room centers
	                if (targetRoom != null && targetRoom.getCenterCell() != null) {
	                    center.addAdjacency(targetRoom.getCenterCell());
	                }
	            }
	        }
	    }
	}

	// determines adjacency connections based on the cell type
	private void calculateCellAdjacency(int row, int col) {
		BoardCell cell = grid[row][col];
		if (cell.isRoomCenter()) {
			addSecretPassage(cell);
			addRoomDoorways(cell);
		} else if (cell.isDoorway()) {
			addWalkwayNeighbors(cell, row, col);
			addDoorwayRoom(cell, row, col);
		} else if (cell.isWalkway()) {
			addWalkwayNeighbors(cell, row, col);
		}
	}
	
	// adds adjacent walkways to a a walkway/doorway cell
	private void addWalkwayNeighbors(BoardCell cell, int row, int col) {
		int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		for (int[] direction : directions) {
			int newRow = row + direction[0];
			int newCol = col + direction[1];
			if (isValidCell(newRow, newCol)) {
				BoardCell neighbor = grid[newRow][newCol];
				
				// walkways can connect to other walkways and doorways
				if (neighbor.isWalkway() || (cell.isWalkway() && neighbor.isDoorway())) {
				    cell.addAdjacency(neighbor);
				}
			}
		}
	}

	// checks whether a row and col are within the board
	private boolean isValidCell(int row, int col) {
		return row >= 0 && row < numRows && col >= 0 && col < numColumns;
	}

	// returns the room that a doorway points towards
	private Room getDoorwayRoom(BoardCell doorway, int row, int col) {
		if (!doorway.isDoorway()) {
			return null;
		}
		int roomRow = row;
		int roomCol = col;
		switch (doorway.getDoorDirection()) {
		case UP:
			roomRow--;
			break;
		case DOWN:
			roomRow++;
			break;
		case LEFT:
			roomCol--;
			break;
		case RIGHT:
			roomCol++;
			break;
		default:
			return null;
		}
		if (!isValidCell(roomRow, roomCol)) {
			return null;
		}
		BoardCell roomCell = grid[roomRow][roomCol];
		if (roomCell.isWalkway() || roomCell.getInitial() == 'X') {
			return null;
		}

		return getRoom(roomCell);
	}

	// connects a doorway to the center of the room it enters
	private void addDoorwayRoom(BoardCell doorway, int row, int col) {
		Room room = getDoorwayRoom(doorway, row, col);
		if (room != null && room.getCenterCell() != null) {
			doorway.addAdjacency(room.getCenterCell());
		}
	}

	// connects a room center to all doorways entering the room
	private void addRoomDoorways(BoardCell center) {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				BoardCell cell = grid[row][col];
				if (cell.isDoorway()) {
					Room room = getDoorwayRoom(cell, row, col);
					if (room != null && room.getCenterCell() == center) {
						center.addAdjacency(cell);
					}
				}
			}
		}
	}

	public void calcTargets(BoardCell startCell, int pathLength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		findAllTargets(startCell, pathLength);
	}

	private void findAllTargets(BoardCell currentCell, int numSteps) {
		for (BoardCell neighbor : currentCell.getAdjList()) {
			if (visited.contains(neighbor)) continue;
			if (neighbor.isOccupied() && !neighbor.isRoomCenter()) continue;

			visited.add(neighbor);

			if (numSteps == 1 || neighbor.isRoomCenter()) {
				targets.add(neighbor);
			} else {
				findAllTargets(neighbor, numSteps - 1);
			}

			visited.remove(neighbor);
		}
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	public Set<BoardCell> getAdjList(int row, int col) {
		return grid[row][col].getAdjList();
	}
	

	// loads the room, space, player, and weapon info from the setup file
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {

	    // reset rooms, players, and weapons before loading
	    roomMap = new HashMap<>();
	    players = new ArrayList<>();
	    weaponList = new ArrayList<>();

	    File file = new File("data/" + setupConfigFile);

	    try (Scanner scanner = new Scanner(file)) {

	        // read each line in the setup file
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine().trim();

	            // skip empty lines and comments
	            if (line.isEmpty() || line.startsWith("//")) {
	                continue;
	            }

	            // separate the line by commas
	            String[] parts = line.split(",", -1);
	            String type = parts[0].trim();

	            // load rooms and spaces
	            if (type.equals("Room") || type.equals("Space")) {

	                if (parts.length != 3) {
	                    throw new BadConfigFormatException("Invalid setup configuration: " + line);
	                }

	                String name = parts[1].trim();
	                String initialText = parts[2].trim();

	                // check the room name and initial
	                if (name.isEmpty() || initialText.length() != 1) {
	                    throw new BadConfigFormatException("Invalid room information: " + line);
	                }

	                char initial = initialText.charAt(0);

	                // store the room or space
	                roomMap.put(initial, new Room(name));

	            } else if (type.equals("Player")) {

	                // player entries must have six fields
	                if (parts.length != 6) {
	                    throw new BadConfigFormatException("Invalid player configuration: " + line);
	                }

	                String name = parts[1].trim();
	                String color = parts[2].trim();
	                String playerType = parts[3].trim();

	                int row;
	                int column;

	                // convert starting location to integers
	                try {
	                    row = Integer.parseInt(parts[4].trim());
	                    column = Integer.parseInt(parts[5].trim());
	                } catch (NumberFormatException e) {
	                    throw new BadConfigFormatException("Invalid player coordinates: " + line);
	                }

	                // check the player information
	                if (name.isEmpty() || color.isEmpty() || row < 0 || column < 0) {
	                    throw new BadConfigFormatException("Invalid player information: " + line);
	                }

	                // create a human or computer player
	                if (playerType.equals("Human")) {
	                    players.add(new HumanPlayer(name, color, row, column));
	                } else if (playerType.equals("Computer")) {
	                    players.add(new ComputerPlayer(name, color, row, column));
	                } else {
	                    throw new BadConfigFormatException("Invalid player type: " + playerType);
	                }

	            } else if (type.equals("Weapon")) {

	                // weapon entries must have two fields
	                if (parts.length != 2 || parts[1].trim().isEmpty()) {
	                    throw new BadConfigFormatException("Invalid weapon configuration: " + line);
	                }
	                // store the weapon name in our weapon list
	                weaponList.add(parts[1].trim());
	            } else {
	                throw new BadConfigFormatException("Invalid setup entry: " + line);
	            }
	        }
	    }
	}
	
	// loads in the board layout
	public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException {
	    List<String[]> rows = new ArrayList<>();
	    File file = new File("data/" + layoutConfigFile);

	    // read the layout file
	    try (Scanner scanner = new Scanner(file)) {
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine().trim();
	            if (line.isEmpty()) {
	                continue;
	            }
	            rows.add(line.split(",", -1));
	        }
	    }

	    // check that the layout is not empty
	    numRows = rows.size();
	    if (numRows == 0) {
	        throw new BadConfigFormatException("Layout file is empty.");
	    }
	    numColumns = rows.get(0).length;
	    
	    // check that every row has the same number of columns
	    for (String[] row : rows) {
	        if (row.length != numColumns) {
	            throw new BadConfigFormatException("Layout rows have inconsistent columns.");
	        }
	    }

	    // create the board grid
	    grid = new BoardCell[numRows][numColumns];

	    // create and configure each cell
	    for (int row = 0; row < numRows; row++) {
	        for (int col = 0; col < numColumns; col++) {
	            String cellData = rows.get(row)[col].trim();
	            if (cellData.isEmpty()) {
	                throw new BadConfigFormatException("Empty cell at row " + row + ", column " + col);
	            }
	            char initial = cellData.charAt(0);
	            if (!roomMap.containsKey(initial)) {
	                throw new BadConfigFormatException("Undefined room initial: " + initial);
	            }
	            BoardCell cell = new BoardCell(row, col, initial);
	            grid[row][col] = cell;
	            configureCell(cell, cellData);
	        }
	    }
	}
	

	// configures the room center, label, passage, and doorway
	private void configureCell(BoardCell cell, String cellData) {
	    char initial = cell.getInitial();
	    Room room = roomMap.get(initial);
	    
	    // check for a secret passage
	    if (cellData.length() == 2 && Character.isUpperCase(cellData.charAt(1))) {
	        cell.setSecretPassage(cellData.charAt(1));
	    }

	    // identify the room center
	    if (cellData.contains("*")) {
	        cell.setRoomCenter(true);
	        room.setCenterCell(cell);
	    }

	    // identify the room label
	    if (cellData.contains("#")) {
	        cell.setRoomLabel(true);
	        room.setLabelCell(cell);
	    }

	    // identify doorway direction
	    if (cellData.length() > 1) {
	        setDoorDirection(cell, cellData.charAt(1));
	    }
	}

	// sets the doorway direction based on the layout symbol
	private void setDoorDirection(BoardCell cell, char symbol) {
	    switch (symbol) {
	        case '^':
	            cell.setDoorDirection(DoorDirection.UP);
	            break;

	        case 'v':
	            cell.setDoorDirection(DoorDirection.DOWN);
	            break;

	        case '<':
	            cell.setDoorDirection(DoorDirection.LEFT);
	            break;

	        case '>':
	            cell.setDoorDirection(DoorDirection.RIGHT);
	            break;

	        default:
	            break;
	    }
	}
	
	// builds the deck containing cards for rooms, players, and weapons
	public void buildDeck() {
		deck.clear();

		// add playable room cards
		for (Room room : roomMap.values()) {
			if (room.isPublicRoom()) {
				deck.add(new Card(room.getName(), CardType.ROOM));
			}
		}

		// add player cards
		for (Player player : players) {
			deck.add(new Card(player.getName(), CardType.PERSON));
		}

		// add weapon cards
		for (String weapon : weaponList) {
			deck.add(new Card(weapon, CardType.WEAPON));
		}
	}

	// picks the solution cards and deals the rest of the deck to players
	public void deal() {
		buildDeck();
		List<Card> workingDeck = new ArrayList<>(deck);
		Collections.shuffle(workingDeck);

		Card solutionPerson = null;
		Card solutionWeapon = null;
		Card solutionRoom = null;

		// pick 1 person, 1 weapon, and 1 room for the solution
		for (int i = workingDeck.size() - 1; i >= 0; i--) {
			Card card = workingDeck.get(i);
			if (solutionPerson == null && card.getCardType() == CardType.PERSON) {
				solutionPerson = card;
				workingDeck.remove(i);
			} else if (solutionWeapon == null && card.getCardType() == CardType.WEAPON) {
				solutionWeapon = card;
				workingDeck.remove(i);
			} else if (solutionRoom == null && card.getCardType() == CardType.ROOM) {
				solutionRoom = card;
				workingDeck.remove(i);
			}
		}

		solution = new Solution(solutionPerson, solutionWeapon, solutionRoom);

		// clear player hands before dealing
		for (Player player : players) {
			player.clearHand();
		}

		// deal remaining cards to players in a round-robin fashion
		int playerIndex = 0;
		for (Card card : workingDeck) {
			players.get(playerIndex).updateHand(card);
			playerIndex = (playerIndex + 1) % players.size();
		}
	}

	// returns the total number of rows
	public int getNumRows() {
		return numRows;
	}
	
	// returns the total number of columns
	public int getNumColumns() {
		return numColumns;
	}
	
	// returns the BoardCell at the specified row/col	
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	// returns a Room object using the character initial
	public Room getRoom(char initial) {
		return roomMap.get(initial);
	}
	
	// returns the Room object associated with a specific BoardCell
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}

	// returns the list of players in the game
	public List<Player> getPlayers() {
		return players;
	}
	
	// returns the deck of cards
	public List<Card> getDeck() {
	    return deck;
	}

	// returns the solution
	public Solution getSolution() {
	    return solution;
	}

	// alias getter in case tests look for getAnswer()
	public Solution getAnswer() {
	    return solution;
	}
}