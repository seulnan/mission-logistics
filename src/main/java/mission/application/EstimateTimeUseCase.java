package mission.application;

import mission.domain.exception.SamePlaceException;
import mission.domain.service.PlaceService;
import mission.domain.service.TravelTimeEstimator;

/**
 * 이동 시간 추정 애플리케이션 유스케이스.
 */
public class EstimateTimeUseCase {

    private final PlaceService placeService;
    private final TravelTimeEstimator travelTimeEstimator;

    public EstimateTimeUseCase(PlaceService placeService, TravelTimeEstimator travelTimeEstimator) {
        this.placeService = placeService;
        this.travelTimeEstimator = travelTimeEstimator;
    }

    public long estimateMinutesByPlaceNames(String fromName, String toName) {
        validateDifferentPlaces(fromName, toName);

        var fromPlaceWithPosition = placeService.getPlaceWithPosition(fromName);
        var toPlaceWithPosition = placeService.getPlaceWithPosition(toName);

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
