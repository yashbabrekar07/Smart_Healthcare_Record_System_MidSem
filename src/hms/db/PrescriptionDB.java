package hms.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import hms.model.Prescription;

// Prescription database access layer
public class PrescriptionDB {

    public ArrayList<Prescription> getAllPrescriptions() throws SQLException {
        ArrayList<Prescription> list = new ArrayList<Prescription>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM prescriptions ORDER BY id DESC");
            while (rs.next()) {
                list.add(new Prescription(
                    rs.getInt("id"), rs.getInt("patient_id"), rs.getInt("doctor_id"),
                    rs.getString("pres_date"), rs.getString("medicines"), rs.getString("instructions")));
            }
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return list;
    }

    public void addPrescription(Prescription p) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO prescriptions (patient_id, doctor_id, pres_date, medicines, instructions) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, p.getPatientId());
            pstmt.setInt(2, p.getDoctorId());
            pstmt.setString(3, p.getDate());
            pstmt.setString(4, p.getMedicines());
            pstmt.setString(5, p.getInstructions());
            pstmt.executeUpdate();
        } finally {
            DBConnection.close(null, pstmt, conn);
        }
    }

    public void deletePrescription(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM prescriptions WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } finally {
            DBConnection.close(null, pstmt, conn);
        }
    }

    public ArrayList<Prescription> getPrescriptionsByPatient(int patientId) throws SQLException {
        ArrayList<Prescription> list = new ArrayList<Prescription>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM prescriptions WHERE patient_id = ? ORDER BY pres_date ASC");
            pstmt.setInt(1, patientId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Prescription(
                    rs.getInt("id"), rs.getInt("patient_id"), rs.getInt("doctor_id"),
                    rs.getString("pres_date"), rs.getString("medicines"), rs.getString("instructions")));
            }
        } finally {
            DBConnection.close(rs, pstmt, conn);
        }
        return list;
    }
}
