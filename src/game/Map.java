/**
 * ---------------------------------------------------------------------------
 * File name: Map.java
 * Project name: Project 5
 * ---------------------------------------------------------------------------
 * Creator's name and email: Michael Ng, ngmw01@etsu.edu
 * Course:  CSCI 1260-940 (Intro Computer Sci II)
 * Creation Date: May 29, 2022
 * ---------------------------------------------------------------------------
 */

package game;
import java.util.Random;

/**
 * The Map class keeps track of the hero's progress.
 *
 * <hr>
 * Date created: Mar 28, 2021
 * <hr>
 * @author Michael Ng
 */
public class Map
{
	private int stagesCompleted, locationX, locationY;
	private String recentMovement; 
	private String[][] battlefield;
	
	/**
	 * The Default Constructor of the Map Class.
	 *
	 * <hr>
	 * Date created: Apr 1, 2021 
	 */
	public Map() 
	{
		stagesCompleted = 0;
		locationX = 0;
		locationY = 0;
	}
	
	/**
	 * A partially parameterized Constructor.
	 * Is called when loading a save file.
	 *
	 * <hr>
	 * Date created: Apr 1, 2021 
	 *
	 * <hr>
	 * @param stagesCompleted
	 */
	public Map(int stagesCompleted) 
	{
		this.stagesCompleted = stagesCompleted;
		locationX = 0;
		locationY = 0;
	}
	
	
	/**
	 * This method initiates the battlefield map by placing enemies & equipment.
	 *
	 * <hr>
	 * Date created: Apr 1, 2021
	 *
	 * <hr>
	 * @param length
	 * @param width
	 * @param enemies
	 */
	public void initiateBattleField(int length, int width, int enemies) 
	{
		int amountEnemies = 0;
		int amountItems = 0;
		locationX = 0;
		locationY = 0;
		
		Random rand = new Random();
		battlefield = new String[length][width];
		battlefield[0][0] = "[-P-] ";
		
		//While there are not the required amount of enemies on the field.
		while(amountEnemies != enemies) 
		{
			for(int j = 0; j < length; j++) 
			{
				for(int i = 0; i < width; i++) 
				{
					//This algorithm spawns enemies first, then items.
					//If there is an enemy already in the room, 
					if(battlefield[j][i] == null) 
					{
						//The chance of an enemy spawning per block is 10%. Change the 10 below to alter the value.
						//amountEnemies != enemies makes sure there are not more enemies than the specified maximum.
						//10% also allows the enemies to be more spread out on bigger maps.
						if((rand.nextInt (100) + 1) <= 10 && amountEnemies != enemies) 
						{
							battlefield[j][i] = "[-E-] ";
							amountEnemies++;
						}
						//The max amount of items that can spawn is based on the size of the room.
						//Has a 20% chance of spawning per room.
						if((rand.nextInt(100)+1) <= 20 && amountItems < length) 
						{
							if(battlefield[j][i] == null) //There might already be an enemy on the field.
							{
								battlefield[j][i] = "[-I-] ";
								amountItems++;
							}
						}
					}
				}
			}
		}
	}


	/**
	 * This method initiates the battlefield map by placing enemies & equipment.
	 * This method is overloaded, allowing for the changing of enemySparseness and itemSparseness.
	 * 
	 * length can be from 2 - 25.
	 * width can be from 2 - 25.
	 * enemies can be from 1 - 20.
	 * enemySparseness can be from 1 - 99.
	 * itemSpawnRate can be from 1 - 99.
	 * All values are inclusive.
	 * 
	 * <hr>
	 * Date created: May 29, 2022
	 *
	 * <hr>
	 * @param length
	 * @param width
	 * @param enemies
	 */
	public void initiateBattleField(int length, int width, int enemies, int enemySparseness, int itemSpawnRate) 
	{
		System.out.println("MP Enemies Before: " + enemies);

		//We might have a 2x2 map and the user put 25 enemies. At max, we can only support 3 enemies here.
		//For example, if we had 5 enemies on a 2x2 map, we set enemies to 2x2 - 1, which is 3.
		if(enemies >= (length * width))
		{
			enemies = (length * width) - 1;
		}

		System.out.println("MP Enemies After: " + enemies);

		int amountEnemies = 0;
		locationX = 0;
		locationY = 0;
		
		Random rand = new Random();
		battlefield = new String[length][width];
		battlefield[0][0] = "[-P-] ";
		
		//While there are not the required amount of enemies on the field.
		while(amountEnemies != enemies) 
		{
			for(int j = 0; j < length; j++) 
			{
				for(int i = 0; i < width; i++) 
				{
					//This algorithm spawns enemies first, then items.
					//If there is an enemy already in the room, we skip it.
					//We can also replace items.
					if(battlefield[j][i] == null || battlefield[j][i] == "[-I-] ") 
					{
						//The chance of an enemy spawning per block is 10%. Change the 10 below to alter the value.
						//amountEnemies != enemies makes sure there are not more enemies than the specified maximum.
						//10% also allows the enemies to be more spread out on bigger maps.
						if((rand.nextInt (100) + 1) <= enemySparseness && amountEnemies != enemies) 
						{
							battlefield[j][i] = "[-E-] ";
							amountEnemies++;
						}
						//The max amount of items that can spawn is unlimited, considering that this is a skirmish.
						//Items spawn based on itemSpawnRate.
						if((rand.nextInt(100)+1) <= itemSpawnRate) //&& amountItems < length) 
						{
							if(battlefield[j][i] == null) //There might already be an enemy on the field.
							{
								battlefield[j][i] = "[-I-] ";
							}
						}
					}
				}
			}
			//Increasing this per run helps prevent a potential forever loop.
			enemySparseness++;
			System.out.println("Iterated While: " + enemySparseness );
		}
	}
	
