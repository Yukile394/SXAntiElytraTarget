package studio.yukilem.yukilem.menu;

import java.lang.invoke.CallSite;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.config.LanguageService;
import studio.yukilem.yukilem.libs.nixlib.gui.ClickContext;
import studio.yukilem.yukilem.libs.nixlib.gui.Gui;
import studio.yukilem.yukilem.libs.nixlib.gui.GuiNavigation;
import studio.yukilem.yukilem.libs.nixlib.gui.PagedGui;
import studio.yukilem.yukilem.libs.nixlib.menu.ActionContext;
import studio.yukilem.yukilem.libs.nixlib.menu.ActionRegistry;
import studio.yukilem.yukilem.libs.nixlib.menu.ClickActions;
import studio.yukilem.yukilem.libs.nixlib.menu.MenuAction;
import studio.yukilem.yukilem.menu.ConfirmRequest;
import studio.yukilem.yukilem.menu.MenuAudio;
import studio.yukilem.yukilem.menu.MenuItemFactory;
import studio.yukilem.yukilem.menu.MenuManager;

public final class MenuActionService {
    public static final String PERM_TELEPORT = "glantielytratarget.profile.teleport";
    public static final String PERM_CLEAR = "glantielytratarget.profile.clear";
    public static final String PERM_VIOLATIONS = "glantielytratarget.profile.violations";
    private final PluginContext ctx;
    private final ActionRegistry registry;

    public MenuActionService(@NotNull PluginContext pluginContext) {
        ActionRegistry actionRegistry;
        this.ctx = pluginContext;
        this.registry = actionRegistry = ActionRegistry.createDefault(pluginContext.plugin());
        this.registerActions();
    }

    @NotNull
    public ActionRegistry registry() {
        return this.registry;
    }

    public void playSound(@NotNull Player player, @NotNull String string) {
        List<String> list = List.of("[sound] " + string);
        this.runActions(player, list);
    }

    public void runActions(@NotNull Player player, @NotNull List<String> list) {
        if (list.isEmpty()) {
            return;
        }
        ClickActions clickActions = ClickActions.parse(Map.of("all-click", list));
        ActionRegistry actionRegistry = this.registry;
        UnaryOperator<String> unaryOperator = UnaryOperator.identity();
        ActionContext actionContext = ActionContext.of(player, unaryOperator);
        clickActions.dispatch(actionRegistry, actionContext);
    }

    @NotNull
    public Consumer<ClickContext> staticHandler(@NotNull Map<String, Object> map, @NotNull Map<String, String> map2) {
        return this.entryHandler(map, map2);
    }

    @NotNull
    public Consumer<ClickContext> entryHandler(@NotNull Map<String, Object> map, @NotNull Map<String, String> map2) {
        ClickActions clickActions = ClickActions.parse(map.get("actions"));
        UnaryOperator<String> unaryOperator = MenuItemFactory.transformer(map2);
        ClickActions clickActions2 = clickActions;
        UnaryOperator<String> unaryOperator2 = unaryOperator;
        return clickContext -> {
            ActionRegistry actionRegistry = this.registry;
            ActionContext actionContext = ActionContext.of(clickContext, unaryOperator2);
            clickActions2.dispatch(actionRegistry, actionContext);
        };
    }

