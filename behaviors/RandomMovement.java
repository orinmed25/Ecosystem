package behaviors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import core.Environment;
import core.Position;
import entities.animals.Animal;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Random movement strategy. Chooses a random free adjacent cell.
 */
public class RandomMovement implements MovementStrategy {

    /**
     * Attempts to move the animal to a random free adjacent cell.
     * @param animal the moving animal
     * @param env the simulation environment
     * @return true if movement succeeded, false otherwise
     */
    @Override
    public boolean move(Animal animal, Environment env) {
        if (animal == null || env == null || animal.getPosition() == null) {
            return false;
        }

        Position current = animal.getPosition();
        List<Position> candidates = new ArrayList<>();

        candidates.add(new Position(current.getRow() - 1, current.getCol()));
        candidates.add(new Position(current.getRow() + 1, current.getCol()));
        candidates.add(new Position(current.getRow(), current.getCol() - 1));
        candidates.add(new Position(current.getRow(), current.getCol() + 1));

        Collections.shuffle(candidates);

        for (Position candidate : candidates) {
            if (env.tryMove(animal, candidate)) {
                return true;
            }
        }

        return false;
    }
}