package hms.model;

// Appointment record linking a patient with a doctor
public class Appointment extends HealthRecord {
    private int doctorId;
    private String time;
    private String reason;
    private String status; // Booked, Done, Cancelled

    public Appointment() {
        super();
        this.doctorId = 0;
        this.time = "";
        this.reason = "";
        this.status = "Booked";
    }

    public Appointment(int id, int patientId, int doctorId, String date, String time, String reason, String status) {
        super(id, patientId, date);
        this.doctorId = doctorId;
        this.time = time;
        this.reason = reason;
        this.status = status;
    }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSummary() {
        return "[" + date + " " + time + "] Appointment #" + id + ": Doctor " + doctorId + " - " + reason + " (" + status + ")";
    }
}
