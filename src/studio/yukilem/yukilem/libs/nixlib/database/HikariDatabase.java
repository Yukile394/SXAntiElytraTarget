package studio.glace.glantielytratarget.libs.nixlib.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import studio.glace.glantielytratarget.libs.hikari.HikariConfig;
import studio.glace.glantielytratarget.libs.hikari.HikariDataSource;

public abstract class HikariDatabase implements Database {
   private static final Duration DEFAULT_CLOSE_TIMEOUT = Duration.ofSeconds(2L);
   protected final HikariDataSource ds;
   private final ExecutorService asyncExecutor;
   private final Set<CompletableFuture<?>> pending = ConcurrentHashMap.newKeySet();
   private final AtomicBoolean closed = new AtomicBoolean();
   private final AtomicBoolean dataSourceCloseStarted = new AtomicBoolean();
   private final CountDownLatch dataSourceClosed = new CountDownLatch(1);
   private final AtomicReference<CompletableFuture<Boolean>> asyncClose = new AtomicReference<>();
   private final String poolName;

   protected HikariDatabase(HikariConfig hikariConfig1) {
      this.ds = new HikariDataSource(hikariConfig1);
      this.poolName = hikariConfig1.getPoolName() == null ? "database" : hikariConfig1.getPoolName();
      int i2 = Math.max(1, Math.min(4, hikariConfig1.getMaximumPoolSize()));
      AtomicInteger atomicInteger3 = new AtomicInteger();
      this.asyncExecutor = Executors.newFixedThreadPool(i2, arg2x -> {
         Thread thread3x = new Thread(arg2x, "nixlib-db-" + this.poolName + "-" + atomicInteger3.incrementAndGet());
         thread3x.setDaemon(true);
         return thread3x;
      });
   }

   @Override
   public Connection borrow() throws SQLException {
      return this.ds.getConnection();
   }

   @Override
   public void execute(String string1, Object... arg2) {
      try (
         Connection connection3 = this.borrow();
         PreparedStatement preparedStatement4 = connection3.prepareStatement(string1);
      ) {
         bind(preparedStatement4, arg2);
         preparedStatement4.execute();
      } catch (SQLException sqlException11) {
         throw new DatabaseException("execute failed: " + string1, sqlException11);
      }
   }

   @Override
   public int update(String string1, Object... arg2) {
      try (
         Connection connection3 = this.borrow();
         PreparedStatement preparedStatement4 = connection3.prepareStatement(string1);
      ) {
         bind(preparedStatement4, arg2);
         return preparedStatement4.executeUpdate();
      } catch (SQLException sqlException11) {
         throw new DatabaseException("update failed: " + string1, sqlException11);
      }
   }

   @Override
   public <T> Optional<T> queryOne(String string1, RowMapper<T> rowMapper2, Object... arg3) {
      try (
         Connection connection4 = this.borrow();
         PreparedStatement preparedStatement5 = connection4.prepareStatement(string1);
      ) {
         bind(preparedStatement5, arg3);

         try (ResultSet resultSet6 = preparedStatement5.executeQuery()) {
            return resultSet6.next() ? Optional.ofNullable((T)rowMapper2.map(resultSet6)) : Optional.empty();
         }
      } catch (SQLException sqlException15) {
         throw new DatabaseException("queryOne failed: " + string1, sqlException15);
      }
   }

   @Override
   public <T> List<T> queryMany(String string1, RowMapper<T> rowMapper2, Object... arg3) {
      ArrayList arrayList4 = new ArrayList();

      try (
         Connection connection5 = this.borrow();
         PreparedStatement preparedStatement6 = connection5.prepareStatement(string1);
      ) {
         bind(preparedStatement6, arg3);

         try (ResultSet resultSet7 = preparedStatement6.executeQuery()) {
            while (resultSet7.next()) {
               arrayList4.add(rowMapper2.map(resultSet7));
            }
         }

         return arrayList4;
      } catch (SQLException sqlException16) {
         throw new DatabaseException("queryMany failed: " + string1, sqlException16);
      }
   }

   @Override
   public CompletableFuture<Void> executeAsync(String string1, Object... arg2) {
      return this.submitAsync(() -> {
         this.execute(string1, arg2);
         return null;
      });
   }

   @Override
   public CompletableFuture<Void> runAsync(Runnable runnable1) {
      return this.submitAsync(() -> {
         runnable1.run();
         return null;
      });
   }

   @Override
   public <T> CompletableFuture<Optional<T>> queryOneAsync(String string1, RowMapper<T> rowMapper2, Object... arg3) {
      return this.submitAsync(() -> this.queryOne(string1, rowMapper2, arg3));
   }

