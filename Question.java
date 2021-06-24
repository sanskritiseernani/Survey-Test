import java.io.Serializable;
import java.util.*;

abstract class Question implements Serializable {

    // I use the transient keyword every time my I/O attributes are involved to ensure serialization and de-serialization happen properly
    protected transient Input questionInput;
    protected transient Output questionOutput;

    private static final long serialVersionUID = 1L;
    protected String prompt;
    protected ResponseCorrectAnswer response; // aggregating rca

    public String getPrompt() {
        return this.prompt;
    }

    public void setPrompt(String p) {
        this.prompt = p;
    }

    public ArrayList<String> getChoices() {
        return new ArrayList<String>();
    }

    public void create() {
        while (this.prompt.length() < 1 ) {
            this.prompt = questionInput.listen();
        }
    }

    //  method to update the IO post de-serialization so that there are no errors when trying to display survey/test
    public void fixIO(Input i, Output o) {
        this.questionInput = i;
        this.questionOutput = o;
    }

    public abstract void display();

    public void modifyChoices() {}

    /* Part C */

    public abstract ResponseCorrectAnswer takeResponse();

}

