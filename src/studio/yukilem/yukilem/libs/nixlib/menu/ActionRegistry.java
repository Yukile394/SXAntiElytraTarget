package studio.glace.glantielytratarget.libs.nixlib.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.libs.nixlib.menu.ActionContext;
import studio.glace.glantielytratarget.libs.nixlib.menu.DefaultActions;
import studio.glace.glantielytratarget.libs.nixlib.menu.MenuAction;
import studio.glace.glantielytratarget.libs.nixlib.menu.Warnings;

public final class ActionRegistry {
    private final Map<String, MenuAction> actions = new HashMap<String, MenuAction>();

    @NotNull
    public ActionRegistry register(@NotNull String string, @NotNull MenuAction menuAction) {
        this.actions.put(ActionRegistry.normalize(string), menuAction);
        return this;
    }

    public boolean has(@NotNull String string) {
        return this.actions.containsKey(ActionRegistry.normalize(string));
    }

    public void runAll(@NotNull List<String> list, @NotNull ActionContext actionContext) {
        for (String string : list) {
            this.run(string, actionContext);
        }
    }

    public void run(@NotNull String string, @NotNull ActionContext actionContext) {
        String string2 = actionContext.apply(string);
        if (string2 == null) {
            return;
        }
        if ((string2 = string2.trim()).isEmpty() || string2.charAt(0) != '[') {
            return;
        }
        int n = string2.indexOf(93);
        if (n < 1) {
            return;
        }
        String string3 = ActionRegistry.normalize(string2.substring(1, n));
        String string4 = n + 1 < string2.length() ? string2.substring(n + 1).trim() : "";
        MenuAction menuAction = this.actions.get(string3);
        if (menuAction == null) {
            Warnings.once("action:" + string3, "Unknown menu action [" + string3 + "] in configuration.");
            return;
        }
        try {
            menuAction.run(actionContext, string4);
        }
        catch (Throwable e) {
            Warnings.once("action-error:" + string3, "Menu action [" + string3 + "] failed: " + e.getMessage());
        }
    }

    @NotNull
    public static ActionRegistry createDefault(@NotNull Plugin plugin) {
        ActionRegistry actionRegistry = new ActionRegistry();
        DefaultActions.install(actionRegistry, plugin);
        return actionRegistry;
    }

    private static String normalize(String string) {
        return string.trim().toLowerCase(Locale.ROOT).replace("[", "").replace("]", "");
    }
}

