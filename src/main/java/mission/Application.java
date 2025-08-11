package mission;

import mission.presentation.GlobalExceptionHandler;

public class Application {
    public static void main(String[] args) {
        var compositionRoot = new CompositionRoot();
        var controller = compositionRoot.consoleController();

        try {
            controller.runOnce();
        } catch (RuntimeException e) {
            GlobalExceptionHandler.handle(e);
        }
    }
}
