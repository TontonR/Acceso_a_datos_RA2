package ruben.net.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    public static void init() {
        try (
                Connection conn = ConnectionDB.getConnection();
                Statement stmt = conn.createStatement();
                InputStream is = DatabaseInitializer.class.getResourceAsStream("/schema.sql")) {
            if (is == null) {
                throw new RuntimeException("schema.sql not found");
            }

            String sql = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            for (String part : sql.split(";")) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing the database", e);
        }
    }
}
