import java.util.Scanner;

public class BookList {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BookArrayList manager = new BookArrayList();
       
        while (true) {
            System.out.println("\n--- Book Inventory System ---");
            System.out.println("1. Add a Book");
            System.out.println("2. View Inventory");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                try {
                    System.out.print("Enter Title: ");
                    String t = sc.nextLine();
                   
                    System.out.print("Enter Price: ");
                    double p = Double.parseDouble(sc.nextLine());

                    System.out.print("Enter ISBN: ");
                    String i = sc.nextLine();
                   
                    System.out.print("Enter Genre (Fiction/Non-Fiction/Sci-Fi/Mystery): ");
                    String g = sc.nextLine();
                   
                    System.out.print("Enter Author: ");
                    String a = sc.nextLine();

                    Book newBook = new Book(t, p, i, g, a);
                    manager.addBook(newBook);
                    System.out.println(">> SUCCESS: Book added.");

                } catch (InvalidPriceException | InvalidGenreException | MissingDataException e) {
                    System.out.println(">> VALIDATION ERROR: " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println(">> ERROR: Price must be a numeric value.");
                } catch (Exception e) {
                    System.out.println(">> UNEXPECTED ERROR: " + e.getMessage());
                }

            } else if (choice.equals("2")) {
                if (manager.isEmpty()) {
                    System.out.println("The inventory is currently empty.");
                } else {
                    manager.displayInventoryTable();
                }
            } else if (choice.equals("3")) {
                System.out.println("Exiting system. Goodbye!");
                break;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
        sc.close();
    }
}