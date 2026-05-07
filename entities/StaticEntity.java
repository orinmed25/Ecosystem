package entities;

import core.Position;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Abstract base class for all static entities in the ecosystem.
 */
public abstract class StaticEntity extends AbstractEntity {

    /**
     * Creates a new static entity.
     * @param position the entity position
     * @param symbol the entity display symbol
     */
    public StaticEntity(Position position, char symbol) {
        super(position, symbol);
    }

    /**
     * Compares this static entity to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o) && (o instanceof StaticEntity);
    }

    /**
     * Returns a string representation of the static entity.
     * @return the string representation
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " "
                + getPosition() + " "
                + "<N/A> "
                + "<" + isAlive() + ">";
    }
}