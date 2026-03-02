// File: VectorOp.java
import java.util.Scanner;

public class VectorOp {
    private static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {
        try {
            System.out.println("--- Java Vector Operations ---");


            Vector v1 = acceptVector("First");
            Vector v2 = acceptVector("Second");


            // Perform Operations
            Vector sum = Vector.add(v1, v2);
            sum.print("Sum");


            Vector diff = Vector.subtract(v1, v2);
            diff.print("Difference");


            double dot = Vector.dotProduct(v1, v2);
            System.out.println("Dot Product: " + dot);


        } catch (InvalidVectorDimensionsException e) {
            System.out.println("Vector Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("General Error: Please enter valid numeric input.");
        }
    }


    public static Vector acceptVector(String name) throws InvalidVectorDimensionsException {
        System.out.print("Enter number of elements for " + name + " vector: ");
        int size = sc.nextInt();
       
        double[] tempArr = new double[size];
        System.out.println("Enter " + size + " values:");
        for (int i = 0; i < size; i++) {
            tempArr[i] = sc.nextDouble();
        }
       
        return new Vector(tempArr);
    }
}

