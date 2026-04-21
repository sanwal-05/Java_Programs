package ui;

import dao.RestaurantDAO;
import models.Restaurant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.sql.SQLException;

public class RestaurantView extends VBox {
    private final RestaurantDAO dao;
    private final TableView<Restaurant> table = new TableView<>();
    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField areaField = new TextField();
    private final TextField searchField = new TextField();
    private final ObservableList<Restaurant> data = FXCollections.observableArrayList();
    private javafx.collections.transformation.FilteredList<Restaurant> filteredData;

    public RestaurantView(RestaurantDAO dao) {
        this.dao = dao;
        setSpacing(20);
        setPadding(new Insets(0));
        getStyleClass().add("content-area");

        Label header = new Label("Restaurant Management");
        header.getStyleClass().add("header-text");

        setupTable();
        setupSearch();
        setupForm();

        getChildren().addAll(header, searchField, table, createFormLayout());
        refreshData();
    }

    private void setupSearch() {
        searchField.setPromptText("🔍 Search by name or area...");
        searchField.setMaxWidth(400);

        filteredData = new javafx.collections.transformation.FilteredList<>(data, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(restaurant -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return restaurant.getName().toLowerCase().contains(lowerCaseFilter) ||
                        restaurant.getArea().toLowerCase().contains(lowerCaseFilter);
            });
        });
        table.setItems(filteredData);
    }

    private void setupTable() {
        TableColumn<Restaurant, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        idCol.setPrefWidth(100);

        TableColumn<Restaurant, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameCol.setPrefWidth(250);

        TableColumn<Restaurant, String> areaCol = new TableColumn<>("Area");
        areaCol.setCellValueFactory(cellData -> cellData.getValue().areaProperty());
        areaCol.setPrefWidth(250);

        table.getColumns().addAll(idCol, nameCol, areaCol);
        table.setItems(data);
        table.setPlaceholder(new Label("No restaurants found"));

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                idField.setText(String.valueOf(newVal.getId()));
                nameField.setText(newVal.getName());
                areaField.setText(newVal.getArea());
                idField.setEditable(false); // ID shouldn't be edited
            }
        });
    }

    private GridPane createFormLayout() {
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);

        idField.setPromptText("Enter ID");
        nameField.setPromptText("Enter Name");
        areaField.setPromptText("Enter Area");

        form.add(new Label("Restaurant ID:"), 0, 0);
        form.add(idField, 1, 0);
        form.add(new Label("Name:"), 0, 1);
        form.add(nameField, 1, 1);
        form.add(new Label("Area:"), 0, 2);
        form.add(areaField, 1, 2);

        HBox actions = new HBox(10);
        actions.setPadding(new Insets(10, 0, 0, 0));

        Button addBtn = new Button("Add New");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setOnAction(e -> handleAdd());

        Button updateBtn = new Button("Update");
        updateBtn.getStyleClass().add("btn-primary");
        updateBtn.setOnAction(e -> handleUpdate());

        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().add("btn-danger");
        deleteBtn.setOnAction(e -> handleDelete());

        Button clearBtn = new Button("Clear");
        clearBtn.getStyleClass().add("btn-clear");
        clearBtn.setOnAction(e -> clearFields());

        actions.getChildren().addAll(addBtn, updateBtn, deleteBtn, clearBtn);
        form.add(actions, 1, 3);

        return form;
    }

    private void setupForm() {
        // Additional setup if needed
    }

    private void handleAdd() {
        try {
            if (validateInput()) {
                Restaurant r = new Restaurant(Integer.parseInt(idField.getText()), nameField.getText(),
                        areaField.getText());
                dao.add(r);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Restaurant added successfully!");
                refreshData();
                clearFields();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "ID must be a number");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    private void handleUpdate() {
        try {
            if (validateInput()) {
                Restaurant r = new Restaurant(Integer.parseInt(idField.getText()), nameField.getText(),
                        areaField.getText());
                dao.update(r);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Restaurant updated successfully!");
                refreshData();
                clearFields();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    private void handleDelete() {
        Restaurant selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a restaurant to delete");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Restaurant: " + selected.getName());
        confirm.setContentText("Are you sure? This will also delete all menu items for this restaurant.");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                dao.delete(selected.getId());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Restaurant deleted.");
                refreshData();
                clearFields();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
            }
        }
    }

    private void refreshData() {
        try {
            data.setAll(dao.getAll());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error Loading Data", e.getMessage());
        }
    }

    private boolean validateInput() {
        if (idField.getText().isEmpty() || nameField.getText().isEmpty() || areaField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required");
            return false;
        }
        return true;
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        areaField.clear();
        idField.setEditable(true);
        table.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
