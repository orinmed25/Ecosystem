package entities.animals;

import behaviors.EscapeMovement;
import behaviors.HerbivoreBehavior;
import core.Position;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Represents a deer in the ecosystem.
 */
public class Deer extends Animal {

    /**
     * Creates a new deer.
     * @param position the deer position
     */
    public Deer(Position position) {
        super(position, 'D', 70, 70, new EscapeMovement(), new HerbivoreBehavior());
    }

    /**
     * Compares this deer to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o) && (o instanceof Deer);
    }

    /**
     * Returns a string representation of the deer.
     * @return the string representation
     */
    @Override
    public String toString() {
        return super.toString();
    }
}