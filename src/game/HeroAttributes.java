/**
 * ---------------------------------------------------------------------------
 * File name: HeroAttributes.java
 * Project name: Project 5
 * ---------------------------------------------------------------------------
 * Creator's name and email: Michael Ng, ngmw01@etsu.edu
 * Course:  CSCI 1260-940 (Intro Computer Sci II)
 * Creation Date: Mar 28, 2021
 * ---------------------------------------------------------------------------
 */

package game;
import java.util.ArrayList;
import armor.Armor;
import armor.BodyPart;
import weapon.Weapon;

/**
 * The HeroAttributes class is responsible for housing the hero's
 * HP, SP, Experience, and their Equipment.
 *
 * <hr>
 * Date created: Mar 28, 2021
 * <hr>
 * @author Michael Ng
 */
public class HeroAttributes extends Participant
{
	//String name and Equipment equipment extend from Participant.
	//Their getter and setter methods are included.
	private int maxHP, HP, maxSP, SP, level, exp, expLevelUp;
	private ArrayList<String> specialSkills = new ArrayList<>();
	
	/**
	 * The Default Constructor of the HeroAttributes class.        
	 *
	 * <hr>
	 * Date created: Mar 31, 2021 
	 *
	 *
	 */
	public HeroAttributes() 
	{
		name = "Pvt. James E. Clipse";
		equipment = new Equipment();
		maxHP = 100;
		HP = maxHP;
		maxSP = 50;
		SP = maxSP;
		level = 1;
		exp = 0;
		expLevelUp = 100;
	}
	
	/**
	 * 
	 * A partially parameterized Constructor of the HeroAttributes class. 
	 * Accepts a String heroName and is used in a new game.
	 *
	 * <hr>
	 * Date created: Mar 31, 2021 
	 *
	 * 
	 * @param heroName
	 */
	public HeroAttributes(String heroName) 
	{
		name = "Pvt. " + heroName;
		equipment = new Equipment();
		maxHP = 100;
		HP = maxHP;
		maxSP = 50;
		SP = maxSP;
		level = 1;
		exp = 0;
		expLevelUp = 100;
	}
	
	/**
	 * A parameterized Constructor.
	 * This constructor is used when a save file is imported. 
	 *
	 * <hr>
	 * Date created: Mar 31, 2021 
	 *
	 * 
	 * @param heroName
	 * @param equipment
	 * @param maxHP
	 * @param maxSP
	 */
	public HeroAttributes(String heroName, int maxHP, int maxSP, int level, int exp, int expLevelUp) 
	{
		name = heroName;
		
		//This will be added to accordingly in the Driver's loadGame method.
		equipment = new Equipment();
		
		this.maxHP = maxHP;
		HP = maxHP;
		this.maxSP = maxSP;
		SP = maxSP;
		this.level = level;
		this.exp = exp;
		this.expLevelUp = expLevelUp;
		restoreSpecialSkills();
	}
	

	/**
	 * @return maxHP
	 */
	public int getMaxHP ( )
	{
		return maxHP;
	}

	
	/**
	 * @return hP
	 */
	public int getHP ( )
	{
		return HP;
	}

	
	/**
	 * @param hP the hP to set
	 */
	public void setHP (int hP)
	{
		HP = hP;
		if(HP < 0) 
		{
			HP = 0;
		}
		else if(HP > maxHP) 
		{
			HP = maxHP;
		}
	}

	
	/**
	 * @return maxSP
	 */
	public int getMaxSP ( )
	{
		return maxSP;
	}

	
	/**
	 * @param maxSP the maxSP to set
	 */
	public void setMaxSP (int maxSP)
	{
		this.maxSP = maxSP;
	}

	
	/**
	 * @return sP
	 */
	public int getSP ( )
	{
		return SP;
	}

	
	/**
	 * @param sP the sP to set
	 */
	public void setSP (int sP)
	{
		SP = sP;
		if(SP < 0) 
		{
			SP = 0;
		}
		else if (SP > maxSP) 
		{
			SP = maxSP;
		}
	}
	
	
	/**
	 * @return level
	 */
	public int getLevel ( )
	{
		return level;
	}

	
	/**
	 * @return exp
	 */
	public int getExp ( )
	{
		return exp;
	}

	
	/**
	 * @return expLevelUp
	 */
	public int getExpLevelUp ( )
	{
		return expLevelUp;
	}

	
	/**
	 * @return specialSkills
	 */
	public ArrayList <String> getSpecialSkills ( )
	{
		return specialSkills;
	}
	
