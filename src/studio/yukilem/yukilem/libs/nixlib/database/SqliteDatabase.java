package studio.yukilem.yukilem.libs.nixlib.database;

import java.nio.file.Path;
import com.zaxxer.hikari.HikariConfig;
import studio.yukilem.yukilem.libs.nixlib.database.HikariDatabase;

public final class SqliteDatabase
extends HikariDatabase {
    public SqliteDatabase(Path path) {
        super(SqliteDatabase.buildConfig(path));
    }

    public SqliteDatabase(String string) {
        super(SqliteDatabase.buildConfig(string));
    }

    @Override
    public String dialect() {
        return "sqlite";
    }

    private static HikariConfig buildConfig(Path path) {
        return SqliteDatabase.buildConfig("jdbc:sqlite:" + String.valueOf(path.toAbsolutePath()));
    }

    private static HikariConfig buildConfig(String string) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(string);
        hikariConfig.setDriverClassName("org.sqlite.JDBC");
        hikariConfig.setMaximumPoolSize(1);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName("nixlib-sqlite");
        return hikariConfig;
    }
}

