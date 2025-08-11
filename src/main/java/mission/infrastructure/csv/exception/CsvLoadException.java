package mission.infrastructure.csv.exception;

public class CsvLoadException extends RuntimeException {
    public CsvLoadException(String resource, Throwable cause) {
        super(resource + " 로드 중 오류", cause);
    }
}
