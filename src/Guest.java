package src;

public class Guest extends User {
    public Guest(String name, String email, String password) {
        super(name, email, password);
    }

    public String getRole() {
        return "GUEST";
    }
}
