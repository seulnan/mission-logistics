package mission.storage.csv;

import mission.business.exception.DomainExceptions;
import mission.implement.domain.position.Position;
import mission.implement.domain.position.PositionRepository;
import mission.util.Validator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * CSV(position.csv) 어댑터
 * 스키마: place_id,lat,lng
 * - Place 1:1 Position 무결성 검증은 Route/Place 저장소와 분리되어야 하지만,
 *   최소한 저장소 내부 중복(place_id) 체크는 수행한다.
 */
public class CsvPositionRepository implements PositionRepository {

    private final Map<Integer, Position> byPlaceId;

    public CsvPositionRepository(String resourceName) {
        Validator.notBlank(resourceName, "position csv 리소스명이 비어 있습니다.");
        List<String[]> rows = CsvReader.read(resourceName, 3, "position.csv: place_id,lat,lng");

        Map<Integer, Position> temp = new HashMap<>();
        for (String[] r : rows) {
            int placeId = Integer.parseInt(r[0].trim());
            double lat = Double.parseDouble(r[1].trim());
            double lng = Double.parseDouble(r[2].trim());

            Position p = new Position(placeId, lat, lng);
            if (temp.putIfAbsent(placeId, p) != null) {
                throw new DomainExceptions.DataIntegrityException("position.csv: 중복된 place_id: " + placeId);
            }
        }
        this.byPlaceId = Map.copyOf(temp);
    }

    @Override
    public Optional<Position> findByPlaceId(int placeId) {
        return Optional.ofNullable(byPlaceId.get(placeId));
    }

    @Override
    public List<Position> findAll() {
        return byPlaceId.values().stream()
                .sorted(Comparator.comparingInt(Position::placeId))
                .collect(Collectors.toList());
    }
}
