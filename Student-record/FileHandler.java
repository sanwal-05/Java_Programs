import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String FILE_NAME = "student.csv";

    public static void initializeFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
                // Hardcoded initial records
                Student s1 = new Student("24070126001", "Sanjana Bhowal", "CSE", 80);
                Student s2 = new Student("24070126002", "Archi Patel", "AIML", 90);

                pw.println(s1.toString());
                pw.println(s2.toString());

                System.out.println(">> System: student.csv initialized with Sanjana and Archi.");
            } catch (IOException e) {
                System.out.println("Error initializing file: " + e.getMessage());
            }
        }
    }

    public static List<Student> loadRecords() {
        List<Student> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 6) {
                    list.add(new Student(d[0], d[1], d[2], Double.parseDouble(d[3])));
                }
            }
        } catch (Exception e) {
            // File might be empty
        }
        return list;
    }

    public static void saveRecords(List<Student> list) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Student s : list) pw.println(s.toString());
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }
}