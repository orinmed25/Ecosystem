package interfaces;

import core.Environment;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Represents an entity that can reproduce.
 */
public interface Reproducible {

    /**
     * Attempts to reproduce in the environment.
     * @param env the simulation environment
     * @return true if reproduction succeeded, false otherwise
     */
    boolean reproduce(Environment env);
}
