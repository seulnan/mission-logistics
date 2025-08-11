package mission.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import mission.domain.model.Position;
import mission.domain.service.TravelTimeEstimator;
import org.junit.jupiter.api.Test;

class TravelTimeEstimatorTest {

    @Test
    void 봉화군청에서_숭실대까지_60kmh_반올림_2시간51분() {
        Position from = new Position(1, 36.8931267, 128.7325885);
        Position to = new Position(2, 37.4945268, 126.9598527);
        TravelTimeEstimator estimator = new TravelTimeEstimator(60.0);

        long minutes = estimator.estimateMinutes(from, to);

        assertEquals(171, minutes, 1); // 2시간 51분 = 171분
    }
}
