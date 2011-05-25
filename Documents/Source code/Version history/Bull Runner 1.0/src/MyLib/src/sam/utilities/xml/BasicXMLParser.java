/*
 * BasicXMLParser.java
 *
 * Created on October 25, 2006, 3:36 PM
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

package sam.utilities.xml;

/**
 *
 * @author Sam Fentress
 * @version 0.01
 */
import java.util.*;

public class BasicXMLParser
{
	/**
	 * Pass only the name of the tag for example "QUESTION" 
	 */
	public static Vector getXMLValue(String xml, String tag) throws Exception
	{
		String xmlString = new String(xml);
		Vector v = new Vector();
		String beginTagToSearch = "<" + tag + ">";
		String endTagToSearch = "</" + tag + ">";
		
		// Look for the first occurrence of begin tag
		int index = xmlString.indexOf(beginTagToSearch);
		
		
		while(index != -1)
		{
                             // Look for end tag
			// DOES NOT HANDLE <tag Blah />
			int lastIndex = xmlString.indexOf(endTagToSearch);


			// Make sure there is no error
			if((lastIndex == -1) || (lastIndex < index))
				throw new Exception("Parse Error");
			
			// extract the substring
			String subs = xmlString.substring((index + beginTagToSearch.length()), lastIndex) ;
			
                            // Add it to our list of tag values
			v.addElement(subs);

                            // Try it again. Narrow down to the part of string which is not 
                            // processed yet.
			try
			{
				xmlString = xmlString.substring(lastIndex + endTagToSearch.length());
			}
			catch(Exception e)
			{
				xmlString = "";
			}
			
                             // Start over again by searching the first occurrence of the begin tag 
                             // to continue the loop.

			index = xmlString.indexOf(beginTagToSearch);
		}		
		
		return v;
	}	
}
