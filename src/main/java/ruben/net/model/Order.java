package ruben.net.model;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int clientId;
    private Timestamp date;
    private double total;

    // Constructors
    public Order() {}
    public Order(int id, int clientId, Timestamp date, double total) {
        this.id = id;
        this.clientId = clientId;
        this.date = date;
        this.total = total;
    }

    // Getters and setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getClientId() {return clientId;}
    public void setClientId(int clientId) {this.clientId = clientId;}
    public Timestamp getDate() {return date;}
    public void setDate(Timestamp date) {this.date = date;}
    public double getTotal() {return total;}
    public void setTotal(double total) {this.total = total;}
}
