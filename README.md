# Smart Healthcare Record System

**Course Project #23:** Object-Oriented Programming (C++ & Java)  
**Academic Level:** 2nd Year B.Tech / Undergraduate  
**Topic:** Patient-record application supporting medical history, prescriptions, appointments, reports, and administrative management.

---

## 1. Project Overview

The **Smart Healthcare Record System** is an end-semester Object-Oriented Programming project implemented across two independent, fully functional editions:

1. **C++ Console Edition:** A high-performance, modular console system strictly adhering to C++98 standards, featuring class and function templates, dynamic memory allocation, virtual base classes, binary file random access (`seekg`/`seekp`), friend classes, operator overloading, and STL containers/algorithms.
2. **Java Desktop Edition:** A GUI-driven enterprise desktop application built with Java Swing and AWT, implementing modular packages, custom exception handling, Java Collection Framework (`ArrayList`, `Vector`, `HashSet`, `HashMap`), and full JDBC database persistence utilizing `Statement`, `PreparedStatement`, and `CallableStatement`.

Both editions share identical business entities, fields, validation rules, and default administrator credentials.

---

## 2. Default Login Credentials

| System Role | Username | Password |
| :--- | :--- | :--- |
| **System Administrator** | `admin` | `admin123` |

*(Note: In the Java GUI, the login dialog comes pre-filled with these credentials for quick evaluation during viva demonstrations. If MySQL is offline, the system safely falls back to standard local credentials).*

---

## 3. Project Directory Structure

```text
Smart_Healthcare_Record_System_MidSem-main/
│
├── README.md                      # Complete project documentation and run guide
├── syllabus_map.md                # Line-by-line mapping of all syllabus topics
├── viva_notes.md                  # 15 targeted Q&A for viva defense
│
├── [C++ Console Edition]
│   ├── main.cpp                   # Menu navigation, console driver, and casting demo
│   ├── person.h                   # Person (abstract), Patient, InPatient, Doctor, DoctorPatient
│   ├── records.h                  # HealthRecord (abstract), Appointment, Prescription, MedicalReport, ReportPrinter
│   ├── store.h                    # Store<T> class template and showAll function template
│   ├── hospital.h                 # Hospital management class, STL map, text/binary file handling
│   ├── healthcare.exe             # Compiled C++ binary
│   └── data/                      # Persistent storage directory
│       ├── users.txt              # Admin authentication credentials
│       ├── patients.txt           # Delimited patient and inpatient text records
│       ├── doctors.txt            # Doctor details and fee schedules
│       ├── appointments.txt       # Appointment logs and status
│       ├── prescriptions.txt      # Medication lists and dosage instructions
│       └── reports.dat            # Binary file with fixed-size records for random access
│
├── [Java Desktop Edition]
│   ├── src/
│   │   └── hms/
│   │       ├── Main.java          # Desktop app launcher (runs DBSetup and displays LoginFrame)
│   │       ├── TestConsole.java   # Scanner-based console test suite for models and DB
│   │       │
│   │       ├── model/             # Domain entity layer
│   │       │   ├── Displayable.java    # Interface for display formatting
│   │       │   ├── Searchable.java     # Interface for ID/name matching
│   │       │   ├── Person.java         # Abstract base entity with encapsulation
│   │       │   ├── Patient.java        # Patient model with equals() and hashCode()
│   │       │   ├── InPatient.java      # Single/hierarchical inheritance (roomNo, admitDate)
│   │       │   ├── Doctor.java         # Doctor entity with specialization and fee
│   │       │   ├── HealthRecord.java   # Abstract medical transaction record
│   │       │   ├── Appointment.java    # Appointment booking model
│   │       │   ├── Prescription.java   # Prescription and medication model
│   │       │   └── MedicalReport.java  # Diagnostic lab report model
│   │       │
│   │       ├── util/              # Utilities and validation
│   │       │   └── Validator.java      # Static validation methods throwing IllegalArgumentException
│   │       │
│   │       ├── db/                # Persistence & JDBC layer
│   │       │   ├── DBConnection.java   # Singleton connection manager (DriverManager)
│   │       │   ├── DBSetup.java        # Schema setup, DDL tables, seed data, stored procedure
│   │       │   ├── PatientDB.java      # CRUD using Statement, PreparedStatement & HashSet
│   │       │   ├── DoctorDB.java       # CRUD & CallableStatement (procedure execution)
│   │       │   ├── AppointmentDB.java  # Appointment transactions and status updates
│   │       │   ├── PrescriptionDB.java # Prescription generation and retrieval
│   │       │   └── ReportDB.java       # Medical report storage and filtering
│   │       │
│   │       └── ui/                # Presentation layer (Swing & AWT)
│   │           ├── LoginFrame.java     # Authenticated login window
│   │           ├── MainFrame.java      # Tabbed dashboard with JMenuBar
│   │           ├── AboutWindow.java    # Pure AWT window (Frame/Dialog) demonstrating delegation
│   │           ├── PatientPanel.java   # Patient registration, search, and JTable listing
│   │           ├── DoctorPanel.java    # Doctor management and stored procedure metrics
│   │           ├── AppointmentPanel.java # Appointment scheduling and status updates
│   │           ├── PrescriptionPanel.java# Prescription creation and patient medication history
│   │           └── ReportPanel.java    # Diagnostic reporting and patient history timeline
│   │
│   ├── lib/
│   │   └── mysql-connector-j-8.0.33.jar # Official MySQL JDBC Driver
│   └── bin/                       # Compiled Java bytecode (.class files)
```

