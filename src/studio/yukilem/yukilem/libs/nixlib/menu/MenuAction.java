package studio.yukilem.yukilem.libs.nixlib.menu;

import studio.yukilem.yukilem.libs.nixlib.menu.ActionContext;

@FunctionalInterface
public interface MenuAction {
    public void run(ActionContext actionContext1, String string2);
}

