package hms.model;

// Medical report record
public class MedicalReport extends HealthRecord {
    private String diagnosis;
    private String notes;

    public MedicalReport() {
        super();
        this.diagnosis = "";
        this.notes = "";
    }

    public MedicalReport(int id, int patientId, String date, String diagnosis, String notes) {
        super(id, patientId, date);
        this.diagnosis = diagnosis;
        this.notes = notes;
    }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getSummary() {
        return "[" + date + "] Report #" + id + ": Diagnosis: " + diagnosis + " | Notes: " + notes;
    }
}
