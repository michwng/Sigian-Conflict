/**
 * ---------------------------------------------------------------------------
 * File name: Enemy.java
 * Project name: Project 5
 * ---------------------------------------------------------------------------
 * Creator's name and email: Michael Ng, ngmw01@etsu.edu
 * Course:  CSCI 1260-940 (Intro Computer Sci II)
 * Creation Date: Apr 2, 2021
 * ---------------------------------------------------------------------------
 */

package game;
import armor.Armor;
import armor.BodyPart;
import weapon.Weapon;

/**
 * The enemy class creates a new enemy.
 * This extends off the abstract Participant class.
 *
 * <hr>
 * Date created: Apr 2, 2021
 * <hr>
 * @author Michael Ng
 */
public class Enemy extends Participant
{
	//String name and Equipment equipment extend from Participant.
	//Their getter and setter methods are included.
	private int HP, primaryATK, rps, clipSize, level, exp;
	
	/**
	 * The Default Constructor of the Enemy class.        
	 *
	 * <hr>
	 * Date created: Mar 31, 2021 
	 */
	public Enemy() 
	{
		HP = 100;
		level = 3;
		checkLevel();
	}
	
	/**
	 * A partially parameterized Constructor of the HeroAttributes class. 
	 * Accepts a String heroName and is used in a new game.
	 *
	 * <hr>
	 * Date created: Mar 31, 2021 
	 *
	 * <hr>
	 * @param heroName
	 */
	public Enemy(int level) 
	{
		HP = 100;
		this.level = level;
		checkLevel();
	}

