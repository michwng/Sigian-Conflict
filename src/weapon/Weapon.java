/**
 * ---------------------------------------------------------------------------
 * File name: Weapon.java
 * Project name: Project 5
 * ---------------------------------------------------------------------------
 * Creator's name and email: Michael Ng, ngmw01@etsu.edu
 * Course:  CSCI 1260-940 (Intro Computer Sci II)
 * Creation Date: Mar 28, 2021
 * ---------------------------------------------------------------------------
 */

package weapon;


/**
 * The Base Class for a weapon. Will extend to all weapons.
 *
 * <hr>
 * Date created: Mar 28, 2021
 * <hr>
 * @author Michael Ng
 */
public class Weapon
{
	private String weaponName, description, modDescription;
	private int clipSize, rps, damage, bonusDamage, totalDamage, weight;
	private double accuracy, bonusAccuracy, totalAccuracy;
	
	/**
	 * The default Constructor for the Weapon class.   
	 *
	 * <hr>
	 * Date created: Mar 28, 2021 
	 *
	 *
	 */
	public Weapon() 
	{
		weaponName = "Weapon";
		description = "";
		modDescription = "";
		clipSize = 10;
		rps = 2;
		damage = 20;
		bonusDamage = 0;
		totalDamage = damage + bonusDamage;
		accuracy = 0.8;
		bonusAccuracy = 0;
		totalAccuracy = accuracy + bonusAccuracy;
	}
	
	/**
	 * The Parameterized Constructor for the Weapon class.
	 *
	 * <hr>
	 * Date created: Mar 28, 2021 
	 *
	 * 
	 * @param weaponName
	 * @param clipSize
	 * @param damage
	 * @param bonusDamage
	 * @param accuracy
	 * @param bonusAccuracy
	 */
	public Weapon(String weaponName, String description, String modDescription, int clipSize, int rps, int damage, int bonusDamage, int weight, double accuracy, double bonusAccuracy) 
	{
		this.weaponName = weaponName;
		this.description = description;
		this.modDescription = modDescription;
		this.clipSize = clipSize;
		this.rps = rps;
		this.damage = damage;
		this.bonusDamage = bonusDamage;
		this.totalDamage = damage + bonusDamage;
		this.weight = weight;
		this.accuracy = accuracy;
		this.bonusAccuracy = bonusAccuracy;
		totalAccuracy = accuracy + bonusAccuracy;
	}

		
	/**
	 * @return weaponName
	 */
	public String getWeaponName ( )
	{
		return weaponName;
	}

	
	/**
	 * @return description
	 */
	public String getDescription ( )
	{
		return description;
	}
	
	
	/**
	 * @return modDescription
	 */
	public String getModDescription ( )
	{
		return modDescription;
	}

	/**
	 * @return clipSize
	 */
	public int getClipSize ( )
	{
		return clipSize;
	}
	
	
	/**
	 * @return rps
	 */
	public int getRps ( )
	{
		return rps;
	}

	/**
	 * @return weight
	 */
	public int getWeight ( )
	{
		return weight;
	}

	
	/**
	 * @return totalDamage
	 */
	public int getTotalDamage ( )
	{
		return totalDamage;
	}

	/**
	 * @return totalAccuracy
	 */
	public double getTotalAccuracy ( )
	{
		return totalAccuracy;
	}
	
	/**
	 * The toString method of the Weapon class.
	 * Returns a description of the weapon.   
	 *
	 * <hr>
	 * Date created: Mar 28, 2021 
	 *
	 * <hr>
	 * @return message
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		String message = "";
			
		message += weaponName;
		message += "\n---------------";
		message += "\nClip Size: " + clipSize;
		message += "\nRounds per Second: " + rps;
		message += "\nDamage: " + damage + "  (+" + bonusDamage + ")";
		message += "\nAccuracy: " + (accuracy*100) + "%  (+" + (bonusAccuracy*100) + "%)";
		message += "\nWeight: " + weight + "lbs";
		message += "\nDescription: " + description;
		message += "\nMod Description: " + modDescription + "\n\n";
		
		return message;
	}
	
}
