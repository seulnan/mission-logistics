package mission.presentation.view;

import java.time.Duration;

public interface OutputView {
    void printEstimatedTime(Duration duration);
    void printError(String message);
}
