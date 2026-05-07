package behaviors;

import java.util.List;
import entities.AbstractEntity;
import entities.animals.Animal;
import interfaces.Consumable;
import interfaces.EdibleByHerbivore;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Feeding behavior for herbivores.
 */
public class HerbivoreBehavior implements FeedingBehavior {

    /**
     * Attempts to feed on the nearest valid herbivore target.
     * @param eater the animal that tries to eat
     * @param nearby the nearby entities
     * @return true if feeding succeeded, false otherwise
     */
    @Override
    public boolean eat(Animal eater, List<AbstractEntity> nearby) {
        if (eater == null || nearby == null || !eater.isAlive()) {
            return false;
        }

        AbstractEntity nearestTarget = null;
        int minDistance = Integer.MAX_VALUE;

        for (AbstractEntity entity : nearby) {
            if (entity == null || entity.equals(eater)) {
                continue;
            }

            if (entity instanceof EdibleByHerbivore && entity instanceof Consumable) {
                int distance = eater.getPosition().distanceTo(entity.getPosition());
                if (distance <= 1 && distance < minDistance) {
                    minDistance = distance;
                    nearestTarget = entity;
                }
            }
        }

        if (nearestTarget == null) {
            return false;
        }

        return eater.eat((Consumable) nearestTarget);
    }
}