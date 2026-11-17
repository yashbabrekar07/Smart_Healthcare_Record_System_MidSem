package hms.model;

// Abstract base class representing a person
public abstract class Person {
    public static final String HOSPITAL_NAME = "Smart Health Clinic";
    public static int personCount = 0;

    protected int id;
    protected String name;
    protected int age;
    protected String gender;
    protected String phone;
    protected String address;

    public Person() {
        this(0, "", 0, "", "", "");
    }

    public Person(int id, String name, int age, String gender, String phone, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        personCount++;
    }

    public abstract String getRole();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
