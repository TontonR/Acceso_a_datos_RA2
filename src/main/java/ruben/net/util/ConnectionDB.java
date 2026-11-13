package ruben.net.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String URL = "jdbc:h2:./dades/shop";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // Embedded H2 database: no server or credentials needed
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
