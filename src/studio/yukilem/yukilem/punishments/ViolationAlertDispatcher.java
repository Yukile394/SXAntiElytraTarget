package studio.yukilem.yukilem.punishments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.StyleSetter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.alerts.AlertService;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.libs.nixlib.utils.ColorUtils;
import studio.yukilem.yukilem.punishments.PlaceholderContext;

final class ViolationAlertDispatcher {
    private static final String ALERTS_PERMISSION = "glantielytratarget.alerts";
    private static final long ALERT_CACHE_TTL_MS = 3000L;
    private final PluginContext ctx;
    private volatile List<Player> cachedRecipients;
    private volatile long lastRecipientRefresh;
    private volatile String cachedTemplate;
    private volatile List<String> cachedHoverLines;

    ViolationAlertDispatcher(@NotNull PluginContext pluginContext) {
        List list = Collections.emptyList();
        this.cachedRecipients = list;
        this.ctx = pluginContext;
    }

    void clearCaches() {
        long l;
        this.cachedTemplate = null;
        this.cachedHoverLines = null;
        List list = Collections.emptyList();
        this.cachedRecipients = list;
        this.lastRecipientRefresh = l = 0L;
    }

    void dispatch(@NotNull Player player, @NotNull PlaceholderContext placeholderContext) {
        int n;
        int n2;
        int n3;
        int n4;
        String string;
        List<Player> list = this.recipients();
        List<Player> list2 = list;
        if (list2.isEmpty()) {
            return;
        }
        String string2 = this.template();
        String string3 = placeholderContext.player();
        String string4 = string2.replace("%player%", string3);
        String string5 = Integer.toString(placeholderContext.vl());
        String string6 = string4.replace("%vl%", string5);
        String string7 = Integer.toString(placeholderContext.ms());
        String string8 = string6.replace("%ms%", string7);
        String string9 = placeholderContext.checkName();
        String string10 = string8.replace("%check_name%", string9);
        Location location = player.getLocation();
        Location location2 = location;
        if (location2.getWorld() != null) {
            Location location3 = location;
            string = location3.getWorld().getName();
        } else {
            string = "unknown";
        }
        String string11 = string;
        Object[] objectArray = new Object[1];
        Location location4 = location;
        Double x = location4.getX();
        objectArray[0] = x;
        String string12 = String.format("%.1f", objectArray);
        Object[] objectArray2 = new Object[1];
        Location location5 = location;
        Double y = location5.getY();
        objectArray2[0] = y;
        String string13 = String.format("%.1f", objectArray2);
        Object[] objectArray3 = new Object[1];
        Location location6 = location;
        Double z = location6.getZ();
        objectArray3[0] = z;
        String string14 = String.format("%.1f", objectArray3);
        List<String> list3 = this.hoverLines();
        StringBuilder sb = new StringBuilder();
        int n5 = 0;
        List<String> list4 = list3;
        int size = list4.size();
        while ((n4 = ++n5) < (n3 = size)) {
            List<String> list5 = list3;
            int n6 = n5;
            String string15 = list5.get(n6);
            String string16 = string11;
            String string17 = string15.replace("%world%", string16);
            String string18 = string12;
            String string19 = string17.replace("%x%", string18);
            String string20 = string13;
            String string21 = string19.replace("%y%", string20);
            String string22 = string14;
            String string23 = string21.replace("%z%", string22);
            String string24 = placeholderContext.checkName();
            String string25 = string23.replace("%check_name%", string24);
            String string26 = Integer.toString(placeholderContext.vl());
            String string27 = string25.replace("%vl%", string26);
            String string28 = Integer.toString(placeholderContext.ms());
            String string29 = string27.replace("%ms%", string28);
            String string30 = placeholderContext.tps();
            String string31 = string29.replace("%tps%", string30);
            String string32 = placeholderContext.player();
            String string33 = string31.replace("%player%", string32);
            int n7 = n5;
            if (n7 > 0) {
                StringBuilder sb2 = sb;
                StringBuilder sb3 = sb2.append('\n');
            }
            StringBuilder sb4 = sb;
            String string34 = string33;
            StringBuilder sb5 = sb4.append(string34);
        }
        String string35 = string10;
        Component component = ColorUtils.translate(string35);
        StringBuilder sb6 = sb;
        Component component2 = ColorUtils.translate(sb6.toString());
        Component component3 = component;
        Component component4 = component2;
        HoverEvent<Component> hoverEvent = HoverEvent.showText(component4);
        StyleSetter styleSetter = component3.hoverEvent(hoverEvent);
        int n8 = 0;
        List<Player> list6 = list;
        int size2 = list6.size();
        while ((n2 = ++n8) < (n = size2)) {
            List<Player> list7 = list;
            int n9 = n8;
            Player player2 = list7.get(n9);
            Player player3 = player2;
            if (!player3.isOnline()) continue;
            Player player4 = player2;
            StyleSetter styleSetter2 = styleSetter;
            player4.sendMessage((Component)styleSetter2);
        }
    }

    @NotNull
    private List<Player> recipients() {
        long l;
        long l2;
        long l3 = System.currentTimeMillis();
        long l4 = l3;
        long l5 = l4 - (l2 = this.lastRecipientRefresh);
        if (l5 > (l = 3000L)) {
            long l6;
            Iterator<? extends Player> iterator;
            ArrayList<Player> arrayList = new ArrayList<Player>();
            Iterator<? extends Player> iterator2 = Bukkit.getOnlinePlayers().iterator();
            while ((iterator = iterator2).hasNext()) {
                Player player;
                UUID uUID;
                AlertService alertService;
                Iterator<? extends Player> iterator3 = iterator2;
                Player player2 = iterator3.next();
                Player player3 = player2;
                if (!player3.hasPermission(ALERTS_PERMISSION) || this.ctx.alerts() == null || !(alertService = this.ctx.alerts()).hasAlertsEnabled(uUID = (player = player2).getUniqueId())) continue;
                ArrayList<Player> arrayList2 = arrayList;
                Player player4 = player2;
                boolean bl = arrayList2.add(player4);
            }
            ArrayList<Player> arrayList3 = arrayList;
            this.cachedRecipients = arrayList3;
            this.lastRecipientRefresh = l6 = l3;
        }
        return this.cachedRecipients;
    }

    @NotNull
    private String template() {
        if (this.cachedTemplate == null) {
            String string;
            this.cachedTemplate = string = this.ctx.language().getRawMessage("alerts.message");
        }
        return this.cachedTemplate;
    }

    @NotNull
    private List<String> hoverLines() {
        if (this.cachedHoverLines == null) {
            List<String> rawMessageList = this.ctx.language().getRawMessageList("alerts.hover");
            this.cachedHoverLines = rawMessageList;
        }
        return this.cachedHoverLines;
    }
}

