package studio.glace.glantielytratarget.libs.nixlib.config.migration;

import studio.glace.glantielytratarget.libs.nixlib.config.migration.MigrationContext;

@FunctionalInterface
public interface MigrationStep {
    public void migrate(MigrationContext migrationContext1);
}

