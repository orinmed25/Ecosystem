package core;

import entities.AbstractEntity;
import interfaces.Actable;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * A daemon thread that drives a single active (Actable) entity. Each iteration the thread
 * sleeps a random 500-1500 ms, then asks the entity to act on the shared environment.
 *
 * The thread stops cleanly when the engine is no longer running or when the entity dies.
 * The whole run loop is wrapped in try/catch so a failure is reported rather than swallowed.
 */
public class EntityThread extends Thread {

    /** Minimum delay between actions, in milliseconds. */
    private static final int MIN_INTERVAL_MS = 500;
    /** Maximum delay between actions, in milliseconds. */
    private static final int MAX_INTERVAL_MS = 1500;

    private final AbstractEntity entity;
    private final Actable actable;
    private final Environment environment;
    private final SimulationEngine engine;

    /**
     * Creates a thread for the given entity.
     * @param entity the entity, used for liveness and cleanup
     * @param actable the same entity viewed as Actable, used to perform actions
     * @param environment the shared world
     * @param engine the owning engine, used for the running flag, logging and notifications
     */
    public EntityThread(AbstractEntity entity, Actable actable,
                        Environment environment, SimulationEngine engine) {
        super("entity-" + entity.getClass().getSimpleName() + "-" + System.identityHashCode(entity));
        this.entity = entity;
        this.actable = actable;
        this.environment = environment;
        this.engine = engine;
        setDaemon(true);
    }

    /**
     * Runs the entity's life loop until shutdown or death.
     */
    @Override
    public void run() {
        try {
            while (engine.isRunning() && entity.isAlive()) {
                Thread.sleep(MIN_INTERVAL_MS
                        + (int) (Math.random() * (MAX_INTERVAL_MS - MIN_INTERVAL_MS + 1)));
                if (!engine.isRunning() || !entity.isAlive()) {
                    break;
                }
                actable.act(this.environment);
                engine.onEntityActed();
            }
        } catch (InterruptedException e) {
            
        } catch (Throwable t) {
           
            engine.logError(entity, t);
        } finally {
            if (!entity.isAlive()) {
                environment.removeEntity(entity);
            }
            engine.onThreadEnded(this);
        }
    }
}
