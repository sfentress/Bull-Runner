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
    
    public void setNumber(int number){
        this.number = number;
    }
    
    public void setDOB(int month, int day, int year){
        this.dobMonth = month;
        this.dobDay = day;
        this.dobYear = year;
    }
    
    final public String getName(){
        return firstName + " " + lastName;
    }
    
    final public String getFirstName(){
        return firstName;
    }
    
    final public String getLastName(){
        return lastName;
    }
    
    final public int getNumber(){
        return number;
    }
    
    final public int getDOBMonth(){
        return dobMonth;
    }
    
    final public int getDOBDay(){
        return dobDay;
    }
    
    final public int getDOBYear(){
        return dobYear;
    }
    
    final public String getDOB(){
        return "" + dobMonth + "/" + dobDay + "/" + dobYear;
    }
    
}
