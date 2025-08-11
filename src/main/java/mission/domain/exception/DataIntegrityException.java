package mission.domain.exception;

/**
 * 데이터 무결성 위반 시 발생하는 예외.
 * 1:1 매핑 실패, 중복 데이터 등을 진단할 수 있도록 상세 정보를 제공한다.
 */
public class DataIntegrityException extends DomainException {

    private final String violationType;
    private final String diagnosticInfo;

    public DataIntegrityException(String violationType, String diagnosticInfo) {
        super(String.format("[%s] 데이터 무결성 위반: %s", violationType, diagnosticInfo));
        this.violationType = violationType;
        this.diagnosticInfo = diagnosticInfo;
    }

    public String getViolationType() {
        return violationType;
    }

    public String getDiagnosticInfo() {
        return diagnosticInfo;
    }
}
