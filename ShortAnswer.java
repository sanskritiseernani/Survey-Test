import java.io.*;

public class ShortAnswer extends Essay implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String length;  // user input for how long the essay should be
    protected int numResponses;

    public ShortAnswer() {}

    // this is the constructor that is called when creating the object in MainDriver
    public ShortAnswer(Input i, Output o) {
        this.questionInput = i;
        this.questionOutput = o;
        this.create();
    }

    @Override
    public void create() {

        questionOutput.display("Enter a prompt for your Short Answer Question: ");
        this.prompt = questionInput.listen();

        String stringResponse = "";

        int num = -1;
        while (num < 0) {
            questionOutput.display("Enter the length of the user's response to your Short Answer Question: ");
            stringResponse = questionInput.listen();
            try {
                num = Integer.parseInt(stringResponse);

            } catch (NumberFormatException e) {
            }
        }

        this.length = stringResponse;

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

    @Override
    public void display() {
        questionOutput.display("Length of short answer: " + getLength() + "\n\n");
        super.display();
    }

    public void setLength(String l) {
        this.length = l;
    }

    public String getLength() {
        return this.length;
    }

    @Override
    public ResponseCorrectAnswer takeResponse() {

        String r;  // user's responses
        ResponseCorrectAnswer resp = new ResponseCorrectAnswer();

        this.display();  // display the question prompt

        // if there are multiple responses required to answer the question
        if (this.numResponses > 1) {

            char c = 'A';
            for (int i=1; i <= this.numResponses; i++) {
                questionOutput.display("\n" + c + ") \n");
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
