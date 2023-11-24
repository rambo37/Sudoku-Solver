package sudoku.solver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import sudoku.board.SudokuBoard;

import static org.junit.jupiter.api.Assertions.*;


public class SudokuSolverTest {
    private final String unsolvable = """
            *,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,
            1,*,*,*,*,*,*,1,*,
            *,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,
            """;
    private final String unsolvableSmall = """
            1,*,*,*,
            *,1,*,*,
            *,*,*,*,
            *,*,*,*,
            """;
    private final String unsolvableBig = """
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            *,*,1,*,*,*,*,*,*,1,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
            """;
    private final String easy = """
            2,3,*,9,1,5,*,*,*
            *,*,*,2,*,*,5,4,*
            6,*,7,*,*,*,*,*,*
            *,*,1,*,*,*,*,*,9
            8,9,*,5,*,3,*,1,7
            5,*,*,*,*,*,6,*,*
            *,*,*,*,*,*,9,*,5
            *,1,6,*,*,7,*,*,*
            *,*,*,3,2,9,*,*,1
            """;
    private final String easySmall = """
            *,2,3,4,
            4,3,*,1,
            3,*,4,2,
            2,4,1,*
            """;
    private final String easyBig = """
            16,8,14,3,12,13,4,6,7,5,15,2,10,11,9,1
            7,13,12,5,11,9,2,3,10,4,8,1,15,16,14,6
            1,10,9,4,16,15,14,8,3,12,11,6,2,7,13,5
            11,15,2,6,10,7,5,1,13,16,9,14,8,4,12,3
            2,12,1,14,7,6,8,15,9,11,10,3,16,13,5,4
            3,5,11,16,1,10,12,13,14,8,4,7,6,9,15,2
            15,7,13,9,2,4,16,14,1,6,5,12,3,8,11,10
            10,6,4,8,5,3,11,9,2,15,13,16,1,14,7,12
            13,14,10,2,4,5,3,16,15,7,6,9,12,1,8,11
            12,11,5,1,8,2,6,7,4,13,14,10,9,15,3,16
            9,3,6,7,13,12,15,10,11,1,16,8,5,2,4,14
            8,4,16,15,14,1,9,11,12,3,2,5,7,6,10,13
            4,2,7,10,15,16,13,12,6,9,3,11,14,5,1,8
            14,1,15,13,9,8,10,5,16,2,12,4,11,3,6,7
            6,16,8,12,3,11,1,4,5,14,7,15,13,10,2,9
            5,9,3,11,6,14,7,2,8,10,1,13,*,*,*,*
            """;
    private final String solved = """
             2,3,4,9,1,5,7,8,6
             1,8,9,2,7,6,5,4,3
             6,5,7,4,3,8,1,9,2
             4,6,1,7,8,2,3,5,9
             8,9,2,5,6,3,4,1,7
             5,7,3,1,9,4,6,2,8
             3,2,8,6,4,1,9,7,5
             9,1,6,8,5,7,2,3,4
             7,4,5,3,2,9,8,6,1
            """;
    private final String solvedSmall = """
            1,2,3,4,
            4,3,2,1,
            3,1,4,2,
            2,4,1,3
            """;
    private final String solvedBig = """
            16,8,14,3,12,13,4,6,7,5,15,2,10,11,9,1
            7,13,12,5,11,9,2,3,10,4,8,1,15,16,14,6
            1,10,9,4,16,15,14,8,3,12,11,6,2,7,13,5
            11,15,2,6,10,7,5,1,13,16,9,14,8,4,12,3
            2,12,1,14,7,6,8,15,9,11,10,3,16,13,5,4
            3,5,11,16,1,10,12,13,14,8,4,7,6,9,15,2
            15,7,13,9,2,4,16,14,1,6,5,12,3,8,11,10
            10,6,4,8,5,3,11,9,2,15,13,16,1,14,7,12
            13,14,10,2,4,5,3,16,15,7,6,9,12,1,8,11
            12,11,5,1,8,2,6,7,4,13,14,10,9,15,3,16
            9,3,6,7,13,12,15,10,11,1,16,8,5,2,4,14
            8,4,16,15,14,1,9,11,12,3,2,5,7,6,10,13
            4,2,7,10,15,16,13,12,6,9,3,11,14,5,1,8
            14,1,15,13,9,8,10,5,16,2,12,4,11,3,6,7
            6,16,8,12,3,11,1,4,5,14,7,15,13,10,2,9
            5,9,3,11,6,14,7,2,8,10,1,13,4,12,16,15
            """;
    private final String game1 = """
            *,*,*,*,5,6,*,9,*
            *,5,*,4,*,*,*,*,*
            *,6,*,3,8,*,*,4,5
            *,*,6,*,*,*,*,*,*
            *,*,*,*,*,*,*,2,*
            3,*,*,*,1,*,8,*,*
            *,*,*,*,4,*,7,*,8
            8,*,*,7,*,3,2,6,*
            *,*,7,8,*,5,*,*,4
            """;
    private final String game2 = """
            2,*,*,7,*,*,*,*,*
            5,*,*,*,*,1,*,*,*
            *,*,8,*,*,9,*,6,3
            9,*,*,*,*,4,*,*,*
            *,*,*,*,*,*,8,3,*
            *,4,1,*,6,*,*,*,9
            *,9,7,*,*,8,6,*,*
            *,*,4,*,*,*,*,1,5
            *,*,*,*,*,*,3,*,*
            """;
    private final String smallGame1 = """
            1,2,3,4,
            *,*,*,*,
            *,*,*,*,
            *,4,*,*
            """;

