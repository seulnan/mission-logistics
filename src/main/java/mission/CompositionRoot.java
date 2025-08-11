package mission;

import mission.application.EstimateTimeUseCase;
import mission.common.config.AppConfig;
import mission.domain.service.PlaceService;
import mission.domain.service.TravelTimeEstimator;
import mission.infrastructure.csv.CsvPlaceRepository;
import mission.presentation.ConsoleController;
import mission.presentation.view.implement.ConsoleInputView;
import mission.presentation.view.implement.ConsoleOutputView;

/**
 * 의존성 생성 및 조립을 담당하는 구성 루트.
 */
public class CompositionRoot {

    private final CsvPlaceRepository placeRepository;
    private final PlaceService placeService;
    private final TravelTimeEstimator travelTimeEstimator;
    private final EstimateTimeUseCase estimateTimeUseCase;

    public CompositionRoot() {
        this.placeRepository = new CsvPlaceRepository();
        this.placeService = new PlaceService(placeRepository);
        this.travelTimeEstimator = new TravelTimeEstimator(AppConfig.speedKmPerHour());
        this.estimateTimeUseCase = new EstimateTimeUseCase(placeService, travelTimeEstimator);
    }

    public ConsoleController consoleController() {
        return new ConsoleController(estimateTimeUseCase, placeService, new ConsoleInputView(), new ConsoleOutputView());
    }
}
