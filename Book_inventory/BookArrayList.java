
import java.util.ArrayList;

public class BookArrayList {
    private ArrayList<Book> inventory = new ArrayList<>();

    public void addBook(Book b) {
        inventory.add(b);
    }

    public ArrayList<Book> getInventory() {
        return inventory;
    }

    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    public void displayInventoryTable() {
        System.out.println("\n" + "=".repeat(85));
        System.out.printf("%-20s | %-15s | %-12s | %-10s | %-10s\n", 
                          "TITLE", "AUTHOR", "GENRE", "ISBN", "PRICE");
        System.out.println("-".repeat(85));

        for (Book b : inventory) {
            System.out.printf("%-20s | %-15s | %-12s | %-10s | $%-9.2f\n", 
                              b.title, b.author, b.genre, b.isbn, b.price);
        }
        System.out.println("=".repeat(85));
    }
}