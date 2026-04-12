import java.util.*;

public class StudentRecordManager {
    private static List<Student> studentList = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);
    private static final List<String> VALID_BRANCHES = Arrays.asList("CSE", "AIML", "RA");

    public static void main(String[] args) {
        FileHandler.initializeFile();
        studentList = FileHandler.loadRecords();

        while (true) {
            System.out.println("\n--- STUDENT RECORD MANAGER ---");
            System.out.println("1. Add Student(s) with Marks\n2. View Records\n3. Update Marks\n4. Delete Student\n5. View Averages (Branch & Total)\n6. Exit");
            System.out.print("Choice: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1: addStudents(); break;
                    case 2: displayRecords(); break;
                    case 3: updateMarks(); break;
                    case 4: deleteStudent(); break;
                    case 5: showAverages(); break;
                    case 6: FileHandler.saveRecords(studentList); return;
                    default: System.out.println("Invalid Choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: Please enter a number.");
            }
        }
    }

    private static void addStudents() {
        try {
            System.out.print("How many students to add? ");
            int count = Integer.parseInt(sc.nextLine());

            for (int i = 0; i < count; i++) {
                System.out.println("\n--- New Student " + (i + 1) + " ---");
                System.out.print("Name: "); String name = sc.nextLine();
                
                String branch;
                while (true) {
                    System.out.print("Branch (CSE, AIML, RA): ");
                    branch = sc.nextLine().toUpperCase();
                    if (VALID_BRANCHES.contains(branch)) break;
                    System.out.println("Invalid Branch! Please choose from CSE, AIML, or RA.");
                }

                double marks;
                while (true) {
                    System.out.print("JAVA Marks (0-100): ");
                    marks = Double.parseDouble(sc.nextLine());
                    if (marks >= 0 && marks <= 100) break;
                    System.out.println("Invalid Marks! Must be between 0 and 100.");
                }

                // PRN generation
                int nextID = 1;
                for (Student s : studentList) {
                    int id = Integer.parseInt(s.prn.substring(s.prn.length() - 3));
                    if (id >= nextID) nextID = id + 1;
                }
                String prn = "24070126" + String.format("%03d", nextID);

                studentList.add(new Student(prn, name, branch, marks));
                FileHandler.saveRecords(studentList);
                System.out.println("Student saved successfully!");
            }
        } catch (Exception e) {
            System.out.println("Input error. Operation cancelled.");
        }
    }

    private static void displayRecords() {
        System.out.printf("\n%-15s | %-20s | %-8s | %-6s | %-5s\n", "PRN", "Name", "Branch", "Marks", "Grade");
        System.out.println("----------------------------------------------------------------------");
        for (Student s : studentList) {
            System.out.printf("%-15s | %-20s | %-8s | %-6.1f | %-5s\n", s.prn, s.name, s.branch, s.javaMarks, s.grade);
        }
    }

    private static void updateMarks() {
        System.out.print("Enter PRN to update: ");
        String prn = sc.nextLine();
        for (Student s : studentList) {
            if (s.prn.equals(prn)) {
                System.out.print("New Marks: ");
                double m = Double.parseDouble(sc.nextLine());
                if (m >= 0 && m <= 100) {
                    s.javaMarks = m;
                    s.calculateGrade();
                    FileHandler.saveRecords(studentList);
                    System.out.println("Updated!");
                } else {
                    System.out.println("Invalid marks.");
                }
                return;
            }
        }
        System.out.println("Student not found.");
    }

    private static void deleteStudent() {
        System.out.print("Enter PRN to delete: ");
        String prn = sc.nextLine();
        if (studentList.removeIf(s -> s.prn.equals(prn))) {
            FileHandler.saveRecords(studentList);
            System.out.println("Deleted.");
        }
    }

    private static void showAverages() {
        if (studentList.isEmpty()) return;

        double totalSum = 0;
        Map<String, Double> bSum = new HashMap<>();
        Map<String, Integer> bCount = new HashMap<>();

        for (Student s : studentList) {
            totalSum += s.javaMarks;
            bSum.put(s.branch, bSum.getOrDefault(s.branch, 0.0) + s.javaMarks);
            bCount.put(s.branch, bCount.getOrDefault(s.branch, 0) + 1);
        }

        System.out.println("\n--- BRANCH AVERAGES ---");
        for (String b : VALID_BRANCHES) {
            if (bCount.containsKey(b)) {
                System.out.printf("%-5s Average: %.2f\n", b, (bSum.get(b) / bCount.get(b)));
            } else {
                System.out.printf("%-5s Average: N/A (No Students)\n", b);
            }
        }
        System.out.printf("\nTOTAL SYSTEM AVERAGE: %.2f\n", (totalSum / studentList.size()));
    }
}