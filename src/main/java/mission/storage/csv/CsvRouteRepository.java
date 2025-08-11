package mission.storage.csv;

import mission.business.exception.DomainExceptions;
import mission.implement.domain.route.Route;
import mission.implement.domain.route.RouteRepository;
import mission.util.Validator;

import java.util.*;

/**
 * CSV(route.csv) 어댑터
 * 스키마: place_id_1,place_id_2,time(h:nn)
 * - 무방향: 저장소는 편의상 (1,2)만 읽더라도, 서비스에서 역방향을 보정한다.
 * - 중복 간선(동일 (a,b)) 검출
 */
public class CsvRouteRepository implements RouteRepository {

    private final List<Route> routes;

    public CsvRouteRepository(String resourceName) {
        Validator.notBlank(resourceName, "route csv 리소스명이 비어 있습니다.");
        List<String[]> rows = CsvReader.read(resourceName, 3, "route.csv: place_id_1,place_id_2,time(h:nn)");

        // 중복 검사 세트(무방향으로 동일 간선을 하나로 간주)
        Set<String> undirectedKey = new HashSet<>();
        List<Route> temp = new ArrayList<>();

        for (String[] r : rows) {
            int a = Integer.parseInt(r[0].trim());
            int b = Integer.parseInt(r[1].trim());
            int minutes = Route.parseMinutes(r[2]);

            int x = Math.min(a, b);
            int y = Math.max(a, b);
            String key = x + "-" + y;

            if (!undirectedKey.add(key)) {
                throw new DomainExceptions.DataIntegrityException("route.csv: 중복된 무방향 경로(" + a + "," + b + ")");
            }
            temp.add(new Route(a, b, minutes));
        }
        this.routes = List.copyOf(temp);
    }

    @Override
    public List<Route> findAll() {
        return routes;
    }
}
