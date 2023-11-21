package sudoku.solver;

import sudoku.board.SudokuBoard;

import java.util.Stack;

/**
 * This class represents a Sudoku solver based on depth-first search algorithm.
 * <p>
 * It extends the SudokuSolver abstract class and maintains an open list of Sudoku boards
 * represented as a Stack. It provides methods to solve a Sudoku puzzle using depth-first search
 * and retrieve the next Sudoku board from the open list.
 *
 * @author Savraj Bassi
 * @version 20/11/2023
 */

public class DepthFirstSolver extends SudokuSolver {

    /**
     * Constructs a new DepthFirstSolver object, initialising the openList as an empty Stack.
     */
    public DepthFirstSolver() {
        openList = new Stack<>();
    }

    /**
     * Retrieves the next Sudoku board from the open list, using the pop method.
     *
     * @return The next Sudoku board to be processed
     */
    @Override
    protected SudokuBoard getNextBoard() {
        return ((Stack<SudokuBoard>) openList).pop();
    }
}
