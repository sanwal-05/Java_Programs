class Vehicle {

    //Attributes
    public String brandName;
    public String modelName;
    public double price;
    public double fuelEfficiency; // Used for mileage calculation
    private String mfgCode;      // Private member
    private String chassisNumber; // Private member

    //Setters and Getters for Private Members
    public void setMfgCode(String mCode) 
    { 
        this.mfgCode = mCode; 
    }
    public String getMfgCode() 
    { 
        return mfgCode; 
    }

    public void setChassisNumber(String cNo) 
    { 
        this.chassisNumber = cNo; 
    }

    public String getChassisNumber() 
    { 
        return chassisNumber; 
    }

    //Constructors

    // Default Constructor
    Vehicle() {
        brandName = "Toyota";
        modelName = "Corolla";
        price = 25000.0;
        fuelEfficiency = 15.5;
        mfgCode = "T-990";
        chassisNumber = "CH000";
    }

    // Parameterized Constructor
    Vehicle(String bName, String mName, double pr, double mileage, String code) {
        brandName = bName;
        modelName = mName;
        price = pr;
        fuelEfficiency = mileage;
        mfgCode = code;
    }

    // Copy Constructor
    Vehicle(Vehicle other) {
        this.brandName = other.brandName;
        this.modelName = other.modelName;
        this.price = other.price;
        this.fuelEfficiency = other.fuelEfficiency;
        this.mfgCode = other.mfgCode;
        this.chassisNumber = other.chassisNumber;
    }

    //Required Methods
    public void start() { System.out.println(brandName + " engine has started."); }
    
    public void stop() { System.out.println(brandName + " has come to a full stop."); }
    
    public void drive() { System.out.println(modelName + " is now in motion."); }

    public double calcMileage(double distance, double fuelUsed) {
        return distance / fuelUsed;
    }

    public void changeSpeed(int newSpeed) {
        System.out.println("Adjusting speed to: " + newSpeed + " km/h");
    }

    // Helper method for the tabular Output
    public void displayRow() {
        System.out.printf("%-15s %-15s %-12.2f %-10.2f\n", 
                          brandName, modelName, price, fuelEfficiency);
    }
}

public class Main_Vehicle {
    public static void main(String[] args) {
        
        // 1. Object Initialization using 3 types of constructors
        Vehicle v1 = new Vehicle(); // Default
        
        Vehicle v2 = new Vehicle("Tesla", "Model 3", 45000.0, 120.0, "TS-500"); // Parameterized
        v2.setChassisNumber("TSLA12345");
        
        Vehicle v3 = new Vehicle(v2); // Copy of v2
        v3.brandName = "Lucid";       // Modifying the copy
        v3.modelName = "Air";
        v3.price = 80000.0;

        // 2. Demonstration of methods
        System.out.println("--- Logs ---");
        v2.start();
        v2.drive();
        v2.changeSpeed(85);
        double tripMileage = v2.calcMileage(300, 2.5); 
        System.out.println("Calculated Trip Mileage for " + v2.brandName + ": " + tripMileage);
        v2.stop();
        System.out.println();

        // 3. Array of Vehicles
        Vehicle[] fleet = {v1, v2, v3};

        // 4. Tabular Summary Print
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-12s %-10s\n", "Brand", "Model", "Price ($)", "Mileage");
        System.out.println("------------------------------------------------------------");
        
        for (Vehicle v : fleet) {
            v.displayRow();
        }
        
        System.out.println("------------------------------------------------------------");
        System.out.println("Total Inventory Count: " + fleet.length);
    }
}