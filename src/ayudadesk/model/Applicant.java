package ayudadesk.model;

// PARENT CLASS (ABSTRACT)
// Shared attributes of every applicant. Fields are private (Encapsulation).
public abstract class Applicant {
    private String name;
    private int age;
    private String address;
    private String contactNumber;
    private String status;

    public Applicant(String name, int age, String address, String contactNumber) {
        setName(name);
        setAge(age);
        setAddress(address);
        setContactNumber(contactNumber);
        this.status = "No application";
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getStatus() {
        return status;
    }

    // Setters with validation (invalid values are stored as empty/0,
    // so the completeness check can catch them later)
    public void setName(String name) {
        if (name == null) {
            this.name = "";
        } else {
            this.name = name.trim();
        }
    }

    public void setAge(int age) {
        if (age > 0 && age <= 120) {
            this.age = age;
        } else {
            this.age = 0;
        }
    }

    public void setAddress(String address) {
        if (address == null) {
            this.address = "";
        } else {
            this.address = address.trim();
        }
    }

    public void setContactNumber(String contactNumber) {
        if (contactNumber == null) {
            this.contactNumber = "";
        } else {
            this.contactNumber = contactNumber.trim();
        }
    }

    // No "public": only classes in the model package (AidApplication) update the status
    void setStatus(String status) {
        this.status = status;
    }

    // Adds an item to a comma-separated list (used by getMissingDetails)
    protected String addMissing(String list, String item) {
        if (list.isEmpty()) {
            return item;
        }
        return list + ", " + item;
    }

    // Lists the details that are missing or invalid (empty text = complete)
    public String getMissingDetails() {
        String missing = "";
        if (name.isEmpty()) {
            missing = addMissing(missing, "Name");
        }
        if (age <= 0) {
            missing = addMissing(missing, "Age");
        }
        if (address.isEmpty()) {
            missing = addMissing(missing, "Address");
        }
        if (!contactNumber.matches("09\\d{9}")) {
            missing = addMissing(missing, "Contact Number (11 digits starting with 09)");
        }
        return missing;
    }

    public boolean hasCompleteDetails() {
        return getMissingDetails().isEmpty();
    }

    // Extra details shown in tables. Subclasses can override it.
    public String getExtraInfo() {
        return "-";
    }

    // Each subclass decides its own priority (higher number = higher priority)
    public abstract int getPriorityLevel();

    public abstract String getApplicantType();

    // Display (console)
    public void displayApplicant() {
        System.out.println("Type: " + getApplicantType());
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Address: " + address);
        System.out.println("Contact Number: " + contactNumber);
        System.out.println("Priority Level: " + getPriorityLevel());
        System.out.println("Status: " + status);
    }
}
