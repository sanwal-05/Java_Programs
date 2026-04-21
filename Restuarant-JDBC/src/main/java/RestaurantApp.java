import java.sql.Connection;
import java.util.Scanner;
import config.DatabaseConfig;
import dao.RestaurantDAO;

public class RestaurantApp {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getConnection();
             Scanner sc = new Scanner(System.in)) {

            RestaurantDAO dao = new RestaurantDAO(conn);
            dao.setupTables();

            while (true) {
                System.out.println("\n====================================");
                System.out.println("   RESTAURANT MANAGEMENT SYSTEM");
                System.out.println("====================================");
                System.out.println("1 - View Restaurants");
                System.out.println("2 - View All Menu Items");
                System.out.println("3 - View Menu by Restaurant ID");
                System.out.println("4 - Update Budget Prices (<=100 to 200)");
                System.out.println("5 - Add Restaurant");
                System.out.println("6 - Delete Restaurant");
                System.out.println("7 - Add Menu Item");
                System.out.println("8 - More (Items <= 100 Search)");
                System.out.println("9 - Exit");
                System.out.print("Select an option: ");

                int choice = sc.nextInt();
                sc.nextLine(); // clear buffer

                switch (choice) {
                    case 1: dao.viewRestaurants(); break;
                    case 2: dao.viewAllMenu(); break;
                    case 3:
                        System.out.print("Enter Restaurant ID: ");
                        dao.viewMenuByRestaurant(sc.nextInt());
                        break;
                    case 4: dao.updatePrices(); break;
                    case 5:
                        System.out.print("New ID: "); int rid = sc.nextInt(); sc.nextLine();
                        System.out.print("Name: "); String rname = sc.nextLine();
                        System.out.print("Area: "); String rarea = sc.nextLine();
                        dao.addRestaurant(rid, rname, rarea);
                        break;
                    case 6:
                        System.out.print("Enter ID to delete: ");
                        dao.deleteRestaurant(sc.nextInt());
                        break;
                    case 7:
                        System.out.print("Item ID: "); int iid = sc.nextInt(); sc.nextLine();
                        System.out.print("Item Name: "); String iname = sc.nextLine();
                        System.out.print("Price: "); double iprice = sc.nextDouble();
                        System.out.print("Rest ID: "); int irid = sc.nextInt();
                        dao.addMenuItem(iid, iname, iprice, irid);
                        break;
                    case 8:
                        System.out.println("Displaying affordable items...");
                        // This can be expanded for more features
                        dao.viewAllMenu(); 
                        break;
                    case 9:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("FATAL ERROR: " + e.getMessage());
        }
    }
}