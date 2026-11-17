package hms.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;
import hms.model.Doctor;

// Doctor database access layer with CallableStatement
public class DoctorDB {

    public ArrayList<Doctor> getAllDoctors() throws SQLException {
        ArrayList<Doctor> list = new ArrayList<Doctor>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM doctors ORDER BY id ASC");
            while (rs.next()) {
                list.add(new Doctor(
                    rs.getInt("id"), rs.getString("name"), rs.getInt("age"), rs.getString("gender"),
                    rs.getString("phone"), rs.getString("address"), rs.getString("specialization"), rs.getDouble("fee")));
            }
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return list;
    }

    public Vector<Doctor> getDoctorsVector() throws SQLException {
        return new Vector<Doctor>(getAllDoctors());
    }

    public HashSet<String> getSpecializations() throws SQLException {
        HashSet<String> specs = new HashSet<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT DISTINCT specialization FROM doctors");
            while (rs.next()) {
                specs.add(rs.getString(1));
            }
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return specs;
    }

    public void addDoctor(Doctor d) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO doctors (name, age, gender, phone, address, specialization, fee) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, d.getName());
            pstmt.setInt(2, d.getAge());
            pstmt.setString(3, d.getGender());
            pstmt.setString(4, d.getPhone());
            pstmt.setString(5, d.getAddress());
            pstmt.setString(6, d.getSpecialization());
            pstmt.setDouble(7, d.getFee());
            pstmt.executeUpdate();
        } finally {
            DBConnection.close(null, pstmt, conn);
        }
    }

    public void updateDoctor(Doctor d) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE doctors SET name=?, age=?, gender=?, phone=?, address=?, specialization=?, fee=? WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, d.getName());
            pstmt.setInt(2, d.getAge());
            pstmt.setString(3, d.getGender());
            pstmt.setString(4, d.getPhone());
            pstmt.setString(5, d.getAddress());
            pstmt.setString(6, d.getSpecialization());
            pstmt.setDouble(7, d.getFee());
            pstmt.setInt(8, d.getId());
            pstmt.executeUpdate();
        } finally {
            DBConnection.close(null, pstmt, conn);
        }
    }

    public void deleteDoctor(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM doctors WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } finally {
            DBConnection.close(null, pstmt, conn);
        }
    }

    public Doctor getDoctorById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Doctor d = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM doctors WHERE id = ?");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                d = new Doctor(
                    rs.getInt("id"), rs.getString("name"), rs.getInt("age"), rs.getString("gender"),
                    rs.getString("phone"), rs.getString("address"), rs.getString("specialization"), rs.getDouble("fee"));
            }
        } finally {
            DBConnection.close(rs, pstmt, conn);
        }
        return d;
    }

    // CallableStatement calling stored procedure doctor_appointment_count(IN docId, OUT total)
    public int getDoctorAppointmentCount(int docId) throws SQLException {
        Connection conn = null;
        CallableStatement cstmt = null;
        int total = 0;

        try {
            conn = DBConnection.getConnection();
            cstmt = conn.prepareCall("{call doctor_appointment_count(?, ?)}");
            cstmt.setInt(1, docId);
            cstmt.registerOutParameter(2, Types.INTEGER);
            cstmt.execute();
            total = cstmt.getInt(2);
        } finally {
            DBConnection.close(null, cstmt, conn);
        }
        return total;
    }
}
