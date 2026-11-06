package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class GameSetupTests {

	// tests that the HumanPlayer is a subclass of Player
	@Test
	public void testHumanPlayer() {
		Player player = new HumanPlayer();
		assertTrue(player instanceof HumanPlayer);
	}
	
	

}
