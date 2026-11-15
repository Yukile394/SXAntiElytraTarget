package studio.yukilem.yukilem.menu;

import java.util.List;

public final class AetSounds {
    public static final String CLICK = "UI_BUTTON_CLICK 0.6 1.3";
    public static final String ERROR = "BLOCK_NOTE_BLOCK_BASS 0.7 0.6";
    public static final String SUCCESS = "ENTITY_EXPERIENCE_ORB_PICKUP 0.5 1.5";
    public static final String TELEPORT = "ENTITY_ENDERMAN_TELEPORT 1.0 1.0";
    public static final String OPEN = "BLOCK_ENDER_CHEST_OPEN 0.5 1.4";
    public static final String CLOSE = "BLOCK_ENDER_CHEST_CLOSE 0.5 1.2";
    public static final List<String> DEFAULT_OPEN_ACTIONS = List.of("[sound] BLOCK_ENDER_CHEST_OPEN 0.5 1.4");
    public static final List<String> DEFAULT_CLOSE_ACTIONS = List.of("[sound] BLOCK_ENDER_CHEST_CLOSE 0.5 1.2");

    private AetSounds() {
    }
}

