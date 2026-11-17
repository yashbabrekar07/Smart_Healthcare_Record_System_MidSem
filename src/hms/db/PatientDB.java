package hms.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import hms.model.InPatient;
import hms.model.Patient;
import hms.util.Validator;

// Patient database access layer executing JDBC queries
public class PatientDB {

    // Listing using Statement
    public ArrayList<Patient> getAllPatients() throws SQLException {
        ArrayList<Patient> list = new ArrayList<Patient>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM patients ORDER BY id ASC");
            while (rs.next()) {
                list.add(extractPatient(rs));
            }
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return list;
    }

    // Insert patient demonstrating nested try block
    public void addPatient(Patient p) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Outer try
            try {
                // Inner try
                conn = DBConnection.getConnection();
                String sql = "INSERT INTO patients (name, age, gender, phone, address, blood_group, room_no, admit_date, is_inpatient) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, p.getName());
                pstmt.setInt(2, p.getAge());
                pstmt.setString(3, p.getGender());
                pstmt.setString(4, p.getPhone());
                pstmt.setString(5, p.getAddress());
                pstmt.setString(6, p.getBloodGroup());

                if (p instanceof InPatient) {
                    InPatient ip = (InPatient) p;
                    pstmt.setInt(7, ip.getRoomNo());
                    pstmt.setString(8, ip.getAdmitDate());
                    pstmt.setInt(9, 1);
                } else {
                    pstmt.setInt(7, 0);
                    pstmt.setString(8, "");
                    pstmt.setInt(9, 0);
                }
                pstmt.executeUpdate();
            } catch (SQLException sqle) {
                System.out.println("Error in nested try block: " + sqle.getMessage());
                throw sqle;
            }
        } finally {
            DBConnection.close(null, pstmt, conn);
        }
    }

    public void updatePatient(Patient p) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE patients SET name=?, age=?, gender=?, phone=?, address=?, blood_group=?, room_no=?, admit_date=?, is_inpatient=? WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, p.getName());
            pstmt.setInt(2, p.getAge());
            pstmt.setString(3, p.getGender());
            pstmt.setString(4, p.getPhone());
            pstmt.setString(5, p.getAddress());
            pstmt.setString(6, p.getBloodGroup());

            if (p instanceof InPatient) {
                InPatient ip = (InPatient) p;
                pstmt.setInt(7, ip.getRoomNo());
                pstmt.setString(8, ip.getAdmitDate());
                pstmt.setInt(9, 1);
            } else {
                pstmt.setInt(7, 0);
                pstmt.setString(8, "");
                pstmt.setInt(9, 0);
            }
            pstmt.setInt(10, p.getId());
            pstmt.executeUpdate();
        } finally {
            DBConnection.close(null, pstmt, conn);
        }
    }

    public void deletePatient(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM patients WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } finally {
            DBConnection.close(null, pstmt, conn);
        }
    }

    public Patient getPatientById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Patient p = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM patients WHERE id = ?");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                p = extractPatient(rs);
            }
        } finally {
            DBConnection.close(rs, pstmt, conn);
        }
        return p;
    }

    // Method overloading: search by integer ID
    public ArrayList<Patient> search(int id) throws SQLException {
        ArrayList<Patient> list = new ArrayList<Patient>();
        Patient p = getPatientById(id);
        if (p != null) list.add(p);
        return list;
    }

    // Method overloading: search by string keyword across name and phone with HashSet merge
    public ArrayList<Patient> search(String query) throws SQLException {
        HashSet<Patient> mergedSet = new HashSet<Patient>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            // Query 1: search by name
            pstmt = conn.prepareStatement("SELECT * FROM patients WHERE LOWER(name) LIKE ?");
            pstmt.setString(1, "%" + query.toLowerCase() + "%");
            rs = pstmt.executeQuery();
            while (rs.next()) mergedSet.add(extractPatient(rs));
            rs.close();
            pstmt.close();

            // Query 2: search by phone
            pstmt = conn.prepareStatement("SELECT * FROM patients WHERE phone LIKE ?");
            pstmt.setString(1, "%" + query + "%");
            rs = pstmt.executeQuery();
            while (rs.next()) mergedSet.add(extractPatient(rs));
        } finally {
            DBConnection.close(rs, pstmt, conn);
        }

        return new ArrayList<Patient>(mergedSet);
    }

    // Helper demonstrating multiple catch block
    public boolean validatePatientFields(String ageStr, String name, String phone, String bg) {
        try {
            int age = Integer.parseInt(ageStr);
            Validator.validateAge(age);
            Validator.validateName(name);
            Validator.validatePhone(phone);
            Validator.validateBloodGroup(bg);
            return true;
        } catch (NumberFormatException nfe) {
            System.out.println("Validation failed: Age must be a number.");
            return false;
        } catch (IllegalArgumentException iae) {
            System.out.println("Validation failed: " + iae.getMessage());
            return false;
        }
    }

    private Patient extractPatient(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int age = rs.getInt("age");
        String gender = rs.getString("gender");
        String phone = rs.getString("phone");
        String address = rs.getString("address");
        String bg = rs.getString("blood_group");
        int room = rs.getInt("room_no");
        String admit = rs.getString("admit_date");
        int isIn = rs.getInt("is_inpatient");

        if (isIn == 1) {
            return new InPatient(id, name, age, gender, phone, address, bg, room, admit);
        }
        return new Patient(id, name, age, gender, phone, address, bg);
    }
}
