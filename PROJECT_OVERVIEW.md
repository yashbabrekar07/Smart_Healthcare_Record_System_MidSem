# Smart Healthcare Record System — Project Brief & Overview

**Course Project #23:** Object-Oriented Programming (C++ & Java)  
**Academic Level:** 2nd-Year B.Tech / Undergraduate  
**Domain:** Hospital & Patient Healthcare Record Management  

---

## 1. Executive Summary

The **Smart Healthcare Record System** is a software solution designed to automate clinical administration and eliminate manual, paper-based patient tracking. It manages the complete patient journey: patient registration (out-patients and admitted in-patients), doctor consultations, appointment booking, medical prescriptions, and diagnostic lab reports.

To satisfy the academic curriculum, the project is engineered in **two independent, fully functional editions**:
1. **C++ Console Edition (C++98):** Emphasizes system-level OOP, memory management (`new`/`delete`), generic class templates (`Store<T>`), virtual base inheritance (Diamond problem), binary file random access (`seekg`/`seekp`), friend mechanisms, operator overloading, and STL containers.
2. **Java Desktop Edition (JDK 8+):** Emphasizes enterprise desktop architecture using Java Swing/AWT, 5-package modular design, custom exception handling, Java Collection Framework (`ArrayList`, `Vector`, `HashSet`, `HashMap`), and complete database persistence via **MySQL & JDBC** (`Statement`, `PreparedStatement`, `CallableStatement`).

---

## 2. Core Entities & Class Hierarchy

Both editions share identical business entities, fields, and inheritance structures:

```
                            [Person] (Abstract Base Class)
                               │
               ┌───────────────┴───────────────┐
               ▼                               ▼
           [Patient]                        [Doctor]
               │                               │
        ┌──────┴──────┐                        │
        ▼             ▼                        ▼
   [InPatient]   [DoctorPatient]* ◄────────────┘ (Diamond Inheritance)
```
*\*`DoctorPatient` demonstrates multiple and hybrid inheritance, resolved via `virtual public Person` in C++.*

```
                         [HealthRecord] (Abstract Base Record)
                               │
               ┌───────────────┼───────────────┐
               ▼               ▼               ▼
         [Appointment]   [Prescription]  [MedicalReport]
```

### Entity Attributes:
* **`Person` (Abstract Base):** `id`, `name`, `age`, `gender`, `phone`, `address`. Pure virtual/abstract method: `getRole()`.
* **`Patient`:** Extends `Person` + `bloodGroup`. Implements sorting by name and ID equality checking.
* **`InPatient`:** Extends `Patient` + `roomNo`, `admitDate` (Multilevel inheritance).
* **`Doctor`:** Extends `Person` + `specialization`, `fee` (Hierarchical inheritance).
* **`DoctorPatient`:** Inherits from both `Patient` and `Doctor` (resolves Diamond Problem using virtual base class).
* **`Appointment`:** Extends `HealthRecord` + `doctorId`, `time`, `reason`, `status` (`Booked` / `Done` / `Cancelled`).
* **`Prescription`:** Extends `HealthRecord` + `doctorId`, `medicines`, `instructions`. Overloaded `+=` operator in C++.
* **`MedicalReport`:** Extends `HealthRecord` + `diagnosis`, `notes`. Stored in binary format in C++.

---

## 3. Key OOP Principles Implemented

### A. C++ OOP Highlights
* **Encapsulation & Protection:** Private and protected data members accessible only via public getters/setters; `this` pointer used to disambiguate parameters.
* **Inheritance Varieties:** Single, multilevel, hierarchical, multiple, and hybrid inheritance.
* **Virtual Base Class:** Solves the Diamond Problem in `DoctorPatient`, preventing duplicate `Person` subobjects.
* **Class & Function Templates:** Generic `Store<T>` manages heap memory lifecycles for any entity type; `showAll<T>()` acts as a generic collection viewer.
* **Friend Class & Functions:** Friend function `operator<<` for streamlined printing; `ReportPrinter` friend class directly accesses private fields of `MedicalReport`.
* **Operator Overloading:** `operator==` (ID comparison), `operator<` (sorting records by name/date), `operator+=` (appending medicines), `operator<<`.
* **Polymorphism & Casting:** Pure virtual functions (`getRole()`, `show()`, `toLine()`), upcasting (`Doctor*` $\rightarrow$ `Person*`), and safe downcasting via `dynamic_cast` with `NULL` validation.
* **File Handling:** Delimited text files for persistent storage (`data/*.txt`) and binary random access (`data/reports.dat`) using fixed-size POD struct `ReportData` with `seekp()` for in-place record updates.
* **STL Containers & Algorithms:** `std::vector`, `std::map<int, int>` for appointment frequency per doctor, `std::sort`, `std::find`, and iterators.

### B. Java OOP, GUI & JDBC Highlights
* **Package Architecture:** 5 distinct packages: `hms.model`, `hms.util`, `hms.db`, `hms.ui`, and `hms`.
* **Abstraction & Interfaces:** Interfaces `Displayable` and `Searchable` implemented by domain models alongside abstract classes `Person` and `HealthRecord`.
* **Exception Handling:** Custom validation throwing unchecked `IllegalArgumentException`; database persistence utilizing `try`, `multiple catch`, `nested try`, and `finally` blocks with `SQLException`.
* **Collection Framework:** `ArrayList<T>`, `Vector` (table models), `HashMap`, and `HashSet<Patient>` (overriding both `equals()` and `hashCode()` on patient ID to eliminate duplicate search results).
* **Full JDBC Persistence:**
  * `DriverManager`: Connection pooling and driver registration.
  * `Statement`: Schema generation (`CREATE DATABASE`, `CREATE TABLE IF NOT EXISTS`) and default seed data population.
  * `PreparedStatement`: SQL injection-safe parameterized queries for all CRUD operations.
  * `CallableStatement`: Executes MySQL stored procedure `doctor_appointment_count` with `IN` and `OUT` parameters.
