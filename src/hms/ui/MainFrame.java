package hms.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import hms.db.DBConnection;

// Main desktop application window with MenuBar and CardLayout panel switcher
public class MainFrame extends JFrame implements ActionListener {

    private CardLayout cardLayout;
    private JPanel cardsPanel;
    private JLabel statusLabel;

    private JMenuItem itemPatients, itemDoctors, itemAppointments, itemPrescriptions, itemReports;
    private JMenuItem itemLogout, itemExit, itemAbout, itemDBStatus;

    private PatientPanel patientPanel;
    private DoctorPanel doctorPanel;
    private AppointmentPanel appointmentPanel;
    private PrescriptionPanel prescriptionPanel;
    private ReportPanel reportPanel;

    public MainFrame() {
        super("Smart Healthcare Record System - Desktop Edition");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Font menuFont = new Font("SansSerif", Font.PLAIN, 13);
        Font statusFont = new Font("SansSerif", Font.PLAIN, 12);

        // Menu Bar Setup
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu menuFile = new JMenu("File");
        menuFile.setFont(menuFont);
        itemLogout = new JMenuItem("Logout");
        itemExit = new JMenuItem("Exit");
        itemLogout.setFont(menuFont);
        itemExit.setFont(menuFont);
        itemLogout.addActionListener(this);
        itemExit.addActionListener(this);
        menuFile.add(itemLogout);
        menuFile.addSeparator();
        menuFile.add(itemExit);
        menuBar.add(menuFile);

        // Records Menu
        JMenu menuRecords = new JMenu("Records");
        menuRecords.setFont(menuFont);
        itemPatients = new JMenuItem("Patients Panel");
        itemDoctors = new JMenuItem("Doctors Panel");
        itemAppointments = new JMenuItem("Appointments Panel");
        itemPrescriptions = new JMenuItem("Prescriptions Panel");
        itemReports = new JMenuItem("Medical Reports Panel");

        JMenuItem[] recordItems = {itemPatients, itemDoctors, itemAppointments, itemPrescriptions, itemReports};
        for (int i = 0; i < recordItems.length; i++) {
            recordItems[i].setFont(menuFont);
            recordItems[i].addActionListener(this);
            menuRecords.add(recordItems[i]);
        }
        menuBar.add(menuRecords);

        // Help Menu
        JMenu menuHelp = new JMenu("Help");
        menuHelp.setFont(menuFont);
        itemAbout = new JMenuItem("About (Pure AWT)");
        itemDBStatus = new JMenuItem("Database Connection Status");
        itemAbout.setFont(menuFont);
        itemDBStatus.setFont(menuFont);
        itemAbout.addActionListener(this);
        itemDBStatus.addActionListener(this);
        menuHelp.add(itemAbout);
        menuHelp.add(itemDBStatus);
        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

        // Center CardLayout Container
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        patientPanel = new PatientPanel();
        doctorPanel = new DoctorPanel();
        appointmentPanel = new AppointmentPanel();
        prescriptionPanel = new PrescriptionPanel();
        reportPanel = new ReportPanel();

        cardsPanel.add(patientPanel, "PATIENTS");
        cardsPanel.add(doctorPanel, "DOCTORS");
        cardsPanel.add(appointmentPanel, "APPOINTMENTS");
        cardsPanel.add(prescriptionPanel, "PRESCRIPTIONS");
        cardsPanel.add(reportPanel, "REPORTS");

        add(cardsPanel, BorderLayout.CENTER);

        // Bottom Status Label
        statusLabel = new JLabel(" Ready | Connected Database: MySQL healthcare_db");
        statusLabel.setFont(statusFont);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        add(statusLabel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == itemPatients) {
            patientPanel.loadTableData();
            cardLayout.show(cardsPanel, "PATIENTS");
            statusLabel.setText(" Module: Patients Management (Encapsulation, InPatient Inheritance, Live Search)");
        } else if (src == itemDoctors) {
            doctorPanel.loadTableData();
            doctorPanel.refreshSpecializationsFilter();
            cardLayout.show(cardsPanel, "DOCTORS");
            statusLabel.setText(" Module: Doctors Management (CallableStatement Procedure & HashSet Filter)");
        } else if (src == itemAppointments) {
            appointmentPanel.refreshDoctorList();
            appointmentPanel.loadTableData();
            cardLayout.show(cardsPanel, "APPOINTMENTS");
            statusLabel.setText(" Module: Appointments Management (Vector ComboBox, HashMap Lookup & Date Filter)");
        } else if (src == itemPrescriptions) {
            prescriptionPanel.loadTableData();
            cardLayout.show(cardsPanel, "PRESCRIPTIONS");
            statusLabel.setText(" Module: Prescriptions Management");
        } else if (src == itemReports) {
            reportPanel.loadTableData();
            cardLayout.show(cardsPanel, "REPORTS");
            statusLabel.setText(" Module: Diagnostic Reports Management");
        } else if (src == itemAbout) {
            AboutWindow about = new AboutWindow();
            about.setVisible(true);
        } else if (src == itemDBStatus) {
            checkDBStatus();
        } else if (src == itemLogout) {
            dispose();
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        } else if (src == itemExit) {
            System.exit(0);
        }
    }

    private void checkDBStatus() {
        try {
            java.sql.Connection conn = DBConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                JOptionPane.showMessageDialog(this, "Connected successfully to MySQL Database at:\n" + DBConnection.URL,
                    "Database Status", JOptionPane.INFORMATION_MESSAGE);
                conn.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database Connection Notice:\n" + ex.getMessage()
                + "\n\nNote: If MySQL Server is not running locally, start MySQL on port 3306.",
                "Database Offline", JOptionPane.WARNING_MESSAGE);
        }
    }
}