	/**
	 * Balances enemy stats and equipment according to level. 
	 * Enemies are also balanced according to difficulty.
	 *
	 * <hr>
	 * Date created: Apr 2, 2021
	 *
	 * <hr>
	 */
	private void checkLevel() 
	{
		double difficulty = 1.0;
		if(Driver.getDifficulty ( ).equals ("Hard")) 
		{
			difficulty = 2;
		}
		else if(Driver.getDifficulty ( ).equals ("Easy")) 
		{
			difficulty = 0.5;
		}
		
		//creates the enemy based on the chapter.
		if(level == 0) 
		{
			name = "Private Rifleman";
			HP = (int)(100*difficulty);
			Weapon weapon = new Weapon("Fake Rifle", "A fake bolt-automatic rifle designed to inflict only pain.", "No Modifications", 12, 1, (int)(15*difficulty), 0, 0, .90, 0);
			Armor helmet = new Armor(BodyPart.Head, "Plastic Helmet", "A Bicycle Helmet. At least it's something.", "Childish - Not so serious.", 2, 0, 0, 1);
			Armor jacket = new Armor(BodyPart.Torso, "Simple Vest", "A basic vest with little protection.", "No Modification", 2, 0, 0, 1);
			Armor hands = new Armor(BodyPart.Hands, "Simple Gloves", "Basic gloves.", "No Modification", 0, 0, 0, 1);
			Armor legs = new Armor(BodyPart.Torso, "Jeans", "Stylish jeans. At least they have style.", "No Modification", 0, 0, 0, 1);
			Armor feet = new Armor(BodyPart.Torso, "Boots", "Boots that allow for easy traction and movement.", "No Modification", 2, 0, 0, 3);
			Armor[] armors = {helmet, jacket, hands, legs, feet};
			equipment = new Equipment(weapon, armors);
			exp = 40;
		}
		else if(level == 1) 
		{
			name = "Enemy Recruit";
			HP = (int)(200*difficulty);
			Weapon hsr = new Weapon("Helios Rifle", "A rifle manufactured by Helios Industries. Difficult to handle.", "No Modifications", 12, 3, (int)(15*difficulty), 0, 0, .60, 0);
			Armor helmet = new Armor(BodyPart.Head, "Helmet", "A standard issue army helmet.", "Security - Wearing this helmet makes one feel safe.", 5, 0, 0, 1);
			Armor jacket = new Armor(BodyPart.Torso, "Jacket", "Standard issue jacket.", "Pockets - Store a lot of things!", 10, 0, 1, 2);
			Armor hands = new Armor(BodyPart.Hands, "Tactical Gloves", "Standard gloves used to improve grip on a weapon.", "Grip - No butterfingers here.", 1, 0, 0, 3);
			Armor legs = new Armor(BodyPart.Legs, "Cargo Pants", "Standard pants that allow flexibility and protection for the wearer.", "Flexible - Technology is nice to have.", 3, 1, 1, 1);
			Armor feet = new Armor(BodyPart.Feet, "Boots", "Standard boots that were made to traverse any terrain. Comfortable.", "Comfortable - Ah... Is that Polyester?", 3, 1, 1, 2);
			Armor[] armors = {helmet, jacket, hands, legs, feet};
			equipment = new Equipment(hsr, armors);
			exp = 70;
		}
		else if(level == 2) 
		{
			name = "Enemy Engineer";
			HP = (int)(300*difficulty);
			Weapon weapon = new Weapon("Carris M3", "A pistol manufactured by Carristellar. It has much power, but is heavy to wield.", "This weapon has no mods.", 20, 2, (int)(40*difficulty), 0, 3, .80, 0);
			Armor helmet = new Armor(BodyPart.Head, "Helmet", "A standard issue army helmet.", "Security - Wearing this helmet makes one feel safe.", 5, 0, 0, 1);
			Armor jacket = new Armor(BodyPart.Torso, "Jacket", "Standard issue jacket.", "Pockets - Store a lot of things!", 10, 0, 1, 2);
			Armor hands = new Armor(BodyPart.Hands, "Tactical Gloves", "Standard gloves used to improve grip on a weapon.", "Grip - No butterfingers here.", 1, 0, 0, 3);
			Armor legs = new Armor(BodyPart.Legs, "Cargo Pants", "Standard pants that allow flexibility and protection for the wearer.", "Flexible - Technology is nice to have.", 3, 1, 1, 1);
			Armor feet = new Armor(BodyPart.Feet, "Boots", "Standard boots that were made to traverse any terrain. Comfortable.", "Comfortable - Ah... Is that Polyester?", 3, 1, 1, 2);
			Armor[] armors = {helmet, jacket, hands, legs, feet};
			equipment = new Equipment(weapon, armors);
			exp = 100;
		}
		else if(level == 3) 
		{
			name = "Enemy Rifleman";
			HP = (int)(400*difficulty);
			Weapon rifle = new Weapon("SBR 1100", "A standard bolt-action rifle issued to all soldiers. It is easy-to-wield and accurate.", "Iron Sights - Increase Accuracy by 5%.", 8, 1, (int)(120*difficulty), 0, 4, .90, .05);
			Armor helmet = new Armor(BodyPart.Head, "Helmet", "A standard issue army helmet.", "Security - Wearing this helmet makes one feel safe.", 5, 0, 0, 1);
			Armor jacket = new Armor(BodyPart.Torso, "Jacket", "Standard issue jacket.", "Pockets - Store a lot of things!", 10, 0, 1, 2);
			Armor hands = new Armor(BodyPart.Hands, "Tactical Gloves", "Standard gloves used to improve grip on a weapon.", "Grip - No butterfingers here.", 1, 0, 0, 3);
			Armor legs = new Armor(BodyPart.Legs, "Cargo Pants", "Standard pants that allow flexibility and protection for the wearer.", "Flexible - Technology is nice to have.", 3, 1, 1, 1);
			Armor feet = new Armor(BodyPart.Feet, "Boots", "Standard boots that were made to traverse any terrain. Comfortable.", "Comfortable - Ah... Is that Polyester?", 3, 1, 1, 2);
			Armor[] armors = {helmet, jacket, hands, legs, feet};
			equipment = new Equipment(rifle, armors);
			exp = 120;
		}
		else if(level == 4) 
		{
			name = "Enemy Medic";
			HP = (int)(500*difficulty);
			Weapon weapon = new Weapon("Cal M4B", "A supremely hefty pistol developed by Callus. The strongest but heaviest of all pistols.", "This weapon has no mods.", 18, 2, (int)(80*difficulty), 0, 5, .75, 0);
			Armor helmet = new Armor(BodyPart.Head, "Headset", "A standard issue army helmet with a modification.", "Headset - Talk and chat freely with nearby teammates.", 5, 0, 1, 5);
			Armor jacket = new Armor(BodyPart.Torso, "Vest", "Bullet-proof vest (mostly).", "Pockets - Store many things!", 20, 0, 1, 2);
			Armor hands = new Armor(BodyPart.Hands, "Tactical Gloves", "Standard gloves used to improve grip on a weapon.", "Grip - No butterfingers here.", 1, 0, 0, 3);
			Armor legs = new Armor(BodyPart.Legs, "Cargo Pants", "Standard pants that allow flexibility and protection for the wearer.", "Flexible - Technology is nice to have.", 3, 1, 1, 1);
			Armor feet = new Armor(BodyPart.Feet, "Boots", "Standard boots that were made to traverse any terrain. Comfortable.", "Comfortable - Ah... Is that Polyester?", 3, 1, 1, 2);
			Armor[] armors = {helmet, jacket, hands, legs, feet};
			equipment = new Equipment(weapon, armors);
			exp = 140;
		}
		else if(level == 5) 
		{
			name = "Elite Rifleman";
			HP = (int)(600*difficulty);
			Weapon weapon = new Weapon("EBR 1100X", "A modified standard rifle that is now semi-automatic. It also has a larger clip size.", "8X Sights - Increase Accuracy by 20%.", 15, 2, (int)(100*difficulty), 0, 12, .78, .20);
			Armor helmet = new Armor(BodyPart.Head, "Headset", "A standard issue army helmet with a modification.", "Headset - Talk and chat freely with nearby teammates.", 5, 0, 1, 5);
			Armor jacket = new Armor(BodyPart.Torso, "Kevlar Jacket", "A Kevlar Jacket. Man, this is heavy.", "Security - This is a lot of padding.", 25, 5, 14, 0);
			Armor hands = new Armor(BodyPart.Hands, "Tactical Gloves", "Standard gloves used to improve grip on a weapon.", "Grip - No butterfingers here.", 1, 0, 0, 3);
			Armor legs = new Armor(BodyPart.Legs, "Cargo Pants", "Standard pants that allow flexibility and protection for the wearer.", "Flexible - Technology is nice to have.", 3, 1, 1, 1);
			Armor feet = new Armor(BodyPart.Feet, "Boots", "Standard boots that were made to traverse any terrain. Comfortable.", "Comfortable - Ah... Is that Polyester?", 3, 1, 1, 2);
			Armor[] armors = {helmet, jacket, hands, legs, feet};
			equipment = new Equipment(weapon, armors);
			exp = 160;
		}
		else if(level == 6) 
		{
			name = "Enemy Sharpshooter";
			HP = (int)(700*difficulty);
			Weapon weapon = new Weapon("Amber Carbine", "A semi-automatic rifle manufactured by Amber Inc. It has very little recoil.", "Iron Sights - Increase Accuracy by 5%.", 12, 3, (int)(100*difficulty), 0, 10, .91, .05);
			Armor helmet = new Armor(BodyPart.Head, "Headset", "A standard issue army helmet with a modification.", "Headset - Talk and chat freely with nearby teammates.", 5, 0, 1, 5);
			Armor jacket = new Armor(BodyPart.Torso, "Kevlar Jacket", "A Kevlar Jacket. Man, this is heavy.", "Security - This is a lot of padding.", 25, 5, 14, 0);
			Armor hands = new Armor(BodyPart.Hands, "Tactical Gloves", "Standard gloves used to improve grip on a weapon.", "Grip - No butterfingers here.", 1, 0, 0, 3);
			Armor legs = new Armor(BodyPart.Legs, "Cargo Pants", "Standard pants that allow flexibility and protection for the wearer.", "Flexible - Technology is nice to have.", 3, 1, 1, 1);
			Armor feet = new Armor(BodyPart.Feet, "Boots", "Standard boots that were made to traverse any terrain. Comfortable.", "Comfortable - Ah... Is that Polyester?", 3, 1, 1, 2);
			Armor[] armors = {helmet, jacket, hands, legs, feet};
			equipment = new Equipment(weapon, armors);
			exp = 220;
		}
		else if(level == 7) 
		{
			name = "Enemy Officer";
			HP = (int)(800*difficulty);
			Weapon weapon = new Weapon("Legion BRM280", "A fancy semi-automatic rifle issued to higher-ups. The most accurate of rifles.", "4x Scope - Increase Accuracy by 15%.", 20, 5, (int)(100*difficulty), 0, 7, .85, .15);
			Armor helmet = new Armor(BodyPart.Head, "Officer Headset", "An officer cap with a modification.", "Headset - Talk and chat freely with nearby teammates.", 5, 0, 1, 5);
			Armor jacket = new Armor(BodyPart.Torso, "Officer's Jacket", "A formal Jacket with Kevlar inside.", "Security - This is a lot of padding.", 25, 5, 14, 0);
			Armor hands = new Armor(BodyPart.Hands, "Offier's Gloves", "Formal gloves. Not designed for holding a gun.", "No Grip - Oops. Butterfingers.", 1, 0, 0, -3);
			Armor legs = new Armor(BodyPart.Legs, "Officer's Pants", "Pants that allow flexibility, protection, and formality for the wearer.", "Flexible - Technology is nice to have.", 3, 1, 1, 1);
			Armor feet = new Armor(BodyPart.Feet, "officer's Boots", "Formal Boots that were made to traverse any terrain. Comfortable.", "Comfortable - Ah... Is that Polyester?", 3, 1, 1, 2);
			Armor[] armors = {helmet, jacket, hands, legs, feet};
			equipment = new Equipment(weapon, armors);
			exp = 260;
		}
		else if(level == 8) 
		{
			name = "Enemy Bodyguard";
			HP = (int)(900*difficulty);
			Weapon weapon = new Weapon("Smoke BR715", "A  powerful bolt-action battle rifle manufactured by Smokestack. Has low clip size.", "Iron Sights - Increase Accuracy by 5%.", 6, 1, (int)(700*difficulty), 0, 9, .80, .05);
			Armor helmet = new Armor(BodyPart.Head, "Headset", "A standard issue army helmet with a modification.", "Headset - Talk and chat freely with nearby teammates.", 5, 0, 1, 5);
			Armor jacket = new Armor(BodyPart.Torso, "Kevlar Jacket", "A Kevlar Jacket. Man, this is heavy.", "Security - This is a lot of padding.", 25, 5, 14, 0);
			Armor hands = new Armor(BodyPart.Hands, "Tactical Gloves", "Standard gloves used to improve grip on a weapon.", "Grip - No butterfingers here.", 1, 0, 0, 3);
			Armor legs = new Armor(BodyPart.Legs, "Cargo Pants", "Standard pants that allow flexibility and protection for the wearer.", "Flexible - Technology is nice to have.", 3, 1, 1, 1);
			Armor feet = new Armor(BodyPart.Feet, "Boots", "Standard boots that were made to traverse any terrain. Comfortable.", "Comfortable - Ah... Is that Polyester?", 3, 1, 1, 2);
			Armor[] armors = {helmet, jacket, hands, legs, feet};
			equipment = new Equipment(weapon, armors);
			exp = 320;
		}
		else if(level == 9) 
		{
			name = "Elite Soldier";
			HP = (int)(1000*difficulty);
			Weapon weapon = new Weapon("Amber AAR50", "One of the few Assault Rifles of this era. Powerful in the right hands.", "Iron Sights - Increase Accuracy by 5%", 36, 8, (int)(100*difficulty), 0, 14, .75, .05);
			Armor helmet = new Armor(BodyPart.Head, "Headset", "A standard issue army helmet with a modification.", "Headset - Talk and chat freely with nearby teammates.", 5, 0, 1, 5);
			Armor jacket = new Armor(BodyPart.Torso, "Kevlar Jacket", "A Kevlar Jacket. Man, this is heavy.", "Security - This is a lot of padding.", 25, 5, 14, 0);
			Armor hands = new Armor(BodyPart.Hands, "Tactical Gloves", "Standard gloves used to improve grip on a weapon.", "Grip - No butterfingers here.", 1, 0, 0, 3);
			Armor legs = new Armor(BodyPart.Legs, "Cargo Pants", "Standard pants that allow flexibility and protection for the wearer.", "Flexible - Technology is nice to have.", 3, 1, 1, 1);
			Armor feet = new Armor(BodyPart.Feet, "Boots", "Standard boots that were made to traverse any terrain. Comfortable.", "Comfortable - Ah... Is that Polyester?", 3, 1, 1, 2);
			Armor[] armors = {helmet, jacket, hands, legs, feet};
			equipment = new Equipment(weapon, armors);
			exp = 420;
		}
		else if(level == 10) 
		{
			name = "Elite Juggernaut";
			HP = (int)(1500*difficulty);
			Weapon weapon = new Weapon("LMG 100", "What is that?! A multi-barreled gun?!", "Intimidation - Cower before the LMG.", 800, 100, (int)(10*difficulty), 0, 12, .78, .20);
			Armor helmet = new Armor(BodyPart.Head, "Kevlar Helmet", "A handy kevlar helmet. The padding is nice.", "Padding - One can get comfortable here.", 10, 2, 2, 7);
			Armor jacket = new Armor(BodyPart.Torso, "Kevlar Jacket", "A Kevlar Jacket. Man, this is heavy.", "Security - This is a lot of padding.", 25, 5, 14, 0);
			Armor hands = new Armor(BodyPart.Hands, "Kevlar Gloves", "Kevlar gloves used to improve grip on a weapon and keep your hands safe.", "Safe - these hands are safe.", 5, 2, 3, 2);
			Armor legs = new Armor(BodyPart.Legs, "Kevlar Pants", "Kevlar pants that provide protection for the wearer.", "Safety - These pants are safe to wear.", 10, 5, 7, 1);
			Armor feet = new Armor(BodyPart.Feet, "Kevlar Boots", "Protect those feet with Kevlar Boots!", "Hot - That's too much padding on boots.", 7, 0, 3, 0);
			Armor[] armors = {helmet, jacket, hands, legs, feet};
			equipment = new Equipment(weapon, armors);
			exp = 500;
		}
		else if(level == 11) //This enemy appears as the last enemy of Stage 5.
		{
			name = "Rebellion Leader";
			HP = (int)(7000*difficulty);
			Weapon weapon = new Weapon("RPG Model E", "This gun shoots explosives?!", "Intimidation - Cower before the RPG.", 1, 1, (int)(5000*difficulty), 0, 12, .60, .20);
			Armor helmet = new Armor(BodyPart.Head, "Kevlar Helmet", "A handy kevlar helmet. The padding is nice.", "Padding - One can get comfortable here.", 10, 2, 2, 7);
			Armor jacket = new Armor(BodyPart.Torso, "Chestplate", "A medieval knight's chestplate. Super heavy.", "Ricochet - Those bullets will bounce right off!", 35, 5, 20, 1);
			Armor hands = new Armor(BodyPart.Hands, "Kevlar Gloves", "Kevlar gloves used to improve grip on a weapon and keep your hands safe.", "Safe - these hands are safe.", 5, 2, 3, 2);
			Armor legs = new Armor(BodyPart.Legs, "Kevlar Pants", "Kevlar pants that provide protection for the wearer.", "Safety - These pants are safe to wear.", 10, 5, 7, 1);
			Armor feet = new Armor(BodyPart.Feet, "Kevlar Boots", "Protect those feet with Kevlar Boots!", "Hot - That's too much padding on boots.", 7, 0, 3, 0);
			Armor[] armors = {helmet, jacket, hands, legs, feet};
			equipment = new Equipment(weapon, armors);
			exp = 1000;
		}
		else if(level == 12) //This enemy appears as the last enemy of a Skirmish.
		{
			name = "Rebellious Warlord";
			HP = (int)(10000*difficulty);
			Weapon weapon = new Weapon("RPG Model B", "This gun shoots many explosives?!", "Future Sight - Lock-On reduces the unwieldiness of this RPG.", 6, 3, (int)(5000*difficulty), 0, 15, .20, .40);
			Armor helmet = new Armor(BodyPart.Head, "Kevlar Helmet", "A handy kevlar helmet. The padding is nice.", "Padding - One can get comfortable here.", 10, 2, 2, 7);
			Armor jacket = new Armor(BodyPart.Torso, "Kevlar Chestplate", "Super heavy chestplate designed to withstand any impact.", "Padding - It's real hot in here.", 35, 10, 20, 1);
			Armor hands = new Armor(BodyPart.Hands, "Kevlar Gloves", "Kevlar gloves used to improve grip on a weapon and keep your hands safe.", "Safe - these hands are safe.", 5, 2, 3, 2);
			Armor legs = new Armor(BodyPart.Legs, "Kevlar Pants", "Kevlar pants that provide protection for the wearer.", "Safety - These pants are safe to wear.", 10, 5, 7, 1);
			Armor feet = new Armor(BodyPart.Feet, "Kevlar Boots", "Protect those feet with Kevlar Boots!", "Hot - That's too much padding on boots.", 7, 0, 3, 0);
			Armor[] armors = {helmet, jacket, hands, legs, feet};
			equipment = new Equipment(weapon, armors);
			exp = 1500;
		}
		
		primaryATK = equipment.getPrimaryWeapon().getTotalDamage ( );
		rps = equipment.getPrimaryWeapon ( ).getRps();
		clipSize = equipment.getPrimaryWeapon ( ).getClipSize ( );
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
	 * @return exp
	 */
	public int getExp ( )
	{
		return exp;
	}
	
	
	/**
	 * Return a description of the enemy.    
	 *
	 * <hr>
	 * Date created: Apr 2, 2021 
	 *
	 * <hr>
	 * @return message
	 * @see game.Participant#toString()
	 */
	public String toString() 
	{
		String message = name;
		
		message += "\n---------------";
		message += "\nHP:   " + HP + "HP";
		message += "\nPrimary Damage: " + primaryATK + "HP Damage";
		message += "\nArmor: " + equipment.getTotalArmor ( ) + "AP";
		message += "\nExperience Reward: " + exp + "XP";
		
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
			message += equipment.toString ("WEAPON");
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