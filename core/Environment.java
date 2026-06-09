package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import entities.AbstractEntity;
import entities.animals.Animal;
import interfaces.EntityLifecycleListener;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Represents the ecosystem world and manages all entities in it.
 *
 * Thread-safety: the shared entity list is guarded by a single {@link ReentrantLock}.
 * The lock is held only for the duration of each short operation (a scan, an add, a move),
 * never across an entity's whole turn or while it sleeps, so entity threads run concurrently
 * and only briefly contend on the quick critical sections.
 */
public class Environment {
    private int rows;
    private int cols;
    private final List<AbstractEntity> entities;

    /** Guards every access to {@link #entities}. */
    private final ReentrantLock lock = new ReentrantLock();
    /** Condition signalled whenever a new entity (a potential resource) is added. */
    private final Condition resourceAvailable = lock.newCondition();
    /** Optional listener notified when entities are added/removed (the engine). */
    private volatile EntityLifecycleListener lifecycleListener;

    /**
     * Creates a new environment.
     * @param rows number of rows in the map
     * @param cols number of columns in the map
     */
    public Environment(int rows, int cols) {
        this.rows = Math.max(10, rows);
        this.cols = Math.max(10, cols);
        this.entities = new ArrayList<>();
    }

    /**
     * Returns the number of rows.
     * @return the number of rows
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * Returns the number of columns.
     * @return the number of columns
     */
    public int getCols() {
        return this.cols;
    }

    /**
     * Registers the listener notified on entity add/remove. Pass null to clear.
     * @param listener the lifecycle listener
     */
    public void setLifecycleListener(EntityLifecycleListener listener) {
        this.lifecycleListener = listener;
    }

