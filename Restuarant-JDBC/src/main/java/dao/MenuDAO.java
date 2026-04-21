package dao;

import models.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {
    private Connection conn;

    public MenuDAO(Connection conn) {
        this.conn = conn;
    }

    public List<MenuItem> getAll() throws SQLException {
        List<MenuItem> list = new ArrayList<>();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Menu ORDER BY price");
        while (rs.next()) {
            list.add(new MenuItem(rs.getInt("item_id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("rest_id")));
        }
        return list;
    }

    public List<MenuItem> getByRestaurant(int restId) throws SQLException {
        List<MenuItem> list = new ArrayList<>();
        String sql = "SELECT * FROM Menu WHERE rest_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, restId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MenuItem(rs.getInt("item_id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("rest_id")));
            }
        }
        return list;
    }

    public void add(MenuItem item) throws SQLException {
        String sql = "INSERT INTO Menu (item_id, name, price, rest_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getId());
            pstmt.setString(2, item.getName());
            pstmt.setDouble(3, item.getPrice());
            pstmt.setInt(4, item.getRestId());
            pstmt.executeUpdate();
        }
    }

    public void update(MenuItem item) throws SQLException {
        String sql = "UPDATE Menu SET name = ?, price = ?, rest_id = ? WHERE item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setDouble(2, item.getPrice());
            pstmt.setInt(3, item.getRestId());
            pstmt.setInt(4, item.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Menu WHERE item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public void updateBudgetPrices() throws SQLException {
        String sql = "UPDATE Menu SET price = 200 WHERE price <= 100";
        conn.createStatement().executeUpdate(sql);
    }
}
