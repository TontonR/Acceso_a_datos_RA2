package ruben.net.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ruben.net.util.ConnectionDB;

public class ConsultesDAO {

    // Orders of one client with their lines and products
    public static void printOrdersByClient(int clientId) throws SQLException {
        String sql = "SELECT o.id AS order_id, o.order_date, c.name AS client_name, "
                + "p.name AS product_name, ol.quantity, ol.unit_price, "
                + "(ol.quantity * ol.unit_price) AS line_total, o.total "
                + "FROM Orders o "
                + "JOIN Clients c ON c.id = o.client_id "
                + "JOIN OrderLines ol ON ol.order_id = o.id "
                + "JOIN Products p ON p.id = ol.product_id "
                + "WHERE o.client_id = ? "
                + "ORDER BY o.id, ol.id";

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                int lastOrderId = -1;
                boolean found = false;

                while (rs.next()) {
                    found = true;
                    int orderId = rs.getInt("order_id");
                    if (orderId != lastOrderId) {
                        if (lastOrderId != -1) {
                            System.out.println();
                        }
                        System.out.printf("Comanda %d - %s - %s - Total final: %.2f €%n",
                                orderId, rs.getString("client_name"), rs.getTimestamp("order_date"),
                                rs.getDouble("total"));
                        lastOrderId = orderId;
                    }
                    System.out.printf("  %s x%d (%.2f €) = %.2f €%n",
                            rs.getString("product_name"), rs.getInt("quantity"),
                            rs.getDouble("unit_price"), rs.getDouble("line_total"));
                }

                if (!found) {
                    System.out.println("Aquest client no té cap comanda.");
                }
            }
        }
    }

    // Final total of every order (the total already includes discounts)
    public static void printOrderTotals() throws SQLException {
        String sql = "SELECT o.id, c.name AS client_name, o.order_date, o.total "
                + "FROM Orders o "
                + "JOIN Clients c ON c.id = o.client_id "
                + "ORDER BY o.id";

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("Comanda %d - %s - %s - Total final: %.2f €%n",
                        rs.getInt("id"), rs.getString("client_name"),
                        rs.getTimestamp("order_date"), rs.getDouble("total"));
            }
            if (!found) {
                System.out.println("No hi ha cap comanda.");
            }
        }
    }
}
