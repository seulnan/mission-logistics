package mission.storage.csv;

import mission.business.exception.DomainExceptions;
import mission.implement.domain.place.Place;
import mission.implement.domain.place.PlaceRepository;
import mission.util.Validator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * CSV(place.csv) 어댑터
 * 스키마: id,name,address
 */
public class CsvPlaceRepository implements PlaceRepository {

    private final Map<Integer, Place> byId;
    private final Map<String, Place> byName;

    public CsvPlaceRepository(String resourceName) {
        Validator.notBlank(resourceName, "place csv 리소스명이 비어 있습니다.");
        List<String[]> rows = CsvReader.read(resourceName, 3, "place.csv: id,name,address");
        Map<Integer, Place> tempById = new HashMap<>();
        Map<String, Place> tempByName = new HashMap<>();

        for (String[] r : rows) {
            int id = Integer.parseInt(r[0].trim());
            String name = r[1].trim();
            String addr = r[2].trim();
            Place p = new Place(id, name, addr);

            if (tempById.putIfAbsent(id, p) != null) {
                throw new DomainExceptions.DataIntegrityException("place.csv: 중복된 id: " + id);
            }
            if (tempByName.putIfAbsent(name, p) != null) {
                throw new DomainExceptions.DataIntegrityException("place.csv: 중복된 name: " + name);
            }
        }
        this.byId = Map.copyOf(tempById);
        this.byName = Map.copyOf(tempByName);
    }

    @Override
    public Optional<Place> findById(int id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public Optional<Place> findByName(String name) {
        return Optional.ofNullable(byName.get(name));
    }

    @Override
    public List<Place> findAll() {
        return byId.values().stream().sorted(Comparator.comparingInt(Place::id)).collect(Collectors.toList());
    }
}
