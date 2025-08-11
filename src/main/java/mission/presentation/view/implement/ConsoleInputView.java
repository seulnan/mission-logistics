package mission.presentation.view.implement;

import api.Console;
import mission.presentation.view.InputView;

public class ConsoleInputView implements InputView {

    @Override
    public String inputStartName() {
        System.out.println("출발지를 입력해주세요. ");
        return Console.readLine();
    }

    @Override
    public String inputEndName() {
        System.out.println("");
        System.out.println("도착지를 입력해주세요. ");
        return Console.readLine();
    }
}
