package studio.glace.glantielytratarget.libs.nixlib.database;

import studio.glace.glantielytratarget.libs.hikari.HikariConfig;
import studio.glace.glantielytratarget.libs.nixlib.database.HikariDatabase;

public final class MysqlDatabase
extends HikariDatabase {
    public MysqlDatabase(String string, int n, String string2, String string3, String string4) {
        super(MysqlDatabase.build(string, n, string2, string3, string4));
    }

    public MysqlDatabase(HikariConfig hikariConfig) {
        super(hikariConfig);
    }

    @Override
    public String dialect() {
        return "mysql";
    }

    private static HikariConfig build(String string, int n, String string2, String string3, String string4) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + string + ":" + n + "/" + string2 + "?useSSL=false&characterEncoding=UTF-8&connectTimeout=5000&socketTimeout=10000&tcpKeepAlive=true");
        hikariConfig.setUsername(string3);
        hikariConfig.setPassword(string4);
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setConnectionTimeout(5000L);
        hikariConfig.setValidationTimeout(2000L);
        hikariConfig.setIdleTimeout(300000L);
        hikariConfig.setPoolName("nixlib-mysql");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
        return hikariConfig;
    }
}

