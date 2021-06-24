import java.io.*;
import java.lang.*;
import java.util.*;


public class MainDriver implements Serializable {

    transient Input userInput = new ConsoleInput();
    transient Output userOutput = new ConsoleOutput();

    Survey mySurvey;
    Test myTest;

    Survey helperSurvey = new Survey(userInput, userOutput);
    Test helperTest = new Test(userInput, userOutput);

    ArrayList<Survey> listOfSurveys = new ArrayList<>();
    ArrayList<Test> listOfTests = new ArrayList<>();

    private static final long serialVersionUID = 1L;

    public MainDriver() {}  // Constructor

    // method to print the 3 menus
    public void printMenus() {

        mainMenu();

        String userResponse = userInput.listen();

        while (!userResponse.equals("1") && !userResponse.equals("2") && !userResponse.equals("exit") && !userResponse.equals("3")) {  // checking for invalid input
            userOutput.display("Please enter a vaild input!\n");
            mainMenu();  // calls main menu until user enters a valid input
            userResponse = userInput.listen();
        }

        if (userResponse.equals("1")) {
            sMenu();  // if user picks survey, call and print survey menu2
        }

        else if (userResponse.equals("exit") || userResponse.equals("3")) {
            System.exit(0);
        }
        else {
            tMenu();  // if user picks test, call and print test menu2
        }
    }

    // method that prints main menu i.e. menu 1
    public void mainMenu() {

        List<String> menu = new ArrayList<String>();  // i use encapsulation here because, why not?
        menu.add("1) Survey\n");
        menu.add("2) Test\n");
        menu.add("3) Type \'exit\' to exit the program or type 3\n");

        userOutput.display("\nMenu 1\n");
        for (String s:menu) {  // print every item in the List
            userOutput.display(s);
        }
    }

    public void sMenu() {

        List<String> surveyMenu = new ArrayList<String>();
        surveyMenu.add("1) Create a new Survey\n");
        surveyMenu.add("2) Display a Survey\n");
        surveyMenu.add("3) Load a Survey\n");
        surveyMenu.add("4) Save a Survey\n");
        surveyMenu.add("5) Modify an Existing Survey\n");
        surveyMenu.add("6) Take a Survey\n");
        surveyMenu.add("7) Tabulate a Survey\n");
        surveyMenu.add("8) Quit\n");

        userOutput.display("Survey Menu 2\n");
        for (String s:surveyMenu) {
            userOutput.display(s);
        }

        String userResponse = userInput.listen();

        // based on what the user inputs, perform a certain operation
        switch (userResponse) {
            case "1":
                createSurvey();
                break;

            case "2":
                displaySurvey();
                break;

            case "3":
                loadSurvey();
                break;

            case "4":
                saveSurvey();
                break;

            case "5":
                modifySurvey();
                break;

            case "6":
                takeSurvey();
                break;

            case "7":
                tabulateSurvey();
                break;

            case "8":
                userOutput.display("Bye!");
                System.exit(0);

            default:  // invalid input handling
                userOutput.display("Please enter in a valid positive integer option from the menu!\n");
                sMenu();
                break;
        }
    }

    public void tMenu() {

        List<String> testMenu = new ArrayList<String>();
        testMenu.add("1) Create a new Test\n");
        testMenu.add("2) Display a Test\n");
        testMenu.add("3) Load a Test\n");
        testMenu.add("4) Save a Test\n");
        testMenu.add("5) Modify an Existing Test\n");
        testMenu.add("6) Take a Test\n");
        testMenu.add("7) Tabulate a Test\n");
        testMenu.add("8) Grade a Test\n");
        testMenu.add("9) Quit\n");

        userOutput.display("Test Menu 2\n");
        for (String s : testMenu) {
            userOutput.display(s);
        }

        String userResponse = userInput.listen();

        switch (userResponse) {
            case "1":
                createTest();
                break;

            case "2":
                displayTest();
                break;

            case "3":
                loadTest();
                break;

            case "4":
                saveTest();
                break;

            case "5":
                modifyTest();
                break;

            case "6":
                takeTest();
                break;

            case "7":
                tabulateTest();
                break;

            case "8":
                gradeTest();
                break;

            case "9":
                userOutput.display("Bye!");
                System.exit(0);

            default:
                userOutput.display("Please enter in a valid positive integer option from the menu!\n");
                tMenu();
                break;
        }
    }

    public void createSurvey() {
        mySurvey = new Survey(userInput, userOutput);
        mySurvey.create();
        listOfSurveys.add(mySurvey);
    }

    public void displaySurvey() {

        if (mySurvey == null) {
            userOutput.display("Please load a Survey first!");
        }
        else {
            mySurvey.display();
        }
    }

    public void loadSurvey() {
        mySurvey = helperSurvey.load();
        if (mySurvey != null) {
            userOutput.display("***Survey " + mySurvey.name + " was successfully loaded!***\n");
        }
    }

    public void saveSurvey() {

        if (mySurvey == null) {
            userOutput.display("Please create a Survey first!\n");
        }
        else {
            mySurvey.save();
            userOutput.display("***Survey " + mySurvey.name + " was successfully saved!***\n");
        }
    }

