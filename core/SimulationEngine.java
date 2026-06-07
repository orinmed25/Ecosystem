package core;

import entities.AbstractEntity;
import interfaces.Actable;
import interfaces.EcosystemObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Runs the ecosystem simulation and notifies registered observers after each tick.
 */
public class SimulationEngine {
    private Environment environment;
    private List<EcosystemObserver> observers = new ArrayList<>();
    private int tickCount = 0;

    /**
     * Creates a new simulation engine.
     * @param environment the simulation environment
     */
    public SimulationEngine(Environment environment) {
        this.environment = environment;
    }

    /**
     * Returns the environment.
     * @return the environment
     */
    public Environment getEnvironment() {
        return this.environment;
    }

    /**
     * Returns the number of ticks that have elapsed.
     * @return the tick count
     */
    public int getTickCount() {
        return tickCount;
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
        for (EcosystemObserver obs : new ArrayList<>(observers)) {
            obs.onWorldChanged(environment, tickCount);
        }
    }

    /**
     * Runs one simulation tick.
     * Uses a snapshot of the entity list to avoid ConcurrentModificationException
     * when entities reproduce or die during a tick.
     */
    public void tick() {
        if (this.environment == null) {
            return;
        }

        List<AbstractEntity> snapshot = new ArrayList<>(this.environment.getEntities());
        for (AbstractEntity entity : snapshot) {
            if (entity instanceof Actable && entity.isAlive()) {
                ((Actable) entity).act(this.environment);
            }
        }

        this.environment.removeDeadEntities();
        tickCount++;
        notifyObservers();
    }

    /**
     * Resets the simulation by removing all entities and resetting the tick counter.
     */
    public void reset() {
        this.environment.clearEntities();
        tickCount = 0;
        notifyObservers();
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
        return "SimulationEngine <environment=" + this.environment + ">";
    }
}
