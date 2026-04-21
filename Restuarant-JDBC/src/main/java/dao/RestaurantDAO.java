package dao;

import models.Restaurant;
import models.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDAO {
    private Connection conn;

    public RestaurantDAO(Connection conn) {
        this.conn = conn;
    }

    public void setupTables() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS Restaurant (rest_id INT PRIMARY KEY, name VARCHAR(50), area VARCHAR(50))");
        stmt.execute("CREATE TABLE IF NOT EXISTS Menu (item_id INT PRIMARY KEY, name VARCHAR(50), price DOUBLE, rest_id INT, FOREIGN KEY (rest_id) REFERENCES Restaurant(rest_id) ON DELETE CASCADE)");
        
        // Seed data
        stmt.executeUpdate("INSERT IGNORE INTO Restaurant VALUES (1, 'Pizza Hut', 'Downtown')");
        stmt.executeUpdate("INSERT IGNORE INTO Restaurant VALUES (2, 'Burger King', 'Midtown')");
    }

    public List<Restaurant> getAll() throws SQLException {
        List<Restaurant> list = new ArrayList<>();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Restaurant");
        while (rs.next()) {
            list.add(new Restaurant(rs.getInt("rest_id"), rs.getString("name"), rs.getString("area")));
        }
        return list;
    }

    public void add(Restaurant r) throws SQLException {
        String sql = "INSERT INTO Restaurant (rest_id, name, area) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, r.getId());
            pstmt.setString(2, r.getName());
            pstmt.setString(3, r.getArea());
            pstmt.executeUpdate();
        }
    }

    public void update(Restaurant r) throws SQLException {
        String sql = "UPDATE Restaurant SET name = ?, area = ? WHERE rest_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, r.getName());
            pstmt.setString(2, r.getArea());
            pstmt.setInt(3, r.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Restaurant WHERE rest_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // --- CLI Helper Methods for RestaurantApp ---

    public void viewRestaurants() throws SQLException {
        List<Restaurant> list = getAll();
        System.out.println("\n--- Restaurants ---");
        for (Restaurant r : list) {
            System.out.println("ID: " + r.getId() + " | Name: " + r.getName() + " | Area: " + r.getArea());
        }
    }

    public void viewAllMenu() throws SQLException {
        MenuDAO menuDAO = new MenuDAO(conn);
        List<models.MenuItem> list = menuDAO.getAll();
        System.out.println("\n--- All Menu Items ---");
        for (models.MenuItem item : list) {
            System.out.println("ID: " + item.getId() + " | Name: " + item.getName() + " | Price: " + item.getPrice() + " | RestID: " + item.getRestId());
        }
    }

    public void viewMenuByRestaurant(int restId) throws SQLException {
        MenuDAO menuDAO = new MenuDAO(conn);
        List<models.MenuItem> list = menuDAO.getByRestaurant(restId);
        System.out.println("\n--- Menu for Restaurant " + restId + " ---");
        for (models.MenuItem item : list) {
            System.out.println("ID: " + item.getId() + " | Name: " + item.getName() + " | Price: " + item.getPrice());
        }
    }

    public void updatePrices() throws SQLException {
        MenuDAO menuDAO = new MenuDAO(conn);
        menuDAO.updateBudgetPrices();
        System.out.println("Successfully updated budget prices (<=100) to 200.");
    }

    public void addRestaurant(int id, String name, String area) throws SQLException {
        add(new Restaurant(id, name, area));
        System.out.println("Restaurant added successfully.");
    }

    public void deleteRestaurant(int id) throws SQLException {
        delete(id);
        System.out.println("Restaurant deleted successfully.");
    }

    public void addMenuItem(int id, String name, double price, int restId) throws SQLException {
        MenuDAO menuDAO = new MenuDAO(conn);
        menuDAO.add(new models.MenuItem(id, name, price, restId));
        System.out.println("Menu item added successfully.");
    }
}
