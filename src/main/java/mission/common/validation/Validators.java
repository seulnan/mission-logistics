package mission.common.validation;

import java.util.function.Supplier;
import mission.domain.exception.InvalidInputException;

public final class Validators {
    private Validators() {}

    public static String requireNonBlank(String value, Supplier<String> messageSupplier) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(messageSupplier.get());
        }
        return value;
    }
}
