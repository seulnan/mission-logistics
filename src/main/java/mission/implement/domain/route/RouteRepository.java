package mission.implement.domain.route;

import java.util.List;

/** 포트(리포지토리) */
public interface RouteRepository {
    List<Route> findAll();
}
