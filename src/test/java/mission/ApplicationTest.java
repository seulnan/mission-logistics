package mission;

import static org.junit.jupiter.api.Assertions.assertTrue;

import api.TestEnvironment;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ApplicationTest extends TestEnvironment {
    @Test
    void 기본_시나리오_정상동작() {
        run(
                List.of(
                        "봉화군청",
                        "숭실대학교 정보과학관"
                )
        );
        String out = output();
        assertTrue(out.contains("출발지를 입력해주세요."));
        assertTrue(out.contains("도착지를 입력해주세요."));
        assertTrue(out.contains("이동 시간은 2시간 51분으로 예측됩니다."));
    }

    @Test
    void 출발지_검증_즉시_예외발생() {
        run(
                List.of(
                        "없는장소",
                        "숭실대학교 정보과학관"
                )
        );
        String out = output();
        assertTrue(out.contains("PlaceNotFoundException : \"없는장소\"을(를) 이름으로 갖는 장소는 존재하지 않습니다."));
    }

    @Test
    void 빈입력이면_InvalidInputException_출력() {
        run(
                List.of(
                        "   ",
                        "숭실대학교 정보과학관"
                )
        );
        String out = output();
        assertTrue(out.contains("InvalidInputException : 출발지 입력은 비어 있을 수 없습니다."));
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
