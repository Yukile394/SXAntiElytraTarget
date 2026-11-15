package studio.glace.glantielytratarget.platform.region;

import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.checks.CheckId;

public final class RegionProtection {
   private RegionProtection() {
   }

   @NotNull
   public static RegionProtectionPolicy create(@NotNull PluginContext pluginContext0) {
      Plugin plugin5 = pluginContext0.plugin().getServer().getPluginManager().getPlugin("WorldGuard");
      Plugin plugin11 = plugin5;
      if (plugin11 != null) {
         Plugin plugin12 = plugin5;
         if (plugin12.isEnabled()) {
            try {
               return new WorldGuardRegionPolicy(pluginContext0);
            } catch (Throwable throwable32) {
               Logger logger25 = pluginContext0.plugin().getLogger();
               String string31 = "WorldGuard integration could not start: " + throwable32.getMessage();
               logger25.warning(string31);
               return RegionProtection.DisabledPolicy.INSTANCE;
            }
         }
      }

      if (pluginContext0.config().getBoolean("detection.worldguard.enabled", true)) {
         pluginContext0.plugin().getLogger().info("WorldGuard integration is enabled but WorldGuard is not installed.");
      }

      return RegionProtection.DisabledPolicy.INSTANCE;
   }

   private enum DisabledPolicy implements RegionProtectionPolicy {
      INSTANCE;

      DisabledPolicy() {
      }

      @Override
      public boolean active() {
         return false;
      }

      @Override
      public boolean disables(@NotNull Player local1, @NotNull CheckId local2) {
         return false;
      }

      @Override
      public boolean disablesLocation(@NotNull Player local1, @NotNull Location local2, @NotNull CheckId checkId3) {
         return false;
      }

      @Override
      public void refreshSettings() {
      }

      @Override
      public void refreshPlayer(@NotNull Player local1, @NotNull Location local2) {
      }

      @Override
      public void clearPlayer(@NotNull UUID local1) {
      }

      @Override
      public void shutdown() {
      }
   }
}
