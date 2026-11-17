# SMART HEALTHCARE RECORD SYSTEM

## 1. Introduction
Healthcare organizations manage large amounts of patient information including personal details, medical history, prescriptions, appointments and medical reports. Managing these records manually can be difficult.

The Smart Healthcare Record System is a C++ based application designed to organize these records using Object-Oriented Programming concepts.

## 2. Problem Statement
A simple system is required to maintain healthcare records in an organized manner and provide basic operations for patients, doctors, appointments, prescriptions and reports.

## 3. Objectives
- Store patient information.
- Store doctor information.
- Manage appointments.
- Maintain prescription records.
- Maintain medical report records.
- Demonstrate OOP concepts using C++.
- Use collections to manage multiple records.

## 4. OOP Concepts Used

### Encapsulation
Data members of classes are private or protected. Public member functions are used to access or modify the data.

### Inheritance
The `Person` class contains common information. `Patient` and `Doctor` inherit from `Person`.

### Constructors
Constructors initialize objects when they are created.

### Collections
C++ `vector` is used to store multiple objects.

## 5. System Modules
1. Patient Management
2. Doctor Management
3. Appointment Management
4. Prescription Management
5. Medical Report Management

## 6. Basic Architecture

GUI / User Input
       |
       v
C++ Classes and Objects
       |
       v
Collections (`vector`)
       |
       v
Future Database Layer

## 7. Future Scope
- Graphical user interface
- Database connectivity
- Authentication
- Search and filtering
- Persistent patient records
- Medical history module
- Report/document upload
- Improved data validation

## 8. Conclusion
The current prototype demonstrates the basic design of a Smart Healthcare Record System using C++ Object-Oriented Programming. It provides a foundation that can be extended with GUI and database functionality in later stages.
