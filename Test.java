import java.io.*;
import java.util.*;
import java.lang.*;
import utils.FileUtils;

public class Test extends Survey implements Serializable {

    private static final long serialVersionUID = 1L;
    protected String dirPath;
    protected String filename;
    protected String fullPath;
    protected String responseDirPath;
    protected String testRespDirPath;

    protected ArrayList<ResponseCorrectAnswer> correctAnswers = new ArrayList<ResponseCorrectAnswer>();

    public Test() {}

    // this is the constructor that is called when creating the object in MainDriver
    public Test(Input i, Output o) {
        this.surveyInput = i;
        this.surveyOutput = o;
        this.dirPath = "tests" + File.separator;  // path for the directory that we are saving the tests to
        this.testRespDirPath = "testResponses" + File.separator;
    }

    @Override
    public void create() {

        int numQuestions;

        // all of this is fairly straightforward- it's error checking and making sure the program does not crash with invalid input
        surveyOutput.display("Please enter a name for your Test: \n");
        this.name = surveyInput.listen();

        while (this.name.length() < 1) {
            surveyOutput.display("Name cannot be blank!\n");
            this.name = surveyInput.listen();
        }

        surveyOutput.display("How many questions would you like to have?\n");
        String stringResponse = surveyInput.listen();

        try {
            numQuestions = Integer.parseInt(stringResponse);

            while (numQuestions <= 0) {
                surveyOutput.display("Please enter a valid positive integer.\n");
                stringResponse = surveyInput.listen();
                numQuestions = Integer.parseInt(stringResponse);
            }

            for(int i=0; i <numQuestions; i++) {
                addQuestionsToTest();
                surveyOutput.display("\n***Question was successfully added***\n");
            }

            surveyOutput.display("\n***Test was successfully created!***\n");

            FileUtils.createDirectory(this.testRespDirPath + this.name + "Responses" + File.separator);
        }
        catch (Exception e) {
            surveyOutput.display("Please enter a positive integer!\n");
            create();
        }

        this.responseDirPath = this.testRespDirPath + this.name + "Responses" + File.separator;
    }

