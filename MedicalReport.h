#ifndef MEDICALREPORT_H
#define MEDICALREPORT_H

#include <iostream>
#include <string>
using namespace std;

class MedicalReport {
private:
    int patientId;
    string reportName;
    string result;
    string date;

public:
    MedicalReport(int p, string rn, string r, string d) {
        patientId = p;
        reportName = rn;
        result = r;
        date = d;
    }

    void display() const {
        cout << "\nPatient ID   : " << patientId
             << "\nReport Name  : " << reportName
             << "\nResult       : " << result
             << "\nDate         : " << date << "\n";
        cout << "--------------------------------\n";
    }
};

#endif
