package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import clueGame.Player;
import clueGame.HumanPlayer;
import clueGame.ComputerPlayer;

public class GameSetupTests {

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
	

}