    // Technically adding both a question and its respective answer to this test
    public void addQuestionsToTest() {

        thirdMenu();
        String userResponse = surveyInput.listen();


        // Helper variables for error-checking within each case
        ArrayList<String> helperList = new ArrayList<String>();
        String helperResponse = "";
        int helperIntResponse = -1;
        int helperIntResponse2 = -1;
        int helperSize = -1;
        int helperCount = -1;
        ArrayList<String> helperPickedNumbers = new ArrayList<String>();
        String[] helperNumberPair = {"-1", "-1"};
        ResponseCorrectAnswer correctAnswer = new ResponseCorrectAnswer();

        List<Integer> alreadyPicked = new ArrayList<>(); // options the user has already picked
        int rNum = -1; // if the user picked a number, store it here | -1 because valid response can never be -1

        String r;  // user's responses
        char letterOption = 'A';

        switch (userResponse) {
            case "1":

                TrueFalse tf = new TrueFalse(surveyInput, surveyOutput);
                this.questions.add(tf);

                helperList = tf.getChoices();
                helperSize = helperList.size();

                for (int i = 0; i < helperSize; i++) {
                    this.surveyOutput.display(Integer.toString(i+1) + ") " + helperList.get(i) + "\n");
                }

                while( helperIntResponse <= 0 || helperIntResponse > helperSize ) {
                    surveyOutput.display("Please enter the number corresponding to the correct answer:\n");
                    helperResponse = surveyInput.listen();

                    try {
                        helperIntResponse = Integer.parseInt(helperResponse);

                    } catch (NumberFormatException e) {}
                }

                correctAnswer.addText(helperResponse);
                this.correctAnswers.add(correctAnswer);

                break;

            case "2":
                MultipleChoice mc = new MultipleChoice(surveyInput, surveyOutput);
                this.questions.add(mc);

                // helperList gets the options of the question we just added
                helperList = mc.getChoices();
                helperSize = helperList.size();
                helperCount = mc.getNumAnswerChoices();

                for (int i = 0; i < helperSize; i++) {
                    this.surveyOutput.display(Integer.toString(i+1) + ") " + helperList.get(i) + "\n");
                }

                for (int i = 0; i < helperCount; i++) {

                    while (correctAnswer.getTexts().contains(helperResponse) || (helperIntResponse < 1 || helperIntResponse > helperSize)) {
                        surveyOutput.display("Please enter the number corresponding to the correct answer (no repetitions):\n");
                        helperResponse = surveyInput.listen();

                        try {
                            helperIntResponse = Integer.parseInt(helperResponse);

                        } catch (NumberFormatException e) {
                            surveyOutput.display("Invalid input! Must be a number!\n");
                        }

                    }
                    correctAnswer.addText(helperResponse);
                    this.surveyOutput.display("Answer added!\n");
                    helperIntResponse = -1;
                }
                this.correctAnswers.add(correctAnswer);

                break;

            case "3":
                ShortAnswer sa = new ShortAnswer(surveyInput, surveyOutput);
                this.questions.add(sa);

                while( helperResponse.length() < 1 ) {
                    surveyOutput.display("Please enter what should be the correct answer:\n");
                    helperResponse = surveyInput.listen();
                }

                correctAnswer.addText(helperResponse);
                this.correctAnswers.add(correctAnswer);

                break;

            case "4":
                Essay essay = new Essay(surveyInput, surveyOutput);
                this.questions.add(essay);

                // The program will still add a "Correct Response" just to keep the correctAnswers ArrayList the same
                // length as the questions ArrayList so as to maintain the ability to index both ArrayLists with the
                // same value.
                correctAnswer.addText("");
                this.correctAnswers.add(correctAnswer);
                break;

            case "5":
                Emoji emoji = new Emoji(surveyInput, surveyOutput);
                this.questions.add(emoji);

                // What's written for the Essay case applies here as well.
                correctAnswer.addText("");
                this.correctAnswers.add(correctAnswer);
                break;

            case "6":
                Matching matching = new Matching(surveyInput, surveyOutput);
                this.questions.add(matching);

                // helperList gets the options of the question we just added
                helperList = matching.getChoices();
                helperSize = helperList.size();
                helperCount = matching.getNumChoices(); // matching's numChoices

                alreadyPicked = new ArrayList<>(); // options the user has already picked
                rNum = -1; // if the user picked a number, store it here | -1 because valid response can never be -1

                r = "";  // user's responses
                letterOption = 'A';

                matching.display();  // display the question prompt

                surveyOutput.display("\n\nEnter the correct matching below: \n\n");


                for (int j=0; j < helperCount; j++) {

                    while (true) {
                        surveyOutput.display(letterOption + ") ");
                        r = surveyInput.listen();

                        try {
                            rNum = Integer.parseInt(r);
                        } catch (NumberFormatException e) {
                            this.surveyOutput.display("Invalid Input! Must be an Integer! \n");
                            continue;
                        }
                        if (rNum < 1 || rNum > helperCount) {
                            this.surveyOutput.display("Invalid Input! Must be between 1 and " + helperCount + "! \n");
                        } else if (alreadyPicked.contains(rNum)) {
                            this.surveyOutput.display("Invalid Input! " + rNum + " was already used! \n");
                        } else {
                            alreadyPicked.add(rNum);
                            r = letterOption + r; // Stores the response as the letter and the number provided from the user combined

                            correctAnswer.addText(r);
                            this.surveyOutput.display("\n");
                            letterOption++;
                            break;
                        }
                    }
                }

                this.correctAnswers.add(correctAnswer); // Adding the current RCA to the RCA List

                break;

            case "7":

                Ranking ranking = new Ranking(surveyInput, surveyOutput);
                this.questions.add(ranking);

                // helperList gets the options of the question we just added
                helperList = ranking.getChoices();
                helperSize = helperList.size();
                helperCount = ranking.getNumChoices(); // matching's numChoices


                alreadyPicked = new ArrayList<>(); // options the user has already picked
                rNum = -1; // if the user picked a number, store it here | -1 because valid response can never be -1

                r = "";  // user's responses
                letterOption = 'A';

                ranking.display();  // display the question prompt

                surveyOutput.display("\n\nEnter the correct rankings below: \n\n");

                for (int j=0; j < helperCount; j++) {

                    while (true) {
                        surveyOutput.display(letterOption + ") ");
                        r = surveyInput.listen();

                        try {
                            rNum = Integer.parseInt(r);
                        } catch (NumberFormatException e) {
                            this.surveyOutput.display("Invalid Input! Must be an Integer! \n");
                            continue;
                        }
                        if (rNum < 1 || rNum > helperCount) {
                            this.surveyOutput.display("Invalid Input! Must be between 1 and " + helperCount + "! \n");
                        } else if (alreadyPicked.contains(rNum)) {
                            this.surveyOutput.display("Invalid Input! " + rNum + " was already used! \n");
                        } else {
                            alreadyPicked.add(rNum);
                            r = letterOption + r; // Stores the response as the letter and the number provided from the user combined

                            correctAnswer.addText(r);
                            this.surveyOutput.display("\n");
                            letterOption++;
                            break;
                        }
                    }
                }

                this.correctAnswers.add(correctAnswer); // Adding the current RCA to the RCA List

                break;

            default:
                surveyOutput.display("Please select a valid positive integer option from the menu!\n");
                addQuestionsToSurvey();
                break;
        }
    }

