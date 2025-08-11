package mission.domain.service;

import mission.domain.model.Position;
import mission.domain.exception.InvalidSpeedException;

/**
 * 이동 시간 추정 도메인 서비스. 주어진 속도로 직선(구면) 이동한다고 가정한다.
 */
public final class TravelTimeEstimator {

    private final double speedKmPerHour;

    public TravelTimeEstimator(double speedKmPerHour) {
        if (speedKmPerHour <= 0) {
            throw new InvalidSpeedException(speedKmPerHour);
        }
        this.speedKmPerHour = speedKmPerHour;
    }

    public long estimateMinutes(Position from, Position to) {
        double kilometers = DistanceCalculator.calculateKilometers(from, to);
        double hours = kilometers / speedKmPerHour;
        return Math.round(hours * 60);
    }
}
