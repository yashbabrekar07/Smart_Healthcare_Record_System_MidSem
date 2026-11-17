package hms.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import hms.db.AppointmentDB;
import hms.db.DoctorDB;
import hms.db.PatientDB;
import hms.model.Appointment;
import hms.model.Doctor;
import hms.util.Validator;

// Appointment management panel with Vector combo box and HashMap doctor name lookup
public class AppointmentPanel extends JPanel implements ActionListener, MouseListener {

    private JTextField txtId, txtPatientId, txtDate, txtTime, txtReason, txtFilterDate;
    private JComboBox<Doctor> cmbDoctors; // Vector<Doctor> data model
    private JComboBox<String> cmbStatus;
    private JButton btnBook, btnStatusDone, btnStatusCancel, btnDelete, btnClear, btnFilterDate, btnShowAll;
    private JTable table;
    private DefaultTableModel tableModel;

    private AppointmentDB appointmentDB;
    private DoctorDB doctorDB;
    private PatientDB patientDB;
    private HashMap<Integer, String> doctorNameMap; // HashMap from doctor id to name

    public AppointmentPanel() {
        appointmentDB = new AppointmentDB();
        doctorDB = new DoctorDB();
        patientDB = new PatientDB();
        doctorNameMap = new HashMap<Integer, String>();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        Font font = new Font("SansSerif", Font.PLAIN, 13);
        Font boldFont = new Font("SansSerif", Font.BOLD, 13);

        // Top Filter Bar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBackground(new Color(245, 245, 245));
        JLabel lblFilter = new JLabel("Filter by Date (YYYY-MM-DD):");
        lblFilter.setFont(boldFont);
        txtFilterDate = new JTextField(12);
        txtFilterDate.setFont(font);
        btnFilterDate = new JButton("Filter");
        btnShowAll = new JButton("Show All");

        btnFilterDate.setFont(font);
        btnFilterDate.setBackground(new Color(0, 128, 128));
        btnFilterDate.setForeground(Color.WHITE);
        btnFilterDate.addActionListener(this);

        btnShowAll.setFont(font);
        btnShowAll.setBackground(new Color(0, 128, 128));
        btnShowAll.setForeground(Color.WHITE);
        btnShowAll.addActionListener(this);

        topPanel.add(lblFilter);
        topPanel.add(txtFilterDate);
        topPanel.add(btnFilterDate);
        topPanel.add(btnShowAll);
        add(topPanel, BorderLayout.NORTH);

        // Form Container
        JPanel formContainer = new JPanel(new BorderLayout(5, 5));
        formContainer.setBackground(Color.WHITE);

        JPanel gridPanel = new JPanel(new GridLayout(7, 2, 8, 8));
        gridPanel.setBackground(Color.WHITE);

        gridPanel.add(new JLabel("Appointment ID:"));
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setFont(font);
        gridPanel.add(txtId);

        gridPanel.add(new JLabel("Patient ID:"));
        txtPatientId = new JTextField();
        txtPatientId.setFont(font);
        gridPanel.add(txtPatientId);

        gridPanel.add(new JLabel("Doctor (Vector<Doctor>):"));
        cmbDoctors = new JComboBox<Doctor>();
        cmbDoctors.setFont(font);
        gridPanel.add(cmbDoctors);

        gridPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        txtDate = new JTextField();
        txtDate.setFont(font);
        gridPanel.add(txtDate);

        gridPanel.add(new JLabel("Time (HH:MM):"));
        txtTime = new JTextField();
        txtTime.setFont(font);
        gridPanel.add(txtTime);

        gridPanel.add(new JLabel("Reason:"));
        txtReason = new JTextField();
        txtReason.setFont(font);
        gridPanel.add(txtReason);

        gridPanel.add(new JLabel("Status:"));
        cmbStatus = new JComboBox<String>(new String[]{"Booked", "Done", "Cancelled"});
        cmbStatus.setFont(font);
        gridPanel.add(cmbStatus);

        formContainer.add(gridPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        btnPanel.setBackground(Color.WHITE);

        btnBook = new JButton("Book");
        btnStatusDone = new JButton("Mark Done");
        btnStatusCancel = new JButton("Cancel");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        JButton[] buttons = {btnBook, btnStatusDone, btnStatusCancel, btnDelete, btnClear};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setFont(font);
            buttons[i].setBackground(new Color(0, 128, 128));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].addActionListener(this);
            btnPanel.add(buttons[i]);
        }
        formContainer.add(btnPanel, BorderLayout.SOUTH);

