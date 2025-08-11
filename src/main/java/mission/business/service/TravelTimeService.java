package mission.business.service;

import mission.business.exception.DomainExceptions;
import mission.implement.domain.place.Place;
import mission.implement.domain.place.PlaceRepository;
import mission.implement.domain.position.Position;
import mission.implement.domain.position.PositionRepository;
import mission.implement.domain.route.Route;
import mission.implement.domain.route.RouteRepository;
import mission.util.Geo;
import mission.util.Validator;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 유스케이스 서비스:
 * - 위경도 기반 직선 이동 시간(속도 60km/h 가정)
 * - route 그래프 기반 최단 시간(다익스트라)
 *
 * Storage 어댑터가 로딩 시 무결성 검사를 수행하지만,
 * 서비스는 유즈케이스 관점의 추가 검증과 탐색 책임을 진다.
 */
public class TravelTimeService {

    private static final double SPEED_KMPH = 60.0;

    private final PlaceRepository placeRepository;
    private final PositionRepository positionRepository;
    private final RouteRepository routeRepository;

    public TravelTimeService(
            PlaceRepository placeRepository,
            PositionRepository positionRepository,
            RouteRepository routeRepository
    ) {
        this.placeRepository = placeRepository;
        this.positionRepository = positionRepository;
        this.routeRepository = routeRepository;
    }

    /** 기본 로직: 위경도 기반 직선거리 / 60km/h */
    public Duration estimateByGeo(String startName, String endName) {
        Place start = placeRepository.findByName(startName)
                .orElseThrow(() -> notFoundPlace(startName));
        Place end = placeRepository.findByName(endName)
                .orElseThrow(() -> notFoundPlace(endName));

        Position sp = positionRepository.findByPlaceId(start.id())
                .orElseThrow(() -> new DomainExceptions.DataIntegrityException(
                        "place_id=" + start.id() + " 의 위치 정보가 존재하지 않습니다."
                ));
        Position ep = positionRepository.findByPlaceId(end.id())
                .orElseThrow(() -> new DomainExceptions.DataIntegrityException(
                        "place_id=" + end.id() + " 의 위치 정보가 존재하지 않습니다."
                ));

        double km = Geo.haversine(sp.lat(), sp.lng(), ep.lat(), ep.lng());
        // 시간(분) = 시간(시)*60 = (km / 60)*60 = km
        long minutes = Math.round(km); // 근사(반올림)
        return Duration.ofMinutes(minutes);
    }

    /** 심화 로직: route.csv 기반 최단 시간(분). 경유 가능, 무방향 그래프. */
    public Optional<Duration> estimateByRoute(String startName, String endName) {
        Place start = placeRepository.findByName(startName)
                .orElseThrow(() -> notFoundPlace(startName));
        Place end = placeRepository.findByName(endName)
                .orElseThrow(() -> notFoundPlace(endName));

        // 인접 리스트 구성
        Map<Integer, List<Route>> graph = routeRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Route::placeId1));

        // 양방향으로 보장되어 있지 않아도, 저장소는 (1,2) 하나만 있으면 (2,1)도 추가 반환하도록 설계해 둘 수 있다.
        // 다만 방어적으로 역방향도 합친다.
        routeRepository.findAll().forEach(r ->
                graph.computeIfAbsent(r.placeId2(), k -> new ArrayList<>())
                        .add(new Route(r.placeId2(), r.placeId1(), r.minutes()))
        );

        // 다익스트라
        return dijkstraMinutes(graph, start.id(), end.id()).map(Duration::ofMinutes);
    }

    private Optional<Long> dijkstraMinutes(Map<Integer, List<Route>> graph, int startId, int endId) {
        if (!graph.containsKey(startId) || !graph.containsKey(endId)) {
            return Optional.empty();
        }

        Map<Integer, Long> dist = new HashMap<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));
        pq.add(new int[]{startId, 0});
        dist.put(startId, 0L);

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0];
            long d = cur[1];

            if (u == endId) return Optional.of(d);
            if (d > dist.getOrDefault(u, Long.MAX_VALUE)) continue;

            for (Route e : graph.getOrDefault(u, List.of())) {
                long nd = d + e.minutes();
                if (nd < dist.getOrDefault(e.placeId2(), Long.MAX_VALUE)) {
                    dist.put(e.placeId2(), nd);
                    pq.add(new int[]{e.placeId2(), (int) nd});
                }
            }
        }
        return Optional.empty();
    }

    private IllegalArgumentException notFoundPlace(String name) {
        return new IllegalArgumentException("\"" + name + "\"을(를) 이름으로 갖는 장소는 존재하지 않습니다.");
    }
}
