package mission;

import mission.common.log.Log;
import mission.common.log.LogLevel;
import mission.presentation.ConsoleController;
import mission.presentation.GlobalExceptionHandler;

@Log(LogLevel.DEBUG)
public class Application {

    @Log(LogLevel.DEBUG)
    public static void main(String[] args) {
        CompositionRoot compositionRoot = new CompositionRoot();
        ConsoleController controller = compositionRoot.consoleController();

        try {
            controller.runOnce();
        } catch (RuntimeException e) {
            GlobalExceptionHandler.handle(e);
        }
    }
}
