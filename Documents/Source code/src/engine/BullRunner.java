/*
 * BullRunner.java
 *
 * Created on May 1, 2006
 * Latest version: July 20, 2006
 *
 * This class is a part of the Bull Runner program, created
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. For more information,
 * please see the License document accompanying this program.
 *
 * @author Sam Fentress
 * @version 1.0
 */

package engine;

import gui.Gui;
import gui.Gui_NewRun;
import gui.Gui_Start;
import sam.fileprocessing.Folders;
import sam.utilities.Logger;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import javax.swing.JOptionPane;
import java.util.Arrays;

/**
 * The main class for the Assessment of Comprehension program. The class
 * actually does very little: It displays the first two GUIs (GUI_Start and
 * GUI_NewRun); creates a new Student from the user input in GUI_NewRun; creates
 * the TestingEngine and main GUI; connects them together; and calls
 * TestingEngine.begin(). TestingEngine takes control of the program from that point
 * on.
 *
 * @author Sam Fentress
 * @version 1.0
 */
public class BullRunner {
    
    private static Gui_Start guiS;
    private static Gui_NewRun guiNR;
    private static Gui guiMain;
    private TestingEngine tester;
    private static boolean verbose;
    
    /**
     * Creates all three GUIs, and displays the first.
     */
    public BullRunner(){
        guiNR = new Gui_NewRun(this);
        
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();   //devices is an array of monitors
        Logger.log("Creating Gui ...");
        guiMain = new Gui(this, devices[0]);
        Logger.log(" ... Gui created");
        Logger.print();
        
        guiS = new Gui_Start(this);
        guiS.setVisible(true);
        
        TestValidator validator = new TestValidator();
        boolean testsAreOk = validator.checkValidation();
        String status = validator.getStatus();
        
        guiS.setStatus(status, testsAreOk, validator);
        
    }
    
    /**
     * Closes the spash window (GUI_Start) and opens the new student window
     * (GUI_NewRun).
     */
    public void getStudent(){
        guiS.dispose();
        guiNR.setVisible(true);
    }
    
    /**
     * Creates a new TestingEngine, and connects it to the main GUI. Starts the
     * testing by calling TestingEngine.begin(), at which point BullRunner relinquishes
     * control of the program to TestingEngine.
     * 
     * @param student The student object to be passed to the TestingEngine.
     */
    public void runNewTest(Student student){
        
        tester = new TestingEngine(this, student, guiMain);
        guiMain.setEngine(tester);
        
        boolean valueListCreated = tester.createValueLists();
        if (!valueListCreated)
            System.exit(0);
        
        if(TestValidator.checkIfAlreadyBegun(student)){
            int choice = JOptionPane.showConfirmDialog(guiNR, "It looks like you have already begun. \nDo you want" +
                    " to start from where you left off?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            switch (choice){
                case JOptionPane.YES_OPTION:
                    guiNR.dispose();
                    tester.beginFromMidpoint();
                    break;
                case JOptionPane.NO_OPTION:
                    tester.deleteOriginalFiles();
                    guiNR.dispose();
                    tester.begin();
                    break;
                default:
                    restart();
                    break;
            }
            
        } else {
            guiNR.dispose();
            tester.begin();
        }
        
    }
    
    /**
     * Creates a new TestingEngine for practice purposes, and connects it to the
     * main GUI. Starts the testing by calling TestingEngine.begin(), at which
     * point BullRunner relinquishes control of the program to TestingEngine.
     */
    public void runNewPractice(){
        try{
            
            Logger.log("Trying to start new practice... ");
            guiS.dispose();
            
            Logger.log(" ... disposed of guiS");
            
            Logger.log(" ... creating new Tester ...");
            tester = new TestingEngine(this, guiMain);
            Logger.log(" ... Tester created");
            
            Logger.log(" ... checking tester ...");
            tester.checkTester();
            
            Logger.log(" ... Setting engine ...");
            guiMain.setEngine(tester);
            Logger.log(" ... engine set");
            
            Logger.log(" ... creating ValueList ...");
            boolean valueListCreated = tester.createValueLists();
            if (!valueListCreated)
                System.exit(0);
            Logger.log(" ... ValueList created");
            Logger.log(" Testing now begining");
            tester.begin();
        } catch (Exception ex){
            StackTraceElement[] trace = ex.getStackTrace();
            JOptionPane.showMessageDialog(guiMain, ex.toString() + " at " + trace[0].toString(), "Warning", JOptionPane.WARNING_MESSAGE);
            Logger.log(ex + " at " + trace[0].toString() + ", " + trace[1].toString() + ", " + trace[2].toString());
            System.exit(0);
        }
        
    }
    
    /**
     * Restarts the program from zero
     */
    public void restart(){
        Logger.log(" ***** restarting *****");
        Logger.print();
        guiNR.dispose();
        guiMain.dispose();
        String[] args = null;
        main(args);
    }
    
    
    /**
     * Creates a new BullRunner, which kicks the program off.
     * 
     * @param args "verbose" can be used to see running information in the
     * control panel.
     */
    public static void main(String[] args) {
        if ((args != null) && (args.length > 0)){
            if (args[0].equalsIgnoreCase("verbose"))
                Logger.setVerbose(true);
            else Logger.setVerbose(false);
        }
        
        final BullRunner aoc = new BullRunner();
    }
    
}
