public class RentalSummary {

    private String firstName;
    private String lastName;
    private String email;
    private double amount;

    public RentalSummary(String firstName, String lastName, String email, double amount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return firstName + "," + lastName + "," + email + "," + amount;
    }
}