	/**
	 * This method adds to experience.
	 *
	 * <hr>
	 * Date created: Apr 2, 2021
	 *
	 * <hr>
	 * @param amount
	 */
	public void addExperience(int amount) 
	{
		exp += amount;
	}
	
	
	/**
	 * returns a boolean based on if the player can level up.
	 *
	 * <hr>
	 * Date created: Apr 2, 2021
	 *
	 * <hr>
	 * @return boolean
	 */
	public boolean canLevelUp() 
	{
		//If the player has more experience than the leveling threshold.
		if(exp >= expLevelUp) 
		{
			return true;
		}
		return false;
	}
	

	/**
	 * Level up the player. Gain skills and equipment upon level up.     
	 *
	 * <hr>
	 * Date created: Apr 2, 2021
	 *
	 * <hr>
	 * @return message
	 */
	public String levelUp() 
	{
		int levelUps = 0;
		String message = "Congratulations! You have leveled up.";

		while(exp >= expLevelUp) 
		{
			level++;
			levelUps++;
			exp -= expLevelUp;
			expLevelUp *= 1.1;
			
			maxHP += 100;
			HP += 100;
			maxSP += 5;
			SP += 5;
			
			if(level == 2) 
			{
				specialSkills.add("Sharpshooter");
				equipment.addArmor(new Armor(BodyPart.Head, "Headset", "A standard issue army helmet with a modification.", "Headset - Talk and chat freely with nearby teammates.", 5, 0, 1, 5));
				equipment.addArmor (new Armor(BodyPart.Torso, "Vest", "Bullet-proof vest (mostly).", "Pockets - Store many things!", 20, 0, 1, 2));
				equipment.addArmor (new Armor(BodyPart.Hands, "Half Gloves", "Gloves that improve grip on a weapon without covering the entire hand.", "Grip - No butterfingers here.", 0, 0, 0, 5));
				equipment.addArmor (new Armor(BodyPart.Hands, "Latex Gloves", "Gloves that keep your hands safe and clean.", "Sanitary - No germs here.", 0, 1, 0, 5));
				equipment.addArmor (new Armor(BodyPart.Legs, "Shorts", "Even more flexible than Cargo Pants!", "Flexible 2 - Leg day is a great day.", 0, 0, 0, 10));
				equipment.addArmor (new Armor(BodyPart.Legs, "Suit Pants", "Black pants that allow for a formal look.", "Lucky - Those Allies won't mind.", 0, 1, 0, 15));
				equipment.addArmor (new Armor(BodyPart.Feet, "Slippers", "Standard boots may get too hot. Wear these cool sandals instead!", "Cool - That foot will catch that nice breeze.", 0, 0, 0, 10));
				equipment.addArmor (new Armor(BodyPart.Feet, "Dress Shoes", "Look formal by walking in black dress shoes.", "Formal - It's easy to set yourself apart from the others.", 0, 2, 1, 12));
				equipment.addArmor (new Armor(BodyPart.Accessory, "Spyglass", "Become a pirate and gaze into the horizon.", "Far Sight - This must be what an Eagle sees.", 0, 0, 0, 9));
				equipment.addArmor (new Armor(BodyPart.Accessory, "Mask", "Change your own persona! Actually, this is a surgical mask...", "Sanitary - No germs are getting through that mask.", 0, 1, 0, 8));
				equipment.addArmor (new Armor(BodyPart.Accessory2, "Wristwatch", "Easily keep track of the time by looking at your wrist.", "Track Time - It's no place to be late.", 0, 1, 0, 5));
				equipment.addArmor (new Armor(BodyPart.Accessory2, "Rose", "Nice to have handy in unexpected situations.", "Rose - A healthy red keeps your hopes high.", 0, 0, 0, 6));
				equipment.addWeapon (new Weapon("Carris M3", "A pistol manufactured by Carristellar. It has much power, but is heavy to wield.", "This weapon has no mods.", 20, 2, 40, 0, 3, .80, 0));
				message += "\nWelcome to Level 2! You have earned a new special skill and more equipment! \nNext threshold at Level 5.";
			}
			else if(level == 5) 
			{
				equipment.addWeapon (new Weapon("EBR 1100X", "A modified standard rifle that is now semi-automatic. It also has a larger clip size.", "8X Sights - Increase Accuracy by 20%.", 15, 2, 100, 0, 12, .78, .20));
				equipment.addWeapon (new Weapon("Cal M4B", "A supremely hefty pistol developed by Callus. The strongest but heaviest of all pistols.", "This weapon has no mods.", 18, 2, 80, 0, 5, .75, 0));
				message += "\nWelcome to Level 5! You have unlocked a stronger rifle! \nNext threshold at Level 8.";
			}
			else if(level == 8) 
			{
				equipment.addWeapon (new Weapon("Legion 5li", "A pistol manufactured by Legion. The lightest and most accurate among all pistols.", "This weapon has no mods.", 20, 4, 20, 0, 0, 1.0, 0));
				equipment.addWeapon (new Weapon("Amber Carbine", "A semi-automatic rifle manufactured by Amber Inc. It has very little recoil.", "Iron Sights - Increase Accuracy by 5%.", 12, 3, 100, 0, 10, .91, .05));
				equipment.addWeapon (new Weapon("Smoke A8", "A pistol manufactured by Smokestack. A strong pistol with a large clip size.", "This weapon has no mods.", 45, 5, 12, 0, 4, .85, 0));
				message += "\nWelcome to Level 8! You have unlocked more weapons! \nNext threshold at Level 10.";
			}
			else if(level == 10) 
			{
				specialSkills.add("Critical Shot");
				equipment.addArmor(new Armor(BodyPart.Head, "Kevlar Helmet", "A handy kevlar helmet. The padding is nice.", "Padding - One can get comfortable here.", 10, 2, 2, 7));
				equipment.addArmor(new Armor(BodyPart.Torso, "Kevlar Jacket", "A Kevlar Jacket. Man, this is heavy.", "Security - This is a lot of padding.", 25, 5, 14, 0));
				equipment.addArmor(new Armor(BodyPart.Hands, "Kevlar Gloves", "Kevlar gloves used to improve grip on a weapon and keep your hands safe.", "Safe - these hands are safe.", 5, 2, 3, 2));
				equipment.addArmor(new Armor(BodyPart.Legs, "Kevlar Pants", "Kevlar pants that provide protection for the wearer.", "Safety - These pants are safe to wear.", 10, 5, 7, 1));
				equipment.addArmor(new Armor(BodyPart.Feet, "Kevlar Boots", "Protect those feet with Kevlar Boots!", "Hot - That's too much padding on boots.", 7, 0, 3, 0));
				equipment.addArmor(new Armor(BodyPart.Accessory, "Necktie", "Formality at its finest. Tying a tie is harder than it sounds.", "Tied Up - Up, Down, Across... What?", 0, 0, 0, 12));
				equipment.addArmor(new Armor(BodyPart.Accessory2, "Deodorant", "In case one doesn't have time to shower.", "Deodorant - The scent of greatness.", 0, 0, 0, 7));
				message += "\nWelcome to Level 10! You have earned a new special skill and more equipment! \nNext threshold at Level 15.";
			}
			else if(level == 15) 
			{
				equipment.addWeapon (new Weapon("Smoke BR715", "A  powerful bolt-action battle rifle manufactured by Smokestack. Has low clip size.", "Iron Sights - Increase Accuracy by 5%.", 6, 1, 700, 0, 9, .80, .05));
				equipment.addWeapon (new Weapon("Legion BRM280", "A fancy semi-automatic rifle issued to higher-ups. The most accurate of rifles.", "4x Scope - Increase Accuracy by 15%.", 20, 5, 100, 0, 7, .85, .15));
				equipment.addWeapon (new Weapon("Amber AAR50", "One of the few Assault Rifles of this era. Powerful in the right hands.", "Iron Sights - Increase Accuracy by 5%", 36, 8, 100, 0, 14, .75, .05));
				message += "\nWelcome to Level 8! You have unlocked more weapons! \nNext threshold at Level 20.";
			}
			else if(level == 20) 
			{
				specialSkills.add ("Weak Point");
				equipment.addWeapon (new Weapon("Cal ARX", "Cal's first model of their Assualt Rifle. Quite difficult to wield, but powerful.", "Iron Sights - Increase Accuracy by 5%", 20, 5, 220, 0, 20, .65, .05));
				equipment.addArmor (new Armor(BodyPart.Head, "Head Plate", "A metal helmet that provides supreme protection.", "Security 2 - Wearing this makes one feel *very* safe.", 10, 5, 3, 0));
				equipment.addArmor (new Armor(BodyPart.Torso, "Chestplate", "A medieval knight's chestplate. Super heavy.", "Ricochet - Those bullets will bounce right off!", 35, 5, 20, 1));
				equipment.addArmor (new Armor(BodyPart.Hands, "Metal Gloves", "Metal gloves used to keep one's hands safe. Not so great for weapon handling.", "No Grip - Oops. Butterfingers.", 10, 0, 5, -2));
				equipment.addArmor (new Armor(BodyPart.Legs, "Metal Pants", "Metal pants that provide immense protection for the wearer.", "Ricochet - Those bullets will bounce right off.", 15, 5, 10, 1));
				equipment.addArmor (new Armor(BodyPart.Feet, "Metal Boots", "Metal boots made to protect a knight's feet. Heavy.", "Metal Feet - Lifting a foot is harder than it looks.", 5, 5, 5, 0));
				message += "\nWelcome to Level 20! You have earned a new special skill and more equipment! \nNext Threshold at Level 25.";
			}
			else if(level == 25) 
			{
				specialSkills.add ("Unload");
				equipment.addWeapon (new Weapon("Carris AR90", "An assualt rifle developed by Carristellar. \nPower can be felt when firing this weapon.", "Iron Sights - Increase Accuracy by 5%", 40, 7, 200, 18, 14, .75, .05));
				message += "\nWelcome to Level 25! You have earned the final special skill and weapon!";
			}
		}
		message += "\nYou are now level " + level + ".";
		message += "\nExp until next level up: " + (expLevelUp - exp) + "XP";
		message += "\n\nMax HP increased by " + (100*levelUps) + "!\nMax SP increased by " + (5*levelUps) + "!";
		
		return message;
	}
	
