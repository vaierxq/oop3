package restaurant.management;

public class Order {
    private int id;
    private String customerName;
    private double totalAmount;
    private String status;

    public Order(int id, String customerName, double totalAmount, String status) {
        this.id = id;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Order(String customerName, double totalAmount, String status) {
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Заказ #" + id + " | " + customerName + " - " + totalAmount + " тг [" + status + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
