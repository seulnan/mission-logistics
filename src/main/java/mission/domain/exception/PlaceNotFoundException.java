package mission.domain.exception;

public class PlaceNotFoundException extends DomainException {
    public PlaceNotFoundException(String name) {
        super("\"" + name + "\"을(를) 이름으로 갖는 장소는 존재하지 않습니다.");
    }
}