	/**
	 * Restores the special skills of the user.
	 * This is called when a savefile is imported, in the parameterized constructor.
	 *
	 * <hr>
	 * Date created: Apr 4, 2021
	 *
	 * <hr>
	 */
	private void restoreSpecialSkills() 
	{
		if(level >= 2) 
		{
			specialSkills.add("Sharpshooter");
		}
		if(level >= 10) 
		{
			specialSkills.add("Critical Shot");
		}
		if(level >= 20) 
		{
			specialSkills.add ("Weak Point");
		}
		if(level >= 25) 
		{
			specialSkills.add ("Unload");
		}
	}
	
	
	/**
	 * The toString method of the HeroAttributes class.     
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
		message += "\nHP:   ( " + HP + "HP / " + maxHP + "HP )";
		message += "\nSP:   ( " + SP + "SP / " + maxSP + "SP )";
		Weapon pw = equipment.getPrimaryWeapon ( );
		message += "\nPrimary Weapon:        " + pw.getWeaponName ( ) + " (" + pw.getTotalDamage ( ) + "DMG | " + pw.getClipSize ( ) + "Ammo | " + (int)(pw.getTotalAccuracy ( )*100) + "% Hit | " + pw.getRps ( ) + "RPS)";
		Weapon sw = equipment.getSecondaryWeapon ( );
		message += "\nSecondary Weapon:   " + sw.getWeaponName ( ) + " (" + sw.getTotalDamage ( ) + "DMG | " + sw.getClipSize ( ) + "Ammo | " + (int)(sw.getTotalAccuracy ( )*100) + "% Hit | " + sw.getRps ( ) + "RPS)";
		message += "\nLuck: " + equipment.getTotalLuck ( );
		message += "\nArmor: " + equipment.getTotalArmor ( ) + "AP";
		message += "\nCarry Weight: " + equipment.getWeight() + "lbs.";
		message += "\nLevel: " + level + "   |   Exp to Level Up: (" + exp + "/" + expLevelUp + ")";
		
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
	
	/**
	 * Return information about each of the hero's special skills.      
	 *
	 * <hr>
	 * Date created: Apr 2, 2021
	 *
	 * <hr>
	 * @return message
	 */
	public String getSpecialSkillDescription() 
	{
		String message = "Special Skills";
		message += "\n--------------------\n";
		
		for(String str: specialSkills) 
		{
			message += str;
			str = str.toUpperCase ( ).trim ( );
			if(str.equals ("SHARPSHOOTER")) 
			{
				message += ": Fire at the Enemy with 100% Accuracy. Ignores up to 30% of Enemy Armor.\n";
			}
			else if(str.equals ("CRITICAL SHOT")) 
			{
				message += ": Fire at the Enemy with 50% increased Luck.\n";
			}
			else if(str.equals ("WEAK POINT")) 
			{
				message += ": Fire at the Enemy with 30% increased Luck and Ignore up to 25% of the Enemy's Armor.\n";
			}
			else if(str.equals ("UNLOAD")) 
			{
				message += ": Fire all the ammo in the clip at the Enemy in 1 turn. Ignores Enemy Armor.\n";
			}
		}
		
		return message;
	}
	
