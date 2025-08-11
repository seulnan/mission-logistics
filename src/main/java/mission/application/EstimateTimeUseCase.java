package mission.application;

import mission.common.log.Log;
import mission.common.log.LogLevel;
import mission.domain.exception.SamePlaceException;
import mission.domain.model.PlaceWithPositionDto;
import mission.domain.service.PlaceService;
import mission.domain.service.TravelTimeEstimator;

/**
 * 이동 시간 추정 애플리케이션 유스케이스.
 */
@Log(LogLevel.DEBUG)
public class EstimateTimeUseCase {

    private final PlaceService placeService;
    private final TravelTimeEstimator travelTimeEstimator;

    public EstimateTimeUseCase(PlaceService placeService, TravelTimeEstimator travelTimeEstimator) {
        this.placeService = placeService;
        this.travelTimeEstimator = travelTimeEstimator;
    }

    @Log(LogLevel.DEBUG)
    public long estimateMinutesByPlaceNames(String fromName, String toName) {
        validateDifferentPlaces(fromName, toName);

        PlaceWithPositionDto fromPlaceWithPosition = placeService.getPlaceWithPosition(fromName);
        PlaceWithPositionDto toPlaceWithPosition = placeService.getPlaceWithPosition(toName);

        return travelTimeEstimator.estimateMinutes(
            fromPlaceWithPosition.position(),
            toPlaceWithPosition.position()
        );
    }

    private void validateDifferentPlaces(String fromName, String toName) {
        if (fromName.equals(toName)) {
            throw new SamePlaceException(fromName);
        }
    }
}
