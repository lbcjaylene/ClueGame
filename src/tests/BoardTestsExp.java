package tests;

import experiment.TestBoard;
import experiment.TestBoardCell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test; // Imports BeforeEach annotations JUnit 5 .   

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTestsExp {
    private TestBoard board;

    @BeforeEach // Method runs before each unit test Method 
    public void setUp() { // set up to initialize fresh test objects prior to each test . 
        board = new TestBoard(); // 4x4
    }

    // ADJACENCY TESTS

    @Test  // JUnit test .    
    public void testAdjacencyTopLeft() { // tests at [0][0]
        Set<TestBoardCell> testList = board.getCell(0, 0).getAdjList(); // gets the adjacency list .    
        assertEquals(2, testList.size()); // Top left has only 2 adjacent neighbors .    
        assertTrue(testList.contains(board.getCell(0, 1)));
        assertTrue(testList.contains(board.getCell(1, 0)));
    }

    @Test
    public void testAdjacencyBottomRight() {
        Set<TestBoardCell> testList = board.getCell(3, 3).getAdjList();
        assertEquals(2, testList.size());
        assertTrue(testList.contains(board.getCell(2, 3)));
        assertTrue(testList.contains(board.getCell(3, 2)));
    }

    @Test
    public void testAdjacencyRightEdge() {
        Set<TestBoardCell> testList = board.getCell(1, 3).getAdjList();
        assertEquals(3, testList.size());
        assertTrue(testList.contains(board.getCell(0, 3)));
        assertTrue(testList.contains(board.getCell(2, 3)));
        assertTrue(testList.contains(board.getCell(1, 2)));
    }

    @Test
    public void testAdjacencyLeftEdge() {
        Set<TestBoardCell> testList = board.getCell(3, 0).getAdjList();
        assertEquals(2, testList.size());
        assertTrue(testList.contains(board.getCell(2, 0)));
        assertTrue(testList.contains(board.getCell(3, 1)));
    }

    @Test
    public void testAdjacencyMiddle() {
        Set<TestBoardCell> testList = board.getCell(2, 2).getAdjList();
        assertEquals(4, testList.size());
        assertTrue(testList.contains(board.getCell(1, 2)));
        assertTrue(testList.contains(board.getCell(3, 2)));
        assertTrue(testList.contains(board.getCell(2, 1)));
        assertTrue(testList.contains(board.getCell(2, 3)));
    }

    // TARGET TESTS
    // Tests normal movement on an empty board (no obstacles) .    
    @Test
    public void testTargetsNormal() {
        TestBoardCell start = board.getCell(0, 0);

        // Test roll 1, 2, 3, and 6 from top-left corner
        board.calcTargets(start, 1); // Calculates reachable targets from [0][0] with a roll of 1 .    
        assertEquals(2, board.getTargets().size()); // Results in 2 targets .    

        board.calcTargets(start, 2);
        assertEquals(3, board.getTargets().size());

        board.calcTargets(start, 3);
        assertEquals(6, board.getTargets().size());

        board.calcTargets(start, 6);
        assertEquals(7, board.getTargets().size());

        // Test roll 1, 2, and 3 from middle .    
        TestBoardCell center = board.getCell(2, 2);

        board.calcTargets(center, 1);
        assertEquals(4, board.getTargets().size());

        board.calcTargets(center, 2);
        assertEquals(8, board.getTargets().size());

        board.calcTargets(center, 3);
        assertEquals(11, board.getTargets().size());
    }

    // Tests entering a room (player stops when entering room)
    @Test
    public void testTargetsRoom() {
        board.getCell(1, 2).setRoom(true);

        board.calcTargets(board.getCell(0, 2), 2);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(4, targets.size());
        assertTrue(targets.contains(board.getCell(1, 2)));

        board.calcTargets(board.getCell(2, 2), 1);
        targets = board.getTargets();
        assertEquals(4, targets.size());
        assertTrue(targets.contains(board.getCell(1, 2)));
    }

    // Tests occupied cells (player cannot move into occupied cells)
    @Test
    public void testTargetsOccupied() {
        board.getCell(1, 0).setOccupied(true);

        board.calcTargets(board.getCell(0, 0), 2);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(1, targets.size());
        assertFalse(targets.contains(board.getCell(1, 0)));

        board.calcTargets(board.getCell(2, 1), 1);
        assertEquals(4, board.getTargets().size());
    }

    // Tests board with both a room and an occupied cell
    @Test
    public void testTargetsMixed() {
        board.getCell(0, 2).setRoom(true);
        board.getCell(1, 2).setOccupied(true);

        board.calcTargets(board.getCell(0, 1), 2);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(3, targets.size());
        assertTrue(targets.contains(board.getCell(0, 2)));
        assertFalse(targets.contains(board.getCell(1, 2)));

        board.calcTargets(board.getCell(2, 2), 3);
        assertEquals(8, board.getTargets().size());
    }
}