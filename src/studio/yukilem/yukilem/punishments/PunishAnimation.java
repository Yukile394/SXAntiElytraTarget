package studio.yukilem.yukilem.punishments;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.config.ConfigurationService;
import studio.yukilem.yukilem.libs.nixlib.scheduler.ScheduledTask;

/**
 * Ban/kick oncesi animasyon:
 * - Oyuncu hareket edemez ve hasar almaz (zombi/oyuncu vuramaz)
 * - Gardiyan (elder guardian) efekti
 * - Lamalar yavasca doner ve oyuncuya tukurur
 * - Son fazda lamalar kuculur (baby), renk degistirir
 * - Sure bitince asil ban/kick komutu calisir
 */
public final class PunishAnimation {
    private static final Set<UUID> RUNNING = ConcurrentHashMap.newKeySet();
    private static final Set<Llama> ALL_LLAMAS = ConcurrentHashMap.newKeySet();
    private static final Map<UUID, Boolean> ORIGINAL_INVULNERABLE = new ConcurrentHashMap<>();
    private static final AtomicBoolean LISTENER_REGISTERED = new AtomicBoolean(false);
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder().character('&').hexColors().build();
    private static final List<String> DEFAULT_COMMANDS = List.of("ban", "tempban", "ban-ip", "banip", "kick");

    private PunishAnimation() {
    }

    public static boolean isRunning(@NotNull UUID uuid) {
        return RUNNING.contains(uuid);
    }

    /** Verilen konsol komutu icin animasyon oynatilmali mi? */
    public static boolean shouldAnimate(@NotNull PluginContext ctx, @NotNull String command) {
        ConfigurationService cfg = ctx.config();
        if (!cfg.getBoolean("punish-animation.enabled", true)) {
            return false;
        }
        String name = commandName(command);
        List<String> configured = cfg.getStringList("punish-animation.commands");
        List<String> list = configured.isEmpty() ? DEFAULT_COMMANDS : configured;
        for (String s : list) {
            if (s != null && s.trim().toLowerCase(Locale.ROOT).equals(name)) {
                return true;
            }
        }
        return false;
    }

    private static String commandName(String command) {
        String name = command.trim().toLowerCase(Locale.ROOT).split("[ \t]+", 2)[0];
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        int colon = name.indexOf(':');
        if (colon >= 0) {
            name = name.substring(colon + 1);
        }
        return name;
    }

