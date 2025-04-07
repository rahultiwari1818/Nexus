package src;

public class Professor extends User {
    public Professor(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public String getRole() {
        return "PROFESSOR";
    }
}
