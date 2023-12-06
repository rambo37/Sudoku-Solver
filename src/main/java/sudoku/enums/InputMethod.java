package sudoku.enums;

/**
 * An enumeration representing different methods for inputting a Sudoku puzzle in the GUI.
 *
 * @author Savraj Bassi
 * @version 06/12/2023
 */

public enum InputMethod {
    GRID("Grid"),
    TEXT_AREA("Text area");

    private final String displayName;

    /**
     * Constructs an InputMethod object with the specified name.
     *
     * @param displayName the display name of the input method
     */
    InputMethod(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the input method.
     *
     * @return the display name
     */
    @Override
    public String toString() {
        return displayName;
    }
}
