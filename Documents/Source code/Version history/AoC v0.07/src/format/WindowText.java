/*
 * WindowText.java
 *
 * Created on July 17, 2006
 * Latest version: July 20, 2006
 *
 * Copyright (C) 2006  Sam Fentress [please append any subsequent authors here]
 *
 * "Copyleft" under the terms of the GNU General Public License, version 2.0 or later
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * @author Sam Fentress
 * @version 0.05
 */

package format;         //EXCLUDE <NO MOVING WINDOW>

import sam.utilities.Logger;

/**
 *
 * @author Sam Fentress
 * @version 0.04
 */
public class WindowText {
     
    private String[] parsedText;
    private String[] blankVersion;
    private String[] normalText;
    private int numWindows;
    private int[] lengthWindow;
    private int index;
    private boolean isInstructions;
    
    /** Creates a new instance of WindowText */
    public WindowText(String text) {
        isInstructions = false;
        createWindowText(text);
    }
    
    public WindowText(String text, boolean isInstructions){
        this.isInstructions = isInstructions;
        createWindowText(text);
    }
    
    private void createWindowText(String text){
        parsedText = null;
        blankVersion = null;
        normalText = new String[2]; normalText[0] = ""; normalText[1] = "";
        String strippedText = removeNormalText(text);
        parse(strippedText);                            // Divide text into array, using the "/" separator
        makeBlanks(parsedText);                 // Make a version consisting of blanks
        index = 0;
    }
    
    public String next(){
        if (index < numWindows){
            String[] tempResult = (String[]) blankVersion.clone();
            tempResult[index] = parsedText[index].toString();      //Swap in appropriate word for blank segment
            String result = combine(tempResult);
            index++;
            
            return result;
        } else return "~nomore~";
    }
    
    public String previewNext(){
        if (index < numWindows){
            String[] tempResult = (String[]) blankVersion.clone();
            tempResult[index] = parsedText[index].toString();      //Swap in appropriate word for blank segment
            String result = combine(tempResult);
            
            return result;
        } else return "~nomore~";
    }
    
    public int getNumWindows(){
        return numWindows;
    }
    
    private String removeNormalText(String text){
        String firstStrippedText = "";
        String strippedText = "";
        
        int startRemoval1 = text.indexOf("<no moving window>");
        int endRemoval1 = text.indexOf("</no moving window>");
        
        if (startRemoval1 > -1 && endRemoval1 > startRemoval1){
            normalText[0] = text.substring(startRemoval1+18,endRemoval1);
            firstStrippedText = text.substring(endRemoval1 + 19, text.length());
        }
        else {
            firstStrippedText = text;
        }
        
        int startRemoval2 = firstStrippedText.indexOf("<no moving window>");
        int endRemoval2 = firstStrippedText.indexOf("</no moving window>");
        
        if (startRemoval2 > -1 && endRemoval2 > startRemoval2){
            normalText[1] = firstStrippedText.substring(startRemoval2+18,endRemoval2);
            strippedText = firstStrippedText.substring(0,startRemoval2);
        }
        else {
            strippedText = firstStrippedText;
        }
        
        return strippedText;
    }
    
    private void parse(String text){
        parsedText = text.split("/");
        numWindows = parsedText.length;
        lengthWindow = new int[numWindows];
        for (int i=0; i<numWindows; i++){
            parsedText[i] = parsedText[i].trim();
            lengthWindow[i] = parsedText[i].length();
        }
    }
    
    private void makeBlanks(String[] text){
        blankVersion = new String[numWindows];
        int totalLength = 0;
        int splitPlace;
        if (isInstructions) splitPlace = 20;
        else splitPlace = 37;
        
        for (int i=0; i<text.length; i++){
            blankVersion[i] = "";
            for (int j=0 ; j<text[i].length(); j++){
                if (totalLength%splitPlace==0 && totalLength!=0){
                }
                if (totalLength%splitPlace==0 && totalLength!=0 && !text[i].substring(j,j+1).equalsIgnoreCase(" ")){    //Make space when needed for split
                    int checkChar = j-1;
                    while (checkChar>0 && !text[i].substring(checkChar,checkChar+1).equalsIgnoreCase(" "))
                        checkChar--;
                    if (checkChar > 0)
                        blankVersion[i] = blankVersion[i].substring(0,checkChar) + " " + blankVersion[i].substring(checkChar+1);
                }
                if (totalLength%splitPlace==0 && totalLength!=0 && text[i].substring(j,j+1).equalsIgnoreCase(" ")){
                    blankVersion[i] = blankVersion[i] + " ";
                }
                else blankVersion[i] = blankVersion[i] + "_";
                totalLength++;
            }
            totalLength++;
        }
    }
    
    private String combine(String[] text){
        String result = normalText[0];
        for (int i = 0; i<text.length; i++)
            result = result + text[i] + " ";
        result = result + normalText[1];
        return result;
    }
    
    public String toString(){ 
        return combine(parsedText);
    }
    
    // For testing purposes:
    /**
     * public static void main(String[] args){
     * WindowText wt = new WindowText("This / is my little / test");
     * Logger.log(wt.next());
     * }
     */
}
