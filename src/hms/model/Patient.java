package hms.model;

// Patient entity inheriting Person and implementing multiple interfaces
public class Patient extends Person implements Displayable, Searchable {
    protected String bloodGroup;

    public Patient() {
        super();
        this.bloodGroup = "";
    }

    public Patient(String name, int age, String gender, String phone, String address, String bloodGroup) {
        this(0, name, age, gender, phone, address, bloodGroup); // Constructor chaining
    }

    public Patient(int id, String name, int age, String gender, String phone, String address, String bloodGroup) {
        super(id, name, age, gender, phone, address);
        this.bloodGroup = bloodGroup;
    }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getRole() { return "Patient"; }

    public String getSummary() {
        return "Patient #" + id + ": " + name + " (" + age + ", " + gender + ") Phone: " + phone + " Blood: " + bloodGroup;
    }

    public boolean matches(String key) {
        if (key == null || key.trim().equals("")) return true;
        String lower = key.toLowerCase();
        return name.toLowerCase().indexOf(lower) >= 0
            || phone.indexOf(lower) >= 0
            || String.valueOf(id).equals(lower)
            || bloodGroup.toLowerCase().indexOf(lower) >= 0;
    }

    // Overriding equals and hashCode on patient id for HashSet duplicate removal
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Patient other = (Patient) obj;
        return this.id == other.id;
    }

    public int hashCode() {
        return Integer.valueOf(id).hashCode();
    }
}
