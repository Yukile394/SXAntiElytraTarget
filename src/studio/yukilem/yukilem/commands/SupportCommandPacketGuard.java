package studio.glace.glantielytratarget.commands;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommand;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommandUnsigned;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.commands.SupportCommand;
import studio.glace.glantielytratarget.platform.PlatformScheduler;
import studio.glace.glantielytratarget.platform.PlayerSessionCache;

public final class SupportCommandPacketGuard
extends PacketListenerAbstract {
    private static final Set<String> SUPPORT_LABELS = Set.of("elytratarget", "elytraac");
    private static final String[] NO_ARGS = new String[0];
    private final PluginContext ctx;
    private final Supplier<SupportCommand> support;

    public SupportCommandPacketGuard(@NotNull PluginContext pluginContext, @NotNull Supplier<SupportCommand> supplier) {
        super(PacketListenerPriority.LOWEST);
        this.ctx = pluginContext;
        this.support = supplier;
    }

    @Override
    public void onPacketReceive(@NotNull PacketReceiveEvent packetReceiveEvent) {
        Player player;
        Player player2;
        Player player3;
        PlayerSessionCache.Session session;
        PlayerSessionCache.Session session2;
        UUID uUID;
        UUID uUID2;
        String string;
        Set<String> set;
        String string2 = SupportCommandPacketGuard.labelOf(packetReceiveEvent);
        String string3 = string2;
        if (string3 == null || !(set = SUPPORT_LABELS).contains(string = string2)) {
            return;
        }
        if (this.ctx.config() == null || !this.ctx.config().isSupportCommandEnabled()) {
            return;
        }
        SupportCommand supportCommand = this.support.get();
        SupportCommand supportCommand2 = supportCommand;
        if (supportCommand2 == null) {
            return;
        }
        User user = packetReceiveEvent.getUser();
        User user2 = user;
        if (user2 != null) {
            User user3 = user;
            uUID2 = user3.getUUID();
        } else {
            uUID2 = null;
        }
        UUID uUID3 = uUID = uUID2;
        if (uUID3 == null) {
            return;
        }
        PlayerSessionCache playerSessionCache = this.ctx.sessionCache();
        PlayerSessionCache playerSessionCache2 = playerSessionCache;
        if (playerSessionCache2 != null) {
            PlayerSessionCache playerSessionCache3 = playerSessionCache;
            UUID uUID4 = uUID;
            session2 = playerSessionCache3.get(uUID4);
        } else {
            session2 = null;
        }
        PlayerSessionCache.Session session3 = session = session2;
        if (session3 != null) {
            PlayerSessionCache.Session session4 = session;
            player3 = session4.player();
        } else {
            player3 = null;
        }
        Player player4 = player2 = player3;
        if (player4 == null || !(player = player2).isOnline()) {
            return;
        }
        packetReceiveEvent.setCancelled(true);
        PlatformScheduler platformScheduler = this.ctx.scheduler();
        Player player5 = player2;
        SupportCommand supportCommand3 = supportCommand;
        Player player6 = player2;
        String string4 = string2;
        Runnable runnable = () -> {
            String[] stringArray = NO_ARGS;
            boolean bl = supportCommand3.execute(player6, string4, stringArray);
        };
        platformScheduler.runEntity(player5, runnable);
    }

    @Nullable
    private static String labelOf(@NotNull PacketReceiveEvent packetReceiveEvent) {
        int n;
        String string;
        String string2;
        String string3;
        String string4;
        PacketType.Play.Client client;
        PacketTypeCommon packetType = packetReceiveEvent.getPacketType();
        PacketTypeCommon packetTypeCommon = packetType;
        if (packetTypeCommon == (client = PacketType.Play.Client.CHAT_COMMAND)) {
            string4 = new WrapperPlayClientChatCommand(packetReceiveEvent).getCommand();
        } else {
            PacketTypeCommon packetTypeCommon2 = packetType;
            PacketType.Play.Client client2 = PacketType.Play.Client.CHAT_COMMAND_UNSIGNED;
            if (packetTypeCommon2 == client2) {
                string4 = new WrapperPlayClientChatCommandUnsigned(packetReceiveEvent).getCommand();
            } else {
                String string5;
                PacketTypeCommon packetTypeCommon3 = packetType;
                PacketType.Play.Client client3 = PacketType.Play.Client.CHAT_MESSAGE;
                if (packetTypeCommon3 == client3) {
                    String string6;
                    string5 = new WrapperPlayClientChatMessage(packetReceiveEvent).getMessage();
                    String string7 = string5;
                    if (string7 == null || !(string6 = string5).startsWith("/")) {
                        return null;
                    }
                } else {
                    return null;
                }
                String string8 = string5;
                string4 = string8.substring(1);
            }
        }
        String string9 = string4;
        if (string9 == null || (string3 = string4).isEmpty()) {
            return null;
        }
        String string10 = string4;
        int n2 = string10.indexOf(32);
        int n3 = n2;
        if (n3 >= 0) {
            String string11 = string4;
            int n4 = n2;
            string2 = string11.substring(0, n4);
        } else {
            string2 = string4;
        }
        String string12 = string = string2;
        int n5 = n = string12.indexOf(58);
        if (n5 >= 0) {
            String string13 = string;
            int n6 = n;
            int n7 = n6 + 1;
            string = string13.substring(n7);
        }
        String string14 = string;
        Locale locale = Locale.ROOT;
        return string14.toLowerCase(locale);
    }
}

