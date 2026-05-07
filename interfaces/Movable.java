package interfaces;

import core.Environment;
/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Represents an entity that can move in the environment.
 */
public interface Movable {
    /**
     * Attempts to move the entity in the environment.
     * @param env the simulation environment
     * @return true if the movement succeeded, false otherwise
     */
    boolean move(Environment env);
}