/*
 * ValueList.java
 *
 * Created on May 1, 2006
 * Latest version: July 20, 2006
 *
 * This class is a part of the Bull Runner program, created
 * for the Language Science Lab at Boston University, under the grant entitled
 * "Assessment of Comprehension Skills in Older Struggling Readers." Please
 * direct any questions regarding the project to Dr. Gloria S. Waters or Dr.
 * David N. Caplan.
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
 * @version 1.0
 */

package engine;

//import com.Ostermiller.util.CSVParser;
import com.Ostermiller.util.ExcelCSVParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import java.lang.Integer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import sam.utilities.Sort;
import sam.utilities.Logger;
import sam.utilities.ArrayUtils;

/**
 * A two-dimensional array with methods relating to sorting and selecting values.
 * ValueLists are created by parsing .csv files for stimuli lists. After grabbing
 * the values, the values are ordered, and can be selected one at a time from
 * the TestingEngine to be displayed by the Gui.
 *
 * @author Sam Fentress
 * @version 1.0
 */
public class ValueList {

    private String name;                //The name of the list, given by the file name of the stimulus list
    private String[] instructions;      //The instructions associated with the file, as given by line #2 of the file
    private String[][] examples;        //Examples shown
    private String[][] values;          //The stimulus values, as given by the columns in the file
    private String type;                //The type of stimuli, as given by line #1 in the file
    private int[] order;                //An array depecting the order that the stimuli will be shown.
    private boolean isRandomOrder;      //Whether the stimuli will be shown in random order
    private boolean isRandomSwap;       //Whether left-right order is random
    private boolean hasExamples;        //Whether test contains an example block
    private boolean hasSound;
    private boolean hasTags;
    private int numTags;
    private String tags;
    private Random rand;
    private String inputType;
    private int lowestNumber, highestNumber;
    private int index;                  //An index counting the current position along order[]
    private int examplesIndex;          //Index for counting along examples
    private int length;                  //The number of stimuli in the ValueList
    private int numExamples;
    private String soundFolder;
    private String imageFolder;


    /**
     * Creates a new ValueList. The file (Excel .csv) passed must comply with
     * the stimulus list standards, to wit: Line 1 must be the stimulus type, e.g.
     * one_word_two_picts; line 2 must be the insructions, formatted in HTML; if there
     * are examples, line 3 must state "<examples>", after which examples are given as
     * normal stimuli. Following each example, the next line must contain the
     * feedback for a correct response, the line after must contain the feedback for
     * and incorrect response. The block is terminated by the line "</examples>". The
     * remaining lines must be columns of values for the stimulus and choices.
     * Note that blank lines are ignored.
     *
     * @param file The .csv file to be parsed
     */
    public ValueList(File file){

        // ********** Get raw values and insert them into an array *********
        Logger.log("Creating a new valuelist for " + file.getName());
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            Logger.print();
        }

        ExcelCSVParser ecp = new ExcelCSVParser(in);
        String[][] rawValues = null;
        Logger.log("Trying to get rawValues...");
        try {
            rawValues = ecp.getAllValues();         //Set rawValues[][] to be an array of all values in file
            Logger.log("Success! rawValues.length = " + rawValues.length);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.print();
        }
        // *****************************************************************

        // ********** Check for options, and set instructions ****************
        String[] firstLine = ArrayUtils.trim(rawValues[0]);
        type = firstLine[0];                     //Set type
        Logger.log("type: " + type);

        setOptions(firstLine);

        String[] instructionsArray = ArrayUtils.trim(rawValues[1]);
        setInstructions(instructionsArray);           //Set instructions
        // *****************************************************************

        // ********** Get examples, if any *********************************
        int valuelessSpace = 2;                     //Total space in list without values

