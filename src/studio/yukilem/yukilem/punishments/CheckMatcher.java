package studio.glace.glantielytratarget.punishments;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public final class CheckMatcher {
    private final boolean matchAll;
    private final Set<String> includes;
    private final Set<String> excludes;

    private CheckMatcher(boolean bl, @NotNull Set<String> set, @NotNull Set<String> set2) {
        this.matchAll = bl;
        this.includes = set;
        this.excludes = set2;
    }

    @NotNull
    public static CheckMatcher parse(@NotNull List<String> list) {
        Iterator<String> iterator;
        boolean bl = false;
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        LinkedHashSet<String> linkedHashSet2 = new LinkedHashSet<String>();
        Iterator<String> iterator2 = list.iterator();
        while ((iterator = iterator2).hasNext()) {
            String string;
            String string2;
            String string3;
            Iterator<String> iterator3 = iterator2;
            String string4 = iterator3.next();
            if (string4 != null && !string4.trim().startsWith("!")) {
                string4 = studio.glace.glantielytratarget.checks.CheckId.normalize(string4.trim());
            } else if (string4 != null) {
                string4 = "!" + studio.glace.glantielytratarget.checks.CheckId.normalize(string4.trim().substring(1).trim());
            }
            String string5 = string4;
            if (string5 == null || (string3 = (string2 = (string = string4).trim())).isEmpty()) continue;
            String string6 = string2;
            if ("*".equals(string6)) {
                bl = true;
                continue;
            }
            String string7 = string2;
            if (string7.startsWith("!")) {
                String string8 = string2;
                String string9 = string8.substring(1).trim();
                String string10 = string9;
                if (string10.isEmpty()) continue;
                LinkedHashSet<String> linkedHashSet3 = linkedHashSet2;
                String string11 = string9;
                boolean bl2 = linkedHashSet3.add(string11);
                continue;
            }
            LinkedHashSet<String> linkedHashSet4 = linkedHashSet;
            String string12 = string2;
            boolean bl3 = linkedHashSet4.add(string12);
        }
        boolean bl4 = bl;
        LinkedHashSet<String> linkedHashSet5 = linkedHashSet;
        Set<String> set = Collections.unmodifiableSet(linkedHashSet5);
        LinkedHashSet<String> linkedHashSet6 = linkedHashSet2;
        Set<String> set2 = Collections.unmodifiableSet(linkedHashSet6);
        CheckMatcher checkMatcher = new CheckMatcher(bl4, set, set2);
        return checkMatcher;
    }

    public boolean matches(@NotNull String string) {
        string = studio.glace.glantielytratarget.checks.CheckId.normalize(string);
        if (this.excludes.contains(string)) {
            return false;
        }
        if (this.matchAll) {
            return true;
        }
        return this.includes.contains(string);
    }

    public boolean isMatchAll() {
        return this.matchAll;
    }

    @NotNull
    public Set<String> includes() {
        return this.includes;
    }

    @NotNull
    public Set<String> excludes() {
        return this.excludes;
    }
}

