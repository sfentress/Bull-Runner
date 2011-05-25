/*
 * TestValidator.java
 *
 * Created on October 25, 2006
 * Latest version: October 25, 2006
 *
 * This class is a part of the Assessment of Comprehension program (AoC), created
 * for the Language Science Lab at Boston University, under the grant entitled
 * "Assessment of Comprehension Skills in Older Struggling Readers." Please
 * direct any questions regarding the project to Gloria S. Waters or David N.
 * Caplan.
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

import gui.Gui;
import sam.fileprocessing.Folders;
import sam.utilities.xml.myXML;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 *
 * @author Sam Fentress
 * @version 0.01
 */
public class TestValidator {
    
    boolean areTestsOk;
    String statusMessage, errorMessage;
    File xmlFile;
    
    /** Creates a new instance of TestValidator */
    public TestValidator() {
        statusMessage = ""; errorMessage  = "";
        xmlFile = null;
    }
    
    public boolean validateTests(){
        areTestsOk = true;
        File[] stimFiles = Folders.getFilesFromFolder("Stimuli","csv",true);
        int numFiles = stimFiles.length;
        
        try{
            myXML test = new myXML("test");
            test.setXMLComment("<!-- Validation file for Assessment Of Comprehension program, v. 0.05.3 or later -->");
            for (int i = 0; i < stimFiles.length; i++) {
                myXML subtest = test.addElement("subtest");
                myXML name = subtest.addElement("name",stimFiles[i].getName());
                myXML update = subtest.addElement("last_updated",new Long(stimFiles[i].lastModified()));
                boolean isTestOk = validateFile(stimFiles[i]);
                if (!isTestOk)
                    areTestsOk = false;
                myXML valid = subtest.addElement("valid", new Boolean(isTestOk));
                
            }
            PrintWriter pw = new PrintWriter(new FileWriter("Documents" + File.separator  + "validation.xml"), true);
            test.serialize(pw);
            
        } catch (myXML.myXMLException e){
            areTestsOk = false;
            errorMessage += "Could not create XML file";
            System.out.println(e.toString());
        } catch (java.io.IOException e){
            areTestsOk = false;
            errorMessage += "Could not create FileWriter or BufferedWriter";
            System.out.println(e.toString());
        }
        
        if (areTestsOk)
            statusMessage = "Success! Tests are all valid";
        else statusMessage = "Warning: Errors were found in one or more tests";
        return areTestsOk;
    }
    
