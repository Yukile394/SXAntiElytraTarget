package studio.yukilem.yukilem.libs.nixlib.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.libs.nixlib.gui.ClickContext;
import studio.yukilem.yukilem.libs.nixlib.menu.ActionContext;
import studio.yukilem.yukilem.libs.nixlib.menu.ActionRegistry;

public final class ClickActions {
    private final List<String> all;
    private final List<String> left;
    private final List<String> right;
    private final List<String> shiftLeft;
    private final List<String> shiftRight;
    private final List<String> middle;

    private ClickActions(List<String> list, List<String> list2, List<String> list3, List<String> list4, List<String> list5, List<String> list6) {
        this.all = list;
        this.left = list2;
        this.right = list3;
        this.shiftLeft = list4;
        this.shiftRight = list5;
        this.middle = list6;
    }

    @NotNull
    public static ClickActions parse(@Nullable Object object) {
        if (!(object instanceof Map)) {
            return new ClickActions(List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
        }
        Map map = (Map)object;
        Map map2 = map;
        return new ClickActions(ClickActions.lines(map2, "all-click", "all_click", "all", "click"), ClickActions.lines(map2, "left-click", "left_click", "left"), ClickActions.lines(map2, "right-click", "right_click", "right"), ClickActions.lines(map2, "shift-left-click", "shift_left_click", "shift-left", "shiftleft"), ClickActions.lines(map2, "shift-right-click", "shift_right_click", "shift-right", "shiftright"), ClickActions.lines(map2, "middle-click", "middle_click", "middle"));
    }

    public boolean isEmpty() {
        return this.all.isEmpty() && this.left.isEmpty() && this.right.isEmpty() && this.shiftLeft.isEmpty() && this.shiftRight.isEmpty() && this.middle.isEmpty();
    }

    @NotNull
    public Consumer<ClickContext> toHandler(@NotNull ActionRegistry actionRegistry, @Nullable UnaryOperator<String> unaryOperator) {
        return clickContext -> this.dispatch(actionRegistry, ActionContext.of(clickContext, unaryOperator));
    }

    public void dispatch(@NotNull ActionRegistry actionRegistry, @NotNull ActionContext actionContext) {
        ClickType clickType;
        actionRegistry.runAll(this.all, actionContext);
        ClickType clickType2 = clickType = actionContext.click() == null ? null : actionContext.click().clickType();
        if (clickType == null) {
            return;
        }
        switch (clickType) {
            case LEFT: {
                actionRegistry.runAll(this.left, actionContext);
                break;
            }
            case RIGHT: {
                actionRegistry.runAll(this.right, actionContext);
                break;
            }
            case SHIFT_LEFT: {
                actionRegistry.runAll(this.shiftLeft, actionContext);
                break;
            }
            case SHIFT_RIGHT: {
                actionRegistry.runAll(this.shiftRight, actionContext);
                break;
            }
            case MIDDLE: {
                actionRegistry.runAll(this.middle, actionContext);
            }
        }
    }

    private static List<String> lines(Map<String, Object> map, String ... stringArray) {
        for (String string : stringArray) {
            if (map.containsKey(string)) {
                return ClickActions.asList(map.get(string));
            }
            String lowerCase = string.toLowerCase(Locale.ROOT);
            if (lowerCase.equals(string) || !map.containsKey(lowerCase)) continue;
            return ClickActions.asList(map.get(lowerCase));
        }
        return List.of();
    }

    private static List<String> asList(Object object) {
        if (object instanceof List) {
            List list = (List)object;
            ArrayList<String> arrayList = new ArrayList<String>(list.size());
            for (Object e : list) {
                if (e == null) continue;
                arrayList.add(String.valueOf(e));
            }
            return arrayList;
        }
        if (object instanceof String) {
            String string = (String)object;
            return List.of(string);
        }
        return List.of();
    }
}

