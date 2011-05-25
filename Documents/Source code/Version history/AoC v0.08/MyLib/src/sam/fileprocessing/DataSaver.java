/*
 * DataSaver.java
 *
 * Created on August 2, 2006, 11:24 AM
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

import java.io.*;
import java.util.Vector;
import sam.utilities.Sort;

/**
 *
 * @author Sam Fentress
 * @version 0.01
 */
public class DataSaver {
    
    private String folderName, fileName;
    private BufferedWriter bufferedData;
    private String header;
    private static BufferedWriter staticBufferedData;
    private int numDataPerLine;
    Vector totalData;
    
    /** Creates a new instance of DataSaver */
    public DataSaver(String folderName, String fileName) {
        this.folderName = folderName;
        this.fileName = fileName;
        totalData = new Vector();
        header = "";
    }
    
    public DataSaver(){
        folderName = "";
        fileName = "data.txt";
        totalData = new Vector();
        header = "";
    }
    
    public void addData(String[] data){
        totalData.add(data);
    }
    
    public void setHeader(String header){
        this.header = header;
    }
    
    public void saveData() {
        new File(folderName).mkdirs();                   //If folder doesn't exist, create it
        try {
            bufferedData = new BufferedWriter(new FileWriter(folderName + File.separator  + fileName));
        } catch (IOException e) {
        }
        try {
            if (header.length() > 0)
                bufferedData.write(header);
                
            for (int i = 0; i<totalData.size(); i++){
                String[] data = (String[]) totalData.elementAt(i);
                for (int j = 0; j < data.length; j++) {
                    bufferedData.write(data[j] + ",");
                }
                bufferedData.write("\n");
            }
            bufferedData.close();
        } catch (IOException e) {
        }
    }
    
    public void saveData(int[] order){
        System.out.println("old size: " + totalData.size());
        Vector unorderedData = new Vector(totalData);
        System.out.println("temp size: " + unorderedData.size());
        totalData = Sort.reorder(unorderedData, order);  
        System.out.println("new size: " + totalData.size());
        saveData();
    }
    
    public void saveData(String[] data){
        addData(data);
        saveData();
    }
    
    public static void saveData(String folderName, String fileName, String[] data){
        new File(folderName).mkdirs();                   //If Results/ file doesn't exist, create it
        try {
            staticBufferedData = new BufferedWriter(new FileWriter(folderName + File.separator  + fileName));
        } catch (IOException e) {
        }
        try {
                for (int j = 0; j < data.length; j++) {
                    staticBufferedData.write(data[j] + ",");
                }
                staticBufferedData.write("\n");
                staticBufferedData.close();
        } catch (IOException e) {
        }
    }
}
