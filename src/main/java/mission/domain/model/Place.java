package mission.domain.model;

/**
 * 장소 정보를 표현하는 불변 레코드.
 */
public record Place(int id, String name, String address) {}
