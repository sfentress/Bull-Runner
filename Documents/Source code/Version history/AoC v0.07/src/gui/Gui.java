/*
 * Gui.java
 *
 * Created on May 1, 2006
 * Latest version: July 20, 2006
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
 * --
 * Note: This and all other GUIs in the AoC program were created using NetBean's
 * Gui editor. The method initComponents() was entirely auto-generated and
 * cannot be edited in NetBeans. Appologies, therefore, if the organization of
 * the code does not always appear to be logical.
 * --
 *
 * @author Sam Fentress
 * @version 0.05
 */

package gui;

import aoc.AoC;
import aoc.TestingEngine;
import format.SoundClip;
import format.WindowText;

import sam.utilities.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

/**
 * The main Gui seen during the experimental proceedure. Gui contains a number
 * of different JPanels, each of which represent a different subtest. When
 * a new test is initiated, the panel representing that subtest is made
 * visible. The method called when setting the values for each stimulus in the
 * subtests performs a different action depending on which Panel is in view.
 * The Gui also records the user's responses and response times, and passes
 * them back the the TestingEngine to be recorded.<p>
 *
 * The GUI performs much of it's own processing, which is probably a faux pas
 * in GUI design, but that's what we have.<p>
 *
 * The GUI can be thought of as a state-based machine: It has different states
 * that it is in, and will perform different actions depending on its state.
 * The states are defined the two String variables, testType and guiType (these
 * used to be nice enums, but enums do not work in Java 1.4, which was required
 * for old-school Macs).
 *
 * @author  Sam Fentress
 * @version 0.4
 */
public class Gui extends javax.swing.JFrame {
    
    private AoC aoc;
    private GraphicsDevice device = null;
    private TestingEngine engine;
    protected long startTime;
    private long endTime, totalTime;     //The times, from the computer clock, to measure the reaction time.
    private String totalTimeString;
    private boolean userCanAct;
    private boolean hasSound;
    private boolean isSoundCheck;
    
    boolean clipPlayed = false;
    private ClipPlayer clipPlayer;
    
    String testType;        // The testType is passed to the GUI by the testingEngine, using the .csv stimulus file
    String guiType;         // The guiType determines the contents of the jFrame, and is tied to the testType
    
    WindowText windowText;          //Needed to control text for Moving-Window tasks
    
    static String x;
    
    /**
     * Creates new form Gui. Sets up all components, but does not make visible.
     */
    public Gui(AoC aoc, GraphicsDevice d) {
        this.aoc = aoc;
        device = d;
        this.setUndecorated(true);
        this.setResizable(false);
        userCanAct = true;
        
        
        initComponents();
        getContentPane().setBackground(Color.white);
        //    refresh = new Timer(this);
        //    refresh.start();
    }
    
    /**
     * Creates Gui for testing purposes only.
     */
    public Gui(GraphicsDevice d) {
        device = d;
        this.setUndecorated(false);
        this.setResizable(false);
        
        initComponents();
        //   refresh = new Timer(this);
        //    refresh.start();
    }
    
    /**
     * Connects the TestingEngine to the Gui.
     */
    public void setEngine(TestingEngine engine){
        Logger.log(" ... ... setting engine ...");
        this.engine = engine;
    }
    
