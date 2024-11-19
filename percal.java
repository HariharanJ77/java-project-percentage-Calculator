import java.sql.*;
import java.util.Scanner;

class Calculator {
    public double calculatePercentage(double obtainedValue, double totalValue) {
        return (obtainedValue / totalValue) * 100;
    }

    public double calculateTotalFromPercentage(double percentage, double value) {
        return (value / percentage) * 100;
    }
}

class PercentageCalculator extends Calculator {
    private Connection connection;

    // Establish database connection
    public void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/percentagecal";
            String user = "root";
            String password = "";
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    // Save calculation to the database
    private void saveCalculation(String operation, double totalValue, double obtainedValue, double percentage, double calculatedTotal) {
        String query = "INSERT INTO perctable (operation, totalValue, obtainedValue, percentage, calculatedTotal) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, operation);
            stmt.setDouble(2, totalValue);
            stmt.setDouble(3, obtainedValue);
            stmt.setDouble(4, percentage);
            stmt.setDouble(5, calculatedTotal);
            stmt.executeUpdate();
            System.out.println("Calculation saved to the database.");
        } catch (SQLException e) {
            System.out.println("Failed to save calculation.");
            e.printStackTrace();
        }
    }

    // Start the calculator
    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Percentage Calculator");
        System.out.println("1. Find Percentage");
        System.out.println("2. Find Total Value from Percentage");
        System.out.println("3. Exit");
        System.out.print("Choose an option (1-3): ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.print("Enter the total value: ");
                double totalValue = scanner.nextDouble();
                System.out.print("Enter the obtained value: ");
                double obtainedValue = scanner.nextDouble();

                double percentage = calculatePercentage(obtainedValue, totalValue);
                System.out.printf("The percentage is: %.2f%%\n", percentage);

                saveCalculation("Percentage Calculation", totalValue, obtainedValue, percentage, 0.0);
                break;

            case 2:
                System.out.print("Enter the percentage: ");
                double givenPercentage = scanner.nextDouble();
                System.out.print("Enter the value: ");
                double value = scanner.nextDouble();

                double total = calculateTotalFromPercentage(givenPercentage, value);
                System.out.printf("The total value is: %.2f\n", total);

                saveCalculation("Total Value Calculation", 0.0, value, givenPercentage, total);
                break;

            case 3:
                System.out.println("Exiting the program. Goodbye!");
                break;

            default:
                System.out.println("Invalid choice! Please select 1, 2, or 3.");
        }

        scanner.close();
    }
}

public class percal {
    public static void main(String[] args) {
        PercentageCalculator calculator = new PercentageCalculator();
        calculator.connectToDatabase();
        calculator.start();
    }
}