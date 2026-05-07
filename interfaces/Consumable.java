package interfaces;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Represents an entity that can be consumed.
 */
public interface Consumable {
    /**
     * Returns the nutritional value gained from consuming this entity.
     * @return the nutrition value
     */
    double getNutritionValue();

    /**
     * Defines what happens after this entity is consumed.
     * @return true if the consume effect succeeded, false otherwise
     */
    boolean onConsumed();
}