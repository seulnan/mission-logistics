package mission.presentation;

import mission.presentation.view.implement.ConsoleOutputView;

/**
 * 표현 계층에서 사용할 간단한 예외 출력기.
 */
public final class GlobalExceptionHandler {
    private static final ConsoleOutputView outputView = new ConsoleOutputView();

    private GlobalExceptionHandler() {}

    public static void handle(Throwable throwable) {
        String errorMessage = throwable.getClass().getSimpleName() + " : " + throwable.getMessage();
        outputView.printError(errorMessage);
    }
}
