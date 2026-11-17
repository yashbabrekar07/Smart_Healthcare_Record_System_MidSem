package hms.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import hms.db.DBConnection;

// User authentication window checking users table in database
public class LoginFrame extends JFrame implements ActionListener {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnCancel;

    public LoginFrame() {
        super("Login - Smart Healthcare Record System");
        setSize(400, 240);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        Font font = new Font("SansSerif", Font.PLAIN, 13);
        Font titleFont = new Font("SansSerif", Font.BOLD, 15);

        JLabel lblTitle = new JLabel("Staff Login Portal", JLabel.CENTER);
        lblTitle.setFont(titleFont);
        lblTitle.setForeground(new Color(0, 128, 128));
        add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField("admin");
        txtUsername.setFont(font);
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField("admin123");
        txtPassword.setFont(font);
        formPanel.add(txtPassword);

        JPanel centerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        centerContainer.setBackground(new Color(245, 245, 245));
        centerContainer.add(formPanel);
        add(centerContainer, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(new Color(245, 245, 245));

        btnLogin = new JButton("Login");
        btnLogin.setFont(font);
        btnLogin.setBackground(new Color(0, 128, 128));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener(this);

        btnCancel = new JButton("Exit");
        btnCancel.setFont(font);
        btnCancel.setBackground(new Color(0, 128, 128));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.addActionListener(this);

        btnPanel.add(btnLogin);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            handleLogin();
        } else if (e.getSource() == btnCancel) {
            System.exit(0);
        }
    }

    private void handleLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (validateCredentials(user, pass)) {
            dispose();
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. (Default: admin / admin123)", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateCredentials(String user, String pass) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception ex) {
            // Fallback default admin credentials if database connection is offline
            if ("admin".equals(user) && "admin123".equals(pass)) {
                return true;
            }
        } finally {
            DBConnection.close(rs, pstmt, conn);
        }
        return false;
    }
}

