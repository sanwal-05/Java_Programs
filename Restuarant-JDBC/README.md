# Restaurant Management System (JDBC & JavaFX)

A modern, professional restaurant management application built with Java, featuring a robust MySQL backend and a sleek JavaFX GUI. This project demonstrates a clean MVC architecture and full CRUD operations for managing both restaurants and their associated menus.

## 🚀 Features
- **Modern JavaFX GUI**: A professional, dark-themed interface for managing data visually.
- **Legacy CLI Interface**: A classic console-based management tool for quick operations.
- **Full CRUD Support**: Create, Read, Update, and Delete both Restaurants and Menu items.
- **Live Search**: Real-time filtering for restaurants and menu items.
- **Bulk Operations**: One-click budget price updates (e.g., set all items <= 100 to 200).
- **Cascading Deletes**: Deleting a restaurant automatically cleans up its associated menu.
- **Database Persistence**: Persistent storage using JDBC and MySQL.

## 🛠️ Technology Stack
- **Language**: Java 17
- **UI Framework**: JavaFX 21
- **Build Tool**: Maven
- **Database**: MySQL / MariaDB
- **Driver**: MySQL Connector/J

## 📋 Prerequisites
- JDK 17 or higher
- Maven 3.x
- MySQL Server running locally on port 3306
- A database named `restaurant_db` (or allow the app to create it)

## ⚙️ Setup & Configuration
1. **Database Credentials**:
   Open `src/main/java/config/DatabaseConfig.java` and update the `USER` and `PASS` variables to match your MySQL credentials.
   ```java
   private static final String USER = "your_username";
   private static final String PASS = "your_password";
   ```

2. **Database Schema**:
   The application handles table creation automatically on the first run of either the GUI or CLI app.

## 🏃 How to Run

### Option 1: JavaFX GUI (Recommended)
Run the following command in the project root:
```bash
mvn javafx:run
```

### Option 2: Console Application
Run the main method in `src/main/java/RestaurantApp.java` from your IDE, or use:
```bash
mvn compile exec:java -Dexec.mainClass="RestaurantApp"
```

## 🏗️ Project Structure
- `src/main/java/config`: Database connection settings.
- `src/main/java/dao`: Data Access Objects (CRUD logic).
- `src/main/java/models`: Data models (Restaurant & MenuItem).
- `src/main/java/ui`: JavaFX views and components.
- `src/main/resources/ui`: CSS styles for the GUI.
- `RestaurantApp.java`: Main entry point for the console application.
