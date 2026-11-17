package hms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Database initialization and seed data setup
public class DBSetup {

    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true";

    public static void main(String[] args) {
        initialize();
    }

    public static void initialize() {
        Connection conn = null;
        Statement stmt = null;

        try {
            try {
                Class.forName(DBConnection.DRIVER);
            } catch (Exception e) {
                try { Class.forName("com.mysql.jdbc.Driver"); } catch (Exception ex) {}
            }

            // Step 1: Create database if not exists
            conn = DriverManager.getConnection(SERVER_URL, DBConnection.USER, DBConnection.PASS);
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS healthcare_db");
            stmt.close();
            conn.close();

            // Step 2: Connect to healthcare_db and create tables
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (username VARCHAR(50) PRIMARY KEY, password VARCHAR(50) NOT NULL)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS patients ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL, age INT NOT NULL, "
                + "gender VARCHAR(10) NOT NULL, phone VARCHAR(15) NOT NULL, address VARCHAR(200), "
                + "blood_group VARCHAR(10), room_no INT DEFAULT 0, admit_date VARCHAR(20) DEFAULT '', is_inpatient INT DEFAULT 0)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS doctors ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL, age INT NOT NULL, "
                + "gender VARCHAR(10) NOT NULL, phone VARCHAR(15) NOT NULL, address VARCHAR(200), "
                + "specialization VARCHAR(100) NOT NULL, fee DOUBLE NOT NULL)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS appointments ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, patient_id INT NOT NULL, doctor_id INT NOT NULL, "
                + "app_date VARCHAR(20) NOT NULL, app_time VARCHAR(20) NOT NULL, reason VARCHAR(200), status VARCHAR(20) DEFAULT 'Booked', "
                + "FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE, "
                + "FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS prescriptions ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, patient_id INT NOT NULL, doctor_id INT NOT NULL, "
                + "pres_date VARCHAR(20) NOT NULL, medicines VARCHAR(300) NOT NULL, instructions VARCHAR(300), "
                + "FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE, "
                + "FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS reports ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, patient_id INT NOT NULL, rep_date VARCHAR(20) NOT NULL, "
                + "diagnosis VARCHAR(200) NOT NULL, notes VARCHAR(300), "
                + "FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE)");

            // Step 3: Stored procedure for doctor appointment count
            stmt.executeUpdate("DROP PROCEDURE IF EXISTS doctor_appointment_count");
            stmt.executeUpdate("CREATE PROCEDURE doctor_appointment_count(IN docId INT, OUT total INT) "
                + "BEGIN SELECT COUNT(*) INTO total FROM appointments WHERE doctor_id = docId; END");

            // Step 4: Insert sample data if tables are empty
            seedInitialData(stmt);
            System.out.println("Database schema and tables ready.");
        } catch (SQLException e) {
            System.out.println("DBSetup: " + e.getMessage());
        } finally {
            DBConnection.close(null, stmt, conn);
        }
    }

    private static void seedInitialData(Statement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
        int count = rs.next() ? rs.getInt(1) : 0;
        rs.close();
        if (count == 0) stmt.executeUpdate("INSERT INTO users VALUES ('admin', 'admin123')");

        rs = stmt.executeQuery("SELECT COUNT(*) FROM patients");
        count = rs.next() ? rs.getInt(1) : 0;
        rs.close();
        if (count == 0) {
            stmt.executeUpdate("INSERT INTO patients (id, name, age, gender, phone, address, blood_group, room_no, admit_date, is_inpatient) VALUES "
                + "(1, 'John Doe', 35, 'M', '9876543210', '45 Park Lane', 'O+', 0, '', 0),"
                + "(2, 'Alice Brown', 28, 'F', '9811223344', '10 Lake View', 'B+', 102, '2026-11-05', 1),"
                + "(3, 'David Wilson', 54, 'M', '9833445566', '88 Hill Road', 'A+', 0, '', 0),"
                + "(4, 'Emma Watson', 24, 'F', '9844556677', '12 Sunset Blvd', 'AB-', 105, '2026-11-04', 1),"
                + "(5, 'Michael Scott', 45, 'M', '9855667788', '172 Scranton St', 'O-', 0, '', 0)");

            stmt.executeUpdate("INSERT INTO doctors (id, name, age, gender, phone, address, specialization, fee) VALUES "
                + "(1, 'Dr. Sarah Lee', 42, 'F', '9123456780', '12 Health Ave', 'Cardiology', 500.0),"
                + "(2, 'Dr. Robert Taylor', 48, 'M', '9123456781', '25 College Road', 'Neurology', 800.0),"
                + "(3, 'Dr. Priya Sharma', 39, 'F', '9123456782', '5 Doctors Colony', 'General Medicine', 400.0)");

            stmt.executeUpdate("INSERT INTO appointments (patient_id, doctor_id, app_date, app_time, reason, status) VALUES "
                + "(1, 1, '2026-11-10', '10:00', 'Chest pain consultation', 'Booked'),"
                + "(2, 2, '2026-11-12', '11:30', 'Migraine follow-up', 'Booked'),"
                + "(3, 3, '2026-11-11', '09:30', 'Annual checkup', 'Done')");

            stmt.executeUpdate("INSERT INTO prescriptions (patient_id, doctor_id, pres_date, medicines, instructions) VALUES "
                + "(1, 1, '2026-11-10', 'Aspirin 75mg, Atorvastatin 10mg', 'Once daily after dinner'),"
                + "(2, 2, '2026-11-12', 'Sumatriptan 50mg, Naproxen 250mg', 'Take at onset of migraine')");

            stmt.executeUpdate("INSERT INTO reports (patient_id, rep_date, diagnosis, notes) VALUES "
                + "(1, '2026-11-10', 'Mild Angina', 'Resting ECG normal, follow up in 2 weeks'),"
                + "(2, '2026-11-12', 'Severe Migraine with Aura', 'MRI Brain normal, prescribed prophylaxis')");
        }
    }
}