    @Override
    public void display() {

        surveyOutput.display("***** Test: " + getName() + " *****\n\n");

        int i = 0;
        for (Question x: questions) {
            x.display();
            surveyOutput.display("\nThe correct answer is: " + this.correctAnswers.get(i).getTexts());
            surveyOutput.display("\n\n");
            i++;
        }

        surveyOutput.display("-----End of Test-----\n");
    }

    /* Using the code provided by Sean Grimes for Serialization here */
    @Override
    public void save() {

        filename = getName();
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
    @Override
    public Test load() {
        String[] filenames;
        File f = new File(dirPath);
        filenames = f.list();
        if (filenames == null)
        {
            this.surveyOutput.display("Please create a test first!\n");
            return null;
        }

        int i = 1;
        for (String x: filenames) {
            surveyOutput.display(i + ") " + x + "\n");
            i++;
        }

        int num = -1;
        while (num < 0 || num > filenames.length) {
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
        Test deserializedObject = null;

        try{
            // Streams to read it in
            FileInputStream fis = new FileInputStream(fullPath);
            ObjectInputStream ois = new ObjectInputStream(fis);

            // Read it in with the cast to specific type
            deserializedObject =  (Test) ois.readObject();

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

    /* Part C */

    @Override
    // Reason for Override: change out "SURVEY" for "TEST" when displaying
    public void take() {

        int counter = 1; // Will be used as the Questions (type) index; purely for display purposes
        ResponseSheet rs = new ResponseSheet(this.name, this.responseDirPath); // Where we're storing user responses
        ResponseCorrectAnswer currentResponse; // Where we're storing each fetched user response (redefined for each question)

        surveyOutput.display("\n**** TEST: " + this.name + " ****\n");
        // for each Question (type) in this Test (type)
        for (Question q: questions) {
            surveyOutput.display("\n" + counter + ". ");
            // fetch a response from the user (and currently store it as an RCA (type))
            currentResponse = q.takeResponse();
            // add that response to the response sheet
            rs.addRCA(currentResponse);
            // and increment the counter (for displaying the next question)
            counter++;
        }
        // save all the user responses
        rs.save();
        surveyOutput.display("\n----End of Test----\n");
        surveyOutput.display("\n*** Test responses were saved ***\n");
    }

    // did the whole thing again here because super.tabulate() would crash
    @Override
    public void tabulate() {

        ResponseSheet helperResponseSheet = new ResponseSheet(this.name, this.responseDirPath);
        ArrayList<ResponseSheet> listOfSheets = helperResponseSheet.loadAll();

        ArrayList<String> spottedResponses = new ArrayList<>();
        ArrayList<Integer> responseFrequency = new ArrayList<>();
        int helperI = 0;
        int helperJ = 0;

        if (listOfSheets == null) {
            surveyOutput.display("There are no responses recorded for this test.\n");
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

                // and for each ResponseSheet j, get the respective response to the question i
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

    @Override
    public void modify(int n) {
        String s;
        String updatedAnswer;
        ResponseCorrectAnswer correctAnswer = new ResponseCorrectAnswer();

        super.modify(n);

        surveyOutput.display("Do you wish to modify the correct answer?\nType \'Y\' for yes\n");
        s = surveyInput.listen();

        if (s.equalsIgnoreCase("Y")) {
            surveyOutput.display("Enter updated correct answer:\n");
            updatedAnswer = surveyInput.listen();
            correctAnswer.addText(updatedAnswer);
            this.correctAnswers.set(n-1, correctAnswer);
            surveyOutput.display("\nCorrect answer was successfully modified!\n");
        }
        else{
            surveyOutput.display("Whoops! You should've typed yes. I guess you don't want to modify your test...\n");
        }
    }

    @Override
    // Grades every response sheet found for this Test
    public void grade() {

        int numEssays = 0; // Not Gradable
        int numEmoji = 0;  // Not Gradable

        ResponseSheet helperResponseSheet = new ResponseSheet(this.name, this.responseDirPath); // Where the responses are saved
        ArrayList<ResponseSheet> listOfSheets = helperResponseSheet.loadAll(); // **MIGHT NEED FIXING**

        //----Counting all the Essay and Emoji questions in this Test
        for (Question q: this.questions) {
            if (q instanceof Essay) {
                numEssays++;
            }
            else if (q instanceof Emoji) {
                numEmoji++;
            }
        }
        //----

        // For each response sheet
        for (int i=0; i < listOfSheets.size(); i++) {

            // start the "questions the user got correct" counter at 0
            int numCorrAns = 0;
            surveyOutput.display("\nUser " + (i+1) + " Grade: ");

            // for each "expected answer" (type RCA),
            for (int j=0; j < this.correctAnswers.size(); j++) {

                // fetch the jth response sheet's ith response
                ResponseCorrectAnswer userRCA = listOfSheets.get(i).getRCA(j);

                // for each String response in the aforementioned RCA,
                for (int k=0; k < userRCA.getTexts().size(); k++) {
                    // fetch the respective String "expected answer",
                    String corrAns = this.correctAnswers.get(j).getTexts().get(k);
                    // Old code
                    //ResponseCorrectAnswer corrRCA = this.correctAnswers.get(k);

                    // check if the kth String in the user RCA == the kth String in the "expected answers" RCA
                    if (userRCA.getTexts().get(k).equals(corrAns)) {
                        // if so, increment, and go back to the top of the loop
                        numCorrAns++;
                        break;
                    }
                    // else, check if the Question (type) associated to the RCAs we are currently looking at is either
                    //  an Essay (type) or Emoji (type). Theoretically, the "expected answers" for these types of
                    //  Questions should be a blank-string, as for these types of Questions, there is no correct
                    //  answer, and as such, any answer counts as a correct answer...
                    else if (!(this.questions.get(j) instanceof Essay) || !(this.questions.get(j) instanceof Emoji)) {
                        // however, we're checking if the Question IS NOT the two types mentioned above. In that case,
                        //  if the user input mismatched what was expected, display that that was the case (and don't
                        //  increment.
                        surveyOutput.display("\nThe answer provided by the user for Q" + (j + 1) + " is incorrect.\n");
                    }

                }
            }

            int possibleToGrade = (this.questions.size() - (numEmoji + numEssays));
            int wrong = possibleToGrade - numCorrAns;

            surveyOutput.display("Correct: " + numCorrAns + "\n");
            surveyOutput.display("Incorrect: " + wrong + "\n");

            numCorrAns = numCorrAns * 10;
            possibleToGrade = possibleToGrade * 10;

            surveyOutput.display("*** Final Grade: " + numCorrAns + "/" + possibleToGrade + " ***\n");
        }
    }
}
