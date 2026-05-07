package entities.plants;

import core.Environment;
import core.Position;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 *
 * Represents a flower in the ecosystem.
 */
public class Flower extends Plant {

    /**
     * Creates a new flower.
     * @param position the flower position
     */
    public Flower(Position position) {
        super(position, 'F', 70, 70, 5, 0.2);
    }

    /**
     * Attempts to reproduce one to three flowers in nearby free cells.
     * @param env the simulation environment
     * @return true if reproduction succeeded, false otherwise
     */
    @Override
    public boolean reproduce(Environment env) {
        return false;
    }

    /**
     * Compares this flower to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o) && (o instanceof Flower);
    }

    /**
     * Returns a string representation of the flower.
     * @return the string representation
     */
    @Override
    public String toString() {
        return super.toString();
    }
}