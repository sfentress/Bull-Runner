/*
 * Student.java
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
 * @author Sam Fentress
 * @version 0.05
 */

package engine;

/**
 * Contains information on the student taking the test.
 *
 * @author Sam Fentress
 * @version 0.4
 */
public class Student {
    
    private String firstName, lastName;
    private int number;
    private int dobMonth, dobDay, dobYear;
    
    /** Creates a new instance of Student */
    public Student() {
    }
    
    /**
     * Sets student's name, trims away white space, and sets initial letter to capital. (These actions are to assist comparison of previous student data with new data).
     * @param firstName Student's first name
     * @param lastName Student's last name
     */
    public void setName(String firstName, String lastName){
        String tempFirstName = firstName;
        String tempLastName = lastName;
        
        tempFirstName = tempFirstName.trim();
        tempLastName = tempLastName.trim();
        
        String firstNameFirstLetter = tempFirstName.substring(0,1).toUpperCase();
        String lastNameFirstLetter =  tempLastName.substring(0,1).toUpperCase();
        
        this.firstName = firstNameFirstLetter + tempFirstName.substring(1,tempFirstName.length());
        this.lastName = lastNameFirstLetter + tempLastName.substring(1,tempLastName.length());
    }
    
    /**
     * Not used. Method for future possible addition, in case student number is desired.
     * @param number Student number
     */
    public void setNumber(int number){
        this.number = number;
    }
    
    /**
     * Set date of birth. Numbers are not checked for consitancy.
     * @param month Month from 1 to 12. Unchecked.
     * @param day Day from 1 to 31. Unchecked.
     * @param year Two-digit year. Unchecked.
     */
    public void setDOB(int month, int day, int year){
        this.dobMonth = month;
        this.dobDay = day;
        this.dobYear = year;
    }
    
    /**
     * 
     * @return Student name, first and last.
     */
    final public String getName(){
        return firstName + " " + lastName;
    }
    
    /**
     * 
     * @return First name.
     */
    final public String getFirstName(){
        return firstName;
    }
    
    /**
     * 
     * @return Last name
     */
    final public String getLastName(){
        return lastName;
    }
    
    /**
     * 
     * @return Student number.
     */
    final public int getNumber(){
        return number;
    }
    
    /**
     * 
     * @return Month from 1-12.
     */
    final public int getDOBMonth(){
        return dobMonth;
    }
    
    /**
     * 
     * @return Day from 1-31.
     */
    final public int getDOBDay(){
        return dobDay;
    }
    
    /**
     * 
     * @return Two-digit year.
     */
    final public int getDOBYear(){
        return dobYear;
    }
    
    /**
     * 
     * @return Date of birth, as mm/dd/yy.
     */
    final public String getDOB(){
        return "" + dobMonth + "/" + dobDay + "/" + dobYear;
    }
    
}