    private final String smallGame2 = """
            *,4,*,*,
            *,*,*,*,
            *,*,*,*,
            *,*,1,*
            """;
    private final String bigGame1 = """
            *,5,*,14,13,*,7,*,*,*,3,10,11,*,1,*
            11,*,*,*,12,14,*,*,1,4,*,9,*,*,*,6
            4,*,*,*,11,10,3,8,16,*,*,*,*,2,*,13
            *,*,12,*,*,*,*,*,*,2,6,11,*,3,7,*
            *,*,15,7,*,*,4,14,10,12,*,*,8,11,*,3
            *,11,*,4,9,*,10,*,3,*,*,2,*,16,14,15
            8,*,10,*,6,*,*,*,14,*,*,*,9,7,4,2
            *,14,*,*,8,13,*,*,9,11,*,4,*,*,5,1
            *,*,*,*,*,16,11,*,*,7,*,*,13,*,8,5
            *,*,*,*,*,8,*,15,*,*,9,*,*,1,3,*
            15,1,5,16,*,*,13,*,*,10,2,12,4,*,*,11
            *,*,3,*,*,6,*,*,*,*,*,1,*,9,*,12
            7,*,14,*,15,*,*,13,*,*,*,*,*,*,*,*
            *,*,9,*,*,*,2,16,15,*,*,*,14,12,10,*
            *,16,*,11,10,4,*,*,*,*,*,14,1,13,15,*
            6,15,*,12,14,3,8,11,*,13,10,*,*,5,*,*
            """;
    private final String bigGame2 = """
            4,5,1,2,13,*,12,8,16,*,15,*,3,*,*,*
            14,*,*,*,*,10,*,*,*,2,*,*,*,*,1,*
            3,*,7,*,*,9,2,1,4,*,13,*,6,*,*,*
            *,*,11,6,*,*,5,*,*,12,*,*,13,*,4,2
            *,*,6,4,1,*,*,*,12,15,*,9,10,*,*,*
            9,*,8,*,10,*,6,*,7,*,*,*,16,12,*,*
            *,*,5,15,3,2,*,7,10,11,6,*,4,*,*,1
            *,*,*,7,*,*,11,*,*,4,*,16,*,13,*,*
            15,*,3,*,*,13,*,*,*,14,11,12,*,*,16,*
            11,*,*,*,*,*,*,2,15,6,*,*,*,*,*,3
            2,6,12,*,5,3,*,*,1,*,*,10,15,11,*,7
            8,*,9,5,6,*,15,*,13,*,*,*,12,*,14,*
            10,*,2,*,*,*,*,*,*,9,*,11,*,16,*,13
            *,*,13,*,*,5,*,14,*,*,2,*,*,15,3,6
            *,1,*,3,*,15,8,*,*,*,*,7,9,2,10,*
            *,*,4,*,2,*,9,13,*,*,14,*,11,*,*,8
            """;

    private final String[] solvableGames = new String[]{game1, game2, smallGame1, smallGame2,
            bigGame1, bigGame2};

    SudokuSolver breadthFirstSolver;
    SudokuSolver depthFirstSolver;
    SudokuSolver bestFirstSolver;

    @BeforeEach
    public void createSolvers() {
        breadthFirstSolver = new BreadthFirstSolver();
        depthFirstSolver = new DepthFirstSolver();
        bestFirstSolver = new BestFirstSolver();
    }

    /**
     * Runs the given solver on all the solvable games to see the solver is able to solve them
     *
     * @param solver The solver to be tested
     */
    private void runSolverOnSolvableGames(SudokuSolver solver) {
        for (String solvableGame : solvableGames) {
            assertSolverSolvesBoard(solver, solvableGame);
        }
    }

    /**
     * Tests that the given solver is able to solve the provided game
     *
     * @param solver The solver to be tested
     * @param game   A string representation of the Sudoku game to be solved
     */
    private void assertSolverSolvesBoard(SudokuSolver solver, String game) {
        SudokuBoard board = solver.solve(game);
        assertTrue(board.solved());
        assertTrue(board.verifySolution());
    }

