package studio.glace.glantielytratarget.libs.nixlib.version;

import studio.glace.glantielytratarget.libs.nixlib.version.ServerType;
import studio.glace.glantielytratarget.libs.nixlib.version.ServerVersion;

public final class ServerCapabilities {
    private final ServerType type;
    private final ServerVersion version;

    public ServerCapabilities(ServerType serverType, ServerVersion serverVersion) {
        this.type = serverType;
        this.version = serverVersion;
    }

    public ServerType type() {
        return this.type;
    }

    public ServerVersion version() {
        return this.version;
    }

    public boolean supportsFolia() {
        return this.type.isMultithreaded();
    }

    public boolean supportsAdventure() {
        return this.type.isPaperBased();
    }

    public boolean supportsTextDisplays() {
        return this.version.isAtLeast(ServerVersion.V1_19_4);
    }

    public boolean supportsBlockDisplays() {
        return this.version.isAtLeast(ServerVersion.V1_19_4);
    }

    public boolean supportsItemComponentApi() {
        return this.version.isAtLeast(ServerVersion.V1_20_5);
    }

    public boolean supportsPersistentDataContainer() {
        return this.version.isAtLeast(ServerVersion.V1_16_5);
    }

    public boolean supportsModernSounds() {
        return this.version.isAtLeast(ServerVersion.V1_19);
    }

    public String toString() {
        return "ServerCapabilities{" + String.valueOf((Object)this.type) + " " + String.valueOf(this.version) + "}";
    }
}

