package mission.implement.domain.place;

import java.util.List;
import java.util.Optional;

/** 포트(리포지토리) - 저장소 세부 구현과 분리 */
public interface PlaceRepository {
    Optional<Place> findById(int id);
    Optional<Place> findByName(String name);
    List<Place> findAll();
}