        if (rawValues[2][0].equalsIgnoreCase("<examples>")){

            hasExamples = true;
            numExamples = 0;
            int lineNo = 3;

            while (!rawValues[lineNo][0].equalsIgnoreCase("</examples>")) {    //First count total examples
                lineNo++;
            }

            int numberOfLines = lineNo - 3;
            numExamples = numberOfLines;
            Logger.log("" + numExamples);
            examples = new String[numExamples][ArrayUtils.trim(rawValues[3]).length];
            lineNo = 3;

            while (!rawValues[lineNo][0].equalsIgnoreCase("</examples>")) {    //Then set examples
                examples[lineNo-3] = ArrayUtils.trim(rawValues[lineNo]);
                if (lineNo % 3 > 0)
                    examples[lineNo-3][0] = "<font size=6>" + examples[lineNo-3][0] + "</font>";
                lineNo++;
            }

            valuelessSpace += 2 + numExamples;                    //add an extra 2 for example text, plus any extra
        } else hasExamples = false;
        // *****************************************************************

        // ********** Get number of tags, if any ***************************
        String[] tagArray = ArrayUtils.trim(rawValues[valuelessSpace]);
        hasTags = false;
        try {
            String[] firstValuesArray = ArrayUtils.trim(rawValues[valuelessSpace+1]);
            if ((type.indexOf("paragraph_") < 1 && tagArray.length < firstValuesArray.length) ||
                    (type.indexOf("paragraph_") > 1 && firstValuesArray.length > 1))
                hasTags = true;
        } catch (Exception e){Logger.log("has only one line");}

        tags = "";
        if (hasTags){
            numTags = tagArray.length;
            for (int i = 0; i < tagArray.length; i++) {
                tags = tags+ tagArray[i] + ",";
            }
            valuelessSpace++;
        }
        // *****************************************************************

        // ********** Get values, including any tags ***********************

        int valuesLength, valuesWidth;
        valuesLength = rawValues.length - valuelessSpace;
        Logger.log("rawValues[valuelessSpace].length = " + rawValues[valuelessSpace].length);
        valuesWidth = rawValues[valuelessSpace].length + 2;
        Logger.log("valuesWidth = " + valuesWidth);

        String[][] tempValues = new String[valuesLength][valuesWidth];
        int numBlanks = 0;

        for (int i=0; i<tempValues.length; i++){        //Create values[][] based on remaining rawValues[][] data
            String tempValuesItem[] = rawValues[i+valuelessSpace];
            if (tempValuesItem.length > 1)
                tempValues[i] = tempValuesItem;
            else numBlanks++;
        }

        values = new String[valuesLength-numBlanks][valuesWidth];

        for (int i = 0; i < values.length; i++) {
            values[i] = tempValues[i];
        }

