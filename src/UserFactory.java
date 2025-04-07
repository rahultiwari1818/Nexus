package src;

public class UserFactory {
    static User createUser(String role,String name,String email,String password){
        if(role.compareTo("student") == 0) return new Student(name,email,password);
        if(role.compareTo("professor") == 0) return new Professor(name,email,password);
        if(role.compareTo("guest") == 0) return new Guest(name,email,password);
        throw new IllegalArgumentException("Unknown role: " + role);
    }
}
