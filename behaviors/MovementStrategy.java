package behaviors;

import core.Environment;
import entities.animals.Animal;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Strategy interface for animal movement.
 */
public interface MovementStrategy {

    /**
     * Attempts to move the given animal in the environment.
     * @param animal the moving animal
     * @param env the simulation environment
     * @return true if movement succeeded, false otherwise
     */
    boolean move(Animal animal, Environment env);
}