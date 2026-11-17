package hms;

import java.util.ArrayList;
import java.util.Scanner;
import hms.db.DBConnection;
import hms.db.DBSetup;
import hms.db.PatientDB;
import hms.model.Appointment;
import hms.model.Displayable;
import hms.model.Doctor;
import hms.model.InPatient;
import hms.model.Patient;
import hms.model.Person;
import hms.util.Validator;

// Test console using Scanner to test models, validation and database connectivity
public class TestConsole {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==================================================");
        System.out.println("     SMART HEALTHCARE RECORD SYSTEM - TEST CONSOLE");
        System.out.println("==================================================");

        int choice = -1;
        while (choice != 0) {
            System.out.println("\n--- TEST MENU ---");
            System.out.println("1. Test OOP Models (Inheritance, Polymorphism & Interfaces)");
            System.out.println("2. Test Validator (Exceptions)");
            System.out.println("3. Test Database Connection & DBSetup");
            System.out.println("4. List Patients from Database (JDBC Statement)");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.nextLine();
                choice = -1;
            }

            switch (choice) {
                case 1:
                    testModels();
                    break;
                case 2:
                    testValidator();
                    break;
                case 3:
                    testDBConnection();
                    break;
                case 4:
                    testListPatients();
                    break;
                case 0:
                    System.out.println("Test console closed.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        scanner.close();
    }

    private static void testModels() {
        System.out.println("\n[1] Testing Model Inheritance & Polymorphism:");
        // Upcast to Person reference (Dynamic Binding)
        Person p1 = new Patient(101, "Alice Green", 29, "F", "9876543210", "12 Main St", "A+");
        Person p2 = new InPatient(102, "Bob White", 45, "M", "9811223344", "44 Oak Ave", "O-", 204, "2026-11-01");
        Person p3 = new Doctor(201, "Dr. John Watson", 52, "M", "9822334455", "Baker Street", "Cardiology", 650.0);

        System.out.println("p1.getRole(): " + p1.getRole() + " | Name: " + p1.getName());
        System.out.println("p2.getRole(): " + p2.getRole() + " | Name: " + p2.getName());
        System.out.println("p3.getRole(): " + p3.getRole() + " | Name: " + p3.getName());

        System.out.println("\n[2] Testing Displayable Interface:");
        Displayable d1 = (Displayable) p1;
        Displayable d2 = (Displayable) p2;
        Displayable d3 = (Displayable) p3;
        System.out.println(d1.getSummary());
        System.out.println(d2.getSummary());
        System.out.println(d3.getSummary());

        System.out.println("\n[3] Testing Appointment Record:");
        Appointment app = new Appointment(301, 101, 201, "2026-11-10", "10:30", "Cardiac evaluation", "Booked");
        System.out.println(app.getSummary());

        System.out.println("\n[4] Testing Patient equals() and hashCode() on ID:");
        Patient copy = new Patient(101, "Alice Another", 30, "F", "9999999999", "Somewhere", "A+");
        System.out.println("p1.equals(copy) [both id=101]: " + p1.equals(copy));
        System.out.println("p1.hashCode(): " + p1.hashCode() + " == copy.hashCode(): " + copy.hashCode());
    }

    private static void testValidator() {
        System.out.println("\nTesting Validator static methods:");

        // Test valid
        try {
            Validator.validateName("John Doe");
            Validator.validateAge(30);
            Validator.validatePhone("9876543210");
            Validator.validateDate("2026-11-10");
            Validator.validateBloodGroup("O+");
            System.out.println("Valid inputs passed validation successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Unexpected failure: " + e.getMessage());
        }

        // Test invalid age
        try {
            Validator.validateAge(150);
            System.out.println("Failed: Invalid age was accepted.");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected exception for age 150: " + e.getMessage());
        }

        // Test invalid phone
        try {
            Validator.validatePhone("12345");
            System.out.println("Failed: Invalid phone was accepted.");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected exception for phone '12345': " + e.getMessage());
        }

        // Test invalid date
        try {
            Validator.validateDate("10-11-2026");
            System.out.println("Failed: Invalid date format was accepted.");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected exception for date '10-11-2026': " + e.getMessage());
        }
    }

    private static void testDBConnection() {
        System.out.println("\nAttempting MySQL connection to: " + DBConnection.URL);
        try {
            DBSetup.initialize();
            System.out.println("Database setup completed successfully.");
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            System.out.println("Note: Ensure MySQL server is running on localhost:3306 and mysql-connector-j.jar is in classpath.");
        }
    }

    private static void testListPatients() {
        System.out.println("\nQuerying patients from MySQL via PatientDB.getAllPatients()...");
        PatientDB db = new PatientDB();
        try {
            ArrayList<Patient> list = db.getAllPatients();
            if (list.isEmpty()) {
                System.out.println("No patients found in database.");
            } else {
                System.out.println("Total Patients: " + list.size());
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(" - " + list.get(i).getSummary());
                }
            }
        } catch (Exception e) {
            System.out.println("Query failed: " + e.getMessage());
            System.out.println("Note: Ensure MySQL server is running on localhost:3306 with credentials root / empty password.");
        }
    }
}

