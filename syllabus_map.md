# Syllabus Mapping Document

**Course:** Object-Oriented Programming (C++ & Java)  
**Project:** Smart Healthcare Record System (Project #23)  
**Authors:** Second-Year B.Tech Students  

This document provides a line-by-line mapping of every topic from the university course syllabus to its exact implementation in the project codebase.

---

## Part 1: Section A — C++ OOP Principles & Syllabus

| # | Syllabus Topic | Implemented Location & Code Reference | Explanation & Real Usage |
| :--- | :--- | :--- | :--- |
| **1** | **Fundamentals of OOPs & Comparison with C** | `person.h`, `records.h`, `store.h` | Replaces procedural C structs with classes combining state and behavior; eliminates memory leaks through destructors. |
| **2** | **Variables, Data Types & Operators** | `person.h` (L14–L20), `records.h` (L17–L20) | `int id`, `double fee`, `string name`, arithmetic and comparison operators used in record calculations. |
| **3** | **Control Structures & Iteration** | `main.cpp` (L123–L165), `hospital.h` (L450–L455) | `switch-case` menu system, `while` loops for user input validation, and `for` loops iterating collections. |
| **4** | **Functions & Modular Programming** | `hospital.h`, `records.h`, `store.h` | Modular design separating data structures, storage layers, and console presentation into separate headers. |
| **5** | **Basics of Console Input and Output** | `main.cpp`, `hospital.h` | Stream extraction (`cin >>`), stream insertion (`cout <<`), `getline()`, and formatted output with `iomanip` (`setw`, `left`). |
| **6** | **Dynamic Memory Allocation** | `store.h` (L38, L43, L49), `hospital.h` (L63) | Objects allocated on the heap via `new` (e.g. `new InPatient(...)`) and cleanly reclaimed using `delete`. |
| **7** | **Classes and Objects (Creation & Destruction)** | `person.h`, `records.h` | Objects instantiated across all entity classes; automated cleanup handled via destructors. |
| **8** | **Data Members & Member Functions** | `person.h` (L14–L20, L40–L55) | Encapsulated attributes with corresponding getter and setter member methods. |
| **9** | **Access Specifiers** | `person.h` (L13 `protected`, L22 `public`), `records.h` (L40 `private`) | `protected` used for base class inheritance; `private` for localized attributes; `public` for interface. |
| **10** | **`this` Pointer** | `person.h` (L48–L55), `records.h` (L58, L97) | Used in setters to resolve naming ambiguity between parameter names and data members (`this->id = id;`). |
| **11** | **Constructors & Destructors** | `person.h` (L25–L36), `records.h` (L22–L24) | Overloaded default and parameterized constructors using initializer lists; `virtual ~Person()` ensures clean cleanup. |
| **12** | **Static Class Members** | `person.h` (L20, L23, `personCount`, `nextId`), `hospital.h` (L21–L27) | Shared `personCount` tracking active individuals; auto-incrementing ID counters. |
| **13** | **Friend Class and Functions** | `records.h` (L150–L154, L158–L165) | Friend function `operator<<` for report display; `ReportPrinter` friend class accessing `MedicalReport` private fields. |
| **14** | **Function Overloading** | `hospital.h` (L67–L74) | `findPatient(int id)` and `findPatient(string name)` overloaded to provide dual lookup mechanisms. |
| **15** | **Operator Overloading** | `records.h` (L34, L87–L93), `person.h` (L99) | `operator==` (ID matching), `operator<` (sorting by name/date), `operator+=` (appending medicines to prescription), `operator<<`. |
| **16** | **Inheritance Types** | `person.h` (L62, L108, L128, L169) | **Single:** `Person` -> `Patient`; **Multilevel:** `Person` -> `Patient` -> `InPatient`; **Hierarchical:** `Person` -> `Patient` & `Doctor`; **Multiple & Hybrid:** `DoctorPatient`. |
| **17** | **Base and Derived Class Constructors** | `person.h` (L73–L76, L112–L115, L137–L143) | Initializer lists passing attributes upwards (e.g. `Patient(...) : Person(...)`). |
| **18** | **Downcasting and Upcasting** | `hospital.h` (L361–L371), `main.cpp` (L151–L157) | Upcasting `Doctor*` to `Person*`; safe downcasting back to `Doctor*` using `dynamic_cast` with `NULL` validation. |
| **19** | **Function Overriding & Polymorphism** | `person.h` (L38, L80, L150), `hospital.h` (L373–L388) | Virtual method `getRole()` dynamically dispatched through polymorphic `vector<Person*>`. |
| **20** | **Pure Virtual Functions & Abstract Classes** | `person.h` (L38 `virtual string getRole() = 0;`), `records.h` (L30–L31) | `Person` and `HealthRecord` act as abstract contracts that cannot be directly instantiated. |
| **21** | **Virtual Base Class** | `person.h` (L62, L128, L169) | `class Patient : virtual public Person` and `class Doctor : virtual public Person` resolve the Diamond Problem in `DoctorPatient`. |
| **22** | **C++ Class Hierarchy** | `person.h`, `records.h` | Clear hierarchical tree: `Person` -> `Patient` / `Doctor` -> `DoctorPatient`; `HealthRecord` -> `Appointment` / `Prescription` / `MedicalReport`. |
| **23** | **Text File Stream Handling** | `hospital.h` (L402–L468), `store.h` (L67–L75) | `ifstream` and `ofstream` storing pipe-delimited data in `data/patients.txt`, `data/doctors.txt`, etc. |
| **24** | **Binary File Handling (`seekg`/`seekp`)** | `store.h` (L12–L20), `hospital.h` (L470–L518) | Fixed-size POD struct `ReportData` written to `data/reports.dat`; in-place binary record updates using `seekp()`. |
| **25** | **Class Templates** | `store.h` (L27–L76) | `Store<T>` manages heterogeneous pointer collections, file saving, and cleanup across all entity types. |
| **26** | **Function Templates** | `store.h` (L79–L87) | `showAll(const Store<T>& store)` generic printer function accepting any compatible entity store. |
| **27** | **Standard Template Library (STL)** | `hospital.h` (L346–L359), `records.h` (L76) | `std::vector`, `std::map<int, int>` for appointment count aggregation, `std::sort`, `std::find`, and STL iterators. |

---

## Part 2: Section B — Java Fundamentals, OOP, GUI, JDBC & Collections

| # | Syllabus Topic | Implemented Location & Code Reference | Explanation & Real Usage |
| :--- | :--- | :--- | :--- |
| **1** | **Introduction, Features & JVM/JRE/JDK** | `src/hms/Main.java`, compiled to `bin/` bytecode | Bytecode execution running across OS platforms via JVM memory architecture. |
| **2** | **Data Types, Variables & Operators** | `src/hms/model/Person.java`, `Doctor.java` | Standard primitives (`int`, `double`, `boolean`) and reference types (`String`). |
| **3** | **Strings and Arrays** | `src/hms/model/Prescription.java`, `DoctorDB.java` | String manipulation, parsing, and Object arrays used in Swing `JTable` row representations. |
| **4** | **Control Flow Statements** | `src/hms/ui/LoginFrame.java`, `PatientDB.java` | Conditional checks, `if-else` verification, and iteration loops over result sets. |
| **5** | **Classes, Objects & Getters/Setters** | `src/hms/model/` (all 10 classes) | Domain entities with private state and explicit accessor/mutator methods. |
| **6** | **`Scanner` Class** | `src/hms/TestConsole.java` (L20, L36) | Interactive console test runner capturing keyboard input via `java.util.Scanner`. |
| **7** | **Constructors, `super` & `this` Keywords** | `src/hms/model/InPatient.java` (L13), `Patient.java` | `super(...)` passes data up inheritance hierarchies; `this.field = field` initializes member data. |
| **8** | **Modifiers (`static`, `final`, `private`, `public`)** | `src/hms/util/Validator.java`, `DBConnection.java` | `private static final` database constants; `static` utility methods; `private` encapsulated attributes. |
| **9** | **Encapsulation & Data Protection** | `src/hms/model/Person.java`, `src/hms/util/Validator.java` | Private data members coupled with strict input validation to guard against illegal states. |
| **10** | **Inheritance Types (`extends` & `implements`)** | `src/hms/model/Patient.java` | `class Patient extends Person implements Displayable, Searchable`. |
| **11** | **Compile-Time Polymorphism (Method Overloading)**| `src/hms/model/Patient.java` (L12, L16), `DoctorDB.java` | Overloaded constructors and database query methods. |
| **12** | **Run-Time Polymorphism (Method Overriding)** | `src/hms/model/Doctor.java` (L30), `InPatient.java` | `@Override public String getRole()` called via dynamic method dispatch. |
| **13** | **Static vs Dynamic Binding** | `src/hms/util/Validator.java` vs `Displayable` | Static calls resolved at compile-time (`Validator.validateAge`); virtual calls resolved dynamically. |
| **14** | **Interfaces & Abstract Classes** | `Displayable.java`, `Searchable.java`, `Person.java` | Interfaces declare contracts (`getDisplayText`); abstract classes enforce shared state and base methods. |
| **15** | **Memory Management (Heap vs Stack)** | Throughout all Java layers | Reference variables on stack pointing to dynamically allocated objects in heap garbage-collected by JVM. |
| **16** | **Packages** | `hms`, `hms.model`, `hms.util`, `hms.db`, `hms.ui` | Explicit modular packaging preventing namespace collisions and enforcing encapsulation boundaries. |
| **17** | **Exception Handling: `try`, `catch`, `finally`** | `src/hms/db/DBConnection.java` (L28–L44), `PatientDB.java` | Proper resource cleanup closing `ResultSet`, `Statement`, and `Connection` in `finally` blocks. |
| **18** | **Multiple Catch & Nested Try Blocks** | `src/hms/db/PatientDB.java` (L45–L75), `DBSetup.java` | Outer `try` catches database logic; inner nested `try` checks driver loading; distinct `SQLException` handling. |
| **19** | **`throw` and `throws` Keywords** | `src/hms/util/Validator.java` (L13, L20), `PatientDB.java` | `throw new IllegalArgumentException(...)`; methods declare checked exceptions via `throws SQLException`. |
| **20** | **GUI Components & Containers** | `src/hms/ui/MainFrame.java`, `PatientPanel.java` | Containers: `JFrame`, `JPanel`, `JTabbedPane`, `JScrollPane`; Components: `JButton`, `JTextField`, `JTable`. |
| **21** | **Layout Managers** | `src/hms/ui/LoginFrame.java`, `PatientPanel.java` | Multi-panel layouts using `BorderLayout`, `GridLayout`, and `FlowLayout` without hardcoded coordinates. |
| **22** | **AWT Components & Menus** | `src/hms/ui/AboutWindow.java`, `MainFrame.java` (L45) | Pure AWT dialog (`java.awt.Frame`, `Dialog`, `Button`, `Label`); Swing menu (`JMenuBar`, `JMenu`, `JMenuItem`). |
| **23** | **Event-Driven Programming & Delegation Model** | `src/hms/ui/LoginFrame.java`, `PatientPanel.java` | Components generate `ActionEvent`; top-level panel classes implement `ActionListener` handling `e.getSource()`. |
| **24** | **JDBC: Steps to Connect & `DriverManager`** | `src/hms/db/DBConnection.java` (L18–L25) | 1. Load driver (`Class.forName`); 2. Establish connection (`DriverManager.getConnection`); 3. Create Statement; 4. Execute; 5. Close. |
| **25** | **JDBC Statement** | `src/hms/db/DBSetup.java` (L27, L36), `PatientDB.java` (L20) | `Statement stmt = conn.createStatement()` used for DDL table creation and full table queries (`SELECT *`). |
| **26** | **JDBC PreparedStatement** | `src/hms/db/PatientDB.java` (L42), `LoginFrame.java` | Parametric SQL queries preventing injection for `INSERT`, `UPDATE`, `DELETE`, and login credentials. |
| **27** | **JDBC CallableStatement** | `src/hms/db/DoctorDB.java` (L90–L105) | Executes stored procedure `{CALL doctor_appointment_count(?, ?)}` with `IN` and `OUT` parameters. |
| **28** | **JDBC ResultSet & CRUD Operations** | `src/hms/db/PatientDB.java`, `AppointmentDB.java` | Full Create, Read, Update, Delete workflows populating objects from `ResultSet` records. |
| **29** | **Collections: `ArrayList` and `Vector`** | `src/hms/db/PatientDB.java`, `ui/PatientPanel.java` | Dynamic list management via `ArrayList<Patient>`; vector usage in Swing `DefaultTableModel` data vectors. |
| **30** | **Collections: `HashSet` & Hashing** | `src/hms/model/Patient.java` (L88–L96), `PatientDB.java` | `HashSet<Patient>` merges name and ID search results, relying on overridden `equals()` and `hashCode()` on patient ID. |

