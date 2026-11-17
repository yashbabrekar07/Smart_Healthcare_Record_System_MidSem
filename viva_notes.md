# Viva Preparation Notes — 15 Key Questions & Answers

**Project:** Smart Healthcare Record System  
**Subject:** Object-Oriented Programming (C++ & Java)  
**Target Level:** 2nd-Year Engineering Viva Defense  

---

### Q1. What is the difference between Stack and Heap memory in C++ and Java?
* **Stack Memory:** Used for primitive local variables, method call frames, and reference pointers. Allocation and deallocation are fast, automatic, and strictly follow Last-In-First-Out (LIFO). When a function or block returns, its stack frame is instantly popped.
* **Heap Memory:** Used for dynamic object storage allocated at runtime (via `new` in both C++ and Java). In C++, heap memory must be explicitly deallocated using `delete` (or a destructor) to avoid memory leaks. In Java, objects stay in the heap until the Garbage Collector cleans them up when no active stack reference points to them.
* **Project Example:** In C++, our `Store<T>` manages heap objects (`new Patient(...)`), and its destructor calls `delete` for each pointer in the vector.

---

### Q2. What is the difference between `final`, `finally`, and `finalize` in Java?
* **`final` (Keyword):** A modifier. A `final` variable is a constant that cannot be reassigned; a `final` method cannot be overridden by subclasses; a `final` class cannot be extended.
* **`finally` (Block):** A block paired with `try-catch` that is guaranteed to execute whether an exception is thrown or caught (used for critical cleanup like closing database connections and result sets).
* **`finalize` (Method):** A protected method of `java.lang.Object` invoked by the garbage collector before an object is reclaimed (now deprecated in modern Java).
* **Project Example:** In `DBConnection.close()`, closing statements and connections is placed inside a `finally` block to prevent database resource leaks.

---

### Q3. Explain Static Binding vs Dynamic Binding with examples from your project.
* **Static Binding (Compile-Time Polymorphism):** Occurs during compilation. Method calls are resolved based on the type of reference and argument list. Examples include function overloading (`findPatient(int id)` vs `findPatient(string name)`) and private/static/final methods.
* **Dynamic Binding (Run-Time Polymorphism):** Occurs at runtime. The actual method executed is determined by the actual object being pointed to, not the reference type. In C++, this requires the `virtual` keyword; in Java, all non-static methods are virtual by default.
* **Project Example:** When traversing a `vector<Person*>`, calling `personPtr->getRole()` dynamically executes `Patient::getRole()` or `Doctor::getRole()` based on the actual object created.

---

### Q4. What is a Virtual Base Class and why is it needed?
* In multiple inheritance, when a derived class inherits from two intermediate classes that both share the same common base class, two separate copies of the base class data are inherited. This ambiguity is known as the **Diamond Problem**.
* By declaring the intermediate classes with `virtual public Base`, the compiler ensures only **one single shared instance** of the base class is created in the final derived object.
* **Project Example:** In `person.h`, `Patient` and `Doctor` both inherit `virtual public Person`. The class `DoctorPatient` inherits from both `Patient` and `Doctor`. Without `virtual`, `DoctorPatient` would have two ambiguous copies of `Person::name` and `Person::id`.

---

### Q5. Why is `Store<T>` implemented as a Class Template in C++?
* **Code Reusability & Type Safety:** Instead of writing separate storage classes for patients, doctors, appointments, prescriptions, and reports (which share identical storage logic like adding, removing by ID, file saving, and freeing memory), a single generic class `template <typename T> class Store` is written once.
* It eliminates repetitive boilerplate code while preserving compile-time type safety.
* **Project Example:** We instantiate `Store<Patient> patientStore;` and `Store<Doctor> doctorStore;`. The template ensures we cannot accidentally add a `Doctor*` into a `Store<Patient>`.

---

### Q6. Why did you override both `equals()` and `hashCode()` in `Patient.java`?
* In Java, any class used with hash-based collections (like `HashSet` or `HashMap`) must obey the **general contract**: if two objects are equal according to `equals()`, they **must** return the identical `hashCode()`.
* Default `equals()` compares memory addresses (`==`). By overriding `equals()` to check `this.id == other.id` and returning `this.id` in `hashCode()`, two distinct `Patient` objects with ID 101 are recognized as the same entity.
* **Project Example:** In `PatientDB.searchPatients()`, we search by name and search by ID, adding all results into a `HashSet<Patient>`. The `HashSet` automatically filters out duplicates based on ID.

---

### Q7. What are the 5 standard steps to connect to a database using JDBC?
1. **Load Driver Class:** Register driver with JVM using `Class.forName("com.mysql.cj.jdbc.Driver")`.
2. **Establish Connection:** Obtain a active connection using `DriverManager.getConnection(url, user, password)`.
3. **Create Statement:** Create a SQL carrier (`Statement`, `PreparedStatement`, or `CallableStatement`).
4. **Execute Query:** Call `executeQuery()` for `SELECT` (returns `ResultSet`) or `executeUpdate()` for `INSERT`/`UPDATE`/`DELETE` (returns int count).
5. **Close Resources:** Cleanly close `ResultSet`, `Statement`, and `Connection` in a `finally` block to release database resources.

---

