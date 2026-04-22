import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// --- Interface ---
interface Product {
    void displayDetails();
}

// --- Legacy Component ---
class LegacyItem {
    private int itemId;
    private String description;

    public LegacyItem(int itemId, String description) {
        this.itemId = itemId;
        this.description = description;
    }

    public void print() {
        System.out.println("Legacy Item [" + itemId + "]: " + description);
    }
}

// --- Adapter Pattern ---
class ProductAdapter implements Product {
    private LegacyItem legacyItem;

    public ProductAdapter(LegacyItem legacyItem) {
        this.legacyItem = legacyItem;
    }

    @Override
    public void displayDetails() {
        // Adapting the print() method to displayDetails()
        legacyItem.print();
    }
}

// --- New Product Implementation ---
class NewProduct implements Product {
    private String name;

    public NewProduct(String name) {
        this.name = name;
    }

    @Override
    public void displayDetails() {
        System.out.println("New Product: " + name);
    }
}

// --- Singleton Pattern & Iterator Pattern ---
class InventoryManager {
    private static InventoryManager instance;
    private List<Product> products;

    // Private constructor for Singleton
    private InventoryManager() {
        products = new ArrayList<>();
    }

    // Thread-safe Singleton access
    public static InventoryManager getInstance() {
        if (instance == null) {
            instance = new InventoryManager();
        }
        return instance;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    // Returns an Iterator for the Product list
    public Iterator<Product> returnInventory() {
        return products.iterator();
    }
}

// --- Main Method ---
public class Main {
    public static void main(String[] args) {
        // 1. Get the single instance of InventoryManager
        InventoryManager inventory = InventoryManager.getInstance();

        // 2. Add a NewProduct
        inventory.addProduct(new NewProduct("MacBook Pro M3"));
        inventory.addProduct(new NewProduct("Sony WH-1000XM5"));

        // 3. Add a LegacyItem using the Adapter
        LegacyItem oldScanner = new LegacyItem(5001, "Industrial Barcode Scanner (Model 2010)");
        Product adaptedLegacyItem = new ProductAdapter(oldScanner);
        inventory.addProduct(adaptedLegacyItem);

        // 4. Iterate through the inventory
        System.out.println("--- Current Inventory ---");
        Iterator<Product> it = inventory.returnInventory();
        
        while (it.hasNext()) {
            Product p = it.next();
            p.displayDetails();
        }
    }
}