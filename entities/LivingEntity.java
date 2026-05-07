package entities;

import core.Environment;
import core.Position;
import interfaces.Actable;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Abstract base class for all living entities in the ecosystem.
 */
public abstract class LivingEntity extends AbstractEntity implements Actable {
    private int age;
    private double energy;
    private double maxEnergy;

    /**
     * Creates a new living entity.
     * @param position the entity position
     * @param symbol the entity display symbol
     * @param energy the initial energy
     * @param maxEnergy the maximum energy
     */
    public LivingEntity(Position position, char symbol, double energy, double maxEnergy) {
        super(position, symbol);
        this.age = 0;
        this.energy = energy;
        this.maxEnergy = maxEnergy;
    }

    /**
     * Returns the age.
     * @return the age
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Returns the current energy.
     * @return the energy
     */
    public double getEnergy() {
        return this.energy;
    }

    /**
     * Returns the maximum energy.
     * @return the maximum energy
     */
    public double getMaxEnergy() {
        return this.maxEnergy;
    }

    /**
     * Sets the age if valid.
     * @param age the new age
     * @return true if update succeeded, false otherwise
     */
    protected boolean setAge(int age) {
        if (age < 0) {
            return false;
        }
        this.age = age;
        return true;
    }

    /**
     * Sets the energy if valid.
     * @param energy the new energy
     * @return true if update succeeded, false otherwise
     */
    protected boolean setEnergy(double energy) {
        if (energy < 0 || energy > this.maxEnergy) {
            return false;
        }
        this.energy = energy;
        return true;
    }

    /**
     * Sets the maximum energy if valid.
     * @param maxEnergy the new maximum energy
     * @return true if update succeeded, false otherwise
     */
    protected boolean setMaxEnergy(double maxEnergy) {
        if (maxEnergy <= 0) {
            return false;
        }
        this.maxEnergy = maxEnergy;
        if (this.energy > this.maxEnergy) {
            this.energy = this.maxEnergy;
        }
        return true;
    }

    /**
     * Performs the default living-entity action for one tick.
     * Increases age, decreases energy by 2, and kills the entity if energy is 0 or less.
     * @param env the simulation environment
     * @return true if the action completed
     */
    @Override
    public boolean act(Environment env) {
        this.age++;
        this.energy -= 2;

        if (this.energy <= 0) {
            this.energy = 0;
            setAlive(false);
        }

        return true;
    }

    /**
     * Compares this living entity to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        if (!(o instanceof LivingEntity)) {
            return false;
        }
        LivingEntity other = (LivingEntity) o;
        return this.age == other.age
                && Double.compare(this.energy, other.energy) == 0
                && Double.compare(this.maxEnergy, other.maxEnergy) == 0;
    }

    /**
     * Returns a string representation of the living entity.
     * @return the string representation
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " "
                + getPosition() + " "
                + "<" + this.energy + "> "
                + "<" + isAlive() + ">";
    }
}