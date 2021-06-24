import java.util.*;

public class ConsoleInput extends Input {

    protected Scanner userInput;

    public ConsoleInput() {
        this.userInput = new Scanner(System.in);
    }

    public String listen() {
        return userInput.nextLine();
    }


}
