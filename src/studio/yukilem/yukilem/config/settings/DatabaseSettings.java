package studio.yukilem.yukilem.config.settings;

import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.config.ConfigView;

public record DatabaseSettings(@NotNull String type, @NotNull String sqliteFile, @NotNull String mysqlHost, int mysqlPort, @NotNull String mysqlDatabase, @NotNull String mysqlUsername, @NotNull String mysqlPassword) {
    @NotNull
    public static DatabaseSettings load(@NotNull ConfigView configView) {
        String string = configView.getString("database.type", "sqlite");
        String string2 = configView.getString("database.sqlite.file", "data.db");
        String string3 = configView.getString("database.mysql.host", "localhost");
        int intVal = configView.getInt("database.mysql.port", 3306);
        String string4 = configView.getString("database.mysql.database", "glantielytratarget");
        String string5 = configView.getString("database.mysql.username", "root");
        String string6 = configView.getString("database.mysql.password", "");
        DatabaseSettings databaseSettings = new DatabaseSettings(string, string2, string3, intVal, string4, string5, string6);
        return databaseSettings;
    }

    public boolean isMysql() {
        String string = this.type;
        return "mysql".equalsIgnoreCase(string);
    }
}

