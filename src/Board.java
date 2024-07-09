import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

/**
 * A minesweeper board with a width, height, amount of mines, and the cells holding the mines. The board
 * is stored as a 2D array with -1 representing a mine, 0 representing a blank space, and positive numbers
 * representing how many mines touch the cell (this includes the eight surrounding neighbors). Flags are assigned
 * by storing a separate 2d array of booleans.
 * @author Hudson Hadley
 */
public class Board {
    /**
     * The width of the board
     */
    private int width;
    /**
     * The height of the board
     */
    private int height;
    /**
     * How many mines are on the board
     */
    private int totalMines;

    /**
     * A 2D array of Cells which describes the current board state
     */
    private Cell[][] cells;

    /**
     * Creates a board with a defined width, height, and total amount of mines.
     * @param width the width we want to assign to the board
     * @param height the height we want to assign to the board
     * @param totalMines the total amount of mines we want to assign to the board
     * @throws IllegalArgumentException if totalMines is greater than the total amount of cells (width x height)
     * @throws NegativeArraySizeException if the width or height is negative
     */
    public Board(int width, int height, int totalMines) {
        if (width < 0 || height < 0)
            throw new NegativeArraySizeException("width and height must be non-negative");
        else if (totalMines > width * height)
            throw new IllegalArgumentException("totalMines must be less than the allotted cells");

        this.width = width;
        this.height = height;
        this.totalMines = totalMines;

        cells = new Cell[height][width];

        fillMines();
        updateCells();
    }

    /**
     * Fills the board with the desired amount of mines
     * @throws IllegalStateException if the board has already been constructed
     */
    private void fillMines() {
        // We need to generate a certain amount of random coordinates to fill with mines
        Random random = new Random();

        // We will store the coordinates as a 1D version of the cells in a hashset. Then we will remove from here at
        // random
        ArrayList<Integer> availableCells = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                availableCells.add(i * width + j); // Convert from 2d to 1d

                // We can also take this time to actually instantiate each cell
                cells[i][j] = new Cell();
            }
        }

        // Keep adding mines until the amount of available cells is equal to the total minus the amount of mines we want
        while (availableCells.size() > (width * height) - totalMines) {
            int index = random.nextInt(availableCells.size());
            int cellIndex = availableCells.get(index);
            cells[cellIndex / width][cellIndex % width].makeMine();
            availableCells.remove(index);
        }
    }

    /**
     * Updates the board after it has been filled with mines such that the cell numbers correspond to how many of
     * their neighbors are mines.
     * @throws IllegalStateException if the board has already been constructed
     */
    private void updateCells() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!cells[i][j].isMine())
                    cells[i][j].setNumber(getNeighborCount(i, j));
            }
        }
    }

    /**
     * Counts how many of the cells neighbors are mines
     * @param row the row of the cell we want to check
     * @param col the column of the cell we want to check
     * @return the number of mines neighboring the cell
     * @throws IndexOutOfBoundsException if the cell is out of bounds
     */
    private int getNeighborCount(int row, int col) {
        if (row < 0 || row > height || col < 0 || col > width)
            throw new IndexOutOfBoundsException("invalid row and col pair");

        int count = 0;

        // Check the 3x3 box around a cell
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Make sure it's in the bounds
                if ( (0 <= row + i && row + i < height) && (0 <= col + j && col + j < width) ) {

                    if (cells[row + i][col + j].isMine())
                        count++;
                }
            }
        }

        // TODO: finish method
        return count;
    }

    /**
     * @return the width of the board
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height of the board
     */
    public int getHeight() {
        return height;
    }

    /**
     * Tests if a cell at a row and column is a mine
     * @param row the row of the cell we want to test
     * @param col the column of the cell we want to test
     * @return true if the cell is a mine
     * @throws IndexOutOfBoundsException if the cell is out of bounds
     */
    public boolean isMine(int row, int col) {
        if (row < 0 || row > height || col < 0 || col > width)
            throw new IndexOutOfBoundsException("invalid row and col pair");
        return cells[row][col].isMine();
    }

    /**
     * Flags/unflags a cell. Whatever the current state of the flag is, it will flip it.
     * @param row the row of the cell
     * @param col the column of the cell
     * @throws IndexOutOfBoundsException if the cell is out of bounds
     */
    public void flag(int row, int col) {
        if (row < 0 || row > height || col < 0 || col > width)
            throw new IndexOutOfBoundsException("invalid row and col pair");

        cells[row][col].flag();
    }

    /**
     * @param row the row of the cell
     * @param col the column of the cell
     * @return true if the cell has a flag
     * @throws IndexOutOfBoundsException if the cell is out of bounds
     */
    public boolean hasFlag(int row, int col) {
        if (row < 0 || row > height || col < 0 || col > width)
            throw new IndexOutOfBoundsException("invalid row and col pair");

        return cells[row][col].hasFlag();
    }

    /**
     * Reveals a certain location and all surrounding blank spots
     * @param row the row of the cell we want to reveal
     * @param col the column of the cell we want to reveal
     */
    public void reveal(int row, int col) {
        // TODO: finish method
    }

    /**
     * A game is won if the board is completely flagged properly. If every cell that has a mine also has a flag, and
     * every cell which does not have a mine has been uncovered, the game is won.
     * @return true if the board is completed and the game is won
     */
    public boolean hasWon() {
        // TODO: finish method
        return false;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (cells[i][j].isMine())
                    output.append("x ");
                else
                    output.append(cells[i][j].getNumber()).append(" ");
            }
            output.append("\n");
        }

        return output.toString();
    }
}
