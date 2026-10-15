package experiment;

import java.util.HashSet;
import java.util.Set;


// 4x4 grid board for movement experimentations.
public class TestBoard {
    private TestBoardCell[][] grid;
    public static final int ROWS = 4;
    public static final int COLS = 4;

    public TestBoard() {
        grid = new TestBoardCell[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                grid[r][c] = new TestBoardCell(r, c);
            }
        }
    }

    public void calcTargets(TestBoardCell startCell, int pathlength) {
        // Stub implementation for TDD
    }

    public TestBoardCell getCell(int row, int col) {
        return grid[row][col];
    }

    public Set<TestBoardCell> getTargets() {
        return new HashSet<>();
    }
}