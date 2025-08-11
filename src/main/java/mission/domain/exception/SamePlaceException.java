package mission.domain.exception;

public class SamePlaceException extends DomainException {
    public SamePlaceException(String name) {
        super("출발지와 도착지가 동일합니다: " + name);
    }
}
