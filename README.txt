## JUnit Test Corrections

While implementing and testing the movement calculations for Clue Paths 2, we discovered that some of our original JUnit tests had incorrect expected target counts. We reviewed the possible movement paths on the 4x4 board and corrected the tests based on the movement rules, rather than changing them simply to pass.

**testTargetsOccupied():**
- Changed the expected target count from 1 to 2.
- Starting at (0,0) with a roll of 2 and (1,0) occupied, the player can reach both (0,2) and (1,1) through (0,1). We originally overlooked one of these paths.

**testTargetsNormal():**
- Changed the expected target count from 8 to 6 for a roll of 2 starting at (2,2).
- Changed the expected target count from 11 to 8 for a roll of 3 starting at (2,2).
- We originally overcounted possible destinations because multiple movement paths can lead to the same cell. Each unique destination should only be counted once.

**testTargetsMixed():**
- Changed the expected target count from 8 to 7 for a roll of 3 starting at (2,2), with (0,2) marked as a room and (1,2) occupied.
- We reviewed the reachable cells and confirmed that the occupied cell blocks certain movement paths. The corrected count reflects the valid destinations under these conditions.

After correcting the test expectations, all 9 JUnit tests passed with 0 failures.