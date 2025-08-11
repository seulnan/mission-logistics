package mission.domain.exception;

/**
 * 유효하지 않은 사용자 입력에 대한 예외.
 */
public class InvalidInputException extends DomainException {
    public InvalidInputException(String message) {
        super(message);
    }
}
