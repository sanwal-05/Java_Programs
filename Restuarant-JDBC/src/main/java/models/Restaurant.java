package models;

import javafx.beans.property.*;

public class Restaurant {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty area;

    public Restaurant(int id, String name, String area) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.area = new SimpleStringProperty(area);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }

    public String getArea() { return area.get(); }
    public StringProperty areaProperty() { return area; }
    public void setArea(String area) { this.area.set(area); }
}
