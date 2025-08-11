package mission.domain.exception;

/**
 * 도메인 전반에서 사용하는 런타임 예외의 상위 타입.
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