    public void checkGui(){
        Logger.log(" ... ... Gui exists");
    }
    /**
     * Sets size to be entire screen, and makes visible. Set cursor to be invisible.
     */
    public void makeVisible(){
        
        DisplayMode oldMode = device.getDisplayMode();
        
        this.setSize(new Dimension(oldMode.getWidth(), oldMode.getHeight()));
        this.validate();
        this.setBackground(Color.white);
        this.setForeground(Color.white);
        getContentPane().setBackground(Color.white);
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        Cursor invisCursor = tk.createCustomCursor(tk.createImage(""),new Point(),null);
        this.setCursor(invisCursor);
        
        device.setFullScreenWindow(this);
        
        examplePane.setVisible(false);
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel_OneWordTwoWords = new javax.swing.JPanel();
        label_OWTW_StimWord = new javax.swing.JLabel();
        label_OWTW_Option1 = new javax.swing.JLabel();
        label_OWTW_Option2 = new javax.swing.JLabel();
        jPanel_OneWordTwoPicts = new javax.swing.JPanel();
        PictOption1 = new javax.swing.JPanel();
        StimWord1 = new javax.swing.JLabel();
        PictOption2 = new javax.swing.JPanel();
        instructionsPane = new javax.swing.JScrollPane();
        Instructions = new javax.swing.JEditorPane();
        jPanel_TwoWordsTwoWords = new javax.swing.JPanel();
        label_TWTW_Option1 = new javax.swing.JLabel();
        label_TWTW_Option2 = new javax.swing.JLabel();
        label_TWTW_StimWord1 = new javax.swing.JLabel();
        jPanel_OneSentTwoWords = new javax.swing.JPanel();
        label_OSTW_Option1 = new javax.swing.JLabel();
        label_OSTW_Option2 = new javax.swing.JLabel();
        label_OSTW_StimWord = new javax.swing.JTextArea();
        examplePane = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel_Sound = new javax.swing.JPanel();
        SoundImage = new javax.swing.JPanel();
        jPanel_SoundCheck = new javax.swing.JPanel();
        jPanel_SoundCheck.setVisible(false);
        sound_text1 = new javax.swing.JTextArea();
        sound_text2 = new javax.swing.JTextArea();
        sound_volumeSlider = new javax.swing.JSlider();
        sound_testSound = new javax.swing.JButton();
        sound_text3 = new javax.swing.JTextArea();
        sound_next = new javax.swing.JButton();
        sound_image = new javax.swing.JLabel();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Assessment of Comprehension v0.1");
        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(java.awt.Color.white);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel_OneWordTwoWords.setLayout(new java.awt.GridBagLayout());

        jPanel_OneWordTwoWords.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_OneWordTwoWords.setMinimumSize(new java.awt.Dimension(1035, 430));
        jPanel_OneWordTwoWords.setOpaque(false);
        jPanel_OneWordTwoWords.setPreferredSize(new java.awt.Dimension(1035, 430));
        label_OWTW_StimWord.setBackground(new java.awt.Color(255, 255, 255));
        label_OWTW_StimWord.setFont(new java.awt.Font("Lucida Grande", 0, 36));
        label_OWTW_StimWord.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_OWTW_StimWord.setText("Beginner");
        label_OWTW_StimWord.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        label_OWTW_StimWord.setMaximumSize(new java.awt.Dimension(300, 60));
        label_OWTW_StimWord.setMinimumSize(new java.awt.Dimension(300, 60));
        label_OWTW_StimWord.setPreferredSize(new java.awt.Dimension(300, 60));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 10;
        jPanel_OneWordTwoWords.add(label_OWTW_StimWord, gridBagConstraints);
        label_OWTW_StimWord.getAccessibleContext().setAccessibleName("StimWord");

        label_OWTW_Option1.setBackground(new java.awt.Color(255, 255, 255));
        label_OWTW_Option1.setFont(new java.awt.Font("Lucida Grande", 0, 36));
        label_OWTW_Option1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_OWTW_Option1.setText("Student");
        label_OWTW_Option1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        label_OWTW_Option1.setMaximumSize(new java.awt.Dimension(300, 50));
        label_OWTW_Option1.setMinimumSize(new java.awt.Dimension(300, 50));
        label_OWTW_Option1.setOpaque(true);
        label_OWTW_Option1.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(300, 20, 0, 25);
        jPanel_OneWordTwoWords.add(label_OWTW_Option1, gridBagConstraints);
        label_OWTW_Option1.getAccessibleContext().setAccessibleName("Option1");

        label_OWTW_Option2.setBackground(new java.awt.Color(255, 255, 255));
        label_OWTW_Option2.setFont(new java.awt.Font("Lucida Grande", 0, 36));
        label_OWTW_Option2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_OWTW_Option2.setText("Study");
        label_OWTW_Option2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        label_OWTW_Option2.setMaximumSize(new java.awt.Dimension(300, 50));
        label_OWTW_Option2.setMinimumSize(new java.awt.Dimension(300, 50));
        label_OWTW_Option2.setOpaque(true);
        label_OWTW_Option2.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(300, 25, 0, 20);
        jPanel_OneWordTwoWords.add(label_OWTW_Option2, gridBagConstraints);
        label_OWTW_Option2.getAccessibleContext().setAccessibleName("Option2");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        getContentPane().add(jPanel_OneWordTwoWords, gridBagConstraints);

        jPanel_OneWordTwoPicts.setLayout(new java.awt.GridBagLayout());

        jPanel_OneWordTwoPicts.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_OneWordTwoPicts.setMinimumSize(new java.awt.Dimension(1021, 768));
        jPanel_OneWordTwoPicts.setOpaque(false);
        jPanel_OneWordTwoPicts.setPreferredSize(new java.awt.Dimension(1021, 768));
        PictOption1.setLayout(new java.awt.GridBagLayout());

        PictOption1.setBackground(new java.awt.Color(255, 255, 255));
        PictOption1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PictOption1.setMinimumSize(new java.awt.Dimension(502, 502));
        PictOption1.setOpaque(false);
        PictOption1.setPreferredSize(new java.awt.Dimension(502, 502));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(80, 5, 0, 6);
        jPanel_OneWordTwoPicts.add(PictOption1, gridBagConstraints);

        StimWord1.setBackground(new java.awt.Color(255, 255, 255));
        StimWord1.setFont(new java.awt.Font("Lucida Grande", 0, 36));
        StimWord1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        StimWord1.setText("Cleaned");
        StimWord1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        StimWord1.setMaximumSize(new java.awt.Dimension(300, 60));
        StimWord1.setMinimumSize(new java.awt.Dimension(300, 60));
        StimWord1.setPreferredSize(new java.awt.Dimension(300, 60));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 10;
        jPanel_OneWordTwoPicts.add(StimWord1, gridBagConstraints);

        PictOption2.setLayout(new java.awt.GridBagLayout());

        PictOption2.setBackground(new java.awt.Color(255, 255, 255));
        PictOption2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PictOption2.setMinimumSize(new java.awt.Dimension(502, 502));
        PictOption2.setOpaque(false);
        PictOption2.setPreferredSize(new java.awt.Dimension(502, 502));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(80, 6, 0, 5);
        jPanel_OneWordTwoPicts.add(PictOption2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jPanel_OneWordTwoPicts, gridBagConstraints);

        instructionsPane.setBorder(null);
        instructionsPane.setMinimumSize(new java.awt.Dimension(620, 560));
        instructionsPane.setPreferredSize(new java.awt.Dimension(620, 560));
        Instructions.setEditable(false);
        Instructions.setContentType("text/html");
        Instructions.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        Instructions.setEnabled(false);
        Instructions.setFocusable(false);
        Instructions.setMinimumSize(new java.awt.Dimension(580, 500));
        Instructions.setPreferredSize(new java.awt.Dimension(580, 500));
        Instructions.setRequestFocusEnabled(false);
        instructionsPane.setViewportView(Instructions);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        getContentPane().add(instructionsPane, gridBagConstraints);

        jPanel_TwoWordsTwoWords.setLayout(new java.awt.GridBagLayout());

        jPanel_TwoWordsTwoWords.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_TwoWordsTwoWords.setOpaque(false);
        label_TWTW_Option1.setBackground(new java.awt.Color(255, 255, 255));
        label_TWTW_Option1.setFont(new java.awt.Font("Lucida Grande", 0, 36));
        label_TWTW_Option1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_TWTW_Option1.setText("Student");
        label_TWTW_Option1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        label_TWTW_Option1.setMaximumSize(new java.awt.Dimension(200, 50));
        label_TWTW_Option1.setMinimumSize(new java.awt.Dimension(200, 50));
        label_TWTW_Option1.setOpaque(true);
        label_TWTW_Option1.setPreferredSize(new java.awt.Dimension(200, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(300, 20, 0, 60);
        jPanel_TwoWordsTwoWords.add(label_TWTW_Option1, gridBagConstraints);

        label_TWTW_Option2.setBackground(new java.awt.Color(255, 255, 255));
        label_TWTW_Option2.setFont(new java.awt.Font("Lucida Grande", 0, 36));
        label_TWTW_Option2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_TWTW_Option2.setText("Study");
        label_TWTW_Option2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        label_TWTW_Option2.setMaximumSize(new java.awt.Dimension(200, 50));
        label_TWTW_Option2.setMinimumSize(new java.awt.Dimension(200, 50));
        label_TWTW_Option2.setOpaque(true);
        label_TWTW_Option2.setPreferredSize(new java.awt.Dimension(200, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(300, 60, 0, 20);
        jPanel_TwoWordsTwoWords.add(label_TWTW_Option2, gridBagConstraints);

        label_TWTW_StimWord1.setBackground(new java.awt.Color(255, 255, 255));
        label_TWTW_StimWord1.setFont(new java.awt.Font("Lucida Grande", 0, 36));
        label_TWTW_StimWord1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_TWTW_StimWord1.setText("Beginner");
        label_TWTW_StimWord1.setMaximumSize(new java.awt.Dimension(700, 60));
        label_TWTW_StimWord1.setMinimumSize(new java.awt.Dimension(600, 60));
        label_TWTW_StimWord1.setOpaque(true);
        label_TWTW_StimWord1.setPreferredSize(new java.awt.Dimension(600, 60));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        jPanel_TwoWordsTwoWords.add(label_TWTW_StimWord1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        getContentPane().add(jPanel_TwoWordsTwoWords, gridBagConstraints);

        jPanel_OneSentTwoWords.setLayout(new java.awt.GridBagLayout());

        jPanel_OneSentTwoWords.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_OneSentTwoWords.setOpaque(false);
        label_OSTW_Option1.setBackground(new java.awt.Color(255, 255, 255));
        label_OSTW_Option1.setFont(new java.awt.Font("Lucida Grande", 0, 36));
        label_OSTW_Option1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_OSTW_Option1.setText("Student");
        label_OSTW_Option1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        label_OSTW_Option1.setMaximumSize(new java.awt.Dimension(300, 50));
        label_OSTW_Option1.setMinimumSize(new java.awt.Dimension(300, 50));
        label_OSTW_Option1.setOpaque(true);
        label_OSTW_Option1.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(200, 20, 0, 30);
        jPanel_OneSentTwoWords.add(label_OSTW_Option1, gridBagConstraints);

        label_OSTW_Option2.setBackground(new java.awt.Color(255, 255, 255));
        label_OSTW_Option2.setFont(new java.awt.Font("Lucida Grande", 0, 36));
        label_OSTW_Option2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_OSTW_Option2.setText("Study");
        label_OSTW_Option2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        label_OSTW_Option2.setMaximumSize(new java.awt.Dimension(300, 50));
        label_OSTW_Option2.setMinimumSize(new java.awt.Dimension(300, 50));
        label_OSTW_Option2.setOpaque(true);
        label_OSTW_Option2.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(200, 30, 0, 20);
        jPanel_OneSentTwoWords.add(label_OSTW_Option2, gridBagConstraints);

        label_OSTW_StimWord.setColumns(20);
        label_OSTW_StimWord.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 26));
        label_OSTW_StimWord.setLineWrap(true);
        label_OSTW_StimWord.setRows(5);
        label_OSTW_StimWord.setWrapStyleWord(true);
        label_OSTW_StimWord.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        label_OSTW_StimWord.setEnabled(false);
        label_OSTW_StimWord.setFocusable(false);
        label_OSTW_StimWord.setMinimumSize(new java.awt.Dimension(600, 300));
        label_OSTW_StimWord.setPreferredSize(new java.awt.Dimension(600, 300));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(30, 0, 0, 0);
        jPanel_OneSentTwoWords.add(label_OSTW_StimWord, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        getContentPane().add(jPanel_OneSentTwoWords, gridBagConstraints);

        examplePane.setLayout(new java.awt.GridBagLayout());

        examplePane.setBackground(new java.awt.Color(255, 255, 255));
        examplePane.setMinimumSize(new java.awt.Dimension(1024, 768));
        examplePane.setOpaque(false);
        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 30));
        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Example");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 700, 850);
        examplePane.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        getContentPane().add(examplePane, gridBagConstraints);

        jPanel_Sound.setLayout(new java.awt.GridBagLayout());

        jPanel_Sound.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_Sound.setOpaque(false);
        SoundImage.setBackground(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 700, 0);
        jPanel_Sound.add(SoundImage, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        getContentPane().add(jPanel_Sound, gridBagConstraints);

        jPanel_SoundCheck.setLayout(new java.awt.GridBagLayout());

        jPanel_SoundCheck.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_SoundCheck.setOpaque(false);
        sound_text1.setColumns(27);
        sound_text1.setEditable(false);
        sound_text1.setFont(new java.awt.Font("Lucida Grande", 0, 24));
        sound_text1.setLineWrap(true);
        sound_text1.setRows(4);
        sound_text1.setText("In the following test you will be listening to words through your headphones. Before starting the test, we will first check the computer's volume.");
        sound_text1.setWrapStyleWord(true);
        sound_text1.setBorder(null);
        sound_text1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        sound_text1.setDragEnabled(false);
        sound_text1.setEnabled(false);
        sound_text1.setFocusable(false);
        sound_text1.setMinimumSize(new java.awt.Dimension(610, 100));
        sound_text1.setOpaque(false);
        sound_text1.setPreferredSize(new java.awt.Dimension(610, 100));
        sound_text1.setRequestFocusEnabled(false);
        jPanel_SoundCheck.add(sound_text1, new java.awt.GridBagConstraints());

        sound_text2.setColumns(27);
        sound_text2.setEditable(false);
        sound_text2.setFont(new java.awt.Font("Lucida Grande", 0, 24));
        sound_text2.setLineWrap(true);
        sound_text2.setRows(4);
        sound_text2.setText("Click on the 'Check sound volume' button with your mouse. Then  adjust the volume until it is at a comfortable level.");
        sound_text2.setWrapStyleWord(true);
        sound_text2.setBorder(null);
        sound_text2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        sound_text2.setDragEnabled(false);
        sound_text2.setEnabled(false);
        sound_text2.setFocusable(false);
        sound_text2.setMinimumSize(new java.awt.Dimension(610, 100));
        sound_text2.setOpaque(false);
        sound_text2.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel_SoundCheck.add(sound_text2, gridBagConstraints);

        sound_volumeSlider.setBackground(new java.awt.Color(255, 255, 255));
        sound_volumeSlider.setEnabled(false);
        sound_volumeSlider.setMinimumSize(new java.awt.Dimension(300, 29));
        sound_volumeSlider.setPreferredSize(new java.awt.Dimension(300, 29));
        sound_volumeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sound_volumeSliderStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 13, 0);
        jPanel_SoundCheck.add(sound_volumeSlider, gridBagConstraints);

        sound_testSound.setFont(new java.awt.Font("Lucida Grande", 0, 18));
        sound_testSound.setText("Check sound volume");
        sound_testSound.setEnabled(false);
        sound_testSound.setOpaque(false);
        sound_testSound.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sound_testSoundMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 4, 0);
        jPanel_SoundCheck.add(sound_testSound, gridBagConstraints);

        sound_text3.setColumns(27);
        sound_text3.setEditable(false);
        sound_text3.setFont(new java.awt.Font("Lucida Grande", 0, 24));
        sound_text3.setLineWrap(true);
        sound_text3.setRows(4);
        sound_text3.setText("If you cannot hear the test, raise your hand now. When the volume sounds good, click 'Next' to continue.");
        sound_text3.setWrapStyleWord(true);
        sound_text3.setBorder(null);
        sound_text3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        sound_text3.setDragEnabled(false);
        sound_text3.setEnabled(false);
        sound_text3.setFocusable(false);
        sound_text3.setMinimumSize(new java.awt.Dimension(610, 100));
        sound_text3.setOpaque(false);
        sound_text3.setPreferredSize(new java.awt.Dimension(610, 100));
        sound_text3.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel_SoundCheck.add(sound_text3, gridBagConstraints);

        sound_next.setFont(new java.awt.Font("Lucida Grande", 0, 18));
        sound_next.setText("Next");
        sound_next.setEnabled(false);
        sound_next.setOpaque(false);
        sound_next.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sound_nextMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        jPanel_SoundCheck.add(sound_next, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        jPanel_SoundCheck.add(sound_image, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        getContentPane().add(jPanel_SoundCheck, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void sound_volumeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sound_volumeSliderStateChanged
        JSlider source = (JSlider)evt.getSource();
        int rawGain = (int)source.getValue();               //Value from 0 to 100
        float gain = (float)(rawGain - 50f) * (2f/5f);      //Value from -20.0 to 20.0
        SoundClip.setGain(gain);
        this.requestFocus();
    }//GEN-LAST:event_sound_volumeSliderStateChanged
    
    private void sound_nextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sound_nextMouseClicked
        if (clipPlayer != null)
            clipPlayer.stopTest();
        hideSoundInstructions();
        engine.runNextSubtest();
    }//GEN-LAST:event_sound_nextMouseClicked
    
    private void sound_testSoundMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sound_testSoundMouseClicked
        if (sound_testSound.isEnabled()){
            String clipAddress = "test.wav";
            clipPlayer = new ClipPlayer(this, clipAddress, true);
            clipPlayer.start();
            this.requestFocus();
        }
        sound_testSound.setEnabled(false);
        sound_volumeSlider.setEnabled(true);
    }//GEN-LAST:event_sound_testSoundMouseClicked
    
    /**
     * The various events relating to user key pressing.
     */
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        
        int key = evt.getKeyCode();             // keyboard code for the key that was pressed
        int mask = evt.getModifiersEx();
        endTime = System.currentTimeMillis();   // Current time
        
        if (key == KeyEvent.VK_Q && mask == 128)              //If ctr-Q, exit
            aoc.restart();
        if (System.currentTimeMillis() - startTime < 150)       //If hit key within 1.5/10 of second, don't count
            return;
        if (!userCanAct)                                        //If gui is deliberately frozen (e.g. playing a sound), don't count
            return;
        if (isSoundCheck)                                       //If running soundCheck, allow only mouse inputs
            return;
        
        if (testType.equalsIgnoreCase("INTRO")){                        //If displaying instructions or intro, any key moves you ahead
            engine.runNextSubtest();
            return;
        }
        if (testType.equalsIgnoreCase("INSTRUCTIONS")){
            engine.updateGuiType();
            if (engine.testHasExamples())
                engine.displayNextExample();
            else engine.displayNext();
            return;
        }
        if (testType.equalsIgnoreCase("MW_INSTRUCTIONS")){
            if (key == KeyEvent.VK_DOWN){
                String nextView = windowText.next();
                if (!nextView.equalsIgnoreCase("~nomore~")){
                    Instructions.setText(nextView);
                    return;
                } else {
                    engine.runNextSubtest();
                    return;
                }
            } else return;
            
        }
        
        if (testType.equalsIgnoreCase("ENDTEXT")){          // Go ahead to next subtest
            engine.runNextSubtest();
            return;
        }
        
        if (testType.equalsIgnoreCase("ONE_SENT_MW_TWO_WORDS")){       // If this is a moving-wondows test
            //   if (key == KeyEvent.VK_DOWN){
            String nextView = windowText.previewNext();
            if (!nextView.equalsIgnoreCase("~nomore~")){
                if (key == KeyEvent.VK_DOWN){
                    nextView = windowText.next();
                    long windowTime = System.currentTimeMillis() - startTime;
                    startTime = System.currentTimeMillis();
                    totalTimeString = totalTimeString + Long.toString(windowTime) + ",";
                    label_OSTW_StimWord.setText(nextView);
                    if (windowText.previewNext().equalsIgnoreCase("~nomore~")){
                        label_OSTW_Option1.setText("Yes");
                        label_OSTW_Option2.setText("No");
                    }
                    return;
                } else return;
            }
            
        }
        
        if (testType.equalsIgnoreCase("PARAGRAPH") && guiType.equalsIgnoreCase("GUI_TEXT")){
            long windowTime = System.currentTimeMillis() - startTime;
            startTime = System.currentTimeMillis();
            totalTimeString = totalTimeString + Long.toString(windowTime) + ",";
            System.out.println("Totaltimestring = "  + totalTimeString);
            engine.displayNext();
            return;
        }
        
        if (testType.equalsIgnoreCase("PARAGRAPH") && guiType.equalsIgnoreCase("GUI_1S_2W")){
            guiType = "GUI_TEXT";
            updateGUI();
        }
        
        if (key == KeyEvent.VK_LEFT) {              //If user hits left or right, record time and pass response to TestingEngine
            totalTime = endTime - startTime;
            totalTimeString = totalTimeString +  Long.toString(totalTime);
            if (engine.isExample)
                engine.checkExampleAnswer(0);
            else engine.userAction(0, totalTimeString);
        }
        
        if (key == KeyEvent.VK_RIGHT) {
            totalTime = endTime - startTime;
            totalTimeString = totalTimeString +  Long.toString(totalTime);
            if (engine.isExample)
                engine.checkExampleAnswer(1);
            else engine.userAction(1, totalTimeString);
        }
        
    }//GEN-LAST:event_formKeyPressed
    
    /**
     * Set the Gui type to that represented by the passed string.
     *
     * @param type The string representation of the TestType
     */
    public void setType(String type){
        testType = type;
        guiType = GuiHelper.getGuiType(testType);
        updateGUI();
    }
    
    /**
     * Checks to see if a given String is a valid test type.
     * @param type The String to be checked
     * @return true if the String is a valid test type
     */
    public static boolean checkType(String type){
        String cleanType = type.replaceAll("[yt][nf]", "two_words");
        String tempGuiType = GuiHelper.getGuiType(cleanType);
        return(!(tempGuiType==null));               // Return false if type is not valid
    }
    
    /**
     * Makes the correct JPanel visible depending on the current TestType.
     */
    protected void updateGUI(){
        
        if (guiType.equalsIgnoreCase("GUI_TEXT"))
            hideEverythingExcept(instructionsPane);
        else if (guiType.equalsIgnoreCase("GUI_1W_2W"))
            hideEverythingExcept(jPanel_OneWordTwoWords);
        else if (guiType.equalsIgnoreCase("GUI_1W_2P"))
            hideEverythingExcept(jPanel_OneWordTwoPicts);
        else if (guiType.equalsIgnoreCase("GUI_2W_2W"))
            hideEverythingExcept(jPanel_TwoWordsTwoWords);
        else if (guiType.equalsIgnoreCase("GUI_1S_2W"))
            hideEverythingExcept(jPanel_OneSentTwoWords);
        
        this.setBackground(Color.white);
        this.setForeground(Color.white);
        this.repaint();
        //refreshALL();
    }
    
    private void hideEverythingExcept(Component thisOne){
        Component[] allComponents = this.getContentPane().getComponents();
        for (int i = 0; i < allComponents.length; i++)
            allComponents[i].setVisible(false);
        thisOne.setVisible(true);
    }
    
    public String getGuiType(){
        return guiType.toString();
    }
    
    public void setExample(boolean ex){     // turn on example pane if True
        if (ex)
            examplePane.setVisible(true);
        else examplePane.setVisible(false);
    }
    
    public void setSound(boolean hasSound){
        this.hasSound = hasSound;
    }
    
    /**
     * Sets the values for the stimuli. Depending on the testType, different
     * actions will be taken with the String[] of stimuli.
     *
     * @param values An array of values representing the current stimuli and choices
     */
    public void setValues(String[] values){
        clipPlayed = false;
        
        if (testType.equalsIgnoreCase("Intro") ||
                testType.equalsIgnoreCase("Instructions") ||
                testType.equalsIgnoreCase("Endtext")) {
            Instructions.setText("<font size=6>" + values[0] + "</font>");
            if (values.length > 1){
                String[] auditoryInstructions = new String[values.length - 1];
                for (int i = 0; i < auditoryInstructions.length; i++) {
                    auditoryInstructions[i] = values[i+1];
                    System.out.println("Values[" + (i+1) + "] = " + values[i+1]);
                }
                clipPlayer = new ClipPlayer(this, auditoryInstructions);
                clipPlayer.start();
                this.requestFocus();
            }
        }
        
        if (testType.equalsIgnoreCase("Paragraph")){
            if (!(values[1].length() > 0))
                Instructions.setText("<font size=6>" + values[0] + "</font>");
            else {
                guiType = "GUI_1S_2W";
                updateGUI();
                label_OSTW_StimWord.setVisible(true);
                label_OSTW_StimWord.setText(values[0]);
                label_OSTW_Option1.setText(values[1]);
                label_OSTW_Option2.setText(values[2]);
            }
        }
        
        if (testType.equalsIgnoreCase("MW_INSTRUCTIONS")){
            windowText = new WindowText(values[0],true);
            Instructions.setText(windowText.next());
        }
        
        if (testType.equalsIgnoreCase("ONE_WORD_TWO_WORDS")){
            if (!hasSound){
                label_OWTW_StimWord.setVisible(true);
                label_OWTW_StimWord.repaint();
                label_OWTW_StimWord.setText(values[0]);
                label_OWTW_Option1.setText(values[1]);
                label_OWTW_Option2.setText(values[2]);
            } else {
                label_OWTW_StimWord.setVisible(false);
                //   showHeadphones(true);
                label_OWTW_Option1.setText("");
                label_OWTW_Option2.setText("");
                clipPlayer = new ClipPlayer(this, values, new JLabel[] {label_OWTW_Option1,label_OWTW_Option2});
                clipPlayer.start();
            }
        }
        
        if (testType.equalsIgnoreCase("ONE_WORD_TWO_PICTS")){
            if (!hasSound){
                //   StimWord1.setVisible(true);
                StimWord1.setText(values[0]);
                GuiHelper.addPictures(new String[] {values[1], values[2]}, new JPanel[] {PictOption1, PictOption2}, true);
            } else {
                StimWord1.setVisible(false);
                // StimWord1.setText(values[0]);
                // showHeadphones(true);
                GuiHelper.clearImages(new JPanel[] {PictOption1, PictOption2});
                clipPlayer = new ClipPlayer(this, values, new JPanel[] {PictOption1, PictOption2}, true);
                clipPlayer.start();
                //    GuiHelper.addPictures(new String[] {values[1], values[2]}, new JPanel[] {PictOption1, PictOption2}, true);
                //refreshALL();
            }
        }
        
        if (testType.equalsIgnoreCase("TWO_WORDS_TWO_WORDS")){
            if (!hasSound){
                label_TWTW_StimWord1.setVisible(true);
                String stimulus, whitespace = "";
                if (values[0].length() < values[1].length()){
                    for (int i=0; i<(values[1].length() - values[0].length()); i++){
                        whitespace = whitespace + " ";
                    }
                }
                label_TWTW_StimWord1.setText(values[0] + "    " + whitespace + values[1]);
                label_TWTW_Option1.setText(values[2]);
                label_TWTW_Option2.setText(values[3]);
            } else {
                label_TWTW_StimWord1.setVisible(false);
                //    showHeadphones(true);
                label_TWTW_Option1.setText("");
                label_TWTW_Option2.setText("");
                clipPlayer = new ClipPlayer(this, values, 2, new JLabel[] {label_TWTW_Option1,label_TWTW_Option2});
                clipPlayer.start();
            }
        }
        
        if (testType.equalsIgnoreCase("ONE_SENT_TWO_WORDS")){
            if (!hasSound){
                label_OSTW_StimWord.setVisible(true);
                label_OSTW_StimWord.setText(values[0]);
                label_OSTW_Option1.setText(values[1]);
                label_OSTW_Option2.setText(values[2]);
            } else {
                label_OSTW_StimWord.setVisible(false);
                showHeadphones(true);
                label_OSTW_Option1.setText("");
                label_OSTW_Option2.setText("");
                clipPlayer = new ClipPlayer(this, values, new JLabel[] {label_OSTW_Option1, label_OSTW_Option2});
                clipPlayer.start();
            }
        }
        
        if (testType.equalsIgnoreCase("ONE_SENT_MW_TWO_WORDS")){
            windowText = new WindowText(values[0]);
            label_OSTW_StimWord.setText(windowText.next());
            label_OSTW_Option1.setText("");
            label_OSTW_Option2.setText("");
        }
        
        //  refreshALL();
        startTime = System.currentTimeMillis();             //Start the timer
        totalTimeString = "";
    }
    
    void setSoundOptionValues(String[] values, Component[] optionLabels, boolean optionsAreImages){
        int valueNumber = values.length - optionLabels.length;
        
        for (int i = 0; i < optionLabels.length; i++){
            if (!optionsAreImages){
                JLabel optionLabel = (JLabel)optionLabels[i];
                optionLabel.setText(values[valueNumber++]);
            } else {
                JPanel jPanel = (JPanel)optionLabels[i];
                GuiHelper.addPictures(values[valueNumber++], jPanel, true);
                //refreshALL();
            }
        }
        
        
    }
    
    /*
     * Prevent the user from making any actions. Used when playing sounds.
     */
    public void freeze(){
        userCanAct = false;
    }
    
    /*
     * reverses freeze(): allows the user to act
     */
    public void unfreeze(){
        userCanAct = true;
    }
    
    void showHeadphones(boolean show){
        if (show){
            if (!jPanel_Sound.isVisible()){
                System.out.println("showing headphones");
                jPanel_Sound.setVisible(true);
                URL url = this.getClass().getResource("Headphones.jpg");
                ImageIcon soundImage = new ImageIcon(url);
                JLabel soundImageLabel = new JLabel(soundImage);
                SoundImage.removeAll();
                SoundImage.repaint();
                SoundImage.add(soundImageLabel);
                soundImageLabel.setVisible(true);
            }
        } else {jPanel_Sound.setVisible(false);}
    }
    
    public void showSoundInstructions(){
        hideEverythingExcept(jPanel_SoundCheck);
        showHeadphones(true);
        URL url = this.getClass().getResource("Volume.jpg");
        ImageIcon volumeImage = new ImageIcon(url);
        sound_image.setIcon(volumeImage);
        jPanel_SoundCheck.repaint();
        sound_testSound.setEnabled(true);
        sound_volumeSlider.setEnabled(false);
        sound_next.setEnabled(true);
        
        Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        this.setCursor(normalCursor);
        isSoundCheck = true;
    }
    
    private void hideSoundInstructions(){
        jPanel_SoundCheck.setVisible(false);
        showHeadphones(false);
        sound_testSound.setFocusable(false);
        sound_testSound.setEnabled(false);
        sound_volumeSlider.setFocusable(false);
        sound_volumeSlider.setEnabled(false);
        sound_next.setEnabled(false);
        sound_next.setFocusable(false);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Cursor invisCursor = tk.createCustomCursor(tk.createImage(""),new Point(),null);
        this.setCursor(invisCursor);
        isSoundCheck = false;
        this.requestFocus();
    }
    
    
    private void refreshAll(){
        Component[] allComponents = this.getContentPane().getComponents();
        for (int i = 0; i < allComponents.length; i++) {
            allComponents[i].repaint();
        }
        this.repaint();
    }
    
    /**
     * Used for testing only.
     *
     * @param args No arguments accepted
     */
    public static void main(String args[]) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();   //devices is an array of monitors
        new Gui(devices[0]).makeVisible();
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane Instructions;
    private javax.swing.JPanel PictOption1;
    private javax.swing.JPanel PictOption2;
    private javax.swing.JPanel SoundImage;
    private javax.swing.JLabel StimWord1;
    private javax.swing.JPanel examplePane;
    private javax.swing.JScrollPane instructionsPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel_OneSentTwoWords;
    private javax.swing.JPanel jPanel_OneWordTwoPicts;
    private javax.swing.JPanel jPanel_OneWordTwoWords;
    private javax.swing.JPanel jPanel_Sound;
    private javax.swing.JPanel jPanel_SoundCheck;
    private javax.swing.JPanel jPanel_TwoWordsTwoWords;
    private javax.swing.JLabel label_OSTW_Option1;
    private javax.swing.JLabel label_OSTW_Option2;
    private javax.swing.JTextArea label_OSTW_StimWord;
    private javax.swing.JLabel label_OWTW_Option1;
    private javax.swing.JLabel label_OWTW_Option2;
    private javax.swing.JLabel label_OWTW_StimWord;
    private javax.swing.JLabel label_TWTW_Option1;
    private javax.swing.JLabel label_TWTW_Option2;
    private javax.swing.JLabel label_TWTW_StimWord1;
    private javax.swing.JLabel sound_image;
    private javax.swing.JButton sound_next;
    private javax.swing.JButton sound_testSound;
    private javax.swing.JTextArea sound_text1;
    private javax.swing.JTextArea sound_text2;
    private javax.swing.JTextArea sound_text3;
    private javax.swing.JSlider sound_volumeSlider;
    // End of variables declaration//GEN-END:variables
    
}

/*
 * This class is used to play sounds in a separate thread. While sounds are
 * being played, freeze() is called to prevent user actions. Once the sound
 * has finished playing, the timer is started and the gui is unfrozen.
 */
class ClipPlayer extends Thread {
    private Gui parent;
    private String[] clipAddress;
    private String[] values;
    private Component[] optionLabels;
    private boolean isTest;
    private boolean stopTest;
    private boolean justPlay;
    private boolean optionsAreImages;
    
