package studio.yukilem.yukilem.libs.nixlib.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import studio.yukilem.yukilem.libs.nixlib.database.RowMapper;

public interface Database
extends AutoCloseable {
    public Connection borrow() throws SQLException;

    public void execute(String string1, Object ... rowMapper2);

    public int update(String string1, Object ... rowMapper2);

    public <T> Optional<T> queryOne(String string1, RowMapper<T> rowMapper2, Object ... arg3);

    public <T> List<T> queryMany(String string1, RowMapper<T> rowMapper2, Object ... arg3);

    public CompletableFuture<Void> executeAsync(String string1, Object ... rowMapper2);

    default public CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable);
    }

    default public <T> CompletableFuture<Optional<T>> queryOneAsync(String string, RowMapper<T> rowMapper, Object ... objectArray) {
        return CompletableFuture.supplyAsync(() -> this.queryOne(string, rowMapper, objectArray));
    }

    public <T> CompletableFuture<List<T>> queryManyAsync(String string1, RowMapper<T> rowMapper2, Object ... arg3);

    public String dialect();

    default public CompletableFuture<Boolean> closeAsync(Duration duration) {
        return CompletableFuture.supplyAsync(() -> this.close(duration));
    }

    default public boolean close(Duration duration) {
        if (duration.isNegative()) {
            throw new IllegalArgumentException("timeout must not be negative");
        }
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(this::close);
        try {
            completableFuture.get(duration.toNanos(), TimeUnit.NANOSECONDS);
            return true;
        }
        catch (TimeoutException e) {
            return false;
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        catch (ExecutionException e) {
            throw new CompletionException(e.getCause());
        }
    }

    @Override
    public void close();
}

