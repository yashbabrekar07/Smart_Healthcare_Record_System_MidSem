#ifndef DOCTOR_H
#define DOCTOR_H

#include <iostream>
#include <string>
#include "Person.h"
using namespace std;

// Doctor also inherits from Person
class Doctor : public Person {
private:
    string specialization;

public:
    Doctor(int i, string n, string s)
        : Person(i, n) {
        specialization = s;
    }

    string getSpecialization() const {
        return specialization;
    }

    void setSpecialization(string s) {
        specialization = s;
    }

    void display() const {
        cout << "\nDoctor ID        : " << id
             << "\nDoctor Name      : " << name
             << "\nSpecialization   : " << specialization << "\n";
        cout << "--------------------------------\n";
    }
};

#endif
