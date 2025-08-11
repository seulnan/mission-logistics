package mission.presentation;

import mission.application.EstimateTimeUseCase;
import mission.domain.service.PlaceService;
import mission.presentation.view.InputView;
import mission.presentation.view.OutputView;
import static mission.common.validation.Validators.requireNonBlank;
import java.time.Duration;

/**
 * 콘솔 입출력을 담당하는 표현 계층 컨트롤러.
 */
public class ConsoleController {

    private final EstimateTimeUseCase estimateTimeUseCase;
    private final PlaceService placeService;
    private final InputView inputView;
    private final OutputView outputView;

    public ConsoleController(EstimateTimeUseCase estimateTimeUseCase, PlaceService placeService,
                             InputView inputView, OutputView outputView) {
        this.estimateTimeUseCase = estimateTimeUseCase;
        this.placeService = placeService;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void runOnce() {
        String from = requireNonBlank(inputView.inputStartName(), () -> "출발지 입력은 비어 있을 수 없습니다.");
        placeService.validatePlaceExists(from);

        String to = requireNonBlank(inputView.inputEndName(), () -> "도착지 입력은 비어 있을 수 없습니다.");

        long minutes = estimateTimeUseCase.estimateMinutesByPlaceNames(from, to);
        Duration duration = Duration.ofMinutes(minutes);
        outputView.printEstimatedTime(duration);
    }
}
