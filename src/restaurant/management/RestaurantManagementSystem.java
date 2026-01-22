package restaurant.management;

import java.sql.*;
import java.util.Scanner;


public class RestaurantManagementSystem {
    private static final String URL = "jdbc:postgresql://localhost:5432/restaurant_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== RESTAURANT MANAGEMENT ===");
            System.out.println("1. Показать меню");
            System.out.println("2. Добавить блюдо");
            System.out.println("3. Обновить цену");
            System.out.println("4. Удалить блюдо");
            System.out.println("5. Показать заказы");
            System.out.println("6. Создать заказ");
            System.out.println("7. Обновить статус заказа");
            System.out.println("0. Выход");
            System.out.print("Выбор: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                showMenu();
            } else if (choice == 2) {
                System.out.print("Название: ");
                String name = sc.nextLine();
                System.out.print("Цена: ");
                double price = sc.nextDouble();
                sc.nextLine();
                System.out.print("Категория: ");
                String category = sc.nextLine();
                addMenuItem(name, price, category);
            } else if (choice == 3) {
                System.out.print("ID блюда: ");
                int id = sc.nextInt();
                System.out.print("Новая цена: ");
                double price = sc.nextDouble();
                updatePrice(id, price);
            } else if (choice == 4) {
                System.out.print("ID блюда: ");
                int id = sc.nextInt();
                deleteMenuItem(id);
            } else if (choice == 5) {
                showOrders();
            } else if (choice == 6) {
                System.out.print("Имя клиента: ");
                String customerName = sc.nextLine();
                System.out.print("Сумма: ");
                double amount = sc.nextDouble();
                sc.nextLine();
                System.out.print("Статус: ");
                String status = sc.nextLine();
                addOrder(customerName, amount, status);
            } else if (choice == 7) {
                System.out.print("ID заказа: ");
                int id = sc.nextInt();
                sc.nextLine();
                System.out.print("Новый статус: ");
                String status = sc.nextLine();
                updateOrderStatus(id, status);
            } else if (choice == 0) {
                break;
            }
        }
        sc.close();
    }

    // Показать меню
    public static void showMenu() {
        try {
            Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM menu_items");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " +
                        rs.getString("name") + " - " +
                        rs.getDouble("price") + " тг (" +
                        rs.getString("category") + ")");
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Добавить блюдо
    public static void addMenuItem(String name, double price, String category) {
        try {
            Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO menu_items (name, price, category) VALUES (?, ?, ?)"
            );
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setString(3, category);
            ps.executeUpdate();
            System.out.println("✓ Блюдо добавлено!");
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Обновить цену
    public static void updatePrice(int id, double price) {
        try {
            Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = c.prepareStatement(
                    "UPDATE menu_items SET price = ? WHERE id = ?"
            );
            ps.setDouble(1, price);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("✓ Цена обновлена!");
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Удалить блюдо
    public static void deleteMenuItem(int id) {
        try {
            Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = c.prepareStatement("DELETE FROM menu_items WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("✓ Блюдо удалено!");
            c.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // Показать заказы
    public static void showOrders() {
        try {
            Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM orders");
            while (rs.next()) {
                System.out.println("Заказ #" + rs.getInt("id") +
                        " | " + rs.getString("customer_name") +
                        " - " + rs.getDouble("total_amount") + " тг [" +
                        rs.getString("status") + "]");
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Добавить заказ
    public static void addOrder(String customerName, double amount, String status) {
        try {
            Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO orders (customer_name, total_amount, status) VALUES (?, ?, ?)"
            );
            ps.setString(1, customerName);
            ps.setDouble(2, amount);
            ps.setString(3, status);
            ps.executeUpdate();
            System.out.println("✓ Заказ создан!");
            c.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Обновить статус заказа
    public static void updateOrderStatus(int id, String status) {
        try {
            Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = c.prepareStatement(
                    "UPDATE orders SET status = ? WHERE id = ?"
            );
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("✓ Статус обновлен!");
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
