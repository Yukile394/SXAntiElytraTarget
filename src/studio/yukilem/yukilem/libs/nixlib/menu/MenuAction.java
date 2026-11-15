package studio.glace.glantielytratarget.libs.nixlib.menu;

import studio.glace.glantielytratarget.libs.nixlib.menu.ActionContext;

@FunctionalInterface
public interface MenuAction {
    public void run(ActionContext actionContext1, String string2);
}

