#ifndef HOSPITAL_H
#define HOSPITAL_H

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <map>
#include <algorithm>
#include <iomanip>
#include <cstdlib>
#include <cstring>

#include "person.h"
#include "records.h"
#include "store.h"

using namespace std;

// Static member definitions
int Person::personCount = 0;
int Person::nextId = 101;
int Patient::nextPatientId = 101;
int Doctor::nextDoctorId = 201;
int Appointment::nextAppointmentId = 301;
int Prescription::nextPrescriptionId = 401;
int MedicalReport::nextReportId = 501;

// Comparison helpers for std::sort using operator<
inline bool comparePatientsByName(const Patient* a, const Patient* b) { return (*a) < (*b); }
inline bool compareRecordsByDate(const HealthRecord* a, const HealthRecord* b) { return (*a) < (*b); }

// Validation helpers
inline bool isValidName(const string& s) { return !s.empty(); }
inline bool isValidAge(int a) { return a >= 0 && a <= 120; }
inline bool isValidPhone(const string& p) {
    if (p.length() != 10) return false;
    for (size_t i = 0; i < p.length(); ++i) {
        if (p[i] < '0' || p[i] > '9') return false;
    }
    return true;
}
inline bool isValidDate(const string& d) {
    if (d.length() != 10 || d[4] != '-' || d[7] != '-') return false;
    for (int i = 0; i < 10; ++i) {
        if (i != 4 && i != 7 && (d[i] < '0' || d[i] > '9')) return false;
    }
    return true;
}

class Hospital {
private:
    Store<Patient> patientStore;
    Store<Doctor> doctorStore;
    Store<Appointment> appointmentStore;
    Store<Prescription> prescriptionStore;
    Store<MedicalReport> reportStore;
    vector<DoctorPatient*> dualRoleDoctors;

public:
    Hospital() {}
    ~Hospital() {
        for (size_t i = 0; i < dualRoleDoctors.size(); ++i) delete dualRoleDoctors[i];
        dualRoleDoctors.clear();
    }

    Patient* findPatient(int id) const { return patientStore.findById(id); }
    Patient* findPatient(const string& name) const {
        const vector<Patient*>& list = patientStore.getAll();
        for (size_t i = 0; i < list.size(); ++i) {
            if (list[i]->getName() == name) return list[i];
        }
        return NULL;
    }
    Doctor* findDoctor(int id) const { return doctorStore.findById(id); }

    void loadData() {
        loadUsers(); loadPatients(); loadDoctors();
        loadAppointments(); loadPrescriptions(); loadReportsBinary();
    }

    void saveData() {
        patientStore.saveToFile("data/patients.txt");
        doctorStore.saveToFile("data/doctors.txt");
        appointmentStore.saveToFile("data/appointments.txt");
        prescriptionStore.saveToFile("data/prescriptions.txt");
        saveReportsBinary();
    }

    bool login() {
        string u, p; int attempts = 0;
        while (attempts < 3) {
            cout << "Username: "; cin >> u;
            cout << "Password: "; cin >> p;
            if (checkAuth(u, p)) { cout << "Login successful. Welcome, " << u << "!\n"; return true; }
            attempts++;
            cout << "Invalid login. Attempts remaining: " << (3 - attempts) << "\n";
        }
        return false;
    }

    void addPatientMenu() {
        string name, gen, phone, addr, bg, admitDate; int age, roomNo; char inPat;
        cout << "\n--- Add Patient ---\nName: "; cin.ignore(); getline(cin, name);
        if (!isValidName(name)) { cout << "Invalid name.\n"; return; }
        cout << "Age: "; cin >> age;
        if (!isValidAge(age)) { cout << "Invalid age.\n"; return; }
        cout << "Gender (M/F): "; cin >> gen;
        cout << "Phone (10 digits): "; cin >> phone;
        if (!isValidPhone(phone)) { cout << "Invalid phone.\n"; return; }
        cout << "Address: "; cin.ignore(); getline(cin, addr);
        cout << "Blood Group: "; getline(cin, bg);
        cout << "In-Patient? (y/n): "; cin >> inPat;

        int id = Patient::nextPatientId++;
        if (inPat == 'y' || inPat == 'Y') {
            cout << "Room No: "; cin >> roomNo;
            cout << "Admit Date (YYYY-MM-DD): "; cin >> admitDate;
            if (!isValidDate(admitDate)) { cout << "Invalid date format.\n"; return; }
            patientStore.add(new InPatient(id, name, age, gen, phone, addr, bg, roomNo, admitDate));
        } else {
            patientStore.add(new Patient(id, name, age, gen, phone, addr, bg));
        }
        saveData();
        cout << "Patient added. ID: " << id << "\n";
    }

