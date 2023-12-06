package sudoku.controller;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
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
 * @version 05/12/2023
 */

public class SudokuController {

    private Stage stage;
    private final Map<String, Integer> boardSizes = new HashMap<>();
    private final Map<String, SudokuSolver> solvers = new HashMap<>();
    private final Map<String, Node> inputMethods = new HashMap<>();
    private int boardSize = 9;
    private int boxSize = (int) Math.sqrt(boardSize);
    private final SimpleDoubleProperty guiBoardSizeProperty = new SimpleDoubleProperty();
    private final SimpleBooleanProperty solving = new SimpleBooleanProperty(false);
    private Task<SudokuBoard> solveTask;

    @FXML
    protected TilePane controls;
    @FXML
    protected ChoiceBox<String> boardSizeSelector;
    @FXML
    protected ChoiceBox<String> solverSelector;
    @FXML
    protected ChoiceBox<String> inputMethodSelector;
    @FXML
    protected Button solve;
    @FXML
    protected Button cancel;
    @FXML
    protected GridPane inputBoard;
    @FXML
    protected TextArea textArea;
    @FXML
    protected GridPane solvedBoard;
    @FXML
    protected ScrollPane root;
    @FXML
    protected VBox leftVBox;
    @FXML
    protected VBox rightVBox;
    @FXML
    protected VBox statusVBox;
    @FXML
    protected Label statusLabel;
    @FXML
    protected ProgressIndicator progressIndicator;
    @FXML
    protected VBox help;

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

        inputMethods.put("Grid", inputBoard);
        inputMethods.put("Text area", textArea);
        inputMethodSelector.getItems().addAll("Grid", "Text area");
        inputMethodSelector.setValue("Grid");
        textArea.setVisible(false);

        rebuildBoard(inputBoard, true);
        rebuildBoard(solvedBoard, false);

        // Rebuild both boards whenever the board size selector's value is changed
        boardSizeSelector.getSelectionModel().selectedItemProperty().addListener((observable,
                                                                                  oldValue,
                                                                                  newValue) -> {
            boardSize = boardSizes.get(newValue);
            // Recalculate the box and cell sizes using the new boardSize
            boxSize = (int) Math.sqrt(boardSize);
            rebuildBoard(inputBoard, true);
            rebuildBoard(solvedBoard, false);
        });

        // Show only the corresponding Node of the newly selected input method
        inputMethodSelector.getSelectionModel().selectedItemProperty().addListener((observable,
                                                                                    oldValue,
                                                                                    newValue) -> {
            inputMethods.values().forEach(inputMethod ->
                    inputMethod.setVisible(inputMethod == inputMethods.get(newValue)));
        });

        // The progress indicator should only be visible when a board is being solved.
        progressIndicator.visibleProperty().bind(solving);
        // The choice boxes and the solve button should be disabled if a puzzle is being solved.
        boardSizeSelector.disableProperty().bind(solving);
        solverSelector.disableProperty().bind(solving);
        solve.disableProperty().bind(solving);
        // The cancel button should be disabled if a puzzle is not being solved.
        cancel.disableProperty().bind(solving.not());

        // Ensure the VBox that contains the status message maintains the same height as the
        // controls TilePane so that both the inputBoard and solvedBoard GridPanes start at the
        // same vertical position.
        statusVBox.minHeightProperty().bind(controls.heightProperty());
        statusVBox.prefHeightProperty().bind(controls.heightProperty());

