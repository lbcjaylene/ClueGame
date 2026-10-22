package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import clueGame.Board;
import clueGame.BadConfigFormatException;
import java.io.FileNotFoundException;

class FileInitTests {

	private static Board board;
	
	@BeforeEach
	public void setUp() throws Exception {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv","ClueSetup.txt");
		board.loadSetupConfig();
	}
	
	// this test checks the first and last entries throughout the ClueSetup.csv file
	
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
	
	@Test
	public void test306RoomNames() throws Exception {
	    Board testBoard = Board.getInstance();

	    testBoard.setConfigFiles(
	        "ClueLayout306.csv",
	        "ClueSetup306.txt"
	    );

	    testBoard.loadSetupConfig();

	    assertEquals("Conservatory",
	        testBoard.getRoom('C').getName());

	    assertEquals("Ballroom",
	        testBoard.getRoom('B').getName());

	    assertEquals("Kitchen",
	        testBoard.getRoom('K').getName());
	}

}
