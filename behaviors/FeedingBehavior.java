package behaviors;

import java.util.List;
import entities.AbstractEntity;
import entities.animals.Animal;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Strategy interface for animal feeding behavior.
 */
public interface FeedingBehavior {

    /**
     * Attempts to feed the given animal using nearby entities.
     * @param eater the animal that tries to eat
     * @param nearby the nearby entities
     * @return true if feeding succeeded, false otherwise
     */
    boolean eat(Animal eater, List<AbstractEntity> nearby);
}