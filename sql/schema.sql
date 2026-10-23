-- Create database
CREATE DATABASE IF NOT EXISTS store;
USE store;

-- Clients table
CREATE TABLE Clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- Products table
CREATE TABLE Products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL
);

-- Orders table
CREATE TABLE Orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (client_id) REFERENCES Clients(id)
);

-- Order lines table
CREATE TABLE OrderLines (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(id),
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

-- Discounts table (per product)
CREATE TABLE Discounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    type ENUM('%', '€') NOT NULL,
    value DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

-- Insert test clients
INSERT INTO Clients (name, email) VALUES
    ('Anna', 'anna@example.com'),
    ('Pere', 'pere@example.com');

-- Insert test products
INSERT INTO Products (name, price, stock) VALUES
    ('Mouse', 19.99, 20),
    ('Keyboard', 29.99, 15),
    ('Monitor', 120.00, 10);

-- Insert test discounts
INSERT INTO Discounts (product_id, type, value) VALUES
    (1, '%', 10),  -- 10% discount on the Mouse
    (3, '€', 20);  -- €20 discount on the Monitor