    void viewPatientsMenu() {
        cout << "\n--- Patients (Sorted by Name) ---\n";
        vector<Patient*> list = patientStore.getAll();
        if (list.empty()) { cout << "No patients found.\n"; return; }
        sort(list.begin(), list.end(), comparePatientsByName);
        printPatHeader();
        for (size_t i = 0; i < list.size(); ++i) list[i]->show();
    }

    void updatePatientMenu() {
        int id; cout << "Patient ID to update: "; cin >> id;
        Patient* p = findPatient(id);
        if (!p) { cout << "Patient not found.\n"; return; }
        string phone, addr;
        cout << "New Phone (Enter to keep): "; cin.ignore(); getline(cin, phone);
        if (!phone.empty() && isValidPhone(phone)) p->setPhone(phone);
        cout << "New Address (Enter to keep): "; getline(cin, addr);
        if (!addr.empty()) p->setAddress(addr);
        saveData();
        cout << "Patient updated.\n";
    }

    void deletePatientMenu() {
        int id; cout << "Patient ID to delete: "; cin >> id;
        if (patientStore.remove(id)) { saveData(); cout << "Patient deleted.\n"; }
        else cout << "Patient ID not found.\n";
    }

    void registerDoctorAsPatient() {
        int docId; cout << "Doctor ID to register as patient: "; cin >> docId;
        Doctor* d = findDoctor(docId);
        if (!d) { cout << "Doctor not found.\n"; return; }
        string bg; cout << "Blood Group: "; cin >> bg;
        int pId = Patient::nextPatientId++;
        DoctorPatient* dp = new DoctorPatient(pId, d->getName(), d->getAge(), d->getGender(),
                                              d->getPhone(), d->getAddress(), bg, d->getSpecialization(), d->getFee());
        dualRoleDoctors.push_back(dp);
        patientStore.add(new Patient(pId, d->getName(), d->getAge(), d->getGender(), d->getPhone(), d->getAddress(), bg));
        saveData();
        cout << "Doctor registered as Patient. New Patient ID: " << pId << "\n";
    }

    void addDoctorMenu() {
        string name, gen, phone, addr, spec; int age; double fee;
        cout << "\n--- Add Doctor ---\nDoctor Name: "; cin.ignore(); getline(cin, name);
        if (!isValidName(name)) { cout << "Invalid name.\n"; return; }
        cout << "Age: "; cin >> age;
        if (!isValidAge(age)) { cout << "Invalid age.\n"; return; }
        cout << "Gender: "; cin >> gen;
        cout << "Phone (10 digits): "; cin >> phone;
        if (!isValidPhone(phone)) { cout << "Invalid phone.\n"; return; }
        cout << "Address: "; cin.ignore(); getline(cin, addr);
        cout << "Specialization: "; getline(cin, spec);
        cout << "Consultation Fee: "; cin >> fee;

        int id = Doctor::nextDoctorId++;
        doctorStore.add(new Doctor(id, name, age, gen, phone, addr, spec, fee));
        saveData();
        cout << "Doctor registered. ID: " << id << "\n";
    }

    void viewDoctorsMenu() {
        cout << "\n--- Doctor Directory ---\n";
        showAll(doctorStore);
    }

    void deleteDoctorMenu() {
        int id; cout << "Doctor ID to delete: "; cin >> id;
        if (doctorStore.remove(id)) { saveData(); cout << "Doctor deleted.\n"; }
        else cout << "Doctor ID not found.\n";
    }

    void bookAppointmentMenu() {
        int pId, dId; string date, time, reason;
        cout << "\n--- Book Appointment ---\nPatient ID: "; cin >> pId;
        if (!findPatient(pId)) { cout << "Patient does not exist.\n"; return; }
        cout << "Doctor ID: "; cin >> dId;
        if (!findDoctor(dId)) { cout << "Doctor does not exist.\n"; return; }
        cout << "Date (YYYY-MM-DD): "; cin >> date;
        if (!isValidDate(date)) { cout << "Invalid date format.\n"; return; }
        cout << "Time (HH:MM): "; cin >> time;
        cout << "Reason: "; cin.ignore(); getline(cin, reason);

        int aId = Appointment::nextAppointmentId++;
        appointmentStore.add(new Appointment(aId, pId, dId, date, time, reason, "Booked"));
        saveData();
        cout << "Appointment booked. ID: " << aId << "\n";
    }

    void viewAppointmentsMenu() {
        cout << "\n--- All Appointments ---\n";
        printAppHeader();
        showAll(appointmentStore);
    }

