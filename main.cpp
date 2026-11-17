#if defined(__STRICT_ANSI__) && defined(__MINGW32__)
#undef __STRICT_ANSI__
#endif

#include "hospital.h"
#include <iostream>
#include <string>

using namespace std;

void patientMenu(Hospital &h) {
  int c = -1;
  while (c != 0) {
    cout << "\n--- Patient Menu ---\n1. Add Patient\n2. View Patients\n3. "
            "Update Patient\n4. Delete Patient\n5. Register Doctor as "
            "Patient\n0. Back\nChoice: ";
    cin >> c;
    if (c == 1)
      h.addPatientMenu();
    else if (c == 2)
      h.viewPatientsMenu();
    else if (c == 3)
      h.updatePatientMenu();
    else if (c == 4)
      h.deletePatientMenu();
    else if (c == 5)
      h.registerDoctorAsPatient();
  }
}

void doctorMenu(Hospital &h) {
  int c = -1;
  while (c != 0) {
    cout << "\n--- Doctor Menu ---\n1. Add Doctor\n2. View Doctors\n3. Delete "
            "Doctor\n0. Back\nChoice: ";
    cin >> c;
    if (c == 1)
      h.addDoctorMenu();
    else if (c == 2)
      h.viewDoctorsMenu();
    else if (c == 3)
      h.deleteDoctorMenu();
  }
}

void appointmentMenu(Hospital &h) {
  int c = -1;
  while (c != 0) {
    cout << "\n--- Appointment Menu ---\n1. Book Appointment\n2. View "
            "Appointments\n3. Change Status\n4. Filter by Date\n0. "
            "Back\nChoice: ";
    cin >> c;
    if (c == 1)
      h.bookAppointmentMenu();
    else if (c == 2)
      h.viewAppointmentsMenu();
    else if (c == 3)
      h.changeAppointmentStatusMenu();
    else if (c == 4)
      h.filterAppointmentsByDateMenu();
  }
}

void prescriptionMenu(Hospital &h) {
  int c = -1;
  while (c != 0) {
    cout << "\n--- Prescription Menu ---\n1. Add Prescription\n2. View "
            "Prescriptions\n0. Back\nChoice: ";
    cin >> c;
    if (c == 1)
      h.addPrescriptionMenu();
    else if (c == 2)
      h.viewPrescriptionsMenu();
  }
}

void reportMenu(Hospital &h) {
  int c = -1;
  while (c != 0) {
    cout << "\n--- Medical Report Menu (Binary File) ---\n1. Add Report\n2. "
            "View Reports\n3. Update Report in Binary File (seekg/seekp)\n0. "
            "Back\nChoice: ";
    cin >> c;
    if (c == 1)
      h.addReportMenu();
    else if (c == 2)
      h.viewReportsMenu();
    else if (c == 3)
      h.updateReportInPlaceMenu();
  }
}

int main() {
  Hospital hospital;
  hospital.loadData();

  cout << "==================================================\n";
  cout << "         SMART HEALTHCARE RECORD SYSTEM\n";
  cout << "==================================================\n";

  if (!hospital.login()) {
    cout << "\nToo many failed login attempts. Exiting.\n";
    return 1;
  }

  int choice = -1;
  while (choice != 0) {
    cout << "\n================== MAIN MENU ==================\n";
    cout << "1. Patient Management\n";
    cout << "2. Doctor Management\n";
    cout << "3. Appointment Management\n";
    cout << "4. Prescription Management\n";
    cout << "5. Medical Report Management\n";
    cout << "6. Search Patient (by ID or Name)\n";
    cout << "7. View Medical History Timeline\n";
    cout << "8. View Appointments per Doctor (STL Map)\n";
    cout << "9. Hospital Directory (Polymorphism)\n";
    cout << "10. Casting Demonstration (Upcast / Downcast)\n";
    cout << "0. Exit\n";
    cout << "Enter your choice: ";
    cin >> choice;

    switch (choice) {
    case 1:
      patientMenu(hospital);
      break;
    case 2:
      doctorMenu(hospital);
      break;
    case 3:
      appointmentMenu(hospital);
      break;
    case 4:
      prescriptionMenu(hospital);
      break;
    case 5:
      reportMenu(hospital);
      break;
    case 6:
      hospital.searchPatientMenu();
      break;
    case 7:
      hospital.viewMedicalHistoryMenu();
      break;
    case 8:
      hospital.showAppointmentsPerDoctor();
      break;
    case 9:
      hospital.showHospitalDirectory();
      break;
    case 10: {
      int docId;
      cout << "Enter Doctor ID for casting demonstration: ";
      cin >> docId;
      hospital.demonstrateCasting(docId);
      break;
    }
    case 0:
      hospital.saveData();
      cout << "\nAll records saved. Thank you for using the system.\n";
      break;
    default:
      cout << "Invalid choice. Please try again.\n";
    }
  }

  return 0;
}
