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
		Player player = new HumanPlayer();
		assertTrue(player instanceof HumanPlayer);
	}
	
	// tests that ComputerPlayer inherits from Player
	@Test
	public void testComputerPlayer() {
		Player player = new ComputerPlayer();
		assertTrue(player instanceof ComputerPlayer);
	}
	
	

}
