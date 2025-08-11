package mission.implement.domain.route;

/**
 * 경로 엔티티 (무방향 간선, minutes는 place_id_1 -> place_id_2 이동 시간)
 * storage 단에서 양방향 보정할 수 있으나 엔티티는 단방향 한 변만 표현.
 */
public record Route(int placeId1, int placeId2, int minutes) {

    /** (h:nn) 형식을 분으로 변환 */
    public static int parseMinutes(String hColonMm) {
        String[] parts = hColonMm.trim().split(":");
        int h = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        return h * 60 + m;
    }
}
