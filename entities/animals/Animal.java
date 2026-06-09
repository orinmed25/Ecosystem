package entities.animals;

import java.util.List;
import behaviors.FeedingBehavior;
import behaviors.MovementStrategy;
import core.Environment;
import core.Position;
import entities.AbstractEntity;
import entities.LivingEntity;
import interfaces.Consumable;
import interfaces.EdibleByCarnivore;
import interfaces.Eater;
import interfaces.Movable;
import interfaces.Sensory;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Abstract base class for all animal entities in the ecosystem.
 */
public abstract class Animal extends LivingEntity
        implements Movable, Consumable, Eater, Sensory, EdibleByCarnivore {

    private int visionRange;
    private MovementStrategy movementStrategy;
    private FeedingBehavior feedingBehavior;

    /** Maximum time (ms) a hungry animal parks itself waiting for a resource to appear. */
    private static final long FOOD_WAIT_MS = 1000;

    /** True while the animal is parked waiting for food (read by other threads / the GUI). */
    private volatile boolean waiting = false;

    /**
     * Creates a new animal.
     *
     * @param position the animal position
     * @param symbol the animal display symbol
     * @param energy the initial energy
     * @param maxEnergy the maximum energy
     * @param movementStrategy the movement strategy
     * @param feedingBehavior the feeding behavior
     */
    public Animal(Position position, char symbol, double energy, double maxEnergy,
                  MovementStrategy movementStrategy, FeedingBehavior feedingBehavior) {
        super(position, symbol, energy, maxEnergy);
        this.visionRange = 2;
        this.movementStrategy = movementStrategy;
        this.feedingBehavior = feedingBehavior;
    }

    /**
     * Returns the vision range.
     * @return the vision range
     */
    public int getVisionRange() {
        return this.visionRange;
    }

    /**
     * Returns the movement strategy.
     * @return the movement strategy
     */
    public MovementStrategy getMovementStrategy() {
        return this.movementStrategy;
    }

    /**
     * Returns the feeding behavior.
     * @return the feeding behavior
     */
    public FeedingBehavior getFeedingBehavior() {
        return this.feedingBehavior;
    }

    /**
     * Sets the vision range if valid.
     * @param visionRange the new vision range
     * @return true if update succeeded, false otherwise
     */
    protected boolean setVisionRange(int visionRange) {
        if (visionRange < 0) {
            return false;
        }
        this.visionRange = visionRange;
        return true;
    }

    /**
     * Sets the movement strategy if valid.
     * @param movementStrategy the new movement strategy
     * @return true if update succeeded, false otherwise
     */
    protected boolean setMovementStrategy(MovementStrategy movementStrategy) {
        if (movementStrategy == null) {
            return false;
        }
        this.movementStrategy = movementStrategy;
        return true;
    }

    /**
     * Sets the feeding behavior if valid.
     * @param feedingBehavior the new feeding behavior
     * @return true if update succeeded, false otherwise
     */
    protected boolean setFeedingBehavior(FeedingBehavior feedingBehavior) {
        if (feedingBehavior == null) {
            return false;
        }
        this.feedingBehavior = feedingBehavior;
        return true;
    }

    /**
     * Performs one animal action in the current tick.
     * The action order is: basic update, sense, move, and attempt eating.
     * @param env the simulation environment
     * @return true if the action completed, false otherwise
     */
    @Override
    public boolean act(Environment env) {
        if (env == null || !isAlive()) {
            return false;
        }

        if (!super.act(env)) {
            return false;
        }

        if (!isAlive()) {
            return true;
        }

        List<AbstractEntity> nearby = sense(env);

        if (!move(env)) {
        }

        boolean ate = false;
        if (this.feedingBehavior != null) {
            ate = this.feedingBehavior.eat(this, nearby);
        }
        if (ate) {
            setWaiting(false);
        } else if (isHungry()) {
            setWaiting(true);
            env.awaitResource(FOOD_WAIT_MS);
        } else {
            setWaiting(false);
        }

        return true;
    }

    /**
     * Returns whether the animal is hungry (below half of its maximum energy).
     * @return true if hungry, false otherwise
     */
    public boolean isHungry() {
        return getEnergy() < getMaxEnergy() * 0.5;
    }

    /**
     * Returns whether the animal is currently in the waiting state (parked, no food found).
     * @return true if waiting, false otherwise
     */
    public boolean isWaiting() {
        return this.waiting;
    }

    /**
     * Updates the waiting state and logs the transition to the console so the wait state is
     * observable while the simulation runs. Only state changes are logged, to avoid spam.
     * @param value the new waiting state
     */
    private void setWaiting(boolean value) {
        if (value == this.waiting) {
            return;
        }
        this.waiting = value;
        if (value) {
            System.out.printf("[WAIT] %s at %s entered WAIT state (no food, energy=%.1f)%n",
                    getClass().getSimpleName(), getPosition(), getEnergy());
        } else {
            System.out.printf("[WAIT] %s at %s left WAIT state (energy=%.1f)%n",
                    getClass().getSimpleName(), getPosition(), getEnergy());
        }
    }

    /**
     * Attempts to move the animal using its movement strategy.
     * @param env the simulation environment
     * @return true if movement succeeded, false otherwise
     */
    @Override
    public boolean move(Environment env) {
        if (env == null || this.movementStrategy == null) {
            return false;
        }
        return this.movementStrategy.move(this, env);
    }

    /**
     * Attempts to eat a consumable target.
     * @param target the target to consume
     * @return true if eating succeeded, false otherwise
     */
    @Override
    public boolean eat(Consumable target) {
        if (target == null || !isAlive()) {
            return false;
        }

        double nutritionValue = target.getNutritionValue();
        if (!target.onConsumed()) {
            return false;
        }

        double updatedEnergy = getEnergy() + nutritionValue;
        if (updatedEnergy > getMaxEnergy()) {
            updatedEnergy = getMaxEnergy();
        }

        return setEnergy(updatedEnergy);
    }

    /**
     * Returns nearby entities from the environment.
     * @param env the simulation environment
     * @return a list of nearby entities
     */
    @Override
    public List<AbstractEntity> sense(Environment env) {
        return env.getNearbyEntities(getPosition());
    }

    /**
     * Returns the nutrition value of this animal.
     * @return 80 percent of the current energy
     */
    @Override
    public double getNutritionValue() {
        return getEnergy() * 0.8;
    }

    /**
     * Defines what happens when the animal is consumed.
     * Synchronized and one-shot: only the first caller succeeds, so two predators can
     * never both consume the same prey (prevents duplicated energy).
     * @return true if this call consumed the animal, false if it was already consumed
     */
    @Override
    public synchronized boolean onConsumed() {
        if (!isAlive()) {
            return false;
        }
        return setAlive(false);
    }

    /**
     * Compares this animal to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        if (!(o instanceof Animal)) {
            return false;
        }
        Animal other = (Animal) o;
        return this.visionRange == other.visionRange;
    }

    /**
     * Returns a string representation of the animal.
     * @return the string representation
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " "
                + getPosition() + " "
                + "<" + getEnergy() + "> "
                + "<" + isAlive() + ">";
    }
    /**
 * Moves the animal to a new position if valid.
 * @param newPosition the new position
 * @return true if update succeeded, false otherwise
 */
public boolean moveTo(Position newPosition) {
    return setPosition(newPosition);
}
}