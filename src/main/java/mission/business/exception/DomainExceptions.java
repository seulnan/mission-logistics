package mission.business.exception;

/**
 * 의미 있는 도메인/무결성 예외 모음.
 * - IllegalArgumentException 로 종료 메세지를 맞추되,
 *   내부 구분을 위해 런타임 예외를 계층화.
 */
public final class DomainExceptions {

    private DomainExceptions() { }

    /** 데이터 무결성 위반(1:1 매핑 불일치, 중복 route 등) */
    public static class DataIntegrityException extends RuntimeException {
        public DataIntegrityException(String message) {
            super(message);
        }
    }
}
