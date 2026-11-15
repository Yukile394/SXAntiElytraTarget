package studio.yukilem.yukilem.libs.nixlib.menu;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

final class Warnings {
    private static final Set<String> SEEN = ConcurrentHashMap.newKeySet();
    private static volatile Logger logger;

    private Warnings() {
    }

    static void logger(Logger logger) {
        Warnings.logger = logger;
    }

    static void once(String string, String string2) {
        if (!SEEN.add(string)) {
            return;
        }
        Logger logger = Warnings.logger;
        if (logger != null) {
            logger.warning(string2);
        }
    }
}

