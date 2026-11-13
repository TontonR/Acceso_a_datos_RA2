package ruben.net.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ruben.net.model.Client;
import ruben.net.util.ConnectionDB;

public class ClientDAO {

    public static void insert(Client c) throws SQLException {
        if (c.getName() == null || c.getName().isBlank()) {
            throw new SQLException("The client name is required!");
        }
        if (c.getEmail() == null || c.getEmail().isBlank()) {
            throw new SQLException("The client email is required!");
        }
        String sql = "INSERT INTO Clients (name, email) VALUES (?, ?)";
        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.executeUpdate();
        }
    }

    public static List<Client> list() throws SQLException {
        String sql = "SELECT * FROM Clients";
        ArrayList<Client> clients = new ArrayList<>();

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clients.add(new Client(rs.getInt("id"), rs.getString("name"), rs.getString("email")));
            }
        }

        return clients;
    }

    public static Client get(int id) throws SQLException {
        String sql = "SELECT * FROM Clients WHERE id = ?";

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Client(id, rs.getString("name"), rs.getString("email"));
                }
            }
        }

        return null;
    }

    public static void update(Client c) throws SQLException {
        String sql = "UPDATE Clients SET name = ?, email = ? WHERE id = ?";

        if (c.getName() == null || c.getName().isBlank()) {
            throw new SQLException("The client name is required!");
        }
        if (c.getEmail() == null || c.getEmail().isBlank()) {
            throw new SQLException("The client email is required!");
        }

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setInt(3, c.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No client found with id " + c.getId());
            }
        }
    }

    public static void delete(int id) throws SQLException {
        String sql = "DELETE FROM Clients WHERE id = ?";

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
