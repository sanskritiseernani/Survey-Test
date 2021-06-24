import java.io.*;
import java.util.*;

public class TrueFalse extends MultipleChoice implements Serializable {

    private static final long serialVersionUID = 1L;

    public TrueFalse() {}

    // this is the constructor that is called when creating the object in MainDriver
    public TrueFalse(Input i, Output o) {
        this.questionInput = i;
        this.questionOutput = o;
        this.choices = new ArrayList<String>();
        this.create();
    }

    @Override
    public void create() {

        questionOutput.display("Enter the prompt for your True/False question: ");
        this.prompt = questionInput.listen();

        this.choices.add("T");
        this.choices.add("F");
    }

    public void display() {
        super.display();
    }

    @Override
    public void modifyChoices() {
        questionOutput.display("T/F Question choices cannot be modified! They are fixed.\n");
        return;
    }

    @Override
    public ResponseCorrectAnswer takeResponse() {

        String r;  // user's responses
        ResponseCorrectAnswer resp = new ResponseCorrectAnswer();

        this.display();  // display the question prompt

        r = questionInput.listen();

        while (!r.equals("1") && !r.equals("2")) {
            questionOutput.display("Please enter a valid input!\n");
            r = questionInput.listen();
        }

        resp.addText(r);

        return resp;
    }
}