    /**
     * Animasyonu baslatir. Ayni oyuncu icin zaten calisiyorsa false doner.
     * Hangi thread'den cagrilirsa cagrilsin guvenlidir (Folia dahil).
     * onFinish animasyon bittiginde (veya oyuncu cikarsa) tam bir kez calisir.
     */
    public static boolean begin(@NotNull PluginContext ctx, @NotNull Player player, @NotNull PlaceholderContext ph, @NotNull Runnable onFinish) {
        UUID uuid = player.getUniqueId();
        if (!RUNNING.add(uuid)) {
            return false;
        }
        if (LISTENER_REGISTERED.compareAndSet(false, true)) {
            Bukkit.getPluginManager().registerEvents(new Guard(), ctx.plugin());
        }
        ConfigurationService cfg = ctx.config();
        int count = clamp(cfg.getInt("punish-animation.llama-count", 8), 3, 16);
        double radius = Math.max(1.5, cfg.getDouble("punish-animation.radius", 3.0));
        int spawnInterval = clamp(cfg.getInt("punish-animation.spawn-interval-ticks", 6), 1, 40);
        double speed = Math.max(0.005, cfg.getDouble("punish-animation.rotation-speed", 0.04));
        int babyTicks = clamp(cfg.getInt("punish-animation.baby-phase-ticks", 40), 10, 400);
        int minTicks = count * spawnInterval + 20 + babyTicks;
        int totalTicks = Math.max(minTicks, cfg.getInt("punish-animation.duration-ticks", 140));
        int babyStart = totalTicks - babyTicks;
        boolean sound = cfg.getBoolean("punish-animation.sound", true);
        String titleRaw = cfg.getString("punish-animation.title", "&c&lBomm");
        String subtitleRaw = cfg.getString("punish-animation.subtitle", "&7Target kullanırsan patlatırlar");
        boolean actionBar = cfg.getBoolean("punish-animation.actionbar", true);
        String actionBarRaw = cfg.getString("punish-animation.actionbar-text", "&cCeza yukleniyor... &f%bar%");

        AtomicBoolean done = new AtomicBoolean(false);
        List<Llama> llamas = new ArrayList<>(count);
        ScheduledTask[] timer = new ScheduledTask[1];
        int[] tick = {0};
        boolean[] baby = {false};
        Llama.Color[] colors = Llama.Color.values();

        Runnable finish = () -> {
            if (!done.compareAndSet(false, true)) {
                return;
            }
            try {
                if (timer[0] != null) {
                    timer[0].cancel();
                }
            } catch (Throwable ignored) {
            }
            for (Llama llama : new ArrayList<>(llamas)) {
                removeLlama(ctx, llama);
            }
            llamas.clear();
            RUNNING.remove(uuid);
            try {
                ctx.scheduler().runEntity(player, () -> {
                    restoreInvulnerable(player);
                    if (player.isOnline()) {
                        player.clearTitle();
                    }
                });
            } catch (Throwable ignored) {
            }
            try {
                onFinish.run();
            } catch (Throwable t) {
                ctx.plugin().getLogger().warning("Punish animation finish failed: " + t.getMessage());
            }
        };

        // Guvenlik agi: oyuncu varligi yok olsa bile (cikis vb.) ceza mutlaka uygulanir.
        ctx.scheduler().runGlobalLater(finish, totalTicks + 60L);

        ctx.scheduler().runEntity(player, () -> {
            try {
                // Hareketsiz + dokunulmaz
                ORIGINAL_INVULNERABLE.putIfAbsent(uuid, player.isInvulnerable());
                player.setInvulnerable(true);
                player.setGliding(false);
                player.setSprinting(false);
                player.setVelocity(new Vector(0, 0, 0));
            } catch (Throwable t) {
                ctx.plugin().getLogger().warning("Punish animation freeze failed: " + t.getMessage());
            }
            try {
                Component title = LEGACY.deserialize(ph.render(titleRaw));
                Component subtitle = LEGACY.deserialize(ph.render(subtitleRaw));
                Title.Times times = Title.Times.times(Duration.ofMillis(300), Duration.ofMillis(Math.max(500L, totalTicks * 50L)), Duration.ofMillis(200));
                player.showTitle(Title.title(title, subtitle, times));
            } catch (Throwable t) {
                ctx.plugin().getLogger().warning("Punish animation title failed: " + t.getMessage());
            }

            timer[0] = ctx.scheduler().scheduler().runForTimer(player, task -> {
                if (done.get()) {
                    task.cancel();
                    return;
                }
                if (!player.isOnline()) {
                    task.cancel();
                    finish.run();
                    return;
                }
                int t = tick[0]++;
                Location center = player.getLocation();

                // Gardiyan (elder guardian) efekti
                if (t % 50 == 0) {
                    try {
                        player.spawnParticle(Particle.ELDER_GUARDIAN, center, 1);
                        if (sound) {
                            player.playSound(center, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.0f);
                        }
                    } catch (Throwable ignored) {
                    }
                }

                // Lamalari teker teker, yavasca olustur
                if (llamas.size() < count && t % spawnInterval == 0) {
                    int index = llamas.size();
                    double angle = slotAngle(index, count) + t * speed;
                    Location spawnLoc = ringLocation(center, radius, angle);
                    try {
                        Llama llama = center.getWorld().spawn(spawnLoc, Llama.class, l -> {
                            l.setAI(false);
                            l.setGravity(false);
                            l.setInvulnerable(true);
                            l.setSilent(true);
                            l.setPersistent(false);
                            l.setCollidable(false);
                            l.setCanPickupItems(false);
                            l.setRemoveWhenFarAway(true);
                            l.setCustomNameVisible(false);
                            l.setAdult();
                            l.setColor(colors[(int) (Math.random() * colors.length)]);
                        });
                        llamas.add(llama);
                        ALL_LLAMAS.add(llama);
                        center.getWorld().spawnParticle(Particle.CLOUD, spawnLoc.clone().add(0, 1, 0), 12, 0.3, 0.5, 0.3, 0.02);
                        if (sound) {
                            player.playSound(center, Sound.ENTITY_LLAMA_AMBIENT, 0.6f, 0.9f + index * 0.03f);
                        }
                    } catch (Throwable ex) {
                        ctx.plugin().getLogger().warning("Punish animation llama spawn failed: " + ex.getMessage());
                    }
                }

                // Son faz: lamalar kuculur
                if (t >= babyStart && !baby[0]) {
                    baby[0] = true;
                    for (Llama llama : llamas) {
                        try {
                            if (llama.isValid()) {
                                llama.setBaby();
                                llama.setAgeLock(true);
                                llama.getWorld().spawnParticle(Particle.POOF, llama.getLocation().add(0, 0.8, 0), 10, 0.2, 0.3, 0.2, 0.02);
                            }
                        } catch (Throwable ignored) {
                        }
                    }
                    if (sound) {
                        player.playSound(center, Sound.ENTITY_LLAMA_ANGRY, 0.8f, 1.6f);
                    }
                }

                // Lamalari dondur, oyuncuya baktir, tukurt / renk degistir
                for (int i = 0; i < llamas.size(); i++) {
                    Llama llama = llamas.get(i);
                    if (llama == null || !llama.isValid()) {
                        continue;
                    }
                    double angle = slotAngle(i, count) + t * speed;
                    Location target = ringLocation(center, radius, angle);
                    target.setYaw((float) Math.toDegrees(Math.atan2(Math.cos(angle), -Math.sin(angle))));
                    target.setPitch(0f);
                    try {
                        llama.teleportAsync(target);
                    } catch (Throwable ignored) {
                    }

                    try {
                        if (!baby[0]) {
                            if ((t + i * 3) % 24 == 0) {
                                Location from = llama.getLocation().add(0, 1.4, 0);
                                Vector dir = player.getEyeLocation().toVector().subtract(from.toVector());
                                if (dir.lengthSquared() > 0.0001) {
                                    Vector velocity = dir.normalize().multiply(0.8);
                                    llama.getWorld().spawn(from, LlamaSpit.class, s -> {
                                        s.setShooter(llama);
                                        s.setVelocity(velocity);
                                    });
                                    if (sound) {
                                        player.playSound(from, Sound.ENTITY_LLAMA_SPIT, 0.5f, 1.0f);
                                    }
                                }
                            }
                        } else if (t % 3 == 0) {
                            llama.setColor(colors[(t / 3 + i) % colors.length]);
                        }
                    } catch (Throwable ignored) {
                    }
                }

                if (actionBar && t % 5 == 0) {
                    int filled = Math.min(10, (int) Math.round(10.0 * t / totalTicks));
                    String bar = "&a" + "|".repeat(filled) + "&8" + "|".repeat(10 - filled);
                    try {
                        player.sendActionBar(LEGACY.deserialize(ph.render(actionBarRaw).replace("%bar%", bar)));
                    } catch (Throwable ignored) {
                    }
                }

                if (t >= totalTicks) {
                    task.cancel();
                    finish.run();
                }
            }, 1L, 1L);
        });
        return true;
    }