    public ClipPlayer(Gui g, String clipAddress){
        parent = g;
        this.clipAddress = new String[] {clipAddress};
        justPlay = true;
    }
    
    public ClipPlayer(Gui g, String[] clipAddress){
        parent = g;
        this.clipAddress = clipAddress;
        justPlay = true;
    }
    
    public ClipPlayer(Gui g, String clipAddress, boolean isTest){
        parent = g;
        this.clipAddress = new String[] {clipAddress};
        this.isTest = isTest;
        stopTest = false;
    }
    
    
    public ClipPlayer(Gui g, String[] values, Component[] optionLabels) {
        parent = g;
        this.values = values;
        this.clipAddress = new String[] {values[0]};
        this.optionLabels = optionLabels;
    }
    
    public ClipPlayer(Gui g, String[] values, int numSoundFiles, Component[] optionLabels){
        parent = g;
        this.values = values;
        clipAddress = new String[numSoundFiles];
        for (int i = 0; i < clipAddress.length; i++)
            clipAddress[i] = values[i];
        this.optionLabels = optionLabels;
    }
    
    public ClipPlayer(Gui g, String[] values, Component[] optionLabels, boolean optionsAreImages) {
        this(g, values, optionLabels);
        this.optionsAreImages = optionsAreImages;
    }
    
