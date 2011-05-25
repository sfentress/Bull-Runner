/*
 * GuiHelper.java
 *
 * Created on October 18, 2006, 2:00 PM
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

package gui;

import sam.utilities.Logger;

import javax.swing.JPanel;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.io.File;
import java.util.Arrays;

/**
 *
 * @author Sam Fentress
 * @version 0.01
 */
class GuiHelper {
    
    public static String getGuiType(String testType){
        String guiType = "";
        
        if (testType.equalsIgnoreCase("INTRO") ||
                testType.equalsIgnoreCase("INSTRUCTIONS") ||
                testType.equalsIgnoreCase("MW_INSTRUCTIONS") ||
                testType.equalsIgnoreCase("ENDTEXT") ||
                testType.equalsIgnoreCase("PARAGRAPH_TWO_WORDS") ||
                testType.equalsIgnoreCase("PARAGRAPH"))
            guiType = "GUI_TEXT";
        else if (testType.equalsIgnoreCase("ONE_WORD_TWO_WORDS") ||
                testType.equalsIgnoreCase("ONE_WORD"))
            guiType = "GUI_1W_2W";
        else if (testType.equalsIgnoreCase("ONE_WORD_TWO_PICTS"))
            guiType = "GUI_1W_2P";
        else if (testType.equalsIgnoreCase("TWO_WORDS_TWO_WORDS") ||
                testType.equalsIgnoreCase("TWO_WORDS"))
            guiType = "GUI_2W_2W";
        else if (testType.equalsIgnoreCase("ONE_SENT_TWO_WORDS") ||
                testType.equalsIgnoreCase("ONE_SENT_MW_TWO_WORDS") ||
                testType.equalsIgnoreCase("ONE_SENT") ||
                testType.equalsIgnoreCase("ONE_SENT_MW"))
            guiType = "GUI_1S_2W";
        else if (testType.equalsIgnoreCase("ONE_SENT_TWO_PICTS"))
            guiType = "GUI_1S_2P";
        else guiType = null;
        
        return guiType;
    }
    
    public static void addPictures(String[] fileNames, JPanel[] jPanels, boolean border){
        for (int i = 0; i < fileNames.length; i++) {
            Logger.log("adding " + fileNames[i] + "as picture");
            String imageFile = "Images" + File.separator + fileNames[i];
            ImageIcon image = new ImageIcon(imageFile);
            JLabel imageLabel = new JLabel(image);
            
            java.awt.GridBagConstraints gridBagConstraints;
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            
            if (border)
                jPanels[i].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            
            jPanels[i].removeAll();
            jPanels[i].add(imageLabel, gridBagConstraints);
            imageLabel.repaint();
            jPanels[i].repaint();
            jPanels[i].setVisible(false);
            jPanels[i].setVisible(true);
        } 
    }
    
    public static void addPictures(String fileName, JPanel jPanel, boolean border){
        addPictures(new String[]{fileName}, new JPanel[]{jPanel}, border);
    }
    
    public static void clearImages(JPanel[] jPanels){
        for (int i = 0; i < jPanels.length; i++) {
            jPanels[i].removeAll();
            jPanels[i].repaint();
        }
    }
    
}
