package interfaces;

import java.util.List;
import core.Environment;
import entities.AbstractEntity;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Represents an entity that can sense nearby entities.
 */
public interface Sensory {

    /**
     * Returns nearby entities in the environment.
     * @param env the simulation environment
     * @return a list of nearby entities
     */
    List<AbstractEntity> sense(Environment env);
}