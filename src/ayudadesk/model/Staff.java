package ayudadesk.model;

// STAFF
public class Staff {
    private String staffId;
    private String name;
    private String password;

    public Staff(String staffId, String name, String password) {
        this.staffId = staffId;
        this.name = name;
        this.password = password;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getName() {
        return name;
    }

    public boolean login(String id, String pass) {
        return staffId.equals(id) && password.equals(pass);
    }

    // Saves the applicant in the system. Returns false if the system is full.
    public boolean registerApplicant(Applicant applicant, AppData data) {
        return data.addApplicant(applicant);
    }

    // true = approve, false = reject
    public boolean reviewApplication(AidApplication application, boolean approve) {
        if (approve) {
            return application.approveApplication();
        } else {
            return application.rejectApplication();
        }
    }
}
