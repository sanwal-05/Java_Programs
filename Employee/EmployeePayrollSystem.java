import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class Employee {
    protected final int id;
    protected final String name;
    protected final String department;
    protected final double baseSalary;
    protected String privilegeLevel;
    protected String category;

    public Employee(int id, String name, String department, double baseSalary, String category, String privilegeLevel) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.baseSalary = baseSalary;
        this.category = category;
        this.privilegeLevel = privilegeLevel;
    }

    public abstract double calculateCTC();
    public abstract String getPermissions();

    public String getRole() {
        return getClass().getSimpleName();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDept() {
        return department;
    }

    public String getCategory() {
        return category;
    }

    public String getPrivilegeLevel() {
        return privilegeLevel;
    }
}

class FullTimeEmployee extends Employee {
    private static final double HEALTH_INSURANCE = 2000;
    private static final double PF_CONTRIBUTION = 3000;

    public FullTimeEmployee(int id, String name, String department, double baseSalary) {
        super(id, name, department, baseSalary, "Full-Time", "STANDARD (Internal Access)");
    }

    @Override
    public double calculateCTC() {
        return baseSalary + HEALTH_INSURANCE + PF_CONTRIBUTION;
    }

    @Override
    public String getPermissions() {
        return "Can view personal profile and public documents. Access to internal knowledge base.";
    }
}

class Manager extends FullTimeEmployee {
    private final double managementAllowance;

    public Manager(int id, String name, String department, double baseSalary, double allowance) {
        super(id, name, department, baseSalary);
        this.managementAllowance = allowance;
        this.privilegeLevel = "HIGH (Management Access)";
    }

    @Override
    public double calculateCTC() {
        return super.calculateCTC() + managementAllowance;
    }

    @Override
    public String getPermissions() {
        return "FULL: Approve leaves, view team salaries, and modify project budgets.";
    }
}

class Executive extends FullTimeEmployee {
    private final double performanceBonus;

    public Executive(int id, String name, String department, double baseSalary, double bonus) {
        super(id, name, department, baseSalary);
        this.performanceBonus = bonus;
        this.privilegeLevel = "MID (Operational Access)";
    }

    @Override
    public double calculateCTC() {
        return super.calculateCTC() + performanceBonus;
    }

    @Override
    public String getPermissions() {
        return "MID: Access to department reports and strategic planning tools.";
    }
}

class ContractEmployee extends Employee {
    protected final int contractDuration;

    public ContractEmployee(int id, String name, String department, double hourlyRate, int duration) {
        super(id, name, department, hourlyRate * 160, "Contract", "LIMITED (Vendor Access)");
        this.contractDuration = duration;
    }

    @Override
    public double calculateCTC() {
        return baseSalary;
    }

    @Override
    public String getPermissions() {
        return "LIMITED: Access to specific project modules only.";
    }
}

class TechnicalLead extends ContractEmployee {
    private final double techExpertiseBonus;

    public TechnicalLead(int id, String name, String department, double rate, int duration, double bonus) {
        super(id, name, department, rate, duration);
        this.techExpertiseBonus = bonus;
        this.privilegeLevel = "TECHNICAL (Admin Access)";
    }

    @Override
    public double calculateCTC() {
        return super.calculateCTC() + techExpertiseBonus;
    }

    @Override
    public String getPermissions() {
        return "TECH: Server root access, Code Review permissions, and Deployment rights.";
    }
}

class Intern extends Employee {
    private final String university;

    public Intern(int id, String name, String department, double stipend, String university) {
        super(id, name, department, stipend, "Intern", "GUEST (Restricted Access)");
        this.university = university;
    }

    @Override
    public double calculateCTC() {
        return baseSalary;
    }

    @Override
    public String getPermissions() {
        return "GUEST: Read-only access to assigned training materials.";
    }
}

class PayrollManager {
    private final List<Employee> employees = new ArrayList<>();

