import utils.FileUtils;
import java.io.*;
import java.lang.*;
import java.lang.invoke.MutableCallSite;
import java.util.*;

public class Survey implements Serializable {

    protected transient Input surveyInput;
    protected transient Output surveyOutput;

    private static final long serialVersionUID = 1L;
    protected String dirPath;
    protected String filename;
    protected String fullPath;
    protected String responseDirPath;
    protected String surveyRespDirPath;

    protected String name;
    protected ArrayList<Question> questions = new ArrayList<Question>();

    public Survey() {}

    // this is the constructor that is called when creating the object in MainDriver
    public Survey(Input i, Output o) {
        this.surveyInput = i;
        this.surveyOutput = o;
        this.dirPath = "surveys" + File.separator;  // path for the directory that we are saving the surveys to
        this.surveyRespDirPath = "surveyResponses" + File.separator;  // path for directory that we're saving our survey responses to
    }

    public void thirdMenu() {

        List<String> menu3 = new ArrayList<String>();
        menu3.add("1) Add a new T/F question\n");
        menu3.add("2) Add a new multiple-choice question\n");
        menu3.add("3) Add a new short answer question\n");
        menu3.add("4) Add a new essay question\n");
        menu3.add("5) Add a new emoji question\n");
        menu3.add("6) Add a new matching question\n");
        menu3.add("7) Add a new Ranking question\n");

        surveyOutput.display("\nMenu 3\n");
        for (String s:menu3) {
            surveyOutput.display(s);
        }
    }

    public void create() {

        // all of this is fairly straightforward- it's error checking and making sure the program does not crash with invalid input
        int numQuestions;

        surveyOutput.display("Please enter a name for your Survey: \n");
        this.name = surveyInput.listen();

        while (this.name.length() < 1) {
            surveyOutput.display("Name cannot be blank!\n");
            this.name = surveyInput.listen();
        }

        surveyOutput.display("How many questions would you like to have?\n");
        String stringResponse = surveyInput.listen();

        // IO validation
        try {
            numQuestions = Integer.parseInt(stringResponse);

            while (numQuestions <= 0) {
                surveyOutput.display("Please enter a valid positive integer:\n");
                stringResponse = surveyInput.listen();
                numQuestions = Integer.parseInt(stringResponse);
            }

            for(int i=0; i <numQuestions; i++) {
                addQuestionsToSurvey();
                surveyOutput.display("\n***Question was successfully added***\n");
            }

            surveyOutput.display("\n***Survey was successfully created!***\n");

            FileUtils.createDirectory(this.surveyRespDirPath + this.name + "Responses" + File.separator);
        }
        catch (Exception e) {
            surveyOutput.display("Please enter a positive integer!\n");
            create();
        }

        responseDirPath = this.surveyRespDirPath + this.name + "Responses" + File.separator;
    }

    public void addQuestionsToSurvey() {

        thirdMenu();
        String userResponse = surveyInput.listen();

        switch (userResponse) {
            case "1":
                TrueFalse tf = new TrueFalse(surveyInput, surveyOutput);  // make object
                this.questions.add(tf);  // add to the list that stores all questions
                break;

            case "2":
                MultipleChoice mc = new MultipleChoice(surveyInput, surveyOutput);
                this.questions.add(mc);
                break;

            case "3":
                ShortAnswer sa = new ShortAnswer(surveyInput, surveyOutput);
                this.questions.add(sa);
                break;

            case "4":
                Essay essay = new Essay(surveyInput, surveyOutput);
                this.questions.add(essay);
                break;

            case "5":
                Emoji emoji = new Emoji(surveyInput, surveyOutput);
                this.questions.add(emoji);
                break;

            case "6":
                Matching matching = new Matching(surveyInput, surveyOutput);
                this.questions.add(matching);
                break;

            case "7":
                Ranking ranking = new Ranking(surveyInput, surveyOutput);
                this.questions.add(ranking);
                break;

            default:  // IO validation
                surveyOutput.display("Please select a valid positive integer option from the menu!\n");
                addQuestionsToSurvey();
                break;
        }
    }

