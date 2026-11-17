#ifndef APPOINTMENT_H
#define APPOINTMENT_H

#include <iostream>
#include <string>
using namespace std;

class Appointment {
private:
    int patientId;
    int doctorId;
    string date;
    string time;

public:
    Appointment(int p, int d, string dt, string t) {
        patientId = p;
        doctorId = d;
        date = dt;
        time = t;
    }

    void display() const {
        cout << "\nPatient ID : " << patientId
             << "\nDoctor ID  : " << doctorId
             << "\nDate       : " << date
             << "\nTime       : " << time << "\n";
        cout << "--------------------------------\n";
    }
};

#endif
