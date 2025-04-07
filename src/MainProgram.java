package src;

public class MainProgram {
    public static void main(String[] args) {
        User putin = UserFactory.createUser("student","Vladimir Putin","putin2046@gmail.com","qwertyuiop");
//        System.out.println(putin.toString());
        DataBaseConfig.getInstance();
    }
}
