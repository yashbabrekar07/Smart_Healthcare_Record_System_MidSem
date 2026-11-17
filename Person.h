#ifndef PERSON_H
#define PERSON_H

#include <cstdlib>
#include <iomanip>
#include <iostream>
#include <string>

using namespace std;

// Abstract base class representing an individual in the hospital
class Person {
protected:
  int id;
  string name;
  int age;
  string gender;
  string phone;
  string address;
  static int personCount;

public:
  static int nextId;

  Person() : id(0), name(""), age(0), gender(""), phone(""), address("") {
    personCount++;
  }
  Person(int id, string name, int age, string gender, string phone,
         string address)
      : id(id), name(name), age(age), gender(gender), phone(phone),
        address(address) {
    personCount++;
    if (id >= nextId)
      nextId = id + 1;
  }
  virtual ~Person() { personCount--; }

  virtual string getRole() const = 0; // Pure virtual function

  int getId() const { return id; }
  string getName() const { return name; }
  int getAge() const { return age; }
  string getGender() const { return gender; }
  string getPhone() const { return phone; }
  string getAddress() const { return address; }

  // Setters using this pointer
  void setId(int id) { this->id = id; }
  void setName(string name) { this->name = name; }
  void setAge(int age) { this->age = age; }
  void setGender(string gender) { this->gender = gender; }
  void setPhone(string phone) { this->phone = phone; }
  void setAddress(string address) { this->address = address; }

  static int getCount() { return personCount; }
  bool operator==(const Person &other) const { return this->id == other.id; }
  bool operator<(const Person &other) const { return this->name < other.name; }

  virtual void show() const {
    cout << left << setw(6) << id << setw(18) << name << setw(6) << age
         << setw(8) << gender << setw(13) << phone << setw(15) << address;
  }
};

// Patient virtually inherits Person to support hybrid inheritance in
// DoctorPatient
class Patient : virtual public Person {
protected:
  string bloodGroup;

public:
  static int nextPatientId;

  Patient() : Person(), bloodGroup("") {}
  Patient(int id, string name, int age, string gender, string phone,
          string address, string bloodGroup)
      : Person(id, name, age, gender, phone, address), bloodGroup(bloodGroup) {
    if (id >= nextPatientId)
      nextPatientId = id + 1;
  }
  virtual ~Patient() {}

  string getBloodGroup() const { return bloodGroup; }
  void setBloodGroup(string bloodGroup) { this->bloodGroup = bloodGroup; }
  virtual string getRole() const { return "Patient"; }

  virtual void show() const {
    Person::show();
    cout << setw(8) << bloodGroup << "\n";
  }

  virtual string toLine() const {
    char b1[16], b2[16];
    sprintf(b1, "%d", id);
    sprintf(b2, "%d", age);
    return string(b1) + "|" + name + "|" + b2 + "|" + gender + "|" + phone +
           "|" + address + "|" + bloodGroup;
  }
};

// InPatient extends Patient (Multilevel inheritance)
class InPatient : public Patient {
private:
  int roomNo;
  string admitDate;

public:
  InPatient() : Patient(), roomNo(0), admitDate("") {}
  InPatient(int id, string name, int age, string gender, string phone,
            string address, string bloodGroup, int roomNo, string admitDate)
      : Person(id, name, age, gender, phone, address),
        Patient(id, name, age, gender, phone, address, bloodGroup),
        roomNo(roomNo), admitDate(admitDate) {}
  virtual ~InPatient() {}

  int getRoomNo() const { return roomNo; }
  string getAdmitDate() const { return admitDate; }
  virtual string getRole() const { return "InPatient"; }

  virtual void show() const {
    Person::show();
    cout << setw(8) << bloodGroup << " Room: " << roomNo << " (" << admitDate
         << ")\n";
  }
};

// Doctor virtually inherits Person
class Doctor : virtual public Person {
protected:
  string specialization;
  double fee;

public:
  static int nextDoctorId;

  Doctor() : Person(), specialization(""), fee(0.0) {}
  Doctor(int id, string name, int age, string gender, string phone,
         string address, string specialization, double fee)
      : Person(id, name, age, gender, phone, address),
        specialization(specialization), fee(fee) {
    if (id >= nextDoctorId)
      nextDoctorId = id + 1;
  }
  virtual ~Doctor() {}

  string getSpecialization() const { return specialization; }
  double getFee() const { return fee; }
  void setSpecialization(string s) { this->specialization = s; }
  void setFee(double f) { this->fee = f; }
  virtual string getRole() const { return "Doctor"; }

  virtual void show() const {
    Person::show();
    cout << setw(18) << specialization << " Fee: " << fee << "\n";
  }

  virtual string toLine() const {
    char b1[16], b2[16], b3[16];
    sprintf(b1, "%d", id);
    sprintf(b2, "%d", age);
    sprintf(b3, "%.2f", fee);
    return string(b1) + "|" + name + "|" + b2 + "|" + gender + "|" + phone +
           "|" + address + "|" + specialization + "|" + b3;
  }
};

// DoctorPatient demonstrates multiple and hybrid inheritance with virtual base
// Person
class DoctorPatient : public Patient, public Doctor {
public:
  DoctorPatient() : Person(), Patient(), Doctor() {}
  DoctorPatient(int id, string name, int age, string gender, string phone,
                string address, string bloodGroup, string specialization,
                double fee)
      : Person(id, name, age, gender, phone, address),
        Patient(id, name, age, gender, phone, address, bloodGroup),
        Doctor(id, name, age, gender, phone, address, specialization, fee) {}
  virtual ~DoctorPatient() {}

  virtual string getRole() const { return "DoctorPatient"; }
  virtual void show() const {
    Person::show();
    cout << setw(8) << bloodGroup << " " << setw(15) << specialization
         << " [Dual Role]\n";
  }
};

#endif
