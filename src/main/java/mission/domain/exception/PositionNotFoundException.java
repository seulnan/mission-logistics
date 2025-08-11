package mission.domain.exception;

public class PositionNotFoundException extends DomainException {
    public PositionNotFoundException(int placeId) {
        super("장소 id=" + placeId + "의 위치 정보를 찾을 수 없습니다.");
    }
}
