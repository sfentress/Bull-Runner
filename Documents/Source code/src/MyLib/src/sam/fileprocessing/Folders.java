/*
 * Folders.java
 *
 * Created on July 28, 2006, 10:40 AM
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

/**
 *
 * @author Sam Fentress
 * @version 0.01
 */
public class Folders {
    
    public static File[] getFilesFromFolder(String folderName){
        String[] filenames;
        File[] files;
        
        File stimFolder = new File(folderName + File.separator+".");
        if (stimFolder.isDirectory()){
            filenames = stimFolder.list();
            files = new File[filenames.length];
            
            for(int i=0; i< filenames.length; i++){
                File file = new File(folderName + File.separator+filenames[i]);
                files[i] = file;
            }
        } else {
            files = new File[0];
        }
        
        return files;
    }
    
    public static File[] getFilesFromFolder(String folderName, String extension){
        File[] allFiles = getFilesFromFolder(folderName);
        File[] goodFiles;
        int numGoodFiles = 0;
        
        for (int i=0; i<allFiles.length; i++){
            if (allFiles[i].getName().indexOf(extension) != -1)
                numGoodFiles++;
        }
        
        goodFiles = new File[numGoodFiles];
        
        int fileNum = 0;
        for (int i=0; i<allFiles.length; i++){
            if (allFiles[i].getName().indexOf(extension) != -1){
                goodFiles[fileNum] = allFiles[i];
                fileNum++;
            }
        }
        
        return goodFiles;
    }
    
    public static File[] getFilesFromFolder(String folderName, String extension, boolean mustStartWithLetterOrDigit){
        File[] allFiles = getFilesFromFolder(folderName, extension);
        if (!mustStartWithLetterOrDigit)
            return allFiles;
        else {
            File[] goodFiles;
            int numGoodFiles = 0;
            
            for (int i=0; i<allFiles.length; i++){
            if (Character.isLetterOrDigit(allFiles[i].getName().charAt(0)))
                numGoodFiles++;
        }
        
        goodFiles = new File[numGoodFiles];
        
        int fileNum = 0;
        for (int i=0; i<allFiles.length; i++){
            if (Character.isLetterOrDigit(allFiles[i].getName().charAt(0))){
                goodFiles[fileNum] = allFiles[i];
                fileNum++;
            }
        }
        
        return goodFiles;
        }
    }
    
    public static File[] getFilesBeginningWith(String folderName, String beginning){
        File[] allFiles = getFilesFromFolder(folderName);
        File[] goodFiles = getFilesBeginningWith(beginning, allFiles);        
        return goodFiles;        
    }
    
    public static File[] getFilesBeginningWith(String folderName, String beginning, String extension){
        File[] allFiles = getFilesFromFolder(folderName, extension);
        File[] goodFiles = getFilesBeginningWith(beginning, allFiles);        
        return goodFiles;      
    }
    
    private static File[] getFilesBeginningWith(String beginning, File[] allFiles){
        File[] goodFiles;
        int numGoodFiles = 0;
        
        for (int i=0; i<allFiles.length; i++){
            if (allFiles[i].getName().indexOf(beginning) == 0)
                numGoodFiles++;
        }
        
        goodFiles = new File[numGoodFiles];
        
        int fileNum = 0;
        for (int i=0; i<allFiles.length; i++){
            if (allFiles[i].getName().indexOf(beginning) == 0){
                goodFiles[fileNum] = allFiles[i];
                fileNum++;
            }
        }
        
        return goodFiles;  
    }
    
    public static void deleteFilesBeginingWith(String folderName, String fileName, String extension){
        File[] filesToBeDeleted = getFilesBeginningWith(folderName, fileName, extension);
        for (int i = 0; i < filesToBeDeleted.length; i++) 
            filesToBeDeleted[i].delete();
    }
    
    public static void main(String[] args){
        File[] files = getFilesBeginningWith("src", "C", "rtf");
        for (int i=0; i<files.length; i++){
            System.out.println(files[i].getName());
            //   System.out.println(files[i]);
        }
    }
}
