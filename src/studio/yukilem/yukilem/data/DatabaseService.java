package studio.yukilem.yukilem.data;

import java.io.File;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.config.settings.DatabaseSettings;
import studio.yukilem.yukilem.libs.nixlib.database.Database;
import studio.yukilem.yukilem.libs.nixlib.database.MysqlDatabase;
import studio.yukilem.yukilem.libs.nixlib.database.SqliteDatabase;

public final class DatabaseService {
    private DatabaseService() {
    }

    @NotNull
    public static Database create(@NotNull DatabaseSettings databaseSettings, @NotNull File file) {
        Path path;
        if (databaseSettings.isMysql()) {
            String string = databaseSettings.mysqlHost();
            int n = databaseSettings.mysqlPort();
            String string2 = databaseSettings.mysqlDatabase();
            String string3 = databaseSettings.mysqlUsername();
            String string4 = databaseSettings.mysqlPassword();
            MysqlDatabase mysqlDatabase = new MysqlDatabase(string, n, string2, string3, string4);
            return mysqlDatabase;
        }
        Path path2 = file.toPath();
        String string = databaseSettings.sqliteFile();
        Path path3 = path = path2.resolve(string);
        SqliteDatabase sqliteDatabase = new SqliteDatabase(path3);
        return sqliteDatabase;
    }
}

