package interfaces;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Represents an entity that can eat consumable targets.
 */
public interface Eater {

    /**
     * Attempts to eat the given consumable target.
     * @param target the target to consume
     * @return true if the eating succeeded, false otherwise
     */
    boolean eat(Consumable target);
}