* **Event-Driven GUI Delegation Model:**
  * Swing components: `JFrame`, `JPanel`, `JTabbedPane`, `JTable`, `JScrollPane`, `JButton`, `JTextField`, `JMenuBar`, `JMenu`, `JMenuItem`.
  * Layout Managers: `BorderLayout`, `GridLayout`, and `FlowLayout`.
  * Pure AWT window: `AboutWindow.java` built with `java.awt.Frame`, `Dialog`, `Button`, and `Label`.
  * Adheres strictly to second-year student design standards: clean top-level listener classes (`ActionListener`, `MouseListener`, `KeyListener`) checking `e.getSource()` without messy inner/anonymous classes.

---

## 4. Database & Storage Architecture

| Layer | Implementation in C++ | Implementation in Java |
| :--- | :--- | :--- |
| **Authentication** | `data/users.txt` (`admin\|admin123`) | MySQL `users` table via `PreparedStatement` |
| **Patients** | `data/patients.txt` | MySQL `patients` table (stores both out/in-patients) |
| **Doctors** | `data/doctors.txt` | MySQL `doctors` table |
| **Appointments** | `data/appointments.txt` | MySQL `appointments` table with Foreign Keys |
| **Prescriptions** | `data/prescriptions.txt` | MySQL `prescriptions` table with Foreign Keys |
| **Medical Reports**| `data/reports.dat` (Binary POD struct) | MySQL `reports` table with Foreign Keys |
| **Stored Procedure**| Aggregated via STL `std::map<int, int>` | MySQL Procedure `doctor_appointment_count` |
| **Offline Fallback**| In-memory store if data files are missing | Fallback credentials (`admin`/`admin123`) if MySQL is offline |

---

## 5. How to Run the Project

### Default Credentials
* **Username:** `admin`
* **Password:** `admin123`

### 1. Running the C++ Console Edition:
```powershell
# Compile with standard GCC (C++98)
g++ -std=c++98 -Wall main.cpp -o healthcare.exe

# Run the program
.\healthcare.exe
```

### 2. Running the Java Desktop GUI Edition:
Ensure MySQL is running (e.g. via XAMPP or MySQL Service), then execute:
```powershell
# Compile all source files
javac -cp ".;lib/mysql-connector-j-8.0.33.jar" -d bin src/hms/model/*.java src/hms/util/*.java src/hms/db/*.java src/hms/ui/*.java src/hms/*.java

# Launch Desktop GUI
java -cp "bin;lib/mysql-connector-j-8.0.33.jar" hms.Main
```

### 3. Running the Java Test Console (Scanner-based CLI):
```powershell
java -cp "bin;lib/mysql-connector-j-8.0.33.jar" hms.TestConsole
```

---

## 6. Project Directory Map

```text
Smart_Healthcare_Record_System_MidSem-main/
│
├── PROJECT_OVERVIEW.md            # Brief project overview & executive brief (this file)
├── README.md                      # Full project manual & setup guide
├── syllabus_map.md                # 27 C++ & 30 Java syllabus topics mapped to exact lines
├── viva_notes.md                  # 15 high-frequency viva defense Q&A
│
├── [C++ Console Edition]
│   ├── main.cpp                   # Menu navigation, console driver, and casting demo
│   ├── person.h                   # Person, Patient, InPatient, Doctor, DoctorPatient
│   ├── records.h                  # HealthRecord, Appointment, Prescription, MedicalReport, ReportPrinter
│   ├── store.h                    # Store<T> class template & showAll function template
│   ├── hospital.h                 # Hospital operations, STL map, text/binary file handling
│   ├── healthcare.exe             # Compiled C++ binary
│   └── data/                      # Persistent storage (.txt files and reports.dat binary)
│
└── [Java Desktop Edition]
    ├── src/hms/
    │   ├── Main.java              # Main desktop entry point
    │   ├── TestConsole.java       # Interactive test console using Scanner
    │   ├── model/                 # 10 entity classes & interfaces (Displayable, Searchable)
    │   ├── util/                  # Validator.java (throws IllegalArgumentException)
    │   ├── db/                    # DBConnection, DBSetup, PatientDB, DoctorDB, etc.
    │   └── ui/                    # MainFrame, LoginFrame, AboutWindow (AWT), 5 Swing panels
    ├── lib/
    │   └── mysql-connector-j-8.0.33.jar # Official MySQL JDBC Driver
    └── bin/                       # Compiled Java bytecode (.class files)
```

---

## 7. Key Viva Defense Takeaways

1. **Why two programs?** One in C++ strictly for C++ OOP syllabus requirements (templates, memory management, virtual base classes, binary files), and one in Java for Java OOP, GUI, and JDBC database requirements.
2. **Why Virtual Base Class in C++?** To solve the Diamond Problem where `DoctorPatient` inherits from both `Patient` and `Doctor`, ensuring only one shared instance of `Person` is created.
3. **Why `Store<T>` as a template?** Maximizes code reusability and compile-time type safety across all entity collections without repeating boilerplate code.
4. **Why `HashSet` with `equals()` and `hashCode()` in Java?** To combine search results by ID and Name without duplicate patient records.
5. **How are all 3 JDBC Statement types used?** `Statement` for DDL table creation; `PreparedStatement` for SQL injection-safe CRUD operations; `CallableStatement` for invoking the MySQL stored procedure with `IN`/`OUT` parameters.
