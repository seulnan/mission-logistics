package mission.implement.domain.position;

import java.util.List;
import java.util.Optional;

/** 포트(리포지토리) */
public interface PositionRepository {
    Optional<Position> findByPlaceId(int placeId);
    List<Position> findAll();
}
