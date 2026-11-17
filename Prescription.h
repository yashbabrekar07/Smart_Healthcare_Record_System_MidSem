#ifndef PRESCRIPTION_H
#define PRESCRIPTION_H

#include <iostream>
#include <string>
using namespace std;

class Prescription {
private:
    int patientId;
    string medicine;
    string dosage;
    string duration;

public:
    Prescription(int p, string m, string d, string du) {
        patientId = p;
        medicine = m;
        dosage = d;
        duration = du;
    }

    void display() const {
        cout << "\nPatient ID : " << patientId
             << "\nMedicine   : " << medicine
             << "\nDosage     : " << dosage
             << "\nDuration   : " << duration << "\n";
        cout << "--------------------------------\n";
    }
};

#endif
