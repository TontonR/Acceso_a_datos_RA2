package ruben.net.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ruben.net.model.Discount;
import ruben.net.util.ConnectionDB;

public class DiscountDAO {

    public static void insert(Discount d) throws SQLException {
        validate(d);
        String sql = "INSERT INTO Discounts (product_id, type, amount) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getProductId());
            ps.setString(2, d.getType());
            ps.setDouble(3, d.getAmount());
            ps.executeUpdate();
        }
    }

    public static List<Discount> list() throws SQLException {
        String sql = "SELECT * FROM Discounts";
        ArrayList<Discount> discounts = new ArrayList<>();

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                discounts.add(new Discount(rs.getInt("id"), rs.getInt("product_id"),
                        rs.getString("type"), rs.getDouble("amount")));
            }
        }

        return discounts;
    }

    public static Discount get(int id) throws SQLException {
        String sql = "SELECT * FROM Discounts WHERE id = ?";

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Discount(id, rs.getInt("product_id"),
                            rs.getString("type"), rs.getDouble("amount"));
                }
            }
        }

        return null;
    }

    public static void update(Discount d) throws SQLException {
        validate(d);
        String sql = "UPDATE Discounts SET product_id = ?, type = ?, amount = ? WHERE id = ?";

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getProductId());
            ps.setString(2, d.getType());
            ps.setDouble(3, d.getAmount());
            ps.setInt(4, d.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No discount found with id " + d.getId());
            }
        }
    }

    public static void delete(int id) throws SQLException {
        String sql = "DELETE FROM Discounts WHERE id = ?";

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private static void validate(Discount d) throws SQLException {
        if (d.getProductId() <= 0) {
            throw new SQLException("The product id is required!");
        }
        if (!"%".equals(d.getType()) && !"€".equals(d.getType())) {
            throw new SQLException("The discount type must be '%' or '€'");
        }
        if (d.getAmount() <= 0) {
            throw new SQLException("The discount amount must be greater than 0");
        }
        if ("%".equals(d.getType()) && d.getAmount() > 100) {
            throw new SQLException("A percentage discount cannot be greater than 100");
        }
    }
}
