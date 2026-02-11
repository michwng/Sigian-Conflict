/**
 * ---------------------------------------------------------------------------
 * File name: AllArmors.java
 * Project name: Project 5
 * ---------------------------------------------------------------------------
 * Creator's name and email: Michael Ng, ngmw01@etsu.edu
 * Course:  CSCI 1260-940 (Intro Computer Sci II)
 * Creation Date: Mar 28, 2021
 * ---------------------------------------------------------------------------
 */

//This class and AllWeapons was made early in anticipation of Project 5.

package armor;
import java.util.ArrayList;

/**
 * The AllArmors class stores a list of all in-game armor.
 * This class is not used. It only acts as a reference.
 *
 * <hr>
 * Date created: Mar 28, 2021
 * <hr>
 * @author Michael Ng
 */
public class AllArmors extends Armor
{
	private ArrayList<Armor> armors = new ArrayList<>();
	
	/**
	 * 
	 * The Constructor for the AllArmors class. Creates Armor objects to be added to the armors ArrayList.   
	 *
	 * <hr>
	 * Date created: Mar 28, 2021 
	 *
	 *
	 */
	public AllArmors() 
	{
		//All Head Armors.
		Armor cap = new Armor(BodyPart.Head, "Cap", "A lucky hat from home that has much sentimental value.", "Lucky - Luck is increased.", 0, 0, 0, 10);
		Armor helmet = new Armor(BodyPart.Head, "Helmet", "A standard issue army helmet.", "Security - Wearing this helmet makes one feel safe.", 5, 0, 0, 1);
		Armor headset = new Armor(BodyPart.Head, "Headset", "A standard issue army helmet with a modification.", "Headset - Talk and chat freely with nearby teammates.", 5, 0, 1, 5);
		Armor kevlarHelmet = new Armor(BodyPart.Head, "Kevlar Helmet", "A handy kevlar helmet. The padding is nice.", "Padding - One can get comfortable here.", 10, 2, 2, 7);
		Armor headplate = new Armor(BodyPart.Head, "Head Plate", "A metal helmet that provides supreme protection.", "Security 2 - Wearing this makes one feel *very* safe.", 10, 5, 3, 0);
		
		//Add all Head armors to the armors Arraylist.
		armors.add (cap);
		armors.add (helmet);
		armors.add (headset);
		armors.add (kevlarHelmet);
		armors.add (headplate);
		
		//All Torso Armors.
		Armor jacket = new Armor(BodyPart.Torso, "Jacket", "Standard issue jacket.", "Pockets - Store a lot of things!", 10, 0, 1, 2);
		Armor vest = new Armor(BodyPart.Torso, "Vest", "Bullet-proof vest (mostly).", "Pockets - Store many things!", 20, 0, 1, 2);
		Armor suit = new Armor(BodyPart.Torso, "Suit", "A business suit. Give those enemies the business.", "Business - Give em' the Business.", 3, 2, 0, 10);
		Armor kevlar = new Armor(BodyPart.Torso, "Kevlar Jacket", "A Kevlar Jacket. Man, this is heavy.", "Security - This is a lot of padding.", 25, 5, 14, 0);
		Armor chestplate = new Armor(BodyPart.Torso, "Chestplate", "A medieval knight's chestplate. Super heavy.", "Ricochet - Those bullets will bounce right off!", 35, 5, 20, 1);
		
		//Add all Torso Armors to the armors ArrayList.
		armors.add(jacket);
		armors.add(vest);
		armors.add(suit);
		armors.add(kevlar);
		armors.add(chestplate);
		
		//All Hand Armors.
		Armor tacGloves = new Armor(BodyPart.Hands, "Tactical Gloves", "Standard gloves used to improve grip on a weapon.", "Grip - No butterfingers here.", 1, 0, 0, 3);
		Armor halfGloves = new Armor(BodyPart.Hands, "Half Gloves", "Gloves that improve grip on a weapon without covering the entire hand.", "Grip - No butterfingers here.", 0, 0, 0, 5);
		Armor latexGloves = new Armor(BodyPart.Hands, "Latex Gloves", "Gloves that keep your hands safe and clean.", "Sanitary - No germs here.", 0, 1, 0, 5);
		Armor kevlarGloves = new Armor(BodyPart.Hands, "Kevlar Gloves", "Kevlar gloves used to improve grip on a weapon and keep your hands safe.", "Safe - these hands are safe.", 5, 2, 3, 2);
		Armor metalGloves = new Armor(BodyPart.Hands, "Metal Gloves", "Metal gloves used to keep one's hands safe. Not so great for weapon handling.", "No Grip - Oops. Butterfingers.", 10, 0, 5, -2);

		//Add all Hand Armors to the armors ArrayList.
		armors.add (tacGloves);
		armors.add (halfGloves);
		armors.add (latexGloves);
		armors.add (kevlarGloves);
		armors.add (metalGloves);
		
		//All Leg Armors.
		Armor cargo = new Armor(BodyPart.Legs, "Cargo Pants", "Standard pants that allow flexibility and protection for the wearer.", "Flexible - Technology is nice to have.", 3, 1, 1, 1);
		Armor shorts = new Armor(BodyPart.Legs, "Shorts", "Even more flexible than Cargo Pants!", "Flexible 2 - Leg day is a great day.", 0, 0, 0, 10);
		Armor suitPants = new Armor(BodyPart.Legs, "Suit Pants", "Black pants that allow for a formal look.", "Lucky - Those Allies won't mind.", 0, 1, 0, 15);
		Armor kevlarPants = new Armor(BodyPart.Legs, "Kevlar Pants", "Kevlar pants that provide protection for the wearer.", "Safety - These pants are safe to wear.", 10, 5, 7, 1);
		Armor metalLeggings = new Armor(BodyPart.Legs, "Metal Pants", "Metal pants that provide immense protection for the wearer.", "Ricochet - Those bullets will bounce right off.", 15, 5, 10, 1);

		//Add all Leg Armors to the armors ArrayList.
		armors.add (cargo);
		armors.add (shorts);
		armors.add (suitPants);
		armors.add (kevlarPants);
		armors.add (metalLeggings);
		
		//All Feet Armors.
		Armor boots = new Armor(BodyPart.Feet, "Boots", "Standard boots that were made to traverse any terrain. Comfortable.", "Comfortable - Ah... Is that Polyester?", 3, 1, 1, 2);
		Armor slippers = new Armor(BodyPart.Feet, "Slippers", "Standard boots may get too hot. Wear these cool sandals instead!", "Cool - That foot will catch that nice breeze.", 0, 0, 0, 10);
		Armor dressShoes = new Armor(BodyPart.Feet, "Dress Shoes", "Look formal by walking in black dress shoes.", "Formal - It's easy to set yourself apart from the others.", 0, 2, 1, 12);
		Armor kevlarBoots = new Armor(BodyPart.Feet, "Kevlar Boots", "Protect those feet with Kevlar Boots!", "Hot - That's too much padding on boots.", 7, 0, 3, 0);
		Armor metalBoots = new Armor(BodyPart.Feet, "Metal Boots", "Metal boots made to protect a knight's feet. Heavy.", "Metal Feet - Lifting a foot is harder than it looks.", 5, 5, 5, 0);

		//Add all Feet Armors to the armors ArrayList.
		armors.add (boots);
		armors.add (slippers);
		armors.add (dressShoes);
		armors.add (kevlarBoots);
		armors.add (metalBoots);
		
		//All Accessories.
		Armor necklace = new Armor(BodyPart.Accessory, "Necklace", "An accessory that reminds you of your old self.", "Reminiscent - Old memories bring hope.", 0, 0, 0, 5);
		Armor waterCanteen = new Armor(BodyPart.Accessory, "Water Canteen", "Quick access to hydration.", "Tactical - This could be used anywhere.", 0, 1, 0, 5);
		Armor spyglass = new Armor(BodyPart.Accessory, "Spyglass", "Become a pirate and gaze into the horizon.", "Far Sight - This must be what an Eagle sees.", 0, 0, 0, 9);
		Armor mask = new Armor(BodyPart.Accessory, "Mask", "Change your own persona! Actually, this is a surgical mask...", "Sanitary - No germs are getting through that mask.", 0, 1, 0, 8);
		Armor necktie = new Armor(BodyPart.Accessory, "Necktie", "Formality at its finest. Tying a tie is harder than it sounds.", "Tied Up - Up, Down, Across... What?", 0, 0, 0, 12);

		//Add all Accessory Armors to the armors ArrayList.
		armors.add (necklace);
		armors.add (waterCanteen);
		armors.add (spyglass);
		armors.add (mask);
		armors.add (necktie);
		
		//All Secondary Accessories.
		Armor ring = new Armor(BodyPart.Accessory2, "Ring", "A cheap plastic ring from vending machines.", "Revisiting - I remember when I first got that ring.", 0, 0, 0, 3);
		Armor chain = new Armor(BodyPart.Accessory2, "Chain", "A chain that connects to a small cap with a picture of a loved one in it.", "Protection - Fighting for loved ones.", 0, 0, 0, 5);
		Armor wristwatch = new Armor(BodyPart.Accessory2, "Wristwatch", "Easily keep track of the time by looking at your wrist.", "Track Time - It's no place to be late.", 0, 1, 0, 5);
		Armor rose = new Armor(BodyPart.Accessory2, "Rose", "Nice to have handy in unexpected situations.", "Rose - A healthy red keeps your hopes high.", 0, 0, 0, 6);
		Armor deodorant = new Armor(BodyPart.Accessory2, "Deodorant", "In case one doesn't have time to shower.", "Deodorant - The scent of greatness.", 0, 0, 0, 7);

		//Add all Secondary Accessory Armors to the armors ArrayList.
		armors.add (ring);
		armors.add (chain);
		armors.add (wristwatch);
		armors.add (rose);
		armors.add (deodorant);
	}
}
