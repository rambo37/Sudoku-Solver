package sudoku.solver;

import sudoku.model.SudokuBoard;

import java.util.PriorityQueue;

/**
 * This class represents a Sudoku solver based on best-first search algorithm.
 * <p>
 * It extends the SudokuSolver abstract class and maintains an open list of Sudoku boards
 * represented as a PriorityQueue. It provides methods to solve a Sudoku puzzle using best-first
 * search and retrieve the next Sudoku board from the open list.
 *
 * @author Savraj Bassi
 * @version 20/11/2023
 */

public class BestFirstSolver extends SudokuSolver {

    /**
     * Constructs a new BestFirstSolver object, initialising the openList as an empty PriorityQueue.
     */
    public BestFirstSolver() {
        openList = new PriorityQueue<>();
    }

    /**
     * Retrieves the next Sudoku board from the open list, using the remove method.
     *
     * @return The next Sudoku board to be processed
     */
    @Override
    protected SudokuBoard getNextBoard() {
        return ((PriorityQueue<SudokuBoard>) openList).remove();
    }
}
