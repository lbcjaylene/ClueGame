/**
 * TestBoard Class
 *
 * Represents a 4x4 Clue game board that calculates
 * adjacency lists for each cell and determines
 * valid movement targets based on the number of steps,
 * occupied cells, and room locations
 *
 * @author Jaylene Khourn, Trisha Varadaraj
 * 
 */

package experiment;
import java.util.HashSet;

import java.util.Set;

public class TestBoard {

    // 4x4 grid dimensions
    public static final int ROWS = 4;
    public static final int COLS = 4;

    // 2D array for board 
    private TestBoardCell[][] grid;

    // tracking target movement + visited cells
    private Set<TestBoardCell> targets;
    private Set<TestBoardCell> visitedCells;

    // constructor: initializes the board grid  .   
    
    public TestBoard() {
        grid = new TestBoardCell[ROWS][COLS];
        targets = new HashSet<>();
        visitedCells = new HashSet<>();

        // creates a new cell object for every slot in grid
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c <COLS; c++) {
                grid[r][c] = new TestBoardCell(r, c);
            }
        }
        setupAdjacencies();
    }

    // helper method to add valid neighbors (up, down, left, right) .    
    
    private void setupAdjacencies() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                TestBoardCell current = grid[r][c];

                // Check Up 
                if (r - 1 >= 0) {
                    current.addAdjacency(grid[r - 1][c]);
                }
                // Check Down 
                if (r + 1 < ROWS) {
                    current.addAdjacency(grid[r + 1][c]);
                }
                // Check Left neighbor
                if (c - 1 >= 0) {
                    current.addAdjacency(grid[r][c- 1]);
                }
                // Check Right neighbor
                if (c + 1 <COLS) {
                    current.addAdjacency(grid[r][c + 1]);
                }
            }
        }
    }

    // calculates all possible movement targets from a starting cell
    // based on the number of steps
    
    public void calcTargets(TestBoardCell startCell, int pathlength) {
        targets.clear();
        visitedCells.clear();
        
        // prevents unnecessary recursion if an invalid movement length is passed
        if (pathlength <= 0) {
        	return;
        }
        
        // Mark starting location as visited so there is no backtrack .    
        visitedCells.add(startCell);
        targetsHelper(startCell, pathlength);
    }

    // recursively finds valid movement targets by checking adjacent cells
    
    private void targetsHelper(TestBoardCell currentCell, int stepsRemaining) {
        for (TestBoardCell neighbor : currentCell.getAdjList()) {

            // Skip cells we already visited
            if (visitedCells.contains(neighbor) || neighbor.getOccupied()) {
                continue;
            }
            visitedCells.add(neighbor);

            // Entering a room stops movement immediately. Reaching 1 step to go means the neighbor is final target .        
            if (neighbor.isRoom() || stepsRemaining == 1) {
                targets.add(neighbor);
            } else {
                targetsHelper(neighbor,stepsRemaining - 1);
            }

            // remove the cell so other movement paths can be used .        
            visitedCells.remove(neighbor);
        }
    }

    public TestBoardCell getCell(int row, int col) {
        return grid[row][col];
    }

    public Set<TestBoardCell> getTargets() {
        return targets;
    }
}



