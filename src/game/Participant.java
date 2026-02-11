/**
 * ---------------------------------------------------------------------------
 * File name: Participant.java
 * Project name: Project 5
 * ---------------------------------------------------------------------------
 * Creator's name and email: Michael Ng, ngmw01@etsu.edu
 * Course:  CSCI 1260-940 (Intro Computer Sci II)
 * Creation Date: Apr 2, 2021
 * ---------------------------------------------------------------------------
 */

//This class was made after HeroAttributes had already been created.
//HeroAttributes has been altered to inherit from this class.

package game;


/**
 * The Participant acts as an abstract superclass for 
 * the HeroAttributes and Enemy classes.
 *
 * <hr>
 * Date created: Apr 2, 2021
 * <hr>
 * @author Michael Ng
 */
public abstract class Participant
{
	protected String name;
	protected Equipment equipment;
	
	
	/**
	 * @return name
	 */
	public String getName ( )
	{
		return name;
	}

	
	/**
	 * @return equipment
	 */
	public Equipment getEquipment ( )
	{
		return equipment;
	}

	
	/**
	 * The toString method of the class.     
	 *
	 * <hr>
	 * Date created: Mar 31, 2021 
	 *
	 * <hr>
	 * @return String - Message
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		String message = name;
		
		message += "\n---------------";
		message += "\nName: " + name;
		message += "\nEquipment: " + equipment.toString ( );
		
		return message;
	}
	
	/**
	 * Gets information from the Equipment class.   
	 *
	 * <hr>
	 * Date created: Apr 1, 2021
	 *
	 * <hr>
	 * @param type
	 * @return message
	 */
	public String getEquipmentInfo(String type) 
	{
		String message = "";
		type = type.toUpperCase ( ).trim();
		if(type.equals ("WEAPON")) 
		{
			message += "\n" + equipment.toString ("WEAPON");
		}
		else if (type.equals("ARMOR"))
		{
			message += equipment.toString ("ARMOR");
		}
		else 
		{
			message += equipment.toString ("ACCESSORY");
		}
		
		return message;
	}
}
