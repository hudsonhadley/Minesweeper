/**
 * A basic coordinate class for easing the access of elements of a 2D List.
 * @author Hudson Hadley
 */
public class Coordinate {
    /**
     * The row of the coordinate in the 2D List.
     */
    private int row;
    /**
     * The column of the coordinate in the 2D List.
     */
    private int col;

    /**
     * Constructor to initialize a coordinate at (row, col).
     * @param row the row of the coordinate we want to initialize
     * @param col the column of the coordinate we want to initialize
     */
    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return the row of the coordinate
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the column of the coordinate
     */
    public int getCol() {
        return col;
    }

    /**
     * @param other another object we want to see if it is equal
     * @return if the row and col are the same
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        else if (!(other instanceof Coordinate))
            return false;

        Coordinate otherCoordinate = (Coordinate) other;

        return this.row == otherCoordinate.row && this.col == otherCoordinate.col;
    }

    /**
     * @return a String representation of the coordinate as (row, col)
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}