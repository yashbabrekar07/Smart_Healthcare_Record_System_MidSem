package hms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Database connection manager using standard JDBC
public class DBConnection {

    // Oracle Driver: oracle.jdbc.driver.OracleDriver
    // Oracle URL: jdbc:oracle:thin:@localhost:1521:xe

    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/healthcare_db?useSSL=false&allowPublicKeyRetrieval=true";
    public static final String USER = "root";
    public static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("MySQL driver not found in classpath.");
            }
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
        if (stmt != null) { try { stmt.close(); } catch (SQLException e) {} }
        if (conn != null) { try { conn.close(); } catch (SQLException e) {} }
    }
}
