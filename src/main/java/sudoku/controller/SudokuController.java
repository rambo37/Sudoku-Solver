package sudoku.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import sudoku.board.SudokuBoard;
import sudoku.solver.BestFirstSolver;
import sudoku.solver.BreadthFirstSolver;
import sudoku.solver.DepthFirstSolver;
import sudoku.solver.SudokuSolver;

import java.util.HashMap;
import java.util.Map;

/**
 * This class serves as the controller of the application.
 *
 * @author Savraj Bassi
 * @version 30/11/2023
 */

public class SudokuController {

    private Stage stage;
    private final Map<String, Integer> boardSizes = new HashMap<>();
    private final Map<String, SudokuSolver> solvers = new HashMap<>();
    private int boardSize = 9;
    private int boxSize = (int) Math.sqrt(boardSize);
    private double cellSize = 640 / (double) boardSize;
    private final SimpleBooleanProperty solving = new SimpleBooleanProperty(false);
    private Task<SudokuBoard> solveTask;

    @FXML
    protected ChoiceBox<String> boardSizeSelector;
    @FXML
    protected ChoiceBox<String> solverSelector;
    @FXML
    protected Button solve;
    @FXML
    protected Button cancel;
    @FXML
    protected GridPane inputBoard;
    @FXML
    protected GridPane solvedBoard;
    @FXML
    protected HBox root;
    @FXML
    protected VBox leftVBox;
    @FXML
    protected VBox rightVBox;
    @FXML
    protected Label statusLabel;
    @FXML
    protected ProgressIndicator progressIndicator;

    /**
     * Initialises the ChoiceBoxes, the progress indicator and constructs both Sudoku boards.
     */
    @FXML
    public void initialize() {
        boardSizes.put("4x4", 4);
        boardSizes.put("9x9", 9);
        boardSizes.put("16x16", 16);
        boardSizeSelector.getItems().addAll("4x4", "9x9", "16x16");
        boardSizeSelector.setValue("9x9");

        solvers.put("Breadth first", new BreadthFirstSolver());
        solvers.put("Depth first", new DepthFirstSolver());
        solvers.put("Best first", new BestFirstSolver());
        solverSelector.getItems().addAll("Breadth first", "Depth first", "Best first");
        solverSelector.setValue("Best first");

        rebuildBoard(inputBoard, true);
        rebuildBoard(solvedBoard, false);

        // Rebuild both boards whenever the board size selector's value is changed
        boardSizeSelector.getSelectionModel().selectedItemProperty().addListener((observable,
                                                                                  oldValue,
                                                                                  newValue) -> {
            boardSize = boardSizes.get(newValue);
            // Recalculate the box and cell sizes using the new boardSize
            boxSize = (int) Math.sqrt(boardSize);
            cellSize = 640 / (double) boardSize;
            rebuildBoard(inputBoard, true);
            rebuildBoard(solvedBoard, false);
        });

        // The progress indicator and cancel button should only be visible when a board is being
        // solved.
        progressIndicator.visibleProperty().bind(solving);
        cancel.visibleProperty().bind(solving);
        // The choice boxes and the solve button should be disabled if a puzzle is being solved.
        boardSizeSelector.disableProperty().bind(solving);
        solverSelector.disableProperty().bind(solving);
        solve.disableProperty().bind(solving);
    }

