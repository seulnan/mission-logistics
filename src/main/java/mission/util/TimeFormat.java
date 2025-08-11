package mission.util;

import java.time.Duration;

/** 시간 포맷터: h:nn */
public final class TimeFormat {
    private TimeFormat() { }

    public static String toHourMinute(Duration d) {
        long minutes = d.toMinutes();
        long h = minutes / 60;
        long m = minutes % 60;
        return h + "시간 " + String.format("%d분", m);
    }
}
