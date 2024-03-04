package sudoku_solver.solver;

import sudoku_solver.model.SudokuBoard;

import java.util.*;

/**
 * This class is an abstract class for a Sudoku solver.
 * <p>
 * It provides methods to solve a Sudoku puzzle using an open list of Sudoku boards, and to
 * retrieve a list of all the times taken to solve a particular puzzle.
 *
 * @author Savraj Bassi
 * @version 04/03/2024
 */

public abstract class SudokuSolver {
    private final Collection<SudokuBoard> openList;
    private final Map<String, ArrayList<Long>> timesMap = new HashMap<>();

    /**
     * Constructs a new SudokuSolver object with the specified openList.
     *
     * @param openList The collection of SudokuBoard objects to be used as the open list for solving
     *                 Sudoku puzzles
     */
    public SudokuSolver(Collection<SudokuBoard> openList) {
        this.openList = openList;
    }

    /**
     * Retrieves the open list of SudokuBoard objects.
     *
     * @return The collection of SudokuBoard objects used as the open list
     */
    protected Collection<SudokuBoard> getOpenList() {
        return openList;
    }

    /**
     * Solves the Sudoku puzzle specified by the String board.
     *
     * @param board The String representation of the Sudoku board
     * @return A solved SudokuBoard object if it exists and otherwise null
     */
    public SudokuBoard solve(String board) {
        openList.clear();
        long startTime = System.currentTimeMillis();

        // The initial starting board, which may already even be complete
        SudokuBoard sudokuBoard = new SudokuBoard(board);
        openList.add(sudokuBoard);

        while (!openList.isEmpty()) {
            // Terminate the thread execution if an interrupt is issued (this may happen if a board
            // is taking too long to solve)
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }
            SudokuBoard current = getNextBoard();
            if (current.solved() && current.verifySolution()) {
                long endTime = System.currentTimeMillis();
                long timeTaken = endTime - startTime;
                if (timesMap.containsKey(board)) {
                    ArrayList<Long> times = timesMap.get(board);
                    times.add(timeTaken);
                } else {
                    ArrayList<Long> times = new ArrayList<>();
                    times.add(timeTaken);
                    timesMap.put(board, times);
                }
                System.out.println("time taken: " + timeTaken + " ms");
                if (current == sudokuBoard) {
                    System.out.println("Solver was given a game that was already solved or that " +
                            "was solved by constraint propagation within SudokuBoard");
                }
                return current;
            }
            openList.addAll(current.getSuccessors());
        }

        return null;
    }

    /**
     * Gets the next Sudoku board from the open list. Different subclasses can use different data
     * structures and therefore may return a different SudokuBoard.
     *
     * @return The next Sudoku board to be processed
     */
    protected abstract SudokuBoard getNextBoard();

    /**
     * Gets a list of all the times taken to solve a particular puzzle represented by the given
     * board.
     *
     * @param board The board representation
     * @return The average time taken to solve the puzzle, or 0 if no solve times are recorded for
     * the board
     */
    public List<Long> getTimes(String board) {
        return timesMap.get(board);
    }
}