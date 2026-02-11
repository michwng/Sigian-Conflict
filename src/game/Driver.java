
/**
 * ---------------------------------------------------------------------------
 * File name: Driver.java
 * Project name: Project 5
 * ---------------------------------------------------------------------------
 * Creator's name and email: Michael Ng, ngmw01@etsu.edu
 * Course:  CSCI 1260-940 (Intro Computer Sci II)
 * Creation Date: Mar 28, 2021
 * Last Updated: May 29, 2022
 * ---------------------------------------------------------------------------
 */

package game;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import armor.Armor;
import weapon.Weapon;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


/**
 * The Driver class runs the Zork-like game.
 *
 * <hr>
 * Date created: Mar 28, 2021
 * <hr>
 * @author Michael Ng
 */
public class Driver
{
	//The main attributes and equipment of the hero.
	private static HeroAttributes heroAttributes;
	//Determines the location of the player on the battlefield and game.
	private static Map map = new Map();
	//The main background music player.
	private static WavePlayer wavePlayer = null;
	//The secondary music player. Plays level up and get item.
	private static WavePlayer swavePlayer = null;
	//WavePlayer that makes click sounds.
	private static WavePlayer clickSound;
	//If the Click.wav or Click2.wav file exists in the music folder.
	private static boolean clickExists = true;
	//Determines if a battle in the battle method is ongoing.
	private static boolean inBattle = false;
	//DIfficulty of the game.
	private static String difficulty = "Medium";
	//Increases the amount of XP gained after each battle. Depends on how high the value.
	private static int xpMultiplier = 1;
	//This array will be used to store medkits and sodas between mapNavigation and Battle methods.
	private static int[] items = new int[2];
	//Determines if the user has fought a skirmish one or more times.
	//This variable is relative and will not be saved to a save file.
	private static boolean foughtSkirmishOnce = false;

	protected static String FileRoot = System.getProperty("user.dir") + File.separator;
	
