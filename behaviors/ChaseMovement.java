package behaviors;

import java.util.List;
import core.Environment;
import core.Position;
import entities.AbstractEntity;
import entities.animals.Animal;

/**
 * Student 1: <FULL_NAME> <ID>
 * Student 2: <FULL_NAME> <ID>
 *
 * Movement strategy that moves toward the nearest nearby entity.
 */
public class ChaseMovement implements MovementStrategy {

    /**
     * Attempts to move the animal one step toward the nearest nearby entity.
     *
     * @param animal the moving animal
     * @param env the simulation environment
     * @return true if movement succeeded, false otherwise
     */
    @Override
    public boolean move(Animal animal, Environment env) {
        if (animal == null || env == null || animal.getPosition() == null) {
            return false;
        }

        List<AbstractEntity> nearby = env.getNearbyEntities(animal.getPosition());
        AbstractEntity nearest = null;
        int minDistance = Integer.MAX_VALUE;

        for (AbstractEntity entity : nearby) {
            if (entity == null || entity.equals(animal)) {
                continue;
            }

            int distance = animal.getPosition().distanceTo(entity.getPosition());
            if (distance < minDistance) {
                minDistance = distance;
                nearest = entity;
            }
        }

        if (nearest == null) {
            return false;
        }

        Position current = animal.getPosition();
        Position target = nearest.getPosition();

        int newRow = current.getRow();
        int newCol = current.getCol();

        if (target.getRow() < current.getRow()) {
            newRow--;
        } else if (target.getRow() > current.getRow()) {
            newRow++;
        } else if (target.getCol() < current.getCol()) {
            newCol--;
        } else if (target.getCol() > current.getCol()) {
            newCol++;
        }

        Position next = new Position(newRow, newCol);

        if (!env.isPositionFree(next)) {
            return false;
        }

        return animal.moveTo(next);
    }
}