package sudoku.model;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuBoardTest {

    @Test
    public void testIllegalNumberInGameString() {
        // 0s are not allowed
        String illegal = """
                *,*,*,*,*,*,*,0,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                """;

        // 10s are not allowed for boards of size 9
        String illegal2 = """
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,10,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                """;

        // 5s are not allowed for boards of size 4
        String illegal3 = """
                1,2,3,5,
                *,*,*,*,
                *,*,*,*,
                *,*,*,*,
                """;

        // 17s are not allowed for boards of size 16
        String illegal4 = """
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
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,17,*,*,
                """;

        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(illegal));
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(illegal2));
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(illegal3));
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(illegal4));
    }

    @Test
    public void testIllegalSizeGameThrowsException() {
        String size3Game = """
                1,*,*,
                2,*,*,
                3,*,*,
                """;

        String size5Game = """
                1,*,*,*,*
                2,*,*,*,*
                3,*,*,*,*
                4,*,*,*,*
                5,*,*,*,*
                """;

        String size10Game = """
                1,*,*,*,*,*,*,*,*,*,
                2,*,*,*,*,*,*,*,*,*,
                3,*,*,*,*,*,*,*,*,*,
                4,*,*,*,*,*,*,*,*,*,
                5,*,*,*,*,*,*,*,*,*,
                6,*,*,*,*,*,*,*,*,*,
                7,*,*,*,*,*,*,*,*,*,
                8,*,*,*,*,*,*,*,*,*,
                9,*,*,*,*,*,*,*,*,*,
                10,*,*,*,*,*,*,*,*,*
                """;

        String size25Game = """
                1,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                2,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                3,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                4,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                5,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                6,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                7,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                8,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                9,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                10,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                11,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                12,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                13,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                14,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                15,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                16,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                17,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                18,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                19,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                20,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                21,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                22,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                23,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                24,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                25,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                """;

        // This game is 8 by 9
        String nonSquareGame = """
                1,*,*,*,*,*,*,*,*,
                2,*,*,*,*,*,*,*,*,
                3,*,*,*,*,*,*,*,*,
                4,*,*,*,*,*,*,*,*,
                5,*,*,*,*,*,*,*,*,
                6,*,*,*,*,*,*,*,*,
                7,*,*,*,*,*,*,*,*,
                8,*,*,*,*,*,*,*,*,
                """;

        // This game is 9 by 8
        String nonSquareGame2 = """
                1,*,*,*,*,*,*,*,
                2,*,*,*,*,*,*,*,
                3,*,*,*,*,*,*,*,
                4,*,*,*,*,*,*,*,
                5,*,*,*,*,*,*,*,
                6,*,*,*,*,*,*,*,
                7,*,*,*,*,*,*,*,
                8,*,*,*,*,*,*,*,
                9,*,*,*,*,*,*,*,
                """;

        // This game is 10 by 9
        String nonSquareGame3 = """
                1,*,*,*,*,*,*,*,*,
                2,*,*,*,*,*,*,*,*,
                3,*,*,*,*,*,*,*,*,
                4,*,*,*,*,*,*,*,*,
                5,*,*,*,*,*,*,*,*,
                6,*,*,*,*,*,*,*,*,
                7,*,*,*,*,*,*,*,*,
                8,*,*,*,*,*,*,*,*,
                9,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                """;

        // This game is 9 by 10
        String nonSquareGame4 = """
                1,*,*,*,*,*,*,*,*,*,
                2,*,*,*,*,*,*,*,*,*,
                3,*,*,*,*,*,*,*,*,*,
                4,*,*,*,*,*,*,*,*,*,
                5,*,*,*,*,*,*,*,*,*,
                6,*,*,*,*,*,*,*,*,*,
                7,*,*,*,*,*,*,*,*,*,
                8,*,*,*,*,*,*,*,*,*,
                9,*,*,*,*,*,*,*,*,*,
                """;

        // This game is nearly 9 by 9 but is missing a value
        String nonSquareGame5 = """
                1,*,*,*,*,*,*,*,*,
                2,*,*,*,*,*,*,*,*,
                3,*,*,*,*,*,*,*,*,
                4,*,*,*,*,*,*,*,*,
                5,*,*,*,*,*,*,*,*,
                6,*,*,*,*,*,*,*,
                7,*,*,*,*,*,*,*,*,
                8,*,*,*,*,*,*,*,*,
                9,*,*,*,*,*,*,*,*,
                """;

        // This game has a row with an extra value
        String nonSquareGame6 = """
                1,*,*,*,*,*,*,*,*,
                2,*,*,*,*,*,*,*,*,
                3,*,*,*,*,*,*,*,*,*
                4,*,*,*,*,*,*,*,*,
                5,*,*,*,*,*,*,*,*,
                6,*,*,*,*,*,*,*,*,
                7,*,*,*,*,*,*,*,*,
                8,*,*,*,*,*,*,*,*,
                9,*,*,*,*,*,*,*,*,
                """;

        // This game has a column with an extra value
        String nonSquareGame7 = """
                1,*,*,*,*,*,*,*,*,
                2,*,*,*,*,*,*,*,*,
                3,*,*,*,*,*,*,*,*,
                4,*,*,*,*,*,*,*,*,
                5,*,*,*,*,*,*,*,*,
                6,*,*,*,*,*,*,*,*,
                7,*,*,*,*,*,*,*,*,
                8,*,*,*,*,*,*,*,*,
                9,*,*,*,*,*,*,*,*,
                *,
                """;

        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(size3Game));
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(size5Game));
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(size10Game));
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(size25Game));
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(nonSquareGame));
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(nonSquareGame2));
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(nonSquareGame3));
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(nonSquareGame4));
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(nonSquareGame5));
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(nonSquareGame6));
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(nonSquareGame7));
    }

    @Test
    public void testUnsolvableSize4GameCreation() {
        // Two 1's in the same row
        String unsolvable = """
                *,*,*,*,
                1,*,*,1,
                *,*,*,*,
                *,*,*,*,
                """;

        // Two 1's in the same column
        String unsolvable2 = """
                *,1,*,*,
                *,*,*,*,
                *,*,*,*,
                *,1,*,*,
                """;

        // Two 1's in the same box
        String unsolvable3 = """
                1,*,*,*,
                *,1,*,*,
                *,*,*,*,
                *,*,*,*,
                """;

        // Board that has a cell that ends up with only 1 legal value during board creation process
        // (i.e., the bottom right cell of the top left box)
        String unsolvable4 = """
                1,2,*,*,
                3,3,*,*,
                *,*,*,*,
                *,*,*,*,
                """;

        assertThrows(IllegalStateException.class, () -> new SudokuBoard(unsolvable));
        assertThrows(IllegalStateException.class, () -> new SudokuBoard(unsolvable2));
        assertThrows(IllegalStateException.class, () -> new SudokuBoard(unsolvable3));
        assertThrows(IllegalStateException.class, () -> new SudokuBoard(unsolvable4));
    }

    @Test
    public void testUnsolvableSize9GameCreation() {
        // Two 1's in the same row
        String unsolvable = """
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

        // Two 1's in the same column
        String unsolvable2 = """
                1,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                1,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                """;

        // Two 1's in the same box
        String unsolvable3 = """
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,1,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,1,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                """;

        // Board that has a cell that ends up with only 1 legal value during board creation process
        // (i.e., the bottom right cell of the top left box)
        String unsolvable4 = """
                1,2,3,*,*,*,*,*,*,
                4,5,6,*,*,*,*,*,*,
                7,8,8,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                """;

        assertThrows(IllegalStateException.class, () -> new SudokuBoard(unsolvable));
        assertThrows(IllegalStateException.class, () -> new SudokuBoard(unsolvable2));
        assertThrows(IllegalStateException.class, () -> new SudokuBoard(unsolvable3));
        assertThrows(IllegalStateException.class, () -> new SudokuBoard(unsolvable4));
    }

    @Test
    public void testUnsolvableSize16GameCreation() {
        // Two 1's in the same row
        String unsolvable = """
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

        // Two 1's in the same column
        String unsolvable2 = """
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,1,*,*,*,
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
                *,*,*,*,*,*,*,*,*,*,*,*,1,*,*,*,
                """;

        // Two 1's in the same box
        String unsolvable3 = """
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
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,1,*,*,*,*,*,*,*,*,*,
                *,*,*,*,1,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                """;

        // Board that has a cell that ends up with only 1 legal value during board creation process
        // (i.e., the bottom right cell of the top left box)
        String unsolvable4 = """
                1,2,3,4,*,*,*,*,*,*,*,*,*,*,*,*,
                5,6,7,8,*,*,*,*,*,*,*,*,*,*,*,*,
                9,10,11,12,*,*,*,*,*,*,*,*,*,*,*,*,
                13,14,15,15,*,*,*,*,*,*,*,*,*,*,*,*,
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
                """;

        assertThrows(IllegalStateException.class, () -> new SudokuBoard(unsolvable));
        assertThrows(IllegalStateException.class, () -> new SudokuBoard(unsolvable2));
        assertThrows(IllegalStateException.class, () -> new SudokuBoard(unsolvable3));
        assertThrows(IllegalStateException.class, () -> new SudokuBoard(unsolvable4));
    }

    @Test
    public void testEasilySolvableGameCreation() {
        String easy = """
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

        SudokuBoard board = new SudokuBoard(easy);
        assertTrue(board.solved());
        assertTrue(board.verifySolution());


        String easySmall = """
                *,2,3,4,
                4,3,*,1,
                3,*,4,2,
                2,4,1,*
                """;

        SudokuBoard smallBoard = new SudokuBoard(easySmall);
        assertTrue(smallBoard.solved());
        assertTrue(smallBoard.verifySolution());


        String easyBig = """
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

        SudokuBoard bigBoard = new SudokuBoard(easyBig);
        assertTrue(bigBoard.solved());
        assertTrue(bigBoard.verifySolution());

    }

    @Test
    public void testEmptyGameCreation() {
        String empty = """
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*
                """;

        SudokuBoard board = new SudokuBoard(empty);
        assertFalse(board.solved());


        String smallEmpty = """
                *,*,*,*,
                *,*,*,*,
                *,*,*,*,
                *,*,*,*,
                """;

        SudokuBoard smallBoard = new SudokuBoard(smallEmpty);
        assertFalse(smallBoard.solved());

        String bigEmpty = """
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
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                """;

        SudokuBoard bigBoard = new SudokuBoard(bigEmpty);
        assertFalse(bigBoard.solved());
    }

    @Test
    public void testNotEasilySolvableGameCreation() {
        String game = """
                *,1,4,*,6,*,3,*,*
                6,2,*,*,*,4,*,*,9
                *,8,*,*,5,*,6,*,*
                *,6,*,2,*,*,*,*,3
                *,7,*,*,1,*,*,5,*
                5,*,*,*,*,9,*,6,*
                *,*,6,*,2,*,*,3,*
                1,*,*,5,*,*,*,9,2
                *,*,7,*,9,*,4,1,*
                """;

        SudokuBoard board = new SudokuBoard(game);
        assertFalse(board.solved());


        String smallGame = """
                *,2,*,*,
                *,*,*,*,
                *,*,*,*,
                *,*,1,*
                """;

        SudokuBoard smallBoard = new SudokuBoard(smallGame);
        assertFalse(smallBoard.solved());


        String bigGame = """
                *,*,*,*,*,*,*,*,7,*,*,*,*,*,*,*
                7,*,*,*,11,*,*,*,*,*,*,*,*,*,*,*
                1,*,*,*,16,*,14,*,3,*,11,*,2,*,*,*
                *,*,2,*,*,*,5,*,*,*,*,*,*,*,*,*
                2,*,*,*,*,*,*,*,*,*,*,*,16,*,*,*
                *,*,11,*,1,*,12,*,14,*,*,*,*,*,*,*
                15,*,*,*,*,*,16,*,*,*,*,*,*,*,*,*
                *,*,*,*,5,*,*,*,2,*,*,*,*,*,7,*
                *,*,*,*,4,*,*,*,*,*,*,*,*,*,8,*
                *,*,*,*,8,*,6,*,*,*,14,*,*,*,3,*
                *,*,*,*,13,*,15,*,11,*,16,*,5,*,4,*
                *,*,16,*,*,*,9,*,12,*,*,*,*,*,*,*
                *,*,*,*,*,*,13,*,6,*,*,*,*,*,*,*
                14,*,*,*,*,*,10,*,*,*,12,*,11,*,*,*
                *,*,*,*,3,*,*,*,5,*,*,*,*,*,*,*
                *,*,*,*,*,*,*,*,*,*,1,*,*,*,16,*
                 """;

        SudokuBoard bigBoard = new SudokuBoard(bigGame);
        assertFalse(bigBoard.solved());
    }


    @Test
    public void testClone() {
        String game = """
                *,1,4,*,6,*,3,*,*
                6,2,*,*,*,4,*,*,9
                *,8,*,*,5,*,6,*,*
                *,6,*,2,*,*,*,*,3
                *,7,*,*,1,*,*,5,*
                5,*,*,*,*,9,*,6,*
                *,*,6,*,2,*,*,3,*
                1,*,*,5,*,*,*,9,2
                *,*,7,*,9,*,4,1,*
                """;

        SudokuBoard board = new SudokuBoard(game);
        SudokuBoard board2 = board.clone();
        assertNotSame(board, board2);
        assertEquals(board.toString(), board2.toString());


        String smallGame = """
                *,2,*,*,
                *,*,*,*,
                *,*,*,*,
                *,*,1,*
                """;

        SudokuBoard smallBoard = new SudokuBoard(smallGame);
        SudokuBoard smallBoard2 = smallBoard.clone();
        assertNotSame(smallBoard, smallBoard2);
        assertEquals(smallBoard.toString(), smallBoard2.toString());


        String bigGame = """
                *,*,*,*,*,*,*,*,7,*,*,*,*,*,*,*
                7,*,*,*,11,*,*,*,*,*,*,*,*,*,*,*
                1,*,*,*,16,*,14,*,3,*,11,*,2,*,*,*
                *,*,2,*,*,*,5,*,*,*,*,*,*,*,*,*
                2,*,*,*,*,*,*,*,*,*,*,*,16,*,*,*
                *,*,11,*,1,*,12,*,14,*,*,*,*,*,*,*
                15,*,*,*,*,*,16,*,*,*,*,*,*,*,*,*
                *,*,*,*,5,*,*,*,2,*,*,*,*,*,7,*
                *,*,*,*,4,*,*,*,*,*,*,*,*,*,8,*
                *,*,*,*,8,*,6,*,*,*,14,*,*,*,3,*
                *,*,*,*,13,*,15,*,11,*,16,*,5,*,4,*
                *,*,16,*,*,*,9,*,12,*,*,*,*,*,*,*
                *,*,*,*,*,*,13,*,6,*,*,*,*,*,*,*
                14,*,*,*,*,*,10,*,*,*,12,*,11,*,*,*
                *,*,*,*,3,*,*,*,5,*,*,*,*,*,*,*
                *,*,*,*,*,*,*,*,*,*,1,*,*,*,16,*
                 """;

        SudokuBoard bigBoard = new SudokuBoard(bigGame);
        SudokuBoard bigBoard2 = bigBoard.clone();
        assertNotSame(bigBoard, bigBoard2);
        assertEquals(bigBoard.toString(), bigBoard2.toString());
    }

    @Test
    public void testCompareTo() {
        String game = """
                1,2,3,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                """;

        String game2 = """
                1,2,3,*,*,*,*,*,*,
                4,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                """;

        SudokuBoard board = new SudokuBoard(game);
        SudokuBoard board2 = new SudokuBoard(game2);

        // Board2 should have a lower heuristic value as it has more filled squares and thus is
        // closer to being a solution
        assertTrue(board.compareTo(board2) > 0);
        assertTrue(board2.compareTo(board) < 0);

        String game3 = """
                1,2,3,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,4,*,
                """;

        SudokuBoard board3 = new SudokuBoard(game3);

        // Board3 should have a lower heuristic value as board2 and board3 have the same number of
        // filled squares, but board3 has fewer total candidates remaining
        assertTrue(board3.compareTo(board2) < 0);
        assertTrue(board2.compareTo(board3) > 0);
    }

    @Test
    public void testCompareToSize4Board() {
        String game = """
                1,2,*,*,
                *,*,*,*,
                *,*,*,*,
                *,*,*,*,
                """;

        String game2 = """
                1,2,*,*,
                3,*,*,*,
                *,*,*,*,
                *,*,*,*,
                """;

        SudokuBoard board = new SudokuBoard(game);
        SudokuBoard board2 = new SudokuBoard(game2);

        // Board2 should have a lower heuristic value as it has more filled squares and thus is
        // closer to being a solution
        assertTrue(board.compareTo(board2) > 0);
        assertTrue(board2.compareTo(board) < 0);

        String game3 = """
                1,2,*,*,
                *,*,*,*,
                *,*,*,*,
                *,*,*,3,
                """;

        SudokuBoard board3 = new SudokuBoard(game3);

        // Board3 should have a lower heuristic value as board2 and board3 have the same number of
        // filled squares, but board3 has fewer total candidates remaining
        assertTrue(board3.compareTo(board2) < 0);
        assertTrue(board2.compareTo(board3) > 0);
    }

    @Test
    public void testCompareToSize16Board() {
        String game = """
                1,2,3,4,*,*,*,*,*,*,*,*,*,*,*,*,
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
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                """;

        String game2 = """
                1,2,3,4,*,*,*,*,*,*,*,*,*,*,*,*,
                5,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
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
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                """;

        SudokuBoard board = new SudokuBoard(game);
        SudokuBoard board2 = new SudokuBoard(game2);

        // Board2 should have a lower heuristic value as it has more filled squares and thus is
        // closer to being a solution
        assertTrue(board.compareTo(board2) > 0);
        assertTrue(board2.compareTo(board) < 0);

        String game3 = """
                1,2,3,4,*,*,*,*,*,*,*,*,*,*,*,*,
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
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,5,*,*,
                """;

        SudokuBoard board3 = new SudokuBoard(game3);

        // Board3 should have a lower heuristic value as board2 and board3 have the same number of
        // filled squares, but board3 has fewer total candidates remaining
        assertTrue(board3.compareTo(board2) < 0);
        assertTrue(board2.compareTo(board3) > 0);
    }

    @Test
    public void testSuccessors() {
        String game = """
                *,1,4,*,6,*,3,*,*
                6,2,*,*,*,4,*,*,9
                *,8,*,*,5,*,6,*,*
                *,6,*,2,*,*,*,*,3
                *,7,*,*,1,*,*,5,*
                5,*,*,*,*,9,*,6,*
                *,*,6,*,2,*,*,3,*
                1,*,*,5,*,*,*,9,2
                *,*,7,*,9,*,4,1,*
                """;

        SudokuBoard board = new SudokuBoard(game);
        List<SudokuBoard> successors = board.getSuccessors();
        assertFalse(successors.isEmpty());

        // the successors should all be closer to being a solution
        for (SudokuBoard successor : successors) {
            assertTrue(board.compareTo(successor) > 0);
        }


        String smallGame = """
                1,2,*,*,
                *,*,*,*,
                *,*,*,*,
                *,*,*,*,
                """;

        SudokuBoard smallBoard = new SudokuBoard(smallGame);
        successors = smallBoard.getSuccessors();
        assertFalse(successors.isEmpty());

        // the successors should all be closer to being a solution
        for (SudokuBoard successor : successors) {
            assertTrue(smallBoard.compareTo(successor) > 0);
        }


        String bigGame = """
                *,*,*,*,*,*,*,*,7,*,*,*,*,*,*,*
                7,*,*,*,11,*,*,*,*,*,*,*,*,*,*,*
                1,*,*,*,16,*,14,*,3,*,11,*,2,*,*,*
                *,*,2,*,*,*,5,*,*,*,*,*,*,*,*,*
                2,*,*,*,*,*,*,*,*,*,*,*,16,*,*,*
                *,*,11,*,1,*,12,*,14,*,*,*,*,*,*,*
                15,*,*,*,*,*,16,*,*,*,*,*,*,*,*,*
                *,*,*,*,5,*,*,*,2,*,*,*,*,*,7,*
                *,*,*,*,4,*,*,*,*,*,*,*,*,*,8,*
                *,*,*,*,8,*,6,*,*,*,14,*,*,*,3,*
                *,*,*,*,13,*,15,*,11,*,16,*,5,*,4,*
                *,*,16,*,*,*,9,*,12,*,*,*,*,*,*,*
                *,*,*,*,*,*,13,*,6,*,*,*,*,*,*,*
                14,*,*,*,*,*,10,*,*,*,12,*,11,*,*,*
                *,*,*,*,3,*,*,*,5,*,*,*,*,*,*,*
                *,*,*,*,*,*,*,*,*,*,1,*,*,*,16,*
                 """;

        SudokuBoard bigBoard = new SudokuBoard(bigGame);
        successors = bigBoard.getSuccessors();
        assertFalse(successors.isEmpty());

        // the successors should all be closer to being a solution
        for (SudokuBoard successor : successors) {
            assertTrue(bigBoard.compareTo(successor) > 0);
        }
    }

    @Test
    public void testVerifySolution() {
        String game = """
                *,1,4,*,6,*,3,*,*
                6,2,*,*,*,4,*,*,9
                *,8,*,*,5,*,6,*,*
                *,6,*,2,*,*,*,*,3
                *,7,*,*,1,*,*,5,*
                5,*,*,*,*,9,*,6,*
                *,*,6,*,2,*,*,3,*
                1,*,*,5,*,*,*,9,2
                *,*,7,*,9,*,4,1,*
                """;
        SudokuBoard board = new SudokuBoard(game);
        assertThrows(IllegalStateException.class, board::verifySolution);


        String solved = """
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
        SudokuBoard solvedBoard = new SudokuBoard(solved);
        assertTrue(solvedBoard.solved());
        assertTrue(solvedBoard.verifySolution());


        String solvedSmall = """
                1,2,3,4,
                4,3,2,1,
                3,1,4,2,
                2,4,1,3
                """;
        SudokuBoard solvedSmallBoard = new SudokuBoard(solvedSmall);
        assertTrue(solvedSmallBoard.solved());
        assertTrue(solvedSmallBoard.verifySolution());


        String solvedBig = """
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
        SudokuBoard solvedBigBoard = new SudokuBoard(solvedBig);
        assertTrue(solvedBigBoard.solved());
        assertTrue(solvedBigBoard.verifySolution());
    }

    @Test
    public void testGetSIZE() {
        String empty = """
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*
                """;
        SudokuBoard emptyBoard = new SudokuBoard(empty);
        assertEquals(emptyBoard.getSIZE(), 9);


        String smallEmpty = """
                *,*,*,*,
                *,*,*,*,
                *,*,*,*,
                *,*,*,*,
                """;
        SudokuBoard smallBoard = new SudokuBoard(smallEmpty);
        assertEquals(smallBoard.getSIZE(), 4);


        String bigEmpty = """
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
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                *,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,
                """;
        SudokuBoard bigBoard = new SudokuBoard(bigEmpty);
        assertEquals(bigBoard.getSIZE(), 16);
    }

    @Test
    public void testGetValues() {
        String bigGame = """
                *,*,*,*,*,*,*,*,7,*,*,*,*,*,*,*
                7,*,*,*,11,*,*,*,*,*,*,*,*,*,*,*
                1,*,*,*,16,*,14,*,3,*,11,*,2,*,*,*
                *,*,2,*,*,*,5,*,*,*,*,*,*,*,*,*
                2,*,*,*,*,*,*,*,*,*,*,*,16,*,*,*
                *,*,11,*,1,*,12,*,14,*,*,*,*,*,*,*
                15,*,*,*,*,*,16,*,*,*,*,*,*,*,*,*
                *,*,*,*,5,*,*,*,2,*,*,*,*,*,7,*
                *,*,*,*,4,*,*,*,*,*,*,*,*,*,8,*
                *,*,*,*,8,*,6,*,*,*,14,*,*,*,3,*
                *,*,*,*,13,*,15,*,11,*,16,*,5,*,4,*
                *,*,16,*,*,*,9,*,12,*,*,*,*,*,*,*
                *,*,*,*,*,*,13,*,6,*,*,*,*,*,*,*
                14,*,*,*,*,*,10,*,*,*,12,*,11,*,*,*
                *,*,*,*,3,*,*,*,5,*,*,*,*,*,*,*
                *,*,*,*,*,*,*,*,*,*,1,*,*,*,16,*
                 """;

        SudokuBoard board = new SudokuBoard(bigGame);
        int[][] values = board.getValues();
        String[] rows = bigGame.split("\n");
        for (int row = 0; row < 9; row++) {
            String currentRow = rows[row];
            String[] squares = currentRow.split(",");

            for (int column = 0; column < 9; column++) {
                String currentSquare = squares[column];
                if (Character.isDigit(currentSquare.charAt(0))) {
                    int value = Integer.parseInt(currentSquare);
                    assertEquals(values[row][column], value);
                }
                else {
                    assertEquals(values[row][column], 0);
                }
            }
        }
    }
}
