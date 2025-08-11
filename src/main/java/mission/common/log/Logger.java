package mission.common.log;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import mission.common.config.AppConfig;

public final class Logger {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Logger() {}

    private static boolean enabled(LogLevel level) {
        LogLevel global = AppConfig.logLevel();
        // DEBUG < WARN < ERROR
        int current = ordinal(global);
        int incoming = ordinal(level);
        return incoming >= current;
    }

    private static int ordinal(LogLevel level) {
        return switch (level) {
            case DEBUG -> 0;
            case WARN -> 1;
            case ERROR -> 2;
        };
    }

    public static void log(Class<?> clazz, LogLevel level, String message) {
        if (!enabled(level)) return;
        String ts = LocalDateTime.now().format(FORMATTER);
        System.out.println("[" + ts + "] [" + level + "] [" + clazz.getSimpleName() + "] " + message);
    }

    public static void logAnnotated(Class<?> clazz) {
        Log typeLog = clazz.getAnnotation(Log.class);
        if (typeLog != null) {
            log(clazz, typeLog.value(), "class loaded");
        }
        for (Method method : clazz.getDeclaredMethods()) {
            Log methodLog = method.getAnnotation(Log.class);
            if (methodLog != null) {
                log(clazz, methodLog.value(), "method annotated: " + method.getName());
            }
        }
    }
}
