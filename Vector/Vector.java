//Vector Operations Program: Implement 2D/3D vector addition, subtraction, and dot product with exception handling.
// File: Vector.java
import java.util.Arrays;


public class Vector {
    public double[] components;


    // Constructor - Throws exception if vector isn't 2D or 3D
    public Vector(double[] newV) throws InvalidVectorDimensionsException {
        if (newV.length < 2 || newV.length > 3) {
            throw new InvalidVectorDimensionsException("Only 2D or 3D vectors are supported.");
        }
        this.components = newV;
    }


    // METHOD 1: Addition
    public static Vector add(Vector v1, Vector v2) throws InvalidVectorDimensionsException {
        checkDimensions(v1, v2);
        double[] res = new double[v1.components.length];
        for (int i = 0; i < v1.components.length; i++) {
            res[i] = v1.components[i] + v2.components[i];
        }
        return new Vector(res);
    }


    // METHOD 2: Subtraction
    public static Vector subtract(Vector v1, Vector v2) throws InvalidVectorDimensionsException {
        checkDimensions(v1, v2);
        double[] res = new double[v1.components.length];
        for (int i = 0; i < v1.components.length; i++) {
            res[i] = v1.components[i] - v2.components[i];
        }
        return new Vector(res);
    }


    // METHOD 3: Dot Product
    public static double dotProduct(Vector v1, Vector v2) throws InvalidVectorDimensionsException {
        checkDimensions(v1, v2);
        double res = 0;
        for (int i = 0; i < v1.components.length; i++) {
            res += v1.components[i] * v2.components[i];
        }
        return res;
    }


    // METHOD 4: Check Dimensions (Internal Validation)
    public static void checkDimensions(Vector v1, Vector v2) throws InvalidVectorDimensionsException {
        if (v1.components.length != v2.components.length) {
            throw new InvalidVectorDimensionsException("Dimension mismatch: " +
                v1.components.length + " vs " + v2.components.length);
        }
    }


    // METHOD 5: Print Method
    public void print(String label) {
        System.out.println(label + ": " + Arrays.toString(components));
    }
}

