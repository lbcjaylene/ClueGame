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
		board.setConfigFiles("data/ClueLayout.csv","data/ClueSetup.txt");
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
	
	



	// tests all 4 doorway directions and a regular cell in the layout
		@Test
		public void testDoorDirections() {
		    BoardCell cell = board.getCell(3, 5);
		    assertTrue(cell.isDoorway());
		    assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		    cell = board.getCell(5, 2);
		    assertTrue(cell.isDoorway());
		    assertEquals(DoorDirection.UP, cell.getDoorDirection());
		    cell = board.getCell(5, 10);
		    assertTrue(cell.isDoorway());
		    assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		    cell = board.getCell(7, 2);
		    assertTrue(cell.isDoorway());
		    assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		    cell = board.getCell(0, 0);
		    assertFalse(cell.isDoorway());
		}
		
		// tests if the room centers and labels are identified correctly
		@Test
		public void testRoomCenterAndLabel() {
		    BoardCell center = board.getCell(2, 2);
		    BoardCell label = board.getCell(2, 3);
		    assertTrue(center.isRoomCenter());
		    assertFalse(center.isRoomLabel());
		    assertTrue(label.isRoomLabel());
		    assertFalse(label.isRoomCenter());
		    assertEquals('E', center.getInitial());
		    assertEquals('E', label.getInitial());
		   
		    // enchantment room
		    Room enchantment = board.getRoom('E');
		   
		    // checking that the room stores the correct center and label cells
		    assertSame(center, enchantment.getCenterCell());
		    assertSame(label, enchantment.getLabelCell());
		}
		
		
		// tests the secret passage destinations and a cell without a passage
		@Test
		public void testSecretPassages() {
		    assertEquals('A', board.getCell(1, 1).getSecretPassage());
		    assertEquals('D', board.getCell(2, 9).getSecretPassage());
		    assertEquals('E', board.getCell(19, 1).getSecretPassage());
		    assertEquals('N', board.getCell(19, 18).getSecretPassage());
		    assertEquals(' ', board.getCell(0, 0).getSecretPassage());
		}	

	
}





