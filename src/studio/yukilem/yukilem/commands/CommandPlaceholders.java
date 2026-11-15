package studio.yukilem.yukilem.commands;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.config.settings.CommandSettings;

public final class CommandPlaceholders {
    private CommandPlaceholders() {
    }

    @NotNull
    public static Map<String, String> command(@NotNull PluginContext pluginContext) {
        CommandSettings commandSettings = pluginContext.commandSettings();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        CommandSettings commandSettings2 = commandSettings;
        if (commandSettings2 == null) {
            HashMap<String, String> hashMap2 = hashMap;
            String string = hashMap2.put("%cmd%", "glantielytratarget");
            HashMap<String, String> hashMap3 = hashMap;
            String string2 = hashMap3.put("%alerts%", "alerts");
            HashMap<String, String> hashMap4 = hashMap;
            String string3 = hashMap4.put("%reload%", "reload");
            HashMap<String, String> hashMap5 = hashMap;
            String string4 = hashMap5.put("%help%", "help");
            HashMap<String, String> hashMap6 = hashMap;
            String string5 = hashMap6.put("%profile%", "profile");
            HashMap<String, String> hashMap7 = hashMap;
            String string6 = hashMap7.put("%data%", "data");
            HashMap<String, String> hashMap8 = hashMap;
            String string7 = hashMap8.put("%reset%", "reset");
            HashMap<String, String> hashMap9 = hashMap;
            String string8 = hashMap9.put("%violations_type%", "violations");
            HashMap<String, String> hashMap10 = hashMap;
            String string9 = hashMap10.put("%total_violations_type%", "total_violations");
            HashMap<String, String> hashMap11 = hashMap;
            String string10 = hashMap11.put("%all%", "all");
            HashMap<String, String> hashMap12 = hashMap;
            String string11 = hashMap12.put("%all_alias%", "*");
            HashMap<String, String> hashMap13 = hashMap;
            String string12 = hashMap13.put("%confirm%", "confirm");
            HashMap<String, String> hashMap14 = hashMap;
            return hashMap14;
        }
        CommandSettings commandSettings3 = commandSettings;
        CommandSettings.DataSub dataSub = commandSettings3.dataSub();
        HashMap<String, String> hashMap15 = hashMap;
        CommandSettings commandSettings4 = commandSettings;
        String string = commandSettings4.name();
        String string13 = hashMap15.put("%cmd%", string);
        HashMap<String, String> hashMap16 = hashMap;
        CommandSettings commandSettings5 = commandSettings;
        String string14 = commandSettings5.alerts();
        String string15 = hashMap16.put("%alerts%", string14);
        HashMap<String, String> hashMap17 = hashMap;
        CommandSettings commandSettings6 = commandSettings;
        String string16 = commandSettings6.reload();
        String string17 = hashMap17.put("%reload%", string16);
        HashMap<String, String> hashMap18 = hashMap;
        CommandSettings commandSettings7 = commandSettings;
        String string18 = commandSettings7.help();
        String string19 = hashMap18.put("%help%", string18);
        HashMap<String, String> hashMap19 = hashMap;
        CommandSettings commandSettings8 = commandSettings;
        String string20 = commandSettings8.profile();
        String string21 = hashMap19.put("%profile%", string20);
        HashMap<String, String> hashMap20 = hashMap;
        CommandSettings commandSettings9 = commandSettings;
        String string22 = commandSettings9.data();
        String string23 = hashMap20.put("%data%", string22);
        HashMap<String, String> hashMap21 = hashMap;
        CommandSettings.DataSub dataSub2 = dataSub;
        String string24 = dataSub2.reset();
        String string25 = hashMap21.put("%reset%", string24);
        HashMap<String, String> hashMap22 = hashMap;
        CommandSettings.DataSub dataSub3 = dataSub;
        String string26 = dataSub3.violationsType();
        String string27 = hashMap22.put("%violations_type%", string26);
        HashMap<String, String> hashMap23 = hashMap;
        CommandSettings.DataSub dataSub4 = dataSub;
        String talViolationsType = dataSub4.totalViolationsType();
        String string28 = hashMap23.put("%total_violations_type%", talViolationsType);
        HashMap<String, String> hashMap24 = hashMap;
        CommandSettings.DataSub dataSub5 = dataSub;
        String string29 = dataSub5.all();
        String string30 = hashMap24.put("%all%", string29);
        HashMap<String, String> hashMap25 = hashMap;
        CommandSettings.DataSub dataSub6 = dataSub;
        String string31 = dataSub6.allAlias();
        String string32 = hashMap25.put("%all_alias%", string31);
        HashMap<String, String> hashMap26 = hashMap;
        CommandSettings.DataSub dataSub7 = dataSub;
        String string33 = dataSub7.confirm();
        String string34 = hashMap26.put("%confirm%", string33);
        HashMap<String, String> hashMap27 = hashMap;
        CommandSettings.DataSub dataSub8 = dataSub;
        String string35 = dataSub8.violationsType().replace('_', ' ');
        String string36 = hashMap27.put("%violations_type_label%", string35);
        HashMap<String, String> hashMap28 = hashMap;
        CommandSettings.DataSub dataSub9 = dataSub;
        String string37 = dataSub9.totalViolationsType().replace('_', ' ');
        String string38 = hashMap28.put("%total_violations_type_label%", string37);
        HashMap<String, String> hashMap29 = hashMap;
        return hashMap29;
    }
}

