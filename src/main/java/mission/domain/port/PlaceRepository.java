package mission.domain.port;

import java.util.Optional;
import mission.domain.model.Place;
import mission.domain.model.Position;

/**
 * 장소 및 위치 조회를 위한 도메인 포트.
 */
public interface PlaceRepository {

    Optional<Place> findPlaceByName(String name);

    Optional<Position> findPositionByPlaceId(int placeId);
}
