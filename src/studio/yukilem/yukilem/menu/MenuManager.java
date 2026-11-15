package studio.yukilem.yukilem.menu;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.data.ViolationRecord;
import studio.yukilem.yukilem.libs.nixlib.gui.GuiManager;
import studio.yukilem.yukilem.platform.PlatformScheduler;

public final class MenuManager {
   private final PluginContext ctx;
   private final ProfileMenu profileMenu;
   private final ViolationsMenu violationsMenu;
   private final ConfirmMenu confirmMenu;
   private final Map<UUID, MenuManager.Session> sessions;

   public MenuManager(@NotNull PluginContext pluginContext1) {
      ConcurrentHashMap concurrentHashMap4 = new ConcurrentHashMap();
      this.sessions = concurrentHashMap4;
      this.ctx = pluginContext1;
      ProfileMenu profileMenu15 = new ProfileMenu(pluginContext1);
      this.profileMenu = profileMenu15;
      ViolationsMenu violationsMenu16 = new ViolationsMenu(pluginContext1);
      this.violationsMenu = violationsMenu16;
      ConfirmMenu confirmMenu17 = new ConfirmMenu(pluginContext1);
      this.confirmMenu = confirmMenu17;
      GuiManager guiManager13 = GuiManager.register(pluginContext1.plugin());
   }

   public void openProfile(@NotNull Player player1, @NotNull UUID uuid2, @NotNull String string3, @NotNull List<ViolationRecord> list4) {
      Map map12 = this.sessions;
      UUID uuid22 = player1.getUniqueId();
      Function function8 = arg0 -> new MenuManager.Session();
      MenuManager.Session session11 = (MenuManager.Session)map12.computeIfAbsent(uuid22, function8);
      MenuManager.Session session15 = session11;
      session15.targetUuid = uuid2;
      MenuManager.Session session16 = session11;
      session16.targetName = string3;
      MenuManager.Session session17 = session11;
      List list26 = List.copyOf(list4);
      session17.violations = list26;
      MenuManager.Session session18 = session11;
      session18.page = 0;
      MenuManager.Session session19 = session11;
      MenuManager.OpenMenu openMenu28 = MenuManager.OpenMenu.PROFILE;
      session19.openMenu = openMenu28;
      ProfileMenu profileMenu21 = this.profileMenu;
      MenuManager.Session session10 = session11;
      List list31 = session10.violations;
      profileMenu21.open(player1, uuid2, string3, list31);
   }

   public void openViolations(@NotNull Player player1, int i2) {
      Map map11 = this.sessions;
      UUID uuid21 = player1.getUniqueId();
      MenuManager.Session session10 = (MenuManager.Session)map11.get(uuid21);
      MenuManager.Session session14 = session10;
      if (session14 != null) {
         MenuManager.Session session15 = session10;
         if (session15.targetUuid != null) {
            MenuManager.Session session17 = session10;
            session17.page = i2;
            MenuManager.Session session18 = session10;
            MenuManager.OpenMenu openMenu23 = MenuManager.OpenMenu.VIOLATIONS;
            session18.openMenu = openMenu23;
            ViolationsMenu violationsMenu20 = this.violationsMenu;
            MenuManager.Session session6 = session10;
            String string25 = session6.targetName;
            MenuManager.Session session7 = session10;
            UUID uuid26 = session7.targetUuid;
            MenuManager.Session session8 = session10;
            List list27 = session8.violations;
            violationsMenu20.open(player1, string25, uuid26, list27, i2);
            return;
         }
      }
   }

