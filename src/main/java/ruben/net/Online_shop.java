package ruben.net;

import java.sql.SQLException;

import ruben.net.dao.ProductDAO;
import ruben.net.model.Product;

/**
 *
 * @author Rubén Muñoz Blanco
 */
public class Online_shop {

    public static void main(String[] args) throws SQLException {
        Product p = new Product(1, "Galletas", 2.59, 23);
        ProductDAO.insert(p);
        System.out.println(ProductDAO.get(p.getId()));
    }
}
