package entities.plants;

import java.util.Collections;
import java.util.List;
import core.Environment;
import core.Position;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
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
     * Attempts to reproduce one to three flowers in free cells at Manhattan distance up to 2.
     * @param env the simulation environment
     * @return true if at least one flower was created, false otherwise
     */
    @Override
    public boolean reproduce(Environment env) {
        if (env == null || !isAlive()) {
            return false;
        }
        if (Math.random() >= getReproductionChance()) {
            return false;
        }

        List<Position> free = freeCellsWithin(env, 2);
        if (free.isEmpty()) {
            return false;
        }

        Collections.shuffle(free);
        int count = 1 + (int) (Math.random() * 3);
        int created = 0;
        for (int i = 0; i < free.size() && created < count; i++) {
            if (env.addEntity(new Flower(free.get(i)))) {
                created++;
            }
        }
        return created > 0;
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
