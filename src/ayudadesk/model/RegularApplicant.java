package ayudadesk.model;

// CHILD CLASS 1
public class RegularApplicant extends Applicant {

    public RegularApplicant(String name, int age, String address, String contactNumber) {
        super(name, age, address, contactNumber);
    }

    @Override
    public int getPriorityLevel() {
        return 1;
    }

    @Override
    public String getApplicantType() {
        return "Regular";
    }
}
