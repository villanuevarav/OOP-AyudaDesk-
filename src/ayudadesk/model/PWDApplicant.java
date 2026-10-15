package ayudadesk.model;

// CHILD CLASS 2
public class PWDApplicant extends Applicant {
    private String pwdIdNumber;
    private String disabilityType;

    public PWDApplicant(String name, int age, String address, String contactNumber,
                        String pwdIdNumber, String disabilityType) {

        super(name, age, address, contactNumber);
        setPwdIdNumber(pwdIdNumber);
        setDisabilityType(disabilityType);
    }

    public String getPwdIdNumber() {
        return pwdIdNumber;
    }

    public String getDisabilityType() {
        return disabilityType;
    }

    public void setPwdIdNumber(String pwdIdNumber) {
        if (pwdIdNumber == null) {
            this.pwdIdNumber = "";
        } else {
            this.pwdIdNumber = pwdIdNumber.trim();
        }
    }

    public void setDisabilityType(String disabilityType) {
        if (disabilityType == null) {
            this.disabilityType = "";
        } else {
            this.disabilityType = disabilityType.trim();
        }
    }

    @Override
    public String getMissingDetails() {
        String missing = super.getMissingDetails();
        if (pwdIdNumber.isEmpty()) {
            missing = addMissing(missing, "PWD ID Number");
        }
        if (disabilityType.isEmpty()) {
            missing = addMissing(missing, "Disability Type");
        }
        return missing;
    }

    @Override
    public String getExtraInfo() {
        return "PWD ID: " + pwdIdNumber + ", " + disabilityType;
    }

    @Override
    public int getPriorityLevel() {
        return 2;
    }

    @Override
    public String getApplicantType() {
        return "PWD";
    }

    @Override
    public void displayApplicant() {
        super.displayApplicant();
        System.out.println("PWD ID Number: " + pwdIdNumber);
        System.out.println("Disability Type: " + disabilityType);
    }
}
