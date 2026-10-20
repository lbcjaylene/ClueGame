package experiment;
import java.util.HashSet;

import java.util.Set;

public class TestBoard {

    // 4x4 grid dimensions
    public static final int ROWS = 4;
    public static final int COLS = 4;

    // 2D array for board 
    private TestBoardCell[][] Grid;

    // tracking target movement + visited cells
    private Set<TestBoardCell> targets;
    private Set<TestBoardCell> visitedCells;

    // Constructor: initializes the board grid  .    
    public TestBoard() {
        Grid = new TestBoardCell[ROWS][COLS];
        targets= new HashSet<>();
        visitedCells = new HashSet<>();

        // Create a new cell object for every slot in grid
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c <COLS; c++) {
                Grid[r][c] = new TestBoardCell(r, c);
            }
        }

        setupAdjacencies();
    }

    // Helper method to add valid neighbors (up, down, left, right) .    
    private void setupAdjacencies() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                TestBoardCell current = Grid[r][c];

                // Check Up 
                if (r -1 >=0) {
                    current.addAdjacency(Grid[r - 1][c]);
                }
                // Check Down 
                if (r + 1 < ROWS) {
                    current.addAdjacency(Grid[r + 1][c]);
                }
                // Check Left 
                if (c- 1 >= 0) {
                    current.addAdjacency(Grid[r][c- 1]);
                }
                // Check Right neighbor
                if (c + 1 <COLS) {
                    current.addAdjacency(Grid[r][c + 1]);
                }
            }
        }
    }

    public void calcTargets(TestBoardCell startCell, int pathlength) {
        targets.clear();
        visitedCells.clear();
        
        // Mark starting location as visited so no backtrack .    
        visitedCells.add(startCell);

      
        TargetsHelper(startCell, pathlength);
    }

    
    private void TargetsHelper(TestBoardCell currentCell, int stepsRemaining) {
        for (TestBoardCell neighbor : currentCell.getAdjList()) {

            // Skip cells we already visited
            if (visitedCells.contains(neighbor)|| neighbor.getOccupied()) {
                continue;
            }

           
            visitedCells.add(neighbor);

            // Entering a room stops movement immediately. Reaching 1 step to go means the neighbor is final target .        
            if (neighbor.isRoom() || stepsRemaining == 1) {
                targets.add(neighbor);
            } else {
                TargetsHelper(neighbor,stepsRemaining - 1);
            }

            // remove from visited so can be used by other .        
            visitedCells.remove(neighbor);
        }
    }

    public TestBoardCell getCell(int row, int col) {
        return Grid[row][col];
    }

    public Set<TestBoardCell> getTargets() {
        return targets;
    }
}



