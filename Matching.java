import java.io.*;
import java.util.*;

public class Matching extends Question implements Serializable {

    private static final long serialVersionUID = 1L;
    protected int numChoices;
    protected ArrayList<String> choices;

    public Matching(Input i, Output o) {
        this.questionInput = i;
        this.questionOutput = o;
        this.choices = new ArrayList<String>();
        this.create();
    }

    public Matching() {}

    public ArrayList<String> getChoices() {
        return this.choices;
    }

    public void setChoices(int i, String s) {
        this.choices.set(i, s);
    }

    @Override
    public void create() {

        questionOutput.display("Enter the prompt for your Matching question: ");
        this.prompt = questionInput.listen();
        super.create();

        questionOutput.display("Enter the number of choices you want your Matching question to have: ");
        String stringResponse = questionInput.listen();

        numChoices = -1;
        while(numChoices <= 0) {
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

            questionOutput.display("Enter matching choice: ");
            option = questionInput.listen();
            this.choices.add(option);
        }
    }

    public void display() {
        questionOutput.display(getPrompt() + "\n\n");

        int choiceNum = 0;
        char letter = 'A';
        int i;

        for (i=1; i <= choices.size()/2; i++) {
            questionOutput.display(letter + ") " + choices.get(choiceNum) + "     " + i + ") " +choices.get(choiceNum+1) + " \n");
            choiceNum += 2;
            letter++;
        }
    }

    @Override
    public void modifyChoices() {
        int index = 0;
        int n;
        String stringResponse;
        String updatedChoice;

        questionOutput.display("Which choice do you wish to modify?\n");

        for (int i=1; i <= choices.size()/2; i++) {
            questionOutput.display(i + ") " + choices.get(index) + "   " + choices.get(index+1) + "    ");
            index += 2;
        }

        stringResponse = questionInput.listen();

        try {
            n = Integer.parseInt(stringResponse);
            questionOutput.display("Enter Updated choice:\n");
            updatedChoice = questionInput.listen();
            this.choices.set(n-1, updatedChoice);

            questionOutput.display("Enter Updated matching choice:\n");
            updatedChoice = questionInput.listen();
            this.choices.set(n, updatedChoice);
        }
        catch (Exception e) {
            modifyChoices();
        }

    }

    public ResponseCorrectAnswer takeResponse() {

        List<Integer> alreadyPicked = new ArrayList<>(); // options the user has already picked
        int rNum = -1; // if the user picked a number, store it here | -1 because valid response can never be -1

        String r;  // raw user responses
        ResponseCorrectAnswer resp = new ResponseCorrectAnswer(); // compiled user responses
        char letterOption = 'A';

        this.display();  // display the question prompt

        questionOutput.display("\n\nEnter your number match below: \n\n");

        for (int i=0; i < this.numChoices; i++) {

            while (true) {
                questionOutput.display(letterOption + ") ");
                r = questionInput.listen();

                try {
                    rNum = Integer.parseInt(r);
                }
                catch (NumberFormatException e){
                    questionOutput.display("Invalid Input! Must be an Integer! \n");
                    continue;
                }
                if (rNum < 1 || rNum > this.numChoices) {
                    questionOutput.display("Invalid Input! Must be between 1 and "+this.numChoices+"! \n");
                }
                else if (alreadyPicked.contains(rNum)) {
                    questionOutput.display("Invalid Input! "+rNum+" was already used! \n");
                }
                else {
                    alreadyPicked.add(rNum);
                    r = letterOption + r; // Stores the response as the letter and the number provided from the user combined

                    resp.addText(r);
                    questionOutput.display("\n");
                    letterOption++;
                    break;
                }
            }



        }

        return resp;

    }

    public int getNumChoices() {
        return this.numChoices;
    }
}