        // Table
        String[] cols = {"ID", "Patient ID", "Doctor ID", "Doctor Name (Map)", "Date", "Time", "Reason", "Status"};
        tableModel = new DefaultTableModel(cols, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null); // Disable cell editing without anonymous class
        table.setFont(font);
        table.setRowHeight(22);
        table.addMouseListener(this);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(550, 400));

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.add(formContainer);
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        refreshDoctorList();
        loadTableData();
    }

    public void refreshDoctorList() {
        try {
            Vector<Doctor> docs = doctorDB.getDoctorsVector();
            cmbDoctors.removeAllItems();
            doctorNameMap.clear();
            for (int i = 0; i < docs.size(); i++) {
                Doctor d = docs.get(i);
                cmbDoctors.addItem(d);
                doctorNameMap.put(Integer.valueOf(d.getId()), d.getName()); // HashMap populating
            }
        } catch (Exception ex) {
            System.out.println("Doctor list refresh: " + ex.getMessage());
        }
    }

    public void loadTableData() {
        tableModel.setRowCount(0);
        try {
            ArrayList<Appointment> list = appointmentDB.getAllAppointments();
            for (int i = 0; i < list.size(); i++) {
                Appointment a = list.get(i);
                String docName = doctorNameMap.get(Integer.valueOf(a.getDoctorId()));
                if (docName == null) docName = "Doctor #" + a.getDoctorId();

                tableModel.addRow(new Object[]{
                    a.getId(), a.getPatientId(), a.getDoctorId(), docName,
                    a.getDate(), a.getTime(), a.getReason(), a.getStatus()
                });
            }
        } catch (Exception ex) {
            System.out.println("Load appointments: " + ex.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnBook) {
            handleBook();
        } else if (src == btnStatusDone) {
            handleStatusChange("Done");
        } else if (src == btnStatusCancel) {
            handleStatusChange("Cancelled");
        } else if (src == btnDelete) {
            handleDelete();
        } else if (src == btnClear) {
            clearForm();
        } else if (src == btnFilterDate) {
            handleFilterDate();
        } else if (src == btnShowAll) {
            loadTableData();
        }
    }

    private void handleBook() {
        try {
            int patientId = Integer.parseInt(txtPatientId.getText().trim());
            if (patientDB.getPatientById(patientId) == null) {
                JOptionPane.showMessageDialog(this, "Patient #" + patientId + " does not exist. Register first.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Doctor selectedDoc = (Doctor) cmbDoctors.getSelectedItem();
            if (selectedDoc == null) {
                JOptionPane.showMessageDialog(this, "Please select a doctor.");
                return;
            }
            int docId = selectedDoc.getId();

            String date = txtDate.getText().trim();
            Validator.validateDate(date);
            String time = txtTime.getText().trim();
            String reason = txtReason.getText().trim();
            String status = cmbStatus.getSelectedItem().toString();

            appointmentDB.addAppointment(new Appointment(0, patientId, docId, date, time, reason, status));
            loadTableData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Appointment booked successfully.");
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Patient ID must be an integer.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error booking: " + ex.getMessage());
        }
    }

    private void handleStatusChange(String newStatus) {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select an appointment from the table.");
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            appointmentDB.updateStatus(id, newStatus);
            loadTableData();
            JOptionPane.showMessageDialog(this, "Appointment marked as " + newStatus + ".");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Status update error: " + ex.getMessage());
        }
    }

    private void handleDelete() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select an appointment to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete appointment #" + idStr + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                appointmentDB.deleteAppointment(Integer.parseInt(idStr));
                loadTableData();
                clearForm();
                JOptionPane.showMessageDialog(this, "Appointment deleted.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Delete error: " + ex.getMessage());
            }
        }
    }

    private void handleFilterDate() {
        String date = txtFilterDate.getText().trim();
        tableModel.setRowCount(0);
        try {
            ArrayList<Appointment> list = appointmentDB.getAppointmentsByDate(date);
            for (int i = 0; i < list.size(); i++) {
                Appointment a = list.get(i);
                String docName = doctorNameMap.get(Integer.valueOf(a.getDoctorId()));
                if (docName == null) docName = "Doctor #" + a.getDoctorId();
                tableModel.addRow(new Object[]{
                    a.getId(), a.getPatientId(), a.getDoctorId(), docName,
                    a.getDate(), a.getTime(), a.getReason(), a.getStatus()
                });
            }
        } catch (Exception ex) {
            System.out.println("Date filter: " + ex.getMessage());
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtPatientId.setText("");
        txtDate.setText("");
        txtTime.setText("");
        txtReason.setText("");
    }

    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            txtPatientId.setText(tableModel.getValueAt(row, 1).toString());
            txtDate.setText(tableModel.getValueAt(row, 4).toString());
            txtTime.setText(tableModel.getValueAt(row, 5).toString());
            txtReason.setText(tableModel.getValueAt(row, 6).toString());
            cmbStatus.setSelectedItem(tableModel.getValueAt(row, 7).toString());
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
