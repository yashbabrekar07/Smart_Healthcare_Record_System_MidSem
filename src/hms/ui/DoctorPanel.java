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
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import hms.db.DoctorDB;
import hms.model.Doctor;
import hms.util.Validator;

// Doctor management panel with CallableStatement procedure call and HashSet filtering
public class DoctorPanel extends JPanel implements ActionListener, MouseListener {

    private JTextField txtId, txtName, txtAge, txtPhone, txtAddress, txtSpecialization, txtFee;
    private JComboBox<String> cmbGender, cmbFilterSpec;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnAppCount, btnFilter;
    private JTable table;
    private DefaultTableModel tableModel;
    private DoctorDB doctorDB;

    public DoctorPanel() {
        doctorDB = new DoctorDB();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        Font font = new Font("SansSerif", Font.PLAIN, 13);
        Font boldFont = new Font("SansSerif", Font.BOLD, 13);

        // Top Filter Panel (using HashSet<String> for distinct specializations)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBackground(new Color(245, 245, 245));
        JLabel lblFilter = new JLabel("Filter Specialization (HashSet):");
        lblFilter.setFont(boldFont);
        cmbFilterSpec = new JComboBox<String>();
        cmbFilterSpec.setFont(font);
        btnFilter = new JButton("Apply Filter");
        btnFilter.setFont(font);
        btnFilter.setBackground(new Color(0, 128, 128));
        btnFilter.setForeground(Color.WHITE);
        btnFilter.addActionListener(this);

        topPanel.add(lblFilter);
        topPanel.add(cmbFilterSpec);
        topPanel.add(btnFilter);
        add(topPanel, BorderLayout.NORTH);

        // Form Container
        JPanel formContainer = new JPanel(new BorderLayout(5, 5));
        formContainer.setBackground(Color.WHITE);

        JPanel gridPanel = new JPanel(new GridLayout(8, 2, 8, 8));
        gridPanel.setBackground(Color.WHITE);

        gridPanel.add(new JLabel("Doctor ID:"));
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setFont(font);
        gridPanel.add(txtId);

        gridPanel.add(new JLabel("Doctor Name:"));
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

        gridPanel.add(new JLabel("Specialization:"));
        txtSpecialization = new JTextField();
        txtSpecialization.setFont(font);
        gridPanel.add(txtSpecialization);

        gridPanel.add(new JLabel("Consultation Fee ($):"));
        txtFee = new JTextField();
        txtFee.setFont(font);
        gridPanel.add(txtFee);

        formContainer.add(gridPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        btnPanel.setBackground(Color.WHITE);

        btnAdd = new JButton("Add Doctor");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        btnAppCount = new JButton("Check Total Appts (CallableStmt)");

        JButton[] buttons = {btnAdd, btnUpdate, btnDelete, btnClear, btnAppCount};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setFont(font);
            buttons[i].setBackground(new Color(0, 128, 128));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].addActionListener(this);
            btnPanel.add(buttons[i]);
        }
        formContainer.add(btnPanel, BorderLayout.SOUTH);

        // Table
        String[] cols = {"ID", "Doctor Name", "Age", "Gender", "Phone", "Specialization", "Fee ($)"};
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

