package studio.glace.glantielytratarget.libs.nixlib.version;

public enum ServerType {
    BUKKIT,
    SPIGOT,
    PAPER,
    FOLIA,
    CANVAS,
    UNKNOWN;


    public boolean isPaperBased() {
        return this == PAPER || this == FOLIA || this == CANVAS;
    }

    public boolean isMultithreaded() {
        return this == FOLIA || this == CANVAS;
    }

    public static ServerType detect() {
        if (ServerType.classPresent("io.canvasmc.canvas.Config")) {
            return CANVAS;
        }
        if (ServerType.classPresent("io.papermc.paper.threadedregions.RegionizedServer")) {
            return FOLIA;
        }
        if (ServerType.classPresent("com.destroystokyo.paper.PaperConfig") || ServerType.classPresent("io.papermc.paper.configuration.Configuration")) {
            return PAPER;
        }
        if (ServerType.classPresent("org.spigotmc.SpigotConfig")) {
            return SPIGOT;
        }
        if (ServerType.classPresent("org.bukkit.Bukkit")) {
            return BUKKIT;
        }
        return UNKNOWN;
    }

    private static boolean classPresent(String string) {
        try {
            Class.forName(string, false, ServerType.class.getClassLoader());
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }
}