    private void registerActions() {
        ActionRegistry actionRegistry = this.registry;
        MenuAction menuAction = (actionContext, string) -> {
            Player player;
            Player player2 = player = actionContext.player();
            MenuAudio.suppressNextClose(player2.getUniqueId());
            Player player3 = player;
            GuiNavigation.suppressBack(player3.getUniqueId());
            Player player4 = player;
            player4.closeInventory();
        };
        ActionRegistry actionRegistry2 = actionRegistry.register("close", menuAction);
        ActionRegistry actionRegistry3 = this.registry;
        MenuAction menuAction2 = (actionContext, string) -> {
            if (this.ctx.menus() != null) {
                MenuManager menuManager = this.ctx.menus();
                Player player = actionContext.player();
                menuManager.refresh(player);
            }
        };
        ActionRegistry actionRegistry4 = actionRegistry3.register("refresh", menuAction2);
        ActionRegistry actionRegistry5 = this.registry;
        MenuAction menuAction3 = (actionContext, string) -> this.page(actionContext, 1);
        ActionRegistry actionRegistry6 = actionRegistry5.register("nextpage", menuAction3);
        ActionRegistry actionRegistry7 = this.registry;
        MenuAction menuAction4 = (actionContext, string) -> this.page(actionContext, -1);
        ActionRegistry actionRegistry8 = actionRegistry7.register("prevpage", menuAction4);
        ActionRegistry actionRegistry9 = this.registry;
        MenuAction menuAction5 = (actionContext, string) -> this.page(actionContext, -1);
        ActionRegistry actionRegistry10 = actionRegistry9.register("previouspage", menuAction5);
        ActionRegistry actionRegistry11 = this.registry;
        MenuAction menuAction6 = (actionContext, string) -> {
            if (this.ctx.menus() != null) {
                MenuManager menuManager = this.ctx.menus();
                Player player = actionContext.player();
                String string2 = string.trim();
                menuManager.openByName(player, string2);
            }
        };
        ActionRegistry actionRegistry12 = actionRegistry11.register("openguimenu", menuAction6);
        ActionRegistry actionRegistry13 = this.registry;
        MenuAction menuAction7 = (actionContext, string) -> {
            Player player = actionContext.player();
            this.openViolations(player);
        };
        ActionRegistry actionRegistry14 = actionRegistry13.register("aet_violations", menuAction7);
        ActionRegistry actionRegistry15 = this.registry;
        MenuAction menuAction8 = (actionContext, string) -> {
            Player player = actionContext.player();
            this.teleport(player);
        };
        ActionRegistry actionRegistry16 = actionRegistry15.register("aet_tp", menuAction8);
        ActionRegistry actionRegistry17 = this.registry;
        MenuAction menuAction9 = (actionContext, string) -> {
            Player player = actionContext.player();
            this.clearViolations(player);
        };
        ActionRegistry actionRegistry18 = actionRegistry17.register("aet_clearvl", menuAction9);
        ActionRegistry actionRegistry19 = this.registry;
        MenuAction menuAction10 = (actionContext, string) -> {
            Player player = actionContext.player();
            this.confirm(player);
        };
        ActionRegistry actionRegistry20 = actionRegistry19.register("aet_confirm", menuAction10);
        ActionRegistry actionRegistry21 = this.registry;
        MenuAction menuAction11 = (actionContext, string) -> {
            Player player = actionContext.player();
            this.cancel(player);
        };
        ActionRegistry actionRegistry22 = actionRegistry21.register("aet_cancel", menuAction11);
    }

    private void page(@NotNull ActionContext actionContext, int n) {
        PagedGui pagedGui;
        int n2;
        int n3;
        PagedGui pagedGui2;
        block5: {
            block4: {
                Gui gui = actionContext.gui();
                Gui gui2 = gui;
                if (!(gui2 instanceof PagedGui)) break block4;
                Gui gui3 = gui;
                pagedGui2 = (PagedGui)gui3;
                if (this.ctx.menus() != null) break block5;
            }
            return;
        }
        PagedGui pagedGui3 = pagedGui2;
        int n4 = pagedGui3.currentPage() + n;
        int n5 = n4;
        if (n5 < 0 || (n3 = n4) >= (n2 = (pagedGui = pagedGui2).totalPages())) {
            return;
        }
        MenuManager menuManager = this.ctx.menus();
        Player player = actionContext.player();
        int n6 = n4;
        menuManager.openViolations(player, n6);
    }

    private void openViolations(@NotNull Player player) {
        if (this.ctx.menus() == null) {
            return;
        }
        if (!player.hasPermission(PERM_VIOLATIONS)) {
            this.denyPermission(player);
            return;
        }
        this.ctx.menus().openViolationsRefreshed(player);
    }

    private void teleport(@NotNull Player player) {
        Player player2;
        if (this.ctx.menus() == null) {
            return;
        }
        if (!player.hasPermission(PERM_TELEPORT)) {
            this.denyPermission(player);
            return;
        }
        UUID uUID = this.ctx.menus().targetUuid(player);
        UUID uUID2 = uUID;
        if (uUID2 == null) {
            return;
        }
        UUID uUID3 = uUID;
        Player player3 = Bukkit.getPlayer(uUID3);
        Player player4 = player3;
        if (player4 == null || !(player2 = player3).isOnline()) {
            this.playSound(player, "BLOCK_NOTE_BLOCK_BASS 0.7 0.6");
            MenuAudio.suppressNextClose(player.getUniqueId());
            GuiNavigation.suppressBack(player.getUniqueId());
            player.closeInventory();
            LanguageService languageService = this.ctx.language();
            String string = this.nameOf(player);
            Map<String, String> map = Map.of("%player%", string);
            Component component = languageService.message("menu.teleport-offline", map);
            player.sendMessage(component);
            return;
        }
        MenuAudio.suppressNextClose(player.getUniqueId());
        GuiNavigation.suppressBack(player.getUniqueId());
        player.closeInventory();
        Player player5 = player3;
        Location location = player5.getLocation();
        boolean bl = player.teleport(location);
        this.playSound(player, "ENTITY_ENDERMAN_TELEPORT 1.0 1.0");
        LanguageService languageService = this.ctx.language();
        Player player6 = player3;
        String name = player6.getName();
        Map<String, String> map = Map.of("%player%", name);
        Component component = languageService.message("menu.teleported", map);
        player.sendMessage(component);
    }

