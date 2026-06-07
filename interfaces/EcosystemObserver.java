package interfaces;

import core.Environment;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Observer interface for the MVC pattern. Implemented by views that need to react to world changes.
 */
public interface EcosystemObserver {
    void onWorldChanged(Environment env, int tickCount);
}
