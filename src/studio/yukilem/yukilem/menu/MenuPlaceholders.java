package studio.yukilem.yukilem.menu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.checks.PlayerCheckData;
import studio.yukilem.yukilem.config.settings.MenuTimeSettings;
import studio.yukilem.yukilem.data.ViolationRecord;

final class MenuPlaceholders {
    private static final String EMPTY = "\u2014";

    private MenuPlaceholders() {
    }

    @NotNull
    static Map<String, String> profile(@NotNull PluginContext pluginContext, @NotNull UUID uUID, @NotNull String string, @NotNull List<ViolationRecord> list) {
        Player player;
        Player player2;
        HashMap<String, String> hashMap;
        HashMap<String, String> hashMap2 = hashMap = new HashMap<String, String>();
        String string2 = hashMap2.put("%player%", string);
        HashMap<String, String> hashMap3 = hashMap;
        String string3 = hashMap3.put("%target%", string);
        HashMap<String, String> hashMap4 = hashMap;
        String string4 = uUID.toString();
        String string5 = hashMap4.put("%uuid%", string4);
        int n = pluginContext.flagRepository() != null ? pluginContext.flagRepository().getTotalViolations(uUID) : 0;
        int n2 = n;
        HashMap<String, String> hashMap5 = hashMap;
        int n3 = n2;
        String string6 = String.valueOf(n3);
        String string7 = hashMap5.put("%total_violations%", string6);
        HashMap<String, String> hashMap6 = hashMap;
        String string8 = String.valueOf(list.size());
        String string9 = hashMap6.put("%violation_count%", string8);
        Player player3 = player2 = Bukkit.getPlayer(uUID);
        boolean bl = player3 != null && (player = player2).isOnline();
        boolean bl2 = bl;
        HashMap<String, String> hashMap7 = hashMap;
        boolean bl3 = bl2;
        String string10 = bl3 ? "true" : "false";
        String string11 = hashMap7.put("%online%", string10);
        HashMap<String, String> hashMap8 = hashMap;
        boolean bl4 = bl2;
        String string12 = bl4 ? pluginContext.language().getRawMessage("menu.status-online") : pluginContext.language().getRawMessage("menu.status-offline");
        String string13 = hashMap8.put("%status%", string12);
        boolean bl5 = bl2;
        if (bl5) {
            double d;
            PlayerCheckData playerCheckData;
            String string14;
            Player player4 = player2;
            Location location = player4.getLocation();
            HashMap<String, String> hashMap9 = hashMap;
            Location location2 = location;
            if (location2.getWorld() != null) {
                Location location3 = location;
                string14 = location3.getWorld().getName();
            } else {
                string14 = EMPTY;
            }
            String string15 = hashMap9.put("%world%", string14);
            HashMap<String, String> hashMap10 = hashMap;
            Location location4 = location;
            String string16 = MenuPlaceholders.oneDecimal(location4.getX());
            String string17 = hashMap10.put("%x%", string16);
            HashMap<String, String> hashMap11 = hashMap;
            Location location5 = location;
            String string18 = MenuPlaceholders.oneDecimal(location5.getY());
            String string19 = hashMap11.put("%y%", string18);
            HashMap<String, String> hashMap12 = hashMap;
            Location location6 = location;
            String string20 = MenuPlaceholders.oneDecimal(location6.getZ());
            String string21 = hashMap12.put("%z%", string20);
            HashMap<String, String> hashMap13 = hashMap;
            Player player5 = player2;
            String string22 = String.valueOf(MenuPlaceholders.resolvePing(pluginContext, player5));
            String string23 = hashMap13.put("%ping%", string22);
            PlayerCheckData playerCheckData2 = playerCheckData = PlayerCheckData.getIfPresent(uUID);
            if (playerCheckData2 == null) {
                d = 0.0;
            } else {
                PlayerCheckData playerCheckData3 = playerCheckData;
                d = playerCheckData3.movementUncertaintyState().getSmoothedBps();
            }
            double d2 = d;
            HashMap<String, String> hashMap14 = hashMap;
            double d3 = d2;
            String string24 = MenuPlaceholders.oneDecimal(d3);
            String string25 = hashMap14.put("%bps%", string24);
            HashMap<String, String> hashMap15 = hashMap;
            Player player6 = player2;
            String string26 = player6.getGameMode().name();
            String string27 = hashMap15.put("%gamemode%", string26);
        } else {
            HashMap<String, String> hashMap16 = hashMap;
            String string28 = hashMap16.put("%world%", EMPTY);
            HashMap<String, String> hashMap17 = hashMap;
            String string29 = hashMap17.put("%x%", EMPTY);
            HashMap<String, String> hashMap18 = hashMap;
            String string30 = hashMap18.put("%y%", EMPTY);
            HashMap<String, String> hashMap19 = hashMap;
            String string31 = hashMap19.put("%z%", EMPTY);
            HashMap<String, String> hashMap20 = hashMap;
            String string32 = hashMap20.put("%ping%", EMPTY);
            HashMap<String, String> hashMap21 = hashMap;
            String string33 = hashMap21.put("%bps%", EMPTY);
            HashMap<String, String> hashMap22 = hashMap;
            String string34 = hashMap22.put("%gamemode%", EMPTY);
        }
        HashMap<String, String> hashMap23 = hashMap;
        return hashMap23;
    }

