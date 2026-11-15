package studio.yukilem.yukilem.checks;

import lombok.Generated;

public enum CheckId {
    HOTBAR_ORDER("HotbarOrderCheck", "ElytraTargetH", Category.COMBAT),
    SEQUENCE("SequenceCheck", "ElytraTargetQ", Category.COMBAT),
    CONSISTENCY("ConsistencyCheck", "ElytraTargetC", Category.COMBAT),
    FW_INPUT("FWInputCheck", "ElytraTargetF", Category.COMBAT),
    SHOOT("ShootCheck", "ElytraTargetS", Category.COMBAT),
    PIVOT("PivotCheck", "ElytraTargetP", Category.COMBAT),
    COMBAT("CombatCheck", "ElytraTargetK", Category.HEURISTIC),
    ELYTRA_PATTERN("ElytraPatternCheck", "ElytraTargetE", Category.HEURISTIC);

    private final String displayName;
    private final String legacyName;
    private final Category category;

    public String displayName() {
        return this.displayName;
    }

    public String legacyName() {
        return this.legacyName;
    }

    /** Maps a pre-rename check name (e.g. "ShootCheck") to its current display name; other input is returned unchanged. */
    public static String normalize(String name) {
        if (name == null) {
            return null;
        }
        for (CheckId id : values()) {
            if (id.legacyName.equalsIgnoreCase(name)) {
                return id.displayName;
            }
        }
        return name;
    }

    public Category category() {
        return this.category;
    }

    public boolean isHeuristic() {
        return this.category == Category.HEURISTIC;
    }

    @Generated
    public String getDisplayName() {
        return this.displayName;
    }

    @Generated
    public Category getCategory() {
        return this.category;
    }

    @Generated
    private CheckId(String displayName, String legacyName, Category category) {
        this.displayName = displayName;
        this.legacyName = legacyName;
        this.category = category;
    }

    public enum Category {
        COMBAT,
        HEURISTIC
    }
}
