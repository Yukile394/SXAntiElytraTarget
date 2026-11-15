package studio.yukilem.yukilem.libs.nixlib.config.migration;

import studio.yukilem.yukilem.libs.nixlib.config.migration.MigrationContext;

@FunctionalInterface
public interface MigrationStep {
    public void migrate(MigrationContext migrationContext1);
}

