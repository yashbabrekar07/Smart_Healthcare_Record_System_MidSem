package hms.model;

// Prescription record
public class Prescription extends HealthRecord {
    private int doctorId;
    private String medicines;
    private String instructions;

    public Prescription() {
        super();
        this.doctorId = 0;
        this.medicines = "";
        this.instructions = "";
    }

    public Prescription(int id, int patientId, int doctorId, String date, String medicines, String instructions) {
        super(id, patientId, date);
        this.doctorId = doctorId;
        this.medicines = medicines;
        this.instructions = instructions;
    }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public String getMedicines() { return medicines; }
    public void setMedicines(String medicines) { this.medicines = medicines; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getSummary() {
        return "[" + date + "] Prescription #" + id + " (Dr. " + doctorId + "): " + medicines + " | " + instructions;
    }
}
