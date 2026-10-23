package ruben.net.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ruben.net.model.Product;

public class ProductDAO {

    public void insert(Product p) throws SQLException {
        // TODO: INSERT INTO products
    }

    public List<Product> list() throws SQLException {
        // TODO: SELECT * FROM products
        return new ArrayList<>();
    }

    public void update(Product p) throws SQLException {
        // TODO: UPDATE products SET ...
    }

    public void delete(int id) throws SQLException {
        // TODO: DELETE FROM products WHERE id =?
    }
}
