package entities.animals;

import behaviors.CarnivoreBehavior;
import behaviors.ChaseMovement;
import core.Position;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Represents a lion in the ecosystem.
 */
public class Lion extends Animal {

    /**
     * Creates a new lion.
     * @param position the lion position
     */
    public Lion(Position position) {
        super(position, 'L', 100, 100, new ChaseMovement(), new CarnivoreBehavior());
    }

    /**
     * Compares this lion to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o) && (o instanceof Lion);
    }

    /**
     * Returns a string representation of the lion.
     * @return the string representation
     */
    @Override
    public String toString() {
        return super.toString();
    }
}