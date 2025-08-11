package mission.infrastructure.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import mission.domain.exception.DataIntegrityException;
import mission.domain.model.Place;
import mission.domain.model.Position;
import mission.domain.model.Route;
import mission.domain.port.PlaceRepository;
import mission.domain.port.RouteRepository;
import mission.infrastructure.csv.exception.CsvLoadException;

/**
 * CSV 리소스 파일에서 장소, 위치, 경로를 로드하는 인프라 어댑터.
 */
public class CsvPlaceRepository implements PlaceRepository, RouteRepository {

    private final Map<Integer, Place> idToPlace = new HashMap<>();
    private final Map<String, Integer> nameToId = new HashMap<>();
    private final Map<Integer, Position> idToPosition = new HashMap<>();
    private final List<Route> routes = new ArrayList<>();
    private final Map<String, Route> routeKeyToRoute = new HashMap<>();
    private boolean dataLoaded = false;

    public CsvPlaceRepository() {
        // 지연 로딩을 위해 생성자에서는 데이터를 로드하지 않음
    }

    @Override
    public Optional<Place> findPlaceByName(String name) {
        ensureDataLoaded();
        Integer id = nameToId.get(name);
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(idToPlace.get(id));
    }

    @Override
    public Optional<Position> findPositionByPlaceId(int placeId) {
        ensureDataLoaded();
        return Optional.ofNullable(idToPosition.get(placeId));
    }

    private void ensureDataLoaded() {
        if (!dataLoaded) {
            loadPlaces();
            loadPositions();
            loadRoutes();
            validateDataIntegrity();
            dataLoaded = true;
        }
    }

