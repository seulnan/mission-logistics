package mission.domain.service;

import mission.common.log.Log;
import mission.common.log.LogLevel;
import mission.domain.model.Place;
import mission.domain.model.PlaceWithPositionDto;
import mission.domain.model.Position;

/**
 * 장소와 위치 관련 도메인 로직을 담당하는 서비스.
 * 핵심 비즈니스 흐름에 집중하고 세부 구현은 추상화한다.
 */
@Log(LogLevel.DEBUG)
public class PlaceService {

    private final PlaceQueryService placeQueryService;
    private final PositionQueryService positionQueryService;

    public PlaceService(PlaceQueryService placeQueryService, PositionQueryService positionQueryService) {
        this.placeQueryService = placeQueryService;
        this.positionQueryService = positionQueryService;
    }

    /**
     * 장소명으로 장소를 조회한다.
     */
    @Log(LogLevel.DEBUG)
    public Place getPlaceByName(String name) {
        return placeQueryService.findByName(name);
    }

    /**
     * 장소 ID로 위치를 조회한다.
     */
    @Log(LogLevel.DEBUG)
    public Position getPositionByPlaceId(int placeId) {
        return positionQueryService.findByPlaceId(placeId);
    }

    /**
     * 장소명으로 장소가 존재하는지 검증한다.
     */
    @Log(LogLevel.DEBUG)
    public void validatePlaceExists(String name) {
        placeQueryService.validateExists(name);
    }

    /**
     * 장소명으로 장소와 해당 위치를 모두 조회한다.
     * 핵심 비즈니스 플로우만 담당한다.
     */
    @Log(LogLevel.DEBUG)
    public PlaceWithPositionDto getPlaceWithPosition(String name) {
        Place place = getPlaceByName(name);
        Position position = getPositionByPlaceId(place.id());
        return PlaceWithPositionDto.of(place, position);
    }
}
