package mission;

import mission.application.controller.DeliveryController;
import mission.business.service.TravelTimeService;
import mission.implement.domain.place.PlaceRepository;
import mission.implement.domain.position.PositionRepository;
import mission.implement.domain.route.RouteRepository;
import mission.storage.csv.CsvPlaceRepository;
import mission.storage.csv.CsvPositionRepository;
import mission.storage.csv.CsvRouteRepository;

/**
 * 프로그램 시작점. 외부 프레임워크 없이 수동 DI를 구성한다.
 * - Controller → Service → Repositories(csv)
 */
public class Application {

    public static void main(String[] args) {
        // Storage adapters (CSV)
        PlaceRepository placeRepository = new CsvPlaceRepository("place.csv");
        PositionRepository positionRepository = new CsvPositionRepository("position.csv");
        RouteRepository routeRepository = new CsvRouteRepository("route.csv");

        // Business service
        TravelTimeService service = new TravelTimeService(placeRepository, positionRepository, routeRepository);

        // Application controller
        DeliveryController controller = new DeliveryController(service);

        // Run
        controller.run();
    }
}
