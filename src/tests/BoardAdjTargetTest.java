package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import clueGame.Board;
import clueGame.BoardCell;
import java.util.Set;
import clueGame.DoorDirection;

class BoardAdjTargetTest {

	private Board board;
	
	@BeforeEach
	public void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}
	
	// tests a walkway with 4 adjacent walkways
	@Test
	public void testWalkwayAdjacency() {
		Set<BoardCell> adj = board.getAdjList(6, 5);
		assertEquals(4, adj.size());
		assertTrue(adj.contains(board.getCell(5, 5)));
		assertTrue(adj.contains(board.getCell(7, 5)));
		assertTrue(adj.contains(board.getCell(6, 4)));
		assertTrue(adj.contains(board.getCell(6, 6)));
	}
	
    // tests all four doorway directions and room connections
    @Test
    public void testDoorwayAdjacency() {
        BoardCell up = board.getCell(5, 2);
        BoardCell down = board.getCell(7, 2);
        BoardCell left = board.getCell(3, 5);
        BoardCell right = board.getCell(5, 10);

        // doorway directions
        assertEquals(DoorDirection.UP, up.getDoorDirection());
        assertEquals(DoorDirection.DOWN, down.getDoorDirection());
        assertEquals(DoorDirection.LEFT, left.getDoorDirection());
        assertEquals(DoorDirection.RIGHT, right.getDoorDirection());

        // UP doorway connects to Enchantment Room
        BoardCell enchantment = board.getRoom('E').getCenterCell();
        assertTrue(up.getAdjList().contains(enchantment));

        // DOWN doorway connects to its room center
        BoardCell downRoom = board.getCell(8, 2);
        BoardCell downCenter = board.getRoom(downRoom).getCenterCell();
        assertTrue(down.getAdjList().contains(downCenter));

        // LEFT doorway connects to its room center
        BoardCell leftRoom = board.getCell(3, 4);
        BoardCell leftCenter = board.getRoom(leftRoom).getCenterCell();
        assertTrue(left.getAdjList().contains(leftCenter));

        // RIGHT doorway connects to its room center
        BoardCell rightRoom = board.getCell(5, 11);
        BoardCell rightCenter = board.getRoom(rightRoom).getCenterCell();
        assertTrue(right.getAdjList().contains(rightCenter));
    }

	// tests that a room center connects to its doorway
	@Test
	public void testRoomCenterAdjacency() {
		BoardCell doorway = board.getCell(5,  2);
		BoardCell roomCenter = board.getRoom('E').getCenterCell();
		Set<BoardCell> adj = roomCenter.getAdjList();
		assertTrue(adj.contains(doorway), "Room center should connect to its doorway");
	}
	
	// tests that a non-center room cell has no adjacency
	@Test
	public void testNonCenterRoomAdjacency() {
		BoardCell roomCell = board.getCell(0, 0);
		Set <BoardCell> adj = roomCell.getAdjList();
		assertFalse(roomCell.isRoomCenter());
		assertTrue(adj.isEmpty(), "Non-center room cells should have no adjacencies");
	}
	
    // tests adjacency for a walkway on the top edge
    @Test
    public void testTopEdgeAdjacency() {
        BoardCell cell = board.getCell(0, 6);
        Set<BoardCell> adj = cell.getAdjList();
        assertEquals(2, adj.size());
        assertTrue(adj.contains(board.getCell(0, 7)));
        assertTrue(adj.contains(board.getCell(1, 6)));
    }

    // tests adjacency for a walkway on the bottom edge
    @Test
    public void testBottomEdgeAdjacency() {
        BoardCell cell = board.getCell(21, 6);
        Set<BoardCell> adj = cell.getAdjList();
        assertEquals(2, adj.size());
        assertTrue(adj.contains(board.getCell(20, 6)));
        assertTrue(adj.contains(board.getCell(21, 7)));
    }
	
    // tests adjacency for a walkway on the left edge
    @Test
    public void testLeftEdgeAdjacency() {
        BoardCell cell = board.getCell(14, 0);
        Set<BoardCell> adj = cell.getAdjList();
        assertEquals(1, adj.size());
        assertTrue(adj.contains(board.getCell(14, 1)));
    }


    // tests adjacency for a walkway on the right edge
    @Test
    public void testRightEdgeAdjacency() {
        BoardCell cell = board.getCell(6, 22);
        Set<BoardCell> adj = cell.getAdjList();
        assertEquals(2, adj.size());
        assertTrue(adj.contains(board.getCell(6, 21)));
        assertTrue(adj.contains(board.getCell(7, 22)));
    }

	// tests the secret passages that are in our board
    @Test
    public void testSecretPassageLocations() {
        int passageCount = 0;
        for (int row = 0; row < board.getNumRows(); row++) {
            for (int col = 0; col < board.getNumColumns(); col++) {
                if (board.getCell(row, col).getSecretPassage() != ' ') {
                    passageCount++;
                }
            }
        }
        assertEquals(4, passageCount);
        assertEquals('A', board.getCell(1, 1).getSecretPassage());
        assertEquals('D', board.getCell(2, 9).getSecretPassage());
        assertEquals('E', board.getCell(19, 1).getSecretPassage());
        assertEquals('N', board.getCell(19, 18).getSecretPassage());
    }

	// tests the secret passage from the Enchantment Room to Ancient City
	@Test
	public void testSecretPassageAdjacency() {
	    BoardCell enchantment = board.getRoom('E').getCenterCell();
	    BoardCell ancientCity = board.getRoom('A').getCenterCell();
	    Set<BoardCell> adj = enchantment.getAdjList();
	    assertEquals('A', board.getCell(1, 1).getSecretPassage());
	    assertTrue(adj.contains(ancientCity), "Enchantment Room should connect to Ancient City");
	}
	

    // tests that walkways do not connect to ordinary room cells
    @Test
    public void testWalkwayBesideRoom() {
        for (int row = 0; row < board.getNumRows(); row++) {
            for (int col = 0; col < board.getNumColumns(); col++) {
                BoardCell cell = board.getCell(row, col);
                if (!cell.isWalkway()) {
                    continue;
                }
                int[][] directions = {
                    {-1, 0}, {1, 0}, {0, -1}, {0, 1}
                };
                for (int[] dir : directions) {
                    int r = row + dir[0];
                    int c = col + dir[1];
                    if (r < 0 || r >= board.getNumRows() || c < 0 || c >= board.getNumColumns()) {
                        continue;
                    }

                    BoardCell neighbor = board.getCell(r, c);
                    if (!neighbor.isWalkway() && neighbor.getInitial() != 'X') {
                        assertFalse(cell.getAdjList().contains(neighbor), "Walkway should not connect directly to a room cell");
                    }
                }
            }
        }
    }

    // tests that one movement step reaches all adjacent walkway cells
    @Test
    public void testWalkwayTargetsOneStep() {
        BoardCell start = board.getCell(6, 5);
        board.calcTargets(start, 1);
        Set<BoardCell> targets = board.getTargets();
        assertEquals(4, targets.size());
        assertTrue(targets.contains(board.getCell(5, 5)));
        assertTrue(targets.contains(board.getCell(7, 5)));
        assertTrue(targets.contains(board.getCell(6, 4)));
        assertTrue(targets.contains(board.getCell(6, 6)));
    }


    // tests target calculations with two movement steps
    @Test
    public void testWalkwayTargetsTwoSteps() {
        BoardCell start = board.getCell(6, 5);
        board.calcTargets(start, 2);
        Set<BoardCell> targets = board.getTargets();
        assertEquals(8, targets.size());
        assertTrue(targets.contains(board.getCell(4, 5)));
        assertTrue(targets.contains(board.getCell(5, 4)));
        assertTrue(targets.contains(board.getCell(5, 6)));
        assertTrue(targets.contains(board.getCell(6, 3)));
        assertTrue(targets.contains(board.getCell(6, 7)));
        assertTrue(targets.contains(board.getCell(7, 4)));
        assertTrue(targets.contains(board.getCell(7, 6)));
        assertTrue(targets.contains(board.getCell(8, 5)));
        assertFalse(targets.contains(start));
    }

    // tests that a player can enter a room from its doorway
    @Test
    public void testEnterRoomTargets() {
        BoardCell doorway = board.getCell(5, 2);
        BoardCell roomCenter = board.getRoom('E').getCenterCell();
        board.calcTargets(doorway, 1);
        Set<BoardCell> targets = board.getTargets();
        assertTrue(targets.contains(roomCenter), "Player should be able to enter Enchantment Room");
    }

    // tests that entering a room ends movement early
    @Test
    public void testEnterRoomWithExtraSteps() {
        BoardCell doorway = board.getCell(5, 2);
        BoardCell roomCenter = board.getRoom('E').getCenterCell();
        board.calcTargets(doorway, 3);
        Set<BoardCell> targets = board.getTargets();
        assertTrue(targets.contains(roomCenter), "Room should be a target even with steps remaining");
    }
    
    // tests that occupied walkways cannot be movement targets
    @Test
    public void testOccupiedWalkwayTargets() {
        BoardCell start = board.getCell(6, 5);
        BoardCell occupied = board.getCell(5, 5);
        occupied.setOccupied(true);
        board.calcTargets(start, 1);
        Set<BoardCell> targets = board.getTargets();
        assertEquals(3, targets.size());
        assertFalse(targets.contains(occupied));
        assertTrue(targets.contains(board.getCell(7, 5)));
        assertTrue(targets.contains(board.getCell(6, 4)));
        assertTrue(targets.contains(board.getCell(6, 6)));
    }
    

    // tests movement through a secret passage
    @Test
    public void testSecretPassageTargets() {
        BoardCell start = board.getRoom('E').getCenterCell();
        BoardCell destination = board.getRoom('A').getCenterCell();
        board.calcTargets(start, 1);
        Set<BoardCell> targets = board.getTargets();
        assertTrue(targets.contains(destination), "Ancient City should be reachable through the secret passage");
    }
    
    // tests that a player can leave a room through its doorway
    @Test
    public void testLeaveRoomTargets() {
        BoardCell start = board.getRoom('E').getCenterCell();
        BoardCell doorway = board.getCell(5, 2);
        board.calcTargets(start, 1);
        Set<BoardCell> targets = board.getTargets();
        assertTrue(targets.contains(doorway), "Player should be able to leave the room through its doorway");
        assertFalse(targets.contains(start), "Starting room should not be a target");
    }


    





	
	


	
	
	
}

