import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

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

        // We will store the coordinates as a coordinate list and randomly remove from it
        ArrayList<Coordinate> availableCells = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                availableCells.add(new Coordinate(i, j));

                // We can also take this time to actually instantiate each cell
                cells[i][j] = new Cell();
            }
        }

        // Keep adding mines until the amount of available cells is equal to the total minus the amount of mines we want
        while (availableCells.size() > (width * height) - totalMines) {
            int index = random.nextInt(availableCells.size());
            Coordinate cellCoordinate = availableCells.get(index);

            cells[cellCoordinate.getRow()][cellCoordinate.getCol()].makeMine();
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
     * @param row the row of the cell
     * @param col the column of the cell
     * @return true if the cell has been revealed
     * @throws IndexOutOfBoundsException if the cell is out of bounds
     */
    public boolean isRevealed(int row, int col) {
        if (row < 0 || row > height || col < 0 || col > width)
            throw new IndexOutOfBoundsException("invalid row and col pair");

        return cells[row][col].isRevealed();
    }

    /**
     * Reveals a certain location and all surrounding blank spots
     * @param row the row of the cell we want to reveal
     * @param col the column of the cell we want to reveal
     * @return if the game continues or not
     * @throws IndexOutOfBoundsException if the cell is out of bounds
     */
    public boolean reveal(int row, int col) {
        if (row < 0 || row > height || col < 0 || col > width)
            throw new IndexOutOfBoundsException("invalid row and col pair");

        // If they hit a mine, game over
        if (cells[row][col].isMine()) {
            cells[row][col].reveal();
            return false;
        } else if (!cells[row][col].isBlank()) { // If they hit a number, we just reveal that number and nothing else
            cells[row][col].reveal();
            return true;
        } else if (cells[row][col].isRevealed()) // If it is already revealed, do nothing
            return true;

        /* Otherwise they must have hit a blank space. In this case, we must reveal every blank space touching which
         * can be travelled to from the current blank space while only stepping on blank spaces going up, down, right,
         * and left. Additionally, the "coastline" of numbers must also be revealed.
         */

        // TREAT AS STACK
        Deque<Coordinate> stack = new ArrayDeque<>();
        cells[row][col].reveal();
        stack.push(new Coordinate(row, col)); // Add the first one

        // Keep going until we get back to the start
        while (!stack.isEmpty()) {
            // For every coordinate at the top of the stack, we need to reveal all number neighbors and venture to the
            // next blank space
            Coordinate current = stack.peek();
            Coordinate next = new Coordinate(0, 0);
            boolean nextFound = false;

            // Check the 3x3 box around a cell
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    // Make sure it's in the bounds
                    if ( (0 <= current.getRow() + i && current.getRow() + i < height) &&
                            (0 <= current.getCol() + j && current.getCol() + j < width) ) {

                        // It is impossible for it to be a mine since it is adjacent to a blank space, so we are only
                        // checking if it is not a blank space
                        if (!cells[current.getRow() + i][current.getCol() + j].isBlank())
                            cells[current.getRow() + i][current.getCol() + j].reveal(); // Reveal the number

                        // If it is blank, one of the add ones is 0 (i.e. it is not a diagonal), and it isn't revealed,
                        // and we still have not found a next coordinate
                        else if ( (i == 0 || j == 0) && !nextFound &&
                                !cells[current.getRow() + i][current.getCol() + j].isRevealed()) {

                            next = new Coordinate(current.getRow() + i, current.getCol() + j);
                            nextFound = true;
                        }
                    }
                }
            }

            // If we found a space to go next, add it to the stack and reveal it
            if (nextFound) {
                stack.push(next);
                cells[next.getRow()][next.getCol()].reveal();
            } else // If we didn't find a place, backtrack
                stack.pop();
        }

        return true;
    }

    /**
     * A game is won if the board is completely flagged properly. If every cell that has a mine also has a flag, and
     * every cell which does not have a mine has been uncovered, the game is won.
     * @return true if the board is completed and the game is won
     */
    public boolean hasWon() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // If it is a mine and doesn't have a flag, the game isn't won
                if (cells[i][j].isMine() && !cells[i][j].hasFlag())
                    return false;
                // If it isn't a mine and it isn't revealed, the game isn't won
                else if (!cells[i][j].isMine() && !cells[i][j].isRevealed())
                    return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (cells[i][j].isRevealed() && cells[i][j].isMine())
                    output.append(ANSI_RED + "X" + ANSI_RED);
                else if (cells[i][j].isRevealed() && !cells[i][j].isMine())
                    if (cells[i][j].isBlank())
                        output.append(ANSI_WHITE).append(cells[i][j].getNumber()).append(ANSI_WHITE);
                    else
                        output.append(ANSI_YELLOW).append(cells[i][j].getNumber()).append(ANSI_YELLOW);
                else if (cells[i][j].hasFlag())
                    output.append(ANSI_GREEN + "!" + ANSI_GREEN);
                else
                    output.append(ANSI_BLUE).append("#").append(ANSI_BLUE);
                output.append(" ");
            }
            output.append(ANSI_RESET).append("\n").append(ANSI_RESET);
        }

        return output.toString();
    }
}