    @NotNull
    static Map<String, String> violation(@NotNull PluginContext pluginContext, @NotNull ViolationRecord violationRecord, int n, int n2, @NotNull SimpleDateFormat simpleDateFormat) {
        HashMap<String, String> hashMap;
        HashMap<String, String> hashMap2 = hashMap = new HashMap<String, String>();
        String string = violationRecord.name();
        String string2 = hashMap2.put("%player%", string);
        HashMap<String, String> hashMap3 = hashMap;
        String string3 = String.valueOf(n);
        String string4 = hashMap3.put("%index%", string3);
        HashMap<String, String> hashMap4 = hashMap;
        String string5 = String.valueOf(n2);
        String string6 = hashMap4.put("%total%", string5);
        HashMap<String, String> hashMap5 = hashMap;
        String string7 = String.valueOf(violationRecord.seq());
        String string8 = hashMap5.put("%seq%", string7);
        HashMap<String, String> hashMap6 = hashMap;
        String string9 = violationRecord.checkName();
        String string10 = hashMap6.put("%check%", string9);
        HashMap<String, String> hashMap7 = hashMap;
        String string11 = violationRecord.checkName();
        String string12 = hashMap7.put("%check_name%", string11);
        HashMap<String, String> hashMap8 = hashMap;
        long l = violationRecord.createdAt();
        Date date = new Date(l);
        String string13 = simpleDateFormat.format(date);
        String string14 = hashMap8.put("%date%", string13);
        HashMap<String, String> hashMap9 = hashMap;
        MenuTimeSettings menuTime = pluginContext.config().getMenuTime();
        long l2 = System.currentTimeMillis();
        long l3 = violationRecord.createdAt();
        long l4 = l2 - l3;
        String string15 = menuTime.formatAgo(l4);
        String string16 = hashMap9.put("%ago%", string15);
        HashMap<String, String> hashMap10 = hashMap;
        String string17 = MenuPlaceholders.oneDecimal(violationRecord.bps());
        String string18 = hashMap10.put("%bps%", string17);
        HashMap<String, String> hashMap11 = hashMap;
        String string19 = violationRecord.clientVersion();
        String string20 = hashMap11.put("%client%", string19);
        HashMap<String, String> hashMap12 = hashMap;
        String string21 = violationRecord.clientVersion();
        String string22 = hashMap12.put("%client_version%", string21);
        HashMap<String, String> hashMap13 = hashMap;
        String string23 = String.valueOf(violationRecord.ping());
        String string24 = hashMap13.put("%ping%", string23);
        HashMap<String, String> hashMap14 = hashMap;
        String string25 = String.valueOf(violationRecord.ping());
        String string26 = hashMap14.put("%ms%", string25);
        HashMap<String, String> hashMap15 = hashMap;
        String string27 = violationRecord.tps();
        String string28 = hashMap15.put("%tps%", string27);
        HashMap<String, String> hashMap16 = hashMap;
        String string29 = violationRecord.world();
        String string30 = hashMap16.put("%world%", string29);
        HashMap<String, String> hashMap17 = hashMap;
        String string31 = MenuPlaceholders.oneDecimal(violationRecord.x());
        String string32 = hashMap17.put("%x%", string31);
        HashMap<String, String> hashMap18 = hashMap;
        String string33 = MenuPlaceholders.oneDecimal(violationRecord.y());
        String string34 = hashMap18.put("%y%", string33);
        HashMap<String, String> hashMap19 = hashMap;
        String string35 = MenuPlaceholders.oneDecimal(violationRecord.z());
        String string36 = hashMap19.put("%z%", string35);
        HashMap<String, String> hashMap20 = hashMap;
        String string37 = MenuPlaceholders.oneDecimal(violationRecord.yaw());
        String string38 = hashMap20.put("%yaw%", string37);
        HashMap<String, String> hashMap21 = hashMap;
        String string39 = MenuPlaceholders.oneDecimal(violationRecord.pitch());
        String string40 = hashMap21.put("%pitch%", string39);
        HashMap<String, String> hashMap22 = hashMap;
        String string41 = violationRecord.gamemode();
        String string42 = hashMap22.put("%gamemode%", string41);
        HashMap<String, String> hashMap23 = hashMap;
        boolean bl = violationRecord.gliding();
        String string43 = MenuPlaceholders.yesNo(pluginContext, bl);
        String string44 = hashMap23.put("%gliding%", string43);
        HashMap<String, String> hashMap24 = hashMap;
        boolean bl2 = violationRecord.onGround();
        String string45 = MenuPlaceholders.yesNo(pluginContext, bl2);
        String string46 = hashMap24.put("%on_ground%", string45);
        HashMap<String, String> hashMap25 = hashMap;
        boolean bl3 = violationRecord.sprinting();
        String string47 = MenuPlaceholders.yesNo(pluginContext, bl3);
        String string48 = hashMap25.put("%sprinting%", string47);
        HashMap<String, String> hashMap26 = hashMap;
        return hashMap26;
    }

    private static int resolvePing(@NotNull PluginContext pluginContext, @NotNull Player player) {
        if (pluginContext.healthMonitor() != null) {
            return pluginContext.healthMonitor().getPlayerPing(player);
        }
        try {
            int ping = player.getPing();
            return ping;
        }
        catch (Throwable e) {
            Throwable e2 = e;
            return 0;
        }
    }

    @NotNull
    private static String oneDecimal(double d) {
        Locale locale = Locale.US;
        Object[] objectArray = new Object[1];
        Double d2 = d;
        objectArray[0] = d2;
        return String.format(locale, "%.1f", objectArray);
    }

    @NotNull
    private static String yesNo(@NotNull PluginContext pluginContext, boolean bl) {
        String string = bl ? pluginContext.language().getRawMessage("menu.yes") : pluginContext.language().getRawMessage("menu.no");
        return string;
    }
}

