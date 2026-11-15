package studio.yukilem.yukilem.util.combat;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.util.combat.CombatSession;

public final class CombatSessionManager {
    private final Map<UUID, CombatSession> sessions;

    public CombatSessionManager() {
        ConcurrentHashMap<UUID, CombatSession> concurrentHashMap = new ConcurrentHashMap<UUID, CombatSession>();
        this.sessions = concurrentHashMap;
    }

    @NotNull
    public CombatSession getSession(@NotNull UUID uUID2) {
        Map<UUID, CombatSession> map = this.sessions;
        Function<UUID, CombatSession> function = uUID -> new CombatSession();
        return map.computeIfAbsent(uUID2, function);
    }

    @Nullable
    public CombatSession getSessionIfPresent(@NotNull UUID uUID) {
        return this.sessions.get(uUID);
    }

    public void seedPlayer(@NotNull UUID uUID2) {
        Map<UUID, CombatSession> map = this.sessions;
        Function<UUID, CombatSession> function = uUID -> new CombatSession();
        CombatSession combatSession = map.computeIfAbsent(uUID2, function);
    }

    public void removePlayer(@NotNull UUID uUID) {
        CombatSession combatSession = this.sessions.remove(uUID);
    }

    public void clearAll() {
        this.sessions.clear();
    }
}

