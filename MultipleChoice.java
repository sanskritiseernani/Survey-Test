import java.io.*;
import java.util.*;

public class MultipleChoice extends Question implements Serializable {

    private static final long serialVersionUID = 1L;

    protected ArrayList<String> choices;
    protected int numChoices;
    protected int numResponses;

    public MultipleChoice() {}

    // this is the constructor that is called when creating the object in MainDriver
    public MultipleChoice(Input i, Output o) {
        this.questionInput = i;
        this.questionOutput = o;
        this.choices = new ArrayList<String>();
        this.create();
    }

    public ArrayList<String> getChoices() {
        return this.choices;
    }


    public void setChoices(int i, String s) {
        this.choices.set(i, s);
    }

    @Override
    public void create() {

        questionOutput.display("Enter the prompt for your multiple-choice question: ");
        this.prompt = questionInput.listen();

        super.create();

        questionOutput.display("Enter the number of choices for your multiple-choice question: ");
        String stringResponse = questionInput.listen();

        this.numChoices = -1;

        while(numChoices < 0) {
            try {
                this.numChoices = Integer.parseInt(stringResponse);
            } catch (NumberFormatException e) {
                questionOutput.display("Please enter a valid positive integer!: ");
                stringResponse = questionInput.listen();
            }
        }

        for (int i=0; i < numChoices; i++) {
            String option;
            questionOutput.display("Enter in choice #" + (i+1) + " ");
            option = questionInput.listen();
            this.choices.add(option);
        }

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

    public void display() {
        questionOutput.display(getPrompt() + "\n\n");

        int choiceNum = 1;

        for (String c: getChoices()) {
            questionOutput.display(choiceNum + ") " + c);
            questionOutput.display("   ");
            choiceNum++;
        }
    }

    @Override
    public void modifyChoices() {
        int counter = 1;
        int n;
        String stringResponse;
        String updatedChoice;

        questionOutput.display("Which choice do you wish to modify?\n");

        for (String c: getChoices()) {
            questionOutput.display(counter + ") " + c + "   ");
            counter++;
        }

        stringResponse = questionInput.listen();

        try {
            n = Integer.parseInt(stringResponse);
            questionOutput.display("\nEnter your updated choice:\n");
            updatedChoice = questionInput.listen();
            this.choices.set(n-1, updatedChoice);

            questionOutput.display("\nSurvey was modified!\n");
        }
        catch (Exception e) {
            modifyChoices();
        }
    }

    public ResponseCorrectAnswer takeResponse() {

        String r;  // user's responses
        ResponseCorrectAnswer resp = new ResponseCorrectAnswer();

        this.display();  // display the question prompt

        // if there are multiple responses required to answer the question
        if (this.numResponses > 1) {

            questionOutput.display("Please Enter " + this.numResponses + " choices:\n");
            char c = 'A';
            for (int i=0; i < this.numResponses; i++) {
                questionOutput.display("\n" + c + ") ");
                r = questionInput.listen();
                int num= -1;
                while (num < 1) {
                    try {
                        num = Integer.parseInt(r);
                        resp.addText(r);

                    } catch (Exception e) {
                        questionOutput.display("Invalid input! Please start over!\n");
                        System.exit(0);
                    }
                }
                c++;
            }
        }
        // if only one response if required to answer the question
        else {

            int x = -1;
            while (x < 1) {
                r = questionInput.listen();
                try {
                    x = Integer.parseInt(r);
                    resp.addText(r);
                } catch (Exception e) {
                    questionOutput.display("Invalid input! Please start over!\n");
                    System.exit(0);
                }
            }
        }
        return resp;
    }

    public int getNumChoices() {
        return this.numChoices;
    }

    public int getNumAnswerChoices() {return this.numResponses;}
}
