package sudoku_solver.enums;

/**
 * An enumeration representing the size of a Sudoku board.
 *
 * @author Savraj Bassi
 * @version 06/12/2023
 */

public enum BoardSize {
    SIZE_4x4(4),
    SIZE_9x9(9),
    SIZE_16x16(16);

    private final int size;

    /**
     * Constructs a BoardSize object with the specified size.
     *
     * @param size the size of the board
     */
    BoardSize(int size) {
        this.size = size;
    }

    /**
     * Returns the size of the board.
     *
     * @return the size of the board
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns a string representation of the board size.
     *
     * @return a string representation of the board size
     */
    @Override
    public String toString() {
        return size + "x" + size;
    }
}
