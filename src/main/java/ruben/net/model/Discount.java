package ruben.net.model;

public class Discount {
    private int id;
    private int productId;
    private String type;
    private double amount;

    // Constructors
    public Discount() {}
    public Discount(int id, int productId, String type, double amount) {
        this.id = id;
        this.productId = productId;
        this.type = type;
        this.amount = amount;
    }

    // Getters and setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getProductId() {return productId;}
    public void setProductId(int productId) {this.productId = productId;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}
}
