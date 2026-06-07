package core;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Represents a position on the 2D ecosystem map.
 */
public class Position {
    private int row;
    private int col;

    /**
     * Creates a new position.
     * @param row the row index
     * @param col the column index
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the row of this position.
     * @return the row value
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns the column of this position.
     * @return the column value
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Sets the row if the value is valid.
     * @param row the new row
     * @return true if the update succeeded, false otherwise
     */
    public boolean setRow(int row) {
        if (row < 0) {
            return false;
        }
        this.row = row;
        return true;
    }

    /**
     * Sets the column if the value is valid.
     * @param col the new column
     * @return true if the update succeeded, false otherwise
     */
    public boolean setCol(int col) {
        if (col < 0) {
            return false;
        }
        this.col = col;
        return true;
    }

    /**
     * Calculates the Manhattan distance between this position and another position.
     * @param other the other position
     * @return the Manhattan distance, or -1 if other is null
     */
    public int distanceTo(Position other) {
        if (other == null) {
            return -1;
        }
        return Math.abs(this.row - other.row) + Math.abs(this.col - other.col);
    }

    /**
     * Compares this position to another object.
     * @param o the object to compare to
     * @return true if both positions are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        Position other = (Position) o;
        return this.row == other.row && this.col == other.col;
    }

    /**
     * Returns a string representation of this position.
     * @return the position as a string
     */
    @Override
    public String toString() {
        return "(" + this.row + "," + this.col + ")";
    }
}