    /**
     * Rebuilds the specified board GridPane by clearing all children and then recreating them.
     * @param board The board GridPane that is to be rebuilt
     * @param editable Whether the board is editable
     */
    private void rebuildBoard(GridPane board, boolean editable) {
        board.getChildren().clear();
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                TextField textField = new TextField();
                textField.setPrefWidth(cellSize);
                textField.setPrefHeight(cellSize);
                textField.setMinWidth(cellSize);
                textField.setMinHeight(cellSize);
                textField.setEditable(editable);
                textField.setFocusTraversable(editable);
                board.add(textField, column, row);

                boolean lastColumnOfBox = (column + 1) % boxSize == 0;
                boolean lastRowOfBox = (row + 1) % boxSize == 0;
                int fontSize;

                switch (boardSize) {
                    case 4 -> fontSize = 45;
                    case 9 -> fontSize = 25;
                    default -> fontSize = 15;
                }
                String style = "-fx-font-size:" + fontSize + "px;";

                int borderWidth = 2;
                if (boardSize == 4) borderWidth = 4;
                if (boardSize == 9) borderWidth = 3;

                if (lastColumnOfBox && lastRowOfBox) {
                    style += String.format("-fx-border-width: 0 %d %d 0;", borderWidth,
                            borderWidth);
                } else if (row == 0 && lastColumnOfBox) {
                    style += String.format("-fx-border-width: %d %d 0 0;", borderWidth,
                            borderWidth);
                } else if (column == 0 && lastRowOfBox) {
                    style += String.format("-fx-border-width: 0 0 %d %d;", borderWidth,
                            borderWidth);
                } else if (lastColumnOfBox) {
                    style += String.format("-fx-border-width: 0 %d 0 0;", borderWidth);
                } else if (lastRowOfBox) {
                    style += String.format("-fx-border-width: 0 0 %d 0;", borderWidth);
                } else if (row == 0 && column == 0) {
                    style += String.format("-fx-border-width: %d 0 0 %d;", borderWidth,
                            borderWidth);
                } else if (row == 0) {
                    style += String.format("-fx-border-width: %d 0 0 0;", borderWidth);
                } else if (column == 0) {
                    style += String.format("-fx-border-width: 0 0 0 %d;", borderWidth);
                } else {
                    style += ("-fx-border-width: 0;");
                }

                textField.setStyle(textField.getStyle() + style);
                textField.setAlignment(Pos.CENTER);

                textField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
                    char inputChar = event.getCharacter().charAt(0);
                    if (!Character.isDigit(inputChar) ||
                            !isValidValue(textField.getText() + inputChar)) {
                        event.consume();
                    }
                });
            }
        }
    }

    private void clearBoardValues(GridPane board) {
        ObservableList<Node> children = board.getChildren();
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                int index = row * boardSize + column;
                Node child = children.get(index);
                if (child.getClass() == TextField.class) {
                    TextField textField = (TextField) child;
                    textField.setText("");
                }
            }
        }
    }

    /**
     * Checks if the provided String is a valid value for a board cell.
     *
     * @param text The text that is to be entered into a board cell
     * @return True if the text is valid and false otherwise
     */
    private boolean isValidValue(String text) {
        try {
            int value = Integer.parseInt(text);
            return value >= 1 && value <= boardSize;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Sets the value of the stage field and binds several properties to the stage's widthProperty
     *
     * @param stage The stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        root.minWidthProperty().bind(stage.widthProperty());
        root.prefWidthProperty().bind(stage.widthProperty());

        leftVBox.prefWidthProperty().bind(stage.widthProperty().divide(2));
        rightVBox.prefWidthProperty().bind(stage.widthProperty().divide(2));
    }

    /**
     * Returns a String representation of the Sudoku board input by the user.
     *
     * @return The String representation of the Sudoku board input by the user
     */
    private String getBoardStringFromInputBoard() {
        StringBuilder result = new StringBuilder();

        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                int index = row * boardSize + column;
                Node child = inputBoard.getChildrenUnmodifiable().get(index);
                if (child.getClass() == TextField.class) {
                    TextField textField = (TextField) child;
                    String text = textField.getText();
                    if (text.isEmpty()) text = "*";
                    result.append(text).append(",");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Fills the solvedBoard GridPane with a String representation of the solved puzzle.
     *
     * @param solved The String representation of the solved puzzle
     */
    private void fillSolution(String solved) {
        String[] rows = solved.split("\n");
        for (int row = 0; row < boardSize; row++) {
            String[] rowValues = rows[row].split(",");
            for (int column = 0; column < boardSize; column++) {
                int index = row * boardSize + column;
                Node child = solvedBoard.getChildren().get(index);
                if (child.getClass() == TextField.class) {
                    TextField textField = (TextField) child;
                    textField.setText(rowValues[column]);
                }
            }
        }
    }

    /**
     * Attempts to solve the Sudoku puzzle input by the user and display the solution in the
     * solvedBoard GridPane. Also updates the status label to inform the user of what is happening
     * or has happened, and shows a progress circle.
     */
    @FXML
    protected void solve() {
        clearBoardValues(solvedBoard);
        solving.set(true);
        setStatus("Solving " + boardSize + "x" + boardSize + " puzzle.", Color.BLACK);

        // Create a Task and run it in a separate thread to prevent the JavaFX application thread
        // becoming unresponsive while the board is being solved
        solveTask = new Task<>() {
            @Override
            protected SudokuBoard call() {
                String board = getBoardStringFromInputBoard();
                System.out.println(board);

                try {
                    return solvers.get(solverSelector.getValue()).solve(board);
                } catch (IllegalStateException e) {
                    return null;
                }
            }

            @Override
            protected void succeeded() {
                SudokuBoard solved = getValue();
                if (solved == null) {
                    setStatus("Unsolvable Sudoku board provided.", Color.RED);

                } else {
                    setStatus("Board solved!", Color.GREEN);
                    System.out.println(solved);
                    String solvedString = solved.toString()
                            .replaceAll(" {2}", " ")
                            .replaceAll(" ", ",")
                            .replaceAll("\n,", "\n")
                            .replaceAll("^,", "");
                    System.out.println(solvedString);

                    fillSolution(solvedString);
                }
                solving.set(false);
            }

            @Override
            protected void failed() {
                setStatus("Error occurred while solving the puzzle.", Color.RED);
                solving.set(false);
                getException().printStackTrace();
            }

            @Override
            protected void cancelled() {
                solving.set(false);
                setStatus("Solving canceled.", Color.BLACK);
            }
        };

        new Thread(solveTask).start();
    }

    /**
     * Cancels the solving of the current Sudoku puzzle.
     */
    @FXML
    protected void cancel() {
        solveTask.cancel();
    }

    /**
     * Sets the status message text and its colour.
     *
     * @param text   The text that the status message will contain.
     * @param colour The colour of the status message.
     */
    private void setStatus(String text, Paint colour) {
        statusLabel.setText(text);
        statusLabel.setTextFill(colour);
    }
}