   @Override
   public <T> CompletableFuture<List<T>> queryManyAsync(String string1, RowMapper<T> rowMapper2, Object... arg3) {
      return this.submitAsync(() -> this.queryMany(string1, rowMapper2, arg3));
   }

   @Override
   public void close() {
      this.close(DEFAULT_CLOSE_TIMEOUT);
   }

   @Override
   public CompletableFuture<Boolean> closeAsync(Duration duration1) {
      CompletableFuture completableFuture2 = this.asyncClose.get();
      if (completableFuture2 != null) {
         return completableFuture2;
      }

      CompletableFuture completableFuture3 = new CompletableFuture();
      if (!this.asyncClose.compareAndSet(null, completableFuture3)) {
         return this.asyncClose.get();
      }

      Thread thread4 = new Thread(() -> {
         try {
            completableFuture3.complete(this.close(duration1));
         } catch (Throwable throwable4x) {
            completableFuture3.completeExceptionally(throwable4x);
         }
      }, "nixlib-db-shutdown");
      thread4.setDaemon(true);
      thread4.start();
      return completableFuture3;
   }

   @Override
   public boolean close(Duration duration1) {
      if (duration1.isNegative()) {
         throw new IllegalArgumentException("timeout must not be negative");
      }

      long l4 = deadlineAfter(duration1.toNanos());
      if (this.closed.compareAndSet(false, true)) {
         this.asyncExecutor.shutdown();
      }

      boolean flag6 = this.awaitExecutor(l4);
      if (!flag6) {
         this.asyncExecutor.shutdownNow();
         RejectedExecutionException rejectedExecutionException7 = new RejectedExecutionException("Database closed before queued work completed: " + this.poolName);
         Iterator iterator8 = this.pending.iterator();

         while (iterator8.hasNext()) {
            ((CompletableFuture)iterator8.next()).completeExceptionally(rejectedExecutionException7);
         }
      }

      this.startDataSourceClose();
      return flag6 && this.awaitDataSource(l4);
   }

   public boolean isClosed() {
      return this.closed.get();
   }

   private <T> CompletableFuture<T> submitAsync(Supplier<T> supplier1) {
      if (this.closed.get()) {
         return CompletableFuture.failedFuture(new RejectedExecutionException("Database is closed: " + this.poolName));
      }

      CompletableFuture completableFuture2 = new CompletableFuture();
      this.pending.add(completableFuture2);

      try {
         this.asyncExecutor.execute(() -> {
            try {
               completableFuture2.complete(supplier1.get());
            } catch (Throwable throwable7) {
               completableFuture2.completeExceptionally(throwable7);
            } finally {
               this.pending.remove(completableFuture2);
            }
         });
      } catch (RejectedExecutionException rejectedExecutionException4) {
         this.pending.remove(completableFuture2);
         completableFuture2.completeExceptionally(rejectedExecutionException4);
      }

      return completableFuture2;
   }

   private boolean awaitExecutor(long l1) {
      if (this.asyncExecutor.isTerminated()) {
         return true;
      }

      long l3 = remainingNanos(l1);
      if (l3 <= 0L) {
         return false;
      }

      try {
         return this.asyncExecutor.awaitTermination(l3, TimeUnit.NANOSECONDS);
      } catch (InterruptedException interruptedException6) {
         Thread.currentThread().interrupt();
         return false;
      }
   }

   private void startDataSourceClose() {
      if (this.dataSourceCloseStarted.compareAndSet(false, true)) {
         Thread thread1 = new Thread(() -> {
            try {
               this.ds.close();
            } finally {
               this.dataSourceClosed.countDown();
            }
         }, "nixlib-db-close");
         thread1.setDaemon(true);
         thread1.start();
      }
   }

   private boolean awaitDataSource(long l1) {
      if (this.dataSourceClosed.getCount() == 0L) {
         return true;
      }

      long l3 = remainingNanos(l1);
      if (l3 <= 0L) {
         return false;
      }

      try {
         return this.dataSourceClosed.await(l3, TimeUnit.NANOSECONDS);
      } catch (InterruptedException interruptedException6) {
         Thread.currentThread().interrupt();
         return false;
      }
   }

   private static long deadlineAfter(long l0) {
      long l2 = System.nanoTime();
      long l4 = l2 + l0;
      return l4 < l2 ? Long.MAX_VALUE : l4;
   }

   private static long remainingNanos(long l0) {
      return l0 == Long.MAX_VALUE ? Long.MAX_VALUE : Math.max(0L, l0 - System.nanoTime());
   }

   private static void bind(PreparedStatement preparedStatement0, Object[] objects1) throws SQLException {
      for (int i2 = 0; i2 < objects1.length; i2++) {
         preparedStatement0.setObject(i2 + 1, objects1[i2]);
      }
   }
}
