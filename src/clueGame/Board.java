/*
 * Board Class
 * 
 * Represents the board using the singleton design patterns.
 * Reads the setup and layout configuration files, creates the board
 * cells, store information about each room, doorway, centers, labels,
 * and the secret passages
 * 
 * @author Jaylene Khourn, Trisha Varadaraj
 * 
 */

package clueGame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import java.util.Set;
import java.util.HashSet; // NEW 

public class Board {

	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private static Board theInstance = new Board();
	
	//NEW
	private java.util.Set<BoardCell> targets = new java.util.HashSet<>();
	private java.util.Set<BoardCell> visited = new java.util.HashSet<>();
	
	// private constructor prevents creating multiple boards
	private Board() {
		super();
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
		// displays an error if either config files cant be loaded
		catch (FileNotFoundException | BadConfigFormatException e) {
			System.err.println("Error initializing board: " + e.getMessage());
		}
		
	}
	public void calcAdjacencies() { // Calculates every adjacent cell that is valid for every cell on the board grid .     
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numColumns; c++) {
				BoardCell cell = grid[r][c];
				cell.getAdjList().clear(); // Clears neighbprs before recalculation 

				// Room Centers
				if (cell.isRoomCenter()) {
					if (cell.getSecretPassage() != ' ') {
						Room targetRoom = roomMap.get(cell.getSecretPassage());
						if (targetRoom != null && targetRoom.getCenterCell() != null) {
							cell.addAdjacency(targetRoom.getCenterCell());
						}
					}
					for (int row = 0; row < numRows; row++) {
						for (int col = 0; col < numColumns; col++) {
							BoardCell checkCell = grid[row][col];
							if (checkCell.isDoorway()) {
								Room entryRoom = getRoom(checkCell);
								if (entryRoom != null && entryRoom.getCenterCell() == cell) {
									cell.addAdjacency(checkCell); // Connect the room center to doorways pointing into room . 
								}
							}
						}
					}
				} 
				// Walkways
				else if (cell.isWalkway()) {
					int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
					for (int[] dir : directions) {
						int nr = r + dir[0];
						int nc = c + dir[1];
						if (nr >= 0 && nr < numRows && nc >= 0 && nc < numColumns) {
							BoardCell neighbor = grid[nr][nc];
							if (neighbor.isWalkway() || neighbor.isDoorway()) {  // Walkway to neighboring walkways and doorways connections . 
								cell.addAdjacency(neighbor);
							}
						}
					}
				} 
				//  Doorways
				else if (cell.isDoorway()) {
					int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
					for (int[] dir : directions) {
						int nr = r + dir[0];
						int nc = c + dir[1];
						if (nr >= 0 && nr < numRows && nc >= 0 && nc < numColumns) {
							BoardCell neighbor = grid[nr][nc];
							if (neighbor.isWalkway()) {
								cell.addAdjacency(neighbor); // Doorway to adjacent walkways 
							}
						}
					}
					Room room = getRoom(cell);
					if (room != null && room.getCenterCell() != null) {
						cell.addAdjacency(room.getCenterCell()); // Connect doorway to room center which corresponds to it .     
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
	

	
	
	// loads the room info from the setup file
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
		
		// new map each time we load in setup
		roomMap = new HashMap<>();
		
		// open the setup config file
		File file = new File("data/" + setupConfigFile);
		try (Scanner scanner = new Scanner(file)) {

	        // read the file one line at a time
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine().trim();

	            // skip empty lines
	            if (line.isEmpty() || line.startsWith("//")) {
	                continue;
	            }

	            // separate the line by commas
	            String[] parts = line.split(",", -1);

	            // each line should have exactly three parts
	            if (parts.length != 3) {
	                throw new BadConfigFormatException ("Invalid setup configuration: " + line);
	            }

	            String type = parts[0].trim();
	            String name = parts[1].trim();
	            String initialText = parts[2].trim();

	            // check that the initial is one character
	            if (initialText.length() != 1) {
	                throw new BadConfigFormatException(
	                    "Invalid room initial: " + initialText);
	            }

	            // check the entry type and room name
	            if ((!type.equals("Room") && !type.equals("Space"))
	                    || name.isEmpty()) {
	                throw new BadConfigFormatException(
	                    "Invalid setup entry: " + line);
	            }

	            // convert the initial from String to char
	            char initial = initialText.charAt(0);

	            // store the room in the map
	            roomMap.put(initial, new Room(name));
	        }
	    }
		
	}
	
	// loads in the board layout
	public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException {
		
		// temporarily stores each row of the layout
		List<String[]> rows = new ArrayList<>();
		
		// opens the layout file from the data folder
		File file = new File("data/" + layoutConfigFile);
		try (Scanner scanner = new Scanner(file)) {
			
			// read each row of the file
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				
				// skip any empty lines
				if (line.isEmpty()) {
					continue;
				}
				
				// split the row into individual cells
				String[] cells = line.split(",", -1);
				rows.add(cells);
			}
		}
		
		// determine the number of rows in the board
		numRows = rows.size();
		
		// making sure the file is not empty
		if(numRows == 0) {
			throw new BadConfigFormatException("Layout file is empty.");
		}
		
		// determine the number of columns by using the first row
		numColumns = rows.get(0).length;
		
		// check every row that has the same number of columns
		for (String[] row : rows) {
		    if (row.length != numColumns) {
		        throw new BadConfigFormatException("Layout rows have inconsistent columns.");
		    }
		}
		
		// create grid for board
		grid = new BoardCell[numRows][numColumns];
		
		// loop through every row in the board
		for (int row = 0; row < numRows; row++) {
			String[] cells = rows.get(row);
			
			// loop through every column in the current row
			for (int col = 0; col < numColumns; col++) {
				
				// remove whitespace
				String cellData = cells[col].trim();

				// check for empty cells before reading the initial
				if (cellData.isEmpty()) {
				    throw new BadConfigFormatException("Empty cell at row " + row + ", column " + col);
				}

				// first char represents the room
				char initial = cellData.charAt(0);
				
				// making sure the initial exists in the setup
				if (!roomMap.containsKey(initial)) {
				    throw new BadConfigFormatException("Undefined room initial: " + initial);
				}
				
				// create a BoardCell at this pos
				grid[row][col] = new BoardCell(row, col, initial);
				
				// checks if this cell contains a secret passage
				if (cellData.length() == 2 && Character.isUpperCase(cellData.charAt(1))) {
				    grid[row][col].setSecretPassage(cellData.charAt(1));
				}
				
				// the asterisk identifies the center of a room
				if (cellData.contains("*")) {
				    grid[row][col].setRoomCenter(true);
				    
				    // stores this cell as the room's center
				    roomMap.get(initial).setCenterCell(grid[row][col]);
				}
				
				// the hashtag identifies the label position of a room
				if (cellData.contains("#")) {
				    grid[row][col].setRoomLabel(true);
				    
				    // stores this cell as the room's label
				    roomMap.get(initial).setLabelCell(grid[row][col]);
				}
				
				// check for additional symbols after the room initial
				if (cellData.length() > 1) {
					char symbol = cellData.charAt(1);
					
					// based on its symbol, assign the doorway direction
					switch(symbol) {
					case '^':
						grid[row][col].setDoorDirection(DoorDirection.UP);
						break;
					case 'v':
						grid[row][col].setDoorDirection(DoorDirection.DOWN);
						break;
					case '<':
						grid[row][col].setDoorDirection(DoorDirection.LEFT);
						break;
					case '>':
						grid[row][col].setDoorDirection(DoorDirection.RIGHT);
						break;
					}
				}
			}
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
	
}

