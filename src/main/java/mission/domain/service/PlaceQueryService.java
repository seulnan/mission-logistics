package mission.domain.service;

import mission.common.log.Log;
import mission.common.log.LogLevel;
import mission.domain.exception.PlaceNotFoundException;
import mission.domain.model.Place;
import mission.domain.port.PlaceRepository;

/**
 * 장소 조회와 관련된 도메인 서비스.
 * 장소 조회의 세부 구현을 담당한다.
 */
@Log(LogLevel.DEBUG)
public class PlaceQueryService {

    private final PlaceRepository placeRepository;

    public PlaceQueryService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    /**
     * 장소명으로 장소를 조회한다. 존재하지 않으면 예외를 발생시킨다.
     */
    @Log(LogLevel.DEBUG)
    public Place findByName(String name) {
        return placeRepository.findPlaceByName(name)
                .orElseThrow(() -> new PlaceNotFoundException(name));
    }

    /**
     * 장소명으로 장소가 존재하는지 검증한다. 존재하지 않으면 예외를 발생시킨다.
     */
    @Log(LogLevel.DEBUG)
    public void validateExists(String name) {
        if (placeRepository.findPlaceByName(name).isEmpty()) {
            throw new PlaceNotFoundException(name);
        }
    }
}
