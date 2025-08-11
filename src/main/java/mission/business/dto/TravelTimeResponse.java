package mission.business.dto;

import java.time.Duration;

/** 확장 여지를 남긴 응답 DTO (현재는 사용하지 않지만, 컨트롤러/뷰 분리에 유용) */
public record TravelTimeResponse(Duration duration) { }
