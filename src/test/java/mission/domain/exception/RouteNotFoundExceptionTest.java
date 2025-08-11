package mission.domain.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RouteNotFoundExceptionTest {

    @Test
    void 예외_메시지_형식_확인() {
        // given
        String fromPlace = "고립된섬";
        String toPlace = "본토";
        String diagnosticInfo = "두 장소 간 직접 경로가 존재하지 않습니다";

        // when
        RouteNotFoundException exception = new RouteNotFoundException(fromPlace, toPlace, diagnosticInfo);

        // then
        assertEquals(fromPlace, exception.getFromPlace());
        assertEquals(toPlace, exception.getToPlace());
        assertEquals(diagnosticInfo, exception.getDiagnosticInfo());

        String expectedMessage = "경로를 찾을 수 없습니다: 고립된섬 → 본토 (두 장소 간 직접 경로가 존재하지 않습니다)";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
