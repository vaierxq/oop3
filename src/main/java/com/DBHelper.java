// ════════════════════════════════════════════════════════
// 2. DBHelper.java - Database
// ════════════════════════════════════════════════════════
package com;
import com.sun.source.tree.ImportTree;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;


@Component
public class DBHelper {
    private static final String URL = "jdbc:postgresql://localhost:5432/restaurant_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";

    // Connection
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ═══ RESTAURANT ═══

    // All
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> list = new ArrayList<>();
        try (Connection c = getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT * FROM restaurants")) {

            while (rs.next()) {
                list.add(new Restaurant(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ById
    public Restaurant getRestaurantById(int id) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM restaurants WHERE id = ?")) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Restaurant(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Create
    public Restaurant createRestaurant(Restaurant r) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO restaurants (name, address, phone) VALUES (?, ?, ?) RETURNING id")) {

            ps.setString(1, r.getName());
            ps.setString(2, r.getAddress());
            ps.setString(3, r.getPhone());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Restaurant(
                        rs.getInt("id"),
                        r.getName(),
                        r.getAddress(),
                        r.getPhone()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update
    public boolean updateRestaurant(int id, Restaurant r) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE restaurants SET name=?, address=?, phone=? WHERE id=?")) {

            ps.setString(1, r.getName());
            ps.setString(2, r.getAddress());
            ps.setString(3, r.getPhone());
            ps.setInt(4, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete
    public boolean deleteRestaurant(int id) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM restaurants WHERE id = ?")) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ═══ MENUITEM ═══

    // All
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> list = new ArrayList<>();
        try (Connection c = getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT * FROM menu_items")) {

            while (rs.next()) {
                list.add(new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ById
    public MenuItem getMenuItemById(int id) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM menu_items WHERE id = ?")) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("category")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Create
    public MenuItem createMenuItem(MenuItem item) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO menu_items (name, price, category) VALUES (?, ?, ?) RETURNING id")) {

            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            ps.setString(3, item.getCategory());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new MenuItem(
                        rs.getInt("id"),
                        item.getName(),
                        item.getPrice(),
                        item.getCategory()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update
    public boolean updateMenuItem(int id, MenuItem item) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE menu_items SET name=?, price=?, category=? WHERE id=?")) {

            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            ps.setString(3, item.getCategory());
            ps.setInt(4, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete
    public boolean deleteMenuItem(int id) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM menu_items WHERE id = ?")) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ═══ ORDER ═══

    // All
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        try (Connection c = getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT * FROM orders")) {

            while (rs.next()) {
                list.add(new Order(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getDouble("total_amount"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ById
    public Order getOrderById(int id) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM orders WHERE id = ?")) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Order(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getDouble("total_amount"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Create
    public Order createOrder(Order order) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO orders (customer_name, total_amount, status) VALUES (?, ?, ?) RETURNING id")) {

            ps.setString(1, order.getCustomerName());
            ps.setDouble(2, order.getTotalAmount());
            ps.setString(3, order.getStatus());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Order(
                        rs.getInt("id"),
                        order.getCustomerName(),
                        order.getTotalAmount(),
                        order.getStatus()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update
    public boolean updateOrder(int id, Order order) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE orders SET customer_name=?, total_amount=?, status=? WHERE id=?")) {

            ps.setString(1, order.getCustomerName());
            ps.setDouble(2, order.getTotalAmount());
            ps.setString(3, order.getStatus());
            ps.setInt(4, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete
    public boolean deleteOrder(int id) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM orders WHERE id = ?")) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
