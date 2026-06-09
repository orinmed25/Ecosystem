package interfaces;

import entities.AbstractEntity;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Callback interface that lets the {@code Environment} notify a manager (the simulation
 * engine) when entities are added to or removed from the world. The engine uses this to
 * start a thread for newly born entities and to reap threads of removed ones, without the
 * model holding any reference to the threading layer or the GUI.
 */
public interface EntityLifecycleListener {

    /**
     * Invoked after an entity was successfully added to the environment.
     * @param entity the entity that was added
     */
    void onEntityAdded(AbstractEntity entity);

    /**
     * Invoked after an entity was removed from the environment.
     * @param entity the entity that was removed
     */
    void onEntityRemoved(AbstractEntity entity);
}
