package entities.plants;

import java.util.ArrayList;
import java.util.List;
import core.Environment;
import core.Position;
import entities.LivingEntity;
import interfaces.Consumable;
import interfaces.EdibleByHerbivore;
import interfaces.Reproducible;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Abstract base class for all plant entities in the ecosystem.
 */
public abstract class Plant extends LivingEntity implements Consumable, Reproducible, EdibleByHerbivore {
    private double growthRate;
    private double reproductionChance;

    /**
     * Creates a new plant.
     * @param position the plant position
     * @param symbol the plant display symbol
     * @param energy the initial energy
     * @param maxEnergy the maximum energy
     * @param growthRate the growth rate per tick
     * @param reproductionChance the reproduction chance
     */
    public Plant(Position position, char symbol, double energy, double maxEnergy,
                 double growthRate, double reproductionChance) {
        super(position, symbol, energy, maxEnergy);
        this.growthRate = growthRate;
        this.reproductionChance = reproductionChance;
    }

    /**
     * Returns the growth rate.
     * @return the growth rate
     */
    public double getGrowthRate() {
        return this.growthRate;
    }

    /**
     * Returns the reproduction chance.
     * @return the reproduction chance
     */
    public double getReproductionChance() {
        return this.reproductionChance;
    }

    /**
     * Sets the growth rate if valid.
     * @param growthRate the new growth rate
     * @return true if update succeeded, false otherwise
     */
    protected boolean setGrowthRate(double growthRate) {
        if (growthRate < 0) {
            return false;
        }
        this.growthRate = growthRate;
        return true;
    }

    /**
     * Sets the reproduction chance if valid.
     * @param reproductionChance the new reproduction chance
     * @return true if update succeeded, false otherwise
     */
    protected boolean setReproductionChance(double reproductionChance) {
        if (reproductionChance < 0 || reproductionChance > 1) {
            return false;
        }
        this.reproductionChance = reproductionChance;
        return true;
    }

    /**
     * Performs one plant action in the current tick.
     * Plants age, gain energy by growth rate up to max energy, and then attempt reproduction.
     * Plants do not lose the per-tick energy that other living entities do.
     * @param env the simulation environment
     * @return true if the action completed, false otherwise
     */
    @Override
    public boolean act(Environment env) {
        if (env == null || !isAlive()) {
            return false;
        }

        if (!setAge(getAge() + 1)) {
            return false;
        }

        double newEnergy = getEnergy() + this.growthRate;
        if (newEnergy > getMaxEnergy()) {
            newEnergy = getMaxEnergy();
        }

        if (!setEnergy(newEnergy)) {
            return false;
        }

        return reproduce(env);
    }

    /**
     * Collects the free cells within the given Manhattan distance of this plant.
     * Helper used by subclasses to place offspring during reproduction.
     * @param env the simulation environment
     * @param maxDistance the maximum Manhattan distance from this plant
     * @return a list of free positions within the distance
     */
    protected List<Position> freeCellsWithin(Environment env, int maxDistance) {
        List<Position> cells = new ArrayList<>();
        for (int dr = -maxDistance; dr <= maxDistance; dr++) {
            for (int dc = -maxDistance; dc <= maxDistance; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                if (Math.abs(dr) + Math.abs(dc) > maxDistance) {
                    continue;
                }
                Position candidate = new Position(getPosition().getRow() + dr,
                                                  getPosition().getCol() + dc);
                if (env.isPositionFree(candidate)) {
                    cells.add(candidate);
                }
            }
        }
        return cells;
    }

    /**
     * Returns the nutritional value of the plant.
     * @return the plant energy
     */
    @Override
    public double getNutritionValue() {
        return getEnergy();
    }

    /**
     * Defines what happens when the plant is consumed.
     * Synchronized and one-shot: only the first caller succeeds, so two herbivores can
     * never both consume the same plant.
     * @return true if this call consumed the plant, false if it was already consumed
     */
    @Override
    public synchronized boolean onConsumed() {
        if (!isAlive()) {
            return false;
        }
        return setAlive(false);
    }

    /**
     * Compares this plant to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        if (!(o instanceof Plant)) {
            return false;
        }
        Plant other = (Plant) o;
        return Double.compare(this.growthRate, other.growthRate) == 0
                && Double.compare(this.reproductionChance, other.reproductionChance) == 0;
    }

    /**
     * Returns a string representation of the plant.
     * @return the string representation
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " "
                + getPosition() + " "
                + "<" + getEnergy() + "> "
                + "<" + isAlive() + ">";
    }
}
