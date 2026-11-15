package studio.glace.glantielytratarget;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.bootstrap.GraceConsoleBanner;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.bootstrap.ServiceRegistry;
import studio.glace.glantielytratarget.config.ConfigurationService;
import studio.glace.glantielytratarget.config.LanguageService;
import studio.glace.glantielytratarget.platform.BedrockPlatformProbe;
import studio.glace.glantielytratarget.platform.PlatformScheduler;
import studio.glace.glantielytratarget.platform.runtime.RuntimeAuthority;
import studio.glace.glantielytratarget.platform.runtime.RuntimeGate;

final class LicensedBootCoordinator {
   private static final long INIT_SEAL = 3237998146L;
   private static final long GRACE_SEAL = 2594964574L;
   private static final long GRACE_TICKS = 1200L;
   private static final int WIRE_A = 16866;
   private final glAntiElytraTarget plugin;
   private final AtomicBoolean verifyInFlight;
   private final Object bootLock;

   LicensedBootCoordinator(@NotNull glAntiElytraTarget glAntiElytraTarget1, @NotNull AtomicBoolean atomicBoolean2, @NotNull Object object3) {
      this.plugin = glAntiElytraTarget1;
      this.verifyInFlight = atomicBoolean2;
      this.bootLock = object3;
   }

   boolean completeFromReload() {
      Object object9 = this.bootLock;
      Object object6 = object9;
      String string5;
      synchronized (object9) {
         PluginContext pluginContext7 = this.plugin.context();
         PluginContext pluginContext13 = pluginContext7;
         long l14 = RuntimeGate.bootReady(pluginContext13);
         long l3 = 0L;
         if (l14 == l3) {
            Object object59 = object6;
            return false;
         }

         PluginContext pluginContext17 = pluginContext7;
         long l18 = RuntimeGate.graceBootAllowed(pluginContext17);
         long l60 = 0L;
         if (l18 == l60) {
            Object object50 = object6;
            return false;
         }

         PluginContext pluginContext25 = pluginContext7;
         long l26 = RuntimeGate.graceExpired(pluginContext25);
         long l62 = 0L;
         if (l26 != l62) {
            Object object51 = object6;
            return false;
         }

         PluginContext pluginContext30 = pluginContext7;
         string5 = pluginContext30.config().getLicenseKey();

         Object object37 = object6;
      }

      String string53 = string5;
      LicensedBootCoordinator.Source source64 = LicensedBootCoordinator.Source.GRACE_RELOAD;
      return this.schedule(string53, source64);
   }

