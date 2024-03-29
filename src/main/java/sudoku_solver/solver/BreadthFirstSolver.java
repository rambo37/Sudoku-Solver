package sudoku_solver.solver;

import sudoku_solver.model.SudokuBoard;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a Sudoku solver based on breadth-first search algorithm.
 * <p>
 * It extends the SudokuSolver abstract class and maintains an open list of Sudoku boards
 * represented as a Queue. It provides methods to solve a Sudoku puzzle using breadth-first search
 * and retrieve the next Sudoku board from the open list.
 *
 * @author Savraj Bassi
 * @version 04/03/2024
 */

public class BreadthFirstSolver extends SudokuSolver {

    /**
     * Constructs a new BreadthFirstSolver object, initialising the openList as an empty LinkedList.
     */
    public BreadthFirstSolver() {
        super(new LinkedList<>());
    }

    /**
     * Retrieves the next Sudoku board from the open list, using the remove method.
     * @return The next Sudoku board to be processed
     */
    @Override
    protected SudokuBoard getNextBoard() {
        return ((Queue<SudokuBoard>) getOpenList()).remove();
    }
}
