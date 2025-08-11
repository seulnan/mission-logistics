package mission.domain.exception;

public class InvalidSpeedException extends DomainException {
    public InvalidSpeedException(double speed) {
        super("속도 값이 유효하지 않습니다: " + speed);
    }
}
