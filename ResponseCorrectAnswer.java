import java.io.*;
import java.util.*;

public class ResponseCorrectAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<String> responses;

    public ResponseCorrectAnswer() {
        responses = new ArrayList<String>();
    }


    public ArrayList<String> getTexts()  {
        return this.responses;
    }

    public void setTexts(ArrayList<String> texts) {
        this.responses = texts;
    }

    public void addText(String text) {
        this.responses.add(text);
    }

    // this method will be worked on in a later part of the hw
    public boolean compare(ResponseCorrectAnswer a, ResponseCorrectAnswer b) {
        // First check of the lengths of the RCAs are equal
        // Then, loop through both RCAs and .equals() their values for each entry respectively
        // If at any point their values dont match, stop and return false
        // If they match in every regard, return true

        if (a.equals(b)) {
            return true;
        }
        else {
            return false;
        }
    }


}
