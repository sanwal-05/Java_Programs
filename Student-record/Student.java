public class Student {
    String prn;
    String name;
    String branch; // Restricted to CSE, AIML, RA
    double javaMarks;
    double percentage;
    String grade;

    public Student(String prn, String name, String branch, double javaMarks) {
        this.prn = prn;
        this.name = name;
        this.branch = branch;
        this.javaMarks = javaMarks;
        calculateGrade();
    }

    public void calculateGrade() {
        this.percentage = this.javaMarks; // Based on 100
        if (percentage >= 90) grade = "O";
        else if (percentage >= 80) grade = "A+";
        else if (percentage >= 70) grade = "A";
        else if (percentage >= 60) grade = "B+";
        else if (percentage >= 50) grade = "B";
        else if (percentage >= 40) grade = "C";
        else grade = "F";
    }

    @Override
    public String toString() {
        // Format: PRN, Name, Branch, Marks, Percentage, Grade
        return prn + "," + name + "," + branch + "," + javaMarks + "," + percentage + "," + grade;
    }
}