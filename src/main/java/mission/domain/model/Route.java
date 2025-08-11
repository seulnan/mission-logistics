package mission.domain.model;

/**
 * 경로 정보를 표현하는 불변 레코드.
 */
public record Route(int fromPlaceId, int toPlaceId, double distanceKm, int durationMinutes) {

    public Route {
        if (fromPlaceId == toPlaceId) {
            throw new IllegalArgumentException("출발지와 도착지가 같을 수 없습니다: " + fromPlaceId);
        }
        if (distanceKm < 0) {
            throw new IllegalArgumentException("거리는 음수일 수 없습니다: " + distanceKm);
        }
        if (durationMinutes < 0) {
            throw new IllegalArgumentException("소요시간은 음수일 수 없습니다: " + durationMinutes);
        }
    }

    /**
     * 경로의 고유 식별자 (from-to 조합)
     */
    public String getRouteKey() {
        return fromPlaceId + "-" + toPlaceId;
    }

    /**
     * 역방향 경로의 고유 식별자 (to-from 조합)
     */
    public String getReverseRouteKey() {
        return toPlaceId + "-" + fromPlaceId;
    }
}
