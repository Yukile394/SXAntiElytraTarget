package studio.glace.glantielytratarget.libs.nixlib.database;

public class DatabaseException
extends RuntimeException {
    public DatabaseException(String string, Throwable e) {
        super(string, e);
    }

    public DatabaseException(String string) {
        super(string);
    }
}

