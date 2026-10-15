package ayudadesk.model;

import java.time.LocalDate;

// AID APPLICATION
// Abstraction: the user interface only calls submitApplication(),
// approveApplication() and rejectApplication(). The validation and
// status tracking are hidden inside this class.
public class AidApplication {
    private Applicant applicant;
    private LocalDate dateSubmitted;
    private double amount;
    private String status;

    public AidApplication(Applicant applicant, double amount) {
        this.applicant = applicant;
        setAmount(amount);
        this.status = "Draft";
    }

    // Getters
    public Applicant getApplicant() {
        return applicant;
    }

    public LocalDate getDateSubmitted() {
        return dateSubmitted;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public void setAmount(double amount) {
        if (amount > 0) {
            this.amount = amount;
        } else {
            this.amount = 0;
        }
    }

    // Completeness validation
    public String getMissingDetails() {
        String missing = applicant.getMissingDetails();
        if (amount <= 0) {
            if (!missing.isEmpty()) {
                missing += ", ";
            }
            missing += "Amount";
        }
        return missing;
    }

    public boolean isComplete() {
        return getMissingDetails().isEmpty();
    }

    // Returns true if the application is complete and is now pending
    public boolean submitApplication() {
        if (!isComplete()) {
            updateStatus("Incomplete");
            return false;
        }
        dateSubmitted = LocalDate.now();
        updateStatus("Pending");
        return true;
    }

    public boolean approveApplication() {
        if (!status.equals("Pending")) {
            return false;
        }
        updateStatus("Approved");
        return true;
    }

    public boolean rejectApplication() {
        if (!status.equals("Pending")) {
            return false;
        }
        updateStatus("Rejected");
        return true;
    }

    // Keeps the application status and the applicant's status the same
    private void updateStatus(String newStatus) {
        this.status = newStatus;
        applicant.setStatus(newStatus);
    }
}
