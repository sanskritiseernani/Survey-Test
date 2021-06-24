import java.io.*;

public class Main implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        System.out.println("\nWelcome to the Survey-Test Maker");
        System.out.println("Use the menu below to start the process of making a Survey or a Test");

        MainDriver mainDriver = new MainDriver();

        while (true) {
            mainDriver.printMenus();
        }
    }
}
