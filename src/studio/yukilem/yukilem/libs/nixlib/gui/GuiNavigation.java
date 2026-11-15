package studio.glace.glantielytratarget.libs.nixlib.gui;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class GuiNavigation {
    private static final Set<UUID> TRANSITIONING = ConcurrentHashMap.newKeySet();
    private static final Set<UUID> SUPPRESS_BACK = ConcurrentHashMap.newKeySet();

    private GuiNavigation() {
    }

    public static void beginTransition(UUID uUID) {
        if (uUID != null) {
            TRANSITIONING.add(uUID);
        }
    }

    public static void endTransition(UUID uUID) {
        if (uUID != null) {
            TRANSITIONING.remove(uUID);
        }
    }

    public static boolean isTransitioning(UUID uUID) {
        return uUID != null && TRANSITIONING.contains(uUID);
    }

    public static void suppressBack(UUID uUID) {
        if (uUID != null) {
            SUPPRESS_BACK.add(uUID);
        }
    }

    public static boolean consumeSuppressBack(UUID uUID) {
        return uUID != null && SUPPRESS_BACK.remove(uUID);
    }

    public static void clear(UUID uUID) {
        if (uUID != null) {
            TRANSITIONING.remove(uUID);
            SUPPRESS_BACK.remove(uUID);
        }
    }
}