        private void loadPlaces() {
        try (InputStream is = getResource("/place.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    mission.common.log.Logger.log(CsvPlaceRepository.class, mission.common.log.LogLevel.WARN,
                            "Empty line found in place.csv, skipping");
                    continue; // 빈 줄 스킵
                }
                String[] fields = line.split(",");
                if (fields.length < 3) {
                    mission.common.log.Logger.log(CsvPlaceRepository.class, mission.common.log.LogLevel.WARN,
                            "Insufficient fields in place.csv line: " + line + ", skipping");
                    continue; // 필드가 부족한 줄 스킵
                }
                int id = Integer.parseInt(fields[0].trim());
                String name = fields[1].trim();
                String address = fields[2].trim();
                Place place = new Place(id, name, address);
                idToPlace.put(id, place);
                nameToId.put(name, id);
            }
        } catch (IOException e) {
            mission.common.log.Logger.log(CsvPlaceRepository.class, mission.common.log.LogLevel.ERROR,
                    "Failed to load place.csv: " + e.getMessage());
            throw new CsvLoadException("place.csv", e);
        }
    }

        private void loadPositions() {
        try (InputStream is = getResource("/position.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    mission.common.log.Logger.log(CsvPlaceRepository.class, mission.common.log.LogLevel.WARN,
                            "Empty line found in position.csv, skipping");
                    continue; // 빈 줄 스킵
                }
                String[] fields = line.split(",");
                if (fields.length < 3) {
                    mission.common.log.Logger.log(CsvPlaceRepository.class, mission.common.log.LogLevel.WARN,
                            "Insufficient fields in position.csv line: " + line + ", skipping");
                    continue; // 필드가 부족한 줄 스킵
                }
                int placeId = Integer.parseInt(fields[0].trim());
                double lat = Double.parseDouble(fields[1].trim());
                double lng = Double.parseDouble(fields[2].trim());
                idToPosition.put(placeId, new Position(placeId, lat, lng));
            }
        } catch (IOException e) {
            mission.common.log.Logger.log(CsvPlaceRepository.class, mission.common.log.LogLevel.ERROR,
                    "Failed to load position.csv: " + e.getMessage());
            throw new CsvLoadException("position.csv", e);
        }
    }

    private InputStream getResource(String path) throws IOException {
        InputStream is = CsvPlaceRepository.class.getResourceAsStream(path);
        if (is == null) {
            throw new IOException("리소스를 찾을 수 없습니다: " + path);
        }
        return is;
    }

    // RouteRepository 구현 메서드들
    @Override
    public List<Route> findAllRoutes() {
        ensureDataLoaded();
        return new ArrayList<>(routes);
    }

    @Override
    public Optional<Route> findDirectRoute(int fromPlaceId, int toPlaceId) {
        ensureDataLoaded();
        String routeKey = fromPlaceId + "-" + toPlaceId;
        return Optional.ofNullable(routeKeyToRoute.get(routeKey));
    }

    @Override
    public List<Route> findRoutesFromPlace(int placeId) {
        ensureDataLoaded();
        return routes.stream()
                .filter(route -> route.fromPlaceId() == placeId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Route> findRoutesToPlace(int placeId) {
        ensureDataLoaded();
        return routes.stream()
                .filter(route -> route.toPlaceId() == placeId)
                .collect(Collectors.toList());
    }

    private void loadRoutes() {
        try (InputStream is = getResource("/route.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    mission.common.log.Logger.log(CsvPlaceRepository.class, mission.common.log.LogLevel.WARN,
                            "Empty line found in route.csv, skipping");
                    continue;
                }
                String[] fields = line.split(",");
                if (fields.length < 4) {
                    mission.common.log.Logger.log(CsvPlaceRepository.class, mission.common.log.LogLevel.WARN,
                            "Insufficient fields in route.csv line: " + line + ", skipping");
                    continue;
                }
                int fromPlaceId = Integer.parseInt(fields[0].trim());
                int toPlaceId = Integer.parseInt(fields[1].trim());
                double distanceKm = Double.parseDouble(fields[2].trim());
                int durationMinutes = Integer.parseInt(fields[3].trim());

                Route route = new Route(fromPlaceId, toPlaceId, distanceKm, durationMinutes);
                routes.add(route);

                // 중복 경로 검사
                String routeKey = route.getRouteKey();
                if (routeKeyToRoute.containsKey(routeKey)) {
                    throw new DataIntegrityException("DUPLICATE_ROUTE",
                            String.format("중복된 경로가 발견되었습니다: %s (기존: %s, 새로운: %s)",
                                    routeKey, routeKeyToRoute.get(routeKey), route));
                }
                routeKeyToRoute.put(routeKey, route);
            }
        } catch (IOException e) {
            mission.common.log.Logger.log(CsvPlaceRepository.class, mission.common.log.LogLevel.ERROR,
                    "Failed to load route.csv: " + e.getMessage());
            throw new CsvLoadException("route.csv", e);
        }
    }

    private void validateDataIntegrity() {
        validatePlacePositionMapping();
        validateRouteConnectivity();
    }

    private void validatePlacePositionMapping() {
        // 1:1 매핑 검증: 모든 place가 position을 가지는지 확인
        for (Place place : idToPlace.values()) {
            if (!idToPosition.containsKey(place.id())) {
                throw new DataIntegrityException("MISSING_POSITION",
                        String.format("장소 '%s' (ID: %d)에 대응하는 위치 정보가 없습니다", place.name(), place.id()));
            }
        }

        // 1:1 매핑 검증: 모든 position이 place를 가지는지 확인
        for (Position position : idToPosition.values()) {
            if (!idToPlace.containsKey(position.placeId())) {
                throw new DataIntegrityException("MISSING_PLACE",
                        String.format("위치 정보 (ID: %d)에 대응하는 장소 정보가 없습니다", position.placeId()));
            }
        }
    }

    private void validateRouteConnectivity() {
        // 고립된 장소 검증: route가 있는데 연결되지 않은 장소가 있는지 확인
        Set<Integer> connectedPlaces = new HashSet<>();
        for (Route route : routes) {
            connectedPlaces.add(route.fromPlaceId());
            connectedPlaces.add(route.toPlaceId());

            // route에 참조된 place가 실제로 존재하는지 확인
            if (!idToPlace.containsKey(route.fromPlaceId())) {
                throw new DataIntegrityException("ROUTE_INVALID_FROM_PLACE",
                        String.format("경로에서 참조하는 출발지 ID %d가 존재하지 않습니다: %s",
                                route.fromPlaceId(), route));
            }
            if (!idToPlace.containsKey(route.toPlaceId())) {
                throw new DataIntegrityException("ROUTE_INVALID_TO_PLACE",
                        String.format("경로에서 참조하는 도착지 ID %d가 존재하지 않습니다: %s",
                                route.toPlaceId(), route));
            }
        }

        // route가 있는 상황에서 고립된 장소가 있는지 확인
        if (!routes.isEmpty()) {
            for (Place place : idToPlace.values()) {
                if (!connectedPlaces.contains(place.id())) {
                    mission.common.log.Logger.log(CsvPlaceRepository.class, mission.common.log.LogLevel.WARN,
                            String.format("고립된 장소 발견: '%s' (ID: %d) - 이 장소로 가거나 이 장소에서 출발하는 경로가 없습니다",
                                    place.name(), place.id()));
                }
            }
        }
    }
}
