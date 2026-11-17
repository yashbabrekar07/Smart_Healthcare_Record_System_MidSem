package hms.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import hms.model.MedicalReport;

// Medical report database access layer
public class ReportDB {

    public ArrayList<MedicalReport> getAllReports() throws SQLException {
        ArrayList<MedicalReport> list = new ArrayList<MedicalReport>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM reports ORDER BY id DESC");
            while (rs.next()) {
                list.add(new MedicalReport(
                    rs.getInt("id"), rs.getInt("patient_id"), rs.getString("rep_date"),
                    rs.getString("diagnosis"), rs.getString("notes")));
            }
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return list;
    }

    public void addReport(MedicalReport r) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO reports (patient_id, rep_date, diagnosis, notes) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, r.getPatientId());
            pstmt.setString(2, r.getDate());
            pstmt.setString(3, r.getDiagnosis());
            pstmt.setString(4, r.getNotes());
            pstmt.executeUpdate();
        } finally {
            DBConnection.close(null, pstmt, conn);
        }
    }

    public void deleteReport(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM reports WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } finally {
            DBConnection.close(null, pstmt, conn);
        }
    }

    public ArrayList<MedicalReport> getReportsByPatient(int patientId) throws SQLException {
        ArrayList<MedicalReport> list = new ArrayList<MedicalReport>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM reports WHERE patient_id = ? ORDER BY rep_date ASC");
            pstmt.setInt(1, patientId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MedicalReport(
                    rs.getInt("id"), rs.getInt("patient_id"), rs.getString("rep_date"),
                    rs.getString("diagnosis"), rs.getString("notes")));
            }
        } finally {
            DBConnection.close(rs, pstmt, conn);
        }
        return list;
    }
}
