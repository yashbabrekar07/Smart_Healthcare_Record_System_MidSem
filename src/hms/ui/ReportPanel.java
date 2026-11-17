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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import hms.db.PatientDB;
import hms.db.ReportDB;
import hms.model.MedicalReport;
import hms.util.Validator;

// Medical report management panel
public class ReportPanel extends JPanel implements ActionListener, MouseListener {

    private JTextField txtId, txtPatientId, txtDate, txtDiagnosis, txtNotes;
    private JButton btnAdd, btnDelete, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;

    private ReportDB reportDB;
    private PatientDB patientDB;

    public ReportPanel() {
        reportDB = new ReportDB();
        patientDB = new PatientDB();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        Font font = new Font("SansSerif", Font.PLAIN, 13);

        JPanel formContainer = new JPanel(new BorderLayout(5, 5));
        formContainer.setBackground(Color.WHITE);

        JPanel gridPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        gridPanel.setBackground(Color.WHITE);

        gridPanel.add(new JLabel("Report ID:"));
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setFont(font);
        gridPanel.add(txtId);

        gridPanel.add(new JLabel("Patient ID:"));
        txtPatientId = new JTextField();
        txtPatientId.setFont(font);
        gridPanel.add(txtPatientId);

        gridPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        txtDate = new JTextField();
        txtDate.setFont(font);
        gridPanel.add(txtDate);

        gridPanel.add(new JLabel("Diagnosis:"));
        txtDiagnosis = new JTextField();
        txtDiagnosis.setFont(font);
        gridPanel.add(txtDiagnosis);

        gridPanel.add(new JLabel("Clinical Notes:"));
        txtNotes = new JTextField();
        txtNotes.setFont(font);
        gridPanel.add(txtNotes);

        formContainer.add(gridPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        btnPanel.setBackground(Color.WHITE);

        btnAdd = new JButton("Add Report");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        JButton[] buttons = {btnAdd, btnDelete, btnClear};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setFont(font);
            buttons[i].setBackground(new Color(0, 128, 128));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].addActionListener(this);
            btnPanel.add(buttons[i]);
        }
        formContainer.add(btnPanel, BorderLayout.SOUTH);

        String[] cols = {"ID", "Patient ID", "Date", "Diagnosis", "Notes"};
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
    }

    public void loadTableData() {
        tableModel.setRowCount(0);
        try {
            ArrayList<MedicalReport> list = reportDB.getAllReports();
            for (int i = 0; i < list.size(); i++) {
                MedicalReport r = list.get(i);
                tableModel.addRow(new Object[]{
                    r.getId(), r.getPatientId(), r.getDate(), r.getDiagnosis(), r.getNotes()
                });
            }
        } catch (Exception ex) {
            System.out.println("Load reports: " + ex.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnAdd) {
            handleAdd();
        } else if (src == btnDelete) {
            handleDelete();
        } else if (src == btnClear) {
            clearForm();
        }
    }

    private void handleAdd() {
        try {
            int patientId = Integer.parseInt(txtPatientId.getText().trim());
            if (patientDB.getPatientById(patientId) == null) {
                JOptionPane.showMessageDialog(this, "Patient #" + patientId + " does not exist.");
                return;
            }

            String date = txtDate.getText().trim();
            Validator.validateDate(date);
            String diag = txtDiagnosis.getText().trim();
            String notes = txtNotes.getText().trim();

            reportDB.addReport(new MedicalReport(0, patientId, date, diag, notes));
            loadTableData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Medical report saved.");
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Patient ID must be an integer.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving report: " + ex.getMessage());
        }
    }

    private void handleDelete() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a report to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete report #" + idStr + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                reportDB.deleteReport(Integer.parseInt(idStr));
                loadTableData();
                clearForm();
                JOptionPane.showMessageDialog(this, "Report deleted.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Delete error: " + ex.getMessage());
            }
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtPatientId.setText("");
        txtDate.setText("");
        txtDiagnosis.setText("");
        txtNotes.setText("");
    }

    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            txtPatientId.setText(tableModel.getValueAt(row, 1).toString());
            txtDate.setText(tableModel.getValueAt(row, 2).toString());
            txtDiagnosis.setText(tableModel.getValueAt(row, 3).toString());
            txtNotes.setText(tableModel.getValueAt(row, 4).toString());
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
