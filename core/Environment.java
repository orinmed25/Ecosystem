package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import entities.AbstractEntity;
import entities.resources.Rock;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Represents the ecosystem world and manages all entities in it.
 */
public class Environment {
    private int rows;
    private int cols;
    private List<AbstractEntity> entities;

    /**
     * Creates a new environment.
     * @param rows number of rows in the map
     * @param cols number of columns in the map
     */
    public Environment(int rows, int cols) {
        this.rows = Math.max(10, rows);
        this.cols = Math.max(10, cols);
        this.entities = new ArrayList<>();
    }

    /**
     * Returns the number of rows.
     * @return the number of rows
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * Returns the number of columns.
     * @return the number of columns
     */
    public int getCols() {
        return this.cols;
    }

    /**
     * Returns an unmodifiable view of the entities list.
     * @return the entities list
     */
    public List<AbstractEntity> getEntities() {
        return Collections.unmodifiableList(this.entities);
    }

    /**
     * Sets the number of rows if valid.
     * @param rows the new number of rows
     * @return true if update succeeded, false otherwise
     */
    protected boolean setRows(int rows) {
        if (rows < 10) {
            return false;
        }
        this.rows = rows;
        return true;
    }

    /**
     * Sets the number of columns if valid.
     * @param cols the new number of columns
     * @return true if update succeeded, false otherwise
     */
    protected boolean setCols(int cols) {
        if (cols < 10) {
            return false;
        }
        this.cols = cols;
        return true;
    }

    /**
     * Checks whether a position is free.
     * A position is free if it is inside the map and has no entity or blocking rock.
     * @param next the position to check
     * @return true if the position is free, false otherwise
     */
    public boolean isPositionFree(core.Position next) {
        if (next == null) {
            return false;
        }

        if (next.getRow() < 0 || next.getRow() >= this.rows || next.getCol() < 0 || next.getCol() >= this.cols) {
            return false;
        }

        for (AbstractEntity entity : this.entities) {
            if (entity.getPosition().equals(next)) {
                return false;
            }
            if (entity instanceof Rock && entity.getPosition().equals(next)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns all entities in Manhattan distance less than or equal to 2 from the given position.
     * @param pos the center position
     * @return a list of nearby entities
     */
    public List<AbstractEntity> getNearbyEntities(core.Position pos) {
        List<AbstractEntity> nearby = new ArrayList<>();

        if (pos == null) {
            return nearby;
        }

        for (AbstractEntity entity : this.entities) {
            if (entity.getPosition().distanceTo(pos) <= 2) {
                nearby.add(entity);
            }
        }

        return nearby;
    }

    /**
     * Adds an entity to the environment if possible.
     * @param entity the entity to add
     * @return true if the add succeeded, false otherwise
     */
    public boolean addEntity(AbstractEntity entity) {
        if (entity == null || entity.getPosition() == null) {
            return false;
        }

        if (!isPositionFree(entity.getPosition())) {
            return false;
        }

        return this.entities.add(entity);
    }

    /**
     * Removes an entity from the environment.
     * @param entity the entity to remove
     * @return true if removal succeeded, false otherwise
     */
    public boolean removeEntity(AbstractEntity entity) {
        if (entity == null) {
            return false;
        }
        return this.entities.remove(entity);
    }

    /**
     * Removes all dead entities from the environment.
     * @return true if the cleanup completed
     */
    public boolean removeDeadEntities() {
        boolean changed = false;
        Iterator<AbstractEntity> iterator = this.entities.iterator();

        while (iterator.hasNext()) {
            AbstractEntity entity = iterator.next();
            if (!entity.isAlive()) {
                iterator.remove();
                changed = true;
            }
        }

        return changed;
    }

    /**
     * Prints the current map using entity symbols.
     * @return true if printing completed
     */
    public boolean printMap() {
        char[][] map = new char[this.rows][this.cols];

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                map[i][j] = ' ';
            }
        }

        for (AbstractEntity entity : this.entities) {
            Position pos = entity.getPosition();
            if (pos.getRow() >= 0 && pos.getRow() < this.rows && pos.getCol() >= 0 && pos.getCol() < this.cols) {
                map[pos.getRow()][pos.getCol()] = entity.getSymbol();
            }
        }

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                System.out.print("[" + map[i][j] + "]");
            }
            System.out.println();
        }

        return true;
    }

    /**
     * Compares this environment to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Environment)) {
            return false;
        }
        Environment other = (Environment) o;
        return this.rows == other.rows
                && this.cols == other.cols
                && this.entities.equals(other.entities);
    }

    /**
     * Returns a string representation of the environment.
     * @return the string representation
     */
    @Override
    public String toString() {
        return "Environment <rows=" + this.rows + ", cols=" + this.cols
                + ", entities=" + this.entities.size() + ">";
    }
}