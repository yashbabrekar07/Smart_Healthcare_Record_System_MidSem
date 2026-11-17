package hms.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import hms.model.Appointment;

// Appointment database access layer
public class AppointmentDB {

    public ArrayList<Appointment> getAllAppointments() throws SQLException {
        ArrayList<Appointment> list = new ArrayList<Appointment>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM appointments ORDER BY app_date ASC, app_time ASC");
            while (rs.next()) {
                list.add(new Appointment(
                    rs.getInt("id"), rs.getInt("patient_id"), rs.getInt("doctor_id"),
                    rs.getString("app_date"), rs.getString("app_time"), rs.getString("reason"), rs.getString("status")));
            }
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return list;
    }

    public void addAppointment(Appointment a) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO appointments (patient_id, doctor_id, app_date, app_time, reason, status) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, a.getPatientId());
            pstmt.setInt(2, a.getDoctorId());
            pstmt.setString(3, a.getDate());
            pstmt.setString(4, a.getTime());
            pstmt.setString(5, a.getReason());
            pstmt.setString(6, a.getStatus());
            pstmt.executeUpdate();
        } finally {
            DBConnection.close(null, pstmt, conn);
        }
    }

    public void updateStatus(int id, String status) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("UPDATE appointments SET status = ? WHERE id = ?");
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } finally {
            DBConnection.close(null, pstmt, conn);
        }
    }

    public void deleteAppointment(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM appointments WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } finally {
            DBConnection.close(null, pstmt, conn);
        }
    }

    public ArrayList<Appointment> getAppointmentsByDate(String date) throws SQLException {
        ArrayList<Appointment> list = new ArrayList<Appointment>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM appointments WHERE app_date = ? ORDER BY app_time ASC");
            pstmt.setString(1, date);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Appointment(
                    rs.getInt("id"), rs.getInt("patient_id"), rs.getInt("doctor_id"),
                    rs.getString("app_date"), rs.getString("app_time"), rs.getString("reason"), rs.getString("status")));
            }
        } finally {
            DBConnection.close(rs, pstmt, conn);
        }
        return list;
    }

    public ArrayList<Appointment> getAppointmentsByPatient(int patientId) throws SQLException {
        ArrayList<Appointment> list = new ArrayList<Appointment>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM appointments WHERE patient_id = ? ORDER BY app_date ASC");
            pstmt.setInt(1, patientId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Appointment(
                    rs.getInt("id"), rs.getInt("patient_id"), rs.getInt("doctor_id"),
                    rs.getString("app_date"), rs.getString("app_time"), rs.getString("reason"), rs.getString("status")));
            }
        } finally {
            DBConnection.close(rs, pstmt, conn);
        }
        return list;
    }
}
