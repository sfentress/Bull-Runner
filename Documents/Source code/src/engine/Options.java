/*
 * Options.java
 *
 * Created on December 8, 2006, 11:42 AM
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

package engine;

import sam.fileprocessing.Folders;
import sam.utilities.xml.myXML;
import sam.utilities.Logger;
import sam.systemtools.Key;

import java.util.Calendar;
import java.util.GregorianCalendar;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;

import java.awt.Color;

/**
 * This class works in coordination with Gui_Options to set options, save options
 * to file, and read options from file. The options are stored as an XML file, and
 * are re-written every time the options are saved.
 *
 * @author Sam Fentress
 * @version 0.01
 */
public class Options {
    
    private boolean anyKeyAhead = true;
    private Key keyAhead;
    private Key leftKey;
    private Key rightKey;
    private int fontSize;
    private Color fontColor;
    private Color backColor;
    
    /** Creates a new instance of Options and reads current options from file */
    public Options() {
        processOptions();
    }
    
    
    /**
     * Sets current options and creates XML file (only if set options are different from default options.
     *
     * @param anyKeyAhead Whether any keyboard button will move the program forward. Default is true.
     * @param keyAhead Which key will move program forward. Default is Space.
     * @param leftKey Which key corresponds to the left option. Default is left arrow.
     * @param rightKey Which key corresponds to the right option. Default is right arrow.
     * @param fontSize Size of font.
     * @param fontColor Color of font.
     * @param backColor Color of background.
     */
    public Options(boolean anyKeyAhead, Key keyAhead, Key leftKey, Key rightKey, int fontSize, Color fontColor, Color backColor){
        Logger.log("creating options");
    this.anyKeyAhead = anyKeyAhead;
    this.keyAhead = keyAhead;
    this.leftKey = leftKey;
    this.rightKey = rightKey;
    this.fontSize = fontSize;
    this.fontColor = fontColor;
    this.backColor = backColor;
    
    if (!sameAsDefaults())
        createXML();
    else
        resetDefaults();
    }
    
    /*
     * Creates XML file from option values.
     */
    private void createXML(){
        try{
            myXML options = new myXML("options");
            Calendar cal = new GregorianCalendar();
            options.setXMLComment("<!-- Options created "+ (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR)
                        + "," + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + " -->");
            
            myXML anyKeyAheadVal = options.addElement("anyKeyAhead",new Boolean(anyKeyAhead));
            myXML keyAheadVal = options.addElement("keyAhead",keyAhead);
            myXML leftKeyVal = options.addElement("leftKey",leftKey);
            myXML rightKeyVal = options.addElement("rightKey",rightKey);
            myXML fontSizeVal = options.addElement("fontSize",new Integer(fontSize));
            myXML fontColorVal = options.addElement("fontColor",fontColor);
            myXML backColorVal = options.addElement("backColor",backColor);
                    
            new File("Documents").mkdirs();
            PrintWriter pw = new PrintWriter(new FileWriter("Documents" + File.separator  + "options.xml"), true);
            options.serialize(pw);
        } catch (myXML.myXMLException e){
            Logger.log("Could not create options XML file");
            Logger.log(e.toString());
        } catch (java.io.IOException e){
            Logger.log("Could not create options XML file");
            Logger.log(e.toString());
        }
            
    }
    
    /**
     * Resets all option values to defaults.
     */
    public static void resetDefaults(){
        Logger.log("resetting defaults");
        Folders.deleteFilesBeginingWith("Documents","options","xml");
    }
    
    private boolean sameAsDefaults(){
        if ((anyKeyAhead != true) ||
                !keyAhead.equals(32,1) ||
                !leftKey.equals(37,1) ||
                !rightKey.equals(39,1) ||
                (fontSize != 36) ||
                (fontColor != Color.BLACK) ||
                (backColor != Color.WHITE))
            return false;
        else return true;
    }
    
    private void processOptions(){
        myXML xmlRoot;
        
        File xmlFile = new File("Documents" + File.separator + "options.xml");
        if (xmlFile.exists()){
            try{
                BufferedReader in = new BufferedReader(new FileReader("Documents" + File.separator + "options.xml"));
                xmlRoot = new myXML((BufferedReader)in);
            } catch (Exception e){
                Logger.log("Warning: Could not read options file");
                setValuesToDefaults();
                return;
            }
        } else{
            setValuesToDefaults();
            return;
        }
        
        myXML xmlAnyKeyAhead = xmlRoot.findElement("anyKeyAhead");     
        String test = xmlAnyKeyAhead.getValue();
        this.anyKeyAhead = Boolean.valueOf(test).booleanValue();
        
        myXML xmlKeyAhead = xmlRoot.findElement("keyAhead");
        this.keyAhead = new Key(xmlKeyAhead.getValue());
        
        myXML xmlLeftKey = xmlRoot.findElement("leftKey");
        this.leftKey = new Key(xmlLeftKey.getValue());
        
        myXML xmlRightKey = xmlRoot.findElement("rightKey");
        this.rightKey = new Key(xmlRightKey.getValue());
        
        myXML xmlFontSize = xmlRoot.findElement("fontSize");
        this.fontSize = Integer.parseInt(xmlFontSize.getValue());
        
        myXML xmlFontColor = xmlRoot.findElement("fontColor");
        this.fontColor = Color.getColor(xmlFontColor.getValue());
        
        myXML xmlBackColor = xmlRoot.findElement("backColor");
        this.backColor = Color.getColor(xmlBackColor.getValue());
    }
    
    private void setValuesToDefaults(){
        anyKeyAhead = true;
        keyAhead = new Key(32,1);
        leftKey = new Key(37,1);
        rightKey = new Key(39,1);
        fontSize =36;
        fontColor = Color.BLACK;
        backColor = Color.WHITE;
    }
    
    /**
     * Whether any key moves program forward.
     * @return true if any key moves program forward.
     */
    public boolean isAnyKeyAhead(){return anyKeyAhead;}
    /**
     * Get which key moves program forward.
     * @return Key that moves program forward.
     */
    public Key getKeyAhead(){return keyAhead;}
    /**
     * Get which key corresponds to left option.
     * @return Key that corresponds to left option.
     */
    public Key getLeftKey(){return leftKey;}
    /**
     * Get which key corresponds to right option.
     * @return Key that corresponds to right option.
     */
    public Key getRightKey(){return rightKey;}
    /**
     * 
     * @return Font size
     */
    public int getFontSize(){return fontSize;}
    /**
     * 
     * @return Font color.
     */
    public Color getFontColor(){return fontColor;}
    /**
     * 
     * @return Background color.
     */
    public Color getBackColor(){return backColor;}
    
    
}
