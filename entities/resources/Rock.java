package entities.resources;

import core.Position;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Represents a rock resource that blocks movement.
 */
public class Rock extends Resource {
    private boolean blocksMovement;

    /**
     * Creates a new rock.
     * @param position the rock position
     */
    public Rock(Position position) {
        super(position, 'X');
        this.blocksMovement = true;
    }

    /**
     * Returns whether this rock blocks movement.
     * @return true if movement is blocked, false otherwise
     */
    public boolean isBlocksMovement() {
        return this.blocksMovement;
    }

    /**
     * Sets the blocksMovement value.
     * @param blocksMovement the new value
     * @return true if update succeeded
     */
    protected boolean setBlocksMovement(boolean blocksMovement) {
        this.blocksMovement = blocksMovement;
        return true;
    }

    /**
     * Compares this rock to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        if (!(o instanceof Rock)) {
            return false;
        }
        Rock other = (Rock) o;
        return this.blocksMovement == other.blocksMovement;
    }

    /**
     * Returns a string representation of the rock.
     * @return the string representation
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " "
                + getPosition() + " "
                + "<N/A> "
                + "<" + isAlive() + ">";
    }
}
