public class Transaction {
    private int id;
    private String date;
    private String description;
    private String category;
    private double amount;
    private String type;

    public Transaction(int id, String date, String description, String category, double amount, String type) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.type = type;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
}