package mission;

import mission.application.EstimateTimeUseCase;
import mission.common.config.AppConfig;
import mission.domain.service.PlaceService;
import mission.domain.service.TravelTimeEstimator;
import mission.infrastructure.csv.CsvPlaceRepository;
import mission.presentation.ConsoleController;
import mission.presentation.formatter.TravelTimeFormatter;
import mission.presentation.view.InputView;
import mission.presentation.view.OutputView;

/**
 * 의존성 생성 및 조립을 담당하는 구성 루트.
 */
public class CompositionRoot {

    private final CsvPlaceRepository placeRepository;
    private final PlaceService placeService;
    private final TravelTimeEstimator travelTimeEstimator;
    private final EstimateTimeUseCase estimateTimeUseCase;
    private final TravelTimeFormatter timeFormatter;

    public CompositionRoot() {
        this.placeRepository = new CsvPlaceRepository();
        this.placeService = new PlaceService(placeRepository);
        this.travelTimeEstimator = new TravelTimeEstimator(AppConfig.speedKmPerHour());
        this.estimateTimeUseCase = new EstimateTimeUseCase(placeService, travelTimeEstimator);
        this.timeFormatter = new TravelTimeFormatter();
    }

    public ConsoleController consoleController() {
        return new ConsoleController(estimateTimeUseCase, placeService, new InputView(), new OutputView(), timeFormatter);
    }
}
