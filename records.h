#ifndef RECORDS_H
#define RECORDS_H

#include <iostream>
#include <string>
#include <cstdlib>
#include <cstring>
#include <iomanip>

using namespace std;

class MedicalReport;

// Abstract base class for healthcare records
class HealthRecord {
protected:
    int id;
    int patientId;
    string date;

public:
    HealthRecord() : id(0), patientId(0), date("") {}
    HealthRecord(int id, int patientId, string date) : id(id), patientId(patientId), date(date) {}
    virtual ~HealthRecord() {}

    int getId() const { return id; }
    int getPatientId() const { return patientId; }
    string getDate() const { return date; }

    virtual void show() const = 0;
    virtual string toLine() const = 0;

    // Operator overloading: compare by date for sorting patient history
    bool operator<(const HealthRecord& other) const { return this->date < other.date; }
    bool operator==(const HealthRecord& other) const { return this->id == other.id; }
};

// Appointment record
class Appointment : public HealthRecord {
private:
    int doctorId;
    string time;
    string reason;
    string status; // Booked, Done, Cancelled

public:
    static int nextAppointmentId;

    Appointment() : HealthRecord(), doctorId(0), time(""), reason(""), status("Booked") {}
    Appointment(int id, int patientId, int doctorId, string date, string time, string reason, string status)
        : HealthRecord(id, patientId, date), doctorId(doctorId), time(time), reason(reason), status(status) {
        if (id >= nextAppointmentId) nextAppointmentId = id + 1;
    }
    virtual ~Appointment() {}

    int getDoctorId() const { return doctorId; }
    string getStatus() const { return status; }
    void setStatus(string s) { this->status = s; }

    virtual void show() const {
        cout << left << setw(6) << id << setw(10) << patientId << setw(10) << doctorId
             << setw(12) << date << setw(8) << time << setw(20) << reason << setw(10) << status << "\n";
    }

    virtual string toLine() const {
        char b1[16], b2[16], b3[16];
        sprintf(b1, "%d", id); sprintf(b2, "%d", patientId); sprintf(b3, "%d", doctorId);
        return string(b1) + "|" + b2 + "|" + b3 + "|" + date + "|" + time + "|" + reason + "|" + status;
    }
};

// Prescription record
class Prescription : public HealthRecord {
private:
    int doctorId;
    string medicines;
    string instructions;

public:
    static int nextPrescriptionId;

    Prescription() : HealthRecord(), doctorId(0), medicines(""), instructions("") {}
    Prescription(int id, int patientId, int doctorId, string date, string medicines, string instructions)
        : HealthRecord(id, patientId, date), doctorId(doctorId), medicines(medicines), instructions(instructions) {
        if (id >= nextPrescriptionId) nextPrescriptionId = id + 1;
    }
    virtual ~Prescription() {}

    // Overloaded operator+= to append medicine to prescription
    Prescription& operator+=(const string& med) {
        if (!medicines.empty()) medicines += ", ";
        medicines += med;
        return *this;
    }

    virtual void show() const {
        cout << left << setw(6) << id << setw(8) << patientId << setw(8) << doctorId
             << setw(12) << date << setw(25) << medicines << setw(20) << instructions << "\n";
    }

    virtual string toLine() const {
        char b1[16], b2[16], b3[16];
        sprintf(b1, "%d", id); sprintf(b2, "%d", patientId); sprintf(b3, "%d", doctorId);
        return string(b1) + "|" + b2 + "|" + b3 + "|" + date + "|" + medicines + "|" + instructions;
    }
};

// Plain POD struct for binary file storage (char arrays only, no std::string, no virtual functions)
struct ReportData {
    int id;
    int patientId;
    char date[16];
    char diagnosis[64];
    char notes[128];
};

// MedicalReport record
class MedicalReport : public HealthRecord {
private:
    string diagnosis;
    string notes;

public:
    static int nextReportId;

    MedicalReport() : HealthRecord(), diagnosis(""), notes("") {}
    MedicalReport(int id, int patientId, string date, string diagnosis, string notes)
        : HealthRecord(id, patientId, date), diagnosis(diagnosis), notes(notes) {
        if (id >= nextReportId) nextReportId = id + 1;
    }
    virtual ~MedicalReport() {}

    string getDiagnosis() const { return diagnosis; }
    string getNotes() const { return notes; }
    void setDiagnosis(string d) { this->diagnosis = d; }
    void setNotes(string n) { this->notes = n; }

    virtual void show() const {
        cout << left << setw(6) << id << setw(10) << patientId << setw(12) << date
             << setw(25) << diagnosis << setw(25) << notes << "\n";
    }

    virtual string toLine() const {
        char b1[16], b2[16];
        sprintf(b1, "%d", id); sprintf(b2, "%d", patientId);
        return string(b1) + "|" + b2 + "|" + date + "|" + diagnosis + "|" + notes;
    }

    // Friend function operator<<
    friend ostream& operator<<(ostream& os, const MedicalReport& r) {
        os << "Report #" << r.id << " [Patient " << r.patientId << "] " << r.date << ": " << r.diagnosis;
        return os;
    }
    friend class ReportPrinter;
};

// Friend class accessing private members of MedicalReport
class ReportPrinter {
public:
    static void printFormatted(const MedicalReport& r) {
        cout << " [OFFICIAL REPORT #" << r.id << "] Patient: " << r.patientId
             << " | Date: " << r.date << "\n Diagnosis: " << r.diagnosis
             << "\n Notes    : " << r.notes << "\n";
    }
};

#endif
