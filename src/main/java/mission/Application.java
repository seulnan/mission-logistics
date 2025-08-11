package mission;

import mission.presentation.ConsoleController;
import mission.presentation.GlobalExceptionHandler;

public class Application {
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
