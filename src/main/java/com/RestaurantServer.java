package com;// main.java.com.RestaurantServer.java

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantServer {
    private static final String URL = "jdbc:postgresql://localhost:5432/restaurant_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";  // ПОМЕНЯЙ НА СВОЙ!
    
    public static void main(String[] args) throws IOException {
        // Создаем сервер на порту 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // Регистрируем endpoints
        server.createContext("/api/menu", new MenuHandler());
        server.createContext("/api/orders", new OrderHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("════════════════════════════════════════");
        System.out.println("✓ REST API Server запущен!");
        System.out.println("   URL: http://localhost:8080");
        System.out.println("════════════════════════════════════════");
        System.out.println("\nДоступные endpoints:");
        System.out.println("  GET  http://localhost:8080/api/menu     - получить все блюда");
        System.out.println("  POST http://localhost:8080/api/menu     - добавить блюдо");
        System.out.println("  GET  http://localhost:8080/api/orders   - получить все заказы");
        System.out.println("  POST http://localhost:8080/api/orders   - создать заказ");
        System.out.println("════════════════════════════════════════\n");
    }
    
    // Handler для работы с меню (MenuItem)
    static class MenuHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String response = "";
            int statusCode = 200;
            
            if (method.equals("GET")) {
                // GET - получить все блюда из БД
                response = getAllMenuItems();
                statusCode = 200;
                
            } else if (method.equals("POST")) {
                // POST - добавить новое блюдо в БД
                String requestBody = readRequestBody(exchange);
                response = createMenuItem(requestBody);
                statusCode = 201;
                
            } else {
                response = "{\"error\":\"Method not allowed. Use GET or POST\"}";
                statusCode = 405;
            }
            
            sendJsonResponse(exchange, statusCode, response);
        }
        
        // Получить все MenuItem из БД
        private String getAllMenuItems() {
            List<String> items = new ArrayList<>();
            
            try {
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM menu_items ORDER BY id");
                
                while (rs.next()) {
                    // Создаем JSON для каждого MenuItem
                    MenuItem item = new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("category")
                    );
                    
                    String json = String.format(
                        "{\"id\":%d,\"name\":\"%s\",\"price\":%.2f,\"category\":\"%s\"}",
                        item.getId(),
                        item.getName(),
                        item.getPrice(),
                        item.getCategory()
                    );
                    items.add(json);
                }
                
                conn.close();
                
            } catch (SQLException e) {
                e.printStackTrace();
                return "{\"error\":\"Database error: " + e.getMessage() + "\"}";
            }
            
            // Возвращаем массив JSON
            return "[" + String.join(",", items) + "]";
        }
        
        // Создать новый MenuItem
        private String createMenuItem(String json) {
            try {
                // Парсим JSON
                String name = extractValue(json, "name");
                double price = Double.parseDouble(extractValue(json, "price"));
                String category = extractValue(json, "category");
                
                // Создаем объект MenuItem
                MenuItem newItem = new MenuItem(name, price, category);
                
                // Сохраняем в БД
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO menu_items (name, price, category) VALUES (?, ?, ?) RETURNING id"
                );
                ps.setString(1, newItem.getName());
                ps.setDouble(2, newItem.getPrice());
                ps.setString(3, newItem.getCategory());
                
                ResultSet rs = ps.executeQuery();
                int id = 0;
                if (rs.next()) {
                    id = rs.getInt(1);
                }
                conn.close();
                
                // Возвращаем JSON с созданным объектом
                return String.format(
                    "{\"success\":true,\"message\":\"MenuItem created\",\"data\":{\"id\":%d,\"name\":\"%s\",\"price\":%.2f,\"category\":\"%s\"}}",
                    id, name, price, category
                );
                
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"success\":false,\"error\":\"" + e.getMessage() + "\"}";
            }
        }
    }
    
    // Handler для работы с заказами (Order)
    static class OrderHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String response = "";
            int statusCode = 200;
            
            if (method.equals("GET")) {
                // GET - получить все заказы
                response = getAllOrders();
                statusCode = 200;
                
            } else if (method.equals("POST")) {
                // POST - создать новый заказ
                String requestBody = readRequestBody(exchange);
                response = createOrder(requestBody);
                statusCode = 201;
                
            } else {
                response = "{\"error\":\"Method not allowed. Use GET or POST\"}";
                statusCode = 405;
            }
            
            sendJsonResponse(exchange, statusCode, response);
        }
        
        // Получить все Order из БД
        private String getAllOrders() {
            List<String> orders = new ArrayList<>();
            
            try {
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM orders ORDER BY id");
                
                while (rs.next()) {
                    // Создаем объект Order
                    Order order = new Order(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getDouble("total_amount"),
                        rs.getString("status")
                    );
                    
                    String json = String.format(
                        "{\"id\":%d,\"customer_name\":\"%s\",\"total_amount\":%.2f,\"status\":\"%s\"}",
                        order.getId(),
                        order.getCustomerName(),
                        order.getTotalAmount(),
                        order.getStatus()
                    );
                    orders.add(json);
                }
                
                conn.close();
                
            } catch (SQLException e) {
                e.printStackTrace();
                return "{\"error\":\"Database error: " + e.getMessage() + "\"}";
            }
            
            return "[" + String.join(",", orders) + "]";
        }
        
        // Создать новый Order
        private String createOrder(String json) {
            try {
                // Парсим JSON
                String customerName = extractValue(json, "customer_name");
                double totalAmount = Double.parseDouble(extractValue(json, "total_amount"));
                String status = extractValue(json, "status");
                
                // Создаем объект Order
                Order newOrder = new Order(customerName, totalAmount, status);
                
                // Сохраняем в БД
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO orders (customer_name, total_amount, status) VALUES (?, ?, ?) RETURNING id"
                );
                ps.setString(1, newOrder.getCustomerName());
                ps.setDouble(2, newOrder.getTotalAmount());
                ps.setString(3, newOrder.getStatus());
                
                ResultSet rs = ps.executeQuery();
                int id = 0;
                if (rs.next()) {
                    id = rs.getInt(1);
                }
                conn.close();
                
                return String.format(
                    "{\"success\":true,\"message\":\"Order created\",\"data\":{\"id\":%d,\"customer_name\":\"%s\",\"total_amount\":%.2f,\"status\":\"%s\"}}",
                    id, customerName, totalAmount, status
                );
                
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"success\":false,\"error\":\"" + e.getMessage() + "\"}";
            }
        }
    }
    
    // ═══════════════════════════════════════════════════
    // Вспомогательные методы
    // ═══════════════════════════════════════════════════
    
    // Отправить JSON ответ
    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        
        byte[] bytes = response.getBytes("UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
    
    // Прочитать тело запроса
    private static String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        return body.toString();
    }
    
    // Извлечь значение из JSON (простой парсер)
    private static String extractValue(String json, String key) {
        // Ищем "key":"value"
        String searchWithQuotes = "\"" + key + "\":\"";
        int start = json.indexOf(searchWithQuotes);
        
        if (start != -1) {
            // Найдено значение в кавычках
            start += searchWithQuotes.length();
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        }
        
        // Ищем "key":value (без кавычек - для чисел)
        String searchWithoutQuotes = "\"" + key + "\":";
        start = json.indexOf(searchWithoutQuotes);
        
        if (start != -1) {
            start += searchWithoutQuotes.length();
            int end = json.indexOf(",", start);
            if (end == -1) {
                end = json.indexOf("}", start);
            }
            return json.substring(start, end).trim();
        }
        
        return "";
    }
}