    public boolean validateFile(File file){
        boolean isFileOk = true;
        
        try {
            ValueList list = new ValueList(file);
            
            boolean isTypeOk = Gui.checkType(list.getType());
            System.out.println(list.getName() + " checking type");
            if (!isTypeOk){
                errorMessage += "  - The test type \"" + list.getType() + "\" in " + file.getName() + " is not a valid test type.\n";
                isFileOk = false;
            }
            
            if  (list.hasSound()){
                System.out.println(list.getName() + " checking sound");
                int numStimuli = 0;
                if ((list.getType().toLowerCase().indexOf("one_word") == 0) ||
                        (list.getType().toLowerCase().indexOf("one_sent") == 0))
                    numStimuli = 1;
                else if (list.getType().toLowerCase().indexOf("two_words") == 0)
                    numStimuli = 2;
                // need to have _mw_
                
                if (list.hasExamples()){
                    for (int i = 0; i < list.getNumExamples(); i++) {
                        for (int j = 0; j < numStimuli; j++) {
                            String soundFile = list.getExamplesNo(i)[j];
                            File f = new File("Sounds" + File.separator + soundFile);
                            if (!f.exists()){
                                errorMessage += "  - The sound \"" + soundFile + ",\" used in " + file.getName() + ", could not be found in \\Sounds\n";
                                isFileOk = false;
                            }
                        }
                    }
                }
                
                int startStimNumber = 0;
                if (list.hasTags())
                    startStimNumber += list.getNumTags();
                
                for (int i = 0; i < list.getNumValues(); i++) {
                    for (int j = startStimNumber; j < numStimuli+startStimNumber; j++) {
                        String soundFile = list.getValueNo(i)[j];
                        File f = new File("Sounds" + File.separator + soundFile);
                        if (!f.exists()){
                            errorMessage += "  - The sound \"" + soundFile + ",\" used in " + file.getName() + ", could not be found in \\Sounds\n";
                            isFileOk = false;
                        }
                        
                    }
                }
            }
            
            if  (list.getType().toLowerCase().indexOf("two_picts") > 0){
                if (list.hasExamples()){
                    for (int i = 0; i < list.getNumExamples(); i++) {
                        String[] imageFiles = list.getExamplesNo(i);
                        for (int j = imageFiles.length - 2; j < imageFiles.length; j++) {
                            String imageFile = imageFiles[j];
                            File f = new File("Images" + File.separator + imageFile);
                            if (!f.exists()){
                                errorMessage += "  - The image \"" + imageFile + ",\" used in " + file.getName() + ", could not be found in \\Images\n";
                                isFileOk = false;
                            }
                            
                        }
                    }
                }
                for (int i = 0; i < list.getNumValues(); i++) {
                    String[] imageFiles = list.getValueNo(i);
                    for (int j = imageFiles.length - 2; j < imageFiles.length; j++) {
                        String imageFile = imageFiles[j];
                        File f = new File("Images" + File.separator + imageFile);
                        if (!f.exists()){
                            errorMessage += "  - The image \"" + imageFile + ",\" used in " + file.getName() + ", could not be found in \\Images\n";
                            isFileOk = false;
                        }
                        
                    }
                }
            }
            
        } catch (ArrayIndexOutOfBoundsException e){
            errorMessage += "  - " + file.getName() + " is not properly formatted.\nMake sure to include both a Test Type and the " +
                    "test's instructions. Read the user manual for instructions on how to format a test.\n";
            isFileOk = false;
            System.out.println(e.toString());
        }
        
        // String instructions = list.getInstructions();
        // if (instructions.length() )
        
        return isFileOk;
    }
    
    public boolean checkValidation(){
        areTestsOk = true;
        myXML xmlRoot;
        
        File xmlFile = new File("Documents" + File.separator + "validation.xml");
        if (xmlFile.exists()){
            try{
                BufferedReader in = new BufferedReader(new FileReader("Documents" + File.separator + "validation.xml"));
                xmlRoot = new myXML((BufferedReader)in);
            } catch (Exception e){
                statusMessage = "Warning: Could not read validation file";
                return false;
            }
        } else{
            areTestsOk = false;
            statusMessage = "Warning: Tests have not yet been validated.";
            return false;
        }
        
        File[] stimFiles = Folders.getFilesFromFolder("Stimuli","csv",true);
        int numFiles = stimFiles.length;
        
        for (int i = 0; i < stimFiles.length; i++) {
            long lastUpdated = stimFiles[i].lastModified();
            myXML xmlSubtest = xmlRoot.getElement(i);
            myXML xmlLastUpdated = xmlSubtest.findElement("last_updated");
            if (lastUpdated > Long.parseLong(xmlLastUpdated.getValue())){
                statusMessage = "Warning: Validation is not up to date.";
                return false;
            }
            myXML xmlValid = xmlSubtest.findElement("valid");
            if (xmlValid.getValue().equalsIgnoreCase("false")){
                statusMessage = "Warning: Errors were found in one or more tests";
                return false;
            }
        }
        
        
        statusMessage = "Tests have been validated and are up to date";
        
        
        return areTestsOk;
    }
    
    
    
    public String getStatus(){return statusMessage;}
    public String getErrorMessage(){return errorMessage;}
    public void clearErrorMessage(){errorMessage = "";}
    
    public static boolean checkIfAlreadyBegun(Student student){
        String studentFilenameBegining = student.getLastName() +
                student.getFirstName().substring(0,1);
        File[] studentResults = Folders.getFilesBeginningWith("Results" + File.separator + studentFilenameBegining, studentFilenameBegining, "csv");
        if (studentResults.length > 0)
            return true;
        else return false;
        
    }
    
}