    /** Plugin kapanirken tum lamalari temizler ve oyunculari eski haline dondurur. */
    public static void cleanupAll() {
        for (Llama llama : new ArrayList<>(ALL_LLAMAS)) {
            try {
                llama.remove();
            } catch (Throwable ignored) {
            }
        }
        ALL_LLAMAS.clear();
        for (UUID id : new ArrayList<>(ORIGINAL_INVULNERABLE.keySet())) {
            try {
                Player p = Bukkit.getPlayer(id);
                if (p != null) {
                    restoreInvulnerable(p);
                }
            } catch (Throwable ignored) {
            }
        }
        ORIGINAL_INVULNERABLE.clear();
        RUNNING.clear();
    }

    private static void restoreInvulnerable(Player player) {
        Boolean original = ORIGINAL_INVULNERABLE.remove(player.getUniqueId());
        if (original != null) {
            try {
                player.setInvulnerable(original);
            } catch (Throwable ignored) {
            }
        }
    }

    private static void removeLlama(PluginContext ctx, Llama llama) {
        ALL_LLAMAS.remove(llama);
        try {
            ctx.scheduler().runEntity(llama, () -> {
                if (llama.isValid()) {
                    llama.getWorld().spawnParticle(Particle.CLOUD, llama.getLocation().add(0, 1, 0), 8, 0.3, 0.5, 0.3, 0.02);
                }
                llama.remove();
            });
        } catch (Throwable t) {
            try {
                llama.remove();
            } catch (Throwable ignored) {
            }
        }
    }

    private static double slotAngle(int index, int count) {
        return (2.0 * Math.PI / count) * index;
    }

    private static Location ringLocation(Location center, double radius, double angle) {
        return new Location(center.getWorld(), center.getX() + Math.cos(angle) * radius, center.getY(), center.getZ() + Math.sin(angle) * radius);
    }

    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    /** Animasyon sirasinda oyuncuyu dondurur ve hasardan korur. */
    private static final class Guard implements Listener {
        @EventHandler(priority = EventPriority.HIGHEST)
        public void onMove(PlayerMoveEvent e) {
            if (!RUNNING.contains(e.getPlayer().getUniqueId())) {
                return;
            }
            Location from = e.getFrom();
            Location to = e.getTo();
            if (to == null) {
                return;
            }
            if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
                Location fixed = from.clone();
                fixed.setYaw(to.getYaw());
                fixed.setPitch(to.getPitch());
                e.setTo(fixed);
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onDamage(EntityDamageEvent e) {
            Entity entity = e.getEntity();
            if (entity instanceof Player && RUNNING.contains(entity.getUniqueId())) {
                e.setCancelled(true);
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onTarget(EntityTargetLivingEntityEvent e) {
            if (e.getTarget() instanceof Player && RUNNING.contains(e.getTarget().getUniqueId())) {
                e.setCancelled(true);
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onGlide(EntityToggleGlideEvent e) {
            if (e.getEntity() instanceof Player && e.isGliding() && RUNNING.contains(e.getEntity().getUniqueId())) {
                e.setCancelled(true);
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onTeleport(PlayerTeleportEvent e) {
            if (!RUNNING.contains(e.getPlayer().getUniqueId())) {
                return;
            }
            PlayerTeleportEvent.TeleportCause cause = e.getCause();
            if (cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL || cause == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) {
                e.setCancelled(true);
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onQuit(PlayerQuitEvent e) {
            if (ORIGINAL_INVULNERABLE.containsKey(e.getPlayer().getUniqueId())) {
                restoreInvulnerable(e.getPlayer());
            }
        }
    }
}
