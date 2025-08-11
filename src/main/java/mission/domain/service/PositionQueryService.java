package mission.domain.service;

import mission.common.log.Log;
import mission.common.log.LogLevel;
import mission.domain.exception.PositionNotFoundException;
import mission.domain.model.Position;
import mission.domain.port.PlaceRepository;

/**
 * 위치 조회와 관련된 도메인 서비스.
 * 위치 조회의 세부 구현을 담당한다.
 */
@Log(LogLevel.DEBUG)
public class PositionQueryService {

    private final PlaceRepository placeRepository;

    public PositionQueryService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    /**
     * 장소 ID로 위치를 조회한다. 존재하지 않으면 예외를 발생시킨다.
     */
    @Log(LogLevel.DEBUG)
    public Position findByPlaceId(int placeId) {
        return placeRepository.findPositionByPlaceId(placeId)
                .orElseThrow(() -> new PositionNotFoundException(placeId));
    }
}
