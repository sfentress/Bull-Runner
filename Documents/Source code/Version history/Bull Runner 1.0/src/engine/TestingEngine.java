/*
 * TestingEngine.java
 *
 * Created on May 11, 2006
 * Latest version: July 20, 2006
 *
 * This class is a part of the Assessment of Comprehension program (BullRunner), created
 * for the Language Science Lab at Boston University, under the grant entitled
 * "Assessment of Comprehension Skills in Older Struggling Readers." Please
 * direct any questions regarding the project to Dr Gloria S. Waters or Dr David
 * N. Caplan.
 *
 * This program was written by Sam Fentress [add any subsequent authors here].
 * Questions about the program may be directed to sfentress@gmail.com.
 *
 * This program is released WITHOUT COPYRIGHT into the PUBLIC DOMAIN. This
 * program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * @author Sam Fentress
 * @version 0.05
 */

package engine;

import gui.Gui;
import format.SoundClip;

import java.io.*;
import javax.swing.JOptionPane;
import java.util.Random;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import sam.fileprocessing.TextFiles;
import sam.fileprocessing.Folders;
import sam.fileprocessing.DataSaver;
import sam.utilities.Logger;
import sam.utilities.ArrayUtils;

/**
 * The main control engine for the testing side of the program. TestingEngine
 * contains an array of ValueLists, and communicates with the Gui to display
 * the experiments. TestingEngine recieves the user's responses from the Gui,
 * and records them in a .csv file.
 *
 * @author Sam Fentress
 * @version 0.4
 */
public class TestingEngine {
    
    private BullRunner aoc;                            //The main class, to be able to interact with it
    private Student student;                    //The student using the program
    private boolean isPractice;                 //Practice session without student
    private Gui gui;                            //The main Gui
    private ValueList[] list;                   //An array of stimuli lists, used to display the experiments
    private int listNo;                         //The current list being used
    private int stimNo;                         //The current stimulus being shown
    // private boolean isRandom;                   //Whether the lists are being displayed in random order
    // private boolean choiceRandom;               //Whether the choices are displayed in random order
    public boolean isExample;                  //Whether the current stimuli are examples
    private boolean haveShownMWInstr;            //Whether MW_instructions have been shown
    private boolean haveShownSoundIntr;         //Whether Sound instructions have been shown
    
    
    private DataSaver dataSaver;
    
    private int order;                          //0 if presents order as TF, 1 if FT. Used to check student's responses
    
    
    /**
     * Creates a new instance of TestingEngine.
     * 
     * @param aoc The main BullRunner
     * @param student The current student using the program
     * @param gui The main Gui
     */
    public TestingEngine(BullRunner aoc, Student student, Gui gui) {
        this.aoc = aoc;
        this.student = student;
        this.gui = gui;
        listNo = -1;            //List starts at -1, as it is immediately increased by 1 on the first trial
        //    isRandom = true;        //Set to T by default
        isPractice = false;
    }
    
    /**
     * Creates a new instance of TestingEngine for a practice begin. No student is
     * created, and no data is saved.
     * 
     * @param aoc The main BullRunner
     * @param gui The main Gui
     */
    public TestingEngine(BullRunner aoc, Gui gui) {
        Logger.log(" ... Tester starting up ...");
        this.aoc = aoc;
        this.gui = gui;
        listNo = -1;            //List starts at -1, as it is immediately increased by 1 on the first trial
        //    isRandom = true;        //Set to T by default
        isPractice = true;
        Logger.log(" ... Tester started");
    }
    
