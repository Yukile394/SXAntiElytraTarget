package studio.glace.glantielytratarget.platform.region;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.checks.CheckId;
import studio.glace.glantielytratarget.config.ConfigView;

record RegionProtectionSettings(boolean enabled, Set<String> disabledRegions, EnumSet<CheckId> disabledRegionChecks, boolean pvpDisabledRegionsEnabled, EnumSet<CheckId> pvpDisabledRegionChecks) {
    private static final String ROOT = "detection.worldguard.";

    @NotNull
    static RegionProtectionSettings load(@NotNull ConfigView configView, @NotNull Consumer<String> consumer) {
        Iterator<String> iterator;
        HashSet<String> hashSet = new HashSet<String>();
        Iterator<String> iterator2 = configView.getStringList("detection.worldguard.disabled-regions").iterator();
        while ((iterator = iterator2).hasNext()) {
            Locale locale;
            Iterator<String> iterator3 = iterator2;
            String string = iterator3.next().trim();
            String lowerCase = string.toLowerCase(locale = Locale.ROOT);
            String string2 = lowerCase;
            if (string2.isEmpty()) continue;
            HashSet<String> hashSet2 = hashSet;
            String string3 = lowerCase;
            boolean bl = hashSet2.add(string3);
        }
        boolean boolean_ = configView.getBoolean("detection.worldguard.enabled", true);
        HashSet<String> hashSet3 = hashSet;
        Set<String> set = Set.copyOf(hashSet3);
        EnumSet<CheckId> enumSet = RegionProtectionSettings.checks(configView.getStringList("detection.worldguard.disabled-checks"), consumer);
        boolean boolean_2 = configView.getBoolean("detection.worldguard.pvp-disabled-regions.enabled", true);
        EnumSet<CheckId> enumSet2 = RegionProtectionSettings.checks(configView.getStringList("detection.worldguard.pvp-disabled-regions.disabled-checks"), consumer);
        RegionProtectionSettings regionProtectionSettings = new RegionProtectionSettings(boolean_, set, enumSet, boolean_2, enumSet2);
        return regionProtectionSettings;
    }

    @NotNull
    EnumSet<CheckId> disabledChecks(boolean bl, boolean bl2) {
        EnumSet<CheckId> enumSet = EnumSet.noneOf(CheckId.class);
        if (this.enabled && bl) {
            EnumSet<CheckId> enumSet2 = enumSet;
            EnumSet<CheckId> enumSet3 = this.disabledRegionChecks;
            boolean bl3 = enumSet2.addAll(enumSet3);
        }
        if (bl2 && this.pvpDisabledRegionsEnabled) {
            EnumSet<CheckId> enumSet4 = enumSet;
            EnumSet<CheckId> enumSet5 = this.pvpDisabledRegionChecks;
            boolean bl4 = enumSet4.addAll(enumSet5);
        }
        EnumSet<CheckId> enumSet6 = enumSet;
        return enumSet6;
    }

    boolean anyGateEnabled() {
        boolean bl = this.enabled || this.pvpDisabledRegionsEnabled;
        return bl;
    }

    boolean matchesRegion(@NotNull String string, @NotNull String string2) {
        String string3;
        Locale locale;
        String string4;
        String string5;
        Set<String> set;
        Set<String> set2 = this.disabledRegions;
        Locale locale2 = Locale.ROOT;
        String lowerCase = string2.toLowerCase(locale2);
        String string6 = lowerCase;
        boolean bl = set2.contains(string6) || (set = this.disabledRegions).contains(string5 = (string4 = string.toLowerCase(locale = Locale.ROOT)) + ":" + (string3 = lowerCase));
        return bl;
    }

    @NotNull
    private static EnumSet<CheckId> checks(@NotNull List<String> list, @NotNull Consumer<String> consumer) {
        Iterator<String> iterator;
        EnumSet<CheckId> enumSet = EnumSet.noneOf(CheckId.class);
        Iterator<String> iterator2 = list.iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator<String> iterator3 = iterator2;
            String string = iterator3.next().trim();
            String string2 = string;
            if (string2.equalsIgnoreCase("ALL")) {
                EnumSet<CheckId> enumSet2 = enumSet;
                EnumSet<CheckId> enumSet3 = EnumSet.allOf(CheckId.class);
                boolean bl = enumSet2.addAll(enumSet3);
                continue;
            }
            String string3 = string;
            CheckId checkId = RegionProtectionSettings.findCheck(string3);
            CheckId checkId2 = checkId;
            if (checkId2 == null) {
                String string4 = string;
                if (string4.isEmpty()) continue;
                String string5 = string;
                String string6 = "Unknown WorldGuard disabled check: " + string5;
                consumer.accept(string6);
                continue;
            }
            EnumSet<CheckId> enumSet4 = enumSet;
            CheckId checkId3 = checkId;
            boolean bl = enumSet4.add(checkId3);
        }
        EnumSet<CheckId> enumSet5 = enumSet;
        return enumSet5;
    }

    private static CheckId findCheck(@NotNull String string) {
        CheckId[] checkIdArray;
        CheckId[] checkIdArray2 = checkIdArray = CheckId.values();
        int length = checkIdArray2.length;
        int n = 0;
        while (true) {
            int n2;
            int n3;
            if ((n3 = n) < (n2 = length)) {
                CheckId checkId;
                int n4;
                CheckId checkId2;
                CheckId checkId3;
                CheckId[] checkIdArray3 = checkIdArray;
                if (!(checkId3 = (checkId2 = checkIdArray3[n4 = n++])).displayName().equalsIgnoreCase(string) && !checkId2.legacyName().equalsIgnoreCase(string) && !(checkId = checkId2).name().equalsIgnoreCase(string)) continue;
                CheckId checkId4 = checkId2;
                return checkId4;
            }
            return null;
        }
    }
}

