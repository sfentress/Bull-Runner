/*
 * Sort.java
 *
 * Created on August 2, 2006, 12:19 PM
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

import java.util.Vector;

/**
 *
 * @author Sam Fentress
 * @version 0.01
 */
public class Sort {
    
    public static void shuffle(int[] A) {
        for (int lastPlace = A.length-1; lastPlace > 0; lastPlace--) {
            // Choose a random location from among 0,1,...,lastPlace.
            int randLoc = (int)(Math.random()*(lastPlace+1));
            // Swap items in locations randLoc and lastPlace.
            int temp = A[randLoc];
            A[randLoc] = A[lastPlace];
            A[lastPlace] = temp;
        }
    }
    
   // public static Object[] reorder(Object[] A, int[] order){
   //     Object[] B = new Object[A.length];
   //     for (int i = 0; i < A.length; i++) {
   //         B[order[i]] = A[i];
   //     }
   //     return B;
   // }
    
    public static Vector reorder(Vector A, int[] order){
        Vector B = new Vector();
        String s = "";
        for (int i = 0; i < A.size(); i++) {
            B.add(s);
        }
        for (int i = 0; i < A.size(); i++) {
            int insertPosition = order[i];
            B.setElementAt(A.elementAt(i), insertPosition);
        }
        return B;
    }
    
    public static void quickSort(int[] A){
        try {
            doQuickSort(A, 0, A.length-1);
        } catch (Exception e){
        }
    }
    
    private static void doQuickSort(int a[], int lo0, int hi0) throws Exception {
        int lo = lo0;
        int hi = hi0;
        //   pause(lo, hi);
        if (lo >= hi) {
            return;
        } else
            if( lo == hi - 1 ) {
            /* 
             * sort a two element list by swapping if necessary 
             */
            if (a[lo] > a[hi]) {
                int T = a[lo];
                a[lo] = a[hi];
                a[hi] = T;
            }
            return;
            }
        /* 
         * Pick a pivot and move it out of the way 
         */
        int pivot = a[(lo + hi) / 2];
        a[(lo + hi) / 2] = a[hi]; a[hi] = pivot;
        while( lo < hi ) {
            /* 
             * Search forward from a[lo] until an element is found that 
             * is greater than the pivot or lo >= hi 
             */
            while (a[lo] <= pivot && lo < hi) {
                lo++;
            }
            /* 
             * Search backward from a[hi] until element is found that 
             * is less than the pivot, or lo >= hi 
             */
            while (pivot <= a[hi] && lo < hi ) {
                hi--;
            }
            /* 
             * Swap elements a[lo] and a[hi] 
             */
            if( lo < hi ) {
                int T = a[lo];
                a[lo] = a[hi];
                a[hi] = T;
                //           pause();
            }
        }
        /* 
         * Put the median in the "center" of the list 
         */
        a[hi0] = a[hi];
        a[hi] = pivot;
        /* 
         * Recursive calls, elements a[lo0] to a[lo-1] are less than or
         * equal to pivot, elements a[hi+1] to a[hi0] are greater than 
         * pivot. 
         */
        doQuickSort(a, lo0, lo-1);
        doQuickSort(a, hi+1, hi0);
    }
    
    public static void main(String[] args) {
        int a[] = {10,9,8,6,7,5,4,3,2,1};
        quickSort(a);
        System.out.println(java.util.Arrays.toString(a));
    }
}