    /**
     * Called by the BullRunner to create an array of experimental stimuli. If there are
     * no .csv files in the /Stimuli/ folder, this returns False.
     * 
     * @return True if succeeds in creating an array of ValueLists
     */
    public boolean createValueLists(){
        boolean success = false;
        Logger.log(" ... Looking for stimulus files ...");
        File[] stimFiles = Folders.getFilesFromFolder("Stimuli","csv",true);
        
        if (stimFiles.length > 0){
            Logger.log(" ... found " + stimFiles.length + " files");
            list = new ValueList[stimFiles.length];
            
            for(int i=0; i< stimFiles.length; i++){
                Logger.log(" ... Trying to create ValueList for " + stimFiles[i].getName() + " ...");
                list[i] = new ValueList(stimFiles[i]);
                list[i].setName(stimFiles[i].getName());
                Logger.log("Testing Engine: createValueLists: list[i].getType() = " + list[i].getType());
                if (list[i].getType().toLowerCase().indexOf("paragraph") >= 0)
                    list[i].setRandomOrder(false);
                if (list[i].getType().toLowerCase().indexOf("_yn") > 0 || list[i].getType().toLowerCase().indexOf("_tf") > 0)
                    list[i].setInputType("lr");
                Logger.log(" ... success");
            }
            success = true;
        } else {
            JOptionPane.showMessageDialog(gui, "Warning: No stimuli files found", "Warning", JOptionPane.WARNING_MESSAGE);
            success = false;
        }
        
        return success;
    }
    
    public void checkTester(){
        Logger.log(" ... ... Tester exists");
    }
    
    /**
     * Sets up the introductions to be displayed and makes the Gui visible
     */
    public void begin(){
        gui.setOptions();
        gui.setType("intro");
        String introText = TextFiles.getTextFromFile("Stimuli" + File.separator + "intro.txt");
        String[] introTextArray = new String[]{introText};
        gui.setValues(introTextArray);
        gui.makeVisible();
    }
    
    public void beginFromMidpoint(){
        String studentFilenameBeginning = student.getLastName() +
                student.getFirstName().substring(0,1);
        File[] studentResults = Folders.getFilesBeginningWith("Results" + File.separator + studentFilenameBeginning, studentFilenameBeginning, "csv");
        listNo = studentResults.length-1;
        gui.makeVisible();
        runNextSubtest();
        // Logger.log(lastUpdatedFile.getName());
    }
    
    public void deleteOriginalFiles(){
        String studentFilenameBegining = student.getLastName() +
                student.getFirstName().substring(0,1);
        Folders.deleteFilesBeginingWith("Results", studentFilenameBegining, "csv");
    }
    
    /**
     * If there are any subtests left, displays the next subtest.
     */
    public void runNextSubtest(){
        Logger.log("TestingEngine: runNextSubtest");
        
        if (listNo < list.length-1){
            listNo++;                                       //note, ListNo starts at -1,  so first time this is set to zero
            
            Logger.log("   listNo: " + listNo);
            
            if (!isPractice){
                String fileName = student.getLastName() +  student.getFirstName().substring(0,1)
                + "-" + list[listNo].getName();
                dataSaver = new DataSaver("Results" + File.separator + student.getLastName() +  student.getFirstName().substring(0,1), fileName);
                Calendar cal = new GregorianCalendar();
                String header = "Student:," + student.getName() + "\n"
                        + "Date of birth:," + student.getDOB() + "\n"
                        + "Subtest:," + list[listNo].getName() + "\n"
                        + "Subtest #:," + (listNo + 1) + "\n"
                        + "Date:," + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR)
                        + "," + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + "\n\n"
                        + list[listNo].getTags() + "Presentation order,Response (0=Left),Accuracy (0=Incorrect),RT\n";
                dataSaver.setHeader(header);
                stimNo = 1;
            }
            
            if (list[listNo].getType().equalsIgnoreCase("ONE_SENT_MW_YN") && !haveShownMWInstr){
                Logger.log("Going to show MW instructions");
                haveShownMWInstr = true;
                listNo--;
                Logger.log(" ... getting instructions");
                String instrText = TextFiles.getTextFromFile("Stimuli" + File.separator + "instructions - moving windows.txt");
                Logger.log("Instructions: " + instrText);
                if (instrText != null){
                    gui.setType("mw_instructions");
                    gui.setValues(new String[] {instrText});
                } else {
                    runNextSubtest();
                }
            } else if (listNo == 0 && !haveShownSoundIntr){                     // Always show sound instr first (?)
                Logger.log("Going to show Sound instructions");
                haveShownSoundIntr = true;
                listNo--;
                gui.showSoundInstructions();
            } else {
                gui.setInputType(list[listNo].getInputType(), list[listNo].getLowestNumber(), list[listNo].getHighestNumber());
                gui.setType("instructions");
                gui.setValues(list[listNo].getInstructions());  //Shows instructions
            }
        }
        
