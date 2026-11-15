package studio.yukilem.yukilem.listener;

import java.util.UUID;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.platform.PlatformScheduler;
import studio.yukilem.yukilem.platform.region.RegionProtectionPolicy;

public final class RegionProtectionListener implements Listener {
   private final PluginContext ctx;

   @EventHandler(priority = EventPriority.MONITOR)
   public void onJoin(@NotNull PlayerJoinEvent playerJoinEvent1) {
      Player player6 = playerJoinEvent1.getPlayer();
      Player player3 = player6;
      Player player4 = player6;
      Location location16 = player4.getLocation();
      this.refresh(player3, location16);
      if (this.ctx.scheduler() != null) {
         PlatformScheduler platformScheduler14 = this.ctx.scheduler();
         Player player15 = player6;
         Player player5 = player6;
         Runnable runnable18 = () -> {
            if (player5.isOnline()) {
               Location location7 = player5.getLocation();
               this.refresh(player5, location7);
            }
         };
         long l19 = 1L;
         platformScheduler14.runEntityLater(player15, runnable18, l19);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onMove(@NotNull PlayerMoveEvent playerMoveEvent1) {
      Player player5 = playerMoveEvent1.getPlayer();
      Player player9 = player5;
      if (player9.isGliding()) {
         Location location6 = playerMoveEvent1.getFrom();
         Location location7 = playerMoveEvent1.getTo();
         Location location15 = location7;
         if (location15 != null) {
            Location location16 = location6;
            Location location3 = location7;
            if (!sameBlock(location16, location3)) {
               Player player19 = player5;
               Location location4 = location7;
               this.refresh(player19, location4);
               return;
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onToggleGlide(@NotNull EntityToggleGlideEvent entityToggleGlideEvent1) {
      if (entityToggleGlideEvent1.isGliding()) {
         Entity entity6 = entityToggleGlideEvent1.getEntity();
         Entity entity10 = entity6;
         if (entity10 instanceof Player) {
            Entity entity12 = entity6;
            Player player5 = (Player)entity12;
            Player player3 = player5;
            Player player4 = player5;
            Location location15 = player4.getLocation();
            this.refresh(player3, location15);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onTeleport(@NotNull PlayerTeleportEvent playerTeleportEvent1) {
      Location location5 = playerTeleportEvent1.getTo();
      Location location7 = location5;
      if (location7 != null) {
         Player player9 = playerTeleportEvent1.getPlayer();
         Location location4 = location5;
         this.refresh(player9, location4);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   public void onRespawn(@NotNull PlayerRespawnEvent playerRespawnEvent1) {
      Player player5 = playerRespawnEvent1.getPlayer();
      Location location6 = playerRespawnEvent1.getRespawnLocation();
      this.refresh(player5, location6);
   }

   @EventHandler(priority = EventPriority.MONITOR)
   public void onWorldChange(@NotNull PlayerChangedWorldEvent playerChangedWorldEvent1) {
      Player player5 = playerChangedWorldEvent1.getPlayer();
      Location location7 = playerChangedWorldEvent1.getPlayer().getLocation();
      this.refresh(player5, location7);
   }

   private void refresh(@NotNull Player player1, @NotNull Location location2) {
      RegionProtectionPolicy regionProtectionPolicy7 = this.ctx.regionProtection();
      RegionProtectionPolicy regionProtectionPolicy10 = regionProtectionPolicy7;
      if (regionProtectionPolicy10 != null) {
         RegionProtectionPolicy regionProtectionPolicy11 = regionProtectionPolicy7;
         if (regionProtectionPolicy11.active()) {
            RegionProtectionPolicy regionProtectionPolicy13 = regionProtectionPolicy7;
            regionProtectionPolicy13.refreshPlayer(player1, location2);
         } else {
            RegionProtectionPolicy regionProtectionPolicy14 = regionProtectionPolicy7;
            UUID uuid16 = player1.getUniqueId();
            regionProtectionPolicy14.clearPlayer(uuid16);
         }
      }
   }

   private static boolean sameBlock(@NotNull Location location0, @NotNull Location location1) {
      World world5 = location0.getWorld();
      World world14 = location1.getWorld();
      if (world5 != world14) {
         return false;
      }

      int i8 = location0.getBlockX();
      int i16 = location1.getBlockX();
      if (i8 == i16) {
         int i10 = location0.getBlockY();
         int i18 = location1.getBlockY();
         if (i10 == i18) {
            int i12 = location0.getBlockZ();
            int i20 = location1.getBlockZ();
            if (i12 == i20) {
               return true;
            }
         }
      }

      return false;
   }

   @Generated
   public RegionProtectionListener(PluginContext pluginContext1) {
      this.ctx = pluginContext1;
   }
}
