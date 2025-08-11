package mission.domain.port;

import mission.domain.model.Route;
import java.util.List;
import java.util.Optional;

/**
 * 경로 조회를 위한 도메인 포트.
 */
public interface RouteRepository {

    /**
     * 모든 경로를 조회한다.
     */
    List<Route> findAllRoutes();

    /**
     * 특정 출발지에서 도착지로의 직접 경로를 조회한다.
     */
    Optional<Route> findDirectRoute(int fromPlaceId, int toPlaceId);

    /**
     * 특정 장소에서 출발하는 모든 경로를 조회한다.
     */
    List<Route> findRoutesFromPlace(int placeId);

    /**
     * 특정 장소로 도착하는 모든 경로를 조회한다.
     */
    List<Route> findRoutesToPlace(int placeId);
}
