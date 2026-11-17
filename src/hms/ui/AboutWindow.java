package hms.ui;

import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

// About window constructed using pure AWT components
public class AboutWindow extends Frame implements ActionListener, WindowListener {

    private Button closeButton;

    public AboutWindow() {
        super("About - Smart Healthcare Record System");
        setSize(450, 280);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 5, 5));
        setBackground(new Color(240, 240, 240));

        Font titleFont = new Font("SansSerif", Font.BOLD, 15);
        Font textFont = new Font("SansSerif", Font.PLAIN, 13);

        Label lblTitle = new Label("Smart Healthcare Record System", Label.CENTER);
        lblTitle.setFont(titleFont);
        lblTitle.setForeground(new Color(0, 128, 128));

        Label lblCourse = new Label("Course: Object-Oriented Programming (OOP)", Label.CENTER);
        lblCourse.setFont(textFont);

        Label lblTopic = new Label("Project #23: Patient Records, History & Reports", Label.CENTER);
        lblTopic.setFont(textFont);

        Label lblTech = new Label("Technology: Java Swing, Pure AWT, JDBC, MySQL", Label.CENTER);
        lblTech.setFont(textFont);

        Label lblAuthor = new Label("Developed by: 2nd Year Computer Science", Label.CENTER);
        lblAuthor.setFont(textFont);

        closeButton = new Button("Close");
        closeButton.setFont(textFont);
        closeButton.addActionListener(this);

        Panel btnPanel = new Panel(new FlowLayout());
        btnPanel.add(closeButton);

        add(lblTitle);
        add(lblCourse);
        add(lblTopic);
        add(lblTech);
        add(lblAuthor);
        add(btnPanel);

        addWindowListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            dispose();
        }
    }

    public void windowClosing(WindowEvent e) {
        dispose();
    }

    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
}

