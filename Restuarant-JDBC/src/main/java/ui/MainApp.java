package ui;

import config.DatabaseConfig;
import dao.MenuDAO;
import dao.RestaurantDAO;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;

public class MainApp extends Application {
    private BorderPane mainLayout;
    private RestaurantDAO restaurantDAO;
    private MenuDAO menuDAO;
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize Database
            connection = DatabaseConfig.getConnection();
            restaurantDAO = new RestaurantDAO(connection);
            menuDAO = new MenuDAO(connection);
            
            // Setup tables (Seed data)
            restaurantDAO.setupTables();

            mainLayout = new BorderPane();
            
            // Sidebar
            VBox sidebar = createSidebar();
            mainLayout.setLeft(sidebar);

            // Default View
            showRestaurantView();

            Scene scene = new Scene(mainLayout, 1100, 700);
            String css = getClass().getResource("/ui/style.css").toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setTitle("Restaurant Management System Pro");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            // Handle fatal startup error
        }
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");

        Label title = new Label("RESTAURANT\nSYS PRO");
        title.getStyleClass().add("sidebar-title");
        title.setAlignment(Pos.CENTER);

        Button restBtn = new Button("Manage Restaurants");
        restBtn.getStyleClass().add("nav-button");
        restBtn.setMaxWidth(Double.MAX_VALUE);
        restBtn.setOnAction(e -> showRestaurantView());

        Button menuBtn = new Button("Manage Menu Items");
        menuBtn.getStyleClass().add("nav-button");
        menuBtn.setMaxWidth(Double.MAX_VALUE);
        menuBtn.setOnAction(e -> showMenuView());

        sidebar.getChildren().addAll(title, restBtn, menuBtn);
        return sidebar;
    }

    private void showRestaurantView() {
        mainLayout.setCenter(new RestaurantView(restaurantDAO));
    }

    private void showMenuView() {
        mainLayout.setCenter(new MenuView(menuDAO, restaurantDAO));
    }

    @Override
    public void stop() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
