package entities;

import java.util.Objects;
import core.Position;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Base abstract class for all entities in the ecosystem.
 */
public abstract class AbstractEntity {
    private volatile Position position;
    private char symbol;
    private volatile boolean alive;

    /**
     * Creates a new entity.
     * @param position the entity position
     * @param symbol the entity display symbol
     */
    public AbstractEntity(Position position, char symbol) {
        this.position = position;
        this.symbol = symbol;
        this.alive = true;
    }

    /**
     * Returns the entity position.
     * @return the position
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Returns the entity symbol.
     * @return the symbol
     */
    public char getSymbol() {
        return this.symbol;
    }

    /**
     * Returns whether the entity is alive.
     * @return true if alive, false otherwise
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Sets the entity position if valid.
     * @param position the new position
     * @return true if update succeeded, false otherwise
     */
    protected boolean setPosition(Position position) {
        if (position == null) {
            return false;
        }
        this.position = position;
        return true;
    }

    /**
     * Sets the entity symbol if valid.
     * @param symbol the new symbol
     * @return true if update succeeded, false otherwise
     */
    protected boolean setSymbol(char symbol) {
        if (symbol == '\0') {
            return false;
        }
        this.symbol = symbol;
        return true;
    }

    /**
     * Sets the alive status.
     * @param alive the new alive value
     * @return true if update succeeded
     */
    protected boolean setAlive(boolean alive) {
        this.alive = alive;
        return true;
    }

    /**
     * Compares this entity to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractEntity)) {
            return false;
        }
        AbstractEntity other = (AbstractEntity) o;
        return this.symbol == other.symbol
                && this.alive == other.alive
                && Objects.equals(this.position, other.position);
    }

    /**
     * Returns a string representation of the entity.
     * @return the string representation
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " "
                + this.position + " "
                + "<N/A> "
                + "<" + this.alive + ">";
    }
}