    @Nested
    class BreadthFirstSolverTest {
        // When given an unsolvable game, an exception should be thrown
        @Test
        public void testUnsolvableGameThrowsException() {
            assertThrows(IllegalStateException.class,
                    () -> breadthFirstSolver.solve(unsolvable));
            assertThrows(IllegalStateException.class,
                    () -> breadthFirstSolver.solve(unsolvableSmall));
            assertThrows(IllegalStateException.class,
                    () -> breadthFirstSolver.solve(unsolvableBig));
        }

        // Solver should solve a game that is solved automatically by the code in SudokuBoard
        @Test
        public void testEasilySolvedGameGivenToSolveMethod() {
            assertSolverSolvesBoard(breadthFirstSolver, easy);
            assertSolverSolvesBoard(breadthFirstSolver, easySmall);
            assertSolverSolvesBoard(breadthFirstSolver, easyBig);
        }

        // An already solved game should be solvable
        @Test
        public void testSolvedGameGivenToSolveMethod() {
            assertSolverSolvesBoard(breadthFirstSolver, solved);
            assertSolverSolvesBoard(breadthFirstSolver, solvedSmall);
            assertSolverSolvesBoard(breadthFirstSolver, solvedBig);
        }

        // Run the solver on all the solvable games and ensure they are all correctly solved
        @Test
        public void testUnsolvedButSolvableGames() {
            runSolverOnSolvableGames(breadthFirstSolver);
        }

        // Check the average times are updated
        @Test
        public void testAverageTime() {
            assertEquals(breadthFirstSolver.getAverageTime(bigGame1), 0);
            breadthFirstSolver.solve(bigGame1);
            assertTrue(breadthFirstSolver.getAverageTime(bigGame1) > 0);
        }
    }


    @Nested
    class DepthFirstSolverTest {
        @Test
        public void testUnsolvableGameThrowsException() {
            assertThrows(IllegalStateException.class, () -> depthFirstSolver.solve(unsolvable));
            assertThrows(IllegalStateException.class,
                    () -> depthFirstSolver.solve(unsolvableSmall));
            assertThrows(IllegalStateException.class, () -> depthFirstSolver.solve(unsolvableBig));
        }

        @Test
        public void testEasilySolvedGameGivenToSolveMethod() {
            assertSolverSolvesBoard(depthFirstSolver, easy);
            assertSolverSolvesBoard(depthFirstSolver, easySmall);
            assertSolverSolvesBoard(depthFirstSolver, easyBig);
        }

        @Test
        public void testSolvedGameGivenToSolveMethod() {
            assertSolverSolvesBoard(depthFirstSolver, solved);
            assertSolverSolvesBoard(depthFirstSolver, solvedSmall);
            assertSolverSolvesBoard(depthFirstSolver, solvedBig);
        }

        @Test
        public void testUnsolvedButSolvableGames() {
            runSolverOnSolvableGames(depthFirstSolver);
        }

        @Test
        public void testAverageTime() {
            assertEquals(depthFirstSolver.getAverageTime(bigGame1), 0);
            depthFirstSolver.solve(bigGame1);
            assertTrue(depthFirstSolver.getAverageTime(bigGame1) > 0);
        }
    }


    @Nested
    class BestFirstSolverTest {
        @Test
        public void testUnsolvableGameThrowsException() {
            assertThrows(IllegalStateException.class, () -> bestFirstSolver.solve(unsolvable));
            assertThrows(IllegalStateException.class, () -> bestFirstSolver.solve(unsolvableSmall));
            assertThrows(IllegalStateException.class, () -> bestFirstSolver.solve(unsolvableBig));
        }

        @Test
        public void testEasilySolvedGameGivenToSolveMethod() {
            assertSolverSolvesBoard(bestFirstSolver, easy);
            assertSolverSolvesBoard(bestFirstSolver, easySmall);
            assertSolverSolvesBoard(bestFirstSolver, easyBig);
        }

        @Test
        public void testSolvedGameGivenToSolveMethod() {
            assertSolverSolvesBoard(bestFirstSolver, solved);
            assertSolverSolvesBoard(bestFirstSolver, solvedSmall);
            assertSolverSolvesBoard(bestFirstSolver, solvedBig);
        }

        @Test
        public void testUnsolvedButSolvableGames() {
            runSolverOnSolvableGames(bestFirstSolver);
        }

        @Test
        public void testAverageTime() {
            assertEquals(bestFirstSolver.getAverageTime(bigGame1), 0);
            bestFirstSolver.solve(bigGame1);
            assertTrue(bestFirstSolver.getAverageTime(bigGame1) > 0);
        }
    }
}