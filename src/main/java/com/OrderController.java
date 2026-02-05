// ════════════════════════════════════════════════════════
// 5. OrderController.java - Controller
// ════════════════════════════════════════════════════════
package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private DBHelper db;

    // GET All
    @GetMapping
    public List<Order> getAll() {
        return db.getAllOrders();
    }

    // GET ById
    @GetMapping("/{id}")
    public Order getById(@PathVariable int id) {
        return db.getOrderById(id);
    }

    // POST Create
    @PostMapping
    public Order create(@RequestBody Order order) {
        return db.createOrder(order);
    }

    // PUT Update
    @PutMapping("/{id}")
    public String update(@PathVariable int id, @RequestBody Order order) {
        boolean success = db.updateOrder(id, order);
        return success ? "Updated" : "Failed";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        boolean success = db.deleteOrder(id);
        return success ? "Deleted" : "Failed";
    }
}