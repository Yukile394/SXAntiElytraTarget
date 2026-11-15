package studio.yukilem.yukilem.libs.nixlib.config;

public class ConfigException
extends RuntimeException {
    public ConfigException(String string) {
        super(string);
    }

    public ConfigException(String string, Throwable e) {
        super(string, e);
    }
}

