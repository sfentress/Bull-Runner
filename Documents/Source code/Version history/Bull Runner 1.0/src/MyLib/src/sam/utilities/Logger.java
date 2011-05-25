/*
 * Logger.java
 *
 * Created on October 2, 2006, 2:11 PM
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

package sam.utilities;

import sam.fileprocessing.DataSaver;

/**
 *
 * @author Sam Fentress
 * @version 0.01
 */
public class Logger {
    
    private static boolean verbose;
    
    public static void setVerbose(boolean isVerbose){
        verbose = isVerbose;
    }
    
    public static void log (String message){
        if (verbose)
            System.out.println(message);
    }
    
    public static void print(){

    }
    
}