    public void takeSurvey() {
        try {
            mySurvey = helperSurvey.load();

            if (mySurvey != null) {
                mySurvey.take();
            }
            else {
                userOutput.display("OR... \nError: This won't work till you save your survey first\nYou should have saved the survey before trying to take it!\n");
            }

        }
        catch (Exception e) {
            userOutput.display("Error: This won't work till you save your survey first\nYou should have saved the survey before trying to take it!\n");
        }
    }

    public void tabulateSurvey() {
        if (mySurvey != null) {
            try {
                mySurvey.tabulate();
            }
            catch(Exception e) {}
        }
        else {
            userOutput.display("Error: No survey to tabulate!\n");
        }
    }

    public void modifySurvey() {

        int n;
        int counter = 1;
        int x;
        String stringResponse;

        userOutput.display("What survey do you wish to modify? Pick a number corresponding to the name:\n");

        for (Survey s: listOfSurveys) {
            userOutput.display(counter + ") " + s.getName() + "\n");
        }

        userOutput.display("If you don't see the survey you want to modify above, type \'L\' to load the saved surveys (remember you will have to rename the survey if you choose to modify a saved survey.)\n");

        stringResponse = userInput.listen();

        try {
            if (stringResponse.equals("L") || stringResponse.equals("l")) {
                mySurvey = helperSurvey.load();
            } else {
                try {
                    x = Integer.parseInt(stringResponse);
                    mySurvey = listOfSurveys.get(x - 1);
                } catch (Exception e) {
                    userOutput.display("Please pick a valid survey number!!\n\n");
                    modifySurvey();
                }
            }
        }
        catch (Exception e) {
            userOutput.display("Please Enter a valid input!!\n");
            stringResponse = userInput.listen();
        }

        if (mySurvey != null) {
            userOutput.display("What question do you wish to modify?\nEnter the question number:\n");
            stringResponse = userInput.listen();
            try {
                n = Integer.parseInt(stringResponse);
                mySurvey.modify(n);
                userOutput.display("Don't forget to save the survey if you want your modifications saved!\n");
                mySurvey.name = "modified " + mySurvey.name;  // i modify the name so there is no problem saving the survey
            } catch (Exception e) {
                userOutput.display("You need to enter a valid, existing question!\nPlease start over with the modification!\n");
            }
        }
    }

    public void createTest() {
        myTest = new Test(userInput, userOutput);
        myTest.create();
        listOfTests.add(myTest);
    }

    public void displayTest() {
        if (myTest == null) {
            userOutput.display("Please load a Test first!\n");
        }
        else {
            myTest.display();
        }
    }

    public void loadTest() {
        myTest = helperTest.load();
        if (myTest != null) {
            userOutput.display("***Test " + myTest.name + " was successfully loaded!***\n");
        }
    }

    public void saveTest() {
        if (myTest == null) {
            userOutput.display("Please create a Test first!\n");
        }
        else {
            myTest.save();
            userOutput.display("***Test " + myTest.name + " was successfully saved!***\n");
        }
    }

    public void takeTest() {
        //try {
            myTest = helperTest.load();

            if (myTest != null) {
                myTest.take();
                myTest.grade();
            }
        //}
        //catch (Exception e) {
        //    userOutput.display("Error: This won't work till you save your test first\nYou should have saved the test before trying to take it!\n");
        //}
    }

    public void tabulateTest() {
        if (myTest != null) {
            try {
                myTest.tabulate();
            }
            catch (Exception e) {}
        }
        else {
            userOutput.display("Error: No test to tabulate!\n");
        }
    }

    public void modifyTest() {

        int n;
        int counter = 1;
        int x;
        String stringResponse;

        userOutput.display("What test do you wish to modify? Pick a number corresponding to the name:\n");

        for (Test t: listOfTests) {
            userOutput.display(counter + ") " + t.getName() + "\n");
        }

        userOutput.display("If you don't see the test you want to modify above, type \'L\' to load the saved tests (remember you will have to rename the test if you choose to modify a saved test.)\n");

        stringResponse = userInput.listen();

        try {
            if (stringResponse.equals("L") || stringResponse.equals("l")) {
                myTest = helperTest.load();
            } else {
                try {
                    x = Integer.parseInt(stringResponse);
                    myTest = listOfTests.get(x - 1);
                } catch (Exception e) {
                    userOutput.display("Please pick a valid test number!!\n\n");
                    modifyTest();
                }
            }
        }
        catch (Exception e) {
            userOutput.display("Please Enter a valid input!!\n");
            stringResponse = userInput.listen();
        }

        if (myTest != null) {
            userOutput.display("What question do you wish to modify?\nEnter the question number:\n");
            stringResponse = userInput.listen();
            try {
                n = Integer.parseInt(stringResponse);
                myTest.modify(n);
                userOutput.display("Don't forget to save the test if you made modifications and want them saved!\n");
            } catch (Exception e) {
                userOutput.display("You need to enter a valid, existing question!\nPlease start over with the modification!\n");
            }
        }
    }

    public void gradeTest() {

        if (myTest != null) {
            myTest.grade();
        }
        else {
            userOutput.display("Please create a test first!\n");
        }
    }
}
