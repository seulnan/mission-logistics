package mission.application.controller;

import api.Console; // 미션 제공 API (외부 제공)
import mission.business.dto.TravelTimeResponse;
import mission.business.exception.DomainExceptions;
import mission.business.service.TravelTimeService;
import mission.util.TimeFormat;

import java.time.Duration;

/**
 * 콘솔 I/O를 담당하는 애플리케이션 계층.
 * - 기본: 위경도 기반 직선 예상 시간
 * - 심화: route.csv 기반 최단 시간
 *
 * 입력 프롬프트와 출력 포맷은 명세에 맞춘다.
 */
public class DeliveryController {

    private final TravelTimeService service;

    public DeliveryController(TravelTimeService service) {
        this.service = service;
    }

    public void run() {
        try {
            String start = Console.readLine();

            Console.println("\n도착지를 입력해주세요.\n> ");
            String end = Console.readline().trim();

            // 기본: 위경도 기반 근사
            Duration geo = service.estimateByGeo(start, end);

            // 심화: 경로 기반 최단 시간(가능한 경우만)
            Duration route = service.estimateByRoute(start, end).orElse(null);

            Duration toPrint = (route != null) ? route : geo;

            Console.println("\n이동 시간은 " + TimeFormat.toHourMinute(toPrint) + "으로 예측됩니다.");
        } catch (IllegalArgumentException e) {
            Console.println("\nIllegalArgumentException : " + e.getMessage());
        } catch (DomainExceptions.DataIntegrityException e) {
            Console.println("\nIllegalStateException : " + e.getMessage());
        }
    }
}
