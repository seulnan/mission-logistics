package mission.util;

/** 지구 대원거리(Haversine) 계산 유틸 */
public final class Geo {
    private static final double R = 6371.0088; // km (mean Earth radius)

    private Geo() { }

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.pow(Math.sin(dLat / 2.0), 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.pow(Math.sin(dLon / 2.0), 2);
        double c = 2 * Math.asin(Math.min(1.0, Math.sqrt(a)));
        return R * c;
    }
}