        // else do nothing
    }
    
    /**
     * This is begin when a user makes a choice on a stimulus. User's action is
     * recorded, and the next experimental stimulus is displayed.
     *
     * @param choice The user's choice: Left = 0, Right = 1
     * @param time The time taken by the user
     */
    public void userAction(String choice, String time){
        Logger.log("TestingEngine: User Action: choice = " + choice + ", time = " + time);
        if (!isPractice)
            recordAction(choice, time);
        displayNext();
    }
    
    /**
     * Displays the next stimulus in the subtest.
     */
    public void displayNext(){
        Logger.log("TestingEngine: displayNext");
        
        String[] nextRawValues = list[listNo].getNext();
        if (nextRawValues[0].equalsIgnoreCase("~end~"))         //If at end of subtest...
            endSubTest();
        else {                                              //Otherwise, display next stimuli
            String[] nextValues;
            if (list[listNo].hasTags()){
                nextValues = new String[nextRawValues.length - list[listNo].getNumTags()];
                int valueNumber = 0;
                for (int i = list[listNo].getNumTags(); i < nextRawValues.length; i++) {
                    nextValues[valueNumber] = nextRawValues[i];
                    valueNumber++;
                }
            } else nextValues = nextRawValues;
            sendValuesToGui(nextValues);
        }
    }
    
    public void sendValuesToGui(String[] values){
        Logger.log("TestingEngine: sendValuesToGui: values: " + ArrayUtils.toString(values));
        String[] valuesToBeShown;
        if (list[listNo].isRandomSwap() && values.length > 2){                              //If list swap order is random, make order random
            Random rand = new Random();
            order = rand.nextInt(2);
            if (order == 1){            //swap last two items
                String temp = values[values.length - 1];
                values[values.length - 1] = values[values.length - 2];
                values[values.length - 2] = temp;
            }
            valuesToBeShown = values;
        } else {
            if (values[values.length-1].equalsIgnoreCase("yes") ||
                    values[values.length-1].equalsIgnoreCase("no")){
                valuesToBeShown = new String[values.length+1];
                for (int i = 0; i < values.length-1; i++)
                    valuesToBeShown[i] = values[i];
                valuesToBeShown[valuesToBeShown.length - 2] = "Yes";
                valuesToBeShown[valuesToBeShown.length - 1] = "No";
            } else if (values[values.length-1].equalsIgnoreCase("true") ||
                    values[values.length-1].equalsIgnoreCase("false")){
                valuesToBeShown = new String[values.length+1];
                for (int i = 0; i < values.length-1; i++)
                    valuesToBeShown[i] = values[i];
                valuesToBeShown[valuesToBeShown.length - 2] = "True";
                valuesToBeShown[valuesToBeShown.length - 1] = "False";
            } else {
                valuesToBeShown = new String[values.length];
                for (int i = 0; i < values.length; i++)
                    valuesToBeShown[i] = values[i];
            }
            
        }
        
        gui.setSound(list[listNo].hasSound());
        Logger.log("TestingEngine: sendValuesToGui: valuesToBeShown: " + ArrayUtils.toString(valuesToBeShown));
        gui.setValues(valuesToBeShown);                      //Send stimuli to gui
    }
    
    public boolean testHasExamples(){
        return list[listNo].hasExamples();
    }
    
    public void displayNextExample(){
        String[] nextValues = list[listNo].getNextExample();
        if (nextValues[0].equalsIgnoreCase("~end~")){         //If at end of subtest...
            isExample = false;
            gui.setExample(false);
            displayNext();                                      //Start real test
        } else {                                              //Otherwise, display next stimuli
            sendValuesToGui(nextValues);
            isExample = true;
            gui.setExample(true);
        }
    }
    
    public void checkExampleAnswer(int choice){
        String[] correctFeedback = list[listNo].getNextExample();
        String[] incorrectFeedback = list[listNo].getNextExample();
        
        boolean correctChoice;
        if (list[listNo].isRandomSwap()){
            if (choice == order)
                correctChoice = true;
            else correctChoice = false;
        } else {
            String[] currentValues = list[listNo].getCurrentExample();
            String correctResponse = currentValues[currentValues.length-1];
            Logger.log("Correct response: " + correctResponse + ", Choice: " + choice);
            if (((correctResponse.equalsIgnoreCase("yes") || correctResponse.equalsIgnoreCase("true")) &&
                    choice == 0) ||
                    ((correctResponse.equalsIgnoreCase("no") || correctResponse.equalsIgnoreCase("false")) &&
                    choice == 1))
                correctChoice = true;
            else correctChoice = false;
        }
        
        gui.setType("instructions");
        
        if (correctChoice)
            gui.setValues(correctFeedback);
        else gui.setValues(incorrectFeedback);
    }
    
    
    private void endSubTest(){
        
        if (!isPractice)
            dataSaver.saveData(list[listNo].getOrder());
        
        if (listNo < list.length-1){
            gui.setType("endtext");
            String[] endText = new String[]{"<font size=6><p>Thanks! You have now finished this part of the test.<p>" +
                    "Please press any key when you are ready to proceed.</font>"};
            gui.setValues(endText);
        } else {
            gui.setType("intro");
            String[] finalText = new String[]{"<font size=6><p align=center>You have now finished the test.</p>" +
                    "<p align=center><b>Thank you for your participation!</b></p></font>"};
            gui.setValues(finalText);
        }
    }
    
    /**
     * Record user's choice and time in .csv file. If choice is correct, record
     * True, else record False.
     *
     * @param choice The user's choice: Left = 0, Right = 1
     * @param time The time taken by the user
     */
    public void recordAction(String choice, String time){
        String recordedChoice;
        if (!choice.equalsIgnoreCase("-")){
        if (list[listNo].isRandomSwap()){
            if (Integer.parseInt(choice) == order)
                recordedChoice = "1";
            else recordedChoice = "0";
        } else if (list[listNo].getType().endsWith("yn") || list[listNo].getType().endsWith("tf")){
            String[] currentValues = list[listNo].getCurrent();
            String correctResponse = currentValues[currentValues.length-1];
            Logger.log("Correct response: " + correctResponse + ", Choice: " + choice);
            if (((correctResponse.trim().equalsIgnoreCase("yes") || correctResponse.trim().equalsIgnoreCase("true")) &&
                    choice.equals("0") ||
                    ((correctResponse.trim().equalsIgnoreCase("no") || correctResponse.trim().equalsIgnoreCase("false")) &&
                    choice.equals("1"))))
                recordedChoice = "1";
            else recordedChoice = "0";
            Logger.log(recordedChoice);
        } else {recordedChoice = "-";}
        } else {recordedChoice = "" + choice;}
        
        // Logger.log("RecordAction: choice = " + recordedChoice + ", time = " + time);
        
        int numTags = 0;
        if (list[listNo].hasTags())
            numTags = list[listNo].getNumTags();
        
        String results[] = new String[numTags + 4];
        for (int i = 0; i < numTags; i++) {
            results[i] = list[listNo].getCurrent()[i];
        }
        
        results[numTags] = "" + stimNo;
        stimNo++;
        results[numTags + 1] = "" + choice;         //Record button press
        results[numTags + 2] = recordedChoice;      //Record accuracy
        results[numTags + 3] = time;                //Record RT
        
        //  String[] results = new String[] {recordedChoice, time};
        dataSaver.addData(results);
    }
    
    
    
    /**
     * Changes the gui depending on the current subtest. Called by the Gui
     * when a key is pressed during instructions.
     */
    public void updateGuiType(){
        String type = list[listNo].getType();
        if (type.endsWith("yn") || type.endsWith("tf"))
            list[listNo].setRandomSwap(false);
        String cleanType = type.replaceAll("[yt][nf]", "two_words");
        gui.setType(cleanType);
    }
    
    /**
     * private void playSound(String soundFile){
     * SoundClip clip = new SoundClip(soundFile);
     * if (clip.isInitialized()){
     * //      systemtools.Time.pause(100);
     * //      clip.play();
     * clip.remove();
     * }
     * }
     */
    
}
