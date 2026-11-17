# Smart Healthcare Record System
**Course:** Object-Oriented Programming | **Stage:** Mid-Sem Progress Review
**Tech:** Java, Swing (GUI), JDBC, SQLite

---

## 1. Problem Statement
Build a patient-record application that manages medical history, prescriptions, appointments and reports, using core OOP concepts: encapsulation, inheritance, collections, JDBC and a GUI.

---

## 2. Workflow (How the System Works)

### 2.1 Layered Architecture

```
  GUI (Swing panels)
        |   user actions (Add / Update / Delete / View)
        v
  Service layer  (HospitalService - HashMap cache + coordination)
        |
        v
  DAO layer  (PatientDAO, DoctorDAO, MedicalHistoryDAO)
        |   PreparedStatement SQL
        v
  JDBC  ->  SQLite database (healthcare.db)
```

The GUI never writes SQL. Every request flows top to bottom, and results come back as Java objects (`Patient`, `Doctor`, `MedicalHistory`) stored in `List` / `Map` collections.

### 2.2 Step-by-Step Execution Flow
1. `Main` starts the app and calls `DatabaseConnection.initializeSchema()`, which creates all tables if they do not exist.
2. `MainFrame` opens a tabbed window (Patients, Doctors, Medical History, plus Phase 2 tabs).
3. User fills a form in a panel and clicks **Add / Update / Delete**.
4. Panel validates input and builds a model object (e.g. `new Patient(...)`).
5. Panel calls `HospitalService`, which calls the matching DAO.
6. DAO runs a `PreparedStatement` on the SQLite connection.
7. Service refreshes its `HashMap<Integer, Patient>` cache and returns a `List` to the panel.
8. Panel reloads its `JTable` with the updated list.

### 2.3 Class Design (OOP Concepts)

| Concept | Where used |
|---|---|
| Encapsulation | All model classes: private fields, getters/setters; `Person.setAge()` validates input |
| Inheritance | `Patient` and `Doctor` extend abstract class `Person` |
| Polymorphism | Abstract `displayInfo()` overridden separately in `Patient` and `Doctor` |
| Collections | `List<Patient>`, `List<Doctor>`, `List<MedicalHistory>`, `HashMap<Integer, Patient>` cache |
| JDBC | `DatabaseConnection` + three DAOs using `PreparedStatement` |
| GUI | `JFrame`, `JTabbedPane`, `JTable`, form panels |

### 2.4 Database Schema
Tables: `patients`, `doctors`, `medical_history`, `prescriptions`, `appointments`, `reports`.
`patient_id` is a foreign key (with `ON DELETE CASCADE`) in all dependent tables.

---

## 3. Work Completed (Mid-Sem)

| Module | Model | DB Table | DAO | GUI |
|---|---|---|---|---|
| Patients | Done | Done | Done | Done |
| Doctors | Done | Done | Done | Done |
| Medical History | Done | Done | Done | Done |
| Prescriptions | Done | Done | Pending | Placeholder tab |
| Appointments | Done | Done | Pending | Placeholder tab |
| Reports | Done | Done | Pending | Placeholder tab |

Also completed: Maven `pom.xml`, layered package structure, service-layer cache, and a tested end-to-end flow (add, view, update, history entry).

---

## 4. Pending Work (End-Sem)

### 4.1 Prescriptions module
- [ ] `PrescriptionDAO` (add, update, delete, list by patient)
- [ ] `PrescriptionPanel` with patient dropdown and doctor dropdown
- [ ] Show prescriptions inside the patient record

### 4.2 Appointments module
- [ ] `AppointmentDAO` (book, reschedule, cancel, list by doctor / patient)
- [ ] `AppointmentPanel` with date, time and status (Scheduled / Completed / Cancelled)
- [ ] Prevent double-booking of the same doctor at the same date and time

### 4.3 Reports module
- [ ] `ReportDAO` (add, delete, list by patient)
- [ ] `ReportPanel` for report type, date and summary
- [ ] Optional: attach a report file path

### 4.4 Integration and improvements
- [ ] Load prescriptions, appointments and reports into `Patient` collections (full patient record view)
- [ ] Search and filter patients (by name, ID, blood group)
- [ ] Input validation: phone number format, date format (YYYY-MM-DD)
- [ ] Export a patient's complete record as a printable text/PDF report
- [ ] Replace `ComingSoonPanel` tabs with the real panels in `MainFrame`

### 4.5 Testing and documentation
- [ ] Test every CRUD operation for the three new modules
- [ ] Test cascade delete (deleting a patient removes related records)
- [ ] UML class diagram and ER diagram for the final report
- [ ] Final project report and viva preparation

---

## 5. Suggested Timeline

| Week | Task |
|---|---|
| 1 | Prescriptions DAO + panel |
| 2 | Appointments DAO + panel (with clash check) |
| 3 | Reports DAO + panel, patient full-record view |
| 4 | Search/filter, validation, export |
| 5 | Testing, diagrams, final report |

---

## 6. How to Run
```bash
# Maven
mvn clean package
java -jar target/SmartHealthcareRecordSystem.jar

# Plain javac (macOS/Linux)
javac -cp lib/sqlite-jdbc-3.36.0.3.jar -d out $(find src -name "*.java")
java -cp "out:lib/sqlite-jdbc-3.36.0.3.jar" com.healthcare.Main
```
The database file `healthcare.db` is created automatically on first run.