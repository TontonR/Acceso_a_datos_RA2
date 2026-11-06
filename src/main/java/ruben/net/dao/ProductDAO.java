package ruben.net.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ruben.net.model.Product;
import ruben.net.util.ConnectionDB;

public class ProductDAO  {

    public static void insert(Product p) throws SQLException {
        if (p.getName() == null || p.getName().isBlank()){
            throw new SQLException("The product name is required!");
        }
        if (p.getPrice() <= 0) {
            throw new SQLException("The product has to been uper to 0");
        }
        if (p.getStock() < 1){
            throw new SQLException("The minimum stock is 1.");
        }
        String sql = "INSERT INTO Products (name, price, stock) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getStock());
            ps.executeQuery();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<Product> list() throws SQLException {
        String sql = "SELECT * FROM Products";
        ArrayList<Product> products = new ArrayList<>();

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery();){
                while (rs.next()){
                    products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("stock")));
                }
            } 
        }

        return products;
    }

    public static Product get(int id) throws SQLException {
        String sql = "SELECT * FROM Products WHERE id = ?";
        
        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()){
                    return new Product(id, rs.getString("name"), rs.getDouble("price"), rs.getInt("stock"));
                }
            }
        }

        return null;
    }

    public static void update(Product p) throws SQLException {
        String sql = "UPDATE Products SET name = ? price = ? stock = ? WHERE id = ?";
        Product product = null;

        if (p.getName() == null || p.getName().isBlank()){
            throw new SQLException("The product name is required!");
        }
        if (p.getPrice() <= 0) {
            throw new SQLException("The product has to been uper to 0");
        }
        if (p.getStock() < 1){
            throw new SQLException("The minimum stock is 1.");
        }

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getStock());
            ps.executeUpdate();
        }
    }

    public static void delete(int id) throws SQLException {
        String sql = "DELETE FROM Products WHERE id = ?";
        
        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
