package studio.glace.glantielytratarget.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.commands.CommandPlaceholders;
import studio.glace.glantielytratarget.config.LanguageService;

public final class CommandMessages {
    private CommandMessages() {
    }

    public static void incorrectArgument(@NotNull PluginContext pluginContext, @NotNull CommandSender commandSender, @NotNull String string) {
        HashMap<String, String> hashMap;
        HashMap<String, String> hashMap2;
        Map<String, String> map;
        Map<String, String> map2 = map = CommandPlaceholders.command(pluginContext);
        HashMap<String, String> hashMap3 = hashMap2 = (hashMap = new HashMap<String, String>(map2));
        Map<String, String> map3 = map;
        String string2 = CommandMessages.resolveUsage(pluginContext, string, map3);
        String string3 = hashMap3.put("%usage%", string2);
        LanguageService languageService = pluginContext.language();
        HashMap<String, String> hashMap4 = hashMap2;
        Component component = languageService.message("general.wrong-args", hashMap4);
        commandSender.sendMessage(component);
    }

    @NotNull
    private static String resolveUsage(@NotNull PluginContext pluginContext, @NotNull String string, @NotNull Map<String, String> map) {
        Iterator<Map.Entry<String, String>> iterator;
        String rawMessage = pluginContext.language().getRawMessage(string);
        Iterator<Map.Entry<String, String>> iterator2 = map.entrySet().iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator<Map.Entry<String, String>> iterator3 = iterator2;
            Map.Entry<String, String> entry = iterator3.next();
            String string2 = rawMessage;
            Map.Entry<String, String> entry2 = entry;
            CharSequence key = entry2.getKey();
            Map.Entry<String, String> entry3 = entry;
            CharSequence value = entry3.getValue();
            rawMessage = string2.replace(key, value);
        }
        String string3 = rawMessage;
        return string3;
    }
}