	/**
	 * The Main Method runs and coordinates the game's main menu.
	 * Note: You may experience some "lag" and the JOptionPane being an empty box.
	 * Lag is believed to originate from WavePlayer, and might be causing the JOptionPane glitch.
	 * When JOptionPane is an empty box, just press your Enter key. It will move to the next prompt.
	 *
	 * <hr>
	 * Date created: Mar 31, 2021
	 *
	 * <hr>
	 * @param args
	 */
	public static void main(String[] args) 
	{	
		int choice = 0;
		String[] options = {"New Game", "Continue", "Options", "End Game"};
		//The Main Theme of the game. Credit to Fate Grand Order.
		playMusic("MainMenu", true);
		
		//While the user has not chosen "End Game".
		while(choice != 3) 
		{
			//It's hard to center the title!
			choice = JOptionPane.showOptionDialog (null, "   * * * * * * * * * * * * The Sigian Conflict * * * * * * * * * * * *", "Welcome", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options, options[0]);
			if(choice == 0) //New Game
			{
				beginNewGame();
			}
			else if(choice == 1) //Continue
			{
				//loadGame returns a boolean based on if the game loaded properly.
				boolean gameLoaded = loadGame();
				//It returns true when loaded successfully, otherwise, returns false.
				if(gameLoaded) 
				{
					//stageSelect allows the player to choose which chapter they want to play.
					stageSelect();
				}
			}
			else if(choice == 2) //Options
			{
				click();
				String[] options2 = {"Difficulty", "EXP Multiplier", "Exit Settings"};
				
				while(choice != 3) 
				{
					choice = JOptionPane.showOptionDialog (null, "Welcome to the Settings Menu!\nDifficulty: " + difficulty + 
						"\nEXP Multiplier: " + xpMultiplier + "\nWhat would you like to change?", 
						"Settings", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options2, options2[0]);
					click();
					if(choice == 0) //difficulty
					{
						//If you wanted the Easy difficulty, you'll have to press the Difficulty button twice.
						if(difficulty.equals ("Medium")) 
						{
							difficulty = "Hard";
							JOptionPane.showMessageDialog (null, "The difficulty has been set to: " + difficulty + ". \nEnemies have x2 HP and ATK.", "Hard Difficulty", JOptionPane.INFORMATION_MESSAGE);
						}
						else if(difficulty.equals ("Hard")) 
						{
							difficulty = "Easy";
							JOptionPane.showMessageDialog (null, "The difficulty has been set to: " + difficulty + ". \nEnemies have halfed HP and ATK.", "Easy Difficulty", JOptionPane.INFORMATION_MESSAGE);
						}
						else if(difficulty.equals ("Easy")) 
						{
							difficulty = "Medium";
							JOptionPane.showMessageDialog (null, "The difficulty has been set to: " + difficulty + ". \nEnemies have normal HP and ATK.", "Medium Difficulty", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else if(choice == 1) //XP Multiplier
					{
						//If you wanted the 3X XP multiplier, you'll have to press the XP Multiplier button twice.
						if(xpMultiplier == 1) 
						{
							xpMultiplier = 2;
							JOptionPane.showMessageDialog (null, "The Experience Multiplier has been set to: " + xpMultiplier
								+ ".\nYou will recieve double the experience after battles.", "Double Experience", JOptionPane.INFORMATION_MESSAGE);
						}
						else if(xpMultiplier == 2) 
						{
							xpMultiplier = 3;
							JOptionPane.showMessageDialog (null, "The Experience Multiplier has been set to: " + xpMultiplier
								+ ".\nYou will recieve triple the experience after battles.", "Triple Experience", JOptionPane.INFORMATION_MESSAGE);
						}
						else if(xpMultiplier == 3) 
						{
							xpMultiplier = 1;
							JOptionPane.showMessageDialog (null, "The Experience Multiplier has been set to: " + xpMultiplier
								+ ".\nYou will recieve the normal experience after battles.", "Normal Experience", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else //Exit settings or X.
					{
						choice = 2;
						click();
						break;
					}
					click();
				}
			}
		}
		endGame();
	}
	
	
	/**
	 * @return difficulty
	 */
	public static String getDifficulty ( )
	{
		return difficulty;
	}


	/**
	 * The beginNewGame method begins a new game.
	 *
	 * <hr>
	 * Date created: Mar 31, 2021
	 *
	 * <hr>
	 */
	private static void beginNewGame() 
	{			
		boolean validName = false;
		
		while(!validName) 
		{
			click();
			//Ask the user for the name of the hero. Default value is James E. Clipse (Get it?).
			String name = (String)(JOptionPane.showInputDialog (null, "What is the name of your soldier?", "Soldier Name", JOptionPane.QUESTION_MESSAGE, null, null, "James E. Clipse"));
			if(name == null)  //Cancel option
			{
				return;
				//Return to the main menu - skip the if-statement several lines down.
			}
			else if(name.isBlank ( )) //if name has no input.
			{
				//Default constructor.
				heroAttributes = new HeroAttributes();
				validName = true;
			}
			else if(name.length ( ) > 20) //name has input, but the name is too long.
			{
				JOptionPane.showMessageDialog (null, "Sorry, this name is too long. \nPlease input a shorter name.", "Name is too long", JOptionPane.ERROR_MESSAGE);
			}
			else //name is valid.
			{
				heroAttributes = new HeroAttributes(name);
				validName = true;
			}
			click();
		}
		
		//This returns the attributes of the hero and their equipped weapons.
		JOptionPane.showMessageDialog (null, heroAttributes.toString ( ) + "\n--------------------\n" + heroAttributes.getEquipmentInfo("Weapon"), "Beginning Attributes", JOptionPane.INFORMATION_MESSAGE);
		click();
		
		//This returns the attributes of the hero's equipped armor.
		JOptionPane.showMessageDialog (null, heroAttributes.getEquipmentInfo("Armor"), "Beginning Attributes", JOptionPane.INFORMATION_MESSAGE);
		click();
		
		//This returns the attributes of the hero's accessories.
		JOptionPane.showMessageDialog (null, heroAttributes.getEquipmentInfo("Accessory"), "Beginning Attributes", JOptionPane.INFORMATION_MESSAGE);
		click();
		
		//Makes sure the user is ready to begin a new game. 
		int pick = JOptionPane.showConfirmDialog (null, "Are you ready to begin?", "Ready", JOptionPane.YES_NO_OPTION);
		if(pick == 0) //Yes 
		{
			chapterBegin(0);
		}
		else 
		{
			//Do nothing. Return to the main menu.
		}
	}
	
	/**
	 * The loadGame method retrieves game information from an old save file.
	 * Returns true if a file was loaded successfully.
	 * Returns false otherwise.
	 *
	 * <hr>
	 * Date created: Mar 31, 2021
	 *
	 * <hr>
	 * @return boolean
	 */
	private static boolean loadGame() 
	{
		//The 2 filepaths to the save files.
		File path = new File(FileRoot + "src\\SaveFiles\\File1");
		File path2 = new File(FileRoot + "src\\SaveFiles\\File2");
		Scanner inputFile = null;
		Scanner inputFile2 = null;

		String filesInformation = "";
		//Filenum is used in the case that a save file is not used. 
		//Otherwise, there is no need for fileNum.
		int fileNum = 1;
		try //Validate the file paths. Note how cleverly fileNum is used.
		{
			inputFile = new Scanner(path);
			fileNum++;
			inputFile2 = new Scanner(path2);
		}
		catch (FileNotFoundException e1) 
		{
			JOptionPane.showMessageDialog (null, "Save file " + fileNum + " was not found.\nPlease select a text file in the following menu.", "Save File not Found.", JOptionPane.ERROR_MESSAGE);
			click();
			JFileChooser fileChooser = new JFileChooser("src\\SaveFiles");
			int pick = fileChooser.showOpenDialog (null);
			if(pick == JFileChooser.APPROVE_OPTION) 
			{
				File file = fileChooser.getSelectedFile ( );
				try
				{
					Scanner validate = new Scanner(file);
					validate.close();
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		//Gets the summary of the hero in File 1.
		if(inputFile.hasNext()) 
		{
			String str = inputFile.nextLine();
			String[] values = str.split ("\\|");
			
			//Name
			filesInformation += "(File 1) " + values[0];
			filesInformation += "\n--------------------\n";
			//Stages Completed
			filesInformation += "Stages Completed: " + values[1] + "\n";
			//Level
			filesInformation += "Level: " + values[2] + "\n";
			//Experience
			filesInformation += "Experience: " + values[3] + "XP / " + values[4] + "XP\n\n";
		}
		else //If there is nothing in File 1.
		{
			filesInformation += "File 1 is Empty.\n\n";
		}
		
		//Gets the summary of the hero in File 2.
		if(inputFile2.hasNext()) 
		{				
			String str = inputFile2.nextLine();
			String[] values = str.split ("\\|");
			
			//Name
			filesInformation +=  "(File 2) " + values[0];
			filesInformation += "\n--------------------\n";
			//Stages Completed
			filesInformation += "Stages Completed: " + values[1] + "\n";
			//Level
			filesInformation += "Level: " + values[2] + "\n";
			//Experience
			filesInformation += "Experience: " + values[3] + "XP / " + values[4] + "XP\n\n";
		}
		else //If there is nothing in File 2.
		{
			filesInformation += "File 2 is Empty.\n\n";
		}
		
		
		//Reset the scanners back to line 1 of the savefile before loading.
		try
		{
			inputFile = new Scanner(path);
			inputFile2 = new Scanner(path2);
		}
		catch (FileNotFoundException e1) //Unlikely to happen because it has already been validated. Still, it's put here just in case.
		{
			JOptionPane.showMessageDialog (null, "A save file was not found.\nPlease select a text file in the following menu.", "Save File not Found.", JOptionPane.ERROR_MESSAGE);
			JFileChooser fileChooser = new JFileChooser("src\\SaveFiles");
			int pick = fileChooser.showOpenDialog (null);
			if(pick == JFileChooser.APPROVE_OPTION) 
			{
				File file = fileChooser.getSelectedFile ( );
				try
				{
					Scanner validate = new Scanner(file);
					validate.close();
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				
			}
			
		}
		//The user chooses which file to load from.
		String[] options = {"File 1", "File 2"};
		
		//Asks the user which file they would like to load from. Displays the hero summaries of both files. 
		int pick = JOptionPane.showOptionDialog (null, "Which file would you like to load from?\n\n" + filesInformation, "Save Game", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		if(pick == 0) //File 1
		{	
			//Loads data from File 1.
			if(inputFile.hasNext ( )) 
			{
				String str = inputFile.nextLine();
				//Say for example line 1 had:
				//Pvt. James E. Clipse|1|3|90|121|
				//Name|StagesCompleted|Level|Exp|Exp to Level Up|
				String[] values = str.split ("\\|");
				
				//Name: values[0];
				String name = values[0];
				
				//maxHP equals level*100.
				int maxHP = (100*Integer.parseInt (values[2]));
				//maxSP equals (45 + (5*level)).
				int maxSP = (45+(5*Integer.parseInt (values[2])));
				//Level: values[2];
				int level = Integer.parseInt (values[2]);
				//Experience: values[3];
				int exp = Integer.parseInt (values[3]);
				//Experience: to Level Up values[4];
				int expLevelUp = Integer.parseInt (values[4]);
				
				heroAttributes = new HeroAttributes(name, maxHP, maxSP, level, exp, expLevelUp);
				
				//Stages Completed: values[1];
				int stagesCompleted = Integer.parseInt(values[1]);
				map = new Map(stagesCompleted);
				
				
				//get the next line. These are equipped weapons.
				str = inputFile.nextLine ( );
				values = str.split ("\\|");
				String primary = values[0];
				String secondary = values[1];
				
				
				//get the next line. These are equipped armors.
				str = inputFile.nextLine ( );
				values = str.split ("\\|");
				String head = values[0];
				String torso = values[1];
				String hands = values[2];
				String leg = values[3];
				String foot = values[4];
				String accessory = values[5];
				String accessory2 = values[6];
				
				
				//get the next line. These are all owned weapons.
				str = inputFile.nextLine ( );
				values = str.split ("\\|");
				//Import all the weapons the hero owned in the save file and their equipped weapons.
				heroAttributes.getEquipment ( ).importWeapons (values, primary, secondary);
				
				
				
				//get the next line. These are all owned armors.
				str = inputFile.nextLine ( );
				values = str.split ("\\|");
				//Imports all the armors the hero owned in the save file and their equipped armors.
				heroAttributes.getEquipment ( ).importArmors (values, head, torso, hands, leg, foot, accessory, accessory2);
			}
			else //If there is no data. Asks the user to start a new game.
			{
				JOptionPane.showMessageDialog (null, "Sorry, this save file has no data associated with it.\nPlease begin a new game.", "No Save Data", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		else if(pick == 1) //File 2
		{
			//Loads data from File 2
			if(inputFile2.hasNext ( )) 
			{
				String str = inputFile2.nextLine();
				//Say for example line 1 had:
				//Pvt. James E. Clipse|1|3|90|121|
				//Name|StagesCompleted|Level|Exp|Exp to Level Up|
				String[] values = str.split ("\\|");
				
				//Name: values[0];
				String name = values[0];
				
				//maxHP equals level*100.
				int maxHP = (100*Integer.parseInt (values[2]));
				//maxSP equals (45 + (5*level)).
				int maxSP = (45+(5*Integer.parseInt (values[2])));
				//Level: values[2];
				int level = Integer.parseInt (values[2]);
				//Experience: values[3];
				int exp = Integer.parseInt (values[3]);
				//Experience: to Level Up values[4];
				int expLevelUp = Integer.parseInt (values[4]);
				
				heroAttributes = new HeroAttributes(name, maxHP, maxSP, level, exp, expLevelUp);
				
				//Stages Completed: values[1];
				int stagesCompleted = Integer.parseInt(values[1]);
				map = new Map(stagesCompleted);
				
				
				//get the next line. These are equipped weapons.
				str = inputFile2.nextLine ( );
				values = str.split ("\\|");
				String primary = values[0];
				String secondary = values[1];
				
				
				//get the next line. These are equipped armors.
				str = inputFile2.nextLine ( );
				values = str.split ("\\|");
				String head = values[0];
				String torso = values[1];
				String hands = values[2];
				String leg = values[3];
				String foot = values[4];
				String accessory = values[5];
				String accessory2 = values[6];
				
				
				//get the next line. These are all owned weapons.
				str = inputFile2.nextLine ( );
				values = str.split ("\\|");
				//Import all the weapons the hero owned in the save file and their equipped weapons.
				heroAttributes.getEquipment ( ).importWeapons (values, primary, secondary);
				
				
				
				//get the next line. These are all owned armors.
				str = inputFile2.nextLine ( );
				values = str.split ("\\|");
				//Imports all the armors the hero owned in the save file and their equipped armors.
				heroAttributes.getEquipment ( ).importArmors (values, head, torso, hands, leg, foot, accessory, accessory2);
			}
			else //If there is no data in the save file that the player chose.
			{
				JOptionPane.showMessageDialog (null, "Sorry, this save file has no data associated with it.\nPlease begin a new game.", "No Save Data", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		click();
		return true;
	}
	
	/**
	 * This method allows the user to play a previous stage, based on their progress.
	 *
	 * <hr>
	 * Date created: Apr 3, 2021
	 * Last Modified: May 29, 2022
	 * <hr>
	 */
	private static void stageSelect() 
	{
		stopMusic();
		playMusic("MainMenu", true);		
		
		int page = 1;
		if(map.getStagesCompleted ( ) == 0) //Possible to achieve if a player chose to escape the tutorial. Allen wouldn't be happy.
		{
			String[] stages = {"Main Menu", "Tutorial"};
			
			int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages, stages[0]);
			if(pick == 1) //Tutorial
			{
				chapterBegin(0);
				return;
			}
			else 
			{
				return; //Return to the main menu.
			}
		}
		else if(map.getStagesCompleted ( ) == 1) //stageSelect after completing the Tutorial.
		{
			String[] stages = {"Main Menu", "Tutorial", ">>"};
			String[] stages2 = {"<<", "Chapter 1 - Rebellion"};
			
			while(true) 
			{
				if(page == 1) 
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages, stages[0]);
					if(pick == 1) //Begins tutorial
					{
						chapterBegin(0);
						return;
					}
					else if(pick == 2) //Next page
					{
						page++;
					}
					else //X or Main Menu pressed
					{
						return; //Return to the main menu.
					}
				}
				else //page is 2.
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages2, stages2[0]);
					if(pick == 0) //<<
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 1 - Rebellion
					{
						chapterBegin(1);
						return;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
			}
		}
		else if(map.getStagesCompleted() == 2) //stageSelect after completing Chapter 1.
		{
			String[] stages = {"Main Menu", "Tutorial", ">>"};
			String[] stages2 = {"<<", "Chapter 1 - Rebellion", ">>"};
			String[] stages3 = {"<<", "Chapter 2 - Defense"};
			
			while(true) 
			{
				if(page == 1) 
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages, stages[0]);
					if(pick == 1) //Begins tutorial
					{
						chapterBegin(0);
						return;
					}
					else if(pick == 2) //Next page
					{
						page++;
					}
					else //Main Menu or X
					{
						return; //Return to the main menu.
					}
				}
				else if(page == 2)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages2, stages2[0]);
					if(pick == 0) //Previous page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 1 - Rebellion
					{
						chapterBegin(1);
						return;
					}
					else if(pick == 2) //Next page
					{
						page++;
					}
					else // X pressed
					{
						return; //Return to the main menu.
					}
				}
				else //page is 3.
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages3, stages3[0]);
					if(pick == 0) //Previous page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 2 - Defense
					{
						chapterBegin(2);
						return;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
			}
		}
		else if(map.getStagesCompleted() == 3) //stageSelect after completing Chapter 2.
		{
			String[] stages = {"Main Menu", "Tutorial", ">>"};
			String[] stages2 = {"<<", "Chapter 1 - Rebellion", ">>"};
			String[] stages3 = {"<<", "Chapter 2 - Defense", ">>"};
			String[] stages4 = {"<<", "Chapter 3 - Revenge"};
			
			while(true) 
			{
				if(page == 1) 
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages, stages[0]);
					if(pick == 1) //Begins tutorial
					{
						chapterBegin(0);
						return;
					}
					else if(pick == 2) //Next Page
					{
						page++;
					}
					else //X or Main menu pressed
					{
						return; //Return to the main menu.
					}
				}
				else if(page == 2)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages2, stages2[0]);
					if(pick == 0) //Previous page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 1 - Rebellion.
					{
						chapterBegin(1);
						return;
					}
					else if(pick == 2) //Next page
					{
						page++;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
				else if (page == 3)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages3, stages3[0]);
					if(pick == 0) //Previous page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 2 - Defense
					{
						chapterBegin(2);
						return;
					}
					else if(pick == 2) //Next Page
					{
						page++;
					}
					else 
					{
						return; //Return to the main menu.
					}
				}
				else //if page is 4.
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages4, stages4[0]);
					if(pick == 0) //Previous page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 3 - Revenge
					{
						chapterBegin(3);
						return;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
			}
		}
		else if(map.getStagesCompleted() == 4) //stageSelect after completing Chapter 3.
		{
			String[] stages = {"Main Menu", "Tutorial", ">>"};
			String[] stages2 = {"<<", "Chapter 1 - Rebellion", ">>"};
			String[] stages3 = {"<<", "Chapter 2 - Defense", ">>"};
			String[] stages4 = {"<<", "Chapter 3 - Revenge", ">>"};
			String[] stages5 = {"<<", "Chapter 4 - Infiltration"};
			
			while(true) 
			{
				if(page == 1) 
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages, stages[0]);
					if(pick == 1) //Begins tutorial
					{
						chapterBegin(0);
						return;
					}
					else if(pick == 2) //Next Page
					{
						page++;
					}
					else //X or main menu pressed
					{
						return; //Return to the main menu.
					}
				}
				else if(page == 2)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages2, stages2[0]);
					if(pick == 0) //Previous page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 1 - Rebellion
					{
						chapterBegin(1);
						return;
					}
					else if(pick == 2) //Next page
					{
						page++;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
				else if (page == 3)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages3, stages3[0]);
					if(pick == 0) //Previous Page
					{
						page--;
					}
					else if(pick == 1)  //Begins Chapter 2 - Defense
					{
						chapterBegin(2);
						return;
					}
					else if(pick == 2) //Next page
					{
						page++;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
				else if(page == 4)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages4, stages4[0]);
					if(pick == 0)  //Previous Page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 3 - Revenge
					{
						chapterBegin(3);
						return;
					}
					else if(pick == 2) //Next page
					{
						page++;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
				else //if page is 5.
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages5, stages5[0]);
					if(pick == 0)  //Previous Page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 4 - Infiltration
					{
						chapterBegin(4);
						return;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
			}
		}
		else if(map.getStagesCompleted() == 4) //stageSelect after completing Chapter 4.
		{
			String[] stages = {"Main Menu", "Tutorial", ">>"};
			String[] stages2 = {"<<", "Chapter 1 - Rebellion", ">>"};
			String[] stages3 = {"<<", "Chapter 2 - Defense", ">>"};
			String[] stages4 = {"<<", "Chapter 3 - Revenge", ">>"};
			String[] stages5 = {"<<", "Chapter 4 - Infiltration", ">>"};
			String[] stages6 = {"<<", "Chapter 5 - Rebellion HQ"};
			
			while(true) 
			{
				if(page == 1) 
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages, stages[0]);
					if(pick == 1) //Begins tutorial
					{
						chapterBegin(0);
						return;
					}
					else if(pick == 2) //Next Page
					{
						page++;
					}
					else //X or Main Menu pressed
					{
						return; //Return to the main menu.
					}
				}
				else if(page == 2)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages2, stages2[0]);
					if(pick == 0) //Previous page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 1 - Rebellion
					{
						chapterBegin(1);
					}
					else if(pick == 2) //Next page
					{
						page++;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
				else if (page == 3)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages3, stages3[0]);
					if(pick == 0) //Previous Page
					{
						page--;
					}
					else if(pick == 1)  //Begins Chapter 2 - Defense
					{
						chapterBegin(2);
						return;
					}
					else if(pick == 2) //Next page
					{
						page++;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
				else if(page == 4)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages4, stages4[0]);
					if(pick == 0)  //Previous Page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 3 - Revenge
					{
						chapterBegin(3);
						return;
					}
					else if(pick == 2) //Next page
					{
						page++;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
				else if(page == 5)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages5, stages5[0]);
					if(pick == 0)  //Previous Page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 4 - Infiltration
					{
						chapterBegin(4);
						return;
					}
					else if(pick == 2) 
					{
						page++;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
				else if(page == 6)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages6, stages6[0]);
					if(pick == 0)  //Previous Page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 5 - Rebellion HQ
					{
						chapterBegin(5);
						return;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
			}
		}
		else //stageSelect after completing Chapter 5.
		{
			String[] stages = {"Main Menu", "Tutorial", ">>"};
			String[] stages2 = {"<<", "Chapter 1 - Rebellion", ">>"};
			String[] stages3 = {"<<", "Chapter 2 - Defense", ">>"};
			String[] stages4 = {"<<", "Chapter 3 - Revenge", ">>"};
			String[] stages5 = {"<<", "Chapter 4 - Infiltration", ">>"};
			String[] stages6 = {"<<", "Chapter 5 - Rebellion HQ", ">>"};
			String[] stages7 = {"<<", "Skirmish Mode"};
			
			while(true) 
			{
				if(page == 1) 
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages, stages[0]);
					if(pick == 1) //Begins tutorial
					{
						chapterBegin(0);
						return;
					}
					else if(pick == 2) //Next Page
					{
						page++;
					}
					else //X or Main Menu pressed
					{
						return; //Return to the main menu.
					}
				}
				else if(page == 2)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages2, stages2[0]);
					if(pick == 0) //Previous page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 1 - Rebellion
					{
						chapterBegin(1);
					}
					else if(pick == 2) //Next page
					{
						page++;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
				else if (page == 3)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages3, stages3[0]);
					if(pick == 0) //Previous Page
					{
						page--;
					}
					else if(pick == 1)  //Begins Chapter 2 - Defense
					{
						chapterBegin(2);
						return;
					}
					else if(pick == 2) //Next page
					{
						page++;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
				else if(page == 4)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages4, stages4[0]);
					if(pick == 0)  //Previous Page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 3 - Revenge
					{
						chapterBegin(3);
						return;
					}
					else if(pick == 2) //Next page
					{
						page++;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
				else if(page == 5)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages5, stages5[0]);
					if(pick == 0)  //Previous Page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 4 - Infiltration
					{
						chapterBegin(4);
						return;
					}
					else if(pick == 2) 
					{
						page++;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
				else if(page == 6)
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages6, stages6[0]);
					if(pick == 0)  //Previous Page
					{
						page--;
					}
					else if(pick == 1) //Begins Chapter 5 - Rebellion HQ
					{
						chapterBegin(5);
						return;
					}
					else if(pick == 2) 
					{
						page++;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
				else //page 7 - Skirmish Mode
				{
					int pick = JOptionPane.showOptionDialog (null, "Please choose a chapter.", "Stage Selection", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, stages7, stages7[0]);
					if(pick == 0)  //Previous Page
					{
						page--;
					}
					else if(pick == 1) //Begins a Skirmish Battle
					{
						chapterBegin(7);
						return;
					}
					else //X pressed
					{
						return; //Return to the main menu.
					}
				}
			}
		}
	}
	
	
	/**
	 * Allow the user to prepare for battle.
	 * They may change their Weapons and Equipment here.
	 * Returns false if the user presses X. 
	 * Otherwise, returns true.
	 *
	 * <hr>
	 * Date created: Apr 4, 2021
	 *
	 * <hr>
	 * @return boolean
	 */
	private static boolean preparations() 
	{
		click();
		stopMusic();
		playMusic("Preparations", true);
		String[] options = {"Change Armor", "Change Weapons", "View Attributes", "View Skills", "Ready!"};
		
		//breaks out of the loop when the player chooses Ready! or X.
		while(true) 
		{
			int optionsPick = JOptionPane.showOptionDialog(null, "Prepare for the upcoming battle!\n" + heroAttributes.toString ( ), "Preparations", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options, options[4]);
			click();
			if(optionsPick == 0) //Change Armor
			{
				int pick = 0;
				while(pick != -1 && pick != 6) 
				{
					Equipment temp = heroAttributes.getEquipment ( );
					//This would have been a much longer piece of code without the use of Equipment temp, which acts as a reference.
					String[] armors = {temp.getHeadArmor ( ).getArmorName ( ), temp.getTorsoArmor ( ).getArmorName ( ), temp.getHandArmor ( ).getArmorName ( ), 
									temp.getLegArmor ( ).getArmorName ( ), temp.getFootArmor ( ).getArmorName ( ), "Accessories", "Back"};
					pick = JOptionPane.showOptionDialog (null, "Change which piece of armor?", "Armor", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, armors, armors[0]);

					ArrayList<Armor> armorChoices;
					if(pick == 0) //Change head armor
					{
						//Get all the head Armors from the hero's equipment.
						armorChoices = heroAttributes.getEquipment ( ).getArmorArrayList ("Head");
						String[] helmets = new String[temp.getArmorArrayList ("Head").size()];
						ArrayList<Armor> headTemp = temp.getArmorArrayList ("Head");
						//Initialize the helmets string array to be used in the JOptionPane option dialog.
						for(int i = 0; i < temp.getArmorArrayList ("Head").size ( ); i++) 
						{
							helmets[i] = headTemp.get (i).getArmorName ( ) + " (" + headTemp.get(i).getTotalDefense ( ) + "AP|" + headTemp.get (i).getLuck ( ) + "LK)";
						}
						
						pick = JOptionPane.showOptionDialog (null, "Which helmet should be equipped?", "Helmet", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, helmets, helmets[0]);
						click();
						if(pick != -1) //The user doesn't press 'X'. This program would crash without this.
						{
							heroAttributes.getEquipment().setHeadArmor (armorChoices.get(pick));
							JOptionPane.showMessageDialog (null, "The " + armorChoices.get (pick).getArmorName ( ) + " was successfully equipped.");
						}
					}
					else if(pick == 1) //Change Torso Armor
					{
						//Get all the torso Armors from the hero's equipment.
						armorChoices = heroAttributes.getEquipment ( ).getArmorArrayList ("Torso");
						String[] bodyArmors = new String[temp.getArmorArrayList ("Torso").size()];
						ArrayList<Armor> torsoTemp = temp.getArmorArrayList ("Torso");
						//Initialize the bodyArmors string array to be used in the JOptionPane option dialog.
						for(int i = 0; i < temp.getArmorArrayList ("Torso").size ( ); i++) 
						{
							bodyArmors[i] = torsoTemp.get (i).getArmorName ( ) + " (" + torsoTemp.get(i).getTotalDefense ( ) + "AP|" + torsoTemp.get (i).getLuck ( ) + "LK)";
						}
						
						pick = JOptionPane.showOptionDialog (null, "Which Body Armor should be equipped?", "Body Armor", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, bodyArmors, bodyArmors[0]);
						click();
						if(pick != -1) //The user doesn't press 'X'. This program would crash without this.
						{
							heroAttributes.getEquipment().setTorsoArmor (armorChoices.get(pick));
							JOptionPane.showMessageDialog (null, "The " + armorChoices.get (pick).getArmorName ( ) + " was successfully equipped.");
						}
					}
					else if(pick == 2) //Change Hand Armor
					{
						//Get all the hand Armors from the hero's equipment.
						armorChoices = heroAttributes.getEquipment ( ).getArmorArrayList ("Hand");
						String[] hands = new String[temp.getArmorArrayList ("Hand").size()];
						ArrayList<Armor> handsTemp = temp.getArmorArrayList ("Hand");
						//Initialize the hands string array to be used in the JOptionPane option dialog.
						for(int i = 0; i < temp.getArmorArrayList ("Hand").size ( ); i++) 
						{
							hands[i] = handsTemp.get (i).getArmorName ( ) + " (" + handsTemp.get(i).getTotalDefense ( ) + "AP|" + handsTemp.get (i).getLuck ( ) + "LK)";
						}
						
						pick = JOptionPane.showOptionDialog (null, "Which hand armor should be equipped?", "Hand Armor", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, hands, hands[0]);
						click();
						if(pick != -1) //The user doesn't press 'X'. This program would crash without this.
						{
							heroAttributes.getEquipment().setHandArmor (armorChoices.get(pick));
							JOptionPane.showMessageDialog (null, "The " + armorChoices.get (pick).getArmorName ( ) + " was successfully equipped.");
						}
					}
					else if(pick == 3) //Change Leg Armor
					{
						//Get all the leg Armors from the hero's equipment.
						armorChoices = heroAttributes.getEquipment ( ).getArmorArrayList ("Leg");
						String[] legs = new String[temp.getArmorArrayList ("Leg").size()];
						ArrayList<Armor> legTemp = temp.getArmorArrayList ("Leg");
						//Initialize the legs string array to be used in the JOptionPane option dialog.
						for(int i = 0; i < temp.getArmorArrayList ("Leg").size ( ); i++) 
						{
							legs[i] = legTemp.get (i).getArmorName ( ) + " (" + legTemp.get(i).getTotalDefense ( ) + "AP|" + legTemp.get (i).getLuck ( ) + "LK)";
						}
						
						pick = JOptionPane.showOptionDialog (null, "Which Leg Armor should be equipped?", "Leg Armor", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, legs, legs[0]);
						click();
						if(pick != -1) //The user doesn't press 'X'. This program would crash without this.
						{
							heroAttributes.getEquipment().setLegArmor (armorChoices.get(pick));
							JOptionPane.showMessageDialog (null, "The " + armorChoices.get (pick).getArmorName ( ) + " was successfully equipped.");
						}
					}
					else if(pick == 4) //Change Foot Armor
					{
						//Get all the feet Armors from the hero's equipment.
						armorChoices = heroAttributes.getEquipment ( ).getArmorArrayList ("Foot");
						String[] feet = new String[temp.getArmorArrayList ("Foot").size()];
						ArrayList<Armor> footTemp = temp.getArmorArrayList ("Foot");
						//Initialize the feet string array to be used in the JOptionPane option dialog.
						for(int i = 0; i < temp.getArmorArrayList ("Foot").size ( ); i++) 
						{
							feet[i] = footTemp.get (i).getArmorName ( ) + " (" + footTemp.get(i).getTotalDefense ( ) + "AP|" + footTemp.get (i).getLuck ( ) + "LK)";
						}
						
						pick = JOptionPane.showOptionDialog (null, "Which Foot Armor should be equipped?", "Foot Armor", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, feet, feet[0]);
						click();
						if(pick != -1) //The user doesn't press 'X'. This program would crash without this.
						{
							heroAttributes.getEquipment().setFootArmor (armorChoices.get(pick));
							JOptionPane.showMessageDialog (null, "The " + armorChoices.get (pick).getArmorName ( ) + " was successfully equipped.");
						}
					}
					else if(pick == 5) //Change Accessories
					{
						//Get all the accessories from the hero's equipment.
						String[] accessories = {temp.getAccessory ( ).getArmorName ( ), temp.getAccessory2 ( ).getArmorName ( )};
						pick = JOptionPane.showOptionDialog (null, "Change which accessory?", "Armor", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, accessories, accessories[0]);
						click();
						if(pick == 0) //Change Primary Accessory
						{
							armorChoices = heroAttributes.getEquipment ( ).getArmorArrayList ("Accessory");
							String[] pAccessories = new String[temp.getArmorArrayList ("Accessory").size()];
							ArrayList<Armor> accTemp = temp.getArmorArrayList ("Accessory");
							//Initialize the pAccessories string array to be used in the JOptionPane option dialog.
							for(int i = 0; i < temp.getArmorArrayList ("Accessory").size ( ); i++) 
							{
								pAccessories[i] = accTemp.get (i).getArmorName ( ) + " (" + accTemp.get(i).getTotalDefense ( ) + "AP|" + accTemp.get (i).getLuck ( ) + "LK)";
							}
							
							pick = JOptionPane.showOptionDialog (null, "Which accessory should be equipped?", "Accessory", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, pAccessories, pAccessories[0]);
							click();
							if(pick != -1) //The user doesn't press 'X'. This program would crash without this.
							{
								heroAttributes.getEquipment().setAccessory (armorChoices.get(pick));
								JOptionPane.showMessageDialog (null, "The " + armorChoices.get (pick).getArmorName ( ) + " was successfully equipped.");
							}
						}
						else if(pick == 1) //Change Secondary Accessory.
						{
							//Get all the secondary accessories from the hero's equipment.
							armorChoices = heroAttributes.getEquipment ( ).getArmorArrayList ("Accessory2");
							String[] sAccessories = new String[temp.getArmorArrayList ("Accessory2").size()];
							ArrayList<Armor> acc2Temp = temp.getArmorArrayList ("Accessory2");
							//Initialize the sAccessories string array to be used in the JOptionPane option dialog.
							for(int i = 0; i < temp.getArmorArrayList ("Accessory2").size ( ); i++) 
							{
								sAccessories[i] = acc2Temp.get (i).getArmorName ( ) + " (" + acc2Temp.get(i).getTotalDefense ( ) + "AP|" + acc2Temp.get (i).getLuck ( ) + "LK)";
							}
							
							pick = JOptionPane.showOptionDialog (null, "Which secondary accessory should be equipped?", "Secondary Accessory", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, sAccessories, sAccessories[0]);
							click();
							if(pick != -1) //The user doesn't press 'X'. This program would crash without this.
							{
								heroAttributes.getEquipment().setAccessory2 (armorChoices.get(pick));
								JOptionPane.showMessageDialog (null, "The " + armorChoices.get (pick).getArmorName ( ) + " was successfully equipped.");
							}
						}
						else //if 'X' is pressed.
						{
							break;
						
						}
					}
				}
			}
			else if(optionsPick == 1) //Change Weapons
			{
				int pick = 0;
				while(pick != -1 && pick != 2) 
				{
					String primaryName = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getWeaponName ( );
					String secondaryName = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getWeaponName ( );
					String[] weapons = {primaryName, secondaryName, "Back"};
					
					//First asks the player which weapon they want to switch.
					pick = JOptionPane.showOptionDialog (null, "Which weapon should be swapped?", "Swap", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, weapons, weapons[0]);
					String[] type = {"Pistol", "Rifle", "Assault Rifle"};
					
					//temp will store an array of weapons in the following code.
					ArrayList<Weapon> temp = null;
					if(pick == 0) //Swap with Primary Weapon
					{
						//Asks the user to swap with a weapon from which category.
						pick = JOptionPane.showOptionDialog (null, "Weapon to be swapped: " + primaryName + "\n\nSwap with a weapon of which category?", "Swap Category", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, type, type[0]);
						if(pick == 0) //Pistol Category
						{
							temp = heroAttributes.getEquipment ( ).getWeaponArrayList ("Pistol");
							String[] pistols = new String[temp.size()];
							//Initialize the pistols array for the JOptionPane option dialog.
							for(int i = 0; i < temp.size(); i++)
							{
								pistols[i] = temp.get (i).getWeaponName ( ) + "(" + temp.get (i).getTotalDamage ( ) + "DMG|" + temp.get (i).getClipSize ( ) + "Ammo|" + temp.get (i).getRps ( ) + "RPS)";
							}
							
							pick = JOptionPane.showOptionDialog (null, "Swap with which Pistol?", "Swap Choice", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, pistols, pistols[0]);
							click();			
							if(pick != -1) //The user doesn't press 'X'
							{
								heroAttributes.getEquipment().setPrimaryWeapon (temp.get (pick));
								JOptionPane.showMessageDialog (null, "Your primary weapon has successfully been swapped with the " + temp.get (pick).getWeaponName ( ) + ".");
							}
						}
						else if(pick == 1) //Rifle Category
						{
							temp = heroAttributes.getEquipment ( ).getWeaponArrayList ("Rifle");
							String[] rifles = new String[temp.size()];
							//Initialize the rifles array for the JOptionPane option dialog.
							for(int i = 0; i < temp.size(); i++)
							{
								rifles[i] = temp.get (i).getWeaponName ( ) + "(" + temp.get (i).getTotalDamage ( ) + "DMG|" + temp.get (i).getClipSize ( ) + "Ammo|" + temp.get (i).getRps ( ) + "RPS)";
							}
							
							pick = JOptionPane.showOptionDialog (null, "Swap with which Rifle?", "Swap Choice", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, rifles, rifles[0]);	
							click();				
							if(pick != -1) //The user doesn't press 'X'
							{
								heroAttributes.getEquipment().setPrimaryWeapon (temp.get (pick));
								JOptionPane.showMessageDialog (null, "Your primary weapon has successfully been swapped with the " + temp.get (pick).getWeaponName ( ) + ".");
							}
						}
						else if(pick == 2) //Assault Rifle Category
						{
							temp = heroAttributes.getEquipment ( ).getWeaponArrayList ("Assault Rifle");
							String[] arifles = new String[temp.size()];
							//The player does not begin with an assault rifle, so adding this if-statement is important to prevent an IndexOutOfBounds error.
							if(arifles.length > 0) 
							{
								//Initialize the arifles array for the JOptionPane option dialog.
								for(int i = 0; i < temp.size(); i++)
								{
									arifles[i] = temp.get (i).getWeaponName ( ) + "(" + temp.get (i).getTotalDamage ( ) + "DMG|" + temp.get (i).getClipSize ( ) + "Ammo|" + temp.get (i).getRps ( ) + "RPS)";
								}
								
								pick = JOptionPane.showOptionDialog (null, "Swap with which Assault Rifle?", "Swap Choice", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, arifles, arifles[0]);		
								click();			
								if(pick != -1) //The user doesn't press 'X'
								{
									heroAttributes.getEquipment().setPrimaryWeapon (temp.get (pick));
									JOptionPane.showMessageDialog (null, "Your primary weapon has successfully been swapped with the " + temp.get (pick).getWeaponName ( ) + ".");
								}
							}
							else 
							{
								JOptionPane.showMessageDialog (null, "You currently do not own any Assualt Rifles.", "No Assault Rifle", JOptionPane.INFORMATION_MESSAGE);
								pick = -1;
							}
						}
						else 
						{
							//Do nothing. Return to the Preparations menu.
						}
						pick = 0;
					}
					else if(pick == 1) //Swap with the Secondary Weapon.
					{
						//Asks the user to swap with a weapon from which category.
						pick = JOptionPane.showOptionDialog (null, "Weapon to be swapped: " + secondaryName + "\n\nSwap with a weapon of which category?", "Swap Category", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, type, type[0]);
						if(pick == 0) //Pistol Category
						{
							temp = heroAttributes.getEquipment ( ).getWeaponArrayList ("Pistol");
							String[] pistols = new String[temp.size()];
							//Initialize the pistols array for the JOptionPane option dialog.
							for(int i = 0; i < temp.size(); i++)
							{
								pistols[i] = temp.get (i).getWeaponName ( ) + "(" + temp.get (i).getTotalDamage ( ) + "DMG|" + temp.get (i).getClipSize ( ) + "Clip|" + (int)(temp.get(i).getTotalAccuracy()*100) + "% Hit)";
							}
							
							pick = JOptionPane.showOptionDialog (null, "Swap with which Pistol?", "Swap Choice", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, pistols, pistols[0]);
							click();
							if(pick != -1) //The user doesn't press 'X'
							{
								heroAttributes.getEquipment().setSecondaryWeapon (temp.get (pick));
								JOptionPane.showMessageDialog (null, "Your secondary weapon has successfully been swapped with the " + temp.get (pick).getWeaponName ( ) + ".");
							}
						}
						else if(pick == 1) //Rifle Category
						{
							temp = heroAttributes.getEquipment ( ).getWeaponArrayList ("Rifle");
							String[] rifles = new String[temp.size()];
							//Initialize the rifles array for the JOptionPane option dialog.
							for(int i = 0; i < temp.size(); i++)
							{
								rifles[i] = temp.get (i).getWeaponName ( ) + "(" + temp.get (i).getTotalDamage ( ) + "DMG|" + temp.get (i).getClipSize ( ) + "Clip|" + (int)(temp.get(i).getTotalAccuracy()*100) + "% Hit)";
							}
							
							pick = JOptionPane.showOptionDialog (null, "Swap with which Rifle?", "Swap Choice", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, rifles, rifles[0]);
							click();
							if(pick != -1) //The user doesn't press 'X'
							{
								heroAttributes.getEquipment().setSecondaryWeapon (temp.get (pick));
								JOptionPane.showMessageDialog (null, "Your secondary weapon has successfully been swapped with the " + temp.get (pick).getWeaponName ( ) + ".");
							}
						}
						else if(pick == 2) //Assault Rifle Category
						{
							temp = heroAttributes.getEquipment ( ).getWeaponArrayList ("Assault Rifle");
							String[] arifles = new String[temp.size()];
							//The player does not begin with an assault rifle, so adding this if-statement is important to prevent an IndexOutOfBounds error.
							if(arifles.length > 0) 
							{
								//Initialize the arifles array for the JOptionPane option dialog.
								for(int i = 0; i < temp.size(); i++)
								{
									arifles[i] = temp.get (i).getWeaponName ( ) + "(" + temp.get (i).getTotalDamage ( ) + "DMG|" + temp.get (i).getClipSize ( ) + "Clip|" + (int)(temp.get(i).getTotalAccuracy()*100) + "% Hit)";
								}
								
								pick = JOptionPane.showOptionDialog (null, "Swap with which Assault Rifle?", "Swap Choice", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, arifles, arifles[0]);
								click();
								if(pick != -1) //The user doesn't press 'X'
								{
									heroAttributes.getEquipment().setSecondaryWeapon (temp.get (pick));
									JOptionPane.showMessageDialog (null, "Your secondary weapon has successfully been swapped with the " + temp.get (pick).getWeaponName ( ) + ".");
								}
							}
							else 
							{
								JOptionPane.showMessageDialog (null, "You currently do not own any Assualt Rifles.", "No Assault Rifle", JOptionPane.INFORMATION_MESSAGE);
								pick = -1;
							}
						}
						else //X is pressed while choosing a weapon from the _ category.
						{
							break;
						}
					}
				}
			}
			else if(optionsPick == 2) //View Attributes 
			{
				JOptionPane.showMessageDialog (null, heroAttributes.toString ( ) + heroAttributes.getAttributeDescription ( ), "Attributes and Descriptions", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(optionsPick == 3) //View Skills
			{
				JOptionPane.showMessageDialog (null, heroAttributes.getSpecialSkillDescription ( ), "Attributes and Descriptions", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(optionsPick == 4) //Ready!
			{
				return true; //Proceed with the Chapter.
			}
			else //X
			{
				return false; //Go back to the stage select.
			}
			click();
		}
	}
	
	
	/**
	 * Shows the plot before the battle.
	 * Of course, it's not as rich as it is in Zork.
	 *
	 * <hr>
	 * Date created: Apr 3, 2021
	 * Last Modified: May 29, 2022
	 * <hr>
	 * @param chapterNum
	 */
	private static void chapterBegin(int chapterNum) 
	{
		click();
		if(chapterNum == 0) //Introduction & Tutorial
		{
			String[] option = {"What?"};
			//The date in this message is not mispelled. This is a different planet, after all.
			JOptionPane.showOptionDialog (null, "It's Day 40 of Basic Space Ranger training.\n\nPlanet: Sigia-X42\nLocation: Allendale Ranger Barracks\nRanger: " + heroAttributes.getName() + "\nDate: 16/37/1916", "Introduction", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();

			option[0] = "OK";
			JOptionPane.showOptionDialog (null, "The starry night fills the sky, as the sun shines on the barracks.\nIt won't be long until our first assignment.", "Introduction", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			JOptionPane.showOptionDialog (null, "Day 40 is an important day.\nWe must successfully secure the building by ourselves in order to make it.", "Introduction", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();

			stopMusic();
			//This briefing music will be replaced in the battle class, as defined below.
			playMusic("Briefing", true);
			
			option[0] = "!!";
			JOptionPane.showOptionDialog (null, "\"Listen up, privates!\" Sgt. Allen shouts.\n\"What you've been doing up to now will determine your fate on the battlefield.\"", "Introduction", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();

			option[0] = "OK";
			JOptionPane.showOptionDialog (null, "We all walk up to our individual building and pick up our gear there.\nI can tell that a majority of us are nervous.", "Introduction", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			JOptionPane.showOptionDialog (null, "\"Each building has 3 enemies inside,\" Sgt. Allen shouts, \n\"You must eliminate them all and proceed to the exit.\"", "Introduction", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();

			option[0] = "Got It.";
			JOptionPane.showOptionDialog (null, "Sgt. Allen looks at all of us. \n\"Let me repeat myself: You must clear everyone in the building first in order to progress.\"\n\"You CANNOT pass until every enemy is downed.\"", "Introduction", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();

			
			option[0] = "READY";
			JOptionPane.showOptionDialog (null, "\"Now, on your mark. Breach the door and clear it out!\"", "Introduction", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			//no preparations. This is because level 1 heroes don't have any new equipment.
			boolean wonBattle = mapNavigation(5, 5, 3, 0);
			
			//wonBattle is true if the player defeated all enemies in the map. Otherwise, returns false.
			if(wonBattle) 
			{
				stopMusic();
				playMusic("AfterMap", true);
				option[0] = "I am?";
				JOptionPane.showOptionDialog (null, "\"Time! It appears that " + heroAttributes.getName ( ) + " is the first to clear his building!\"", "Introduction", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
				click();
				
				option[0] = "Yes Sir.";
				JOptionPane.showOptionDialog (null, "Sergeant Allen looks at you closely. \n\"You've passed basic, " + heroAttributes.getName ( ) + ", and we have something urgent.\"" +
								"\n\"Meet me here at 0700 hours to discuss your first assignment.\"", "Introduction", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
				click();
				
				option[0] = "Indeed";
				JOptionPane.showOptionDialog (null, "Basic is over already... \nIt's hard to belive that it's been 40 days.", "Introduction", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
				click();
				
				//Ensures that this is the user's first time completing this level.
				if(map.getStagesCompleted ( ) <= 0) 
				{
					map.setStagesCompleted (1);
				}
			}
		}
		if(chapterNum == 1) // Rebellion Chapter
		{
			//Introduction leading into Chapter 1.
			String[] option = {"OK"};
			JOptionPane.showOptionDialog (null, "The bright sun illuminated all of the surrounding terrain.\nIt also came at the cost of a lot of heat.\nI'm glad briefing is inside this shaded tent.", "Chapter 1", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			JOptionPane.showOptionDialog (null, "Sergeant Allen walks into the tent carrying a piece of paper. \nHe spreads it out so all of us can see the contents written inside.", "Chapter 1", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();

			stopMusic();
			//This briefing music will be replaced in the battle class, as defined below.
			playMusic("Briefing", true);
			
			
			option[0] = "Rebellion?";
			JOptionPane.showOptionDialog (null, "\"Your first assignment is to clear a town of rebelling citizens in Sigrai City.\"\n\"Sigrai City is located directly to the South of this camp.\"", "Chapter 1", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Yes Sir!";
			JOptionPane.showOptionDialog (null, "\"We've recieved direct instructions to supress this rebellion.\"\n\"Leave no rebelling citizen out. Do I make myself clear?\"", "Chapter 1", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "I see.";
			JOptionPane.showOptionDialog (null, "He points to the west and east side of the city. \n\"We will halve our forces and 'pinch' the rebellion.\"", "Chapter 1", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Building?";
			JOptionPane.showOptionDialog (null, "Allen looks at you. \n\"" + heroAttributes.getName ( ) + ", you will assist the right flank by taking out this building.\"\nHe points to a large building on a map.", "Chapter 1", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "I see.";
			JOptionPane.showOptionDialog (null, "\"This building is one of the radio stations.\"\n\"We must eliminate them before they broadcast our location.\"", "Chapter 1", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Yes Sir!";
			JOptionPane.showOptionDialog (null, "\"While you are there, you will allow the other Rangers to conduct a full assault at the same time.\"\n\"Are you up to the task?\"", "Chapter 1", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Go Outside";
			JOptionPane.showOptionDialog (null, "Allen nods. He begins tasking the other rangers.", "Chapter 1", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Begin Preparations";
			JOptionPane.showOptionDialog (null, "With a day this hot, a march from here to Sigrai City may prove demoralizing.\nHopefully that building is air-conditioned.", "Chapter 1", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			//Prepare the user for battle. If they leave preparations, returns to stage select.
			if(preparations()) 
			{
				//Creates a 7X7 map with 4 enemies. This is Chapter 1.
				boolean wonBattle = mapNavigation(7, 7, 4, 1);

				//wonBattle is true if the player defeated all enemies in the map. Otherwise, returns false.
				//Post-Battle.
				if(wonBattle) 
				{
					stopMusic();
					playMusic("AfterMap", true);
					option[0] = "Back at Allendale Barracks";
					JOptionPane.showOptionDialog (null, "With the radio tower downed in time, you were able to secure an ambush.\nYou spend time defending the radio tower until the operation is finished.", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();
					
					option[0] = "Regiment 47?";
					JOptionPane.showOptionDialog (null, "\"Well done,\" Sergeant Allen says.\n\"Consider this your first completed mission as Regiment 47.\"", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();

					option[0] = "OK";
					JOptionPane.showOptionDialog (null, "Other rangers looked somewhat confused as well. \n\"Don't worry. You'll get used to it,\" Allen replies.", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();
					
					option[0] = "Alright!";
					JOptionPane.showOptionDialog (null, "\"Anyways,\" he begins, \"it will not be until next month for your next assignment.\"\n\"Rest well and boost your morale until then.\"", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();

					option[0] = "OK";
					JOptionPane.showOptionDialog (null, "You spend the next few days relaxing. \nAt the same time, you also prepare for Regiment 47's next assignment.", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					
					//Ensures that this is the user's first time completing this level.
					if(map.getStagesCompleted ( ) <= 1) 
					{
						map.setStagesCompleted (2);
					}
				}
			}
			else 
			{
				return;
			}
			
		}
		else if(chapterNum == 2) //Defense Chapter
		{
			String[] option = {"OK"};
			JOptionPane.showOptionDialog (null, "It is night at Allendale Ranger Barracks.\n\nPlanet: Sigia-X42\nLocation: Allendale Ranger Barracks\nRanger: " + heroAttributes.getName() + "\nDate: 16/45/1916", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "OK";
			JOptionPane.showOptionDialog (null, "\"Regiment 47... huh?\" you think, looking up at the ceiling on your bed.\n\"I can finally begin serving my country.\"", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Wow.";
			JOptionPane.showOptionDialog (null, "You look to the side. \nThe other 49 members of Regiment 47 sleep soundly, accompanied by the chirps of crickets.\n", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Hmm...";
			JOptionPane.showOptionDialog (null, "Moonlight shines through the windows, illuminating the gravel that makes up the barrack floor. \nThis scenery feels very familiar.", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Go Outside";
			JOptionPane.showOptionDialog (null, "Suddenly, you are struck with a sense of homesickness. \nYou no longer find yourself tired.", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "I Miss My Home...";
			JOptionPane.showOptionDialog (null, "Outside, you can fully feel the breeze of the wind. \nThe coolness brings back fond memories of your house.", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();

			stopMusic();
			//This briefing music will be replaced in the battle class, as defined below.
			playMusic("Ambience", true);
			
			option[0] = "What is it?";
			JOptionPane.showOptionDialog (null, "You can hear a faint sound in the wind.\nIs that an emergency alert?\nYou decide to check it out.", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "What?";
			JOptionPane.showOptionDialog (null, "Are those members of the rebellion? \nThey appear to be raiding the shared supplies of Regiments 41-50!", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Begin Preparations";
			JOptionPane.showOptionDialog (null, "Upon sneaking and closer inspection, you can confirm that these are not Regiment soldiers. \nIt's time to put a stop to them!", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			
			//Prepare the user for battle. If they leave preparations, returns to stage select.
			if(preparations()) 
			{
				//Creates a 7X7 map with 4 enemies. This is Chapter 2.
				boolean wonBattle = mapNavigation(7, 7, 4, 2);

				//wonBattle is true if the player defeated all enemies in the map. Otherwise, returns false.
				if(wonBattle) 
				{
					stopMusic();
					playMusic("AfterMap", true);
					option[0] = "OK";
					JOptionPane.showOptionDialog (null, "\"The raiders were successfully driven off. \nBefore returning to the barracks, you made sure no other raiders were in the base.\"", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();
					
					option[0] = "Sleep";
					JOptionPane.showOptionDialog (null, "You feel somewhat insecure, knowing that they got into the base.\nWere they only after the supplies?", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();
					
					option[0] = "The Next Morning";
					JOptionPane.showOptionDialog (null, "Perhaps you shouldn't dwell on it. \nYou go to sleep after clearing your mind.", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();
					
					option[0] = "Meeting";
					JOptionPane.showOptionDialog (null, "Upon getting up and leaving the barracks, we saw a note by the door. \"Meet at briefing room at 1000 Hours.\"", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();
					
					//Ensures that this is the user's first time completing this level.
					if(map.getStagesCompleted ( ) <= 2) 
					{
						map.setStagesCompleted (3);
					}
				}
			}
			else 
			{
				return;
			}
		}
		else if(chapterNum == 3) //Revenge Chapter
		{

			stopMusic();
			//This briefing music will be replaced in the battle class, as defined below.
			playMusic("Briefing", true);
			
			String[] option = {"Oh!"};
			JOptionPane.showOptionDialog (null, "Sergeant Allen appeared frustrated as we entered the briefing room. \n\"This isn't good,\" he says, \"members of the Rebellion last night attacked our camp.\"", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Not Good.";
			JOptionPane.showOptionDialog (null, "\"There were a few soldiers that helped repel some of them away,\" he said.\n\"But, most of them got away with our supplies.\"", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "OK";
			JOptionPane.showOptionDialog (null, "Sergeant Allen looks at all of us. \n\"Don't worry boys. Our next assignment is going to get those supplies back.\"", "Chapter 2", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Alright.";
			JOptionPane.showOptionDialog (null, "\"Listen up, everyone,\" Sergeant Allen begins. \n\"Today, we will secure and take back our supplies.\"", "Chapter 3", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "OK";
			JOptionPane.showOptionDialog (null, "He opens a strategy map and puts it on the table. \n\"We suspect that they're stashing our supplies in this building.\"" +
							"\nHe points to a building in a town that is east of Sigrai City.", "Chapter 3", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();

			option[0] = "OK";
			JOptionPane.showOptionDialog (null, "\"To mix up our strategy, we'll suprise them by attacking the building's rear entrance.\"\n\"From there, we clear our each floor room by room.\"", "Chapter 3", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Yes, Sir!";
			JOptionPane.showOptionDialog (null, "Allen looks at you. \"" + heroAttributes.getName ( ) + ", you will be tasked with the first floor.\nAre you up for this?\"", "Chapter 3", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();

			option[0] = "OK";
			JOptionPane.showOptionDialog (null, "\"Good.\" he looks at the other rangers to give them their tasks.", "Chapter 3", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Begin Preparations";
			JOptionPane.showOptionDialog (null, "Once briefing is over, we all have the last of our supplies before heading out.\nWe'll get those supplies back before we know it.", "Chapter 3", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();

			//Prepare the user for battle. If they leave preparations, returns to stage select.
			if(preparations()) 
			{
				//Creates a 8X8 map with 5 enemies. This is Chapter 3.
				boolean wonBattle = mapNavigation(8, 8, 5, 3);

				//wonBattle is true if the player defeated all enemies in the map. Otherwise, returns false.
				if(wonBattle) 
				{
					stopMusic();
					playMusic("AfterMap", true);
					option[0] = "To the Barracks!";
					JOptionPane.showOptionDialog (null, "After clearing out the enemies, your Regiment secures and readies the supplies. \nAllendale Barracks, here we come!", "Chapter 3", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();
					
					option[0] = "Nice.";
					JOptionPane.showOptionDialog (null, "\"Well done,\" Sergeant Allen says to all of us. \n\"I believe this calls for a victory party.\"", "Chapter 3", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();

					option[0] = "Homesick Again";
					JOptionPane.showOptionDialog (null, "Regiments 41-50 sat together and chatted while eating throughout the night. \nSeeing the lit lanterns and torches through the night reminded me of home.", "Chapter 3", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();
					
					option[0] = "OK";
					JOptionPane.showOptionDialog (null, "You decide to sit by yourself next to a lantern. Looking closely at the shadows that react to its flicker, it feels comforting.", "Chapter 3", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();

					option[0] = "OK";
					JOptionPane.showOptionDialog (null, "You shake your head. Home was a long time ago, and it's time to embrace the present." +
									"\nThese barracks are my new home. I must look forward and continue serving Regiment 47.", "Chapter 3", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					
					//Ensures that this is the user's first time completing this level.
					if(map.getStagesCompleted ( ) <= 3) 
					{
						map.setStagesCompleted (4);
					}
				}
			}
			else 
			{
				return;
			}
		}
		else if(chapterNum == 4) //Infiltration Chapter
		{
			String[] option = {"OK"};
			JOptionPane.showOptionDialog (null, "It is mid-day at Allendale Ranger Barracks. 2 weeks after retaking the supplies.\n\nPlanet: Sigia-X42\nLocation: Allendale Ranger Barracks - Briefing Room" +
							"\nRanger: " + heroAttributes.getName() + "\nDate: 17/04/1916", "Chapter 4", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();

			stopMusic();
			//This briefing music will be replaced in the battle class, as defined below.
			playMusic("Briefing", true);
			
			option[0] = "OK";
			JOptionPane.showOptionDialog (null, "\"We've located the Rebellion Headquarters in Sigia City, west of Sigrai.\"\n\"Regiments 41-50 have been tasked with putting it down.\"", "Chapter 4", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "OK";
			JOptionPane.showOptionDialog (null, "\"To start off, Regiment 47 will clear out surrounding buildings.\"\n\"Meanwhile, our main force will push closer to the Headquarter.\"", "Chapter 4", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "I understand.";
			JOptionPane.showOptionDialog (null, "Allen looks at you, signifying that he is going to give you a task. \"" + heroAttributes.getName ( ) + ", you will clear this shopping mall.\"\nHe points to a large building.", "Chapter 4", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Begin Preparations";
			JOptionPane.showOptionDialog (null, "He then turns to the other members and issues them orders. \nThis operation begins at 0400, so we can catch them by suprise.", "Chapter 4", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			
			//Prepare the user for battle. If they leave preparations, returns to stage select.
			if(preparations()) 
			{
				//Creates a 9X9 map with 6 enemies. This is Chapter 4.
				boolean wonBattle = mapNavigation(9, 9, 6, 4);

				//wonBattle is true if the player defeated all enemies in the map. Otherwise, returns false.
				if(wonBattle) 
				{
					stopMusic();
					playMusic("AfterMap", true);
					option[0] = "OK";
					JOptionPane.showOptionDialog (null, "The shopping mall was successfully cleared of hostiles. \nYou decide to join the main force, which has managed to breach the HQ.", "Chapter 4", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();
					
					option[0] = "OK";
					JOptionPane.showOptionDialog (null, "\"This is it.\"\n\"This is the fight that will put down the long-standing Rebellion.\"", "Chapter 4", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					
					//Ensures that this is the user's first time completing this level.
					if(map.getStagesCompleted ( ) <= 4) 
					{
						map.setStagesCompleted (5);
					}
				}
			}
			else 
			{
				return;
			}
		}
		else if(chapterNum == 5) //Rebellion HQ Chapter
		{

			stopMusic();
			//This briefing music will be replaced in the battle class, as defined below.
			playMusic("Briefing", true);
			
			String[] option = {"OK"};
			JOptionPane.showOptionDialog (null, "There are only red blaring sirens and alerts here. \n\nPlanet: Sigia-X42\nLocation: Rebellion Headquarters" +
							"\nRanger: " + heroAttributes.getName() + "\nDate: 17/05/1916", "Chapter 5", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "OK";
			JOptionPane.showOptionDialog (null, "The main force has successfully breached the Headquarters and began rounding up members of the Rebellion.\nDue to its size, we must clear each room individually.", "Chapter 5", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			option[0] = "Begin Preparations";
			JOptionPane.showOptionDialog (null, "This is it. \nI can only expect to see the most dangerous of enemies here.", "Chapter 5", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
			click();
			
			
			
			//Prepare the user for battle. If they leave preparations, returns to stage select.
			if(preparations()) 
			{
				//Creates a 10X10 map with 7 enemies. This is Chapter 5.
				boolean wonBattle = mapNavigation(10, 10, 7, 5);
				
				//wonBattle is true if the player defeated all enemies in the map. Otherwise, returns false.
				if(wonBattle) 
				{
					stopMusic();
					playMusic("AfterMap", true);
					option[0] = "OK";
					JOptionPane.showOptionDialog (null, "The Rebellion Leader pleads for his life upon his defeat.\nHe grabs his radio and urges everyone in the HQ to surrender.", "Chapter 5", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();
					
					option[0] = "What Now?";
					JOptionPane.showOptionDialog (null, "You gather the leader and nearby Rebellion members and regroup with the main force.\nThe battle has been won.", "Chapter 5", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();
					
					option[0] = "Awesome";
					JOptionPane.showOptionDialog (null, "Sergeant Allen appears from the group raising his weapon in victory. \n\"Hoorah, Rangers. We've earned ourselves a well-deserved win.\"", "Chapter 5", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();
					
					option[0] = "Hooray!";
					JOptionPane.showOptionDialog (null, "Hip hip...", "Chapter 5", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option, option[0]);
					click();
					
					
					
					boolean seeCredits = true;
					//The user does not get the choice to pick unless they have already completed the stage.
					if(map.getStagesCompleted ( ) == 6) 
					{
						int choice = JOptionPane.showConfirmDialog (null, "Would you like to see the credits?", "Credits", JOptionPane.YES_NO_OPTION);
						if(choice == 1) //User chooses "No".
						{
							seeCredits = false;
						}
					}
					//The Credits! map.getStagesCompleted will be set to 6 after the credits are finished.
					if(seeCredits) 
					{
						//Credits
						stopMusic();
						playMusic("EavingTime", true);
						JOptionPane.showMessageDialog (null, "Congratulations! You have beaten the game!\nThank you for playing The Sigian Conflict!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
						click();
						
						//Credits Roll
						JOptionPane.showMessageDialog (null, "Credits" +
															"\n---------------\n" +
															"Coding: Michael Ng" +
															"\nProgramming: Michael Ng" +
															"\nArt: Michael Ng" + //There isn't much art other than the JOptionPane menus.
															"\nWriting: Michael Ng" +
															"\nDirector: Michael Ng" +
															"\nPlanning: Michael Ng" +
															"\nTesting: Michael Ng", "Credits", JOptionPane.INFORMATION_MESSAGE);
						click();
						
						// JOptionPane.showMessageDialog (null, "Music originates from a variety of sources.\nThey are not owned by the developer." +
						// 									"\n---------------\n" +
						// 									"Fate Grand Order -First Order-: Eaving Time (Ending), KYRIELIGHT (Preparations), Scalavera (Battle), The Shield of Chaldeas (Battle 2).\n" + 
						// 									"Fate/Zero: Grief (Defeat).\n" + 
						// 									"Fire Emblem Fates: Get Item, Level Up.\n" + 
						// 									"Overwatch: Victory.\n" + 
						// 									"Valkyria Chronicles: Main Theme (Main Menu), Europa at War (Ambience), The Gallian War (AfterMap)\n" + 
						// 									"Valkyria Chronicles 2: Briefing\n" +
						// 									"Fire Emblem Three Houses: Fodlan Winds (Rain), Fodlan Winds (Thunder), Indomitable Will Rain (Final Boss)\n" +
						// 									"https://mixkit.co/free-sound-effects/click/: Click (Select click)\n" + 
						// 									"https://www.fesliyanstudios.com/royalty-free-sound-effects-download/video-game-menu-153: Click2 (Menu Button Press B Sound Effect)", "Music Credits", JOptionPane.INFORMATION_MESSAGE);
						// click();

						//Updated Soundtrack Menu - Maybe more readable?
						JOptionPane.showMessageDialog (null, "Music originates from a variety of sources.\nThey are not owned by the developer." +
															"\n---------------\n" +
															"Main Menu: Valkyria Chronicles - Main Theme" +
															"Briefing: Valkyria Chronicles 2 - Briefing" +
															"Battle Map Chapter 0 & 1: Fire Emblem: Three Houses - Fodlan Winds (Rain)" +
															"Battle Map Chapter 2 - 5: Valkyria Chronicles - Europa at War" +
															"Battle Chapter 0 & 1: Fire Emblem: Three Houses - Fodlan Winds (Thunder)" +
															"Battle Chapter 2 & 3: Fate Grand Order (First Order) - Scalavera" +
															"Battle Chapter 4 & 5: Fate Grand Order (First Order) - The Shield of Chaldeas" +
															"Get Item: Fire Emblem Fates" +
															"Level Up: Fire Emblem Fates" +
															"Defeat: Fate/Zero - Grief\n" + 
															"Victory: Overwatch" +
															"After Map: Valkyria Chronicles - The Gallian War" +
															"Preparations: Fate Grand Order (First Order) - KYRIELIGHT" +
															"Final Boss: Fire Emblem: Three Houses - Indomitable Will (Rain)" + 
															"Ending: Fate Grand Order (First Order) - Eaving Time" + 
															"Click (Not in Battle) - https://mixkit.co/free-sound-effects/click/ - Select Click" +
															"Click (In Battle) - https://www.fesliyanstudios.com/royalty-free-sound-effects-download/video-game-menu-153 - Menu Button Press B Sound Effect",
															 "Music Credits", JOptionPane.INFORMATION_MESSAGE);
						click();
						
						JOptionPane.showMessageDialog (null, "You may continue playing the game if you wish.\nThanks again for playing!", "Post Game", JOptionPane.INFORMATION_MESSAGE);
						click();
					}			
					
					//Ensures that this is the user's first time completing this level.
					if(map.getStagesCompleted ( ) <= 5) 
					{
						map.setStagesCompleted (6);
					}
				}
			}
			else 
			{
				return; //returns to stage select.
			}
		}
		else if(chapterNum == 7)
		{
			String input;
			int battleFieldLength = 1;
			int battleFieldWidth = 1;
			int numEnemies = 1;
			int enemySparseness = 1;
			int itemSpawnRate = 1;

			boolean stringValid = false;

			JOptionPane.showMessageDialog(null, "Welcome to Skirmish Mode!" +
			"\nHere, you can customize your... " + 
			"\n - Preferred battlefield size (up to 25x25 inclusive)" + 
			"\n - Number of Enemies on the Battlefield" + 
			"\n - Sparseness of Enemies on the Battlefield" + 
			"\n - Item Spawn Rate", "Skirmish Mode Customization", JOptionPane.INFORMATION_MESSAGE);
			
			while(!stringValid) //Length
			{
				input = JOptionPane.showInputDialog(null, "To Begin, Please Input the Length of the Battlefield. \n(Please type a number between 2 - 25 inclusive.");
				try
				{
					if(input == null)
					{
						stringValid = true;
						return;
					}
					battleFieldLength = Integer.parseInt(input);
					if(battleFieldLength >= 2 && battleFieldLength <= 25)
					{
						stringValid = true;
					}
					else 
					{
						JOptionPane.showMessageDialog(null, "Oops! This number isn't between 2 - 25! \nPlease try again.");
					}
				} 
				catch (Exception ex)
				{

					JOptionPane.showMessageDialog(null, "Sorry, but this input is invalid.\nYou Typed: \"" + input + "\"", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
			}
			stringValid = false;
			input = "";
			

			while(!stringValid) //Width
			{
				input = JOptionPane.showInputDialog(null, "Please Input the Width of the Battlefield. \n(Please type a number between 2 - 25 inclusive.");
				try
				{
					if(input == null)
					{
						stringValid = true;
						return;
					}
					battleFieldWidth = Integer.parseInt(input);
					if(battleFieldWidth >= 2 && battleFieldWidth <= 25)
					{
						stringValid = true;
					}
					else 
					{
						JOptionPane.showMessageDialog(null, "Oops! This number isn't between 2 - 25! \nPlease try again.", "Invalid Number", JOptionPane.OK_OPTION);
					}
				} 
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null, "Sorry, but this input is invalid.\nYou Typed: \"" + input + "\"", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
			}
			stringValid = false;
			input = "";
			

			while(!stringValid) //Number of Enemies
			{
				input = JOptionPane.showInputDialog(null, "Please Input the Number of Enemies to spawn on the Battlefield. \n(Please type a number between 1 - 20 inclusive.");
				try
				{
					if(input == null)
					{
						stringValid = true;
						return;
					}
					numEnemies = Integer.parseInt(input);
					if(numEnemies >= 1 && numEnemies <= 20)
					{
						stringValid = true;
					}
					else 
					{
						JOptionPane.showMessageDialog(null, "Oops! This number isn't between 1 - 20! \nPlease try again.", "Invalid Number", JOptionPane.OK_OPTION);
					}
				} 
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null, "Sorry, but this input is invalid.\nYou Typed: \"" + input + "\"", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
			}
			stringValid = false;
			input = "";
			

			while(!stringValid) //Sparseness of Enemies
			{
				input = JOptionPane.showInputDialog(null, "Please Input the Sparseness of Enemies on the Battlefield. \nIn other words, how far apart enemies should be (on average) from each other (Default: 10).\n(Please type a number between 10 - 99 inclusive.");
				try
				{
					if(input == null)
					{
						stringValid = true;
						return;
					}
					enemySparseness = Integer.parseInt(input);
					if(enemySparseness >= 10 && enemySparseness <= 99)
					{
						stringValid = true;
					}
					else 
					{
						JOptionPane.showMessageDialog(null, "Oops! This number isn't between 10 - 99! \nPlease try again.", "Invalid Number", JOptionPane.OK_OPTION);
					}
				} 
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null, "Sorry, but this input is invalid.\nYou Typed: \"" + input + "\"", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
			}
			stringValid = false;
			input = "";
			

			while(!stringValid) //Item Spawn Rate
			{
				input = JOptionPane.showInputDialog(null, "Please Input the Chance of Items spawning on the Battlefield. \nThe default value is 20.\n(Please type a number between 1 - 99 inclusive.");
				try
				{
					if(input == null)
					{
						stringValid = true;
						return;
					}
					itemSpawnRate = Integer.parseInt(input);
					if(itemSpawnRate >= 1 && itemSpawnRate <= 99)
					{
						stringValid = true;
					}
					else 
					{
						JOptionPane.showMessageDialog(null, "Oops! This number isn't between 1 - 99! \nPlease try again.", "Invalid Number", JOptionPane.OK_OPTION);
					}
				} 
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null, "Sorry, but this input is invalid.\nYou Typed: \"" + input + "\"", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
			}
			stringValid = false;
			input = "";

			//Prepare the user for battle. If they leave preparations, returns to stage select.
			if(preparations()) 
			{
				//Creates a 10X10 map with 7 enemies. This is Chapter 5.
				boolean wonBattle = mapNavigation(battleFieldLength, battleFieldWidth, numEnemies, enemySparseness, itemSpawnRate);
				
				//wonBattle is true if the player defeated all enemies in the map. Otherwise, returns false.
				if(wonBattle) 
				{
					stopMusic();
					playMusic("AfterMap", true);
					String[] options = {"OK"};
					JOptionPane.showOptionDialog (null, "Congratulations! You completed the Skirmish!", "Skirmish Complete", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options, options[0]);
					foughtSkirmishOnce = true;
					click();
				}
			}
			else 
			{
				return; //returns to stage select.
			}
		}
		//Restore HP and SP, save game, and go to stage select.
		heroAttributes.setHP (heroAttributes.getMaxHP ( ));
		heroAttributes.setSP (heroAttributes.getMaxSP ( ));
		click();
		saveGame();
		stageSelect();
	}
	

	
	/**
	 * Allows the user to navigate the 2D Array map.
	 * If the hero escaped, returns false. Otherwise, returns true.
	 *
	 * <hr>
	 * Date created: Apr 2, 2021
	 * Last Modified: May 29, 2022
	 * <hr>
	 * @param length
	 * @param width
	 * @param enemies
	 * @param chapter
	 * @return boolean
	 */
	private static boolean mapNavigation(int length, int width, int enemies, int chapter) 
	{
		stopMusic();
		if(chapter == 0 || chapter == 1 || chapter == 7) 
		{
			playMusic("FodlanWindsR", true);
		}
		else 
		{
			playMusic("Ambience", true);
		}
		
		
		String temp = "";
		map.initiateBattleField(length, width, enemies);
		//Heal 50% of HP.
		//int medkits = 1;
		//items[0] will represent MedKits.
		items[0] = 1;
		
		//Heal 50% of SP.
		//int sodas = 1;
		//items[1] will represent Sodas.
		items[1] = 1;
		
		//If the player has escaped from the map.
		boolean escape = false;
		//While there are still enemies and the player did not escape.
		while(enemies != 0 && !escape) 
		{
			String[] choices = {"Move Left", "Move Up", "Move Right", "Move Down", "Items", "Stats", "Escape"};
			//Allow the user to pick an option in the array directly above.
			int pick = JOptionPane.showOptionDialog (null, "HP: " + heroAttributes.getHP() + "HP / " + heroAttributes.getMaxHP ( ) + "HP" +
				"\nSP: " + heroAttributes.getSP ( ) + "SP / " + heroAttributes.getMaxSP() + "SP\n" +
				"Enemies Remaining: " + enemies + "\n\n" +
				map.getBattleFieldString ( ), "Battlefield", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, choices[1]);
			click();
			
			if(pick == 0) //Move Left
			{
				temp = map.movePlayer ("WEST");
			}
			else if(pick == 1) //Move Up
			{
				temp = map.movePlayer ("NORTH");
			}
			else if(pick == 2) //Move Right
			{
				temp = map.movePlayer ("EAST");
			}
			else if(pick == 3) //Move Down
			{
				temp = map.movePlayer ("SOUTH");
			}
			else if(pick == 4) //Items
			{
				String[] picks = {"Medkit (" + items[0] + ")", "Soda (" + items[1] + ")"};
				int itemUse = JOptionPane.showOptionDialog (null, "Use which item?" +
								"\nHP: " + heroAttributes.getHP() + "HP / " + heroAttributes.getMaxHP ( ) + "HP" +
								"\nSP: " + heroAttributes.getSP ( ) + "SP / " + heroAttributes.getMaxSP ( ) + "SP", "Items", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, picks, picks[0]);
				if(itemUse == 0) //Medkit
				{
					if(items[0] >= 1) 
					{
						pick = JOptionPane.showConfirmDialog (null, "Heal for 50% of your HP?\n\nMedkits: " + items[0] + "\nCurrent HP:   ( " + heroAttributes.getHP ( ) + "HP / " + heroAttributes.getMaxHP() + "HP )", "Use Medkit", JOptionPane.YES_NO_OPTION);
						if(pick == 0) //yes 
						{
							click();
							//The heroAttribute's setHP method doesn't allow HP to go above maxHP. Be sure when using.
							heroAttributes.setHP (heroAttributes.getHP() + (int)(heroAttributes.getMaxHP ( ) / 2));
							JOptionPane.showMessageDialog (null, "The Hero has been healed.\nHero HP:    " + heroAttributes.getHP() + "HP / " + heroAttributes.getMaxHP ( ) + "HP");
							items[0]--;
						}
					}
					else //If the user doesn't have any medkits
					{
						JOptionPane.showMessageDialog (null, "You have no medkits to use.", "No Medkits", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else if(itemUse == 1) //Soda
				{
					if(items[1] >= 1) 
					{
						pick = JOptionPane.showConfirmDialog (null, "Renew 50% of your SP?\nSodas: " + items[1] + "\nCurrent SP:   ( " + heroAttributes.getSP ( ) + "SP / " + heroAttributes.getMaxSP() + "SP )", "Use Soda", JOptionPane.YES_NO_OPTION);
						if(pick == 0) //yes 
						{
							click();
							//The heroAttribute's setSP method doesn't allow HP to go above maxSP. Be sure when using.
							heroAttributes.setSP (heroAttributes.getSP() + (int)(heroAttributes.getMaxSP ( ) / 2));
							JOptionPane.showMessageDialog (null, "The Hero's SP has been restored.\nHero SP:    " + heroAttributes.getSP() + "SP / " + heroAttributes.getMaxSP ( ) + "SP");
							items[1]--;
						}
					}
					else //If the user doesn't have any sodas.
					{
						JOptionPane.showMessageDialog (null, "You have no medkits to use.", "No Medkits", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				
				pick = 4;
			}
			else if(pick == 5) //Stats
			{
				//Returns a description of the hero's attributes.
				JOptionPane.showMessageDialog(null, heroAttributes.toString ( ), heroAttributes.getName(), JOptionPane.INFORMATION_MESSAGE);
			}
			else if(pick == 6) //Escape
			{
				//Asks if the user want to escape the battlefield.
				pick = JOptionPane.showConfirmDialog (null, "Are you sure you want to escape?\nAll progress in this chapter will be lost!", "Escape", JOptionPane.YES_NO_OPTION);
				if(pick == 0)//yes
				{
					escape = true;
				}
				pick = 5;
			}
			else 
			{
				//Do nothing. This happens when 'X' is pressed.
			}
			
			//If the player escaped, skips the following statements.
			if(!escape) 
			{
				//Without the if statement,
				//You can get infinite items after healing on an item tile!
				if(pick != 4 && pick != 5) 
				{
					if(temp.equals("Enemy")) 
					{
						//pick is reused here. Ask if the user wants to confront the enemy.
						pick = JOptionPane.showConfirmDialog (null, "It appears that there is an enemy in the room. \nEnter?", "Enter Room", JOptionPane.YES_NO_OPTION);
						if(pick == 0) //yes 
						{
							click();
							JOptionPane.showMessageDialog (null, "You confronted the enemy!", "Confrontation", JOptionPane.INFORMATION_MESSAGE);
							click();
							
							//If this is the last enemy of Chapter 5...
							if(chapter == 5 && enemies == 1) 
							{
								//Chapter 6 is hypothetical. Rather, it tells the Battle class that this is the last chapter and the last enemy.
								//The battle class is altered to take Chapter 6 as the final boss.
								chapter = 6;
							}
							
							//Fun Fact: Without this if-statement, it is possible to glitch through a wall after retreating from battle. 
							//tutorial battle returns true when the battle is won. Otherwise, return false.
							if(battle(chapter)) //This method initiates a battle and moves the player based on the outcome.
							{
								map.commitMove(true);
								enemies--;
							}
							else 
							{
								map.commitMove (false);
							}
							click();
							
							//Restart the music. If Chapter is 0 or 1 (or 7), switches the music of FodlanWinds from Thunder to Rain.
							//Note: This music switch is (almost) the same as it is in Fire Emblem: Three Houses.
							if(chapter == 0 || chapter == 1 || chapter == 7) 
							{
								swapMusic("FodlanWindsR", "FodlanWindsT");
							}
							else //plays Ambience.wav
							{
								playMusic("Ambience", true);
							}
						}
						else //No is picked.
						{
							map.commitMove (false);
						}
					}
					else if(temp.equals ("Item")) 
					{
						JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " finds useful items as they are looking around the room.\nThese will prove useful later on!");
						click();
						
						Random rand = new Random();
						int item = rand.nextInt(2); //Gets 0 or 1.
						//Can find 1-2 medkits or sodas.
						int randNum = rand.nextInt(2)+1;
						
						try //Play the get item sound effect.
						{
							swavePlayer = new WavePlayer(FileRoot + "src\\Music\\GetItem.wav", false);
						}
						catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
						{
							e.printStackTrace();
						}
						
						if(item == 0) //signifies medkit amount
						{
							items[0] += randNum;
							JOptionPane.showMessageDialog (null, "You found " + randNum + " medkits!\nYou now have " + items[0] + " Medkits.");
						}
						else //add to sodas.
						{
							items[1] += randNum;
							JOptionPane.showMessageDialog (null, "You found " + randNum + " Sodas!\nYou now have " + items[1] + " Sodas.");
						}
						map.commitMove(true);
					}
					else if(temp.equals ("None")) // if the room has nothing in it.
					{
						map.commitMove(true); //Moves the player and continues.
					}
				}
			}
			click();
			
		} // All enemies in map defeated or the player escaped.
		
		//if the hero did not escape.
		if(!escape) 
		{
			try
			{
				stopMusic ( );
				swavePlayer = new WavePlayer(FileRoot + "src\\Music\\Victory.wav", false);
			}
			catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1)
			{
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog (null, "Congratulations! You have cleared the chapter!", "Chapter Cleared!", JOptionPane.INFORMATION_MESSAGE);
		}
		stopMusic();
		//if the hero escaped, return false. Otherwise, return true.
		return !escape;
	}

	/**
	 * Allows the user to navigate the 2D Array map.
	 * If the hero escaped, returns false. Otherwise, returns true.
	 * This method is overridden from the one above.
	 * 
	 *
	 * <hr>
	 * Date created: Apr 2, 2021
	 * Last Modified: May 29, 2022
	 * <hr>
	 * @param length - The Length of the 2D Map.
	 * @param width - The Width of the 2D Map.
	 * @param enemies - The Number of Enemies to Spawn on the 2D Map.
	 * @param enemySparseness - The Sparseness of Enemies spawning on the 2D Map.
	 * @param itemSpawnRate - The Item Spawn Rate of Items on the 2D Map.
	 * @return boolean
	 */
	private static boolean mapNavigation(int length, int width, int enemies, int enemySparseness, int itemSpawnRate) 
	{
		//We might have a 2x2 map and the user put 25 enemies. At max, we can only support 3 enemies here.
		//For example, if we had 5 enemies on a 2x2 map, we set enemies to 2x2 - 1, which is 3.
		if(enemies >= (length * width))
		{
			enemies = (length * width) - 1;
		}

		stopMusic();
		playMusic("FodlanWindsR", true);

		//Because the only time we will be using this method is in a skirmish, 
		//we automatically know that chapter should be 7.
		int chapter = 7;
		
		System.out.println("BeforeInit");
		String temp = "";
		map.initiateBattleField(length, width, enemies, enemySparseness, itemSpawnRate);

		
		System.out.println("AfterInit");
		//Heal 50% of HP.
		//int medkits = 1;
		//items[0] will represent MedKits.
		items[0] = 1;
		
		//Heal 50% of SP.
		//int sodas = 1;
		//items[1] will represent Sodas.
		items[1] = 1;
		
		//If the player has escaped from the map.
		boolean escape = false;
		//While there are still enemies and the player did not escape.
		while(enemies != 0 && !escape) 
		{
			String[] choices = {"Move Left", "Move Up", "Move Right", "Move Down", "Items", "Stats", "Escape"};
			//Allow the user to pick an option in the array directly above.
			int pick = JOptionPane.showOptionDialog (null, "HP: " + heroAttributes.getHP() + "HP / " + heroAttributes.getMaxHP ( ) + "HP" +
				"\nSP: " + heroAttributes.getSP ( ) + "SP / " + heroAttributes.getMaxSP() + "SP\n" +
				"Enemies Remaining: " + enemies + "\n\n" +
				map.getBattleFieldString ( ), "Battlefield", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, choices[1]);
			click();
			
			if(pick == 0) //Move Left
			{
				temp = map.movePlayer ("WEST");
			}
			else if(pick == 1) //Move Up
			{
				temp = map.movePlayer ("NORTH");
			}
			else if(pick == 2) //Move Right
			{
				temp = map.movePlayer ("EAST");
			}
			else if(pick == 3) //Move Down
			{
				temp = map.movePlayer ("SOUTH");
			}
			else if(pick == 4) //Items
			{
				String[] picks = {"Medkit (" + items[0] + ")", "Soda (" + items[1] + ")"};
				int itemUse = JOptionPane.showOptionDialog (null, "Use which item?" +
								"\nHP: " + heroAttributes.getHP() + "HP / " + heroAttributes.getMaxHP ( ) + "HP" +
								"\nSP: " + heroAttributes.getSP ( ) + "SP / " + heroAttributes.getMaxSP ( ) + "SP", "Items", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, picks, picks[0]);
				if(itemUse == 0) //Medkit
				{
					if(items[0] >= 1) 
					{
						pick = JOptionPane.showConfirmDialog (null, "Heal for 50% of your HP?\n\nMedkits: " + items[0] + "\nCurrent HP:   ( " + heroAttributes.getHP ( ) + "HP / " + heroAttributes.getMaxHP() + "HP )", "Use Medkit", JOptionPane.YES_NO_OPTION);
						if(pick == 0) //yes 
						{
							click();
							//The heroAttribute's setHP method doesn't allow HP to go above maxHP. Be sure when using.
							heroAttributes.setHP (heroAttributes.getHP() + (int)(heroAttributes.getMaxHP ( ) / 2));
							JOptionPane.showMessageDialog (null, "The Hero has been healed.\nHero HP:    " + heroAttributes.getHP() + "HP / " + heroAttributes.getMaxHP ( ) + "HP");
							items[0]--;
						}
					}
					else //If the user doesn't have any medkits
					{
						JOptionPane.showMessageDialog (null, "You have no medkits to use.", "No Medkits", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else if(itemUse == 1) //Soda
				{
					if(items[1] >= 1) 
					{
						pick = JOptionPane.showConfirmDialog (null, "Renew 50% of your SP?\nSodas: " + items[1] + "\nCurrent SP:   ( " + heroAttributes.getSP ( ) + "SP / " + heroAttributes.getMaxSP() + "SP )", "Use Soda", JOptionPane.YES_NO_OPTION);
						if(pick == 0) //yes 
						{
							click();
							//The heroAttribute's setSP method doesn't allow HP to go above maxSP. Be sure when using.
							heroAttributes.setSP (heroAttributes.getSP() + (int)(heroAttributes.getMaxSP ( ) / 2));
							JOptionPane.showMessageDialog (null, "The Hero's SP has been restored.\nHero SP:    " + heroAttributes.getSP() + "SP / " + heroAttributes.getMaxSP ( ) + "SP");
							items[1]--;
						}
					}
					else //If the user doesn't have any sodas.
					{
						JOptionPane.showMessageDialog (null, "You have no medkits to use.", "No Medkits", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				
				pick = 4;
			}
			else if(pick == 5) //Stats
			{
				//Returns a description of the hero's attributes.
				JOptionPane.showMessageDialog(null, heroAttributes.toString ( ), heroAttributes.getName(), JOptionPane.INFORMATION_MESSAGE);
			}
			else if(pick == 6) //Escape
			{
				//Asks if the user want to escape the battlefield.
				pick = JOptionPane.showConfirmDialog (null, "Are you sure you want to escape?\nAll progress in this chapter will be lost!", "Escape", JOptionPane.YES_NO_OPTION);
				if(pick == 0)//yes
				{
					escape = true;
				}
				pick = 5;
			}
			else 
			{
				//Do nothing. This happens when 'X' is pressed.
			}
			
			//If the player escaped, skips the following statements.
			if(!escape) 
			{
				//Without the if statement,
				//You can get infinite items after healing on an item tile!
				if(pick != 4 && pick != 5) 
				{
					if(temp.equals("Enemy")) 
					{
						//pick is reused here. Ask if the user wants to confront the enemy.
						pick = JOptionPane.showConfirmDialog (null, "It appears that there is an enemy in the room. \nEnter?", "Enter Room", JOptionPane.YES_NO_OPTION);
						if(pick == 0) //yes 
						{
							click();
							JOptionPane.showMessageDialog (null, "You confronted the enemy!", "Confrontation", JOptionPane.INFORMATION_MESSAGE);
							click();
							
							//If this is the last enemy of The Skirmish...
							if(enemies == 1) 
							{
								//Chapter 8 is hypothetical. Rather, it tells the Battle class that this is the last chapter and the last enemy.
								//The battle class is altered to take Chapter 8 as the final boss.
								chapter = 8;
							}
							
							//Fun Fact: Without this if-statement, it is possible to glitch through a wall after retreating from battle. 
							//tutorial battle returns true when the battle is won. Otherwise, return false.
							if(battle(chapter)) //This method initiates a battle and moves the player based on the outcome.
							{
								map.commitMove(true);
								enemies--;
							}
							else 
							{
								map.commitMove (false);
							}
							click();
							
							//Restart the music. If Chapter is 0 or 1 (or 7), switches the music of FodlanWinds from Thunder to Rain.
							//Note: This music switch is (almost) the same as it is in Fire Emblem: Three Houses.
							if(chapter == 0 || chapter == 1 || chapter == 7) 
							{
								swapMusic("FodlanWindsR", "FodlanWindsT");
							}
							else //plays Ambience.wav
							{
								playMusic("Ambience", true);
							}
						}
						else //No is picked.
						{
							map.commitMove (false);
						}
					}
					else if(temp.equals ("Item")) 
					{
						JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " finds useful items as they are looking around the room.\nThese will prove useful later on!");
						click();
						
						Random rand = new Random();
						int item = rand.nextInt(2); //Gets 0 or 1.
						//Can find 1-2 medkits or sodas.
						int randNum = rand.nextInt(2)+1;
						
						try //Play the get item sound effect.
						{
							swavePlayer = new WavePlayer(FileRoot + "src\\Music\\GetItem.wav", false);
						}
						catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
						{
							e.printStackTrace();
						}
						
						if(item == 0) //signifies medkit amount
						{
							items[0] += randNum;
							JOptionPane.showMessageDialog (null, "You found " + randNum + " medkits!\nYou now have " + items[0] + " Medkits.");
						}
						else //add to sodas.
						{
							items[1] += randNum;
							JOptionPane.showMessageDialog (null, "You found " + randNum + " Sodas!\nYou now have " + items[1] + " Sodas.");
						}
						map.commitMove(true);
					}
					else if(temp.equals ("None")) // if the room has nothing in it.
					{
						map.commitMove(true); //Moves the player and continues.
					}
				}
			}
			click();
			
		} // All enemies in map defeated or the player escaped.
		
		//if the hero did not escape.
		if(!escape) 
		{
			try
			{
				stopMusic ( );
				swavePlayer = new WavePlayer(FileRoot + "src\\Music\\Victory.wav", false);
			}
			catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1)
			{
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog (null, "Congratulations! You have cleared the chapter!", "Chapter Cleared!", JOptionPane.INFORMATION_MESSAGE);
		}
		stopMusic();
		//if the hero escaped, return false. Otherwise, return true.
		return !escape;
	}

	
	/**
	 * The battle method initiates a battle.
	 * Return true if won. Otherwise, return false.
	 *
	 * <hr>
	 * Date created: Apr 2, 2021
	 * Last Modified: May 29, 2022
	 * <hr>
	 * @param chapter
	 * @return boolean
	 */
	private static boolean battle(int chapter) 
	{
		//These variables will keep track of what the player does in battle.
		//Keeps the battle running while true.
		inBattle = true;
		//If the player runs from battle, becomes true.
		boolean retreated = false;
		
		Random rand = new Random();
		
		//Enemy levels may range from 3 different levels. The beginning number is based on the chapter number.
		//For example, chapter 1 can have enemies of level: 0, 1, and 2. Chapter 2 has enemies of level 2, 3, and 4. Chapter 4 has enemies of level 6, 7, and 8.
		int enemyLevel = 0;
		
		//If the chapter is not the tutorial. (Tutorial is Chapter 0)
		if(chapter != 0) //Sets enemyLevel according to chapter.
		{
			enemyLevel = rand.nextInt(3) + ((chapter-1)*2);
			//Chapter 6 signifies the final boss.
			if(chapter == 6) 
			{
				enemyLevel = 11;
				//Some dialog before the final battle. By the way, this guy is quite overpowered if you don't have the metal armor equipped.
				JOptionPane.showMessageDialog (null, "A strongly-equipped enemy stands before you.", "Before Final Battle", JOptionPane.INFORMATION_MESSAGE);
				JOptionPane.showMessageDialog (null, "\"Your time is about to come to an end, young ranger,\" he says. \nHe pulls out a large weapon.", "Before Final Battle", JOptionPane.INFORMATION_MESSAGE);
				JOptionPane.showMessageDialog (null, "\"As the Leader of this rebellion, I will destroy you!\" he says. \n\"Prepare for the fight of your life.\"", "Before Final Battle", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(chapter == 7)
			{
				//Creates a random enemy from level 0 up to 10 inclusive.
				enemyLevel = rand.nextInt(11);
			}
			//chapter 8 signifies a boss of a skirmish.
			else if(chapter == 8)
			{
				enemyLevel = 12;
				//Some dialog before the final battle. By the way, this guy is quite overpowered if you don't have the metal armor equipped.
				if(!foughtSkirmishOnce)
				{
					JOptionPane.showMessageDialog (null, "A strongly-equipped enemy stands before you.", "Before the Last Skirmish Battle", JOptionPane.INFORMATION_MESSAGE);
					JOptionPane.showMessageDialog (null, "\"You there.\" He asks. \"You must be the " + heroAttributes.name + " that I've heard so much about.\"", "Before the Last Skirmish Battle", JOptionPane.INFORMATION_MESSAGE);
					JOptionPane.showMessageDialog (null, "\"Tell me... What is it that you fight for?\"\n\"The government? Your family? For honor and glory?\"", "Before the Last Skirmish Battle", JOptionPane.INFORMATION_MESSAGE);
					JOptionPane.showMessageDialog (null, "Before you can react, he shakes his head. \n\"Nevermind that. Let this battle decide the victor of this skirmish.\"", "Before the Last Skirmish Battle", JOptionPane.INFORMATION_MESSAGE);
					JOptionPane.showMessageDialog (null, "\"I hope you are ready. I won't hold anything back.\"", "Before the Last Skirmish Battle", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		Enemy enemy = new Enemy(enemyLevel);
		
		//THe music for the battle. This is dependent on chapter.
		if(chapter == 0 || chapter == 1) 
		{
			//If Chapter is 0 or 1, switches the music of FodlanWinds from Rain to Thunder.
			//Note: This music switch is (almost) the same as it is in Fire Emblem: Three Houses.
			swapMusic("FodlanWindsR", "FodlanWindsT");
		}
		else if(chapter == 2 || chapter == 3) 
		{
			//Plays Battle.wav from the Music folder if Chapter is 2 or 3.
			stopMusic();
			playMusic("Battle", true);
		}
		else if (chapter == 4 || chapter == 5)
		{
			//Plays Battle2.wav from the Music folder if Chapter is 4 or 5.
			stopMusic();
			playMusic("Battle2", true);
		}
		else if (chapter == 6)
		{
			//Plays FinalBoss.wav from the Music folder if this is the last enemy of chapter 5.
			stopMusic();
			playMusic("FinalBoss", true);
		}
		else if (chapter == 7)
		{
			swapMusic("FodlanWindsR", "FodlanWindsT");
		}
		else if (chapter == 8)
		{
			//Plays FinalBoss.wav from the Music folder if this is the last enemy of chapter 5.
			stopMusic();
			playMusic("FinalBoss", true);
		}
		
		
		//These temporary variables are instrumental in reducing large method chains.
		int clipSizePrimary = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getClipSize ( );
		int clipSizeSecondary = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getClipSize ( );
		int ammoPrimary = clipSizePrimary;
		int ammoSecondary = clipSizeSecondary;
		int enemyAmmo = enemy.getClipSize();
		int enemyMaxHP = enemy.getHP ( );
		
		boolean moved = false;
		while(inBattle) 
		{
			String[] actions = {"Attack", "Special Attack", "Examine", "Use Item", "Retreat"};
			click();
			//Player Phase.
			while(!moved) //While the player has not moved.
			{
				//Shows an option dialog with a breif display of the HP/SP of the hero and the HP of the enemy.
				int pick = JOptionPane.showOptionDialog (null, "Choose an Option." +
								"\nYour HP:      " + heroAttributes.getHP ( ) + "HP / " + heroAttributes.getMaxHP() + "HP " + 
								"\nYour SP:      " + heroAttributes.getSP ( ) + "SP / " + heroAttributes.getMaxSP ( ) + "SP\n" +
								"\nEnemy HP:   " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP", "Battle", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, actions, actions[0]);
				click();
				
				if(pick == 0) //User picks "Attack". Costs a turn.
				{
					String[] weaponChoice = {heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getWeaponName() + " (" + ammoPrimary + "/" + clipSizePrimary + ")", 
											heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getWeaponName ( ) + " (" + ammoSecondary + "/" + clipSizeSecondary + ")"};
					//Asks the user to attack with which weapon.
					pick = JOptionPane.showOptionDialog (null, "Attack with which weapon?", "Attack Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, weaponChoice, weaponChoice[0]);

					click();
					if(pick == 0) //User chooses Primary Weapon.
					{
						String weaponName = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getWeaponName ( );
						if(ammoPrimary > 0) //if primary weapon has ammo left.
						{
							//These temporary variables are instrumental in reducing large method chains.
							int rps = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getRps ( );

							//if we have less ammo than our rps, we set our rps to the ammo we have in primary.
							if(ammoPrimary < rps)
							{
								rps = ammoPrimary;
							}

							int damage = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getTotalDamage ( );
							double enemyArmor = enemy.getEquipment ( ).getTotalArmor ( ) / 100.0;
							int luck = heroAttributes.getEquipment ( ).getTotalLuck ( );
							double accuracy = heroAttributes.getEquipment().getPrimaryWeapon ( ).getTotalAccuracy ( );
							//Every 5lbs decreases accuracy by .01.
							double weight = heroAttributes.getEquipment ( ).getWeight ( ) / 500.0;
							int damageDealt = 0;
							int shotsConnected = 0;
							int damageTotal = 0;
							int luckShots = 0;
							
							for(int i = 0; i < rps; i++) 
							{
								if(ammoPrimary > 0) 
								{
									if(rand.nextInt(100) < ((accuracy*100)-weight))
									{
										if(rand.nextInt(100) < luck) //The chance of a lucky hit is (luck / 100).
										{
											luckShots++;
											damageDealt += damage;
										}
										damageDealt += damage;
										shotsConnected++;
									}
									ammoPrimary--;
								}
							}
							damageTotal = damageDealt - (int)(damageDealt * enemyArmor);
							enemy.setHP (enemy.getHP() - damageTotal);
							//Display the results of the Player Phase. Shows damage done, shots connected, HP damage & reduction, luckshots, and resulting enemy HP.
							JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
							+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
							"\nThe Enemy's Armor reduced the damage received by " + (int)(damageDealt * enemyArmor) + "HP!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
											"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
							moved = true;
						}
						else //if the primary weapon has no ammo left.
						{
							moved = true;
							JOptionPane.showMessageDialog (null, "The " + weaponName + " is out of ammo!\nIt was reloaded at the cost of this turn.", "Reloaded", JOptionPane.INFORMATION_MESSAGE);
							ammoPrimary = clipSizePrimary;
						}
						
					}
					else if(pick == 1) //User chooses Secondary Weapon.
					{
						String weaponName = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getWeaponName ( );
						if(ammoSecondary > 0) //if secondary weapon has ammo left.
						{
							//These temporary variables are instrumental in reducing large method chains.
							int rps = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getRps ( );

							//if we have less ammo than our rps, we set our rps to the ammo we have in primary.
							if(ammoSecondary < rps)
							{
								rps = ammoSecondary;
							}

							int damage = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getTotalDamage ( );
							double enemyArmor = enemy.getEquipment ( ).getTotalArmor ( ) / 100.0;
							int luck = heroAttributes.getEquipment ( ).getTotalLuck ( );
							double accuracy = heroAttributes.getEquipment().getSecondaryWeapon ( ).getTotalAccuracy ( );
							//Every 5lbs decreases accuracy by .01.
							double weight = heroAttributes.getEquipment ( ).getWeight ( ) / 500.0;
							int damageDealt = 0;
							int shotsConnected = 0;
							int luckShots = 0;
							int damageTotal = 0;
							
							for(int i = 0; i < rps; i++) 
							{
								if(ammoSecondary > 0) 
								{
									if(rand.nextInt(100) < ((accuracy*100)-weight) && ammoSecondary > 0) 
									{
										if(rand.nextInt(100) < luck) //The chance of a lucky hit is (luck / 100).
										{
											damageDealt += damage;
											luckShots++;
										}
										damageDealt += damage;
										shotsConnected++;
									}
									ammoSecondary--;
								}
							}
							damageTotal = damageDealt - (int)(damageDealt * enemyArmor);
							enemy.setHP (enemy.getHP() - damageTotal);
							//Display the results of the Player Phase. Shows damage done, shots connected, HP damage & reduction, luckshots, and resulting enemy HP.
							JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
							+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
							"\nThe Enemy's Armor reduced the damage received by " + (int)(damageDealt * enemyArmor) + "HP!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
											"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
							moved = true;
						}
						else //if the secondary weapon has no ammo left.
						{
							moved = true;
							JOptionPane.showMessageDialog (null, "The " + weaponName + " is out of ammo!\nIt was reloaded at the cost of this turn.", "Reloaded", JOptionPane.INFORMATION_MESSAGE);
							ammoSecondary = clipSizeSecondary;
						}
						click();
					}
					else //If 'X' is pressed.
					{
						//Do nothing. Return to the main screen.
					}
					pick = 0;
				}
				else if(pick == 1) //User picks Special Attack. Costs a turn.
				{
					//The Special Skills array will differ based on the hero's level.
					String[] specialSkills = new String[heroAttributes.getSpecialSkills().size()];
					
					if(specialSkills.length != 0) //if the specialSkills arraylist is not empty.
					{
						for(int i = 0; i < specialSkills.length; i++) 
						{
							specialSkills[i] = heroAttributes.getSpecialSkills ( ).get (i) + " (" + ((i+1)*5) + "SP)";
						}

						
						int specialSkill = JOptionPane.showOptionDialog (null, "Which special skill should the hero use?" +
										"\nYour SP: " + heroAttributes.getSP ( ) + "SP / " + heroAttributes.getMaxSP ( ) + "SP", "Skill to Use", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, specialSkills, specialSkills[0]);
						
						
						//Note: Special Skills are unaffected by weight.
						if(specialSkill == 0) //User picks "Sharpshooter" (100% Accuracy. Costs 5SP)
						{
							if(heroAttributes.getSP() >= 5) 
							{
								String[] weaponChoice = {heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getWeaponName() + " (" + ammoPrimary + "/" + clipSizePrimary + ")", 
												heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getWeaponName ( ) + " (" + ammoSecondary + "/" + clipSizeSecondary + ")"};
								pick = JOptionPane.showOptionDialog (null, "Attack with which weapon?", "Attack Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, weaponChoice, weaponChoice[0]);
			
								click();
								if(pick == 0) //Casts the Sharpshooter skill using the Primary Weapon.
								{
									String weaponName = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getWeaponName ( );
									if(ammoPrimary > 0) //if primary weapon has ammo left.
									{
										//These temporary variables are instrumental in reducing large method chains.
										int rps = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getRps ( );

										//if we have less ammo than our rps, we set our rps to the ammo we have in primary.
										if(ammoPrimary < rps)
										{
											rps = ammoPrimary;
										}
										
										int damage = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getTotalDamage ( );
										int luck = heroAttributes.getEquipment ( ).getTotalLuck ( );
										int damageDealt = 0;
										int shotsConnected = 0;
										int luckShots = 0;
										int damageTotal = 0;
										double enemyArmor;
										if(enemy.getEquipment ( ).getTotalArmor ( ) <= 30)
										{
											//If the enemy has 30 or less armor, we set it to 0.
											enemyArmor = 0.0;
										}
										else 
										{
											enemyArmor = (enemy.getEquipment ( ).getTotalArmor ( ) / 100.0) - 0.30;
										}
										
										for(int i = 0; i < rps; i++) 
										{
											if(ammoPrimary > 0) //unlike regular attack, removes accuracy.
											{
												if(rand.nextInt(100) < luck) 
												{
													//Score a lucky shot. This doubles the damage of the bullet by adding damage twice.
													//Damage is added immediately below and immediately after this if statement.
													damageDealt += damage;
													luckShots++;
												}
												damageDealt += damage;
												shotsConnected++;
												ammoPrimary--;
											}
										}
										damageTotal = damageDealt - (int)(damageDealt * enemyArmor);
										enemy.setHP (enemy.getHP() - damageTotal);
										//Display the results of the Player Phase. Shows damage done, shots connected, HP damage & reduction, luckshots, and resulting enemy HP.
										if(shotsConnected > 1 && enemyArmor > 0.0)
										{
											JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
											+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
											"\nThe Enemy's Armor withstood the barrage! " + (int)(damageDealt * enemyArmor) + "HP of damage was reduced!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
															"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										}
										else if(shotsConnected > 1)
										{
											JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
											+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
											"\nThe Enemy's Armor couldn't block the barrage! 0HP of damage was reduced!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
															"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										}
										else 
										{
											JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
											+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
											"\nThe Enemy's Armor couldn't block the shot! 0HP of damage was reduced!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
															"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										}

										heroAttributes.setSP (heroAttributes.getSP ( ) - 5);
										moved = true;
									}
									else //if the primary weapon has no ammo left.
									{
										moved = true;
										JOptionPane.showMessageDialog (null, "The " + weaponName + " is out of ammo!\nIt was reloaded at the cost of this turn.", "Reloaded", JOptionPane.INFORMATION_MESSAGE);
										ammoPrimary = clipSizePrimary;
									}
								}
								else if(pick == 1) //Casts the Sharpshooter skill using the Secondary Weapon.
								{
									String weaponName = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getWeaponName ( );
									if(ammoSecondary > 0) //if secondary weapon has ammo left.
									{									
										//These temporary variables are instrumental in reducing large method chains.
										int rps = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getRps ( );

										//if we have less ammo than our rps, we set our rps to the ammo we have in primary.
										if(ammoSecondary < rps)
										{
											rps = ammoSecondary;
										}
										
										int damage = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getTotalDamage ( );
										int luck = heroAttributes.getEquipment ( ).getTotalLuck ( );
										int damageDealt = 0;
										int shotsConnected = 0;
										int luckShots = 0;
										int damageTotal = 0;
										double enemyArmor;
										if(enemy.getEquipment ( ).getTotalArmor ( ) <= 30)
										{
											//If the enemy has 30 or less armor, we set it to 0.
											enemyArmor = 0.0;
										}
										else 
										{
											enemyArmor = (enemy.getEquipment ( ).getTotalArmor ( ) / 100.0) - 0.30;
										}
										
										for(int i = 0; i < rps; i++) //removes accuracy.
										{
											if(ammoSecondary > 0) 
											{
												if(rand.nextInt(100) < luck) 
												{
													damageDealt += damage;
													luckShots++;
												}
												damageDealt += damage;
												shotsConnected++;
												ammoSecondary--;
											}
										}
										damageTotal = damageDealt - (int)(damageDealt * enemyArmor);
										enemy.setHP (enemy.getHP() - damageTotal);
										//Display the results of the Player Phase. Shows damage done, shots connected, HP damage & reduction, luckshots, and resulting enemy HP.
										if(shotsConnected > 1 && enemyArmor > 0.0)
										{
											JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
											+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
											"\nThe Enemy's Armor withstood the barrage! " + (int)(damageDealt * enemyArmor) + "HP of damage was reduced!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
															"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										}
										else if(shotsConnected > 1)
										{
											JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
											+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
											"\nThe Enemy's Armor couldn't block the barrage! 0HP of damage was reduced!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
															"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										}
										else 
										{
											JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
											+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
											"\nThe Enemy's Armor couldn't block the shot! 0HP of damage was reduced!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
															"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										}

										heroAttributes.setSP (heroAttributes.getSP ( ) - 5);
										moved = true;
									}
									else //if the secondary weapon has no ammo left.
									{
										moved = true;
										JOptionPane.showMessageDialog (null, "The " + weaponName + " is out of ammo!\nIt was reloaded at the cost of this turn.", "Reloaded", JOptionPane.INFORMATION_MESSAGE);
										ammoSecondary = clipSizeSecondary;
									}
									click();
								}
								else //If 'X' is pressed.
								{
									//Do nothing. Return to the main screen.
								}
							}
							else //SP is lower than 5.
							{
								JOptionPane.showMessageDialog (null, "Sorry, this skill requires 5 SP to use.\nYour SP: " + heroAttributes.getSP ( ) + "SP.");
							}
						}
						else if(specialSkill == 1) //User picks Critical Shot (65% increase in luck per shot. Costs 10SP).
						{
							if(heroAttributes.getSP() >= 10) 
							{
								String[] weaponChoice = {heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getWeaponName() + " (" + ammoPrimary + "/" + clipSizePrimary + ")", 
												heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getWeaponName ( ) + " (" + ammoSecondary + "/" + clipSizeSecondary + ")"};
								pick = JOptionPane.showOptionDialog (null, "Attack with which weapon?", "Attack Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, weaponChoice, weaponChoice[0]);
			
								click();
								if(pick == 0) //Casts Critical Shot using Primary Weapon. 
								{
									String weaponName = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getWeaponName ( );
									if(ammoPrimary > 0) //if primary weapon has ammo left.
									{
										//These temporary variables are instrumental in reducing large method chains.
										int rps = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getRps ( );

										//if we have less ammo than our rps, we set our rps to the ammo we have in primary.
										if(ammoPrimary < rps)
										{
											rps = ammoPrimary;
										}
										
										int damage = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getTotalDamage ( );
										int luck = heroAttributes.getEquipment ( ).getTotalLuck ( ) + 65;
										double accuracy = heroAttributes.getEquipment().getPrimaryWeapon ( ).getTotalAccuracy ( );
										int damageDealt = 0;
										int shotsConnected = 0;
										int luckShots = 0;
										int damageTotal = 0;
										double enemyArmor = enemy.getEquipment ( ).getTotalArmor ( ) / 100.0;
										
										for(int i = 0; i < rps; i++) 
										{
											if(ammoPrimary > 0) 
											{
												if(rand.nextInt(100) < (accuracy*100)) 
												{
													if(rand.nextInt(100) < luck) 
													{
														damageDealt += damage;
														luckShots++;
													}
													damageDealt += damage;
													shotsConnected++;
												}
												ammoPrimary--;
											}
										}
										damageTotal = damageDealt - (int)(damageDealt * enemyArmor);
										enemy.setHP (enemy.getHP() - damageTotal);
										//Display the results of the Player Phase. Shows damage done, shots connected, HP damage & reduction, luckshots, and resulting enemy HP.
										JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
														+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
														"\nThe Enemy's Armor reduced the damage received by " + (int)(damageDealt * enemyArmor) + "HP!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
																		"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										heroAttributes.setSP (heroAttributes.getSP ( ) - 10);
										moved = true;
									}
									else //if the primary weapon has no ammo left.
									{
										moved = true;
										JOptionPane.showMessageDialog (null, "The " + weaponName + " is out of ammo!\nIt was reloaded at the cost of this turn.", "Reloaded", JOptionPane.INFORMATION_MESSAGE);
										ammoPrimary = clipSizePrimary;
									}
									
								}
								else if(pick == 1) //Casts Critical Shot using Secondary Weapon.
								{
									String weaponName = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getWeaponName ( );
									if(ammoSecondary > 0) //if secondary weapon has ammo left.
									{
										//These temporary variables are instrumental in reducing large method chains.
										int rps = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getRps ( );

										//if we have less ammo than our rps, we set our rps to the ammo we have in primary.
										if(ammoSecondary < rps)
										{
											rps = ammoSecondary;
										}
										
										int damage = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getTotalDamage ( );
										int luck = heroAttributes.getEquipment ( ).getTotalLuck ( ) + 65;
										double accuracy = heroAttributes.getEquipment().getSecondaryWeapon ( ).getTotalAccuracy ( );
										int damageDealt = 0;
										int shotsConnected = 0;
										int luckShots = 0;
										int damageTotal = 0;
										double enemyArmor = enemy.getEquipment ( ).getTotalArmor ( ) / 100.0;
										
										for(int i = 0; i < rps; i++) 
										{
											if(ammoSecondary > 0) 
											{
												if(rand.nextInt(100) < (accuracy*100)) 
												{
													if(rand.nextInt(100) < luck) 
													{
														damageDealt += damage;
														luckShots++;
													}
													damageDealt += damage;
													shotsConnected++;
												}
												ammoSecondary--;
											}
										}
										damageTotal = damageDealt - (int)(damageDealt * enemyArmor);
										enemy.setHP (enemy.getHP() - damageTotal);
										//Display the results of the Player Phase. Shows damage done, shots connected, HP damage & reduction, luckshots, and resulting enemy HP.
										JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
														+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
														"\nThe Enemy's Armor reduced the damage received by " + (int)(damageDealt * enemyArmor) + "HP!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
																		"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										heroAttributes.setSP (heroAttributes.getSP ( ) - 10);
										moved = true;
									}
									else //if the secondary weapon has no ammo left.
									{
										moved = true;
										JOptionPane.showMessageDialog (null, "The " + weaponName + " is out of ammo!\nIt was reloaded at the cost of this turn.", "Reloaded", JOptionPane.INFORMATION_MESSAGE);
										ammoSecondary = clipSizeSecondary;
									}
									click();
								}
								else //If 'X' is pressed.
								{
									//Do nothing. Return to the main screen.
								}
							}
							else  //The player has less than 10 SP.
							{
								JOptionPane.showMessageDialog (null, "Sorry, this skill requires 10 SP to use.\nYour SP: " + heroAttributes.getSP ( ) + "SP.");
							}
							
						}
						else if(specialSkill == 2) //User picks Triple Clip (Triple Rounds Per Second. Costs 15SP).
						{
							if(heroAttributes.getSP() >= 15) 
							{
								String[] weaponChoice = {heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getWeaponName() + " (" + ammoPrimary + "/" + clipSizePrimary + ")", 
												heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getWeaponName ( ) + " (" + ammoSecondary + "/" + clipSizeSecondary + ")"};
								pick = JOptionPane.showOptionDialog (null, "Attack with which weapon?", "Attack Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, weaponChoice, weaponChoice[0]);
			
								click();
								if(pick == 0) //Cases Weak Point using Primary Weapon.
								{
									String weaponName = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getWeaponName ( );
									if(ammoPrimary > 0) //if primary weapon has ammo left.
									{
										//These temporary variables are instrumental in reducing large method chains.
										int rps = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getRps ( ); 

										//if we have less ammo than our rps, we set our rps to the ammo we have in primary.
										if(ammoPrimary < rps)
										{
											rps = ammoPrimary;
										}
										
										int damage = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getTotalDamage ( );
										int luck = heroAttributes.getEquipment ( ).getTotalLuck ( ) + 40;
										double accuracy = heroAttributes.getEquipment().getPrimaryWeapon ( ).getTotalAccuracy ( );
										int damageDealt = 0;
										int shotsConnected = 0;
										int luckShots = 0;
										int damageTotal = 0;
										double enemyArmor;
										if(enemy.getEquipment ( ).getTotalArmor ( ) <= 25)
										{
											//If the enemy has 30 or less armor, we set it to 0.
											enemyArmor = 0.0;
										}
										else 
										{
											enemyArmor = (enemy.getEquipment ( ).getTotalArmor ( ) / 100.0) - 0.25;
										}
										
										for(int i = 0; i < rps; i++) 
										{
											if(ammoPrimary > 0)
											{
												if(rand.nextInt(100) < (accuracy*100)) 
												{
													if(rand.nextInt(100) < luck) 
													{
														damageDealt += damage;
														luckShots++;
													}
													damageDealt += damage;
													shotsConnected++;
												}
												ammoPrimary--;	
											}
										}
										damageTotal = damageDealt - (int)(damageDealt * enemyArmor);
										enemy.setHP (enemy.getHP() - damageTotal);
										//Display the results of the Player Phase. Shows damage done, shots connected, HP damage & reduction, luckshots, and resulting enemy HP.
										if(shotsConnected > 1 && enemyArmor > 0.0)
										{
											JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
											+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
											"\nThe Enemy's Armor withstood the barrage! " + (int)(damageDealt * enemyArmor) + "HP of damage was reduced!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
															"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										}
										else if(shotsConnected > 1)
										{
											JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
											+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
											"\nThe Enemy's Armor couldn't block the barrage! 0HP of damage was reduced!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
															"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										}
										else 
										{
											JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
											+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
											"\nThe Enemy's Armor couldn't block the shot! 0HP of damage was reduced!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
															"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										}
										heroAttributes.setSP (heroAttributes.getSP ( ) - 15);
										moved = true;
									}
									else //if the primary weapon has no ammo left.
									{
										moved = true;
										JOptionPane.showMessageDialog (null, "The " + weaponName + " is out of ammo!\nIt was reloaded at the cost of this turn.", "Reloaded", JOptionPane.INFORMATION_MESSAGE);
										ammoPrimary = clipSizePrimary;
									}
									
								}
								else if(pick == 1) //Casts Weak Point using Secondary Weapon.
								{
									String weaponName = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getWeaponName ( );
									if(ammoSecondary > 0) //if secondary weapon has ammo left.
									{
										//These temporary variables are instrumental in reducing large method chains.
										int rps = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getRps ( );

										//if we have less ammo than our rps, we set our rps to the ammo we have in primary.
										if(ammoSecondary < rps)
										{
											rps = ammoSecondary;
										}
										
										int damage = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getTotalDamage ( );
										int luck = heroAttributes.getEquipment ( ).getTotalLuck ( ) + 40;
										double accuracy = heroAttributes.getEquipment().getSecondaryWeapon ( ).getTotalAccuracy ( );
										int damageDealt = 0;
										int shotsConnected = 0;
										int luckShots = 0;
										int damageTotal = 0;
										double enemyArmor;
										if(enemy.getEquipment ( ).getTotalArmor ( ) <= 25)
										{
											//If the enemy has 30 or less armor, we set it to 0.
											enemyArmor = 0.0;
										}
										else 
										{
											enemyArmor = (enemy.getEquipment ( ).getTotalArmor ( ) / 100.0) - 0.25;
										}
										
										for(int i = 0; i < rps; i++) 
										{
											if(ammoSecondary > 0) 
											{
												if(rand.nextInt(100) < (accuracy*100) && ammoSecondary > 0) 
												{
													if(rand.nextInt(100) < luck) 
													{
														damageDealt += damage;
														luckShots++;
													}
													damageDealt += damage;
													shotsConnected++;
												}
												ammoSecondary--;	
											}
										}
										damageTotal = damageDealt - (int)(damageDealt * enemyArmor);
										enemy.setHP (enemy.getHP() - damageTotal);
										//Display the results of the Player Phase. Shows damage done, shots connected, HP damage & reduction, luckshots, and resulting enemy HP.
										if(shotsConnected > 1 && enemyArmor > 0.0)
										{
											JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
											+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
											"\nThe Enemy's Armor withstood the barrage! " + (int)(damageDealt * enemyArmor) + "HP of damage was reduced!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
															"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										}
										else if(shotsConnected > 1)
										{
											JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
											+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
											"\nThe Enemy's Armor couldn't block the barrage! 0HP of damage was reduced!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
															"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										}
										else 
										{
											JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
											+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
											"\nThe Enemy's Armor couldn't block the shot! 0HP of damage was reduced!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
															"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										}
										heroAttributes.setSP (heroAttributes.getSP ( ) - 15);
										moved = true;
									}
									else //if the secondary weapon has no ammo left.
									{
										moved = true;
										JOptionPane.showMessageDialog (null, "The " + weaponName + " is out of ammo!\nIt was reloaded at the cost of this turn.", "Reloaded", JOptionPane.INFORMATION_MESSAGE);
										ammoSecondary = clipSizeSecondary;
									}
									click();
								}
								else //If 'X' is pressed.
								{
									//Do nothing. Return to the main screen.
								}
							}
							else //The player has less than 15 SP.
							{
								JOptionPane.showMessageDialog (null, "Sorry, this skill requires 15 SP to use.\nYour SP: " + heroAttributes.getSP ( ) + "SP.");
							}
						}
						else if(specialSkill == 3) //User picks Unload (Fire all rounds from weapon. Costs 20SP).
						{
							if(heroAttributes.getSP() >= 20) 
							{
								String[] weaponChoice = {heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getWeaponName() + " (" + ammoPrimary + "/" + clipSizePrimary + ")", 
												heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getWeaponName ( ) + " (" + ammoSecondary + "/" + clipSizeSecondary + ")"};
								pick = JOptionPane.showOptionDialog (null, "Attack with which weapon?", "Attack Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, weaponChoice, weaponChoice[0]);
			
								click();
								if(pick == 0) //Casts Unload using Primary Weapon.
								{
									String weaponName = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getWeaponName ( );
									if(ammoPrimary > 0) //if primary weapon has ammo left.
									{
										//These temporary variables are instrumental in reducing large method chains.
										int rps = ammoPrimary; //Instead of getting RPS, we get primary ammo, so we fire all rounds.
										int damage = heroAttributes.getEquipment ( ).getPrimaryWeapon ( ).getTotalDamage ( );
										int luck = heroAttributes.getEquipment ( ).getTotalLuck ( );
										double accuracy = heroAttributes.getEquipment().getPrimaryWeapon ( ).getTotalAccuracy ( );
										int damageDealt = 0;
										int shotsConnected = 0;
										int luckShots = 0;
										int damageTotal = 0;
										double enemyArmor = enemy.getEquipment ( ).getTotalArmor ( ) / 100.0;
										
										for(int i = 0; i < rps; i++) 
										{
											if(ammoPrimary > 0) 
											{
												if(rand.nextInt(100) < (accuracy*100)) 
												{
													if(rand.nextInt(100) < luck) 
													{
														damageDealt += damage;
														luckShots++;
													}
													damageDealt += damage;
													shotsConnected++;
												}
												ammoPrimary--;	
											}
										}
										damageTotal = damageDealt - (int)(damageDealt * enemyArmor);
										enemy.setHP (enemy.getHP() - damageTotal);
										//Display the results of the Player Phase. Shows damage done, shots connected, HP damage & reduction, luckshots, and resulting enemy HP.
										JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
														+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
														"\nThe Enemy's Armor reduced the damage received by " + (int)(damageDealt * enemyArmor) + "HP!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
																		"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										heroAttributes.setSP (heroAttributes.getSP ( ) - 20);
										moved = true;
									}
									else //if the primary weapon has no ammo left.
									{
										moved = true;
										JOptionPane.showMessageDialog (null, "The " + weaponName + " is out of ammo!\nIt was reloaded at the cost of this turn.", "Reloaded", JOptionPane.INFORMATION_MESSAGE);
										ammoPrimary = clipSizePrimary;
									}
									
								}
								else if(pick == 1) //Casts Unload using Secondary Weapon.
								{
									String weaponName = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getWeaponName ( );
									if(ammoSecondary > 0) //if secondary weapon has ammo left.
									{
										//These temporary variables are instrumental in reducing large method chains.
										int rps = ammoSecondary; //Instead of getting RPS, we get secondary ammo, so we fire all rounds.
										int damage = heroAttributes.getEquipment ( ).getSecondaryWeapon ( ).getTotalDamage ( );
										int luck = heroAttributes.getEquipment ( ).getTotalLuck ( );
										double accuracy = heroAttributes.getEquipment().getSecondaryWeapon ( ).getTotalAccuracy ( );
										int damageDealt = 0;
										int shotsConnected = 0;
										int luckShots = 0;
										int damageTotal = 0;
										double enemyArmor = enemy.getEquipment ( ).getTotalArmor ( ) / 100.0;
										
										for(int i = 0; i < rps; i++) 
										{
											if(ammoSecondary > 0) 
											{
												if(rand.nextInt(100) < (accuracy*100) && ammoSecondary > 0) 
												{
													if(rand.nextInt(100) < luck) 
													{
														damageDealt += damage;
														luckShots++;
													}
													damageDealt += damage;
													shotsConnected++;
												}
												ammoSecondary--;	
											}
										}
										damageTotal = damageDealt - (int)(damageDealt * enemyArmor);
										enemy.setHP (enemy.getHP() - damageTotal);
										//Display the results of the Player Phase. Shows damage done, shots connected, HP damage & reduction, luckshots, and resulting enemy HP.
										JOptionPane.showMessageDialog (null, heroAttributes.getName ( ) + " fires " + rps + " rounds at the " + enemy.getName ( ) + " using their " + weaponName + ".\n" 
														+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to the " + enemy.getName ( ) + " (" + damageDealt + "DMG in Total)." +
														"\nThe Enemy's Armor reduced the damage received by " + (int)(damageDealt * enemyArmor) + "HP!\n\n" + luckShots + " rounds resulted in Lucky Hits!" +
																		"\nEnemy HP is now " + enemy.getHP ( ) + "HP / " + enemyMaxHP + "HP.");
										heroAttributes.setSP (heroAttributes.getSP ( ) - 20);
										moved = true;
									}
									else //if the secondary weapon has no ammo left.
									{
										moved = true;
										JOptionPane.showMessageDialog (null, "The " + weaponName + " is out of ammo!\nIt was reloaded at the cost of this turn.", "Reloaded", JOptionPane.INFORMATION_MESSAGE);
										ammoSecondary = clipSizeSecondary;
									}
									click();
								}
								else //If 'X' is pressed.
								{
									//Do nothing. Return to the main screen.
								}
							}
							else //The player has less than 20 SP.
							{
								JOptionPane.showMessageDialog (null, "Sorry, this skill requires 20 SP to use.\nYour SP: " + heroAttributes.getSP ( ) + "SP.");
							}
						}
					}
					else //if the user does not own any special skills.
					{
						JOptionPane.showMessageDialog (null, "Sorry, you have no special skills at the moment.", "No Special Skills", JOptionPane.INFORMATION_MESSAGE);
					}
					
					pick = 0;
					click();
				}
				else if(pick == 2) //User picks Examine. This does not cost a turn.
				{
					JOptionPane.showMessageDialog (null, "Enemy Statistics" +
									"\n--------------------\n" + enemy.toString ( ), "Enemy Summary", JOptionPane.INFORMATION_MESSAGE);
					click();
					JOptionPane.showMessageDialog (null, enemy.getEquipmentInfo ("Weapon"), "Enemy Weapon", JOptionPane.INFORMATION_MESSAGE);
					click();
					JOptionPane.showMessageDialog (null, "Enemy Armor" +
									"\n--------------------\n" + enemy.getEquipmentInfo ("Armor"), "Enemy Armor", JOptionPane.INFORMATION_MESSAGE);
				}
				else if(pick == 3) //User picks Use Item. Costs a turn when HP or SP is restored.
				{
					String[] picks = {"Medkit (" + items[0] + ")", "Soda (" + items[1] + ")"};
					int itemUse = JOptionPane.showOptionDialog (null, "Use which item?" + 
																	"\nHP: " + heroAttributes.getHP() + "HP / " + heroAttributes.getMaxHP ( ) + "HP" + 
																	"\nSP: " + heroAttributes.getSP ( ) + "SP / " + heroAttributes.getMaxSP ( ) + "SP" +
																	"\nNote: Using either item will cost a turn.", "Items", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, picks, picks[0]);
					if(itemUse == 0) //Medkit
					{
						if(items[0] >= 1) 
						{
							pick = JOptionPane.showConfirmDialog (null, "Heal for 50% of your HP?\nMedkits: " + items[0] + "\n\nCurrent HP:   (" + heroAttributes.getHP ( ) + "HP / " + heroAttributes.getMaxHP() + "HP )", "Use Medkit", JOptionPane.YES_NO_OPTION);
							if(pick == 0) //yes 
							{
								click();
								//The heroAttribute's setHP method doesn't allow HP to go above maxHP. Be sure when using.
								heroAttributes.setHP (heroAttributes.getHP() + (int)(heroAttributes.getMaxHP ( ) / 2));
								JOptionPane.showMessageDialog (null, "The Hero has been healed.\nHero HP:    " + heroAttributes.getHP() + "HP / " + heroAttributes.getMaxHP ( ) + "HP");
								items[0]--;
							}
							moved = true;
						}
						else //If the player has no medkits
						{
							JOptionPane.showMessageDialog (null, "You have no medkits to use.", "No Medkits", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else if(itemUse == 1) //Soda
					{
						if(items[1] >= 1) 
						{
							pick = JOptionPane.showConfirmDialog (null, "Renew 50% of your SP?\nSodas: " + items[1] + "\nCurrent SP:   (" + heroAttributes.getSP ( ) + "SP / " + heroAttributes.getMaxSP() + "SP)", "Use Soda", JOptionPane.YES_NO_OPTION);
							if(pick == 0) //yes 
							{
								click();
								//The heroAttribute's setSP method doesn't allow HP to go above maxSP. Be sure when using.
								heroAttributes.setSP (heroAttributes.getSP() + (int)(heroAttributes.getMaxSP ( ) / 2));
								JOptionPane.showMessageDialog (null, "The Hero's SP has been restored.\n\nHero SP:    " + heroAttributes.getSP() + "SP / " + heroAttributes.getMaxSP ( ) + "SP");
								items[1]--;
							}
							moved = true;
						}
						else //If the player has no medkits
						{
							JOptionPane.showMessageDialog (null, "You have no medkits to use.", "No Medkits", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
				else if(pick == 4) //User picks Retreat. Retreats from battle and the enemy remains visible on the map.
				{
					pick = JOptionPane.showConfirmDialog (null, "Are you sure? This enemy may be replaced with a new enemy afterwards.", "Confirmation", JOptionPane.YES_NO_OPTION);
					if(pick == 0) //yes
					{
						inBattle = false;
						retreated = true;
						moved = true;
					}
					else //no, return to the battle.
					{
						pick = 3;
					}
				}
			click();
			}
			//Enemy Phase.
			while(moved && inBattle && enemy.getHP() != 0) 
			{
				int damageDealt = 0;
				double accuracy = enemy.getEquipment ( ).getPrimaryWeapon ( ).getTotalAccuracy ( );
				int damageTotal = 0;
				int shotsConnected = 0;
				double heroDamageReduction = heroAttributes.getEquipment ( ).getTotalArmor ( ) / 100.0;
				
				if(enemyAmmo > 0) 
				{
					for(int i = 0; i < enemy.getRps ( ); i++) 
					{
						if(rand.nextInt(100) < (accuracy*100) && enemyAmmo > 0) 
						{
							damageDealt += enemy.getEquipment ( ).getPrimaryWeapon ( ).getTotalDamage ( );
							shotsConnected++;
						}
						enemyAmmo--;
					}
					damageTotal = damageDealt - (int)(damageDealt * heroDamageReduction);
					heroAttributes.setHP (heroAttributes.getHP() - damageTotal);

					//Display the results of the Enemy Phase. Shows damage done, shots connected, HP damage & reduction, and resulting hero HP.
					JOptionPane.showMessageDialog (null, enemy.getName ( ) + " fires " + enemy.getRps ( ) + " rounds at " + heroAttributes.getName ( ) + " using their " + 
					enemy.getEquipment().getPrimaryWeapon ( ).getWeaponName ( ) + ".\n" 
					+ shotsConnected + " shots connect, dealing " + damageTotal + "HP of Damage to " + heroAttributes.getName ( ) + " (" + damageDealt + "DMG in Total).\n\n"
					+ "Thanks to the Hero's Armor, damage recieved was reduced by " + (int)(damageDealt * heroDamageReduction) + "HP." +
									"\nHero HP is now " + heroAttributes.getHP ( ) + "HP / " + heroAttributes.getMaxHP ( ) + "HP.", "Enemy Phase", JOptionPane.INFORMATION_MESSAGE);
					click();
				}
				else //If the enemy's weapon has no ammo.
				{
					JOptionPane.showMessageDialog (null, enemy.getName ( ) + " fires their " + enemy.getEquipment().getPrimaryWeapon ( ).getWeaponName ( ) + ", but it clicks!\n" 
									+ "They use this turn to reload their " + enemy.getEquipment ( ).getPrimaryWeapon ( ).getWeaponName ( ) + ".");
									click();
					enemyAmmo = enemy.getClipSize ( );
				}
				moved = false;
			}
				
			if(enemy.getHP() == 0 || heroAttributes.getHP() == 0 || retreated) 
			{
				inBattle = false;
			}
		}
		//After battle
		if(heroAttributes.getHP() != 0 && !retreated)  //Hero HP is not 0 and they did not retreat.
		{
			JOptionPane.showMessageDialog (null, "Congratulations! You have defeated the " + enemy.getName() + ".\nYou have been awarded " + (enemy.getExp ( ) * xpMultiplier) + " experience.");
			click();
			heroAttributes.addExperience(enemy.getExp ( ) * xpMultiplier);
			if(heroAttributes.canLevelUp ( )) 
			{
				try //plays level up music and shows the levelup message.
				{
					wavePlayer.pause ( );
					swavePlayer = new WavePlayer(FileRoot + "src\\Music\\LevelUp.wav", false);
					JOptionPane.showMessageDialog (null, heroAttributes.levelUp ( ), "Level Up", JOptionPane.INFORMATION_MESSAGE);
					swavePlayer.stop ( );
					wavePlayer.resumeAudio ( );
				}
				catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
				{
					System.out.println ("Error playing Level Up: " + e.getMessage());
				}
			}
			if(chapter != 0 && chapter != 1) //If the chapter is not 0 or 1.
			{
				//This is because stopping the music on chapter 0 & 1 interrupts
				//Fodlan Winds, making it start at the beginning every time after battle.
				wavePlayer.stop ( );
			}
			return true;
		}
		else if(retreated) //A retreat was performed
		{
			if(chapter != 0 && chapter != 1) 
			{
				//Same concept as above.
				wavePlayer.stop ( );
			}
			inBattle = false;
			return false;
		}
		else //The hero was defeated.
		{
			stopMusic();
			try //Play defeat music
			{
				swavePlayer = new WavePlayer(FileRoot + "src\\Music\\Grief.wav", false);
			}
			catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
			{
				e.printStackTrace();
			}
			//Rather than a typical defeat, the hero just restores their HP and stays in the room before they engaged the enemy.
			JOptionPane.showMessageDialog (null, heroAttributes.getName() + " was defeated in battle.\nThey have retreated into a room to recover.", "Defeat", JOptionPane.DEFAULT_OPTION);
			click();
			//Restore hero HP.
			heroAttributes.setHP (heroAttributes.getMaxHP ( ));
			JOptionPane.showMessageDialog (null, heroAttributes.getName() + " has recovered!\nTheir HP is now:   " + heroAttributes.getHP() + "HP / " + heroAttributes.getMaxHP ( ) + "HP", "Recovery", JOptionPane.DEFAULT_OPTION);
			click();
			
			swavePlayer.stop ( );
			return false;
		}
	}
	
	
	/**
	 * Saves the game into a text file located in the SaveFiles folder.
	 *
	 * <hr>
	 * Date created: Apr 4, 2021
	 *
	 * <hr>
	 */
	private static void saveGame() 
	{
		boolean notSaved = true;
		while(notSaved) 
		{
			int pick = JOptionPane.showConfirmDialog (null, "Would you like to save your game?\n" + heroAttributes.toString ( ) + "\n", "Save Game", JOptionPane.YES_NO_OPTION);
			
			String filepath = new String(FileRoot + "src\\SaveFiles\\File1");
			String filepath2 = new String(FileRoot + "src\\SaveFiles\\File2");
			String filesInformation = "";
			
			if(pick == 0) //yes, save game.
			{
				//This section previews both save files.
				PrintWriter writer = null;
				
				//filepaths to the save files.
				File path = new File(filepath);
				File path2 = new File(filepath2);
				Scanner inputFile = null;
				Scanner inputFile2 = null;

				
				int fileNum = 1;
				try //validate both files.
				{
					inputFile = new Scanner(path);
					fileNum++;
					inputFile2 = new Scanner(path2);
				}
				catch (FileNotFoundException e1) 
				{
					JOptionPane.showMessageDialog (null, "Save file " + fileNum + " was not found.\nPlease select a text file in the following menu.", "Save File not Found.", JOptionPane.ERROR_MESSAGE);
					JFileChooser fileChooser = new JFileChooser("src\\SaveFiles");
					pick = fileChooser.showOpenDialog (null);
					if(pick == JFileChooser.APPROVE_OPTION) 
					{
						File file = fileChooser.getSelectedFile ( );
						try
						{
							Scanner validate = new Scanner(file);
							validate.close();
						}
						catch (FileNotFoundException e)
						{
							e.printStackTrace();
						}
					}
				}
				pick = 0;
				
				//Get the summaries of the data in both files. (This one for file 1)
				if(inputFile.hasNext()) 
				{
					String str = inputFile.nextLine();
					String[] values = str.split ("\\|");
					
					//Name
					filesInformation += "(File 1) " + values[0];
					filesInformation += "\n--------------------\n";
					//Stages Completed
					filesInformation += "Stages Completed: " + values[1] + "\n";
					//Level
					filesInformation += "Level: " + values[2] + "\n";
					//Experience
					filesInformation += "Experience: " + values[3] + "XP / " + values[4] + "XP\n\n";
				}
				else 
				{
					filesInformation += "File 1 is Empty.\n\n";
				}
				//Get the summary of the data in file 2.
				if(inputFile2.hasNext()) 
				{				
					String str = inputFile2.nextLine();
					String[] values = str.split ("\\|");
					
					//Name
					filesInformation +=  "(File 2) " + values[0];
					filesInformation += "\n--------------------\n";
					//Stages Completed
					filesInformation += "Stages Completed: " + values[1] + "\n";
					//Level
					filesInformation += "Level: " + values[2] + "\n";
					//Experience
					filesInformation += "Experience: " + values[3] + "XP / " + values[4] + "XP\n\n";
				}
				else 
				{
					filesInformation += "File 2 is Empty.\n\n";
				}
				
				
				//The user chooses which file to save into.
				String[] options = {"File 1", "File 2"};
				
				pick = JOptionPane.showOptionDialog (null, "Which file would you like to save to?\n\n" + filesInformation, "Save Game", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				String savedPath = "";
				if(pick == 0) //File 1
				{
					savedPath = filepath;
				}
				else if(pick == 1) //File 2
				{
					savedPath = filepath2;
				}
				else 
				{
					//Do Nothing.
				}
				
				
				if(!savedPath.isBlank ( )) 
				{
					try
					{
						writer = new PrintWriter(savedPath);
					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}
					
					writer.println(heroAttributes.getName() + "|" +  map.getStagesCompleted ( ) + "|" + heroAttributes.getLevel ( ) + "|" + heroAttributes.getExp() + 
						"|" + heroAttributes.getExpLevelUp ( ));
					writer.println (heroAttributes.getEquipment().getPrimaryWeapon ( ).getWeaponName ( ) + "|" + heroAttributes.getEquipment().getSecondaryWeapon ( ).getWeaponName ( ));
					
					writer.println(heroAttributes.getEquipment ( ).getHeadArmor ( ).getArmorName ( ) + "|" + heroAttributes.getEquipment ( ).getTorsoArmor ( ).getArmorName ( ) + 
						"|" + heroAttributes.getEquipment ( ).getHandArmor ( ).getArmorName ( ) + "|" + heroAttributes.getEquipment ( ).getLegArmor ( ).getArmorName ( ) + "|" + 
									heroAttributes.getEquipment ( ).getFootArmor ( ).getArmorName ( ) + "|" + heroAttributes.getEquipment ( ).getAccessory ( ).getArmorName ( ) + "|" + 
						heroAttributes.getEquipment ( ).getAccessory2 ( ).getArmorName ( ));
					
					ArrayList<Weapon> weapons = heroAttributes.getEquipment ( ).getOwnedWeapons ( );
					for(Weapon weapon: weapons) 
					{
						writer.print (weapon.getWeaponName ( ) + "|");
					}
					writer.println ( );
					
					ArrayList<Armor> armors = heroAttributes.getEquipment ( ).getOwnedArmors ( );
					for(Armor armor: armors) 
					{
						writer.print (armor.getArmorName ( ) + "|");
					}
					
					notSaved = false;
					JOptionPane.showMessageDialog (null, "Your game has successfully been saved!", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
					writer.close();
				}
			}
			else //If the user chose X in the previous choice. (Which file would you like to save to?)
			{
				pick = JOptionPane.showConfirmDialog (null, "Are you sure you do not want to save your game?\nAll progress will be lost when the game is closed.", "Save Game", JOptionPane.YES_NO_OPTION);
				if(pick == 0) //Yes
				{
					notSaved = false;
					break;
				}
			}
		}
	}
	
	
	/**
	 * Plays music based on parameter input. May loop.
	 *
	 * <hr>
	 * Date created: Apr 2, 2021
	 *
	 * <hr>
	 * @param trackName
	 * @param loop
	 */
	private static void playMusic(String trackName, boolean loop) 
	{
		try //Plays the music with the same trackName as the parameter.
		{
			//wavePlayer has already been formatted to accept trackname rather than filePath.
			wavePlayer = new WavePlayer(FileRoot + "src\\Music\\" + trackName + ".wav", loop);
		}
		catch (UnsupportedAudioFileException e)
		{
			System.out.println("Sorry, this Audio Format is not Supported." + e.getMessage ( ));
		}
		catch (IOException e)
		{
			System.out.println("Sorry, the file could not be found.\n[" + e.getMessage ( ) + "]");
		}
		catch (LineUnavailableException e)
		{
			System.out.println("Sorry, " + e.getMessage ( ));
		}
	}
	
	
	/**
	 * Swaps music files, playing the second file beginning where the first one ended.
	 * Used primarily for soundtracks "FodlanWindsR" and "FodlanWindsT".
	 * These typically swap when switching from mapNavigation to Battle in Chapter 0 & 1.
	 *
	 * <hr>
	 * Date created: Apr 8, 2021
	 *
	 * <hr>
	 * @param trackName
	 * @param trackName2
	 */
	private static void swapMusic(String trackName, String trackName2) 
	{
		trackName = FileRoot + "src\\Music\\" + trackName + ".wav";
		trackName2 = FileRoot + "src\\Music\\" + trackName2 + ".wav";
		wavePlayer.swap (trackName, trackName2);
	}
	
	
	/**
	 * Generates a click sound.
	 *
	 * <hr>
	 * Date created: Apr 2, 2021
	 *
	 * <hr>
	 */
	private static void click() 
	{
		if(clickExists) //If the click.wav or click2.wav file can be played.
		{
			if(inBattle) 
			{
				//Click sound changes in battle.
				try
				{
					clickSound = new WavePlayer(FileRoot + "src\\Music\\Click2.wav", false);
				}
				catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
				{
					e.printStackTrace();
					//If the click2.wav file is not found, disables click sounds. 
					clickExists = false;
				}
			}
			else 
			{
				try
				{
					clickSound = new WavePlayer(FileRoot + "src\\Music\\Click.wav", false);
				}
				catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
				{
					e.printStackTrace();
					//If the click.wav file is not found, disables click sounds.
					clickExists = false;
				}
			}
		}
	}
	
	/**
	 * Stops the wavePlayer object from playing music.
	 *
	 * <hr>
	 * Date created: Apr 2, 2021
	 *
	 * <hr>
	 */
	private static void stopMusic() 
	{
		wavePlayer.stop ( );
		//swavePlayer may not have been initialized, so this if-statement prevents NullPointerException.
		if(swavePlayer != null) 
		{
			swavePlayer.stop ( );			
		}
		clickSound.stop ( );
	}
	

	/**
	 * This method ends the game. 
	 *
	 * <hr>
	 * Date created: Mar 31, 2021
	 *
	 * <hr>
	 */
	private static void endGame() 
	{
		click();
		JOptionPane.showMessageDialog (null, "Thank you for playing The Sigian Conflict!", "Thank you!", JOptionPane.INFORMATION_MESSAGE);
		click();
		System.exit (0);
	}
}//This is actually quite a bit of code.