	/**
	 * This method returns the battlefield map in string format.     
	 *
	 * <hr>
	 * Date created: Apr 1, 2021
	 *
	 * <hr>
	 * @return message
	 */
	public String getBattleFieldString() 
	{
		String message = "";
		
		//Puts the battlefield into string format. This was actually harder than expected to get into the right position.
		for(int j = battlefield[0].length-1; j >= 0; j--) 
		{
			for(int i = 0; i < battlefield[1].length; i++) 
			{
				//if a battlefield tile is null, replaces it with an empty cell.
				if(battlefield[i][j] == null) 
				{
					battlefield[i][j] = "[ - - ] ";
				}
				message += battlefield[i][j];
			}
			message += "\n";
		}
		
		message += "__________";
		message += "\nLegend";
		message += "\n--------";
		message += "\n[-I-]: Item";
		message += "\n[-E-]: Enemy";
		message += "\n[-P-]: Player";
		
		
		return message;
	}
	
	
	/**
	 * This method returns the battlefield map in string format.    
	 * Returns Enemy, Item, or None 
	 *
	 * <hr>
	 * Date created: Apr 1, 2021
	 *
	 * <hr>
	 * @return "Enemy", "Item", "None" - String
	 */
	public String movePlayer(String direction) 
	{
		direction = direction.toUpperCase ( );
		if(direction.equals ("SOUTH")) 
		{
			locationY += -1;
			if(locationY <= -1) 
			{
				locationY = 0;
			}
			recentMovement = "S";
			
		}
		else if(direction.equals ("EAST")) 
		{
			locationX += 1;
			if(locationX >= battlefield[0].length) 
			{
				locationX = battlefield[0].length-1;
			}
			recentMovement = "E";
		}
		else if(direction.equals ("NORTH")) 
		{
			locationY += 1;
			if(locationY == battlefield.length) 
			{
				locationY = battlefield.length-1;
			}
			recentMovement = "N";
		}
		else if(direction.equals ("WEST")) 
		{
			locationX += -1;
			if(locationX <= -1) 
			{
				locationX = 0;
			}
			recentMovement = "W";
		}

		
		if(battlefield[locationX][locationY].equals ("[-E-] ")) 
		{
			return "Enemy";
		}
		else if(battlefield[locationX][locationY].equals ("[-I-] ")) 
		{
			return "Item";
		}
		else 
		{
			return "None";
		}
	}
	
	/**
	 * Sets the player's location to locationX and locationY.
	 * Removes the previous [P] of the player's place.
	 *
	 * <hr>
	 * Date created: Apr 2, 2021
	 *
	 * <hr>
	 * @param moved
	 */
	public void commitMove(boolean moved) 
	{
		if(moved) //if an enemy was defeated by the player.
		{
			battlefield[locationX][locationY] = "[-P-] ";

		
			if(recentMovement.equals("S")) 
			{
				if(battlefield[locationX][locationY+1] == "[-P-] ") 
				{
					battlefield[locationX][locationY+1] = "[ - - ] ";
				}
				
			}
			else if(recentMovement.equals("E")) 
			{
				if(battlefield[locationX-1][locationY] == "[-P-] ") 
				{
					battlefield[locationX-1][locationY] = "[ - - ] ";
				}
				
			}
			else if(recentMovement.equals("N")) 
			{
				if(battlefield[locationX][locationY-1] == "[-P-] ") 
				{
					battlefield[locationX][locationY-1] = "[ - - ] ";
				}
				
			}
			else if(recentMovement.equals("W")) 
			{
				if(battlefield[locationX+1][locationY] == "[-P-] ") 
				{
					battlefield[locationX+1][locationY] = "[ - - ] ";
				}
				
			}
		}
		else //if the player decided not to enter the room.
		{
			if(recentMovement.equals("S")) 
			{
				locationY++;
			}
			else if(recentMovement.equals("E")) 
			{
				locationX--;
			}
			else if(recentMovement.equals("N")) 
			{
				locationY--;
			}
			else if(recentMovement.equals("W")) 
			{
				locationX++;
			}
		}
	}
	
	
	/**
	 * This method moves the player back to their previous position
	 * if they decide not to go into a room.
	 *
	 * <hr>
	 * Date created: Apr 2, 2021
	 *
	 * <hr>
	 */
	public void moveBack() 
	{
		if(recentMovement.equals("S")) 
		{
			locationY++;
		}
		else if(recentMovement.equals ("E")) 
		{
			locationX--;
		}
		else if(recentMovement.equals ("N")) 
		{
			locationY--;
		}
		else if(recentMovement.equals ("W")) 
		{
			locationX++;
		}
	}
	
	
	/**
	 * @return stagesCompleted
	 */
	public int getStagesCompleted ( )
	{
		return stagesCompleted;
	}

	
	/**
	 * @param stagesCompleted the stagesCompleted to set
	 */
	public void setStagesCompleted (int stagesCompleted)
	{
		this.stagesCompleted = stagesCompleted;
	}

	/**
	 * @return locationX
	 */
	public int getLocationX ( )
	{
		return locationX;
	}

	
	/**
	 * @return locationY
	 */
	public int getLocationY ( )
	{
		return locationY;
	}
}