    void changeAppointmentStatusMenu() {
        int id; cout << "Appointment ID: "; cin >> id;
        Appointment* app = appointmentStore.findById(id);
        if (!app) { cout << "Appointment not found.\n"; return; }
        cout << "1. Mark as Done\n2. Mark as Cancelled\nChoice: ";
        int sc; cin >> sc;
        if (sc == 1) app->setStatus("Done");
        else if (sc == 2) app->setStatus("Cancelled");
        saveData();
        cout << "Status updated to: " << app->getStatus() << "\n";
    }

    void filterAppointmentsByDateMenu() {
        string date; cout << "Enter Date (YYYY-MM-DD): "; cin >> date;
        cout << "\n--- Appointments on " << date << " ---\n";
        printAppHeader();
        const vector<Appointment*>& list = appointmentStore.getAll();
        for (size_t i = 0; i < list.size(); ++i) {
            if (list[i]->getDate() == date) list[i]->show();
        }
    }

    void addPrescriptionMenu() {
        int pId, dId; string date, meds, instr;
        cout << "\n--- Add Prescription ---\nPatient ID: "; cin >> pId;
        if (!findPatient(pId)) { cout << "Patient not found.\n"; return; }
        cout << "Doctor ID: "; cin >> dId;
        if (!findDoctor(dId)) { cout << "Doctor not found.\n"; return; }
        cout << "Date (YYYY-MM-DD): "; cin >> date;
        cout << "Medicine: "; cin.ignore(); getline(cin, meds);
        cout << "Instructions: "; getline(cin, instr);

        int prId = Prescription::nextPrescriptionId++;
        Prescription* pres = new Prescription(prId, pId, dId, date, meds, instr);
        cout << "Add extra medicine? (y/n): "; char ch; cin >> ch;
        if (ch == 'y' || ch == 'Y') {
            string extra; cout << "Extra medicine: "; cin.ignore(); getline(cin, extra);
            (*pres) += extra; // Uses operator+=
        }
        prescriptionStore.add(pres);
        saveData();
        cout << "Prescription saved. ID: " << prId << "\n";
    }

    void viewPrescriptionsMenu() {
        cout << "\n--- Prescriptions ---\n";
        showAll(prescriptionStore);
    }

    void addReportMenu() {
        int pId; string date, diag, notes;
        cout << "\n--- Add Medical Report ---\nPatient ID: "; cin >> pId;
        if (!findPatient(pId)) { cout << "Patient not found.\n"; return; }
        cout << "Date (YYYY-MM-DD): "; cin >> date;
        cout << "Diagnosis: "; cin.ignore(); getline(cin, diag);
        cout << "Notes: "; getline(cin, notes);

        int repId = MedicalReport::nextReportId++;
        MedicalReport* r = new MedicalReport(repId, pId, date, diag, notes);
        reportStore.add(r);
        saveReportsBinary();
        cout << "Report added. ID: " << repId << "\n";
        cout << "\nPrintout via Friend Class:\n";
        ReportPrinter::printFormatted(*r);
        cout << "Log Line via operator<<: " << *r << "\n";
    }

    void viewReportsMenu() {
        cout << "\n--- Medical Reports (Binary File) ---\n";
        showAll(reportStore);
    }

    void updateReportInPlaceMenu() {
        int repId; cout << "Report ID to update in binary file: "; cin >> repId;
        MedicalReport* r = reportStore.findById(repId);
        if (!r) { cout << "Report not found.\n"; return; }
        string diag, notes;
        cout << "New Diagnosis: "; cin.ignore(); getline(cin, diag);
        cout << "New Notes: "; getline(cin, notes);
        if (updateBinaryRecord(repId, diag, notes)) {
            r->setDiagnosis(diag);
            r->setNotes(notes);
            cout << "Report updated directly in binary file via seekp.\n";
        } else {
            cout << "Binary update failed.\n";
        }
    }

    void searchPatientMenu() {
        cout << "1. By ID  2. By Name\nChoice: "; int sc; cin >> sc;
        if (sc == 1) {
            int id; cout << "ID: "; cin >> id;
            Patient* p = findPatient(id);
            if (p) { printPatHeader(); p->show(); } else cout << "Not found.\n";
        } else if (sc == 2) {
            string name; cout << "Name: "; cin.ignore(); getline(cin, name);
            Patient* p = findPatient(name);
            if (p) { printPatHeader(); p->show(); } else cout << "Not found.\n";
        }
    }

