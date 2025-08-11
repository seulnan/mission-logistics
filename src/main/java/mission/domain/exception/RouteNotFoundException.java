package mission.domain.exception;

/**
 * 경로를 찾을 수 없을 때 발생하는 예외.
 * 고립된 장소나 연결되지 않은 경로를 진단할 수 있도록 상세 정보를 제공한다.
 */
public class RouteNotFoundException extends DomainException {

    private final String fromPlace;
    private final String toPlace;
    private final String diagnosticInfo;

    public RouteNotFoundException(String fromPlace, String toPlace, String diagnosticInfo) {
        super(String.format("경로를 찾을 수 없습니다: %s → %s (%s)", fromPlace, toPlace, diagnosticInfo));
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.diagnosticInfo = diagnosticInfo;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public String getDiagnosticInfo() {
        return diagnosticInfo;
    }
}