        // Align the start of the help section with the inputBoard
        help.translateXProperty().bind(inputBoard.layoutXProperty());
    }

    /**
     * Rebuilds the specified board GridPane by clearing all children and then recreating them.
     *
     * @param board    The board GridPane that is to be rebuilt
     * @param editable Whether the board is editable
     */
    private void rebuildBoard(GridPane board, boolean editable) {
        board.getChildren().clear();
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                TextField textField = new TextField();
                DoubleBinding binding = guiBoardSizeProperty.divide(boardSize);
                textField.prefWidthProperty().bind(binding);
                textField.prefHeightProperty().bind(binding);
                textField.minWidthProperty().bind(binding);
                textField.minHeightProperty().bind(binding);
                textField.setEditable(editable);
                textField.setFocusTraversable(editable);
                textField.setAlignment(Pos.CENTER);
                board.add(textField, column, row);

                boolean lastColumnOfBox = (column + 1) % boxSize == 0;
                boolean lastRowOfBox = (row + 1) % boxSize == 0;

                String style = editable ? "" : "-fx-cursor: default;";

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

                // The font size of the text field will need to vary as the size of the text field
                // changes (which happens as stage width changes).
                DoubleBinding fontSizeBinding;
                switch (boardSize) {
                    case 4, 9 -> fontSizeBinding = binding.multiply(0.5);
                    default -> fontSizeBinding = binding.multiply(0.45);
                }

                // This Property stores the style String for the text field. It starts with the
                // border css styles and then appends the font size using the fontSizeBinding
                SimpleStringProperty stringProperty = new SimpleStringProperty(style + "-fx-font" +
                        "-size:");
                // Bind the styleProperty of the text field to the stringProperty so that changes to
                // the stringProperty (as a result of the fontSizeBinding changing due to the stage
                // being resized) updates the CSS for the text field.
                StringExpression stringExpression =
                        stringProperty.concat(fontSizeBinding.asString()).concat("px;");
                textField.styleProperty().bind(stringExpression);

                if (!editable) {
                    textField.addEventFilter(MouseEvent.MOUSE_PRESSED, Event::consume);
                } else {
                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        // Empty String is allowed
                        if (newValue.isEmpty()) return;
                        if (!isValidValue(newValue)) textField.setText(oldValue);
                    });

                    // Add arrow key traversal
                    textField.setOnKeyPressed(event -> {
                        ObservableList<Node> children = board.getChildren();
                        int index = children.indexOf(textField);

                        if (event.getCode() == KeyCode.UP) {
                            if (index >= boardSize) {
                                children.get(index - boardSize).requestFocus();
                            } else {
                                children.get(index + boardSize * (boardSize - 1)).requestFocus();
                            }
                        } else if (event.getCode() == KeyCode.DOWN) {
                            if (index < boardSize * boardSize - boardSize) {
                                children.get(index + boardSize).requestFocus();
                            } else {
                                children.get(index - boardSize * (boardSize - 1)).requestFocus();
                            }
                        } else if (event.getCode() == KeyCode.LEFT) {
                            if (index % boardSize != 0) {
                                children.get(index - 1).requestFocus();
                            } else {
                                children.get(index + boardSize - 1).requestFocus();
                            }
                        } else if (event.getCode() == KeyCode.RIGHT) {
                            if (index % boardSize != boardSize - 1) {
                                children.get(index + 1).requestFocus();
                            } else {
                                children.get(index - (boardSize - 1)).requestFocus();
                            }
                        }
                    });
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
     * Sets the value of each TextField of the given board to the empty String.
     *
     * @param board The GridPane board that is to have its values cleared
     */
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

        // The maximum size of each GridPane board
        DoubleBinding guiBoardSizeBinding = stage.widthProperty().divide(2.6);
        guiBoardSizeProperty.bind(guiBoardSizeBinding);
        inputBoard.maxWidthProperty().bind(guiBoardSizeBinding);
        textArea.maxWidthProperty().bind(guiBoardSizeBinding);
        textArea.maxHeightProperty().bind(guiBoardSizeBinding);
        solvedBoard.maxWidthProperty().bind(guiBoardSizeBinding);
        controls.maxWidthProperty().bind(guiBoardSizeBinding);
        controls.prefWidthProperty().bind(guiBoardSizeBinding);
        controls.hgapProperty().bind(stage.widthProperty().divide(30));
        // Wrap each child at the end of the solvedBoard
        help.getChildren().forEach(child -> {
            if (child.getClass() == Text.class) {
                Text text = (Text) child;
                DoubleBinding wrappingWidth = leftVBox.prefWidthProperty().add(guiBoardSizeBinding);
                text.wrappingWidthProperty().bind(wrappingWidth);
            }
        });
    }

    /**
     * Returns a String representation of the Sudoku board using the Node that was used to input the
     * puzzle.
     *
     * @param inputMethod The Node which was used to input the puzzle
     * @return A String representation of the Sudoku board
     */
    private String getBoardStringFromNode(Node inputMethod) {
        if (inputMethod.getClass() == GridPane.class) {
            return getBoardStringFromInputBoard();
        } else {
            return getBoardStringFromTextArea();
        }
    }

    /**
     * Returns a String representation of the Sudoku board input by the user via the GridPane.
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
     * Returns a String representation of the Sudoku board input by the user via the TextArea.
     *
     * @return The String representation of the Sudoku board input by the user
     */
    private String getBoardStringFromTextArea() {
        return textArea.getText().strip();
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
                Node inputMethod = inputMethods.get(inputMethodSelector.getValue());
                String board = getBoardStringFromNode(inputMethod);
                System.out.println(board);
                SudokuBoard sudokuBoard = new SudokuBoard(board);
                int actualSize = sudokuBoard.getSIZE();
                // This check is necessary since if a board is input by the text area input method,
                // it may be a legal board but of the incorrect size.
                if (actualSize != boardSize) {
                    throw new IllegalStateException("Expected " + boardSize + "x" + boardSize +
                            " board but received " + actualSize + "x" + actualSize + " board.");
                }
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
                solving.set(false);
                setStatus(getException().getMessage(), Color.RED);
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
