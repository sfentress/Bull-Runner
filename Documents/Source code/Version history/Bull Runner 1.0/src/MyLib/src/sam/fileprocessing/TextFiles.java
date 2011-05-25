/*
 * TextFiles.java
 *
 * Created on July 14, 2006, 2:33 PM
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
 */

package sam.fileprocessing;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Sam Fentress
 * @version 0.01
 */
public class TextFiles {
    
    public static String getTextFromFile(String fileName) {
        File file = new File(fileName);
        //...checks on file are elided
        StringBuffer contents = new StringBuffer();
        
        //declared here only to make visible to finally clause
        BufferedReader input = null;
        try {
            //use buffering, reading one line at a time
            //FileReader always assumes default encoding is OK!
            input = new BufferedReader( new FileReader(file) );
            String line = null; //not declared within while loop
                /*
                 * readLine is a bit quirky :
                 * it returns the content of a line MINUS the newline.
                 * it returns null only for the END of the stream.
                 * it returns an empty String if two newlines appear in a row.
                 */
            while (( line = input.readLine()) != null){
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        } finally {
            try {
                if (input!= null) {
                    //flush and close both "input" and its underlying FileReader
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return contents.toString();
    }
    
    public static int fileLength(String fileName){
        int fileLength = 0;
        File file = new File(fileName);
        
        BufferedReader input = null;
        try {
            //use buffering, reading one line at a time
            //FileReader always assumes default encoding is OK!
            input = new BufferedReader( new FileReader(file) );
            String line = null; //not declared within while loop

            while (( line = input.readLine()) != null){
                fileLength++;
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        } finally {
            try {
                if (input!= null) {
                    //flush and close both "input" and its underlying FileReader
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        return fileLength;
    }
    
    public static void main(String[] args){
        String fileName = "src" + File.separator + "ggg.csv";
        System.out.println(fileLength(fileName));
    }
    
}
