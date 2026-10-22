package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;

public class Board {

	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	
	// singleton method
	
	private static Board theInstance = new Board();
	
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
	
	public void loadLayoutConfig() {
		
	}
	
	// retrieve a room with only the initial 
	
	public Room getRoom(char initial) {
		return roomMap.get(initial);
	}
}
