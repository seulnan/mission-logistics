package mission.presentation.view.implement;

import mission.presentation.view.OutputView;
import java.time.Duration;

public class ConsoleOutputView implements OutputView {

    @Override
    public void printEstimatedTime(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        System.out.println("이동 시간은 " + hours + "시간 " + minutes + "분으로 예측됩니다. ");
    }

    @Override
    public void printError(String message) {
        System.out.println(message);
    }
}
