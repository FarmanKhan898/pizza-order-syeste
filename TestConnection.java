import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try {
            // Get a connection to the database
            Connection conn = DBConnection.getConnection();

            // If we get here, the connection was successful
            System.out.println("✅ Successfully connected to the database!");

            // Close the connection
            conn.close();
        } catch (SQLException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            System.err.println("Make sure:");
            System.err.println("1. MySQL server is running");
            System.err.println("2. Database 'pizza_ordering_system' exists");
            System.err.println("3. Username and password are correct");
            System.err.println("4. MySQL connector JAR is in classpath");
        }
    }
}