import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Ranking extends Matching implements Serializable {

    private static final long serialVersionUID = 1L;
    protected int numChoices;
    protected ArrayList<String> choices;

    public Ranking() {}

    public Ranking(Input i, Output o) {
        this.questionInput = i;
        this.questionOutput = o;
        this.choices = new ArrayList<String>();
        this.create();
    }

    public ArrayList<String> getChoices() {
        return this.choices;
    }

    @Override
    public void create() {

        questionOutput.display("Enter the prompt for your Ranking question: ");
        this.prompt = questionInput.listen();
//        super.create();

        questionOutput.display("Enter the number of choices you want your Ranking question to have: ");
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
        }
    }

    public void display() {
        questionOutput.display(getPrompt() + "\n\n");

        char letterOption = 'A';
        int i;

        for (String c: this.getChoices()) {
            questionOutput.display(letterOption + ") " + c + "   ");
            letterOption++;
        }
    }

    @Override
    public void modifyChoices() {
        int index = 0;
        int n;
        String stringResponse;
        String updatedChoice;

        questionOutput.display("Which choice do you wish to modify?\n");

        for (int i=1; i <= this.choices.size(); i++) {
            questionOutput.display(i + ") " + choices.get(index));
            index += 1;
        }

        stringResponse = questionInput.listen();

        try {
            n = Integer.parseInt(stringResponse);
            questionOutput.display("Enter Updated choice:\n");
            updatedChoice = questionInput.listen();
            this.choices.set(n-1, updatedChoice);
        }
        catch (Exception e) {
            modifyChoices();
        }

    }

    public ResponseCorrectAnswer takeResponse() {

        List<Integer> alreadyPicked = new ArrayList<>(); // options the user has already picked
        int rNum = -1; // if the user picked a number, store it here | -1 because valid response can never be -1

        String r;  // user's responses
        ResponseCorrectAnswer resp = new ResponseCorrectAnswer();
        char letterOption = 'A';
//        int i = 1;

        this.display();  // display the question prompt

        questionOutput.display("\n\nEnter your rankings below: \n\n");


        for (int i=0; i < this.numChoices; i++) {

            while (true) {
                questionOutput.display(letterOption + ") ");
                r = questionInput.listen();

                try {
                    rNum = Integer.parseInt(r);
                } catch (NumberFormatException e) {
                    questionOutput.display("Invalid Input! Must be an Integer! \n");
                    continue;
                }
                if (rNum < 1 || rNum > this.numChoices) {
                    questionOutput.display("Invalid Input! Must be between 1 and " + this.numChoices + "! \n");
                } else if (alreadyPicked.contains(rNum)) {
                    questionOutput.display("Invalid Input! " + rNum + " was already used! \n");
                } else {
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



