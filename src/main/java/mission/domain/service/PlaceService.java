package mission.domain.service;

import mission.domain.exception.PlaceNotFoundException;
import mission.domain.exception.PositionNotFoundException;
import mission.domain.model.Place;
import mission.domain.model.Position;
import mission.domain.port.PlaceRepository;

/**
 * 장소와 위치 관련 도메인 로직을 담당하는 서비스.
 * 장소 조회, 위치 조회, 검증 등의 공통 로직을 제공한다.
 */
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    /**
     * 장소명으로 장소를 조회한다. 존재하지 않으면 예외를 발생시킨다.
     */
    public Place getPlaceByName(String name) {
        return placeRepository.findPlaceByName(name)
                .orElseThrow(() -> new PlaceNotFoundException(name));
    }

    /**
     * 장소 ID로 위치를 조회한다. 존재하지 않으면 예외를 발생시킨다.
     */
    public Position getPositionByPlaceId(int placeId) {
        return placeRepository.findPositionByPlaceId(placeId)
                .orElseThrow(() -> new PositionNotFoundException(placeId));
    }

    /**
     * 장소명으로 장소가 존재하는지 검증한다. 존재하지 않으면 예외를 발생시킨다.
     */
    public void validatePlaceExists(String name) {
        if (placeRepository.findPlaceByName(name).isEmpty()) {
            throw new PlaceNotFoundException(name);
        }
    }

    /**
     * 장소명으로 장소와 해당 위치를 모두 조회한다.
     */
    public PlaceWithPosition getPlaceWithPosition(String name) {
        Place place = getPlaceByName(name);
        Position position = getPositionByPlaceId(place.id());
        return new PlaceWithPosition(place, position);
    }

    /**
     * 장소와 위치 정보를 함께 담는 불변 레코드.
     */
    public record PlaceWithPosition(Place place, Position position) {}
}
