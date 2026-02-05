// ════════════════════════════════════════════════════════
// 3. RestaurantController.java - Controller
// ════════════════════════════════════════════════════════
package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private DBHelper db;

    // GET All
    @GetMapping
    public List<Restaurant> getAll() {
        return db.getAllRestaurants();
    }

    // GET ById
    @GetMapping("/{id}")
    public Restaurant getById(@PathVariable int id) {
        return db.getRestaurantById(id);
    }

    // POST Create
    @PostMapping
    public Restaurant create(@RequestBody Restaurant restaurant) {
        return db.createRestaurant(restaurant);
    }

    // PUT Update
    @PutMapping("/{id}")
    public String update(@PathVariable int id, @RequestBody Restaurant restaurant) {
        boolean success = db.updateRestaurant(id, restaurant);
        return success ? "Updated" : "Failed";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        boolean success = db.deleteRestaurant(id);
        return success ? "Deleted" : "Failed";
    }
}