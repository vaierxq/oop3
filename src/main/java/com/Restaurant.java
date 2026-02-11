package com;

public class Restaurant {
    private int id;
    private String name;
    private String address;
    private String phone;

    // ✅ нужен Jackson
    public Restaurant() {}

    public Restaurant(int id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Restaurant(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }

    // ✅ нужны Jackson
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return "Ресторан #" + id + " | " + name + " | " + address + " | " + phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;
        Restaurant r = (Restaurant) o;
        return id == r.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
