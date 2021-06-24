import java.io.*;

public class Essay extends Question implements Serializable {

    private static final long serialVersionUID = 1L;
    protected int numResponses;

    public Essay() {}  // blank constructor

    // this is the constructor that is called when creating the object in MainDriver
    public Essay(Input i, Output o) {
        this.questionInput = i;
        this.questionOutput = o;
        this.create();
    }

    // open ended essay question and so no choices for user to pick from
    @Override
    public void create() {
        questionOutput.display("Enter a prompt for your Essay Question: ");
        this.prompt = questionInput.listen();
        super.create();

        // multiple responses/answers functionality
        String helperResponse = "";
        int helperIntResponse = -1;
        while (helperIntResponse < 1)
        {
            this.questionOutput.display("Please enter the number of responses required to fully answer the question:\n");
            helperResponse = questionInput.listen();

            try {
                helperIntResponse = Integer.parseInt(helperResponse);

            } catch (NumberFormatException e) {}
        }
        this.numResponses = helperIntResponse;
    }

    public void display() {
        questionOutput.display(getPrompt() + "\n\n");
    }

    public ResponseCorrectAnswer takeResponse() {

        String r;  // user's responses
        ResponseCorrectAnswer resp = new ResponseCorrectAnswer();

        this.display();  // display the question prompt

        // if there are multiple responses required to answer the question
        if (this.numResponses > 1) {

            char c = 'A';
            for (int i=0; i < this.numResponses; i++) {
                questionOutput.display("\n" + (c) + ") \n");
                r = questionInput.listen();
                resp.addText(r);
                c++;
            }
        }
        // if only one response if required to answer the question
        else {
            r = questionInput.listen();
            resp.addText(r);
        }

        return resp;
    }
}
