package ruben.net;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ruben.net.dao.ClientDAO;
import ruben.net.dao.ConsultesDAO;
import ruben.net.dao.DiscountDAO;
import ruben.net.dao.OrderDAO;
import ruben.net.dao.ProductDAO;
import ruben.net.model.Client;
import ruben.net.model.Discount;
import ruben.net.model.OrderLine;
import ruben.net.model.Product;
import ruben.net.util.DatabaseInitializer;

public class App {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            DatabaseInitializer.init();
        } catch (RuntimeException e) {
            System.out.println("No s'ha pogut inicialitzar la base de dades: " + e.getMessage());
            return;
        }

        int option;
        do {
            System.out.println("\n===== BOTIGA ONLINE =====");
            System.out.println("1. Gestionar Productes");
            System.out.println("2. Gestionar Clients");
            System.out.println("3. Crear Comanda");
            System.out.println("4. Llistar Comandes");
            System.out.println("5. Gestionar Descomptes");
            System.out.println("0. Sortir");
            option = readInt("Opció: ");

            try {
                switch (option) {
                    case 1 -> productsMenu();
                    case 2 -> clientsMenu();
                    case 3 -> createOrder();
                    case 4 -> ordersMenu();
                    case 5 -> discountsMenu();
                    case 0 -> System.out.println("Fins aviat!");
                    default -> System.out.println("Opció no vàlida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de dades: " + e.getMessage());
            }
        } while (option != 0);

        sc.close();
    }

    private static void productsMenu() {
        int option;
        do {
            System.out.println("\n--- PRODUCTES ---");
            System.out.println("1. Llistar");
            System.out.println("2. Afegir");
            System.out.println("3. Actualitzar");
            System.out.println("4. Eliminar");
            System.out.println("0. Tornar");
            option = readInt("Opció: ");

            try {
                switch (option) {
                    case 1 -> listProducts();
                    case 2 -> addProduct();
                    case 3 -> updateProduct();
                    case 4 -> deleteProduct();
                    case 0 -> {}
                    default -> System.out.println("Opció no vàlida.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (option != 0);
    }

    private static void clientsMenu() {
        int option;
        do {
            System.out.println("\n--- CLIENTS ---");
            System.out.println("1. Llistar");
            System.out.println("2. Afegir");
            System.out.println("3. Actualitzar");
            System.out.println("4. Eliminar");
            System.out.println("0. Tornar");
            option = readInt("Opció: ");

            try {
                switch (option) {
                    case 1 -> listClients();
                    case 2 -> addClient();
                    case 3 -> updateClient();
                    case 4 -> deleteClient();
                    case 0 -> {}
                    default -> System.out.println("Opció no vàlida.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (option != 0);
    }

    private static void discountsMenu() {
        int option;
        do {
            System.out.println("\n--- DESCOMPTES ---");
            System.out.println("1. Llistar");
            System.out.println("2. Afegir");
            System.out.println("3. Actualitzar");
            System.out.println("4. Eliminar");
            System.out.println("0. Tornar");
            option = readInt("Opció: ");

            try {
                switch (option) {
                    case 1 -> listDiscounts();
                    case 2 -> addDiscount();
                    case 3 -> updateDiscount();
                    case 4 -> deleteDiscount();
                    case 0 -> {}
                    default -> System.out.println("Opció no vàlida.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (option != 0);
    }

    private static void ordersMenu() throws SQLException {
        int option;
        do {
            System.out.println("\n--- COMANDES ---");
            System.out.println("1. Totes les comandes amb total");
            System.out.println("2. Comandes d'un client");
            System.out.println("0. Tornar");
            option = readInt("Opció: ");

            switch (option) {
                case 1 -> ConsultesDAO.printOrderTotals();
                case 2 -> ConsultesDAO.printOrdersByClient(readInt("Id del client: "));
                case 0 -> {}
                default -> System.out.println("Opció no vàlida.");
            }
        } while (option != 0);
    }

    private static void listProducts() throws SQLException {
        System.out.println("\n--- LLISTA DE PRODUCTES ---");
        for (Product p : ProductDAO.list()) {
            System.out.printf("%d - %s (%.2f €) Estoc: %d%n",
                    p.getId(), p.getName(), p.getPrice(), p.getStock());
        }
    }

    private static void addProduct() throws SQLException {
        String name = readString("Nom: ");
        double price = readDouble("Preu: ");
        int stock = readInt("Estoc: ");
        ProductDAO.insert(new Product(0, name, price, stock));
        System.out.println("Producte afegit.");
    }

    private static void updateProduct() throws SQLException {
        int id = readInt("Id del producte: ");
        Product p = ProductDAO.get(id);
        if (p == null) {
            System.out.println("No existeix cap producte amb aquest id.");
            return;
        }
        p.setName(readString("Nou nom: "));
        p.setPrice(readDouble("Nou preu: "));
        p.setStock(readInt("Nou estoc: "));
        ProductDAO.update(p);
        System.out.println("Producte actualitzat.");
    }

    private static void deleteProduct() throws SQLException {
        int id = readInt("Id del producte: ");
        if (ProductDAO.get(id) == null) {
            System.out.println("No existeix cap producte amb aquest id.");
            return;
        }
        ProductDAO.delete(id);
        System.out.println("Producte eliminat.");
    }

    private static void listClients() throws SQLException {
        System.out.println("\n--- LLISTA DE CLIENTS ---");
        for (Client c : ClientDAO.list()) {
            System.out.printf("%d - %s - %s%n", c.getId(), c.getName(), c.getEmail());
        }
    }

    private static void addClient() throws SQLException {
        String name = readString("Nom: ");
        String email = readString("Correu: ");
        ClientDAO.insert(new Client(0, name, email));
        System.out.println("Client afegit.");
    }

    private static void updateClient() throws SQLException {
        int id = readInt("Id del client: ");
        Client c = ClientDAO.get(id);
        if (c == null) {
            System.out.println("No existeix cap client amb aquest id.");
            return;
        }
        c.setName(readString("Nou nom: "));
        c.setEmail(readString("Nou correu: "));
        ClientDAO.update(c);
        System.out.println("Client actualitzat.");
    }

    private static void deleteClient() throws SQLException {
        int id = readInt("Id del client: ");
        if (ClientDAO.get(id) == null) {
            System.out.println("No existeix cap client amb aquest id.");
            return;
        }
        ClientDAO.delete(id);
        System.out.println("Client eliminat.");
    }

    private static void listDiscounts() throws SQLException {
        System.out.println("\n--- LLISTA DE DESCOMPTES ---");
        for (Discount d : DiscountDAO.list()) {
            System.out.printf("%d - Producte %d - %s%.2f%n",
                    d.getId(), d.getProductId(), d.getType(), d.getAmount());
        }
    }

    private static void addDiscount() throws SQLException {
        int productId = readInt("Id del producte: ");
        if (ProductDAO.get(productId) == null) {
            System.out.println("No existeix cap producte amb aquest id.");
            return;
        }
        String type = readString("Tipus (% o €): ");
        double amount = readDouble("Valor: ");
        DiscountDAO.insert(new Discount(0, productId, type, amount));
        System.out.println("Descompte afegit.");
    }

    private static void updateDiscount() throws SQLException {
        int id = readInt("Id del descompte: ");
        Discount d = DiscountDAO.get(id);
        if (d == null) {
            System.out.println("No existeix cap descompte amb aquest id.");
            return;
        }
        d.setType(readString("Nou tipus (% o €): "));
        d.setAmount(readDouble("Nou valor: "));
        DiscountDAO.update(d);
        System.out.println("Descompte actualitzat.");
    }

    private static void deleteDiscount() throws SQLException {
        int id = readInt("Id del descompte: ");
        if (DiscountDAO.get(id) == null) {
            System.out.println("No existeix cap descompte amb aquest id.");
            return;
        }
        DiscountDAO.delete(id);
        System.out.println("Descompte eliminat.");
    }

    private static void createOrder() throws SQLException {
        int clientId = readInt("Id del client: ");
        if (ClientDAO.get(clientId) == null) {
            System.out.println("No existeix cap client amb aquest id.");
            return;
        }

        listProducts();
        List<OrderLine> lines = new ArrayList<>();
        while (true) {
            int productId = readInt("Id del producte (0 per acabar): ");
            if (productId == 0) {
                break;
            }
            Product p = ProductDAO.get(productId);
            if (p == null) {
                System.out.println("No existeix cap producte amb aquest id.");
                continue;
            }
            int quantity = readInt("Quantitat: ");
            if (quantity <= 0) {
                System.out.println("La quantitat ha de ser major que 0.");
                continue;
            }
            lines.add(new OrderLine(productId, quantity));
        }

        if (lines.isEmpty()) {
            System.out.println("No s'ha afegit cap línia. Comanda cancel·lada.");
            return;
        }

        int orderId = OrderDAO.create(clientId, lines);
        System.out.println("Comanda " + orderId + " creada correctament.");
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        if (!sc.hasNextLine()) {
            System.out.println();
            System.exit(0);
        }
        return sc.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readString(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Introdueix un número enter vàlid.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readString(prompt).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Introdueix un número vàlid.");
            }
        }
    }
}
