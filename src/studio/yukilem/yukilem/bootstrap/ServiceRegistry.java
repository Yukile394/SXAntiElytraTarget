package studio.glace.glantielytratarget.bootstrap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;

public final class ServiceRegistry {
    private static final long BOUNDED_SHUTDOWN_TIMEOUT_MILLIS = 250L;
    private final Logger logger;
    private final List<Entry> entries;

    public ServiceRegistry(@NotNull Logger logger) {
        ArrayList<Entry> arrayList = new ArrayList<Entry>();
        this.entries = arrayList;
        this.logger = logger;
    }

    @NotNull
    public ServiceRegistry register(@NotNull String string, @NotNull Runnable runnable) {
        long l = 0L;
        this.add(string, runnable, l, false);
        return this;
    }

    @NotNull
    public ServiceRegistry registerBounded(@NotNull String string, @NotNull Runnable runnable) {
        long l = 250L;
        this.add(string, runnable, l, false);
        return this;
    }

    @NotNull
    public ServiceRegistry registerMainThread(@NotNull String string, @NotNull Runnable runnable) {
        long l = 0L;
        this.add(string, runnable, l, true);
        return this;
    }

    public void shutdownMainThread() {
        List<Entry> list;
        ArrayList<Entry> arrayList = new ArrayList<Entry>();
        List<Entry> list2 = list = this.entries;
        synchronized (list) {
            try {
                List<Entry> list3 = this.entries;
                ArrayList<Entry> arrayList2 = arrayList;
                Predicate<Entry> predicate = entry -> {
                    if (!entry.mainThread) {
                        return false;
                    }
                    boolean bl = arrayList2.add((Entry)entry);
                    return true;
                };
                boolean bl = list3.removeIf(predicate);
                List<Entry> list4 = list2;
                // ** MonitorExit[arg11_8] (shouldn't be in output)
            }
            catch (Throwable e) {
                Throwable e2 = e;
                List<Entry> list5 = list2;
                // ** MonitorExit[arg14_11] (shouldn't be in output)
                throw e;
            }
            ArrayList<Entry> arrayList3 = arrayList;
            this.shutdownEntries(arrayList3);
            return;
        }
    }

    public void shutdownAll() {
        List<Entry> list;
        List<Entry> list2 = list = this.entries;
        synchronized (list) {
            try {
                ArrayList<Entry> arrayList;
                List<Entry> list3 = this.entries;
                ArrayList<Entry> arrayList2 = arrayList = new ArrayList<Entry>(list3);
                this.entries.clear();
                List<Entry> list4 = list2;
                // ** MonitorExit[arg11_6] (shouldn't be in output)
                ArrayList<Entry> arrayList3 = arrayList2;
                this.shutdownEntries(arrayList3);
                return;
            }
            catch (Throwable e) {
                Throwable e2 = e;
                List<Entry> list5 = list2;
                // ** MonitorExit[arg14_10] (shouldn't be in output)
                throw e;
            }
        }
    }

    private void add(@NotNull String string, @NotNull Runnable runnable, long l, boolean bl) {
        List<Entry> list;
        List<Entry> list2 = list = this.entries;
        synchronized (list) {
            try {
                List<Entry> list3 = this.entries;
                Entry entry = new Entry(string, runnable, l, bl);
                boolean bl2 = list3.add(entry);
                List<Entry> list4 = list2;
                // ** MonitorExit[arg19_10] (shouldn't be in output)
            }
            catch (Throwable e) {
                Throwable e2 = e;
                List<Entry> list5 = list2;
                // ** MonitorExit[arg21_13] (shouldn't be in output)
                throw e;
            }
            return;
        }
    }

    private void shutdownEntries(@NotNull List<Entry> list) {
        Iterator<Entry> iterator;
        Collections.reverse(list);
        Iterator<Entry> iterator2 = list.iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator<Entry> iterator3 = iterator2;
            Entry entry = iterator3.next();
            try {
                Entry entry2 = entry;
                long l = entry2.timeoutMillis;
                long l2 = 0L;
                if (l > l2) {
                    Entry entry3 = entry;
                    this.runBounded(entry3);
                    continue;
                }
                Entry entry4 = entry;
                entry4.shutdown.run();
            }
            catch (Throwable e) {
                Throwable e2 = e;
                Entry entry5 = entry;
                String string = entry5.name;
                Throwable e3 = e2;
                this.logFailure(string, e3);
            }
        }
    }

    private void runBounded(@NotNull Entry entry) {
        Thread thread;
        Thread thread2;
        Runnable runnable = () -> {
            try {
                entry.shutdown.run();
            }
            catch (Throwable e) {
                Throwable e2 = e;
                String string = entry.name;
                Throwable e3 = e2;
                this.logFailure(string, e3);
            }
        };
        String string = "AET-Shutdown-" + entry.name;
        Thread thread3 = thread2 = (thread = new Thread(runnable, string));
        thread3.setDaemon(true);
        Thread thread4 = thread2;
        thread4.start();
        try {
            Thread thread5 = thread2;
            long l = entry.timeoutMillis;
            thread5.join(l);
            Thread thread6 = thread2;
            if (thread6.isAlive()) {
                Thread thread7 = thread2;
                thread7.interrupt();
                Logger logger = this.logger;
                String string2 = entry.name;
                long l2 = entry.timeoutMillis;
                String string3 = "Shutdown service '" + string2 + "' exceeded " + l2 + "ms; continuing server shutdown.";
                logger.warning(string3);
            }
        }
        catch (InterruptedException e) {
            InterruptedException e2 = e;
            Thread.currentThread().interrupt();
        }
    }

    private void logFailure(@NotNull String string, @NotNull Throwable e) {
        Logger logger = this.logger;
        String simpleName = e.getClass().getSimpleName();
        String string2 = String.valueOf(e.getMessage());
        String string3 = "Failed to shut down service '" + string + "': " + simpleName + ": " + string2;
        logger.warning(string3);
    }

    private static final class Entry {
        private final String name;
        private final Runnable shutdown;
        private final long timeoutMillis;
        private final boolean mainThread;

        @Generated
        public Entry(String string, Runnable runnable, long l, boolean bl) {
            this.name = string;
            this.shutdown = runnable;
            this.timeoutMillis = l;
            this.mainThread = bl;
        }
    }
}

