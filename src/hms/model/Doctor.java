package hms.model;

// Doctor entity extending Person
public class Doctor extends Person implements Displayable {
    protected String specialization;
    protected double fee;

    public Doctor() {
        super();
        this.specialization = "";
        this.fee = 0.0;
    }

    public Doctor(int id, String name, int age, String gender, String phone, String address,
                  String specialization, double fee) {
        super(id, name, age, gender, phone, address);
        this.specialization = specialization;
        this.fee = fee;
    }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }

    public String getRole() { return "Doctor"; }

    public String getSummary() {
        String title = name.startsWith("Dr.") ? name : ("Dr. " + name);
        return title + " (" + specialization + "), Fee: $" + fee + ", Phone: " + phone;
    }

    public String toString() {
        return name + " (" + specialization + ")";
    }
}