   public void openViolationsRefreshed(@NotNull Player player1) {
      Map map9 = this.sessions;
      UUID uuid27 = player1.getUniqueId();
      MenuManager.Session session7 = (MenuManager.Session)map9.get(uuid27);
      MenuManager.Session session12 = session7;
      if (session12 != null) {
         MenuManager.Session session13 = session7;
         if (session13.targetUuid != null) {
            MenuManager.Session session15 = session7;
            UUID uuid8 = session15.targetUuid;
            if (this.ctx.violationRepository() != null && this.ctx.scheduler() != null) {
               PlatformScheduler platformScheduler25 = this.ctx.scheduler();
               UUID uuid5 = uuid8;
               Runnable runnable29 = () -> {
                  List list9x = this.ctx.violationRepository().load(uuid5);
                  PlatformScheduler platformScheduler15x = this.ctx.scheduler();
                  List list8x = list9x;
                  Runnable runnable17 = () -> {
                     if (player1.isOnline()) {
                        Map map10 = this.sessions;
                        UUID uuid21 = player1.getUniqueId();
                        MenuManager.Session session7x = (MenuManager.Session)map10.get(uuid21);
                        MenuManager.Session session13x = session7x;
                        if (session13x != null) {
                           MenuManager.Session session14 = session7x;
                           if (session14.targetUuid != null) {
                              MenuManager.Session session16 = session7x;
                              if (session16.targetUuid.equals(uuid5)) {
                                 MenuManager.Session session19 = session7x;
                                 List list24 = List.copyOf(list8x);
                                 session19.violations = list24;
                                 this.openViolations(player1, 0);
                                 return;
                              }
                           }
                        }
                     }
                  };
                  platformScheduler15x.runEntity(player1, runnable17);
               };
               platformScheduler25.runAsync(runnable29);
               return;
            }

            this.openViolations(player1, 0);
            return;
         }
      }
   }

   public void openByName(@NotNull Player player1, @NotNull String string2) {
      Locale locale5 = Locale.ROOT;
      String string6 = string2.toLowerCase(locale5);
      byte b7 = -1;
      String string10 = string6;
      switch (string10.hashCode()) {
         case -1600986843:
            String string21 = string6;
            if (string21.equals("violation")) {
               b7 = 2;
            }
            break;
         case -309425751:
            String string12 = string6;
            if (string12.equals("profile")) {
               b7 = 0;
            }
            break;
         case 1909015534:
            String string18 = string6;
            if (string18.equals("violations")) {
               b7 = 1;
            }
      }

      byte b15 = b7;
      switch (b15) {
         case 0:
            this.reopenProfile(player1);
            break;
         case 1:
         case 2:
            this.openViolationsRefreshed(player1);
      }
   }

   public void reopenProfile(@NotNull Player player1) {
      Map map9 = this.sessions;
      UUID uuid18 = player1.getUniqueId();
      MenuManager.Session session8 = (MenuManager.Session)map9.get(uuid18);
      MenuManager.Session session12 = session8;
      if (session12 != null) {
         MenuManager.Session session13 = session8;
         if (session13.targetUuid != null) {
            MenuManager.Session session15 = session8;
            MenuManager.OpenMenu openMenu19 = MenuManager.OpenMenu.PROFILE;
            session15.openMenu = openMenu19;
            ProfileMenu profileMenu17 = this.profileMenu;
            MenuManager.Session session5 = session8;
            UUID uuid21 = session5.targetUuid;
            MenuManager.Session session6 = session8;
            String string22 = session6.targetName;
            MenuManager.Session session7 = session8;
            List list23 = session7.violations;
            profileMenu17.open(player1, uuid21, string22, list23);
            return;
         }
      }
   }

