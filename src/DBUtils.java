

import java.sql.*;
public class DBUtils {
    private static Connection con;
    private static Statement stmt;

    // Database configuration
    private static final String URL = "jdbc:mysql://localhost:3306/student_management";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = con.createStatement();
            System.out.println("✓ Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("DB connection error: " + e.getMessage());
        }
    }

    public static int  executeQuery(String query) {
        try {
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        } return 0;
    }
    public static ResultSet executeQueryForResult(String query) {
        try {
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
            return null;
        }
    }
    public static void closeConnection() {
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
            if (con != null && !con.isClosed()) {
                con.close();
            }
            System.out.println("✓ Database connection closed!");
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
    public static Connection getConnection() {
        return con;
    }
    public static boolean isConnected() {
        try {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

}
