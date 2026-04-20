import java.sql.*;

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
        stmt.executeUpdate("INSERT IGNORE INTO Menu VALUES (101, 'Margherita', 99, 1)");
    }

    public void viewRestaurants() throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Restaurant");
        System.out.println("\n--- ALL RESTAURANTS ---");
        System.out.printf("%-5s | %-20s | %-20s\n", "ID", "Name", "Area");
        while (rs.next()) {
            System.out.printf("%-5d | %-20s | %-20s\n", rs.getInt(1), rs.getString(2), rs.getString(3));
        }
    }

    public void viewAllMenu() throws SQLException {
        String sql = "SELECT m.item_id, m.name, m.price, r.name FROM Menu m JOIN Restaurant r ON m.rest_id = r.rest_id ORDER BY m.price";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        System.out.println("\n--- ALL MENU ITEMS ---");
        System.out.printf("%-5s | %-15s | %-8s | %-15s\n", "ID", "Item", "Price", "Restaurant");
        while (rs.next()) {
            System.out.printf("%-5d | %-15s | %-8.2f | %-15s\n", rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4));
        }
    }

    public void viewMenuByRestaurant(int restId) throws SQLException {
        String sql = "SELECT name, price FROM Menu WHERE rest_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, restId);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Items for Restaurant ID " + restId + ":");
            while (rs.next()) System.out.println("- " + rs.getString(1) + " ($" + rs.getDouble(2) + ")");
        }
    }

    public void updatePrices() throws SQLException {
        String sql = "UPDATE Menu SET price = 200 WHERE price <= 100";
        int count = conn.createStatement().executeUpdate(sql);
        System.out.println("Success: Updated " + count + " budget items to 200.");
    }

    public void addRestaurant(int id, String name, String area) throws SQLException {
        String sql = "INSERT INTO Restaurant VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id); pstmt.setString(2, name); pstmt.setString(3, area);
            pstmt.executeUpdate();
            System.out.println("Restaurant '" + name + "' added.");
        }
    }

    public void addMenuItem(int itemId, String name, double price, int restId) throws SQLException {
        String sql = "INSERT INTO Menu VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId); pstmt.setString(2, name);
            pstmt.setDouble(3, price); pstmt.setInt(4, restId);
            pstmt.executeUpdate();
            System.out.println("Item '" + name + "' added to menu.");
        }
    }

    public void deleteRestaurant(int id) throws SQLException {
        String sql = "DELETE FROM Restaurant WHERE rest_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int count = pstmt.executeUpdate();
            if (count > 0) System.out.println("Restaurant deleted.");
            else System.out.println("Restaurant ID not found.");
        }
    }
}