---

## 4. Compilation & Execution Guide

### Part A: C++ Console Edition

The C++ edition complies with standard C++98 and produces zero warnings with GCC:

```powershell
# 1. Compile C++ sources
g++ -std=c++98 -Wall main.cpp -o healthcare.exe

# 2. Run the application
.\healthcare.exe
```

#### Key Menu Options in C++:
- **Option 1-5:** Manage Patients, Doctors, Appointments, Prescriptions, and Reports.
- **Option 6:** Search Patient by ID or Name (Demonstrates Function Overloading).
- **Option 7:** View Medical History Timeline (Demonstrates Polymorphic Record Traversal).
- **Option 8:** View Appointments per Doctor (Demonstrates STL `std::map<int, int>`).
- **Option 9:** Hospital Directory (Demonstrates Polymorphic `vector<Person*>`).
- **Option 10:** Casting Demonstration (Demonstrates Upcasting and Safe `dynamic_cast`).
- **Under Medical Reports (Option 5 -> 3):** In-place binary file update using `seekg` and `seekp`.

---

### Part B: Java Desktop Swing Edition

Ensure JDK 8 or higher is installed and present in your system PATH.

```powershell
# 1. Compile all Java source files into bin directory
javac -cp ".;lib/mysql-connector-j-8.0.33.jar" -d bin src/hms/model/*.java src/hms/util/*.java src/hms/db/*.java src/hms/ui/*.java src/hms/*.java

# 2. Launch the Java Desktop GUI
java -cp "bin;lib/mysql-connector-j-8.0.33.jar" hms.Main
```

#### Launching the Java Test Console:
If you want to run interactive unit verification through the command line (demonstrating `Scanner`, exceptions, collections, and model tests):
```powershell
java -cp "bin;lib/mysql-connector-j-8.0.33.jar" hms.TestConsole
```

---

## 5. Database Setup (MySQL)

- The Java application automatically runs `DBSetup.initialize()` on startup.
- It automatically creates the database `healthcare_db`, creates all required tables (`users`, `patients`, `doctors`, `appointments`, `prescriptions`, `reports`), seeds initial sample records, and creates the stored procedure `doctor_appointment_count`.
- **Database Configuration:**
  - URL: `jdbc:mysql://localhost:3306/healthcare_db`
  - User: `root`
  - Password: `""` (empty password by default)
  - *(To change credentials, edit [DBConnection.java](file:///c:/Users/ASUS%20VIVOBOOK%20PRO/OneDrive/Desktop/Smart_Healthcare_Record_System_MidSem-main/src/hms/db/DBConnection.java)).*
- **Offline Fallback:** If MySQL is not running on the host machine, the application will notify the console without crashing, allowing full demonstration of UI components and in-memory structures.

---

## 6. Key OOP Features & Design Principles

1. **Encapsulation & Data Protection:** All entity attributes are private/protected; access is mediated through public getter and setter methods with strict range/format validation.
2. **Inheritance Hierarchies:**
   - Single & Multilevel: `Person` -> `Patient` -> `InPatient`
   - Hierarchical: `Person` -> `Patient`, `Person` -> `Doctor`
   - Multiple / Hybrid (Diamond Inheritance): `Person` -> `Patient` & `Doctor` -> `DoctorPatient` resolved via `virtual` base inheritance in C++.
3. **Polymorphism:**
   - Compile-Time: Function overloading (`findPatient(int)` vs `findPatient(string)`) and constructor overloading.
   - Run-Time: Pure virtual methods (`getRole()`, `show()`, `toLine()`) and interface implementations (`Displayable`, `Searchable`) bound dynamically at runtime.
4. **Templates & STL (C++):**
   - Generic container `Store<T>` manages memory lifecycles (`new` / `delete`).
   - Standard Template Library `std::vector`, `std::map`, `std::sort`, `std::find`.
5. **JDBC Persistence (Java):**
   - `Statement`: Schema generation and table population.
   - `PreparedStatement`: SQL injection-safe parameterized queries for CRUD operations.
   - `CallableStatement`: Invocation of MySQL stored procedures with OUT parameters.
6. **GUI Event Delegation (Java):**
   - Pure separation of UI and business logic.
   - Adherence to second-year student design rules: strictly clean top-level listener classes without complex inner or anonymous classes.
