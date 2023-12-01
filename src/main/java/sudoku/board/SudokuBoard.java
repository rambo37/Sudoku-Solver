package sudoku.board;

import java.util.*;

/**
 * This class represents a Sudoku board.
 * <p>
 * The Sudoku board is represented internally as a two-dimensional array of CandidateSet objects.
 * Each CandidateSet represents the set of candidates for a particular square on the board. The
 * SudokuBoard class also maintains a separate boolean array to keep track of whether a value has
 * been set for each square.
 * <p>
 * The size of the Sudoku board, the total number of squares, and the size of each box within the
 * board are all stored as attributes in the class. The number of filled squares and the total
 * number of candidates are also tracked as they are used to compute a heuristic value.
 * <p>
 * Instances are created from a String representation of a Sudoku board. The String should use new
 * line characters to separate rows, commas to separate values, and any non-numerical character for
 * initially blank squares. If the String representation specifies an unwinnable game, an exception
 * is thrown. Games must be either 4x4, 9x9 or 16x16.
 * <p>
 * The class uses constraint propagation to reduce the search space and automatically fill squares
 * with only 1 legal value remaining. As such, games that can be solved entirely without any
 * guessing are already solved upon creation.
 * <p>
 * This class does not provide any way of directly accessing or modifying squares externally.
 * Rather, it provides a public getSuccessors() function to generate valid successors of the current
 * game. It also implements the Comparable interface and overrides the compareTo method to
 * facilitate the use of searching algorithms that rely on heuristics.
 * <p>
 * Also provided are the solved() and verifySolution() methods that can be used to check if a game
 * has been solved. The solved() function should be used in order to quickly check a game has been
 * finished, whereas the verifySolution() function should be used to verify the finished game has
 * been correctly solved. verifySolution() should only be called after solved() has been called and
 * returned true.
 *
 * @author Savraj Bassi
 * @version 25/11/2023
 */

public class SudokuBoard implements Comparable<SudokuBoard> {
    private CandidateSet[][] board;
    private boolean[][] hasValueSet;
    private int SIZE;
    private int BOX_SIZE;
    private int squaresRemaining;
    private int totalNumberOfCandidates;
    private final static int MAX_BOARD_SIZE = 16;
    // 2D arrays to provide efficient access to all BoardPositions for a specific row/column
    private final static BoardPosition[][] rows;
    private final static BoardPosition[][] columns;