    private void clearViolations(@NotNull Player player2) {
        ConfirmRequest confirmRequest;
        String string;
        String string2;
        if (this.ctx.menus() == null) {
            return;
        }
        if (!player2.hasPermission(PERM_CLEAR)) {
            this.denyPermission(player2);
            return;
        }
        UUID uUID = this.ctx.menus().targetUuid(player2);
        String string3 = this.ctx.menus().targetName(player2);
        UUID uUID2 = uUID;
        if (uUID2 == null || (string2 = string3) == null) {
            return;
        }
        String rawMessage = this.ctx.language().getRawMessage("menu.confirm-clear-vl");
        String string4 = string3;
        String string5 = string = rawMessage.replace("%player%", string4);
        UUID uUID3 = uUID;
        String string6 = string3;
        Consumer<Player> consumer = player -> this.performClear((Player)player, uUID3, string6);
        Consumer<Player> consumer2 = player -> {
            if (this.ctx.menus() != null) {
                this.ctx.menus().reopenProfile((Player)player);
            }
        };
        ConfirmRequest confirmRequest2 = confirmRequest = new ConfirmRequest(string5, consumer, consumer2);
        MenuManager menuManager = this.ctx.menus();
        ConfirmRequest confirmRequest3 = confirmRequest2;
        menuManager.openConfirm(player2, confirmRequest3);
    }

    private void performClear(@NotNull Player player, @NotNull UUID uUID, @NotNull String string) {
        if (this.ctx.warnings() != null) {
            this.ctx.warnings().clearViolations(uUID);
        }
        if (this.ctx.violationRepository() != null) {
            this.ctx.violationRepository().clear(uUID);
        }
        if (this.ctx.flagRepository() != null) {
            this.ctx.flagRepository().resetTotalViolations(uUID);
        }
        MenuAudio.suppressNextClose(player.getUniqueId());
        GuiNavigation.suppressBack(player.getUniqueId());
        player.closeInventory();
        this.playSound(player, "ENTITY_EXPERIENCE_ORB_PICKUP 0.5 1.5");
        LanguageService languageService = this.ctx.language();
        Map<String, String> map = Map.of("%player%", string);
        Component component = languageService.message("menu.violations-cleared", map);
        player.sendMessage(component);
    }

    private void confirm(@NotNull Player player) {
        if (this.ctx.menus() == null) {
            return;
        }
        ConfirmRequest confirmRequest = this.ctx.menus().confirmRequest(player);
        ConfirmRequest confirmRequest2 = confirmRequest;
        if (confirmRequest2 == null) {
            return;
        }
        ConfirmRequest confirmRequest3 = confirmRequest;
        confirmRequest3.onConfirm().accept(player);
    }

    private void cancel(@NotNull Player player) {
        if (this.ctx.menus() == null) {
            return;
        }
        ConfirmRequest confirmRequest = this.ctx.menus().confirmRequest(player);
        ConfirmRequest confirmRequest2 = confirmRequest;
        if (confirmRequest2 == null) {
            MenuAudio.suppressNextClose(player.getUniqueId());
            GuiNavigation.suppressBack(player.getUniqueId());
            player.closeInventory();
            return;
        }
        ConfirmRequest confirmRequest3 = confirmRequest;
        confirmRequest3.onCancel().accept(player);
    }

    private void denyPermission(@NotNull Player player) {
        this.playSound(player, "BLOCK_NOTE_BLOCK_BASS 0.7 0.6");
        Component message = this.ctx.language().getMessage("general.no-permission");
        player.sendMessage(message);
    }

    @NotNull
    private String nameOf(@NotNull Player player) {
        String string;
        String string2 = this.ctx.menus() == null ? null : this.ctx.menus().targetName(player);
        String string3 = string = string2;
        String string4 = string3 == null ? "?" : string;
        return string4;
    }

    @NotNull
    static Map<String, String> merge(@NotNull Map<String, String> map, @NotNull String string, @NotNull String string2) {
        HashMap<String, String> hashMap;
        HashMap<String, String> hashMap2 = hashMap = new HashMap<String, String>(map);
        String string3 = hashMap2.put(string, string2);
        HashMap<String, String> hashMap3 = hashMap;
        return hashMap3;
    }
}

