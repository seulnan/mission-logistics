package mission.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import mission.domain.exception.SamePlaceException;
import mission.domain.model.Place;
import mission.domain.model.PlaceWithPositionDto;
import mission.domain.model.Position;
import mission.domain.service.PlaceService;
import mission.domain.service.TravelTimeEstimator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EstimateTimeUseCaseTest {

    @Mock
    private PlaceService placeService;

    @Mock
    private TravelTimeEstimator travelTimeEstimator;

    private EstimateTimeUseCase estimateTimeUseCase;

    @BeforeEach
    void setUp() {
        estimateTimeUseCase = new EstimateTimeUseCase(placeService, travelTimeEstimator);
    }

    @Test
    void 정상_시간계산() {
        // given
        String fromName = "봉화군청";
        String toName = "숭실대학교 정보과학관";

        Place fromPlace = new Place(1, fromName, "경북 봉화군");
        Position fromPosition = new Position(1, 36.8931267, 128.7325885);
        PlaceWithPositionDto fromDto = PlaceWithPositionDto.of(fromPlace, fromPosition);

        Place toPlace = new Place(2, toName, "서울 동작구");
        Position toPosition = new Position(2, 37.4945268, 126.9598527);
        PlaceWithPositionDto toDto = PlaceWithPositionDto.of(toPlace, toPosition);

        when(placeService.getPlaceWithPosition(fromName)).thenReturn(fromDto);
        when(placeService.getPlaceWithPosition(toName)).thenReturn(toDto);
        when(travelTimeEstimator.estimateMinutes(fromPosition, toPosition)).thenReturn(171L);

        // when
        long result = estimateTimeUseCase.estimateMinutesByPlaceNames(fromName, toName);

        // then
        assertEquals(171L, result);
        verify(placeService).getPlaceWithPosition(fromName);
        verify(placeService).getPlaceWithPosition(toName);
        verify(travelTimeEstimator).estimateMinutes(fromPosition, toPosition);
    }

    @Test
    void 동일한_장소_예외발생() {
        // given
        String samePlaceName = "봉화군청";

        // when & then
        SamePlaceException exception = assertThrows(SamePlaceException.class,
                () -> estimateTimeUseCase.estimateMinutesByPlaceNames(samePlaceName, samePlaceName));

        assertTrue(exception.getMessage().contains(samePlaceName));
        verifyNoInteractions(placeService, travelTimeEstimator);
    }

    @Test
    void PlaceService_호출_순서_확인() {
        // given
        String fromName = "A";
        String toName = "B";

        Place fromPlace = new Place(1, "A", "addr");
        Position fromPosition = new Position(1, 0.0, 0.0);
        PlaceWithPositionDto fromDto = PlaceWithPositionDto.of(fromPlace, fromPosition);

        Place toPlace = new Place(2, "B", "addr");
        Position toPosition = new Position(2, 1.0, 1.0);
        PlaceWithPositionDto toDto = PlaceWithPositionDto.of(toPlace, toPosition);

        when(placeService.getPlaceWithPosition(fromName)).thenReturn(fromDto);
        when(placeService.getPlaceWithPosition(toName)).thenReturn(toDto);
        when(travelTimeEstimator.estimateMinutes(fromPosition, toPosition)).thenReturn(60L);

        // when
        estimateTimeUseCase.estimateMinutesByPlaceNames(fromName, toName);

        // then - 호출 순서 검증
        InOrder inOrder = inOrder(placeService, travelTimeEstimator);
        inOrder.verify(placeService).getPlaceWithPosition(fromName);
        inOrder.verify(placeService).getPlaceWithPosition(toName);
        inOrder.verify(travelTimeEstimator).estimateMinutes(fromPosition, toPosition);
    }
}