   long beginGracePeriod(@NotNull ConfigurationService configurationService1) throws Exception {
      glAntiElytraTarget glAntiElytraTarget51 = this.plugin;
      PlatformScheduler platformScheduler3 = new PlatformScheduler(glAntiElytraTarget51);
      PlatformScheduler platformScheduler7 = platformScheduler3;
      PluginContext pluginContext13 = this.plugin.context();
      PlatformScheduler platformScheduler41 = platformScheduler7;
      pluginContext13.setScheduler(platformScheduler41);
      glAntiElytraTarget glAntiElytraTarget53 = this.plugin;
      LanguageService languageService14 = new LanguageService(glAntiElytraTarget53, configurationService1);
      LanguageService languageService8 = languageService14;
      LanguageService languageService15 = languageService8;
      languageService15.initializeSilent();
      PluginContext pluginContext18 = this.plugin.context();
      LanguageService languageService42 = languageService8;
      pluginContext18.setLanguage(languageService42);
      Logger logger66 = this.plugin.getLogger();
      File file70 = this.plugin.artifactFile();
      RuntimeAuthority runtimeAuthority19 = new RuntimeAuthority("FILL_HERE", logger66, file70);
      RuntimeAuthority runtimeAuthority9 = runtimeAuthority19;
      RuntimeAuthority runtimeAuthority20 = runtimeAuthority9;
      Runnable runnable44 = () -> {
         BukkitScheduler bukkitScheduler6 = this.plugin.getServer().getScheduler();
         glAntiElytraTarget glAntiElytraTarget8x = this.plugin;
         Runnable runnable9x = () -> {
            PluginManager pluginManager5 = this.plugin.getServer().getPluginManager();
            glAntiElytraTarget glAntiElytraTarget6x = this.plugin;
            pluginManager5.disablePlugin(glAntiElytraTarget6x);
         };
         BukkitTask bukkitTask7x = bukkitScheduler6.runTask(glAntiElytraTarget8x, runnable9x);
      };
      runtimeAuthority20._fr0(runnable44);
      RuntimeAuthority runtimeAuthority21 = runtimeAuthority9;
      runtimeAuthority21._armGrace0();
      PluginContext pluginContext24 = this.plugin.context();
      RuntimeAuthority runtimeAuthority45 = runtimeAuthority9;
      pluginContext24.setRuntime(runtimeAuthority45);
      ServiceRegistry serviceRegistry10 = this.plugin.services();
      ServiceRegistry serviceRegistry28 = serviceRegistry10;
      if (serviceRegistry28 == null) {
         Logger logger57 = this.plugin.getLogger();
         ServiceRegistry serviceRegistry29 = new ServiceRegistry(logger57);
         serviceRegistry10 = serviceRegistry29;
         glAntiElytraTarget glAntiElytraTarget31 = this.plugin;
         ServiceRegistry serviceRegistry46 = serviceRegistry10;
         glAntiElytraTarget31.setServices(serviceRegistry46);
      }

      ServiceRegistry serviceRegistry32 = serviceRegistry10;
      PlatformScheduler platformScheduler58 = platformScheduler7;
      Object object67 = Objects.requireNonNull(platformScheduler58);
      Runnable runnable59 = platformScheduler58::shutdown;
      ServiceRegistry serviceRegistry33 = serviceRegistry32.registerMainThread("scheduler", runnable59);
      RuntimeAuthority runtimeAuthority60 = runtimeAuthority9;
      Object object68 = Objects.requireNonNull(runtimeAuthority60);
      Runnable runnable61 = runtimeAuthority60::shutdown;
      ServiceRegistry serviceRegistry34 = serviceRegistry33.register("runtime", runnable61);
      GraceConsoleBanner.graceRequired(this.plugin.getLogger());
      PlatformScheduler platformScheduler38 = platformScheduler7;
      Runnable runnable50 = this::onGraceDeadlineElapsed;
      long l62 = 1200L;
      platformScheduler38.runGlobalLater(runnable50, l62);
      return 2594964574L;
   }

   void beginAsyncStartup(@NotNull ConfigurationService configurationService1, @NotNull String string2, @NotNull LicensedBootCoordinator.Source source3) {
      glAntiElytraTarget glAntiElytraTarget39 = this.plugin;
      PlatformScheduler platformScheduler5 = new PlatformScheduler(glAntiElytraTarget39);
      PlatformScheduler platformScheduler8 = platformScheduler5;
      PluginContext pluginContext13 = this.plugin.context();
      PlatformScheduler platformScheduler33 = platformScheduler8;
      pluginContext13.setScheduler(platformScheduler33);
      glAntiElytraTarget glAntiElytraTarget41 = this.plugin;
      LanguageService languageService14 = new LanguageService(glAntiElytraTarget41, configurationService1);
      LanguageService languageService9 = languageService14;
      LanguageService languageService15 = languageService9;
      languageService15.initializeSilent();
      PluginContext pluginContext18 = this.plugin.context();
      LanguageService languageService34 = languageService9;
      pluginContext18.setLanguage(languageService34);
      ServiceRegistry serviceRegistry10 = this.plugin.services();
      ServiceRegistry serviceRegistry22 = serviceRegistry10;
      if (serviceRegistry22 == null) {
         Logger logger44 = this.plugin.getLogger();
         ServiceRegistry serviceRegistry23 = new ServiceRegistry(logger44);
         serviceRegistry10 = serviceRegistry23;
         glAntiElytraTarget glAntiElytraTarget25 = this.plugin;
         ServiceRegistry serviceRegistry35 = serviceRegistry10;
         glAntiElytraTarget25.setServices(serviceRegistry35);
      }

      ServiceRegistry serviceRegistry26 = serviceRegistry10;
      PlatformScheduler platformScheduler45 = platformScheduler8;
      Object object48 = Objects.requireNonNull(platformScheduler45);
      Runnable runnable46 = platformScheduler45::shutdown;
      ServiceRegistry serviceRegistry27 = serviceRegistry26.registerMainThread("scheduler", runnable46);
      this.plugin.getLogger().info("Verifying license key...");
      boolean flag32 = this.schedule(string2, source3);
   }

