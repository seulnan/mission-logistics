package mission.infrastructure.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import mission.domain.model.Place;
import mission.domain.model.Position;
import mission.domain.port.PlaceRepository;
import mission.infrastructure.csv.exception.CsvLoadException;

/**
 * CSV 리소스 파일에서 장소와 위치를 로드하는 인프라 어댑터.
 */
public class CsvPlaceRepository implements PlaceRepository {

    private final Map<Integer, Place> idToPlace = new HashMap<>();
    private final Map<String, Integer> nameToId = new HashMap<>();
    private final Map<Integer, Position> idToPosition = new HashMap<>();
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
            dataLoaded = true;
        }
    }

    private void loadPlaces() {
        try (InputStream is = getResource("/place.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0].trim());
                String name = fields[1].trim();
                String address = fields[2].trim();
                Place place = new Place(id, name, address);
                idToPlace.put(id, place);
                nameToId.put(name, id);
            }
        } catch (IOException e) {
            throw new CsvLoadException("place.csv", e);
        }
    }

    private void loadPositions() {
        try (InputStream is = getResource("/position.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                int placeId = Integer.parseInt(fields[0].trim());
                double lat = Double.parseDouble(fields[1].trim());
                double lng = Double.parseDouble(fields[2].trim());
                idToPosition.put(placeId, new Position(placeId, lat, lng));
            }
        } catch (IOException e) {
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
}