    public void seedData() {
        employees.add(new Manager(1001, "John Smith", "IT", 85000, 15000));
        employees.add(new Executive(1002, "Sarah Connor", "Operations", 70000, 8000));
        employees.add(new TechnicalLead(2001, "James Bond", "CyberSec", 50, 12, 10000));
        employees.add(new ContractEmployee(2002, "Ethan Hunt", "Logistics", 45, 6));
        employees.add(new Intern(3001, "Peter Parker", "R&D", 2500, "MIT"));
        employees.add(new Intern(3002, "Gwen Stacy", "Biology", 2800, "Empire State"));
    }

    public void displayEmployees(String filter) {
        System.out.println("\n" + "=".repeat(100));
        System.out.printf("| %-5s | %-15s | %-12s | %-15s | %-12s | %-10s |%n",
                          "ID", "Name", "Dept", "Role", "Category", "CTC");
        System.out.println("-".repeat(100));

        for (Employee e : employees) {
            if ("ALL".equals(filter) || e.getCategory().equalsIgnoreCase(filter)) {
                System.out.printf("| %-5d | %-15s | %-12s | %-15s | %-12s | %-10.2f |%n",
                                  e.getId(), e.getName(), e.getDept(), e.getRole(), e.getCategory(), e.calculateCTC());
            }
        }
        System.out.println("=".repeat(100));
    }

    public void findAndShowEmployee(int id) {
        for (Employee e : employees) {
            if (e.getId() == id) {
                System.out.println("\n>>> DETAILED VIEW FOR ID: " + id);
                System.out.println("Name:           " + e.getName());
                System.out.println("Department:     " + e.getDept());
                System.out.println("Category:       " + e.getCategory());
                System.out.println("Specific Role:  " + e.getRole());
                System.out.printf("Net CTC:        $%.2f%n", e.calculateCTC());
                System.out.println("--------------------------------------------------");
                System.out.println("PRIVILEGE INFO:");
                System.out.println("Access Level:   " + e.getPrivilegeLevel());
                System.out.println("Permissions:    " + e.getPermissions());
                System.out.println("--------------------------------------------------");
                return;
            }
        }
        System.out.println("Employee with ID " + id + " not found.");
    }
}

public class EmployeePayrollSystem {
    public static void main(String[] args) {
        PayrollManager manager = new PayrollManager();
        manager.seedData();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            System.out.println("\n--- EMPLOYEE PAYROLL & PRIVILEGE SYSTEM ---");
            System.out.println("1) Show All Employees");
            System.out.println("2) Filter by Type (Full-Time/Contract/Intern)");
            System.out.println("3) View Detailed Employee Info (by ID)");
            System.out.println("4) Exit");
            System.out.print("Select an option: ");

            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }

            switch (input) {
                case "1":
                    manager.displayEmployees("ALL");
                    break;
                case "2":
                    System.out.println("\nSelect Type to Filter:");
                    System.out.println("F - Full-Time");
                    System.out.println("C - Contract");
                    System.out.println("I - Intern");
                    System.out.print("Choice: ");
                    if (scanner.hasNextLine()) {
                        String filterInput = scanner.nextLine().trim().toUpperCase();
                        switch (filterInput) {
                            case "F":
                                manager.displayEmployees("Full-Time");
                                break;
                            case "C":
                                manager.displayEmployees("Contract");
                                break;
                            case "I":
                                manager.displayEmployees("Intern");
                                break;
                            default:
                                System.out.println("Invalid category selected.");
                        }
                    }
                    break;
                case "3":
                    System.out.print("Enter Employee ID: ");
                    if (scanner.hasNextLine()) {
                        String idInput = scanner.nextLine().trim();
                        try {
                            int id = Integer.parseInt(idInput);
                            manager.findAndShowEmployee(id);
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid numeric ID.");
                        }
                    }
                    break;
                case "4":
                    System.out.println("System shutting down...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }

        scanner.close();
    }
}

