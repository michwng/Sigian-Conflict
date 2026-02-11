/**
 * ---------------------------------------------------------------------------
 * File name: AllWeapons.java
 * Project name: Project 5
 * ---------------------------------------------------------------------------
 * Creator's name and email: Michael Ng, ngmw01@etsu.edu
 * Course:  CSCI 1260-940 (Intro Computer Sci II)
 * Creation Date: Mar 28, 2021
 * ---------------------------------------------------------------------------
 */

//This class and AllArmors was made early in anticipation of Project 5.

package weapon;
import java.util.ArrayList;

/**
 * The AllWeapons class stores a list of most weapons in the game.
 * This class is not used. It only acts as a reference.
 * A few enemies have custom weapons.
 *
 * <hr>
 * Date created: Mar 28, 2021
 * <hr>
 * @author Michael Ng
 */
public class AllWeapons extends Weapon
{
	//This arraylist will store all in-game weapons defined below in the constructor.
	private ArrayList<Weapon> weapons = new ArrayList<>();
	
	/**
	 * The Constructor for the AllWeapons class. Creates Weapon objects to be added to the weapons arraylist.
	 *
	 * <hr>
	 * Date created: Mar 28, 2021 
	 *
	 *
	 */
	public AllWeapons() 
	{
		//Note: All of these are made-up names. If they share a name with a real weapon, it is coincidental.
		//All In-Game Pisols. (Index 0 - 4)
		Weapon pistol = new Weapon("Amber M7", "A pistol manufactured by Amber Inc. It is highly accurate and easy to handle.", "This weapon has no mods.", 32, 4, 10, 0, 0, .95, 0);
		Weapon pistol2 = new Weapon("Carris M3", "A pistol manufactured by Carristellar. It has much power, but is heavy to wield.", "This weapon has no mods.", 20, 2, 40, 0, 3, .80, 0);
		Weapon pistol3 = new Weapon("Legion 5li", "A pistol manufactured by Legion. The lightest and most accurate among all pistols.", "This weapon has no mods.", 20, 4, 20, 0, 0, 1.0, 0);
		Weapon pistol4 = new Weapon("Cal M4B", "A supremely hefty pistol developed by Callus. The strongest but heaviest of all pistols.", "This weapon has no mods.", 18, 2, 80, 0, 5, .75, 0);
		Weapon pistol5 = new Weapon("Smoke A8", "A pistol manufactured by Smokestack. A strong pistol with a large clip size.", "This weapon has no mods.", 45, 5, 12, 0, 4, .85, 0);
		
		//Add the pistols above into the weapons ArrayList.
		weapons.add (pistol);
		weapons.add (pistol2);
		weapons.add (pistol3);
		weapons.add (pistol4);
		weapons.add (pistol5);
		
		//All In-Game Rifles. (Index 5 - 9)
		Weapon rifle = new Weapon("SBR 1100", "A standard bolt-action rifle issued to all soldiers. It is easy-to-wield and accurate.", "Iron Sights - Increase Accuracy by 5%.", 8, 1, 120, 0, 4, .90, .05);
		Weapon rifle2 = new Weapon("EBR 1100X", "A modified standard rifle that is now semi-automatic. It also has a larger clip size.", "8X Sights - Increase Accuracy by 20%.", 15, 2, 100, 0, 12, .78, .20);
		Weapon rifle3 = new Weapon("Amber Carbine", "A semi-automatic rifle manufactured by Amber Inc. It has very little recoil.", "Iron Sights - Increase Accuracy by 5%.", 12, 3, 100, 0, 10, .91, .05);
		Weapon rifle4 = new Weapon("Smoke BR715", "A  powerful bolt-action battle rifle manufactured by Smokestack. Has low clip size.", "Iron Sights - Increase Accuracy by 5%.", 6, 1, 700, 0, 9, .80, .05);
		Weapon rifle5 = new Weapon("Legion BRM280", "A fancy semi-automatic rifle issued to higher-ups. The most accurate of rifles.", "4x Scope - Increase Accuracy by 15%.", 20, 5, 130, 0, 7, .85, .15);
		
		//Add the rifles into the weapons ArrayList.
		weapons.add (rifle);
		weapons.add (rifle2);
		weapons.add (rifle3);
		weapons.add (rifle4);
		weapons.add (rifle5);
		
		//All In-Game Assault Rifles. (Index 10 - 12)
		Weapon ar = new Weapon("Amber AAR50", "One of the few Assault Rifles of this era. Powerful in the right hands.", "Iron Sights - Increase Accuracy by 5%", 36, 8, 100, 0, 14, .75, .05);
		Weapon ar2 = new Weapon("Cal ARX", "Cal's first model of their Assualt Rifle. Quite difficult to wield, but powerful.", "Iron Sights - Increase Accuracy by 5%", 20, 5, 220, 0, 20, .65, .05);
		Weapon ar3 = new Weapon("Carris AR90", "An assualt rifle developed by Carristellar. \nPower can be felt when firing this weapon.", "Iron Sights - Increase Accuracy by 5%", 40, 7, 200, 18, 14, .75, .05);
		
		//Add Assault Rifles into the weapons arraylist.
		weapons.add (ar);
		weapons.add (ar2);
		weapons.add (ar3);
		
	}	
}