	/**
	 * Return a description of what each attribute does.
	 *
	 * <hr>
	 * Date created: Apr 4, 2021
	 *
	 * <hr>
	 * @return
	 */
	public String getAttributeDescription() 
	{
		String message = "";
		
		message += "\nHP - Your Hero's Lifeforce. Don't let it reach 0.";
		message += "\nSP - Special Points used in Special Skills.";
		message += "\nDMG - The amount of damage the weapon deals";
		message += "\nSecondary Damage - The amount of damage your secondary weapon deals";
		message += "\nAmmo - The Clip Size of the weapon.";
		message += "\n% Hit - The accuracy of the weapon.";
		message += "\nRPS - Rounds per second. A measure of how many rounds are discharged in one attack.";
		message += "\nClip Size - The amount of rounds a weapon may hold.";
		message += "\nAccuracy - How often a round will damage an enemy.";

		message += "\n-----\n";
		message += "\nLuck - Chance of a round doing double damage to an enemy.";
		message += "\nArmor - Reduces damage from enemy weapons based on value.";
		message += "\nWeight - Reduces accuracy based on how heavy the equipment is. Every 5lbs reduces accuracy by 1%.";
		message += "\nLuck Shot - Doubles the damage of the lucky round. Chance increases with Luck.";
		
		return message;
	}
	
}
