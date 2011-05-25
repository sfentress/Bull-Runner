/*
 * Key.java
 *
 * Created on December 8, 2006, 2:42 PM
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

package sam.systemtools;

import java.awt.event.KeyEvent;

/**
 *
 * @author Sam Fentress
 * @version 0.01
 */
public class Key {
    
    public final int KEY_CODE;
    public final int KEY_LOCATION;
    public final char KEY_CHAR;
    private String keyString = "";
    private boolean isSkeletonKey = false;
    
    
    /** Creates a new instance of Key */
    public Key(int keyCode, int keyLocation, char keyChar) {
        this.KEY_CODE = keyCode;
        this.KEY_LOCATION = keyLocation;
        this.KEY_CHAR = keyChar;
    }
    
    public Key(int keyCode, int keyLocation) {
        this.KEY_CODE = keyCode;
        this.KEY_LOCATION = keyLocation;
        this.KEY_CHAR = '?';
    }
    
    public Key(String keyCodeAndLocation){
        String[] codeAndLocation = keyCodeAndLocation.split(",");
        this.KEY_CODE = Integer.parseInt(codeAndLocation[0]);
        this.KEY_LOCATION = Integer.parseInt(codeAndLocation[1]);
        this.KEY_CHAR = '?';
    }
    
    public Key(boolean isSkeletonKey){
        this.isSkeletonKey = isSkeletonKey;
        this.KEY_CHAR = 0;
        this.KEY_CODE = 0;
        this.KEY_LOCATION = 0;
    }
    
    public boolean isSkeletonKey(){return isSkeletonKey;}
    
    public String getString(){
        switch (KEY_CODE){
            case 0:
                keyString = "(SKELETON)";
                break;
            case 8:
                keyString = "DEL";
                break;
            case 10:
                keyString = "ENT";
                break;
            case 16:
                if (KEY_LOCATION == 2)
                    keyString = "L. Sft";
                else keyString = "R. Sft";
                break;
            case 17:
                if (KEY_LOCATION == 2)
                    keyString = "L. Ctr";
                else keyString = "R. Ctr";
                break;
            case 18:
                if (KEY_LOCATION == 2)
                    keyString = "L. Alt";
                else keyString = "R. Alt";
                break;
            case 32:
                keyString = "SPACE";
                break;
            case 37:
                keyString = "L. Arrow";
                break;
            case 38:
                keyString = "U. Arrow";
                break;
            case 39:
                keyString = "R. Arrow";
                break;
            case 40:
                keyString = "D. Arrow";
                break;
            case 157:
                if (KEY_LOCATION == 2)
                    keyString = "L. Apl";
                else keyString = "R. Apl";
                break;
            default: keyString = KeyEvent.getKeyText(KEY_CODE);
        }
        
        return keyString;
    }
    
    public String toString(){
        return "" + KEY_CODE + "," + KEY_LOCATION;
    }
    
    public boolean equals(Key otherKey){
        if (((this.KEY_CODE == otherKey.KEY_CODE) &&
                (this.KEY_LOCATION == otherKey.KEY_LOCATION)) ||
                isSkeletonKey || otherKey.isSkeletonKey())
            return true;
        else return false;
    }
    
    public boolean equals(int otherKeyCode, int otherKeyLocation){
        if (((this.KEY_CODE == otherKeyCode) &&
                (this.KEY_LOCATION == otherKeyLocation)) ||
                isSkeletonKey)
            return true;
        else return false;
    }
    
    public boolean isLetter(){
        return ((KEY_CODE > 64) && (KEY_CODE < 91));
    }
    
    public boolean isNumber(){
        return ((KEY_CODE > 47) && (KEY_CODE < 58));
    } 
    
    public int getNumber(){return (KEY_CODE-48);}
}
