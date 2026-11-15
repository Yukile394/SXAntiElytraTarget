package studio.glace.glantielytratarget.commands.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.commands.CommandMessages;
import studio.glace.glantielytratarget.commands.SubCommand;
import studio.glace.glantielytratarget.config.LanguageService;
import studio.glace.glantielytratarget.data.FlagRepository;
import studio.glace.glantielytratarget.platform.PlatformScheduler;

public final class ProfileSubCommand implements SubCommand {
   public ProfileSubCommand() {
   }

   @Override
   public boolean execute(@NotNull PluginContext pluginContext1, @NotNull CommandSender commandSender2, @NotNull String[] strings3) {
      if (commandSender2 instanceof Player player10) {
         Player player19 = player10;
         String string61 = this.permission();
         if (!player19.hasPermission(string61)) {
            Player player56 = player10;
            Component component77 = pluginContext1.language().getMessage("general.no-permission");
            player56.sendMessage(component77);
            return true;
         }

         if (pluginContext1.menus() != null && pluginContext1.flagRepository() != null && pluginContext1.violationRepository() != null) {
            if (strings3.length < 2) {
               Player player66 = player10;
               CommandMessages.incorrectArgument(pluginContext1, player66, "commands.usage.profile");
               return true;
            }

            String string11 = strings3[1];
            FlagRepository flagRepository12 = pluginContext1.flagRepository();
            String string37 = string11;
            Player player13 = Bukkit.getPlayerExact(string37);
            Player player39 = player13;
            UUID uuid90;
            String string91;
            if (player39 != null) {
               Player player40 = player13;
               uuid90 = player40.getUniqueId();
               Player player42 = player13;
               string91 = player42.getName();
            } else {
               FlagRepository flagRepository47 = flagRepository12;
               String string70 = string11;
               UUID uuid14 = flagRepository47.uuidForName(string70);
               UUID uuid49 = uuid14;
               if (uuid49 == null) {
                  Player player50 = player10;
                  LanguageService languageService72 = pluginContext1.language();
                  String string89 = string11;
                  Map map88 = Map.of("%player%", string89);
                  Component component73 = languageService72.message("data-reset.profile-no-data", map88);
                  player50.sendMessage(component73);
                  return true;
               }

               UUID uuid52 = uuid14;
               uuid90 = uuid52;
               FlagRepository flagRepository53 = flagRepository12;
               UUID uuid74 = uuid14;
               String string15 = flagRepository53.nameForUuid(uuid74);
               String string55 = string15;
               String string60;
               if (string55 != null) {
                  string60 = string15;
               } else {
                  string60 = string11;
               }

               string91 = string60;
            }

            PlatformScheduler platformScheduler45 = pluginContext1.scheduler();
            UUID uuid82 = uuid90;
            Player player86 = player10;
            String string9 = string91;
            Runnable runnable69 = () -> {
               List list11x = pluginContext1.violationRepository().load(uuid82);
               PlatformScheduler platformScheduler15x = pluginContext1.scheduler();
               List list10x = list11x;
               Runnable runnable17 = () -> {
                  if (player86.isOnline() && pluginContext1.menus() != null) {
                     pluginContext1.menus().openProfile(player86, uuid82, string9, list10x);
                  }
               };
               platformScheduler15x.runEntity(player86, runnable17);
            };
            platformScheduler45.runAsync(runnable69);
            return true;
         } else {
            Player player27 = player10;
            Component component64 = pluginContext1.language().getMessage("error", "&cPlugin is still initializing. Please try again shortly.");
            player27.sendMessage(component64);
            return true;
         }
      } else {
         Component component80 = pluginContext1.language().getMessage("general.players-only");
         commandSender2.sendMessage(component80);
         return true;
      }
   }

   @NotNull
   @Override
   public String name() {
      return "profile";
   }

   @NotNull
   @Override
   public String permission() {
      return "glantielytratarget.profile";
   }

   @NotNull
   @Override
   public String description() {
      return "Open a player's anticheat profile";
   }
}
