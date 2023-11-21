package sudoku.board;

import java.util.Objects;

/**
 * A class that represents the position of a particular square in a SudokuBoard object.
 * <p>
 * Stores only the row and column, and provides accessors for both. Defines the equals and hashCode
 * methods to support equality checking.
 *
 * @author Savraj Bassi
 * @version 20/11/2023
 */

public class BoardPosition {
    private final int row;
    private final int column;

    /**
     * Constructs a new BoardPosition object with the specified row and column.
     *
     * @param row    The row of the square
     * @param column The column of the square
     */
    public BoardPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the row of the square.
     *
     * @return The row of the square
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column of the square.
     *
     * @return The column of the square
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns a string representation of the BoardPosition object.
     *
     * @return A a string representation of the BoardPosition object
     */
    @Override
    public String toString() {
        return "{row: " + row + ", column: " + column + "}";
    }

    /**
     * Checks if this BoardPosition object is equal to another object.
     *
     * @param o The object to be compared
     * @return True if the objects are equal and false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardPosition that = (BoardPosition) o;
        return row == that.row && column == that.column;
    }

    /**
     * Generates a hash code for the BoardPosition object.
     *
     * @return The hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
