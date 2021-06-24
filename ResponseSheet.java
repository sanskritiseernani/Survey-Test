import utils.FileUtils;

import java.io.*;
import java.util.*;

public class ResponseSheet implements Serializable {

    protected String dirPath;
    protected String filename;
    protected String fullPath;

    protected int counter;
    private static final long serialVersionUID = 1L;
    String parentSurveyName;

    protected ArrayList<ResponseCorrectAnswer> myRCAs = new ArrayList<>();

    public ResponseSheet() {}

    public ResponseSheet(String surveyName, String path) {

        this.parentSurveyName = surveyName;
        this.dirPath = path;
        File directory = new File(path);
        File[] filesInDirectory = directory.listFiles();

        this.counter = filesInDirectory.length;
        this.counter++;
        this.filename = parentSurveyName + "Responses" + this.counter;

    }

    public void addRCA(ResponseCorrectAnswer r) {
        this.myRCAs.add(r);
    }

    public ResponseCorrectAnswer getRCA(int i) {
        return this.myRCAs.get(i);
    }

    // Code by Sean
    public void save() {

        while (checkFileExists(filename)) {
            this.counter++;
            this.filename = parentSurveyName + "Responses" + Integer.toString(1 + this.counter);
        }

        fullPath = dirPath + filename;

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

    public boolean checkFileExists(String f) {
        String temp = dirPath + f;
        boolean check = new File(temp).exists();
        return check;
    }

    /* Using the code provided by Sean Grimes for de-Serialization here */
    public ArrayList<ResponseSheet> loadAll() {

        ArrayList<ResponseSheet> tempList = new ArrayList<>();

        String[] filenames;
        File f = new File(dirPath);  // Directory object
        filenames = f.list();  // listing all the files present in directory f

        if (filenames == null) {
            return null;
        }


        for (String filename : filenames) {

            fullPath = dirPath + filename;

            File tst = new File(fullPath);
            if (!tst.exists() || !tst.isFile())
                throw new IllegalArgumentException(fullPath + " is invalid");

            // Create the generic object that will hold the deserialized object
            ResponseSheet deserializedObject = null;

            try {
                // Streams to read it in
                FileInputStream fis = new FileInputStream(fullPath);
                ObjectInputStream ois = new ObjectInputStream(fis);

                // Read it in with the cast to specific type
                deserializedObject = (ResponseSheet) ois.readObject();

                // Cleanup
                ois.close();
                fis.close();
//            deserializedObject.fixIO(surveyInput, surveyOutput);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(2);
            }
            tempList.add(deserializedObject);
        }
        return tempList;
    }

}
