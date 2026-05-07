package interfaces;

import core.Environment;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Represents an entity that performs an action in each simulation tick.
 */
public interface Actable {

    /**
     * Performs one action in the current tick.
     * @param env the simulation environment
     * @return true if the action succeeded, false otherwise
     */
    boolean act(Environment env);
}