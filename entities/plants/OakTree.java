package entities.plants;

import core.Environment;
import core.Position;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Represents an oak tree in the ecosystem.
 */
public class OakTree extends Plant {

    /**
     * Creates a new oak tree.
     * @param position the oak tree position
     */
    public OakTree(Position position) {
        super(position, 'T', 80, 120, 2, 0.05);
    }

    /**
     * Attempts to reproduce an oak tree in a nearby free cell.
     * @param env the simulation environment
     * @return true if reproduction succeeded, false otherwise
     */
    @Override
    public boolean reproduce(Environment env) {
        return false;
    }

    /**
     * Compares this oak tree to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o) && (o instanceof OakTree);
    }

    /**
     * Returns a string representation of the oak tree.
     * @return the string representation
     */
    @Override
    public String toString() {
        return super.toString();
    }
}