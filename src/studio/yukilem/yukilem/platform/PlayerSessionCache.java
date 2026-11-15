package studio.yukilem.yukilem.platform;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.platform.BedrockPlatformProbe;

public final class PlayerSessionCache {
    private static final long PERMISSION_REFRESH_NANOS = 5000000000L;
    private final Map<UUID, Session> sessions;

    public PlayerSessionCache() {
        ConcurrentHashMap<UUID, Session> concurrentHashMap = new ConcurrentHashMap<UUID, Session>();
        this.sessions = concurrentHashMap;
    }

    public void onJoin(@NotNull Player player) {
        boolean bl;
        Session session;
        UUID uniqueId = player.getUniqueId();
        Map<UUID, Session> map = this.sessions;
        UUID uUID = uniqueId;
        Function<UUID, Session> function = Session::new;
        Session session2 = session = map.computeIfAbsent(uUID, function);
        session2.player = player;
        Session session3 = session;
        session3.bedrock = bl = BedrockPlatformProbe.isBedrockPlayer(player);
        Session session4 = session;
        long l = System.nanoTime();
        this.refreshPermission(session4, player, l);
    }

    public void onQuit(@NotNull UUID uUID) {
        Session session = this.sessions.remove(uUID);
    }

    public void clear() {
        this.sessions.clear();
    }

    @Nullable
    public Session resolve(@NotNull UUID uUID, @NotNull Player player, @NotNull User user) {
        long l;
        Session session = this.sessions.get(uUID);
        Session session2 = session;
        if (session2 == null) {
            boolean bl;
            Map<UUID, Session> map = this.sessions;
            Function<UUID, Session> function = Session::new;
            Session session3 = session = map.computeIfAbsent(uUID, function);
            session3.bedrock = bl = BedrockPlatformProbe.isBedrockPlayer(player);
        }
        Session session4 = session;
        session4.player = player;
        Session session5 = session;
        session5.user = user;
        long l2 = l = System.nanoTime();
        Session session6 = session;
        long l3 = session6.permissionRefreshAt;
        long l4 = l2 - l3;
        long l5 = 5000000000L;
        if (l4 >= l5) {
            Session session7 = session;
            long l6 = l;
            this.refreshPermission(session7, player, l6);
        }
        Session session8 = session;
        return session8;
    }

    @Nullable
    public Session get(@NotNull UUID uUID) {
        return this.sessions.get(uUID);
    }

    @Nullable
    public User userOf(@NotNull Player player) {
        Map<UUID, Session> map = this.sessions;
        UUID uniqueId = player.getUniqueId();
        Session session = map.get(uniqueId);
        Session session2 = session;
        if (session2 != null) {
            Session session3 = session;
            if (session3.user != null) {
                Session session4 = session;
                return session4.user;
            }
        }
        try {
            Session session5;
            User user;
            User user2 = user = PacketEvents.getAPI().getPlayerManager().getUser(player);
            if (user2 != null && (session5 = session) != null) {
                User user3;
                Session session6 = session;
                session6.user = user3 = user;
            }
            User user4 = user;
            return user4;
        }
        catch (Throwable e) {
            Throwable e2 = e;
            return null;
        }
    }

    private void refreshPermission(@NotNull Session session, @NotNull Player player, long l) {
        boolean bl;
        boolean bl2 = false;
        try {
            bl2 = player.hasPermission("glantielytratarget.bypass");
        }
        catch (Throwable e) {
            Throwable e2 = e;
        }
        session.bedrock = bl = BedrockPlatformProbe.isBedrockPlayer(player);
        boolean bl3 = bl2;
        boolean bl4 = bl3 || session.bedrock;
        session.bypass = bl4;
        session.permissionRefreshAt = l;
    }

    public static final class Session {
        private final UUID uuid;
        private volatile Player player;
        private volatile User user;
        private volatile boolean bedrock;
        private volatile boolean bypass;
        private volatile long permissionRefreshAt;

        Session(UUID uUID) {
            this.uuid = uUID;
        }

        @NotNull
        public UUID uuid() {
            return this.uuid;
        }

        @Nullable
        public Player player() {
            return this.player;
        }

        @Nullable
        public User user() {
            return this.user;
        }

        public boolean bedrock() {
            return this.bedrock;
        }

        public boolean bypass() {
            return this.bypass;
        }
    }
}

