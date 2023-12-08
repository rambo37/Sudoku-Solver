package sudoku_solver.gui;

import sudoku_solver.controller.SudokuController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is used to launch the JavaFX application and serves no other purpose.
 *
 * @author Savraj Bassi
 * @version 29/11/2023
 */

public class SudokuGUI extends Application {

    /**
     * Invokes the launch method of the javafx.application.Application class to launch the
     * application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application by loading SudokuGUI.fxml, creating a Scene for the Stage, showing
     * the Stage and initialises the SudokuController by passing the primaryStage object to it.
     *
     * @param primaryStage The primary Stage object for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        FXMLLoader fxmlLoader = new FXMLLoader(SudokuGUI.class.getResource("/FXML/SudokuGUI.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("/CSS/Application.css");
            primaryStage.setTitle("Sudoku solver");
            primaryStage.setScene(scene);
            primaryStage.show();
            // Pass the primary stage to the controller
            SudokuController sudokuController = fxmlLoader.getController();
            sudokuController.setStage(primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}