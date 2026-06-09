package entities.animals;

import behaviors.HerbivoreBehavior;
import behaviors.RandomMovement;
import core.Environment;
import core.Position;
import interfaces.Reproducible;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Represents a rabbit in the ecosystem.
 */
public class Rabbit extends Animal implements Reproducible {

    /**
     * Creates a new rabbit.
     * @param position the rabbit position
     */
    public Rabbit(Position position) {
        super(position, 'R', 50, 50, new RandomMovement(), new HerbivoreBehavior());
    }

    /**
     * Performs one rabbit action in the current tick.
     * Rabbit reproduction is checked after the regular animal behavior.
     * @param env the simulation environment
     * @return true if the action completed, false otherwise
     */
    @Override
    public boolean act(Environment env) {
        if (!super.act(env)) {
            return false;
        }

        if (getEnergy() > 30 && Math.random() < 0.3) {
            reproduce(env);
        }

        return true;
    }

    /**
     * Attempts to reproduce a new rabbit in an adjacent free cell.
     * @param env the simulation environment
     * @return true if reproduction succeeded, false otherwise
     */
    @Override
    public boolean reproduce(Environment env) {
        if (env == null || !isAlive()) {
            return false;
        }

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        java.util.List<Position> free = new java.util.ArrayList<>();
        for (int i = 0; i < dr.length; i++) {
            Position candidate = new Position(getPosition().getRow() + dr[i],
                                              getPosition().getCol() + dc[i]);
            if (env.isPositionFree(candidate)) {
                free.add(candidate);
            }
        }

        if (free.isEmpty()) {
            return false;
        }

        Position spot = free.get((int) (Math.random() * free.size()));
        Rabbit offspring = new Rabbit(spot);
        if (!env.addEntity(offspring)) {
            return false;
        }

        double shared = getEnergy() / 2;
        offspring.setEnergy(shared);
        setEnergy(shared);
        return true;
    }

    /**
     * Compares this rabbit to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o) && (o instanceof Rabbit);
    }

    /**
     * Returns a string representation of the rabbit.
     * @return the string representation
     */
    @Override
    public String toString() {
        return super.toString();
    }
}