### Q8. What is the difference between `Statement`, `PreparedStatement`, and `CallableStatement`?
* **`Statement`:** Used for executing static SQL queries without parameters. Compiled by database on every execution. (Used in `DBSetup.java` to run DDL `CREATE TABLE` and `SELECT *`).
* **`PreparedStatement`:** Used for parameterized SQL queries with `?` placeholders. Pre-compiled once by database for performance, and automatically escapes input to prevent **SQL Injection attacks**. (Used in `PatientDB.java` for all insert, update, delete, and login verification).
* **`CallableStatement`:** Used to invoke stored procedures and functions defined inside the database engine. Can handle `IN` and `OUT` parameters. (Used in `DoctorDB.java` to call `{CALL doctor_appointment_count(?, ?)}`).

---

### Q9. How does the Event Delegation Model work in your Java GUI?
* The **Delegation Event Model** defines three components:
  1. **Event Source:** The GUI component where the user performs an action (e.g. `JButton btnLogin`).
  2. **Event Object:** An instance encapsulating the action (e.g. `ActionEvent e`).
  3. **Event Listener:** An interface implemented to receive and handle the event (`ActionListener`).
* **Implementation Standard:** Rather than using messy anonymous or inner classes, our panels are top-level classes implementing `ActionListener`. The component registers the panel (`btn.addActionListener(this);`). In `actionPerformed(ActionEvent e)`, we identify the source via `e.getSource() == btnLogin` and dispatch the appropriate method.

---

### Q10. Why is `Person` an Abstract Class with a Pure Virtual Function in C++?
* A hospital contains specific real-world roles (patients, doctors, administrators), but a generic, undefined "person" should never exist as a standalone entity in the database.
* By declaring `virtual string getRole() const = 0;`, `Person` becomes an **Abstract Class**, making direct instantiation (`Person p;`) illegal by the compiler.
* This forces all derived classes (`Patient`, `Doctor`) to provide concrete implementations of `getRole()`, establishing an OOP architectural contract.

---

### Q11. What is the difference between Upcasting and Downcasting?
* **Upcasting:** Converting a derived-class pointer/reference to a base-class pointer (e.g. `Doctor*` to `Person*`). It is **always safe** and implicit because an `is-a` relationship holds true (a Doctor is a Person).
* **Downcasting:** Converting a base-class pointer back down to a derived-class pointer (e.g. `Person*` to `Doctor*`). It is potentially unsafe because the base pointer might actually point to a `Patient`, not a `Doctor`.
* **Safe Downcasting:** We use `dynamic_cast<Doctor*>(personPtr)`. If the object is not actually a `Doctor`, `dynamic_cast` safely returns `NULL`, allowing us to perform an explicit null check before accessing `Doctor`-specific methods like `getSpecialization()`.

---

### Q12. How does Binary File Handling (`seekg`/`seekp`) differ from Text File Handling?
* **Text File Handling (`ifstream`/`ofstream`):** Stores data as formatted ASCII strings separated by delimiters (e.g. `id|name|age`). It requires sequential reading, string tokenization, and parsing using `atoi()`/`getline()`. To update a single record, the entire file must be rewritten.
* **Binary File Handling (`ios::binary`):** Dumps exact byte representations of fixed-size POD (Plain Old Data) structs directly to disk using `read()` and `write()`.
* **Random Access:** Using `seekg(offset, ios::beg)` (get pointer) and `seekp(offset, ios::beg)` (put pointer), we jump directly to record position `index * sizeof(ReportData)` and update a medical report **in-place** in `reports.dat` without rewriting the rest of the file.

---

### Q13. What is the difference between Checked and Unchecked Exceptions in Java?
* **Checked Exceptions:** Inherit directly from `java.lang.Exception`. The compiler forces the programmer to handle them using `try-catch` or declare them using `throws`. They represent recoverable external failure conditions (e.g. `SQLException` when a database server is unreachable).
* **Unchecked Exceptions:** Inherit from `java.lang.RuntimeException`. The compiler does not force explicit declaration. They represent programming bugs or invalid user inputs (e.g. `IllegalArgumentException`).
* **Project Example:** `Validator.validateAge()` throws unchecked `IllegalArgumentException` when age < 0 or > 120; `PatientDB.insertPatient()` declares `throws SQLException`.

---

### Q14. In C++, why is `vector<Person*>` (pointers) used instead of `vector<Person>` for the hospital directory?
* **Object Slicing:** If you store base class values in a `vector<Person>`, assigning a derived `Patient` or `Doctor` object causes "slicing" — only the base `Person` slice is copied, and all derived members (`bloodGroup`, `specialization`) and virtual method tables are lost.
* **Polymorphism Requirement:** In C++, dynamic binding via virtual functions only works through **pointers** or **references**. Storing `Person*` allows the vector to hold pointers to `Patient`, `InPatient`, `Doctor`, and `DoctorPatient` while preserving runtime polymorphism when calling `dir[i]->getRole()`.

---

### Q15. How does AWT differ from Swing in Java, and where did you use AWT?
* **AWT (Abstract Window Toolkit):** Java's original GUI toolkit (`java.awt`). Components are **heavyweight** because they are rendered directly by native operating system peer controls.
* **Swing:** Built on top of AWT (`javax.swing`). Components are **lightweight** and written entirely in pure Java, offering richer components like `JTable`, `JTabbedPane`, and consistent cross-platform look-and-feel.
* **Project Example:** We used Swing for the main dashboard (`MainFrame`, `PatientPanel`, etc.) and built `AboutWindow.java` using **pure AWT** (`java.awt.Frame`, `java.awt.Dialog`, `java.awt.Button`, `java.awt.Label`, `java.awt.FlowLayout`) to satisfy the AWT syllabus requirement.

