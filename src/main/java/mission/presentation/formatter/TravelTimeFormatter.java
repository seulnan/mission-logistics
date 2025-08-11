package mission.presentation.formatter;

/**
 * 이동 시간을 사용자에게 표시할 형식으로 변환하는 포매터.
 */
public class TravelTimeFormatter {

    public String formatMinutes(long totalMinutes) {
        long hours = totalMinutes / 60;
        long remainingMinutes = totalMinutes % 60;
        return hours + "시간 " + remainingMinutes + "분";
    }
}
