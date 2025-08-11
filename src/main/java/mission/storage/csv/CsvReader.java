package mission.storage.csv;

import mission.business.exception.DomainExceptions;
import mission.util.Validator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** 리소스(csv) 로더. 간단한 CSV(쉼표, 헤더 없음) 파서. */
final class CsvReader {

    private CsvReader() { }

    static List<String[]> read(String resourceName, int expectedColumns, String schemaHint) {
        Validator.notBlank(resourceName, "csv 리소스명이 비어 있습니다.");

        InputStream in = CsvReader.class.getClassLoader().getResourceAsStream(resourceName);
        if (in == null) {
            throw new DomainExceptions.DataIntegrityException("리소스를 찾을 수 없습니다: " + resourceName);
        }

        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null) {
                row++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue; // 주석/빈줄 허용
                String[] parts = line.split(",");
                if (parts.length != expectedColumns) {
                    throw new DomainExceptions.DataIntegrityException(
                            resourceName + " 라인 " + row + ": 컬럼 수(" + parts.length + ")가 스키마(" + schemaHint + ")와 다릅니다."
                    );
                }
                rows.add(parts);
            }
        } catch (DomainExceptions.DataIntegrityException e) {
            throw e;
        } catch (Exception e) {
            throw new DomainExceptions.DataIntegrityException(resourceName + " 읽기 실패: " + e.getMessage());
        }
        return rows;
    }
}
