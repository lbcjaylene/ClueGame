/*
- Board Class
- 
- Represents the board using the singleton design patterns.
- Reads the setup and layout configuration files, creates the board
- cells, store information about each room, doorway, 
- centers, labels, and the secret passages
- 
- @author 
Jaylene Khourn, Trisha Varadaraj
- 
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
	private List<String> weaponList = new ArrayList<>();
	private List<Card> deck = new ArrayList<>();
	private Solution solution;
	
	// setters and getters for testing
	
	// sets a known solution for testing accusations
	public void setTheAnswer(Solution answer) { 
		this.solution = answer; 
	}
	
	// sets the player order for testing suggestions
	public void setPlayers(List<Player> players) { 
		this.players = players; 
	}
	
	// sets the cards used by the computer suggestion tests
	public void setDeck(List<Card> deck) { 
		this.deck = deck; 
	}
	
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
			// loads the room info before we create the board layout
			try {
				loadSetupConfig();
				loadLayoutConfig();
				calcAdjacencies(); 
				buildDeck();
			}
			// displays an error if either config files cannot be loaded
			catch (FileNotFoundException | BadConfigFormatException e) {
				System.err.println("Error initializing board: " + e.getMessage());
			}
		}

	// checks if an accusation matches the secret solution envelope
	public boolean checkAccusation(Solution accusation) {
		if (solution == null || accusation == null) {
			return false;
		}
		return accusation.getPerson().equals(solution.getPerson()) &&
			   accusation.getWeapon().equals(solution.getWeapon()) &&
			   accusation.getRoom().equals(solution.getRoom());
	}

	// processes players in turn order (starting with player after accuser) to see if anyone can disprove a suggestion
	public Card handleSuggestion(Solution suggestion, Player accuser) {
		
		// finds the suggesting player in the turn order
		int accuserIndex = players.indexOf(accuser);
		if (accuserIndex == -1) {
			return null;
		}
		
		// starts with the next player and skips the suggesting player
		int numPlayers = players.size();
		for (int i = 1; i < numPlayers; i++) {
			int currentIndex = (accuserIndex + i) % numPlayers;
			Player player = players.get(currentIndex);
			Card disprovedCard = player.disproveSuggestion(suggestion);
			if (disprovedCard != null) {
				return disprovedCard;
			}
		}
		return null;
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
	
	// adds adjacent walkways to a walkway/doorway cell
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
	
	// checks whether a row and col are within the board
		private boolean isValidCell(int row, int col) {
			return row >= 0 && row < numRows && col >= 0 && col < numColumns;
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

	// calculates the possible movement targets from a starting cell
	public void calcTargets(BoardCell startCell, int pathLength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		findAllTargets(startCell, pathLength);
	}

	// recursively finds all possible movement targets
	private void findAllTargets(BoardCell currentCell, int numSteps) {
		for (BoardCell neighbor : currentCell.getAdjList()) {
			
			// skips cells that have already been visited
			if (visited.contains(neighbor)) continue;
			
			// skip occupied walkways
			if (neighbor.isOccupied() && !neighbor.isRoomCenter()) continue;
			visited.add(neighbor);
			
			// add target if no steps remain or a room is reached
			if (numSteps == 1 || neighbor.isRoomCenter()) {
				targets.add(neighbor);
			} else {
				
				// continue searching with one less step
				findAllTargets(neighbor, numSteps - 1);
			}
			
			// remove cell so it can be used in another path
			visited.remove(neighbor);
		}
	}
	
	// loads the room, space, player, and weapon info from the setup file
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {

	    // reset rooms, players, and weapons before loading
	    roomMap = new HashMap<>();
	    players = new ArrayList<>();
	    weaponList = new ArrayList<>();
	    File file = new File("data/" + setupConfigFile);
	    try (Scanner scanner = new Scanner(file)) {
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine().trim();

	            if (line.isEmpty() || line.startsWith("//")) {
	                continue;
	            }

	            String[] parts = line.split(",", -1);
	            String type = parts[0].trim();

	            if (type.equals("Room") || type.equals("Space")) {
	                if (parts.length != 3) {
	                    throw new BadConfigFormatException("Invalid setup configuration: " + line);
	                }
	                String name = parts[1].trim();
	                String initialText = parts[2].trim();

	                if (name.isEmpty() || initialText.length() != 1) {
	                    throw new BadConfigFormatException("Invalid room information: " + line);
	                }
	                char initial = initialText.charAt(0);
	                roomMap.put(initial, new Room(name));
	            } else if (type.equals("Player")) {

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

	                if (name.isEmpty() || color.isEmpty() || row < 0 || column < 0) {
	                    throw new BadConfigFormatException("Invalid player information: " + line);
	                }

	                if (playerType.equals("Human")) {
	                    players.add(new HumanPlayer(name, color, row, column));
	                } else if (playerType.equals("Computer")) {
	                    players.add(new ComputerPlayer(name, color, row, column));
	                } else {
	                    throw new BadConfigFormatException("Invalid player type: " + playerType);
	                }
	            } else if (type.equals("Weapon")) {

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

	    try (Scanner scanner = new Scanner(file)) {
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine().trim();
	            if (line.isEmpty()) {
	                continue;
	            }
	            rows.add(line.split(",", -1));
	        }
	    }

	    numRows = rows.size();
	    if (numRows == 0) {
	        throw new BadConfigFormatException("Layout file is empty.");
	    }
	    numColumns = rows.get(0).length;
	    
	    for (String[] row : rows) {
	        if (row.length != numColumns) {
	            throw new BadConfigFormatException("Layout rows have inconsistent columns.");
	        }
	    }

	    grid = new BoardCell[numRows][numColumns];

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
	    
	    if (cellData.length() == 2 && Character.isUpperCase(cellData.charAt(1))) {
	        cell.setSecretPassage(cellData.charAt(1));
	    }

	    if (cellData.contains("*")) {
	        cell.setRoomCenter(true);
	        room.setCenterCell(cell);
	    }

	    if (cellData.contains("#")) {
	        cell.setRoomLabel(true);
	        room.setLabelCell(cell);
	    }

	    if (cellData.length() > 1) {
	        setDoorDirection(cell, cellData.charAt(1));
	    }
	}

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
		
		// adds room cards to the deck and connects them to room centers
		for (Room room : roomMap.values()) {
		    if (room.isPublicRoom()) {
		        Card roomCard = new Card(room.getName(), CardType.ROOM);
		        deck.add(roomCard);

		        // assigns the matching card to the room center
		        if (room.getCenterCell() != null) {
		            room.getCenterCell().setRoomCard(roomCard);
		        }
		    }
		}
		for (Player player : players) {
			deck.add(new Card(player.getName(), CardType.PERSON));
		}
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

		for (Player player : players) {
			player.clearHand();
			player.clearSeenCards();
		}

		int playerIndex = 0;
		for (Card card : workingDeck) {
			players.get(playerIndex).updateHand(card);
			playerIndex = (playerIndex + 1) % players.size();
		}
	}

	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	public Room getRoom(char initial) {
		return roomMap.get(initial);
	}
	
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public List<Card> getDeck() {
	    return deck;
	}

	public Solution getSolution() {
	    return solution;
	}

	public Solution getAnswer() {
	    return solution;
	}
	
	// returns the set of possible movement targets
	public Set<BoardCell> getTargets() {
		return targets;
	}

	// returns the adjacent cells for the given row and column
	public Set<BoardCell> getAdjList(int row, int col) {
		return grid[row][col].getAdjList();
	}
}