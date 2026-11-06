package ruben.net.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionDB {
    // Try connection to the database, if there's an error return an exception
    public static Connection getConnection() throws SQLException {
        // Loads the config.properties data to lauch the database with the correct values
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
        } catch (IOException e) {
            throw new SQLException("Could not read config.properties: " + e.getMessage());
        }
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        // Launch the database with the previous data
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("Connection established without errors");
        return conn;
    }

}
