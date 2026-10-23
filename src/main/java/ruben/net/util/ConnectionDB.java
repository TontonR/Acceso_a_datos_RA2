package ruben.net.util;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    // Obtain database access keys from environment variables
    private static final String url = System.getenv("DB_URL");
    private static final String user = System.getenv("DB_USER");
    private static final String password = System.getenv("DB_PASSWORD");

    // Try connection to the database, if there's an error return an exception
    public static Connection getConnection() throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connection established without errors");
            return conn;
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
        return DriverManager.getConnection(url, user, password);
    }

}
