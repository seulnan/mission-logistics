package mission.domain.model;

/**
 * 장소와 위치 정보를 함께 담는 애플리케이션 DTO.
 */
public record PlaceWithPositionDto(Place place, Position position) {

    public static PlaceWithPositionDto of(Place place, Position position) {
        return new PlaceWithPositionDto(place, position);
    }
}
