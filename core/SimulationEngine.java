package core;

import entities.AbstractEntity;
import interfaces.Actable;
import interfaces.EcosystemObserver;
import interfaces.EntityLifecycleListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Manages the live, multithreaded simulation: it starts and stops a thread per active
 * entity, reaps threads when entities die, logs important events, and notifies observers
 * (the GUI) whenever the world changes. It also implements {@link EntityLifecycleListener}
 * so that entities born at runtime automatically get their own thread.
 */
public class SimulationEngine implements EntityLifecycleListener {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Environment environment;
    private final List<EcosystemObserver> observers = new CopyOnWriteArrayList<>();
    private final List<EntityThread> threads = new CopyOnWriteArrayList<>();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicInteger actionCount = new AtomicInteger(0);

    /**
     * Creates a new simulation engine and registers itself as the environment's
     * lifecycle listener.
     * @param environment the simulation environment
     */
    public SimulationEngine(Environment environment) {
        this.environment = environment;
        if (environment != null) {
            environment.setLifecycleListener(this);
        }
    }

    /**
     * Returns the environment.
     * @return the environment
     */
    public Environment getEnvironment() {
        return this.environment;
    }

    /**
     * Returns the number of entity actions performed since the last reset.
     * In the live (threaded) simulation there is no single global tick, so this counter
     * of individual entity actions is shown as the "tick" progress indicator.
     * @return the action count
     */
    public int getTickCount() {
        return actionCount.get();
    }

    /**
     * Returns whether the live simulation is currently running.
     * @return true if running, false otherwise
     */
    public boolean isRunning() {
        return running.get();
    }

    /**
     * Sets the environment if valid.
     * @param environment the new environment
     * @return true if update succeeded, false otherwise
     */
    protected boolean setEnvironment(Environment environment) {
        if (environment == null) {
            return false;
        }
        this.environment = environment;
        environment.setLifecycleListener(this);
        return true;
    }

    /**
     * Registers an observer to be notified after each world change.
     * @param observer the observer to add
     */
    public void addObserver(EcosystemObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Removes a previously registered observer.
     * @param observer the observer to remove
     */
    public void removeObserver(EcosystemObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        int count = actionCount.get();
        for (EcosystemObserver obs : observers) {
            obs.onWorldChanged(environment, count);
        }
    }

    
    /**
     * Starts the live simulation: spawns one thread for every alive active entity.
     * Does nothing if already running.
     */
    public synchronized void start() {
        if (running.get() || this.environment == null) {
            return;
        }
        running.set(true);
        log("Simulation started");
        for (AbstractEntity entity : environment.getEntities()) {
            spawnThread(entity);
        }
    }

    /**
     * Stops the live simulation: signals all threads to finish, interrupts them so they
     * wake from sleeping/waiting, and waits briefly for them to end. Does nothing if not running.
     */
    public synchronized void stop() {
        if (!running.get()) {
            return;
        }
        running.set(false);
        log("Simulation stopped");

        List<EntityThread> current = new ArrayList<>(threads);
        for (EntityThread t : current) {
            t.interrupt();
        }
        environment.signalAllWaiters();
        for (EntityThread t : current) {
            try {
                t.join(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        threads.clear();
    }

    /**
     * Performs a single synchronous simulation round. Intended for the "Step" button while
     * the live simulation is stopped; ignored while it is running.
     */
    public synchronized void step() {
        if (running.get()) {
            log("Step ignored: live simulation is running");
            return;
        }
        if (this.environment == null) {
            return;
        }
        tick();
    }

    /**
     * Runs one synchronous round: every active entity acts once, dead entities are removed,
     * the action counter advances and observers are notified. Used by {@link #step()}.
     */
    public void tick() {
        for (AbstractEntity entity : environment.getEntities()) {
            if (entity instanceof Actable && entity.isAlive()) {
                ((Actable) entity).act(this.environment);
            }
        }
        environment.removeDeadEntities();
        actionCount.incrementAndGet();
        notifyObservers();
    }

    /**
     * Stops the simulation and clears the world, resetting the action counter.
     */
    public synchronized void reset() {
        stop();
        environment.clearEntities();
        actionCount.set(0);
        log("Simulation reset");
        notifyObservers();
    }

    /**
     * Stops the simulation for application shutdown, ensuring no threads remain.
     */
    public synchronized void shutdown() {
        stop();
        log("Simulation shut down");
    }

    private void spawnThread(AbstractEntity entity) {
        if (entity instanceof Actable && entity.isAlive()) {
            EntityThread t = new EntityThread(entity, (Actable) entity, environment, this);
            threads.add(t);
            t.start();
        }
    }

    /**
     * Called by an entity thread after the entity acts: advances the counter and refreshes
     * observers. The GUI is responsible for marshalling the update onto the event thread.
     */
    public void onEntityActed() {
        actionCount.incrementAndGet();
        notifyObservers();
    }

    /**
     * Called by an entity thread when it finishes; deregisters it and refreshes observers.
     * @param thread the thread that ended
     */
    public void onThreadEnded(EntityThread thread) {
        threads.remove(thread);
        notifyObservers();
    }

    /**
     * Logs an uncaught error from an entity thread.
     * @param entity the entity whose thread failed
     * @param error the error that occurred
     */
    public void logError(AbstractEntity entity, Throwable error) {
        System.err.println("[SIM ERROR] Thread for "
                + entity.getClass().getSimpleName() + " failed: " + error);
        error.printStackTrace();
    }

    /**
     * Lifecycle hook: when an entity is born at runtime (e.g. through reproduction) and the
     * simulation is running, give it its own thread.
     * @param entity the entity that was added
     */
    @Override
    public void onEntityAdded(AbstractEntity entity) {
        if (running.get()) {
            spawnThread(entity);
        }
    }

    /**
     * Lifecycle hook for removals. The entity's own thread ends itself, so no action is needed.
     * @param entity the entity that was removed
     */
    @Override
    public void onEntityRemoved(AbstractEntity entity) {
        
    }

    private void log(String message) {
        System.out.println("[SIM " + LocalTime.now().format(TIME_FORMAT) + "] " + message);
    }

    /**
     * Compares this simulation engine to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimulationEngine)) {
            return false;
        }
        SimulationEngine other = (SimulationEngine) o;
        return this.environment.equals(other.environment);
    }

    /**
     * Returns a string representation of the simulation engine.
     * @return the string representation
     */
    @Override
    public String toString() {
        return "SimulationEngine <environment=" + this.environment
                + ", running=" + running.get() + ">";
    }
}