   public void refreshProfile(@NotNull Player player1) {
      Map map11 = this.sessions;
      UUID uuid31 = player1.getUniqueId();
      MenuManager.Session session8 = (MenuManager.Session)map11.get(uuid31);
      MenuManager.Session session14 = session8;
      if (session14 != null) {
         MenuManager.Session session15 = session8;
         if (session15.targetUuid != null) {
            MenuManager.Session session17 = session8;
            UUID uuid9 = session17.targetUuid;
            MenuManager.Session session19 = session8;
            String string10 = session19.targetName;
            if (this.ctx.violationRepository() != null && this.ctx.scheduler() != null) {
               PlatformScheduler platformScheduler30 = this.ctx.scheduler();
               UUID uuid5 = uuid9;
               String string7 = string10;
               Runnable runnable34 = () -> {
                  List list11x = this.ctx.violationRepository().load(uuid5);
                  PlatformScheduler platformScheduler17x = this.ctx.scheduler();
                  List list9x = list11x;
                  Runnable runnable19x = () -> {
                     if (player1.isOnline()) {
                        Map map13 = this.sessions;
                        UUID uuid26 = player1.getUniqueId();
                        MenuManager.Session session10x = (MenuManager.Session)map13.get(uuid26);
                        MenuManager.Session session16 = session10x;
                        if (session16 != null) {
                           MenuManager.Session tmp17xx = session10x;
                           if (tmp17xx.targetUuid != null) {
                              MenuManager.Session tmp19xx = session10x;
                              if (tmp19xx.targetUuid.equals(uuid5)) {
                                 MenuManager.Session session22 = session10x;
                                 List list29 = List.copyOf(list9x);
                                 session22.violations = list29;
                                 MenuManager.Session session23 = session10x;
                                 MenuManager.OpenMenu openMenu30x = MenuManager.OpenMenu.PROFILE;
                                 session23.openMenu = openMenu30x;
                                 ProfileMenu profileMenu25 = this.profileMenu;
                                 MenuManager.Session tmp9xx = session10x;
                                 List list32 = tmp9xx.violations;
                                 profileMenu25.open(player1, uuid5, string7, list32);
                                 return;
                              }
                           }
                        }
                     }
                  };
                  platformScheduler17x.runEntity(player1, runnable19x);
               };
               platformScheduler30.runAsync(runnable34);
               return;
            }

            this.reopenProfile(player1);
            return;
         }
      }
   }

   public void refresh(@NotNull Player player1) {
      Map map6 = this.sessions;
      UUID uuid16 = player1.getUniqueId();
      MenuManager.Session session5 = (MenuManager.Session)map6.get(uuid16);
      MenuManager.Session session9 = session5;
      if (session9 != null) {
         MenuManager.Session session10 = session5;
         if (session10.targetUuid != null) {
            MenuManager.Session session12 = session5;
            MenuManager.OpenMenu openMenu13 = session12.openMenu;
            MenuManager.OpenMenu openMenu17 = MenuManager.OpenMenu.VIOLATIONS;
            if (openMenu13 == openMenu17) {
               this.refreshViolations(player1);
            } else {
               this.refreshProfile(player1);
            }

            return;
         }
      }
   }

   public void refreshViolations(@NotNull Player player1) {
      Map map11 = this.sessions;
      UUID uuid31 = player1.getUniqueId();
      MenuManager.Session session8 = (MenuManager.Session)map11.get(uuid31);
      MenuManager.Session session14 = session8;
      if (session14 != null) {
         MenuManager.Session session15 = session8;
         if (session15.targetUuid != null) {
            MenuManager.Session session17 = session8;
            UUID uuid9 = session17.targetUuid;
            MenuManager.Session session19 = session8;
            int i10 = session19.page;
            if (this.ctx.violationRepository() != null && this.ctx.scheduler() != null) {
               PlatformScheduler platformScheduler29 = this.ctx.scheduler();
               UUID uuid5 = uuid9;
               int i7 = i10;
               Runnable runnable33 = () -> {
                  List list11x = this.ctx.violationRepository().load(uuid5);
                  PlatformScheduler platformScheduler17x = this.ctx.scheduler();
                  List list9x = list11x;
                  Runnable runnable19x = () -> {
                     if (player1.isOnline()) {
                        Map map15x = this.sessions;
                        UUID uuid29x = player1.getUniqueId();
                        MenuManager.Session session10x = (MenuManager.Session)map15x.get(uuid29x);
                        MenuManager.Session session18 = session10x;
                        if (session18 != null) {
                           MenuManager.Session tmp19xx = session10x;
                           if (tmp19xx.targetUuid != null) {
                              MenuManager.Session session21 = session10x;
                              if (session21.targetUuid.equals(uuid5)) {
                                 MenuManager.Session session24 = session10x;
                                 List list32 = List.copyOf(list9x);
                                 session24.violations = list32;
                                 MenuManager.Session session33x = session10x;
                                 double d36 = session33x.violations.size();
                                 double d45 = 45;
                                 int i42 = (int)Math.ceil(d36 / d45);
                                 int i48 = Math.max(1, i42) - 1;
                                 int i44 = Math.min(i7, i48);
                                 this.openViolations(player1, i44);
                                 return;
                              }
                           }
                        }
                     }
                  };
                  platformScheduler17x.runEntity(player1, runnable19x);
               };
               platformScheduler29.runAsync(runnable33);
               return;
            }

            int i35 = i10;
            this.openViolations(player1, i35);
            return;
         }
      }
   }

