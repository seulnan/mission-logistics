package mission.util;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/** 공통 Validator (Optional 기반, 커스텀 메시지) */
public final class Validator {
    private Validator() { }

    public static <T> T notNull(T target, String message) {
        if (target == null) throw new IllegalArgumentException(message);
        return target;
    }

    public static String notBlank(String s, String message) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException(message);
        return s;
    }

    public static <T> T orElseThrow(Optional<T> opt, Supplier<? extends RuntimeException> ex) {
        return opt.orElseThrow(ex);
    }
}
