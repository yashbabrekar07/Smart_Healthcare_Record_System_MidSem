package hms.model;

// Abstract base class for healthcare records
public abstract class HealthRecord implements Displayable {
    protected int id;
    protected int patientId;
    protected String date;

    public HealthRecord() {
        this(0, 0, "");
    }

    public HealthRecord(int id, int patientId, String date) {
        this.id = id;
        this.patientId = patientId;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public abstract String getSummary();
}
