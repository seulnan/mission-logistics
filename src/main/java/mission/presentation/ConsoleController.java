package mission.presentation;

import mission.application.EstimateTimeUseCase;
import mission.domain.service.PlaceService;
import mission.presentation.view.InputView;
import mission.presentation.view.OutputView;
import mission.presentation.formatter.TravelTimeFormatter;
import static mission.common.validation.Validators.requireNonBlank;

/**
 * 콘솔 입출력을 담당하는 표현 계층 컨트롤러.
 */
public class ConsoleController {

    private final EstimateTimeUseCase estimateTimeUseCase;
    private final PlaceService placeService;
    private final InputView inputView;
    private final OutputView outputView;
    private final TravelTimeFormatter timeFormatter;

    public ConsoleController(EstimateTimeUseCase estimateTimeUseCase, PlaceService placeService,
            InputView inputView, OutputView outputView, TravelTimeFormatter timeFormatter) {
        this.estimateTimeUseCase = estimateTimeUseCase;
        this.placeService = placeService;
        this.inputView = inputView;
        this.outputView = outputView;
        this.timeFormatter = timeFormatter;
    }

    public void runOnce() {
        String from = getValidatedInput("출발지를 입력해주세요. ", "출발지 입력은 비어 있을 수 없습니다.");
        placeService.validatePlaceExists(from);

        outputView.println("");
        String to = getValidatedInput("도착지를 입력해주세요. ", "도착지 입력은 비어 있을 수 없습니다.");

        long minutes = estimateTimeUseCase.estimateMinutesByPlaceNames(from, to);
        String formattedTime = timeFormatter.formatMinutes(minutes);

        outputView.println("");
        outputView.println("이동 시간은 " + formattedTime + "으로 예측됩니다. ");
    }

    private String getValidatedInput(String prompt, String errorMessage) {
        outputView.println(prompt);
        return requireNonBlank(inputView.readLine(), () -> errorMessage);
    }
}
