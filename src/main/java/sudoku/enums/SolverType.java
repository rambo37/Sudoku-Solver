package sudoku.enums;

import sudoku.solver.BestFirstSolver;
import sudoku.solver.BreadthFirstSolver;
import sudoku.solver.DepthFirstSolver;
import sudoku.solver.SudokuSolver;

/**
 * An enumeration representing different types of solvers for a Sudoku puzzle.
 *
 * @author Savraj Bassi
 * @version 06/12/2023
 */

public enum SolverType {
    BREADTH_FIRST("Breadth first", new BreadthFirstSolver()),
    DEPTH_FIRST("Depth first", new DepthFirstSolver()),
    BEST_FIRST("Best first", new BestFirstSolver());

    private final String displayName;
    private final SudokuSolver solver;

    /**
     * Constructs a SolverType object with the given display name and solver instance.
     *
     * @param displayName the display name of the solver type
     * @param solver      the solver instance of the corresponding solver type
     */
    SolverType(String displayName, SudokuSolver solver) {
        this.displayName = displayName;
        this.solver = solver;
    }

    /**
     * Returns the solver instance of the solver type.
     *
     * @return the solver instance
     */
    public SudokuSolver getSolver() {
        return solver;
    }

    /**
     * Returns the display name of the solver type.
     *
     * @return the display name
     */
    @Override
    public String toString() {
        return displayName;
    }
}
