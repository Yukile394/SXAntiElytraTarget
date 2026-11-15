package studio.glace.glantielytratarget.config.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.config.ConfigView;

public record CommandSettings(@NotNull String name, @NotNull List<String> aliases, @NotNull String alerts, @NotNull String reload, @NotNull String help, @NotNull String profile, @NotNull String data, @NotNull DataSub dataSub) {
    public static final String DATA_TYPE_VIOLATIONS = "violations";
    public static final String DATA_TYPE_TOTAL = "total_violations";

    @NotNull
    public static CommandSettings load(@NotNull ConfigView configView) {
        DataSub dataSub;
        String string = CommandSettings.word(configView, "commands.data-sub.reset", "reset");
        String string2 = CommandSettings.word(configView, "commands.data-sub.violations-type", DATA_TYPE_VIOLATIONS);
        String string3 = CommandSettings.word(configView, "commands.data-sub.total-violations-type", DATA_TYPE_TOTAL);
        String string4 = CommandSettings.word(configView, "commands.data-sub.all", "all");
        String string5 = CommandSettings.word(configView, "commands.data-sub.all-alias", "*");
        String string6 = CommandSettings.word(configView, "commands.data-sub.confirm", "confirm");
        DataSub dataSub2 = dataSub = new DataSub(string, string2, string3, string4, string5, string6);
        String string7 = configView.getString("commands.name", "glantielytratarget");
        List<String> list = List.of("aet", "elytratargetac");
        List<String> list2 = CommandSettings.list(configView, "commands.aliases", list);
        String string8 = CommandSettings.word(configView, "commands.alerts", "alerts");
        String string9 = CommandSettings.word(configView, "commands.reload", "reload");
        String string10 = CommandSettings.word(configView, "commands.help", "help");
        String string11 = CommandSettings.word(configView, "commands.profile", "profile");
        String string12 = CommandSettings.word(configView, "commands.data", "data");
        DataSub dataSub3 = dataSub2;
        CommandSettings commandSettings = new CommandSettings(string7, list2, string8, string9, string10, string11, string12, dataSub3);
        return commandSettings;
    }

    public boolean isDataReset(@NotNull String string) {
        return this.dataSub.reset().equalsIgnoreCase(string);
    }

    public boolean isAllTarget(@NotNull String string) {
        boolean bl = this.dataSub.all().equalsIgnoreCase(string) || this.dataSub.allAlias().equalsIgnoreCase(string);
        return bl;
    }

    public boolean isDataConfirm(@NotNull String string) {
        return this.dataSub.confirm().equalsIgnoreCase(string);
    }

    @Nullable
    public String canonicalDataType(@NotNull String string) {
        if (this.dataSub.violationsType().equalsIgnoreCase(string)) {
            return DATA_TYPE_VIOLATIONS;
        }
        if (this.dataSub.totalViolationsType().equalsIgnoreCase(string)) {
            return DATA_TYPE_TOTAL;
        }
        return null;
    }

    @NotNull
    public List<String> dataTypeSuggestions() {
        String string = this.dataSub.violationsType();
        Locale locale = Locale.ROOT;
        String lowerCase = string.toLowerCase(locale);
        String talViolationsType = this.dataSub.totalViolationsType();
        Locale locale2 = Locale.ROOT;
        String lowerCase2 = talViolationsType.toLowerCase(locale2);
        return List.of(lowerCase, lowerCase2);
    }

    @NotNull
    public List<String> allTargetSuggestions() {
        ArrayList<String> arrayList;
        ArrayList<String> arrayList2 = arrayList = new ArrayList<String>(2);
        String string = this.dataSub.all();
        Locale locale = Locale.ROOT;
        String lowerCase = string.toLowerCase(locale);
        boolean bl = arrayList2.add(lowerCase);
        if (!this.dataSub.allAlias().isBlank()) {
            ArrayList<String> arrayList3 = arrayList;
            String string2 = this.dataSub.allAlias();
            boolean bl2 = arrayList3.add(string2);
        }
        ArrayList<String> arrayList4 = arrayList;
        return arrayList4;
    }

    @NotNull
    private static String word(@NotNull ConfigView configView, @NotNull String string, @NotNull String string2) {
        String string3 = configView.getString(string, string2);
        String string4 = string3;
        String string5 = string4.isBlank() ? string2 : string3;
        return string5;
    }

    @NotNull
    private static List<String> list(@NotNull ConfigView configView, @NotNull String string, @NotNull List<String> list) {
        List<String> stringList = configView.getStringList(string);
        List<String> list2 = stringList;
        List<String> list3 = list2.isEmpty() ? list : stringList;
        return list3;
    }

    public record DataSub(@NotNull String reset, @NotNull String violationsType, @NotNull String totalViolationsType, @NotNull String all, @NotNull String allAlias, @NotNull String confirm) {
    }
}