   public void openConfirm(@NotNull Player player1, @NotNull ConfirmRequest confirmRequest2) {
      Map map8 = this.sessions;
      UUID uuid15 = player1.getUniqueId();
      Function function6 = arg0 -> new MenuManager.Session();
      MenuManager.Session session7 = (MenuManager.Session)map8.computeIfAbsent(uuid15, function6);
      MenuManager.Session session11 = session7;
      session11.confirmRequest = confirmRequest2;
      MenuManager.Session session12 = session7;
      MenuManager.OpenMenu openMenu17 = MenuManager.OpenMenu.CONFIRM;
      session12.openMenu = openMenu17;
      this.confirmMenu.open(player1, confirmRequest2);
   }

   @Nullable
   public UUID targetUuid(@NotNull Player player1) {
      Map map6 = this.sessions;
      UUID uuid12 = player1.getUniqueId();
      MenuManager.Session session5 = (MenuManager.Session)map6.get(uuid12);
      MenuManager.Session session9 = session5;
      UUID uuid11;
      if (session9 == null) {
         uuid11 = null;
      } else {
         MenuManager.Session session10 = session5;
         uuid11 = session10.targetUuid;
      }

      return uuid11;
   }

   @Nullable
   public String targetName(@NotNull Player player1) {
      Map map6 = this.sessions;
      UUID uuid12 = player1.getUniqueId();
      MenuManager.Session session5 = (MenuManager.Session)map6.get(uuid12);
      MenuManager.Session session9 = session5;
      String string10;
      if (session9 == null) {
         string10 = null;
      } else {
         MenuManager.Session session11 = session5;
         string10 = session11.targetName;
      }

      return string10;
   }

   @NotNull
   public List<ViolationRecord> violations(@NotNull Player player1) {
      Map map5 = this.sessions;
      UUID uuid13 = player1.getUniqueId();
      MenuManager.Session session4 = (MenuManager.Session)map5.get(uuid13);
      MenuManager.Session session8 = session4;
      if (session8 != null) {
         MenuManager.Session session9 = session4;
         if (session9.violations != null) {
            MenuManager.Session session11 = session4;
            return session11.violations;
         }
      }

      return List.of();
   }

   public int currentViolationsPage(@NotNull Player player1) {
      Map map5 = this.sessions;
      UUID uuid11 = player1.getUniqueId();
      MenuManager.Session session4 = (MenuManager.Session)map5.get(uuid11);
      MenuManager.Session session8 = session4;
      int i10;
      if (session8 == null) {
         i10 = 0;
      } else {
         MenuManager.Session session9 = session4;
         i10 = session9.page;
      }

      return i10;
   }

   @Nullable
   public ConfirmRequest confirmRequest(@NotNull Player player1) {
      Map map6 = this.sessions;
      UUID uuid12 = player1.getUniqueId();
      MenuManager.Session session5 = (MenuManager.Session)map6.get(uuid12);
      MenuManager.Session session9 = session5;
      ConfirmRequest confirmRequest11;
      if (session9 == null) {
         confirmRequest11 = null;
      } else {
         MenuManager.Session session10 = session5;
         confirmRequest11 = session10.confirmRequest;
      }

      return confirmRequest11;
   }

   public void evict(@NotNull UUID uuid1) {
      Object object6 = this.sessions.remove(uuid1);
   }

   public void shutdown() {
      this.sessions.clear();
      GuiManager.unregister();
   }

   private enum OpenMenu {
      PROFILE,
      VIOLATIONS,
      CONFIRM;

      OpenMenu() {
      }
   }

   private static final class Session {
      private UUID targetUuid;
      private String targetName;
      private List<ViolationRecord> violations;
      private int page;
      private MenuManager.OpenMenu openMenu;
      private ConfirmRequest confirmRequest;

      private Session() {
         List list2 = List.of();
         this.violations = list2;
         MenuManager.OpenMenu openMenu5 = MenuManager.OpenMenu.PROFILE;
         this.openMenu = openMenu5;
      }
   }
}
