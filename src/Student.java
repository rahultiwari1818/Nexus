package src;

public class Student extends User {
    public Student(String name, String email, String password) {
        super(name, email, password);
    }

    public String getRole() {
        return "STUDENT";
    }
}

