package entities.resources;

import core.Position;
import interfaces.Consumable;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 *
 * Represents a water resource that can be consumed.
 */
public class Water extends Resource implements Consumable {

    /**
     * Creates a new water resource.
     * @param position the water position
     */
    public Water(Position position) {
        super(position, 'W');
    }

    /**
     * Returns the nutritional value of water.
     * @return 100
     */
    @Override
    public double getNutritionValue() {
        return 100;
    }

    /**
     * Defines what happens when water is consumed.
     * Water does not disappear after being consumed.
     * @return true if the action succeeded
     */
    @Override
    public boolean onConsumed() {
        return true;
    }

    /**
     * Compares this water resource to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o) && (o instanceof Water);
    }

    /**
     * Returns a string representation of the water resource.
     * @return the string representation
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " "
                + getPosition() + " "
                + "<100> "
                + "<" + isAlive() + ">";
    }
}