    void viewMedicalHistoryMenu() {
        int pId; cout << "Patient ID for timeline: "; cin >> pId;
        Patient* p = findPatient(pId);
        if (!p) { cout << "Patient not found.\n"; return; }
        cout << "\n=== MEDICAL TIMELINE: " << p->getName() << " (ID: " << pId << ") ===\n";

        vector<HealthRecord*> timeline;
        const vector<Appointment*>& apps = appointmentStore.getAll();
        for (size_t i = 0; i < apps.size(); ++i) if (apps[i]->getPatientId() == pId) timeline.push_back(apps[i]);
        const vector<Prescription*>& pres = prescriptionStore.getAll();
        for (size_t i = 0; i < pres.size(); ++i) if (pres[i]->getPatientId() == pId) timeline.push_back(pres[i]);
        const vector<MedicalReport*>& reps = reportStore.getAll();
        for (size_t i = 0; i < reps.size(); ++i) if (reps[i]->getPatientId() == pId) timeline.push_back(reps[i]);

        if (timeline.empty()) { cout << "No history found.\n"; return; }
        sort(timeline.begin(), timeline.end(), compareRecordsByDate);
        for (size_t i = 0; i < timeline.size(); ++i) {
            cout << "[" << timeline[i]->getDate() << "] ";
            timeline[i]->show();
        }
    }

    void showAppointmentsPerDoctor() {
        cout << "\n--- Appointments per Doctor (STL map) ---\n";
        map<int, int> counts;
        const vector<Appointment*>& apps = appointmentStore.getAll();
        for (size_t i = 0; i < apps.size(); ++i) counts[apps[i]->getDoctorId()]++;

        cout << left << setw(12) << "Doctor ID" << setw(20) << "Doctor Name" << setw(15) << "Appointments" << "\n";
        cout << "------------------------------------------------\n";
        for (map<int, int>::iterator it = counts.begin(); it != counts.end(); ++it) {
            Doctor* d = findDoctor(it->first);
            string dName = d ? d->getName() : "Unknown";
            cout << left << setw(12) << it->first << setw(20) << dName << setw(15) << it->second << "\n";
        }
    }

    void demonstrateCasting(int docId) {
        Doctor* doc = findDoctor(docId);
        if (!doc) { cout << "Doctor not found.\n"; return; }
        Person* personPtr = doc; // Upcast
        cout << "[Casting Demo] Upcast successful. Person role: " << personPtr->getRole() << "\n";
        Doctor* downcasted = dynamic_cast<Doctor*>(personPtr); // Downcast with NULL check
        if (downcasted != NULL) {
            cout << "[Casting Demo] Downcast successful! Specialization: "
                 << downcasted->getSpecialization() << ", Fee: " << downcasted->getFee() << "\n";
        }
    }

    void showHospitalDirectory() {
        cout << "\n--- Hospital Directory (Polymorphic vector<Person*>) ---\n";
        vector<Person*> directory;
        const vector<Doctor*>& docs = doctorStore.getAll();
        for (size_t i = 0; i < docs.size(); ++i) directory.push_back(docs[i]);
        const vector<Patient*>& pats = patientStore.getAll();
        for (size_t i = 0; i < pats.size(); ++i) directory.push_back(pats[i]);

        cout << left << setw(6) << "ID" << setw(18) << "Name" << setw(14) << "Role" << setw(14) << "Phone" << "\n";
        cout << "------------------------------------------------------\n";
        for (size_t i = 0; i < directory.size(); ++i) {
            cout << left << setw(6) << directory[i]->getId() << setw(18) << directory[i]->getName()
                 << setw(14) << directory[i]->getRole() << setw(14) << directory[i]->getPhone() << "\n";
        }
        cout << "Total individuals: " << Person::getCount() << "\n";
    }

private:
    void printPatHeader() const {
        cout << left << setw(6) << "ID" << setw(18) << "Name" << setw(6) << "Age"
             << setw(8) << "Gender" << setw(13) << "Phone" << setw(15) << "Address" << setw(8) << "Blood" << "\n";
        cout << "--------------------------------------------------------------------------------\n";
    }
    void printAppHeader() const {
        cout << left << setw(6) << "ID" << setw(10) << "Patient" << setw(10) << "Doctor"
             << setw(12) << "Date" << setw(8) << "Time" << setw(20) << "Reason" << setw(10) << "Status" << "\n";
        cout << "--------------------------------------------------------------------------------\n";
    }

    bool checkAuth(const string& u, const string& p) {
        ifstream fin("data/users.txt");
        if (!fin.is_open()) return (u == "admin" && p == "admin123");
        string line;
        while (getline(fin, line)) {
            vector<string> parts = splitLine(line, '|');
            if (parts.size() >= 2 && parts[0] == u && parts[1] == p) { fin.close(); return true; }
        }
        fin.close();
        return false;
    }

