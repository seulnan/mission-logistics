package mission.domain.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DataIntegrityExceptionTest {

    @Test
    void 예외_메시지_형식_확인() {
        // given
        String violationType = "MISSING_POSITION";
        String diagnosticInfo = "장소 'Test Place' (ID: 999)에 대응하는 위치 정보가 없습니다";

        // when
        DataIntegrityException exception = new DataIntegrityException(violationType, diagnosticInfo);

        // then
        assertEquals(violationType, exception.getViolationType());
        assertEquals(diagnosticInfo, exception.getDiagnosticInfo());
        assertTrue(exception.getMessage().contains(violationType));
        assertTrue(exception.getMessage().contains(diagnosticInfo));
        assertTrue(exception.getMessage().contains("데이터 무결성 위반"));
    }

    @Test
    void 중복_경로_예외_메시지() {
        // given
        String violationType = "DUPLICATE_ROUTE";
        String diagnosticInfo = "중복된 경로가 발견되었습니다: 1-2";

        // when
        DataIntegrityException exception = new DataIntegrityException(violationType, diagnosticInfo);

        // then
        assertEquals("[DUPLICATE_ROUTE] 데이터 무결성 위반: 중복된 경로가 발견되었습니다: 1-2",
                     exception.getMessage());
    }
}
