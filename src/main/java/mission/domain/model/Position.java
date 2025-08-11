package mission.domain.model;

/**
 * 위경도 위치를 표현하는 불변 레코드.
 */
public record Position(int placeId, double latitude, double longitude) {}
