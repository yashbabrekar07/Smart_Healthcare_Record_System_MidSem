package hms;

import hms.db.DBSetup;
import hms.ui.LoginFrame;

// Application entry point for Java Desktop Edition
public class Main {

    public static void main(String[] args) {
        System.out.println("Starting Smart Healthcare Record System (Java Desktop Edition)...");

        // Initialize database schema and default sample data
        DBSetup.initialize();

        // Launch login window
        LoginFrame login = new LoginFrame();
        login.setVisible(true);
    }
}