    /* Using the code provided by Sean Grimes for Serialization here */
    public void save() {

        filename = getName();

        while (checkFileExists(filename)) {
            surveyOutput.display("Please re-name your survey/test since a file with the current name already exists!:\n");
            this.name = surveyInput.listen();
            filename = getName();
        }

        fullPath = dirPath + filename;
        FileUtils.createDirectory(dirPath);

        try{
            FileOutputStream fos = new FileOutputStream(fullPath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(this);

            oos.close();
            fos.close();
        }
        catch(IOException e){
            e.printStackTrace();
            System.exit(2);
        }
    }

    /* Using the code provided by Sean Grimes for de-Serialization here */
    public Survey load() {

        String[] filenames;
        File f = new File(dirPath);  // Directory object
        filenames = f.list();  // listing all the files present in directory f

        if (filenames == null)
        {
            this.surveyOutput.display("Please create a survey first!\n");
            return null;
        }

        int i = 1;

        // printing out all the filenames in the directory for the user to pick from
        for (String x: filenames) {
            surveyOutput.display(i + ") " + x + "\n");
            i++;
        }

        int num = -1;
        while (num <= 0 || num > filenames.length) {
            surveyOutput.display("Please enter the number corresponding to the name of the file you want to load: \n");
            String stringResponse = surveyInput.listen();
            try {
                num = Integer.parseInt(stringResponse);

            } catch (NumberFormatException e) {}
        }

        filename = filenames[num-1];

        fullPath = dirPath + filename;

        File tst = new File(fullPath);
        if(!tst.exists() || !tst.isFile())
            throw new IllegalArgumentException(fullPath + " is invalid");

        // Create the generic object that will hold the deserialized object
        Survey deserializedObject = null;

        try{
            // Streams to read it in
            FileInputStream fis = new FileInputStream(fullPath);
            ObjectInputStream ois = new ObjectInputStream(fis);

            // Read it in with the cast to specific type
            deserializedObject =  (Survey) ois.readObject();

            // Cleanup
            ois.close();
            fis.close();
            deserializedObject.fixIO(surveyInput, surveyOutput);
        }
        catch(IOException | ClassNotFoundException e){
            surveyOutput.display("Survey not found! Exiting program..\n");
            e.printStackTrace();
            System.exit(2);
        }
        return deserializedObject;
    }

    public void display() {

        surveyOutput.display("***** SURVEY: " + this.name + " *****\n\n");

        for (Question x: questions) {
            x.display();
            surveyOutput.display("\n\n");
        }

        surveyOutput.display("-----End of Survey-----\n");
    }

    public boolean checkFileExists(String f) {
        String temp = dirPath + f;
        boolean check = new File(temp).exists();
        return check;
    }

    public void fixIO(Input i, Output o) {
        this.surveyInput = i;
        this.surveyOutput = o;

        for (Question x: questions) {
            x.fixIO(i,o);
        }
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    /* Part C */

    public void take() {

        if (!checkFileExists(getName())) {
            surveyOutput.display("\nCannot take Survey that has not been saved. Please save survey first!\n");
            return;
        }

        int counter = 1;
        ResponseSheet rs = new ResponseSheet(this.name, this.responseDirPath);
        ResponseCorrectAnswer currentResponse;

        surveyOutput.display("\n**** SURVEY: " + this.name + " ****\n");
        for (Question q: questions) {
            surveyOutput.display("\n" + counter + ". ");
            currentResponse = q.takeResponse();
            rs.addRCA(currentResponse);
            counter++;
        }
        rs.save();
        surveyOutput.display("\n----End of Survey----\n");
        surveyOutput.display("\n*** Survey responses were saved ***\n");
    }

    public void tabulate() {

        ResponseSheet helperResponseSheet = new ResponseSheet(this.name, this.responseDirPath);
        ArrayList<ResponseSheet> listOfSheets = helperResponseSheet.loadAll();

        ArrayList<String> spottedResponses = new ArrayList<>();
        ArrayList<Integer> responseFrequency = new ArrayList<>();
        int helperI = 0;
        int helperJ = 0;

        if (listOfSheets == null) {
            surveyOutput.display("There are no responses recorded for this survey.\n");
            return;
        }

        // for each question
        for (int i = 0; i < this.questions.size(); i++) {

            surveyOutput.display("\nQuestion: \n");
            this.questions.get(i).display();

            surveyOutput.display("\n\nTabulation: \n");

            // (also, clean up the spottedResponses and responseFrequency Lists)
            spottedResponses = new ArrayList<>();
            responseFrequency = new ArrayList<>();

            // loop through all of our ResponseSheets
            for (int j = 0; j < listOfSheets.size(); j++) {

                // and for each ResponseSheet j, get the respective response to Question i
                ResponseCorrectAnswer currRCA = listOfSheets.get(j).getRCA(i);

                // and for each response, fetch each String that makes up the response
                for (int k = 0; k < currRCA.getTexts().size(); k++) {

                    String currString = currRCA.getTexts().get(k);
                    //surveyOutput.display(currString);

                    // If we've seen this response before,
                    if (spottedResponses.contains(currString)) {
                        // find its index in our spotted List and record that index.
                        helperI = spottedResponses.indexOf(currString);
                        // Then, use that index to fetch the frequency number for how many times we've seen that response.
                        helperJ = responseFrequency.get(helperI);
                        // Then, increment it by 1.
                        helperJ++;
                        // Finally, reset the respective frequency to the new value.
                        responseFrequency.set(helperI, helperJ);
                    }
                    // If we've never seen this response,
                    else {
                        // Add it our spotted List as a new entry,
                        spottedResponses.add(currString);
                        // as well as to our frequency List (These entries should have the same index).
                        responseFrequency.add(1);
                    }
                }
            }
            // Once we are done counting all of the responses,
            Question q = questions.get(i);
            // for any question that contains options,
             if (q instanceof  TrueFalse || q instanceof MultipleChoice || q instanceof Emoji) {
                 // loop through the options,
                 for (int m = 0; m < q.getChoices().size(); m++) {
                    String helperS = Integer.toString(m+1);
                    // and check if the options were ever selected.
                    if (spottedResponses.contains(helperS)) {
                        helperI = spottedResponses.indexOf(helperS);
                        helperJ = responseFrequency.get(helperI);
                        // If they were, fetch the frequency.
                        this.surveyOutput.display(q.getChoices().get(m) + " : " + helperJ + "\n");
                    }
                    else {
                        // If they weren't, say 0.
                        this.surveyOutput.display(q.getChoices().get(m) + " : " + 0 + "\n");
                    }
                }

            }
            else if (q instanceof ShortAnswer || q instanceof Essay) {
                for (int o = 0; o < spottedResponses.size(); o++) {
                    this.surveyOutput.display(spottedResponses.get(o) + " : " + responseFrequency.get(o) + "\n");
                }
            }
             else if (q instanceof Ranking) {

                 char letterOption = 'A'; // Beginning of our options
                 // loop through the options,
                 for (int g = 0; g < q.getChoices().size(); g++) {

                     // helperS : store the user choice, in this case, something like A1 or C2
                     // At this line, we're looping through the letter options

                     String helperS = Character.toString(letterOption);

                     for (int h = 1; h <= q.getChoices().size(); h++) {

                         // This creates the letter-number combination response option (A1, C2, etc)
                         helperS = helperS + Integer.toString(h);

                         // and check if the options were ever selected.
                         if (spottedResponses.contains(helperS)) {
                             helperI = spottedResponses.indexOf(helperS);
                             helperJ = responseFrequency.get(helperI);
                             // If they were, fetch the frequency.
                             this.surveyOutput.display(helperS + " : " + helperJ + "\n");
                         }
                         else {
                             // If they weren't, say 0.
                             this.surveyOutput.display(helperS + " : " + 0 + "\n");
                         }
                         helperS = Character.toString(letterOption);
                     }
                     letterOption++;
                 }
             }
            else if (q instanceof Matching) {

                 char letterOption = 'A'; // Beginning of our options
                 // loop through the options,
                 for (int m = 0; m < q.getChoices().size()/2; m++) {

                     // helperS : store the user choice, in this case, something like A1 or C2
                     // At this line, we're looping through the letter options

                     String helperS = Character.toString(letterOption);

                     for (int n = 1; n <= q.getChoices().size()/2; n++) {

                         // This creates the letter-number combination response option (A1, C2, etc)
                         helperS = helperS + Integer.toString(n);

                         // and check if the options were ever selected.
                         if (spottedResponses.contains(helperS)) {
                             helperI = spottedResponses.indexOf(helperS);
                             helperJ = responseFrequency.get(helperI);
                             // If they were, fetch the frequency.
                             this.surveyOutput.display(helperS + " : " + helperJ + "\n");
                         }
                         else {
                             // If they weren't, say 0.
                             this.surveyOutput.display(helperS + " : " + 0 + "\n");
                         }
                         helperS = Character.toString(letterOption);
                     }
                     letterOption++;
                 }
            }

        }
    }

    public void modify(int n) {
        String option;
        String updatedPrompt;

        Question modifiedQ = this.questions.get(n-1);

        surveyOutput.display("Do you wish to modify the prompt?\nEnter \'1\' for YES, \'2\' for NO:\n");
        option = surveyInput.listen();

        while (!option.equals("1") && !option.equals("2")) {
            surveyOutput.display("Please enter a valid choice of \'1\' for YES or \'2\' for NO\n");
            option = surveyInput.listen();
        }

        // if the user modifies the prompt, this block of code asks them if they want to modify the choices after they are done
        // modifying the prompt
        if (option.equals("1")) {

            surveyOutput.display(modifiedQ.getPrompt());
            surveyOutput.display("\nEnter a new prompt:\n");
            updatedPrompt = surveyInput.listen();

            modifiedQ.setPrompt(updatedPrompt);

            surveyOutput.display("\nSurvey/Test " +this.name+ " was modified!\n");

            if (modifiedQ instanceof MultipleChoice || modifiedQ instanceof Matching) {

                surveyOutput.display("\nDo you wish to modify the choices?\nEnter \'1\' for YES, \'2\' for NO:\n");
                option = surveyInput.listen();

                while (!option.equals("1") && !option.equals("2")) {
                    surveyOutput.display("Please enter a valid choice of \'1\' for YES or \'2\' for NO\n");
                    option = surveyInput.listen();
                }

                if (option.equals("1")) {
                    modifiedQ.modifyChoices();
                }

                if (option.equals("2")) {
                    return;
                }
            }
        }

        // if the user decides not to modify the prompt, this block of code asks them if they want to modify the choices
        if (option.equals("2")) {
            if (modifiedQ instanceof MultipleChoice || modifiedQ instanceof Matching) {

                surveyOutput.display("\nDo you wish to modify the choices?\nEnter \'1\' for YES, \'2\' for NO:\n");
                option = surveyInput.listen();

                while (!option.equals("1") && !option.equals("2")) {
                    surveyOutput.display("Please enter a valid choice of \'1\' for YES or \'2\' for NO\n");
                    option = surveyInput.listen();
                }

                if (option.equals("1")) {
                    modifiedQ.modifyChoices();
                }

                if (option.equals("2")) {
                    return;
                }
            }
        }
    }

    public void grade() {}

}