    /**
     * Returns a snapshot copy of the current entities. The copy is safe to iterate
     * from any thread (including the GUI) while other threads modify the world.
     * @return a snapshot list of the entities
     */
    public List<AbstractEntity> getEntities() {
        lock.lock();
        try {
            return new ArrayList<>(this.entities);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Sets the number of rows if valid.
     * @param rows the new number of rows
     * @return true if update succeeded, false otherwise
     */
    protected boolean setRows(int rows) {
        if (rows < 10) {
            return false;
        }
        this.rows = rows;
        return true;
    }

    /**
     * Sets the number of columns if valid.
     * @param cols the new number of columns
     * @return true if update succeeded, false otherwise
     */
    protected boolean setCols(int cols) {
        if (cols < 10) {
            return false;
        }
        this.cols = cols;
        return true;
    }

    /**
     * Checks whether a position is free (inside the map and unoccupied), without locking.
     * Must be called while holding {@link #lock}.
     * @param next the position to check
     * @return true if the position is free, false otherwise
     */
    private boolean isFreeNoLock(Position next) {
        if (next == null) {
            return false;
        }
        if (next.getRow() < 0 || next.getRow() >= this.rows
                || next.getCol() < 0 || next.getCol() >= this.cols) {
            return false;
        }
        for (AbstractEntity entity : this.entities) {
            if (entity.getPosition().equals(next)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether a position is free.
     * A position is free if it is inside the map and has no entity occupying it.
     * @param next the position to check
     * @return true if the position is free, false otherwise
     */
    public boolean isPositionFree(Position next) {
        lock.lock();
        try {
            return isFreeNoLock(next);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Atomically moves the animal to the target cell if it is free.
     * The free-check and the move happen in one critical section, so two animals
     * can never end up on the same cell.
     * @param animal the animal to move
     * @param target the destination position
     * @return true if the animal was moved, false otherwise
     */
    public boolean tryMove(Animal animal, Position target) {
        if (animal == null) {
            return false;
        }
        lock.lock();
        try {
            if (!isFreeNoLock(target)) {
                return false;
            }
            return animal.moveTo(target);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns all entities in Manhattan distance less than or equal to 2 from the given position.
     * @param pos the center position
     * @return a list of nearby entities
     */
    public List<AbstractEntity> getNearbyEntities(Position pos) {
        List<AbstractEntity> nearby = new ArrayList<>();
        if (pos == null) {
            return nearby;
        }
        lock.lock();
        try {
            for (AbstractEntity entity : this.entities) {
                if (entity.getPosition().distanceTo(pos) <= 2) {
                    nearby.add(entity);
                }
            }
        } finally {
            lock.unlock();
        }
        return nearby;
    }

    /**
     * Adds an entity to the environment if its cell is free.
     * On success, wakes any threads waiting for a resource and notifies the lifecycle
     * listener (outside the lock, so thread creation never happens while locked).
     * @param entity the entity to add
     * @return true if the add succeeded, false otherwise
     */
    public boolean addEntity(AbstractEntity entity) {
        if (entity == null || entity.getPosition() == null) {
            return false;
        }
        lock.lock();
        try {
            if (!isFreeNoLock(entity.getPosition())) {
                return false;
            }
            this.entities.add(entity);
            resourceAvailable.signalAll();
        } finally {
            lock.unlock();
        }
        EntityLifecycleListener l = this.lifecycleListener;
        if (l != null) {
            l.onEntityAdded(entity);
        }
        return true;
    }

    /**
     * Removes an entity from the environment.
     * @param entity the entity to remove
     * @return true if removal succeeded, false otherwise
     */
    public boolean removeEntity(AbstractEntity entity) {
        if (entity == null) {
            return false;
        }
        boolean removed;
        lock.lock();
        try {
            removed = this.entities.remove(entity);
        } finally {
            lock.unlock();
        }
        EntityLifecycleListener l = this.lifecycleListener;
        if (removed && l != null) {
            l.onEntityRemoved(entity);
        }
        return removed;
    }

    /**
     * Removes all dead entities from the environment.
     * @return true if any entity was removed
     */
    public boolean removeDeadEntities() {
        List<AbstractEntity> removed = new ArrayList<>();
        lock.lock();
        try {
            Iterator<AbstractEntity> iterator = this.entities.iterator();
            while (iterator.hasNext()) {
                AbstractEntity entity = iterator.next();
                if (!entity.isAlive()) {
                    iterator.remove();
                    removed.add(entity);
                }
            }
        } finally {
            lock.unlock();
        }
        EntityLifecycleListener l = this.lifecycleListener;
        if (l != null) {
            for (AbstractEntity entity : removed) {
                l.onEntityRemoved(entity);
            }
        }
        return !removed.isEmpty();
    }

    /**
     * Causes the calling thread to wait until a new resource (entity) is added to the
     * world, or until the timeout elapses. Used by hungry animals to avoid busy-waiting.
     * The lock is released while waiting, so the rest of the simulation proceeds.
     * @param timeoutMillis the maximum time to wait, in milliseconds
     */
    public void awaitResource(long timeoutMillis) {
        if (!(Thread.currentThread() instanceof EntityThread)) {
            return;
        }
        lock.lock();
        try {
            resourceAvailable.await(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Wakes every thread currently waiting in {@link #awaitResource(long)}.
     * Used on shutdown/reset so waiting threads can exit promptly.
     */
    public void signalAllWaiters() {
        lock.lock();
        try {
            resourceAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the entity located at exactly the given position, or null if none.
     * @param pos the position to look up
     * @return the entity at that position, or null
     */
    public AbstractEntity getEntityAt(Position pos) {
        if (pos == null) {
            return null;
        }
        lock.lock();
        try {
            for (AbstractEntity entity : this.entities) {
                if (entity.getPosition().equals(pos)) {
                    return entity;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes all entities from the environment and wakes any waiting threads.
     */
    public void clearEntities() {
        lock.lock();
        try {
            this.entities.clear();
            resourceAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Prints the current map using entity symbols.
     * @return true if printing completed
     */
    public boolean printMap() {
        char[][] map = new char[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                map[i][j] = ' ';
            }
        }

        lock.lock();
        try {
            for (AbstractEntity entity : this.entities) {
                Position pos = entity.getPosition();
                if (pos.getRow() >= 0 && pos.getRow() < this.rows
                        && pos.getCol() >= 0 && pos.getCol() < this.cols) {
                    map[pos.getRow()][pos.getCol()] = entity.getSymbol();
                }
            }
        } finally {
            lock.unlock();
        }

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                System.out.print("[" + map[i][j] + "]");
            }
            System.out.println();
        }
        return true;
    }

    /**
     * Compares this environment to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Environment)) {
            return false;
        }
        Environment other = (Environment) o;
        return this.rows == other.rows
                && this.cols == other.cols
                && this.getEntities().equals(other.getEntities());
    }

    /**
     * Returns a string representation of the environment.
     * @return the string representation
     */
    @Override
    public String toString() {
        lock.lock();
        try {
            return "Environment <rows=" + this.rows + ", cols=" + this.cols
                    + ", entities=" + this.entities.size() + ">";
        } finally {
            lock.unlock();
        }
    }
}
