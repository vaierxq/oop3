package restaurant.management;

public class FoodItem {
    private String name;
    private double calories;
    private String type;

    public FoodItem(String name, double calories, String type) {
        this.name = name;
        this.calories = calories;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public double getCalories() {
        return calories;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + " (" + type + ") - " + calories + " ккал";
    }
}
