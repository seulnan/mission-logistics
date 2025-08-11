package mission.presentation;

/**
 * 표현 계층에서 사용할 간단한 예외 출력기.
 */
public final class GlobalExceptionHandler {
    private GlobalExceptionHandler() {}

    public static void handle(Throwable throwable) {
        // 예외 타입 이름과 메시지를 출력
        System.out.println(throwable.getClass().getSimpleName() + " : " + throwable.getMessage());
    }
}
