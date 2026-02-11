package game;
/**
 * --------------------------------------------------------------------------
 * File name: WavePlayer.java
 * Project name: Project 5
 * --------------------------------------------------------------------------
 * Creator's name and email: GeeksforGeeks, https://www.geeksforgeeks.org/
 * Modified by: Michael Ng, ngmw01@etsu.edu
 * Course: CSCI 1260-940
 * Creation Date: 3/24/2018
 * Modification Date: 3/31/2021
 * @version 1.01
 * --------------------------------------------------------------------------
 */

//Original Project: https://www.geeksforgeeks.org/play-audio-file-using-java/
/**
 * The modified version of this audioplayer removes
 * static references and alters the constructor.
 * Many methods were removed since they did not function correctly.
 * 
 * Note: Audio Files are pulled from the same folder as Semester Project.
 * Most files have been compressed in .wav format so the size is roughly 40.5 MB.
 * 
 * Music Files are pulled from a multitude of video games/movies.
 * None of these soundtracks are made or owned by Michael Ng.
 * 
 * Soundtracks originate from:
 * Fate Grand Order -First Order-: Eaving Time (Ending), KYRIELIGHT (Preparations), Scalavera (Battle), The Shield of Chaldeas (Battle 2). 
 * Fate/Zero: Grief (Defeat). 
 * Fire Emblem Fates: Get Item, Level Up. 
 * Overwatch: Victory. 
 * Valkyria Chronicles: Main Theme (Main Menu) Europa at War (Ambience), The Gallian War (AfterMap).
 * Valkyria Chronicles 2: Briefing.
 * Fire Emblem Three Houses: Fodlan Winds (Rain), Fodlan Winds (Thunder), Indomitable Will Rain (Final Boss)
 * https://mixkit.co/free-sound-effects/click/: Click (Select click)
 * https://www.fesliyanstudios.com/royalty-free-sound-effects-download/video-game-menu-153: Click2 (Menu Button Press B Sound Effect)
 * 
 * All credit for the original WavePlayer (SimpleAudioPlayer) goes to GeeksForGeeks.
 */

// Java program to play an Audio file using Clip Object 
import java.io.File; 
import java.io.IOException; 

import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 


/**
 * WavePlayer allows the interface to play background
 * music while code is running. Music can be stopped and changed.
 * 
 * The use of errors or the try{} and catch{} method
 * is required for the implementation of audio.
 * 
 * Created: March 24, 2018
 * Date Modified: March 31, 2021
 */
public class WavePlayer 
{ 
	// to store current position 
	Long currentFrame; 
	Clip clip; 
	
	AudioInputStream audioInputStream; 
	static String filePath; 	
	
	
	/**
	 * Constructor to initialize streams and clip 
	 * altered to add the option to loop music files and
	 * now calls on the beginPlaying method.
	 * 
	 * Date Modified: 3/31/2020
	 * 
	 * @param fileName
	 * @param loop
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public WavePlayer(String fileName, boolean loop) 
		throws UnsupportedAudioFileException, 
		IOException, LineUnavailableException 
	{ 
		// create AudioInputStream object 
		audioInputStream = AudioSystem.getAudioInputStream(new File(fileName)); 
		
		// create clip reference 
		clip = AudioSystem.getClip(); 
		
		// open audioInputStream to the clip 
		clip.open(audioInputStream); 

		try
		{ 
			filePath = fileName; 
			play(); 
			if(loop)
			{
				//loops the clip infinitely.
				clip.loop(Clip.LOOP_CONTINUOUSLY); 
			}
		} 
		catch (Exception ex) 
		{ 
			System.out.println("Error with playing sound."); 
			ex.printStackTrace(); 
		}
	}
	
	/**
	 * Method to play the audio.
	 * 
	 * Date Modified: 3/31/2020
	 */
	public void play() 
	{ 
		//start the clip 
		clip.start(); 
	} 
	
	/**
	 * Stops the clip and save the location of when it stopped.
	 *
	 * <hr>
	 * Date created: Apr 7, 2021
	 *
	 * <hr>
	 */
	public void pause()
	{
		currentFrame = clip.getMicrosecondPosition();
		clip.stop();
	}
	
	/**
	 * Resume the audio where it had been saved.
	 *
	 * <hr>
	 * Date created: Apr 7, 2021
	 *
	 * <hr>
	 */
	public void resumeAudio()
	{
		play();
		clip.loop (Clip.LOOP_CONTINUOUSLY);
	}
	
	
	/**
	 * Method used to stop the audio.
	 * Modified to remove throws declaration. 
	 * This is because the sound file is validated in the constructor.
	 * 
	 * Date Modified: 3/31/2021
	 * 
	 */
	public void stop()
	{ 
		currentFrame = 0L;
		clip.stop(); 
		clip.close(); 
	} 
	
	//This int is only used here in the swap class.
	private int swap = 1;
	/**
	 * A new method not in the original source. 
	 * Stops an audio soundtrack and plays another 
	 * at roughly the same millisecond that it ended.
	 *
	 * <hr>
	 * Date created: Apr 7, 2021
	 *
	 * <hr>
	 */
	public void swap(String filePath1, String filePath2)
	{
		currentFrame = this.clip.getMicrosecondPosition ( );

		if(swap == 0) 
		{
			filePath = filePath1;
			swap = 1;
		}
		else 
		{
			
			filePath = filePath2;
			swap = 0;
		}
		clip.close ( );
		resetAudioStream();
		clip.setMicrosecondPosition (currentFrame-50);
		play ( );
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	
	/**
	 * Resets the audio stream, allowing for the input of a new audio file. 
	 * Important for the swap method.
	 * This method has been modified to be able to handle exceptions within the method.
	 *
	 * <hr>
	 * Date created: March 24, 2018
	 * Date Modified: April 7, 2021
	 * <hr>
	 */
	private void resetAudioStream()
	{
		try
		{
			audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
			clip.open(audioInputStream);
		}
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
		{
			System.out.println ("Couldn't play " + filePath + " because " + e.getMessage ( ));
		}
	}
} 