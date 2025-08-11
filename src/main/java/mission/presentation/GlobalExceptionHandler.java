package mission.presentation;

import mission.common.log.Logger;
import mission.common.log.LogLevel;
import mission.presentation.view.implement.ConsoleOutputView;

/**
 * 표현 계층에서 사용할 간단한 예외 출력기.
 */
public final class GlobalExceptionHandler {
    private static final ConsoleOutputView outputView = new ConsoleOutputView();

    private GlobalExceptionHandler() {}

    public static void handle(Throwable throwable) {
        String errorMessage = throwable.getClass().getSimpleName() + " : " + throwable.getMessage();

        // ERROR 레벨로 예외 로깅
        Logger.log(GlobalExceptionHandler.class, LogLevel.ERROR,"Exception occurred: " + throwable.getClass().getSimpleName() + " - " + throwable.getMessage());

        outputView.printError(errorMessage);
    }
}
