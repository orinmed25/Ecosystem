package core;

import entities.AbstractEntity;
import entities.animals.Animal;
import entities.plants.Plant;
import interfaces.Actable;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Runs the ecosystem simulation.
 */
public class SimulationEngine {
    private Environment environment;

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
     * Runs one simulation tick.
     * Activates all actable entities, removes dead entities, updates the world and prints the map and stats.
     */
    public void tick() {
        if (this.environment == null) {
            return;
        }

        for (AbstractEntity entity : this.environment.getEntities()) {
            if (entity instanceof Actable) {
                ((Actable) entity).act(this.environment);
            }
        }

        this.environment.removeDeadEntities();
        this.environment.printMap();
        printStatistics();
    }

    /**
     * Prints basic simulation statistics.
     */
    private void printStatistics() {
        int aliveCount = 0;
        int animalCount = 0;
        int plantCount = 0;

        for (AbstractEntity entity : this.environment.getEntities()) {
            if (entity.isAlive()) {
                aliveCount++;
            }
            if (entity instanceof Animal) {
                animalCount++;
            }
            if (entity instanceof Plant) {
                plantCount++;
            }
        }

        System.out.println("Alive entities: " + aliveCount);
        System.out.println("Animals: " + animalCount);
        System.out.println("Plants: " + plantCount);
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