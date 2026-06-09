package behaviors;

import java.util.List;
import core.Environment;
import core.Position;
import entities.AbstractEntity;
import entities.animals.Animal;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Movement strategy that moves away from the nearest nearby entity.
 */
public class EscapeMovement implements MovementStrategy {

    /**
     * Attempts to move the animal one step away from the nearest nearby entity.
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
        Position threat = nearest.getPosition();

        int newRow = current.getRow();
        int newCol = current.getCol();

        if (threat.getRow() < current.getRow()) {
            newRow++;
        } else if (threat.getRow() > current.getRow()) {
            newRow--;
        } else if (threat.getCol() < current.getCol()) {
            newCol++;
        } else if (threat.getCol() > current.getCol()) {
            newCol--;
        }

        Position next = new Position(newRow, newCol);

        
        return env.tryMove(animal, next);
    }
}