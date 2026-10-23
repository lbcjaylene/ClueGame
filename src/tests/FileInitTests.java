package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

class FileInitTests {

	private static Board board;
	
	// initializes our board before each test
	@BeforeEach
	public void setUp() throws Exception {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv","ClueSetup.txt");
		board.initialize();
	}
	
	// this test checks the first and last entries throughout the ClueSetup.csv file for our board
	@Test
	void testRoomNames() {
		assertEquals("Walkway", board.getRoom('W').getName());
		assertEquals("Unused", board.getRoom('X').getName());
		assertEquals("Enchantment Room", board.getRoom('E').getName());
		assertEquals("Trial Chambers", board.getRoom('T').getName());
		assertEquals("Stronghold", board.getRoom('S').getName());
		assertEquals("Ancient City", board.getRoom('A').getName());
		assertEquals("End City", board.getRoom('D').getName());
	}
	
	// tests whether the layout file contains the correct board dimensions
	@Test
	public void testBoardDimensions() {
		assertEquals(22, board.getNumRows());
		assertEquals(23, board.getNumColumns());
	}
	
}
