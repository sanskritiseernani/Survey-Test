import java.io.*;
import java.util.*;

public class Emoji extends MultipleChoice implements Serializable {

    private static final long serialVersionUID = 1L;

    List<String> emojiChoices;
    protected int numChoices = 5;  // emoji choices are fixed
    protected int numResponses;

    public Emoji() {}  // blank constructor

    // this is the constructor that is called when creating the object in MainDriver
    public Emoji(Input i, Output o) {
        this.questionInput = i;
        this.questionOutput = o;
        this.emojiChoices = new ArrayList<String>();
        this.create();
    }

    // pretty straightforward. It takes the prompt from the user and then prints a list of options

    @Override
    public void create() {

        emojiChoices = new ArrayList<String>();
        questionOutput.display("Enter the prompt for your Emoji question: ");
        this.prompt = questionInput.listen();

        emojiChoices.add("1) Smiles");
        emojiChoices.add("2) Frowns");
        emojiChoices.add("3) Angry");
        emojiChoices.add("4) Surprised");
        emojiChoices.add("5) Sad");

        // This section is to get the number of options the user needs to input to be a fully answered question
        String helperResponse = "";
        int helperIntResponse = -1;
        while (helperIntResponse < 1 || helperIntResponse > this.numChoices)
        {
            this.questionOutput.display("Please enter the number of options to fully answer the question:\n");
            helperResponse = questionInput.listen();

            try {
                helperIntResponse = Integer.parseInt(helperResponse);

            } catch (NumberFormatException e) {}
        }
        this.numResponses = helperIntResponse;
    }

    public void printEmoji() {

        for (String e: emojiChoices) {
            questionOutput.display(e);
            questionOutput.display("   ");
        }
    }

    @Override
    public void display() {
        questionOutput.display(getPrompt() + "\n\n");
        printEmoji();
    }

    @Override
    public void modifyChoices() {
        questionOutput.display("Emoji Question choices cannot be modified! They are fixed.\n");
        return;
    }

    @Override
    public ResponseCorrectAnswer takeResponse() {

        String r;  // user's responses
        ResponseCorrectAnswer resp = new ResponseCorrectAnswer();

        this.display();  // display the question prompt

        // if there are multiple responses required to answer the question
        if (this.numResponses > 1) {

            for (int i=0; i < this.numResponses; i++) {
                questionOutput.display("\nEnter response " + (i+1) + "- ");
                r = questionInput.listen();
                while (!r.equals("1") && !r.equals("2") && !r.equals("3") && !r.equals("4") && !r.equals("5")) {
                    questionOutput.display("Please enter a valid input!\n");
                    r = questionInput.listen();
                }
                resp.addText(r);
            }
        }
        // if only one response if required to answer the question
        else {
            r = questionInput.listen();
            while (!r.equals("1") && !r.equals("2") && !r.equals("3") && !r.equals("4") && !r.equals("5")) {
                questionOutput.display("Please enter a valid input!\n");
                r = questionInput.listen();
            }

            resp.addText(r);
        }
        return resp;
    }
}