    public ClipPlayer(Gui g, String[] values, int numSoundFiles, Component[] optionLabels, boolean optionsAreImages){
        this(g, values, numSoundFiles, optionLabels);
        this.optionsAreImages = optionsAreImages;
    }
    
    public void stopTest(){
        stopTest = true;
    }
    
    synchronized public void run() {
        parent.showHeadphones(true);
        if (isTest){
            while (!stopTest){
                SoundClip clip = new SoundClip("Sounds" + File.separator + clipAddress[0]);
                if (clip.isInitialized()) {
                    clip.play();
                    clip.remove();
                }
                try {
                    sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            parent.freeze();
            for (int i=0; i<clipAddress.length; i++){
                System.out.println("playing sound file: " + clipAddress[i]);
                SoundClip clip = new SoundClip("Sounds" + File.separator + clipAddress[i]);
                while(!parent.clipPlayed) {
                    if (clip.isInitialized()) {
                        clip.play();
                        clip.remove();
                        parent.clipPlayed = true;
                    }
                }
                try {
                    sleep(200);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                parent.clipPlayed = false;
            }
            if (!justPlay){
                parent.startTime = System.currentTimeMillis();
                parent.setSoundOptionValues(values, optionLabels, optionsAreImages);
            }
            parent.unfreeze();
        }
    }
}
