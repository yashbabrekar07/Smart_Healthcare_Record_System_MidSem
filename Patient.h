#ifndef PATIENT_H
#define PATIENT_H

#include <iostream>
#include <string>
#include "Person.h"
using namespace std;

// Patient inherits common properties from Person
class Patient : public Person {
private:
    int age;
    string phone;
    string bloodGroup;

public:
    Patient(int i, string n, int a, string p, string bg)
        : Person(i, n) {
        age = a;
        phone = p;
        bloodGroup = bg;
    }

    int getAge() const { return age; }
    string getPhone() const { return phone; }
    string getBloodGroup() const { return bloodGroup; }

    void setAge(int a) { age = a; }
    void setPhone(string p) { phone = p; }
    void setBloodGroup(string bg) { bloodGroup = bg; }

    void display() const {
        cout << "\nPatient ID   : " << id
             << "\nName         : " << name
             << "\nAge          : " << age
             << "\nPhone        : " << phone
             << "\nBlood Group  : " << bloodGroup << "\n";
        cout << "--------------------------------\n";
    }
};

#endif
