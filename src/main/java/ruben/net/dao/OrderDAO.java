package ruben.net.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.List;

import ruben.net.model.OrderLine;
import ruben.net.util.ConnectionDB;

public class OrderDAO {

    public static int create(int clientId, List<OrderLine> lines) throws SQLException {
        if (lines == null || lines.isEmpty()) {
            throw new SQLException("The order must have at least one line");
        }

        String insertOrder = "INSERT INTO Orders (client_id) VALUES (?)";
        String selectProduct = "SELECT price, stock FROM Products WHERE id = ? FOR UPDATE";
        String updateStock = "UPDATE Products SET stock = stock - ? WHERE id = ?";
        String insertLine = "INSERT INTO OrderLines (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        String updateTotal = "UPDATE Orders SET total = ? WHERE id = ?";

        try (Connection conn = ConnectionDB.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int orderId;
                try (PreparedStatement ps = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, clientId);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Could not get the new order id");
                        }
                        orderId = keys.getInt(1);
                    }
                }

                double[] subtotals = new double[lines.size()];
                double total = 0;

                for (int i = 0; i < lines.size(); i++) {
                    OrderLine line = lines.get(i);
                    if (line.getQuantity() <= 0) {
                        throw new SQLException("The quantity must be greater than 0");
                    }

                    double unitPrice;
                    int stock;
                    try (PreparedStatement ps = conn.prepareStatement(selectProduct)) {
                        ps.setInt(1, line.getProductId());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next()) {
                                throw new SQLException("Product not found: " + line.getProductId());
                            }
                            unitPrice = rs.getDouble("price");
                            stock = rs.getInt("stock");
                        }
                    }

                    if (stock < line.getQuantity()) {
                        throw new SQLException("Insufficient stock for product " + line.getProductId());
                    }

                    try (PreparedStatement ps = conn.prepareStatement(updateStock)) {
                        ps.setInt(1, line.getQuantity());
                        ps.setInt(2, line.getProductId());
                        ps.executeUpdate();
                    }

                    try (PreparedStatement ps = conn.prepareStatement(insertLine)) {
                        ps.setInt(1, orderId);
                        ps.setInt(2, line.getProductId());
                        ps.setInt(3, line.getQuantity());
                        ps.setDouble(4, unitPrice);
                        ps.executeUpdate();
                    }

                    subtotals[i] = unitPrice * line.getQuantity();
                    total += subtotals[i];
                }

                // Apply discounts after the lines; if this fails, keep the order without discount
                Savepoint afterLines = conn.setSavepoint("AfterLines");
                try {
                    double discountedTotal = applyDiscounts(conn, lines, subtotals, total);
                    updateTotal(conn, orderId, discountedTotal);
                } catch (SQLException e) {
                    conn.rollback(afterLines);
                    updateTotal(conn, orderId, total);
                }

                conn.commit();
                return orderId;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private static double applyDiscounts(Connection conn, List<OrderLine> lines, double[] subtotals, double total)
            throws SQLException {
        String sql = "SELECT type, amount FROM Discounts WHERE product_id = ?";
        double discount = 0;

        for (int i = 0; i < lines.size(); i++) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, lines.get(i).getProductId());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        if ("%".equals(rs.getString("type"))) {
                            discount += subtotals[i] * rs.getDouble("amount") / 100;
                        } else {
                            discount += rs.getDouble("amount");
                        }
                    }
                }
            }
        }

        return Math.max(0, total - discount);
    }

    private static void updateTotal(Connection conn, int orderId, double total) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE Orders SET total = ? WHERE id = ?")) {
            ps.setDouble(1, total);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }
}
