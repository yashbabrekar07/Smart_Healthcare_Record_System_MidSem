package hms.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import hms.db.AppointmentDB;
import hms.db.PatientDB;
import hms.db.PrescriptionDB;
import hms.db.ReportDB;
import hms.model.Appointment;
import hms.model.InPatient;
import hms.model.MedicalReport;
import hms.model.Patient;
import hms.model.Prescription;

// Patient management panel implementing delegation listeners directly
public class PatientPanel extends JPanel implements ActionListener, MouseListener, KeyListener {

    private JTextField txtId, txtName, txtAge, txtPhone, txtAddress, txtRoomNo, txtAdmitDate, txtSearch;
    private JComboBox<String> cmbGender, cmbBloodGroup;
    private JCheckBox chkInPatient;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnHistory;
    private JTable table;
    private DefaultTableModel tableModel;

    private PatientDB patientDB;
    private AppointmentDB appointmentDB;
    private PrescriptionDB prescriptionDB;
    private ReportDB reportDB;

    public PatientPanel() {
        patientDB = new PatientDB();
        appointmentDB = new AppointmentDB();
        prescriptionDB = new PrescriptionDB();
        reportDB = new ReportDB();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        Font font = new Font("SansSerif", Font.PLAIN, 13);
        Font boldFont = new Font("SansSerif", Font.BOLD, 13);

        // Top Search Bar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBackground(new Color(245, 245, 245));
        JLabel lblSearch = new JLabel("Live Search (Name / Phone / ID):");
        lblSearch.setFont(boldFont);
        txtSearch = new JTextField(22);
        txtSearch.setFont(font);
        txtSearch.addKeyListener(this); // KeyListener delegation
        topPanel.add(lblSearch);
        topPanel.add(txtSearch);
        add(topPanel, BorderLayout.NORTH);

        // Center Form Panel
        JPanel formContainer = new JPanel(new BorderLayout(5, 5));
        formContainer.setBackground(Color.WHITE);

        JPanel gridPanel = new JPanel(new GridLayout(9, 2, 8, 8));
        gridPanel.setBackground(Color.WHITE);

        gridPanel.add(new JLabel("Patient ID:"));
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setFont(font);
        gridPanel.add(txtId);

        gridPanel.add(new JLabel("Full Name:"));
        txtName = new JTextField();
        txtName.setFont(font);
        gridPanel.add(txtName);

        gridPanel.add(new JLabel("Age:"));
        txtAge = new JTextField();
        txtAge.setFont(font);
        gridPanel.add(txtAge);

        gridPanel.add(new JLabel("Gender:"));
        cmbGender = new JComboBox<String>(new String[]{"Male", "Female", "Other"});
        cmbGender.setFont(font);
        gridPanel.add(cmbGender);

        gridPanel.add(new JLabel("Phone (10 digits):"));
        txtPhone = new JTextField();
        txtPhone.setFont(font);
        gridPanel.add(txtPhone);

        gridPanel.add(new JLabel("Address:"));
        txtAddress = new JTextField();
        txtAddress.setFont(font);
        gridPanel.add(txtAddress);

        gridPanel.add(new JLabel("Blood Group:"));
        cmbBloodGroup = new JComboBox<String>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        cmbBloodGroup.setFont(font);
        gridPanel.add(cmbBloodGroup);

        gridPanel.add(new JLabel("Is In-Patient:"));
        chkInPatient = new JCheckBox("Admitted Patient");
        chkInPatient.setBackground(Color.WHITE);
        chkInPatient.setFont(font);
        gridPanel.add(chkInPatient);

        gridPanel.add(new JLabel("Room No & Admit Date:"));
        JPanel roomDatePanel = new JPanel(new GridLayout(1, 2, 5, 0));
        roomDatePanel.setBackground(Color.WHITE);
        txtRoomNo = new JTextField("0");
        txtRoomNo.setFont(font);
        txtAdmitDate = new JTextField("YYYY-MM-DD");
        txtAdmitDate.setFont(font);
        roomDatePanel.add(txtRoomNo);
        roomDatePanel.add(txtAdmitDate);
        gridPanel.add(roomDatePanel);

        formContainer.add(gridPanel, BorderLayout.CENTER);

        // Buttons Row
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        btnPanel.setBackground(Color.WHITE);

        btnAdd = new JButton("Add Patient");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear Form");
        btnHistory = new JButton("View Medical History");

        JButton[] buttons = {btnAdd, btnUpdate, btnDelete, btnClear, btnHistory};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setFont(font);
            buttons[i].setBackground(new Color(0, 128, 128));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].addActionListener(this); // ActionListener delegation
            btnPanel.add(buttons[i]);
        }
        formContainer.add(btnPanel, BorderLayout.SOUTH);

        // Right Table
        String[] columns = {"ID", "Name", "Age", "Gender", "Phone", "Blood", "In-Patient", "Room", "Admit Date"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null); // Disable cell editing without anonymous class
        table.setFont(font);
        table.setRowHeight(22);
        table.addMouseListener(this); // MouseListener delegation

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(550, 400));

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.add(formContainer);
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        loadTableData();
    }

    public void loadTableData() {
        tableModel.setRowCount(0);
        try {
            ArrayList<Patient> list = patientDB.getAllPatients();
            for (int i = 0; i < list.size(); i++) {
                Patient p = list.get(i);
                int room = 0;
                String admit = "";
                String isIn = "No";
                if (p instanceof InPatient) {
                    InPatient ip = (InPatient) p;
                    room = ip.getRoomNo();
                    admit = ip.getAdmitDate();
                    isIn = "Yes";
                }
                tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), p.getAge(), p.getGender(), p.getPhone(),
                    p.getBloodGroup(), isIn, room == 0 ? "-" : String.valueOf(room), admit
                });
            }
        } catch (Exception e) {
            System.out.println("Load patient table: " + e.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnAdd) {
            handleAdd();
        } else if (src == btnUpdate) {
            handleUpdate();
        } else if (src == btnDelete) {
            handleDelete();
        } else if (src == btnClear) {
            clearForm();
        } else if (src == btnHistory) {
            showHistoryDialog();
        }
    }

    private void handleAdd() {
        String name = txtName.getText().trim();
        String ageStr = txtAge.getText().trim();
        String phone = txtPhone.getText().trim();
        String bg = cmbBloodGroup.getSelectedItem().toString();

        if (!patientDB.validatePatientFields(ageStr, name, phone, bg)) {
            JOptionPane.showMessageDialog(this, "Please check form inputs (valid name, age 0-120, 10-digit phone).", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            String gender = cmbGender.getSelectedItem().toString();
            String address = txtAddress.getText().trim();

            if (chkInPatient.isSelected()) {
                int room = Integer.parseInt(txtRoomNo.getText().trim());
                String date = txtAdmitDate.getText().trim();
                patientDB.addPatient(new InPatient(0, name, age, gender, phone, address, bg, room, date));
            } else {
                patientDB.addPatient(new Patient(name, age, gender, phone, address, bg));
            }
            loadTableData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Patient added successfully.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding patient: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a patient from the table first.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            String name = txtName.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String gender = cmbGender.getSelectedItem().toString();
            String phone = txtPhone.getText().trim();
            String address = txtAddress.getText().trim();
            String bg = cmbBloodGroup.getSelectedItem().toString();

            if (chkInPatient.isSelected()) {
                int room = Integer.parseInt(txtRoomNo.getText().trim());
                String date = txtAdmitDate.getText().trim();
                patientDB.updatePatient(new InPatient(id, name, age, gender, phone, address, bg, room, date));
            } else {
                patientDB.updatePatient(new Patient(id, name, age, gender, phone, address, bg));
            }
            loadTableData();
            JOptionPane.showMessageDialog(this, "Patient updated.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating: " + ex.getMessage());
        }
    }

    private void handleDelete() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a patient to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete patient #" + idStr + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idStr);
                patientDB.deletePatient(id);
                loadTableData();
                clearForm();
                JOptionPane.showMessageDialog(this, "Patient deleted.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Delete failed: " + ex.getMessage());
            }
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtRoomNo.setText("0");
        txtAdmitDate.setText("YYYY-MM-DD");
        chkInPatient.setSelected(false);
    }

    private void showHistoryDialog() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a patient from table to view medical history.");
            return;
        }

        try {
            int patientId = Integer.parseInt(idStr);
            Patient p = patientDB.getPatientById(patientId);
            String pName = (p != null) ? p.getName() : ("Patient #" + patientId);

            ArrayList<Appointment> apps = appointmentDB.getAppointmentsByPatient(patientId);
            ArrayList<Prescription> pres = prescriptionDB.getPrescriptionsByPatient(patientId);
            ArrayList<MedicalReport> reps = reportDB.getReportsByPatient(patientId);

            JDialog dialog = new JDialog();
            dialog.setTitle("Medical History - " + pName);
            dialog.setSize(600, 450);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(5, 5));

            JTextArea txtArea = new JTextArea();
            txtArea.setEditable(false);
            txtArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            txtArea.append("========================================================\n");
            txtArea.append(" COMPLETE MEDICAL HISTORY FOR: " + pName + " (ID: " + patientId + ")\n");
            txtArea.append("========================================================\n\n");

            txtArea.append("--- APPOINTMENTS ---\n");
            if (apps.isEmpty()) txtArea.append("  No appointments found.\n");
            for (int i = 0; i < apps.size(); i++) {
                txtArea.append("  * " + apps.get(i).getSummary() + "\n");
            }

            txtArea.append("\n--- PRESCRIPTIONS ---\n");
            if (pres.isEmpty()) txtArea.append("  No prescriptions found.\n");
            for (int i = 0; i < pres.size(); i++) {
                txtArea.append("  * " + pres.get(i).getSummary() + "\n");
            }

            txtArea.append("\n--- MEDICAL REPORTS ---\n");
            if (reps.isEmpty()) txtArea.append("  No reports found.\n");
            for (int i = 0; i < reps.size(); i++) {
                txtArea.append("  * " + reps.get(i).getSummary() + "\n");
            }

            dialog.add(new JScrollPane(txtArea), BorderLayout.CENTER);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not load history: " + ex.getMessage());
        }
    }

    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            txtName.setText(tableModel.getValueAt(row, 1).toString());
            txtAge.setText(tableModel.getValueAt(row, 2).toString());
            cmbGender.setSelectedItem(tableModel.getValueAt(row, 3).toString());
            txtPhone.setText(tableModel.getValueAt(row, 4).toString());
            cmbBloodGroup.setSelectedItem(tableModel.getValueAt(row, 5).toString());

            String isIn = tableModel.getValueAt(row, 6).toString();
            if ("Yes".equals(isIn)) {
                chkInPatient.setSelected(true);
                txtRoomNo.setText(tableModel.getValueAt(row, 7).toString());
                txtAdmitDate.setText(tableModel.getValueAt(row, 8).toString());
            } else {
                chkInPatient.setSelected(false);
                txtRoomNo.setText("0");
                txtAdmitDate.setText("");
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        String query = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        try {
            ArrayList<Patient> list = query.isEmpty() ? patientDB.getAllPatients() : patientDB.search(query);
            for (int i = 0; i < list.size(); i++) {
                Patient p = list.get(i);
                int room = 0;
                String admit = "";
                String isIn = "No";
                if (p instanceof InPatient) {
                    InPatient ip = (InPatient) p;
                    room = ip.getRoomNo();
                    admit = ip.getAdmitDate();
                    isIn = "Yes";
                }
                tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), p.getAge(), p.getGender(), p.getPhone(),
                    p.getBloodGroup(), isIn, room == 0 ? "-" : String.valueOf(room), admit
                });
            }
        } catch (Exception ex) {
            System.out.println("Search error: " + ex.getMessage());
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}
}
