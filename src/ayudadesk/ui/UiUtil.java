package ayudadesk.ui;

import ayudadesk.model.Applicant;

// Small helper methods shared by the screens
final class UiUtil {

    private UiUtil() {
    }

    // Text shown in the applicant drop-down lists, e.g. "1. Juan Dela Cruz (Regular)"
    static String applicantLabel(int index, Applicant applicant) {
        String name = applicant.getName();
        if (name.isEmpty()) {
            name = "(no name)";
        }
        return (index + 1) + ". " + name + " (" + applicant.getApplicantType() + ")";
    }

    static String money(double amount) {
        return String.format("PHP %,.2f", amount);
    }
}
