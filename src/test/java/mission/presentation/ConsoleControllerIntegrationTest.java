package mission.presentation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import api.TestEnvironment;
import java.util.List;
import mission.Application;
import org.junit.jupiter.api.Test;

class ConsoleControllerIntegrationTest extends TestEnvironment {

    @Test
    void 빈입력_출발지에서_즉시_에러() {
        run(List.of("   ", "숭실대학교 정보과학관"));
        String out = output();
        assertTrue(out.contains("InvalidInputException : 출발지 입력은 비어 있을 수 없습니다."));
    }

    @Test
    void 출발지_존재하지_않음() {
        run(List.of("없는장소", "숭실대학교 정보과학관"));
        String out = output();
        assertTrue(out.contains("PlaceNotFoundException : \"없는장소\"을(를) 이름으로 갖는 장소는 존재하지 않습니다."));
    }

    @Test
    void 도착지_존재하지_않음() {
        run(List.of("봉화군청", "없는장소"));
        String out = output();
        assertTrue(out.contains("PlaceNotFoundException : \"없는장소\"을(를) 이름으로 갖는 장소는 존재하지 않습니다."));

    }

    @Test
    void 동일_장소_입력시_예외() {
        run(List.of("봉화군청", "봉화군청"));
        String out = output();
        assertTrue(out.contains("SamePlaceException : 출발지와 도착지가 동일합니다: 봉화군청"));
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
