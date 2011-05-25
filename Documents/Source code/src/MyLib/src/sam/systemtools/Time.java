/*
 * Time.java
 *
 * Created on July 27, 2006, 1:45 PM
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

/**
 *
 * @author Sam Fentress
 * @version 0.01
 */
public class Time {
    
    /** Creates a new instance of Time */
    public Time() {
    }
    
    public static void pause(long pauseTime){
        long startTime = System.currentTimeMillis();
        long currentTime;
        do {
            currentTime = System.currentTimeMillis();
        } while (currentTime - startTime < pauseTime);
    }
    
}
