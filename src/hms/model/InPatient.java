package hms.model;

// InPatient extends Patient with room and admission details
public class InPatient extends Patient {
    private int roomNo;
    private String admitDate;

    public InPatient() {
        super();
        this.roomNo = 0;
        this.admitDate = "";
    }

    public InPatient(int id, String name, int age, String gender, String phone, String address,
                     String bloodGroup, int roomNo, String admitDate) {
        super(id, name, age, gender, phone, address, bloodGroup);
        this.roomNo = roomNo;
        this.admitDate = admitDate;
    }

    public int getRoomNo() { return roomNo; }
    public void setRoomNo(int roomNo) { this.roomNo = roomNo; }
    public String getAdmitDate() { return admitDate; }
    public void setAdmitDate(String admitDate) { this.admitDate = admitDate; }

    public String getRole() { return "InPatient"; }

    public String getSummary() {
        return super.getSummary() + " [Room " + roomNo + ", Admitted: " + admitDate + "]";
    }
}