   private void onGraceDeadlineElapsed() {
      Object object7 = this.bootLock;
      Object object5 = object7;
      String string4;
      synchronized (object7) {
         label31: {
            long l11 = RuntimeGate.bootReady(this.plugin.context());
            long l2 = 0L;
            if (l11 == l2) {
               Object object14 = object5;
               return;
            }

            this.reloadConfigFromDisk();
            string4 = this.plugin.context().config().getLicenseKey();
            String string36 = string4;
            if (!"FILL_HERE".equals(string36)) {
               String string26 = string4;
               if (!string26.isEmpty()) {
                  Object object28 = object5;
                  break label31;
               }
            }

            this.expireGraceAndDisable();
            Object object35 = object5;
            return;
         }
      }

      this.plugin.getLogger().info("Grace period ended. Verifying license key...");
      String string38 = string4;
      LicensedBootCoordinator.Source source39 = LicensedBootCoordinator.Source.GRACE_DEADLINE;
      boolean flag33 = this.schedule(string38, source39);
   }

   private void reloadConfigFromDisk() {
      try {
         this.plugin.reloadConfig();
         this.plugin.context().config().reload();
      } catch (Exception exception16) {
         Logger logger13 = this.plugin.getLogger();
         String string15 = "Failed to reload config at grace deadline: " + exception16.getMessage();
         logger13.severe(string15);
      }
   }

   private boolean schedule(@NotNull String string1, @NotNull LicensedBootCoordinator.Source source2) {
      PlatformScheduler platformScheduler8 = this.plugin.context().scheduler();
      PlatformScheduler platformScheduler13 = platformScheduler8;
      if (platformScheduler13 == null) {
         glAntiElytraTarget glAntiElytraTarget33 = this.plugin;
         PlatformScheduler platformScheduler14 = new PlatformScheduler(glAntiElytraTarget33);
         platformScheduler8 = platformScheduler14;
         PluginContext pluginContext17 = this.plugin.context();
         PlatformScheduler platformScheduler28 = platformScheduler8;
         pluginContext17.setScheduler(platformScheduler28);
      }

      if (!this.verifyInFlight.compareAndSet(false, true)) {
         this.plugin.getLogger().info("License verification already in progress.");
         return false;
      } else {
         PlatformScheduler platformScheduler25 = platformScheduler8;
         PlatformScheduler platformScheduler9 = platformScheduler25;
         PlatformScheduler platformScheduler26 = platformScheduler9;
         PlatformScheduler platformScheduler6 = platformScheduler9;
         Runnable runnable32 = () -> {
            RuntimeAuthority runtimeAuthority8x = null;

            label22: {
               label21: {
                  try {
                     try {
                        runtimeAuthority8x = this.verifyNetwork(string1);
                        break label21;
                     } catch (Exception exception37) {
                        Logger logger16 = this.plugin.getLogger();
                        String string30 = "License verification failed: " + exception37.getMessage();
                        logger16.severe(string30);
                        exception37.printStackTrace();
                     }
                  } catch (Throwable throwable38) {
                     this.verifyInFlight.set(false);
                     throw throwable38;
                  }

                  this.verifyInFlight.set(false);
                  break label22;
               }

               this.verifyInFlight.set(false);
            }

            RuntimeAuthority runtimeAuthority20 = runtimeAuthority8x;
            RuntimeAuthority runtimeAuthority36 = runtimeAuthority20;
            RuntimeAuthority runtimeAuthority6x = runtimeAuthority36;
            Runnable runnable33x = () -> this.applyOnMain(runtimeAuthority6x, source2);
            platformScheduler6.runGlobal(runnable33x);
         };
         platformScheduler26.runAsync(runnable32);
         return true;
      }
   }

