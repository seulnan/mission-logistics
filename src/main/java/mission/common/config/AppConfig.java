package mission.common.config;

import mission.common.log.LogLevel;

public final class AppConfig {

    private static final double DEFAULT_SPEED = 60.0;
    private static final LogLevel DEFAULT_LOG_LEVEL = LogLevel.DEBUG;

    private AppConfig() {}

    public static double speedKmPerHour() {
        String prop = System.getProperty("speed.kmph");
        if (prop == null || prop.isBlank()) {
            prop = System.getenv("SPEED_KMPH");
        }
        if (prop == null || prop.isBlank()) {
            return DEFAULT_SPEED;
        }
        try {
            return Double.parseDouble(prop.trim());
        } catch (NumberFormatException e) {
            mission.common.log.Logger.log(AppConfig.class, mission.common.log.LogLevel.WARN,
                    "Invalid speed configuration: " + prop + ", using default: " + DEFAULT_SPEED);
            return DEFAULT_SPEED;
        }
    }

    public static LogLevel logLevel() {
        String prop = System.getProperty("log.level");
        if (prop == null || prop.isBlank()) {
            prop = System.getenv("LOG_LEVEL");
        }
        if (prop == null || prop.isBlank()) {
            return DEFAULT_LOG_LEVEL;
        }
        try {
            return LogLevel.valueOf(prop.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            mission.common.log.Logger.log(AppConfig.class, mission.common.log.LogLevel.WARN,
                    "Invalid log level configuration: " + prop + ", using default: " + DEFAULT_LOG_LEVEL);
            return DEFAULT_LOG_LEVEL;
        }
    }
}
