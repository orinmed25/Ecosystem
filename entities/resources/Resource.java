package entities.resources;

import core.Position;
import entities.StaticEntity;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Abstract base class for all resource entities in the ecosystem.
 */
public abstract class Resource extends StaticEntity {

    /**
     * Creates a new resource.
     * @param position the resource position
     * @param symbol the resource display symbol
     */
    public Resource(Position position, char symbol) {
        super(position, symbol);
    }

    /**
     * Compares this resource to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o) && (o instanceof Resource);
    }

    /**
     * Returns a string representation of the resource.
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