   private void applyOnMain(@Nullable RuntimeAuthority runtimeAuthority1, @NotNull LicensedBootCoordinator.Source source2) {
      Object object12 = this.bootLock;
      Object object7 = object12;
      synchronized (object12) {
         if (!this.stillAllowed(source2)) {
            if (runtimeAuthority1 != null) {
               runtimeAuthority1.shutdown();
            }

            Object object17 = object7;
         } else if (runtimeAuthority1 == null) {
            this.handleFailure(source2);
            Object object23 = object7;
         } else {
            long l8;
            try {
               l8 = LicensedServiceWiring.apply(this.plugin, runtimeAuthority1);
            } catch (Exception exception79) {
               runtimeAuthority1.shutdown();
               Logger logger60 = this.plugin.getLogger();
               String string73 = "Failed to complete license startup: " + exception79.getMessage();
               logger60.severe(string73);
               exception79.printStackTrace();
               this.handleFailure(source2);
               Object object63 = object7;
               return;
            }

            long l28 = l8;
            long l5 = 3237998146L;
            long l30 = l28 ^ l5;
            long l75 = 0L;
            if (l30 != l75) {
               this.handleFailure(source2);
               Object object34 = object7;
            } else {
               this.plugin.registerListeners();
               this.plugin.registerCommands();
               long l41 = this.plugin.finalizeLicensedBoot();
               long l77 = 0L;
               if (l41 != l77) {
                  this.handleFailure(source2);
                  Object object55 = object7;
               } else {
                  runtimeAuthority1._clearGrace0();
                  if (BedrockPlatformProbe.isGeyserAvailable(5791842) || BedrockPlatformProbe.isFloodgateAvailable(1490920131)) {
                     this.plugin.getLogger().info("Geyser/Floodgate detected. Bedrock players will be bypassed.");
                  }

                  this.plugin.getLogger().info("All components initialized successfully!");
                  Object object53 = object7;
               }
            }
         }
      }
   }

   private boolean stillAllowed(@NotNull LicensedBootCoordinator.Source source1) {
      if (this.plugin.isShutdownStarted()) {
         return false;
      }

      PluginContext pluginContext6 = this.plugin.context();

      return switch (source1) {
         case GRACE_DEADLINE -> {
            PluginContext pluginContext15 = pluginContext6;
            long l16 = RuntimeGate.bootReady(pluginContext15);
            long l4 = 0L;
            if (l16 != l4) {
               PluginContext pluginContext19 = pluginContext6;
               long l20 = RuntimeGate.graceExpired(pluginContext19);
               long l41 = 0L;
               if (l20 == l41) {
                  yield true;
               }
            }

            yield false;
         }
         case GRACE_RELOAD -> {
            PluginContext pluginContext24 = pluginContext6;
            long l25 = RuntimeGate.bootReady(pluginContext24);
            long l43 = 0L;
            if (l25 != l43) {
               PluginContext pluginContext28 = pluginContext6;
               long l29 = RuntimeGate.graceBootAllowed(pluginContext28);
               long l45 = 0L;
               if (l29 != l45) {
                  PluginContext pluginContext32 = pluginContext6;
                  long l33 = RuntimeGate.graceExpired(pluginContext32);
                  long l47 = 0L;
                  if (l33 == l47) {
                     yield true;
                  }
               }
            }

            yield false;
         }
         case DIRECT_ENABLE -> {
            PluginContext pluginContext36 = pluginContext6;
            long l37 = RuntimeGate.bootReady(pluginContext36);
            long l49 = 0L;
            if (l37 != l49) {
               yield true;
            } else {
               yield false;
            }
         }
      };
   }

   private void handleFailure(@NotNull LicensedBootCoordinator.Source source1) {
      LicensedBootCoordinator.Source source4 = LicensedBootCoordinator.Source.GRACE_DEADLINE;
      if (source1 == source4) {
         this.latchGraceExpiredAndDisable();
      } else {
         PluginManager pluginManager8 = this.plugin.getServer().getPluginManager();
         glAntiElytraTarget glAntiElytraTarget11 = this.plugin;
         pluginManager8.disablePlugin(glAntiElytraTarget11);
      }
   }

