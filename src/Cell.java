/**
 * A cell of a minesweeper board. Each cell has a number, as well as two boolean variables to describe it: isFlagged
 * and isRevealed. The number is -1 if it is a mine, 0 if it is blank, and positive if it
 * has a neighbor which is a mine. The number will correspond to how many neighbors are mines.
 * @author Hudson Hadley
 */
public class Cell {
    /**
     * The number of the cell which is -1 if it is a mine, 0 if it is blank, and positive if it has a neighbor that
     * is a mine. If it is 2, then it has 2 and exactly 2 neighbors which are mines.
     */
    private int number;
    /**
     * If the cell is flagged
     */
    private boolean isFlagged;
    /**
     * If the cell is revealed. (Note that a cell cannot both be flagged and revealed at the same time)
     */
    private boolean isRevealed;

    /**
     * The default constructor which creates a blank cell with hasn't been revealed, and isn't flagged.
     */
    public Cell() {
        // TODO: finish method
    }

    /**
     * @return the number of the cell
     */
    public int getNumber() {
        // TODO: finish method
        return -1;
    }

    /**
     * Sets the number of the cell
     * @param number the number we want to assign to the cell
     */
    public void setNumber(int number) {
        // TODO: finish method
    }

    /**
     * @return true if the cell is a mine
     */
    public boolean isMine() {
        // TODO: finish method
        return false;
    }

    /**
     * @return true if the cell is flagged
     */
    public boolean isFlagged() {
        // TODO: finish method
        return false;
    }

    /**
     * @return true if the cell is flagged
     */
    public boolean isRevealed() {
        // TODO: finish method
        return false;
    }

    /**
     * Flips the flag boolean variable. If the cell was flagged, it will switch to not being flagged and vice versa.
     */
    public void flag() {
        // TODO: finish method
    }

    /**
     * If the cell is not already revealed, it set the isRevealed variable to true.
     */
    public void reveal() {
        // TODO: finish method
    }
}