        length = values.length;                     //Set length
        addFolderNames();
        order = new int[length];
        setOrder();                                 //Set order (randomized or not)
        // *****************************************************************
    }

    /**
     * Sets stim file's individual options.
     * @param options Array of options set by user.
     */
    public void setOptions(String[] options){
        Logger.log("Checking options");

        hasSound = false; isRandomOrder = true; isRandomSwap = true; soundFolder = "";
        imageFolder = ""; inputType = "lr"; lowestNumber = 0; highestNumber = 9;  //defaults

        for (int i = 1; i < options.length; i++) {

            if (options[i].equals(null))
                options[i] = "";

            // *** Get basic options
            if (options[i].equalsIgnoreCase("sound"))
                hasSound = true;
            if (options[i].equals("not random order"))
                isRandomOrder = false;
            if (options[i].equalsIgnoreCase("not random swap"))
                isRandomSwap = false;

            // *** Get input options
            if (options[i].equalsIgnoreCase("lr"))
                inputType = "lr";
            if (options[i].equalsIgnoreCase("letters"))
                inputType = "letters";
            String possibleInputType = options[i];
            String[] possibleInputArray = possibleInputType.split(": ");
            if (possibleInputArray[0].equalsIgnoreCase("numbers")){
                inputType = "numbers";
                if (possibleInputArray.length > 1){
                    String[] highLowNumbers = possibleInputArray[1].split("-");
                    lowestNumber = Integer.parseInt(highLowNumbers[0]);
                    highestNumber = Integer.parseInt(highLowNumbers[1]);
                } else {
                    lowestNumber = 0;
                    highestNumber = 9;
                }
            }

            // *** Get folder options
            String possibleFolderName = options[i];
            String[] possibleFolderArray = possibleFolderName.split(": ");
            if (possibleFolderArray[0].equalsIgnoreCase("sound folder"))
                soundFolder = possibleFolderArray[1];
            if (possibleFolderArray[0].equalsIgnoreCase("image folder"))
                imageFolder = possibleFolderArray[1];
        }
        //  *** These defaults are enforced
        if (type.equalsIgnoreCase("one_word")||type.equalsIgnoreCase("two_words")||
                type.equalsIgnoreCase("one_sent")||type.equalsIgnoreCase("one_sent_mw")||
                type.equalsIgnoreCase("paragraph"))
            isRandomSwap = false;
        if (type.toLowerCase().indexOf("paragraph") >= 0)
            isRandomOrder = false;
        if (type.toLowerCase().indexOf("_yn") > 0 || type.toLowerCase().indexOf("_tf") > 0 ||
                type.toLowerCase().indexOf("_two") > 0)
            setInputType("lr");
    }

    /**
     * Set's name of stim file.
     * @param name Stim file's name.
     */
    public void setName(String name){this.name = name;}

    /**
     *
     * @return Name of stim file.
     */
    public String getName(){return name;}

    /**
     *
     * @return Type of subtest.
     */
    public String getType(){return type;}

    /**
     *
     * @param inputType String representing type of user input subtest accepts.
     */
    public void setInputType(String inputType){this.inputType = inputType;}

    /**
     *
     * @return The type of user input accepted by subtest.
     */
    public String getInputType(){return inputType;}

    /**
     *
     * @return If inpt type is "number," the lowest number accepted as an input.
     */
    public int getLowestNumber(){return lowestNumber;}

    /**
     *
     * @return If inpt type is "number," the highest number accepted as an input.
     */
    public int getHighestNumber(){return highestNumber;}

    /**
     *
     * @return True if subtest uses sound as stimuli.
     */
    public boolean hasSound(){return hasSound;}

    /**
     *
     * @return True if stimuli have tags for data saving.
     */
    public boolean hasTags(){return hasTags;}

    /**
     *
     * @return Number of tags stimuli use.
     */
    public int getNumTags(){return numTags;}

    /**
     *
     * @return Tags used by stimuli.
     */
    public String getTags(){return tags;}

    /**
     * Sets the instruction to be shown to the user.
     * @param rawInstructions Unedited version of instructions.
     */
    public void setInstructions(String[] rawInstructions){
        instructions = new String[rawInstructions.length];
        instructions[0] = "<p align=\"center\"><font size=TITLESIZE><u><b>Instructions" +   //Add pre- and post- HTML formatting text
                "</b></u></font></p><font size=TEXTSIZE><p><p>" + rawInstructions[0] +
                "</font>";
        if (instructions.length > 1)
            for (int i = 1; i < rawInstructions.length; i++) {
            instructions[i] = rawInstructions[i];
            }

    }
    /**
     *
     * @return Instructions for the subtest.
     */
    public String[] getInstructions(){
        return instructions;
    }

    /**
     *
     * @param isRandom True if order of stimuli is to be random.
     */
    public void setRandomOrder(boolean isRandom){
        this.isRandomOrder = isRandomOrder;
        setOrder();
    }

    /**
     *
     * @return True if order of stimuli is random.
     */
    public boolean isRandomOrder(){return isRandomOrder;}

    /**
     *
     * @param isRandom True if left-right order of stimuli is to be random.
     */
    public void setRandomSwap(boolean isRandom){isRandomSwap = isRandom;}
    /**
     *
     * @return True if left-right order of stimuli is random.
     */
    public boolean isRandomSwap(){return isRandomSwap;}

    /**
     *
     * @return True if subtest uses examples.
     */
    public boolean hasExamples(){
        return hasExamples;
    }

    private void addFolderNames(){
        if (!soundFolder.equals("")){
            for (int i = 0; i < getNumExamples(); i++) {
                for (int j = 0; j < getExamplesNo(i).length; j++) {
                    String filename = getExamplesNo(i)[j];
                    String lowFilename = filename.toLowerCase();
                    if (lowFilename.indexOf(".wav") > 0)
                        examples[i*3][j] = soundFolder + File.separator + filename;
                }
            }
            for (int i = 0; i < values.length; i++) {
                for (int j = 0; j < values[i].length; j++) {
                    String filename = values[i][j];
                    String lowFilename = filename.toLowerCase();
                    if (lowFilename.indexOf(".wav") > 0)
                        values[i][j] = soundFolder + File.separator + filename;
                }
            }
        }
        if (!imageFolder.equals("")){
            for (int i = 0; i < getNumExamples(); i++) {
                for (int j = 0; j < getExamplesNo(i).length; j++) {
                    String filename = getExamplesNo(i)[j];
                    String lowFilename = filename.toLowerCase();
                    if (lowFilename.indexOf(".jpg") > 0)
                        examples[i*3][j] = imageFolder + File.separator + filename;
                }
            }
            for (int i = 0; i < values.length; i++) {
                for (int j = 0; j < values[i].length; j++) {
                    String filename = values[i][j];
                    String lowFilename = filename.toLowerCase();
                    if (lowFilename.indexOf(".jpg") > 0)
                        values[i][j] = imageFolder + File.separator + filename;
                }
            }
        }
    }

    private void setOrder(){
        for (int i = 0; i<length; i++)
            order[i] = i;

        if (isRandomOrder)                           //If random, shuffle array
            Sort.shuffle(order);
    }

    /**
     *
     * @return Order that stimuli will be displayed in.
     */
    public int[] getOrder(){return order;}

    /**
     * Returns next values of values[][], based on order of order[] and index
     * @return Array of stimuli for the next item to be displayed.
     */
    public String[] getNext(){

        String[] nextValues = new String[values[0].length];

        if (index > values.length-1) nextValues[0] = "~end~";   //Flag to single end to testingEngine
        else {
            nextValues = values[order[index]];
            index++;
        }
        return nextValues;
    }

    /**
     * Returns the current value of values[][]. Doesn't update index.
     * @return Array of stimuli currently being displayed.
     */
    public String[] getCurrent(){

        String[] nextValues = new String[values[0].length];
        int oldIndex = index - 1;

        nextValues = values[order[oldIndex]];

        return nextValues;
    }

    /**
     *
     * @param index Item number to be returned.
     * @return Array of stimuli at the specified index number.
     */
    public String[] getValueNo(int index){
        String[] nextValues = new String[values[0].length];
        nextValues = values[order[index]];
        return nextValues;
    }

    /**
     *
     * @return Index of stimulus currently being shown.
     */
    public int currentValueNo(){                //Which value we are currently on
        return order[index-1];                  //-1, as index is already updated by now
    }

    /**
     *
     * @return Total number of items.
     */
    public int getNumValues(){return values.length;}

    /**
     *
     * @return Total number of examples.
     */
    public int getNumExamples(){return (numExamples/3);}

    /**
     *
     * @param index Example number to be returned.
     * @return Example at specified index.
     */
    public String[] getExamplesNo(int index){return examples[(index*3)];}

    /**
     * Returns next example, based on examplesIndex
     * @return Array representing stimulus to be shown.
     */
    public String[] getNextExample(){
        String[] nextValues = new String[values[0].length];

        if (examplesIndex > examples.length-1) nextValues[0] = "~end~";
        else {
            nextValues = examples[examplesIndex];
            examplesIndex++;
        }

        return nextValues;
    }

    /**
     * Returns the current example. Doesn't update index.
     * @return Array representing current stimulus being shown.
     */
    public String[] getCurrentExample(){

        String[] nextValues = new String[values[0].length];
        int oldIndex = examplesIndex - 3;

        nextValues = examples[oldIndex];

        return nextValues;
    }



}
