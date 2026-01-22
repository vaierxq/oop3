package restaurant.management;

public class MenuItem {
    private int id;
    private String name;
    private double price;
    private String category;

    public MenuItem(int id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public MenuItem(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return id + " | " + name + " - " + price + " тг (" + category + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItem)) return false;
        MenuItem item = (MenuItem) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
