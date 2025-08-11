package mission.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import mission.domain.model.Place;
import mission.domain.model.Position;
import mission.domain.port.PlaceRepository;
import mission.domain.service.TravelTimeEstimator;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class EstimateTimeUseCaseTest {

    static class FakeRepo implements PlaceRepository {
        @Override
        public Optional<Place> findPlaceByName(String name) {
            if (name.equals("A")) return Optional.of(new Place(1, "A", "addr"));
            if (name.equals("B")) return Optional.of(new Place(2, "B", "addr"));
            return Optional.empty();
        }

        @Override
        public Optional<Position> findPositionByPlaceId(int placeId) {
            if (placeId == 1) return Optional.of(new Position(1, 36.8931267, 128.7325885));
            if (placeId == 2) return Optional.of(new Position(2, 37.4945268, 126.9598527));
            return Optional.empty();
        }
    }

    @Test
    void 정상_시간계산() {
        FakeRepo fakeRepo = new FakeRepo();
        mission.domain.service.PlaceService placeService = new mission.domain.service.PlaceService(fakeRepo);
        EstimateTimeUseCase uc = new EstimateTimeUseCase(placeService, new TravelTimeEstimator(60.0));
        long minutes = uc.estimateMinutesByPlaceNames("A", "B");
        assertEquals(171, minutes, 1);
    }
}
