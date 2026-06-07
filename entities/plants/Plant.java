package entities.plants;

import core.Environment;
import core.Position;
import entities.LivingEntity;
import interfaces.Consumable;
import interfaces.EdibleByHerbivore;
import interfaces.Reproducible;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Plant entity in the ecosystem - can be a generic plant or subclassed (Flower, OakTree).
 * Implements growth, energy management, and reproduction behaviors.
 */
public class Plant extends LivingEntity implements Consumable, Reproducible, EdibleByHerbivore {
    private double growthRate;
    private double reproductionChance;

    /**
     * Creates a new plant with default parameters.
     * @param position the plant position
     */
    public Plant(Position position) {
        this(position, 'P', 40, 40, 2.0, 0.3);
    }

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
     * Attempts to reproduce if conditions are met.
     * Plants reproduce when energy is high enough and random chance succeeds.
     * @param env the simulation environment
     * @return true if reproduction attempt was made
     */
    @Override
    public boolean reproduce(Environment env) {
        if (env == null || !isAlive()) {
            return false;
        }

       
        if (getEnergy() >= getMaxEnergy() * 0.8 && Math.random() < this.reproductionChance) {
            // Find a random free position
            Position newPosition = null;
            int attempts = 0;
            while (newPosition == null && attempts < 10) {
                int row = (int) (Math.random() * env.getRows());
                int col = (int) (Math.random() * env.getCols());
                Position candidate = new Position(row, col);
                if (env.isPositionFree(candidate)) {
                    newPosition = candidate;
                }
                attempts++;
            }

            
            if (newPosition != null) {
                Plant offspring = new Plant(newPosition, this.getSymbol(), getMaxEnergy() * 0.5, 
                                           getMaxEnergy() * 0.5, this.growthRate, this.reproductionChance);
                env.addEntity(offspring);
                
                
                setEnergy(getEnergy() * 0.7);
                return true;
            }
        }

        return false;
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
     * @return true if the update succeeded
     */
    @Override
    public boolean onConsumed() {
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