    // Initialise the arrays to contain all valid board positions for every row/column of the
    // largest possible board size. If the actual size is less, a copy of these arrays with the
    // appropriate size will be created when needed
    static {
        rows = new BoardPosition[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
        columns = new BoardPosition[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
        for (int i = 0; i < MAX_BOARD_SIZE; i++) {
            BoardPosition[] row = new BoardPosition[MAX_BOARD_SIZE];
            BoardPosition[] column = new BoardPosition[MAX_BOARD_SIZE];
            for (int j = 0; j < MAX_BOARD_SIZE; j++) {
                row[j] = new BoardPosition(i, j);
                column[j] = new BoardPosition(j, i);
            }
            rows[i] = row;
            columns[i] = column;
        }
    }

    /**
     * Initialises the fields of the SudokuBoard class that require initialisation. Used exclusively
     * by the clone method to only perform the bare minimum amount of initialisation for improved
     * performance.
     *
     * @param size The size of the Sudoku board (e.g., 9 for a 9 by 9 board)
     */
    private SudokuBoard(int size) {
        initialiseFields(size);
    }

    /**
     * Initialises the fields of the SudokuBoard class that require initialisation.
     * @param size The size of the Sudoku board (e.g., 9 for a 9 by 9 board)
     */
    private void initialiseFields(int size) {
        board = new CandidateSet[size][size];
        hasValueSet = new boolean[size][size];
        SIZE = size;
        squaresRemaining = size * size;
        BOX_SIZE = (int) Math.sqrt(size);
        totalNumberOfCandidates = size * size * size;
    }

    /**
     * Creates a new SudokuBoard from a String representation of a Sudoku board. If during the
     * creation process it becomes evident the game is unsolvable, throws an IllegalStateException.
     * If the board has an invalid size, an IllegalArgumentException is thrown. The only valid sizes
     * are 4x4, 9x9 and 16x16.
     *
     * @param board The String representation of a Sudoku board. Each row of the board should be on
     *              a separate line and the values within a row should be separated by commas.
     *              Any non-numerical character can be used for empty squares.
     */
    public SudokuBoard(String board) {
        String[] rows = board.split("\n");
        int size = rows.length;
        if (size != 4 && size != 9 && size != 16) {
            throw new IllegalArgumentException("Invalid board size. Only 4x4, 9x9 and 16x16 " +
                    "boards are supported.");
        }

        initialiseFields(size);
        initialiseBoard();

        for (int row = 0; row < size; row++) {
            String currentRow = rows[row];
            String[] squares = currentRow.split(",");
            // Row does not contain the right amount of values
            if (squares.length != size) {
                throw new IllegalArgumentException("Invalid board size. Only 4x4, 9x9 and 16x16 " +
                        "boards are supported.");
            }
            for (int column = 0; column < size; column++) {
                String currentSquare = squares[column];
                if (Character.isDigit(currentSquare.charAt(0))) {
                    int value = Integer.parseInt(currentSquare);
                    // Do not attempt to set the square value if it has already got a value set -
                    // this could have happened due to an earlier iteration's call to setSquareValue
                    // triggering the setting of the current square's value
                    if (!hasValueSet[row][column] && !setSquareValue(row, column, value)) {
                        throw new IllegalStateException("Unsolvable Sudoku game");
                    }
                }
            }
        }
    }

    /**
     * Initialises the board variable with a fresh CandidateSet object for each square. These sets
     * store all possible legal values for their respective square. This corresponds to all values
     * from 1 up to the board size (inclusive) upon creation.
     */
    private void initialiseBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                board[row][column] = new CandidateSet(SIZE);
            }
        }
    }

    /**
     * Sets the value of the square at board[row][column] to the specified value. Updates some
     * variables and calls updateBoard to perform constraint propagation. The return value indicates
     * whether the game is potentially winnable after setting the value of the square.
     *
     * @param row    The row of the square
     * @param column The column of the square
     * @param value  The value that is to be assigned to the square
     * @return True if the assignment does not result in an unwinnable game and false otherwise
     */
    private boolean setSquareValue(int row, int column, int value) {
        int previousSize = board[row][column].size();
        board[row][column].assignValue(value);
        hasValueSet[row][column] = true;
        squaresRemaining--;
        totalNumberOfCandidates -= (previousSize - 1);
        return updateBoard(row, column, value);
    }

    /**
     * Performs constraint propagation once a square has been assigned a value. Updates the row,
     * column and box of the square that was just assigned a value. The return value indicates
     * whether the game is potentially winnable after updating the board following the setting of a
     * square.
     *
     * @param row           The row of the square that was just assigned a value
     * @param column        The column of the square that was just assigned a value
     * @param assignedValue The value that was assigned to the square
     * @return True if the assignment did not result in an unwinnable game and false otherwise
     */
    private boolean updateBoard(int row, int column, int assignedValue) {
        // Eliminates the assigned value from all other squares in the same row. If the returned
        // value from excludeValueFromCandidateSets is false, does not bother performing the update
        // to the other squares in the same column/box.
        BoardPosition[] boardRow = getRow(row);
        if (!excludeValueFromCandidateSets(row, column, assignedValue, boardRow)) return false;

        BoardPosition[] boardColumn = getColumn(column);
        if (!excludeValueFromCandidateSets(row, column, assignedValue, boardColumn)) return false;

        BoardPosition[] box = getBox(row, column);
        if (!excludeValueFromCandidateSets(row, column, assignedValue, box)) return false;

        // Attempts to eliminate any naked pairs. Performed on a one-by-one basis as before to
        // prevent unnecessarily performing computation on an unwinnable game.
        if (!checkForAndEliminateNakedPairs(boardRow)) return false;
        if (!checkForAndEliminateNakedPairs(boardColumn)) return false;
        return checkForAndEliminateNakedPairs(box);
    }

    /**
     * Attempts to eliminate a particular candidate from an array of BoardPositions, except for the
     * square at board[row][column] - this is the position of the square that was just assigned a
     * value, so we do not want to remove the value from the set of candidate values of that square.
     * The return value indicates whether the game is potentially winnable after eliminating the
     * candidate value from the other squares.
     *
     * @param row           The row of the square to exclude
     * @param column        The column of the square to exclude
     * @param assignedValue The value that we want to remove from the candidate sets
     * @param squares       An array of the board positions whose sets should have the assigned
     *                      value removed
     * @return True if the elimination did not result in an unwinnable game and false otherwise
     */
    private boolean excludeValueFromCandidateSets(int row, int column, int assignedValue,
                                                  BoardPosition[] squares) {
        for (BoardPosition square : squares) {
            int currentRow = square.getRow();
            int currentColumn = square.getColumn();

            // Ensure that the current square is not the one we wish to exclude before removing
            if (board[row][column] != board[currentRow][currentColumn]) {
                if (!removeValueFromCandidateSet(assignedValue, currentRow, currentColumn)) {
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * Performs the removal of a specific value from the set of candidate values for the square at
     * board[row][column]. If after removing the value the candidate set ends up empty, returns
     * false since that square can no longer have any value. If instead the size of the candidate
     * set becomes 1, automatically calls setSquareValue on that square. The return value indicates
     * whether the game is potentially winnable after eliminating the candidate value.
     *
     * @param value  The candidate value to remove
     * @param row    The row of the square whose candidate set we want to remove the value from
     * @param column The column of the square whose candidate set we want to remove the value from
     * @return True if the elimination did not result in an unwinnable game and false otherwise
     */
    private boolean removeValueFromCandidateSet(int value, int row, int column) {
        CandidateSet candidateSet = board[row][column];
        boolean removed = candidateSet.remove(value);
        if (removed) totalNumberOfCandidates--;

        // No more possible values
        if (candidateSet.isEmpty()) return false;

        // This square has now only got 1 possible legal value but was not assigned, so assign it
        if (candidateSet.size() == 1 && !hasValueSet[row][column]) {
            int candidate = candidateSet.getOnlyCandidate();
            return setSquareValue(row, column, candidate);
        }

        return true;
    }

    /**
     * Attempts to locate and eliminate any naked pairs for a collection of squares from the same
     * row, column or box. The return value indicates whether the game is potentially winnable after
     * eliminating any discovered naked pairs.
     *
     * @param squares An array of squares to check and eliminate naked pairs from
     * @return True if the elimination(s) did not result in an unwinnable game and false otherwise
     */
    private boolean checkForAndEliminateNakedPairs(BoardPosition[] squares) {
        // Map to keep track of which squares have the same candidate set
        Map<CandidateSet, BoardPosition> positionMap = new HashMap<>();
        for (BoardPosition square : squares) {
            CandidateSet candidateSet = board[square.getRow()][square.getColumn()];
            if (candidateSet.size() == 2) {
                // If the map already contains the candidate set of the current square, that means
                // there are 2 squares with the same candidate set. Therefore, perform the naked
                // pair elimination
                if (positionMap.containsKey(candidateSet)) {
                    // This is the other square with the same candidate set
                    BoardPosition otherSquare = positionMap.get(candidateSet);
                    int[] candidates = candidateSet.getCandidates();
                    int value1 = candidates[0];
                    int value2 = candidates[1];
                    if (!eliminateNakedPair(squares, square, otherSquare, value1, value2)) {
                        return false;
                    }
                } else {
                    positionMap.put(candidateSet, square);
                }
            }
        }
        return true;
    }

    /**
     * Performs the elimination of a particular naked pair. The return value indicates whether
     * the game is potentially winnable after eliminating the naked pair.
     *
     * @param squares An array of squares to eliminate the naked pair values from
     * @param square1 The first of the naked pair
     * @param square2 The second square of the naked pair
     * @param value1  The first value that is shared by both squares
     * @param value2  The second value that is shared by both squares
     * @return True if the elimination did not result in an unwinnable game and false otherwise
     */
    private boolean eliminateNakedPair(BoardPosition[] squares, BoardPosition square1,
                                       BoardPosition square2, int value1, int value2) {
        for (BoardPosition square : squares) {
            if (square != square1 && square != square2) {
                if (!removeValueFromCandidateSet(value1, square.getRow(), square.getColumn())) {
                    return false;
                }
                if (!removeValueFromCandidateSet(value2, square.getRow(), square.getColumn())) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns an array of BoardPositions objects of all the squares in a particular row.
     *
     * @param row The row for which all squares should be returned
     * @return An array of BoardPosition objects where each position corresponds to a particular
     * square in that row
     */
    private BoardPosition[] getRow(int row) {
        if (SIZE == MAX_BOARD_SIZE) return rows[row];
        return Arrays.copyOfRange(rows[row], 0, SIZE);
    }

    /**
     * Returns an array of BoardPositions objects of all the squares in a particular column.
     *
     * @param column The column for which all squares should be returned
     * @return An array of BoardPosition objects where each position corresponds to a particular
     * square in that column
     */
    private BoardPosition[] getColumn(int column) {
        if (SIZE == MAX_BOARD_SIZE) return columns[column];
        return Arrays.copyOfRange(columns[column], 0, SIZE);
    }

    /**
     * Returns an array of BoardPositions objects of all the squares in a particular box.
     *
     * @param row    The row of the square for which all squares in the same box should be returned
     * @param column The column of the square for which all squares in the same box should be
     *               returned
     * @return An array of BoardPosition objects where each position corresponds to a particular
     * square in that box
     */
    private BoardPosition[] getBox(int row, int column) {
        BoardPosition[] box = new BoardPosition[SIZE];

        // The index of the first row of the box
        int beginRow = getBoxStartIndex(row);
        // The index of the first column of the box
        int beginColumn = getBoxStartIndex(column);

        // An index into the box array
        int index = 0;
        for (int i = beginRow; i < beginRow + BOX_SIZE; i++) {
            for (int j = beginColumn; j < beginColumn + BOX_SIZE; j++) {
                box[index] = new BoardPosition(i, j);
                index++;
            }
        }

        return box;
    }

    /**
     * Returns the starting index of a box given a row/column index. For example, if we have a 9 by
     * 9 Sudoku board, the box start indexes are 0, 3, and 6.
     *
     * @param index The row/column index that we want to determine the box index for
     * @return The starting index of the box
     */
    private int getBoxStartIndex(int index) {
        return index / BOX_SIZE * BOX_SIZE;
    }

    /**
     * Checks if every square has been assigned a value. Note that this method does not verify the
     * correctness of the board, just that it has been filled. Runs in O(1) time.
     *
     * @return True if every square has been filled with a single value
     */
    public boolean solved() {
        return squaresRemaining == 0;
    }

    /**
     * Returns a List of successor states of the current board. Only returns the successors of the
     * most constrained variable, that is the square with the fewest legal candidates. If any
     * successor is a solution, that is returned as the sole successor in the List.
     *
     * @return A List of successor states
     */
    public List<SudokuBoard> getSuccessors() {
        List<SudokuBoard> successors = new LinkedList<>();
        // fewestLegalValues keeps track of the smallest number of legal values, initially set to
        // SIZE + 1 since we have not yet discovered the smallest number (which at most will be
        // equal to SIZE).
        int fewestLegalValues = SIZE + 1;
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (hasValueSet[row][column]) continue;
                int[] candidates = board[row][column].getCandidates();
                List<SudokuBoard> currentSquareSuccessors = new LinkedList<>();
                for (Integer candidate : candidates) {
                    SudokuBoard successor = clone();
                    if (successor.setSquareValue(row, column, candidate)) {
                        // Just return the solution as the sole successor if we discover it
                        if (successor.solved()) {
                            List<SudokuBoard> solution = new LinkedList<>();
                            solution.add(successor);
                            return solution;
                        }
                        // Since the successor is not a solution, add it to the list of
                        // successors
                        currentSquareSuccessors.add(successor);
                    }
                }

                // If there ends up being only 1 candidate value of the current square that works,
                // we should only return the successors of that board since all valid successors
                // would have to make that assignment.
                if (currentSquareSuccessors.size() == 1) {
                    return currentSquareSuccessors.get(0).getSuccessors();
                }

                // Update the List of successors and the fewestLegalValues variable, if necessary
                if (currentSquareSuccessors.size() < fewestLegalValues) {
                    successors = currentSquareSuccessors;
                    fewestLegalValues = currentSquareSuccessors.size();
                }
            }

        }
        return successors;
    }

    /**
     * Verifies that the board has been correctly solved (unlike solved() which just checks that all
     * squares have been filled). Should not be invoked unless the board has been completely filled,
     * as indicated by solved() returning true.
     *
     * @return True if the board is a valid solution and false otherwise
     */
    public boolean verifySolution() {
        // Check rows
        for (int row = 0; row < SIZE; row++) {
            // Create a Candidate Set of the correct size
            CandidateSet set = new CandidateSet(SIZE);
            // Attempt to remove every value in the column from the set, if a removal returns false,
            // then a duplicate was found
            for (int column = 0; column < SIZE; column++) {
                int value = board[row][column].getOnlyCandidate();
                if (!set.remove(value)) return false; // Duplicate value found
            }
        }

        // Check columns
        for (int column = 0; column < SIZE; column++) {
            CandidateSet set = new CandidateSet(SIZE);
            for (int row = 0; row < SIZE; row++) {
                int value = board[row][column].getOnlyCandidate();
                if (!set.remove(value)) return false;
            }
        }

        // Check boxes
        for (int boxRow = 0; boxRow < SIZE; boxRow += BOX_SIZE) {
            for (int boxColumn = 0; boxColumn < SIZE; boxColumn += BOX_SIZE) {
                CandidateSet set = new CandidateSet(SIZE);
                for (int row = boxRow; row < boxRow + BOX_SIZE; row++) {
                    for (int column = boxColumn; column < boxColumn + BOX_SIZE; column++) {
                        int value = board[row][column].getOnlyCandidate();
                        if (!set.remove(value)) return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Compares this object with the specified object for order. Returns a negative integer, zero,
     * or a positive integer as this object is less than, equal to, or greater than the specified
     * object.
     *
     * @param other The object to be compared
     * @return A negative integer, zero, or a positive integer as this object is less than, equal
     * to, or greater than the specified object.
     */
    @Override
    public int compareTo(SudokuBoard other) {
        if (squaresRemaining < other.squaresRemaining) {
            return -1;
        } else if (squaresRemaining > other.squaresRemaining) {
            return 1;
        } else {
            // Use another heuristic as a tiebreaker
            return Integer.compare(totalNumberOfCandidates, other.totalNumberOfCandidates);
        }
    }

    /**
     * Returns a string representation of the SudokuBoard object.
     *
     * @return A string representation of the SudokuBoard object.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (SIZE < 10) {
            for (int row = 0; row < SIZE; row++) {
                for (int column = 0; column < SIZE; column++) {
                    int[] values = board[row][column].getCandidates();
                    if (values.length > 1) {
                        result.append("*");
                    } else if (values.length == 0) {
                        result.append("_");
                    } else {
                        for (int value : values) {
                            result.append(value);
                        }
                    }
                    result.append("  ");
                }
                result.append("\n");
            }
        } else {
            for (int row = 0; row < SIZE; row++) {
                for (int column = 0; column < SIZE; column++) {
                    int[] values = board[row][column].getCandidates();
                    if (values.length > 1) {
                        result.append(" * ");
                    } else if (values.length == 0) {
                        result.append(" _ ");
                    } else {
                        int value = board[row][column].getOnlyCandidate();
                        if (value < 10) {
                            result.append(" ").append(value).append(" ");
                        } else {
                            result.append(value).append(" ");
                        }

                    }
                }
                result.append("\n");
            }
        }

        return result.toString();
    }

    /**
     * Creates and returns a clone of this instance. Does not call super.clone() as a deep copy
     * must be created.
     *
     * @return A clone of this instance
     */
    @Override
    public SudokuBoard clone() {
        SudokuBoard clone = new SudokuBoard(SIZE);
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                clone.board[row][column] = board[row][column].clone();
                clone.hasValueSet[row][column] = hasValueSet[row][column];
            }
        }
        clone.squaresRemaining = squaresRemaining;
        clone.totalNumberOfCandidates = totalNumberOfCandidates;
        return clone;
    }
}