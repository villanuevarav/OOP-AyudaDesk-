package ayudadesk.model;

// APP DATA
// Holds everything the program stores while it is running, so every
// screen shares the same staff, applicants and applications.
public class AppData {
    private Staff[] staffAccounts = {
        new Staff("S001", "Maria Reyes", "admin123"),
        new Staff("S002", "Jose Santos", "staff456")
    };

    private Applicant[] applicants = new Applicant[100];
    private int applicantCount = 0;

    private AidApplication[] applications = new AidApplication[100];
    private int applicationCount = 0;

    // Returns the matching Staff, or null if the ID or password is wrong
    public Staff login(String id, String pass) {
        for (int i = 0; i < staffAccounts.length; i++) {
            if (staffAccounts[i].login(id, pass)) {
                return staffAccounts[i];
            }
        }
        return null;
    }

    public boolean addApplicant(Applicant applicant) {
        if (applicantCount >= applicants.length) {
            return false;
        }
        applicants[applicantCount] = applicant;
        applicantCount++;
        return true;
    }

    public int getApplicantCount() {
        return applicantCount;
    }

    public Applicant getApplicant(int index) {
        return applicants[index];
    }

    public boolean canAddApplication() {
        return applicationCount < applications.length;
    }

    public boolean addApplication(AidApplication application) {
        if (applicationCount >= applications.length) {
            return false;
        }
        applications[applicationCount] = application;
        applicationCount++;
        return true;
    }

    public int getApplicationCount() {
        return applicationCount;
    }

    public AidApplication getApplication(int index) {
        return applications[index];
    }

    // Returns the pending applications, highest priority first (PWD before Regular).
    // Applications with the same priority stay in the order they were submitted.
    public AidApplication[] getPendingApplications() {
        int count = 0;
        for (int i = 0; i < applicationCount; i++) {
            if (applications[i].getStatus().equals("Pending")) {
                count++;
            }
        }

        AidApplication[] pending = new AidApplication[count];
        int n = 0;
        for (int i = 0; i < applicationCount; i++) {
            if (applications[i].getStatus().equals("Pending")) {
                pending[n] = applications[i];
                n++;
            }
        }

        // Bubble sort. getPriorityLevel() is polymorphic (Regular = 1, PWD = 2).
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - 1 - i; j++) {
                int first = pending[j].getApplicant().getPriorityLevel();
                int second = pending[j + 1].getApplicant().getPriorityLevel();

                if (first < second) {
                    AidApplication temp = pending[j];
                    pending[j] = pending[j + 1];
                    pending[j + 1] = temp;
                }
            }
        }
        return pending;
    }
}
