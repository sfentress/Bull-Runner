/*
 * ArrayUtils.java
 *
 * Created on November 3, 2006, 2:24 PM
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

import java.util.Arrays;

/**
 *
 * @author Sam Fentress
 * @version 0.01
 */
public class ArrayUtils {
    
    public static String[] trim(String[] A){
        int startBlanks = 0;
        int endBlanks = 0;
        boolean finishedWithStartBlanks = false;
        for (int i = 0; i < A.length; i++) {
            String x =  A[i];
            if (x.equals("")){
                if (!finishedWithStartBlanks)
                    startBlanks++;
                else if (isBlankTillTheEnd(A,i))
                    endBlanks++;
            }
            else finishedWithStartBlanks = true;
        }
        
        String[] trimmedArray = new String[A.length - (startBlanks + endBlanks)];
        
        for (int i = 0; i < trimmedArray.length; i++) {
            trimmedArray[i] = A[i+startBlanks];
        }
        return trimmedArray;
    }
    
    private static boolean isBlankTillTheEnd(String[] A, int index){
        for (int i = index; i < A.length; i++) {
            String x = A[i];
            if (!x.equals(""))
                return false;
        }
        return true;
    }
    
}
