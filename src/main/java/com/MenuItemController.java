// ════════════════════════════════════════════════════════
// 4. MenuItemController.java - Controller
// ════════════════════════════════════════════════════════
package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuItemController {

    @Autowired
    private DBHelper db;

    // GET All
    @GetMapping
    public List<MenuItem> getAll() {
        return db.getAllMenuItems();
    }

    // GET ById
    @GetMapping("/{id}")
    public MenuItem getById(@PathVariable int id) {
        return db.getMenuItemById(id);
    }

    // POST Create
    @PostMapping
    public MenuItem create(@RequestBody MenuItem item) {
        return db.createMenuItem(item);
    }

    // PUT Update
    @PutMapping("/{id}")
    public String update(@PathVariable int id, @RequestBody MenuItem item) {
        boolean success = db.updateMenuItem(id, item);
        return success ? "Updated" : "Failed";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        boolean success = db.deleteMenuItem(id);
        return success ? "Deleted" : "Failed";
    }
}