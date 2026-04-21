package ui;

import dao.MenuDAO;
import dao.RestaurantDAO;
import models.MenuItem;
import models.Restaurant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.sql.SQLException;

public class MenuView extends VBox {
    private final MenuDAO menuDao;
    private final RestaurantDAO restDao;
    private final TableView<MenuItem> table = new TableView<>();
    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField priceField = new TextField();
    private final ComboBox<Restaurant> restCombo = new ComboBox<>();
    private final TextField searchField = new TextField();
    private final ObservableList<MenuItem> data = FXCollections.observableArrayList();
    private javafx.collections.transformation.FilteredList<MenuItem> filteredData;

    public MenuView(MenuDAO menuDao, RestaurantDAO restDao) {
        this.menuDao = menuDao;
        this.restDao = restDao;
        setSpacing(20);
        setPadding(new Insets(0));
        getStyleClass().add("content-area");

        Label header = new Label("Menu Management");
        header.getStyleClass().add("header-text");

        setupTable();
        setupSearch();
        refreshRestaurants();
        
        getChildren().addAll(header, searchField, table, createFormLayout());
        refreshData();
    }

    private void setupSearch() {
        searchField.setPromptText("🔍 Search by item name...");
        searchField.setMaxWidth(400);
        
        filteredData = new javafx.collections.transformation.FilteredList<>(data, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return item.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        table.setItems(filteredData);
    }

    private void setupTable() {
        TableColumn<MenuItem, Integer> idCol = new TableColumn<>("Item ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        idCol.setPrefWidth(100);

        TableColumn<MenuItem, String> nameCol = new TableColumn<>("Item Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameCol.setPrefWidth(200);

        TableColumn<MenuItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        priceCol.setPrefWidth(100);

        TableColumn<MenuItem, Integer> restIdCol = new TableColumn<>("Restaurant ID");
        restIdCol.setCellValueFactory(cellData -> cellData.getValue().restIdProperty().asObject());
        restIdCol.setPrefWidth(120);

        table.getColumns().addAll(idCol, nameCol, priceCol, restIdCol);
        table.setItems(data);
        table.setPlaceholder(new Label("No menu items found"));

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                idField.setText(String.valueOf(newVal.getId()));
                nameField.setText(newVal.getName());
                priceField.setText(String.valueOf(newVal.getPrice()));
                idField.setEditable(false);
                
                // Select restaurant in combo
                restCombo.getItems().stream()
                    .filter(r -> r.getId() == newVal.getRestId())
                    .findFirst()
                    .ifPresent(rest -> restCombo.setValue(rest));
            }
        });
    }

    private GridPane createFormLayout() {
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);

        idField.setPromptText("Enter ID");
        nameField.setPromptText("Enter Item Name");
        priceField.setPromptText("Enter Price");
        restCombo.setPromptText("Select Restaurant");
        restCombo.setMaxWidth(Double.MAX_VALUE);

        form.add(new Label("Item ID:"), 0, 0);
        form.add(idField, 1, 0);
        form.add(new Label("Item Name:"), 0, 1);
        form.add(nameField, 1, 1);
        form.add(new Label("Price:"), 0, 2);
        form.add(priceField, 1, 2);
        form.add(new Label("Restaurant:"), 0, 3);
        form.add(restCombo, 1, 3);

        HBox actions = new HBox(10);
        actions.setPadding(new Insets(10, 0, 0, 0));
        
        Button addBtn = new Button("Add Key");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setOnAction(e -> handleAdd());

        Button updateBtn = new Button("Update");
        updateBtn.getStyleClass().add("btn-primary");
        updateBtn.setOnAction(e -> handleUpdate());

        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().add("btn-danger");
        deleteBtn.setOnAction(e -> handleDelete());

        Button budgetBtn = new Button("Bulk Fix Price");
        budgetBtn.getStyleClass().add("btn-clear");
        budgetBtn.setTooltip(new Tooltip("Set all items with price <= 100 to 200"));
        budgetBtn.setOnAction(e -> handleBudgetUpdate());

        Button clearBtn = new Button("Clear");
        clearBtn.getStyleClass().add("btn-clear");
        clearBtn.setOnAction(e -> clearFields());

        actions.getChildren().addAll(addBtn, updateBtn, deleteBtn, budgetBtn, clearBtn);
        form.add(actions, 1, 4);

        return form;
    }

    private void handleAdd() {
        try {
            if (validateInput()) {
                MenuItem item = new MenuItem(
                    Integer.parseInt(idField.getText()), 
                    nameField.getText(), 
                    Double.parseDouble(priceField.getText()), 
                    restCombo.getValue().getId()
                );
                menuDao.add(item);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Menu item added!");
                refreshData();
                clearFields();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid ID or Price");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    private void handleUpdate() {
        try {
            if (validateInput()) {
                MenuItem item = new MenuItem(
                    Integer.parseInt(idField.getText()), 
                    nameField.getText(), 
                    Double.parseDouble(priceField.getText()), 
                    restCombo.getValue().getId()
                );
                menuDao.update(item);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Menu item updated!");
                refreshData();
                clearFields();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    private void handleDelete() {
        MenuItem selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select an item to delete");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Item: " + selected.getName());
        confirm.setContentText("Are you sure?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                menuDao.delete(selected.getId());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Item deleted.");
                refreshData();
                clearFields();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
            }
        }
    }

    private void handleBudgetUpdate() {
        try {
            menuDao.updateBudgetPrices();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Budget prices (<= 100) updated to 200.");
            refreshData();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    private void refreshData() {
        try {
            data.setAll(menuDao.getAll());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error Loading Data", e.getMessage());
        }
    }

    private void refreshRestaurants() {
        try {
            restCombo.setItems(FXCollections.observableArrayList(restDao.getAll()));
            // Custom cell factory to show Name instead of just toString
            restCombo.setCellFactory(lv -> new ListCell<Restaurant>() {
                @Override
                protected void updateItem(Restaurant item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? "" : item.getName() + " (ID: " + item.getId() + ")");
                }
            });
            restCombo.setButtonCell(restCombo.getCellFactory().call(null));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load restaurants");
        }
    }

    private boolean validateInput() {
        if (idField.getText().isEmpty() || nameField.getText().isEmpty() || 
            priceField.getText().isEmpty() || restCombo.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required");
            return false;
        }
        return true;
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        priceField.clear();
        restCombo.setValue(null);
        idField.setEditable(true);
        table.getSelectionModel().clearSelection();
        refreshRestaurants(); // Refresh list in case restaurants were added/deleted
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
