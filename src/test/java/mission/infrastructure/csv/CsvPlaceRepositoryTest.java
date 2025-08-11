package mission.infrastructure.csv;

import static org.junit.jupiter.api.Assertions.*;

import mission.domain.exception.DataIntegrityException;
import mission.domain.model.Place;
import mission.domain.model.Position;
import mission.domain.model.Route;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

class CsvPlaceRepositoryTest {

    @Test
    void 정상_데이터_로딩() {
        // given & when
        CsvPlaceRepository repository = new CsvPlaceRepository();

        // then - 기본 데이터가 로드되는지 확인
        assertDoesNotThrow(() -> {
            Optional<Place> place = repository.findPlaceByName("봉화군청");
            assertTrue(place.isPresent());
            assertEquals("봉화군청", place.get().name());

            Optional<Position> position = repository.findPositionByPlaceId(place.get().id());
            assertTrue(position.isPresent());

            List<Route> routes = repository.findAllRoutes();
            assertFalse(routes.isEmpty());
        });
    }

    @Test
    void 경로_조회_기능() {
        // given
        CsvPlaceRepository repository = new CsvPlaceRepository();

        // when & then
        Optional<Route> directRoute = repository.findDirectRoute(1, 2);
        assertTrue(directRoute.isPresent());
        assertEquals(1, directRoute.get().fromPlaceId());
        assertEquals(2, directRoute.get().toPlaceId());

        List<Route> fromPlace1 = repository.findRoutesFromPlace(1);
        assertFalse(fromPlace1.isEmpty());
        assertTrue(fromPlace1.stream().allMatch(route -> route.fromPlaceId() == 1));

        List<Route> toPlace2 = repository.findRoutesToPlace(2);
        assertFalse(toPlace2.isEmpty());
        assertTrue(toPlace2.stream().allMatch(route -> route.toPlaceId() == 2));
    }
}