        loadTableData();
        refreshSpecializationsFilter();
    }

    public void loadTableData() {
        tableModel.setRowCount(0);
        try {
            ArrayList<Doctor> list = doctorDB.getAllDoctors();
            for (int i = 0; i < list.size(); i++) {
                Doctor d = list.get(i);
                tableModel.addRow(new Object[]{
                    d.getId(), d.getName(), d.getAge(), d.getGender(), d.getPhone(), d.getSpecialization(), d.getFee()
                });
            }
        } catch (Exception ex) {
            System.out.println("Load doctors: " + ex.getMessage());
        }
    }

    public void refreshSpecializationsFilter() {
        try {
            cmbFilterSpec.removeAllItems();
            cmbFilterSpec.addItem("All Specializations");
            HashSet<String> specs = doctorDB.getSpecializations();
            Iterator<String> it = specs.iterator();
            while (it.hasNext()) {
                cmbFilterSpec.addItem(it.next());
            }
        } catch (Exception ex) {
            System.out.println("Filter load: " + ex.getMessage());
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
        } else if (src == btnAppCount) {
            handleAppointmentCountProcedure();
        } else if (src == btnFilter) {
            handleFilter();
        }
    }

    private void handleAdd() {
        try {
            String name = txtName.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String gender = cmbGender.getSelectedItem().toString();
            String phone = txtPhone.getText().trim();
            String address = txtAddress.getText().trim();
            String spec = txtSpecialization.getText().trim();
            double fee = Double.parseDouble(txtFee.getText().trim());

            Validator.validateName(name);
            Validator.validateAge(age);
            Validator.validatePhone(phone);

            doctorDB.addDoctor(new Doctor(0, name, age, gender, phone, address, spec, fee));
            loadTableData();
            refreshSpecializationsFilter();
            clearForm();
            JOptionPane.showMessageDialog(this, "Doctor registered successfully.");
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Age and Fee must be valid numbers.", "Validation", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a doctor from the table.");
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            String name = txtName.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String gender = cmbGender.getSelectedItem().toString();
            String phone = txtPhone.getText().trim();
            String address = txtAddress.getText().trim();
            String spec = txtSpecialization.getText().trim();
            double fee = Double.parseDouble(txtFee.getText().trim());

            doctorDB.updateDoctor(new Doctor(id, name, age, gender, phone, address, spec, fee));
            loadTableData();
            refreshSpecializationsFilter();
            JOptionPane.showMessageDialog(this, "Doctor record updated.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update error: " + ex.getMessage());
        }
    }

    private void handleDelete() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a doctor to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete doctor #" + idStr + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                doctorDB.deleteDoctor(Integer.parseInt(idStr));
                loadTableData();
                refreshSpecializationsFilter();
                clearForm();
                JOptionPane.showMessageDialog(this, "Doctor deleted.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Delete error: " + ex.getMessage());
            }
        }
    }

    // Demonstrates CallableStatement stored procedure execution
    private void handleAppointmentCountProcedure() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a doctor first to check appointments.");
            return;
        }
        try {
            int docId = Integer.parseInt(idStr);
            int count = doctorDB.getDoctorAppointmentCount(docId); // CallableStatement
            JOptionPane.showMessageDialog(this, "Total booked appointments for Dr. " + txtName.getText() + ": " + count,
                "CallableStatement Result", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Procedure error: " + ex.getMessage());
        }
    }

    private void handleFilter() {
        String selected = cmbFilterSpec.getSelectedItem().toString();
        tableModel.setRowCount(0);
        try {
            ArrayList<Doctor> list = doctorDB.getAllDoctors();
            for (int i = 0; i < list.size(); i++) {
                Doctor d = list.get(i);
                if ("All Specializations".equals(selected) || d.getSpecialization().equalsIgnoreCase(selected)) {
                    tableModel.addRow(new Object[]{
                        d.getId(), d.getName(), d.getAge(), d.getGender(), d.getPhone(), d.getSpecialization(), d.getFee()
                    });
                }
            }
        } catch (Exception ex) {
            System.out.println("Filter: " + ex.getMessage());
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtSpecialization.setText("");
        txtFee.setText("");
    }

    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            txtName.setText(tableModel.getValueAt(row, 1).toString());
            txtAge.setText(tableModel.getValueAt(row, 2).toString());
            cmbGender.setSelectedItem(tableModel.getValueAt(row, 3).toString());
            txtPhone.setText(tableModel.getValueAt(row, 4).toString());
            txtSpecialization.setText(tableModel.getValueAt(row, 5).toString());
            txtFee.setText(tableModel.getValueAt(row, 6).toString());
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