   private void expireGraceAndDisable() {
      GraceConsoleBanner.graceExpired(this.plugin.getLogger());
      this.latchGraceExpiredAndDisable();
   }

   private void latchGraceExpiredAndDisable() {
      RuntimeAuthority runtimeAuthority4 = this.plugin.context().runtime();
      RuntimeAuthority runtimeAuthority8 = runtimeAuthority4;
      if (runtimeAuthority8 != null) {
         RuntimeAuthority runtimeAuthority9 = runtimeAuthority4;
         runtimeAuthority9._expireGrace0();
      }

      PluginManager pluginManager13 = this.plugin.getServer().getPluginManager();
      glAntiElytraTarget glAntiElytraTarget14 = this.plugin;
      pluginManager13.disablePlugin(glAntiElytraTarget14);
   }

   @Nullable
   private RuntimeAuthority verifyNetwork(@NotNull String string1) {
      Object object10 = this.bootLock;
      Object object8 = object10;
      RuntimeAuthority runtimeAuthority7;
      synchronized (object10) {
         runtimeAuthority7 = this.plugin.context().runtime();
         Object object15 = object8;
      }

      RuntimeAuthority runtimeAuthority19 = runtimeAuthority7;
      if (runtimeAuthority19 != null) {
         RuntimeAuthority runtimeAuthority20 = runtimeAuthority7;
         runtimeAuthority20.shutdown();
      }

      Logger logger64 = this.plugin.getLogger();
      File file66 = this.plugin.artifactFile();
      RuntimeAuthority runtimeAuthority21 = new RuntimeAuthority(string1, logger64, file66);
      RuntimeAuthority runtimeAuthority67 = runtimeAuthority21;
      RuntimeAuthority runtimeAuthority22 = runtimeAuthority67;
      Runnable runnable47 = () -> {
         BukkitScheduler bukkitScheduler6 = this.plugin.getServer().getScheduler();
         glAntiElytraTarget glAntiElytraTarget8x = this.plugin;
         Runnable runnable9 = () -> {
            PluginManager pluginManager5 = this.plugin.getServer().getPluginManager();
            glAntiElytraTarget glAntiElytraTarget6x = this.plugin;
            pluginManager5.disablePlugin(glAntiElytraTarget6x);
         };
         BukkitTask bukkitTask7x = bukkitScheduler6.runTask(glAntiElytraTarget8x, runnable9);
      };
      runtimeAuthority22._fr0(runnable47);

      try {
         RuntimeAuthority runtimeAuthority23 = runtimeAuthority67;
         runtimeAuthority23._vx0();
      } catch (IllegalStateException illegalStateException69) {
         RuntimeAuthority runtimeAuthority25 = runtimeAuthority67;
         runtimeAuthority25.shutdown();
         return null;
      }

      RuntimeAuthority runtimeAuthority27 = runtimeAuthority67;
      ClassLoader classLoader50 = this.plugin.pluginClassLoader();
      int i28 = runtimeAuthority27._cl0(classLoader50);
      RuntimeAuthority runtimeAuthority29 = runtimeAuthority67;
      ClassLoader classLoader53 = this.plugin.pluginClassLoader();
      if (runtimeAuthority29._cl0(classLoader53) > 100) {
         this.plugin.getLogger().warning("Runtime integrity warning: unusual class load count.");
      }

      RuntimeAuthority runtimeAuthority35 = runtimeAuthority67;
      long l38 = runtimeAuthority35._wire0(16866) ^ 16866;
      RuntimeAuthority runtimeAuthority58 = runtimeAuthority67;
      long l59 = runtimeAuthority58._co0();
      long l40 = l38 | l59;
      long l61 = 0L;
      if (l40 != l61) {
         RuntimeAuthority runtimeAuthority43 = runtimeAuthority67;
         runtimeAuthority43.shutdown();
         return null;
      } else {
         return runtimeAuthority67;
      }
   }

   enum Source {
      GRACE_DEADLINE,
      GRACE_RELOAD,
      DIRECT_ENABLE;

      Source() {
      }
   }
}
