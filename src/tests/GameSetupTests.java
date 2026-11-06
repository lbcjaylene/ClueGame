package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import clueGame.Player;
import clueGame.HumanPlayer;
import clueGame.ComputerPlayer;
import clueGame.Board;

public class GameSetupTests {
	private static Board board;
	
	// initializes the board before running the setup tests
	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}
	
	// tests that all six players are loaded from the setup file
	@Test
	public void testPlayerCount() {
		assertEquals(6, board.getPlayers().size());
	}

	// tests that the HumanPlayer is a subclass of Player
	@Test
	public void testHumanPlayer() {
		Player player = new HumanPlayer("Steve", "Blue", 0, 5);
		assertTrue(player instanceof HumanPlayer);
	}
	
	// tests that ComputerPlayer inherits from Player
	@Test
	public void testComputerPlayer() {
		Player player = new ComputerPlayer("Alex", "Orange", 0, 17);
		assertTrue(player instanceof ComputerPlayer);
	}
	
	// tests that a players name is stored correctly
	@Test
	public void testPlayerName() {
		Player player = new HumanPlayer("Steve", "Blue", 0, 5);
		assertEquals("Steve", player.getName());
	}
	
	// tests that a player's color and starting location are stored correctly
	@Test
	public void testPlayerColorAndLocation() {
		Player player = new HumanPlayer("Steve", "Blue", 0, 5);
		assertEquals("Blue", player.getColor());
		assertEquals(0, player.getRow());
		assertEquals(5, player.getColumn());
	}
	
	// tests that one human and five computer players are loaded
	@Test
	public void testPlayerTypes() {
		int humanCount = 0;
		int computerCount = 0;
		
		// count the number of each player type
		for (Player player : board.getPlayers()) {
	        if (player instanceof HumanPlayer) {
	            humanCount++;
	        } else if (player instanceof ComputerPlayer) {
	            computerCount++;
	        }
	    }

	    // verify the correct number of each player type
	    assertEquals(1, humanCount);
	    assertEquals(5, computerCount);
	}
	
	// tests that player info is loaded correctly from setup file
	@Test
	public void testPlayerInformation() {
		
		// get the first player loaded from the setup file
	    Player player = board.getPlayers().get(0);

	    // verify Steve's name, color, and starting location
	    assertEquals("Steve", player.getName());
	    assertEquals("Blue", player.getColor());
	    assertEquals(0, player.getRow());
	    assertEquals(5, player.getColumn());

	    // verify Steve is the human-controlled player
	    assertTrue(player instanceof HumanPlayer);
	}


}
