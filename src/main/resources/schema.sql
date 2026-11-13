-- H2 schema. Recreated on every run so the sample data is always available.

DROP TABLE IF EXISTS OrderLines;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Discounts;
DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS Clients;

CREATE TABLE Clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE Products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL
);

CREATE TABLE Orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (client_id) REFERENCES Clients(id)
);

CREATE TABLE OrderLines (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(id),
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

CREATE TABLE Discounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    type VARCHAR(1) NOT NULL CHECK (type IN ('%', '€')),
    amount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

INSERT INTO Clients (name, email) VALUES
    ('Anna', 'anna@example.com'),
    ('Pere', 'pere@example.com');

INSERT INTO Products (name, price, stock) VALUES
    ('Mouse', 19.99, 20),
    ('Keyboard', 29.99, 15),
    ('Monitor', 120.00, 10);

INSERT INTO Discounts (product_id, type, amount) VALUES
    (1, '%', 10),
    (3, '€', 20);
