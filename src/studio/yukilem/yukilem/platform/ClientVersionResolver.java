package studio.glace.glantielytratarget.platform;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ClientVersionResolver {
    private static final boolean VIA_PRESENT;

    private ClientVersionResolver() {
    }

    @NotNull
    public static ClientVersion resolve(@NotNull User user, @Nullable UUID uUID) {
        ClientVersion clientVersion = ClientVersionResolver.resolveKnown(user, uUID);
        ClientVersion clientVersion2 = clientVersion;
        if (clientVersion2 != null) {
            ClientVersion clientVersion3 = clientVersion;
            return clientVersion3;
        }
        return ClientVersion.getById(PacketEvents.getAPI().getServerManager().getVersion().getProtocolVersion());
    }

    @Nullable
    public static ClientVersion resolveKnown(@NotNull User user, @Nullable UUID uUID) {
        ClientVersion clientVersion;
        block8: {
            ProtocolVersion protocolVersion;
            int n;
            ClientVersion clientVersion2;
            int n2;
            ClientVersion clientVersion3;
            ClientVersion clientVersion4;
            ClientVersion clientVersion5;
            ProtocolVersion protocolVersion2;
            int n3;
            ClientVersion clientVersion6;
            int n4;
            ProtocolVersion protocolVersion3;
            block7: {
                if (VIA_PRESENT) {
                    if (uUID == null) {
                        return null;
                    }
                } else {
                    ClientVersion clientVersion7;
                    ClientVersion clientVersion8;
                    ClientVersion clientVersion9 = user.getClientVersion();
                    ClientVersion clientVersion10 = clientVersion9;
                    ClientVersion clientVersion11 = clientVersion10 != null && (clientVersion8 = clientVersion9) != (clientVersion7 = ClientVersion.UNKNOWN) ? clientVersion9 : null;
                    return clientVersion11;
                }
                try {
                    ProtocolVersion protocolVersion4;
                    ProtocolVersion protocolVersion5 = protocolVersion3 = Via.getAPI().getPlayerProtocolVersion(uUID);
                    if (protocolVersion5 != null && (protocolVersion4 = protocolVersion3).isKnown()) break block7;
                    return null;
                }
                catch (Throwable e) {
                    Throwable e2 = e;
                    return null;
                }
            }
            ProtocolVersion protocolVersion6 = protocolVersion3;
            clientVersion = ClientVersion.getById(protocolVersion6.getVersion());
            ClientVersion clientVersion12 = user.getClientVersion();
            ClientVersion clientVersion13 = clientVersion;
            ClientVersion clientVersion14 = ClientVersion.UNKNOWN;
            if (clientVersion13 != clientVersion14 && (n4 = (clientVersion6 = clientVersion).getProtocolVersion()) == (n3 = (protocolVersion2 = protocolVersion3).getVersion()) && (clientVersion5 = clientVersion12) != null && (clientVersion4 = clientVersion12) != (clientVersion3 = ClientVersion.UNKNOWN) && (n2 = (clientVersion2 = clientVersion12).getProtocolVersion()) == (n = (protocolVersion = protocolVersion3).getVersion())) break block8;
            return null;
        }
        ClientVersion clientVersion15 = clientVersion;
        return clientVersion15;
    }

    static {
        boolean bl = Bukkit.getPluginManager().getPlugin("ViaVersion") != null;
        VIA_PRESENT = bl;
    }
}

