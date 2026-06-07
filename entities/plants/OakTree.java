package entities.plants;

import java.util.List;
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
     * Attempts to reproduce a new oak tree in a free cell at Manhattan distance 1.
     * @param env the simulation environment
     * @return true if an oak tree was created, false otherwise
     */
    @Override
    public boolean reproduce(Environment env) {
        if (env == null || !isAlive()) {
            return false;
        }
        if (Math.random() >= getReproductionChance()) {
            return false;
        }

        List<Position> free = freeCellsWithin(env, 1);
        if (free.isEmpty()) {
            return false;
        }

        Position spot = free.get((int) (Math.random() * free.size()));
        return env.addEntity(new OakTree(spot));
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
