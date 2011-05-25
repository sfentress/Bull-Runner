/*
 * ValueList.java
 *
 * Created on May 1, 2006
 * Latest version: July 20, 2006
 *
 * This class is a part of the Assessment of Comprehension program (AoC), created
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
 * @version 0.05
 */

package aoc;

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
 * @version 0.04
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
        
        hasSound = false; isRandomOrder = true; isRandomSwap = true; soundFolder = ""; imageFolder = "";   //defaults
        for (int i = 1; i < firstLine.length; i++) {
            if (firstLine[i].equalsIgnoreCase("sound"))
                hasSound = true;
            if (firstLine[i].equals("not random order"))
                isRandomOrder = false;
            if (firstLine[i].equalsIgnoreCase("not random swap"))
                isRandomSwap = false;
            String possibleFolderName = firstLine[i];
            String[] possibleFolderArray = possibleFolderName.split(": ");
            if (possibleFolderArray[0].equalsIgnoreCase("sound folder"))
                soundFolder = possibleFolderArray[1];
            if (possibleFolderArray[0].equalsIgnoreCase("image folder"))
                imageFolder = possibleFolderArray[1];
        }
        
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
            if (tagArray.length < firstValuesArray.length)
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
        System.out.println("rawValues[valuelessSpace].length = " + rawValues[valuelessSpace].length);
        valuesWidth = ArrayUtils.trim(rawValues[valuelessSpace]).length;
        System.out.println("valuesWidth = " + valuesWidth);
        values = new String[valuesLength][valuesWidth];
        
        for (int i=0; i<values.length; i++){        //Create values[][] based on remaining rawValues[][] data
            values[i] = ArrayUtils.trim(rawValues[i+valuelessSpace]);
      //      System.out.println("values["+i+"] = " + Arrays.toString(values[i]));
        }
        
        length = values.length;                     //Set length
        addFolderNames();
        order = new int[length];
        setOrder();                                 //Set order (randomized or not)
        // *****************************************************************
    }
    
    public void setName(String name){this.name = name;}
    
    public String getName(){return name;}
    
    public String getType(){return type;}
    
    public boolean hasSound(){return hasSound;}
    
    public boolean hasTags(){return hasTags;}
    
    public int getNumTags(){return numTags;}
    
    public String getTags(){return tags;}
    
    public void setInstructions(String[] rawInstructions){
        instructions = new String[rawInstructions.length];
        instructions[0] = "<p align=\"center\"><font size=7><u><b>Instructions" +   //Add pre- and post- HTML formatting text
                "</b></u></font></p><font size=6><p><p>" + rawInstructions[0] +
                "</font>";
        if (instructions.length > 1)
            for (int i = 1; i < rawInstructions.length; i++) {
            instructions[i] = rawInstructions[i];
            }
        
    }
    public String[] getInstructions(){
        return instructions;
    }
    
    public void setRandomOrder(boolean isRandom){
        this.isRandomOrder = isRandomOrder;
        setOrder();
    }
    
    public boolean isRandomOrder(){return isRandomOrder;}
    
    public void setRandomSwap(boolean isRandom){isRandomSwap = isRandom;}
    public boolean isRandomSwap(){return isRandomSwap;}
    
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
    
    public int[] getOrder(){return order;}
    
    /**
     * Returns next values of values[][], based on order of order[] and index
     */
    public String[] getNext(){
        
        String[] nextValues = new String[values[0].length];
        System.out.println("getting next");
        System.out.println("nextValues.length = " + nextValues.length);
        
        if (index > values.length-1) nextValues[0] = "~end~";   //Flag to single end to testingEngine
        else {
            nextValues = values[order[index]];
            index++;
        }
   //     System.out.println("returning: " + Arrays.toString(nextValues));
        return nextValues;
    }
    
    /**
     * Returns the current value of values[][]. Doesn't update index.
     */
    public String[] getCurrent(){
        
        String[] nextValues = new String[values[0].length];
        int oldIndex = index - 1;
        
        nextValues = values[order[oldIndex]];
        
        return nextValues;
    }
    
    public String[] getValueNo(int index){
        String[] nextValues = new String[values[0].length];
        nextValues = values[order[index]];
        return nextValues;
    }
    
    public int currentValueNo(){                //Which value we are currently on
        return order[index-1];                  //-1, as index is already updated by now
    }
    
    public int getNumValues(){return values.length;}
    
    public int getNumExamples(){return (numExamples/3);}
    
    public String[] getExamplesNo(int index){return examples[(index*3)];}
    
    /**
     * Returns next example, based on examplesIndex
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
     */
    public String[] getCurrentExample(){
        
        String[] nextValues = new String[values[0].length];
        int oldIndex = examplesIndex - 3;
        
        nextValues = examples[oldIndex];
        
        return nextValues;
    }
    
    
    
}