    void loadUsers() {
        ifstream fin("data/users.txt");
        if (!fin.is_open()) {
            ofstream fout("data/users.txt");
            if (fout.is_open()) { fout << "admin|admin123\n"; fout.close(); }
        } else fin.close();
    }

    void loadPatients() {
        ifstream fin("data/patients.txt");
        if (!fin.is_open()) return;
        string l;
        while (getline(fin, l)) {
            if (l.empty()) continue;
            vector<string> p = splitLine(l, '|');
            if (p.size() >= 7) patientStore.add(new Patient(atoi(p[0].c_str()), p[1], atoi(p[2].c_str()), p[3], p[4], p[5], p[6]));
        }
        fin.close();
    }

    void loadDoctors() {
        ifstream fin("data/doctors.txt");
        if (!fin.is_open()) return;
        string l;
        while (getline(fin, l)) {
            if (l.empty()) continue;
            vector<string> p = splitLine(l, '|');
            if (p.size() >= 8) doctorStore.add(new Doctor(atoi(p[0].c_str()), p[1], atoi(p[2].c_str()), p[3], p[4], p[5], p[6], atof(p[7].c_str())));
        }
        fin.close();
    }

    void loadAppointments() {
        ifstream fin("data/appointments.txt");
        if (!fin.is_open()) return;
        string l;
        while (getline(fin, l)) {
            if (l.empty()) continue;
            vector<string> p = splitLine(l, '|');
            if (p.size() >= 7) appointmentStore.add(new Appointment(atoi(p[0].c_str()), atoi(p[1].c_str()), atoi(p[2].c_str()), p[3], p[4], p[5], p[6]));
        }
        fin.close();
    }

    void loadPrescriptions() {
        ifstream fin("data/prescriptions.txt");
        if (!fin.is_open()) return;
        string l;
        while (getline(fin, l)) {
            if (l.empty()) continue;
            vector<string> p = splitLine(l, '|');
            if (p.size() >= 6) prescriptionStore.add(new Prescription(atoi(p[0].c_str()), atoi(p[1].c_str()), atoi(p[2].c_str()), p[3], p[4], p[5]));
        }
        fin.close();
    }

    void saveReportsBinary() {
        ofstream fout("data/reports.dat", ios::binary | ios::trunc);
        if (!fout.is_open()) return;
        const vector<MedicalReport*>& list = reportStore.getAll();
        for (size_t i = 0; i < list.size(); ++i) {
            ReportData d;
            d.id = list[i]->getId();
            d.patientId = list[i]->getPatientId();
            memset(d.date, 0, sizeof(d.date));
            memset(d.diagnosis, 0, sizeof(d.diagnosis));
            memset(d.notes, 0, sizeof(d.notes));
            strncpy(d.date, list[i]->getDate().c_str(), sizeof(d.date) - 1);
            strncpy(d.diagnosis, list[i]->getDiagnosis().c_str(), sizeof(d.diagnosis) - 1);
            strncpy(d.notes, list[i]->getNotes().c_str(), sizeof(d.notes) - 1);
            fout.write(reinterpret_cast<const char*>(&d), sizeof(ReportData));
        }
        fout.close();
    }

    void loadReportsBinary() {
        ifstream fin("data/reports.dat", ios::binary);
        if (!fin.is_open()) return;
        ReportData d;
        while (fin.read(reinterpret_cast<char*>(&d), sizeof(ReportData))) {
            reportStore.add(new MedicalReport(d.id, d.patientId, d.date, d.diagnosis, d.notes));
        }
        fin.close();
    }

    bool updateBinaryRecord(int repId, const string& newDiag, const string& newNotes) {
        fstream file("data/reports.dat", ios::in | ios::out | ios::binary);
        if (!file.is_open()) return false;
        ReportData d; int idx = 0; bool found = false;
        while (file.read(reinterpret_cast<char*>(&d), sizeof(ReportData))) {
            if (d.id == repId) {
                found = true;
                memset(d.diagnosis, 0, sizeof(d.diagnosis));
                memset(d.notes, 0, sizeof(d.notes));
                strncpy(d.diagnosis, newDiag.c_str(), sizeof(d.diagnosis) - 1);
                strncpy(d.notes, newNotes.c_str(), sizeof(d.notes) - 1);
                file.seekp(idx * sizeof(ReportData), ios::beg);
                file.write(reinterpret_cast<const char*>(&d), sizeof(ReportData));
                break;
            }
            idx++;
        }
        file.close();
        return found;
    }
};

#endif
