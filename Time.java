/*	Authors:	Group 18
 * 		-Micah Knerr
 * 		-Ryan Farrell
 * 	Class: COSC1320, Spring 2016
 * 	Project: Sudoku
 * 	Program: Time.java
 * 	Description:
 * 		Sets up and manages a timer for a game
 */

package sudokuGame;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class Time
{
	public static final int MILLISECONDS = 1000;
	
	private Timer timer;
	private TimeListener listener;
	/**
	 * Creates a timer and starts it with no initial delay.
	 * Time between events is determined by the MILLISECONDS constant.
	 */
	public Time()
	{
		listener = new TimeListener();
		timer = new Timer(MILLISECONDS, listener);
		timer.start();
	}
	
	/**
	 * String representation of the time. 
	 * The string is formatted as minutes:seconds.
	 * 
	 * @return String representation of the time.
	 */
	public String toString()
	{
		return String.format("%d:%02d", listener.minutes,listener.seconds);
	}
	
	/**
	 * Prepares the timer for a new game by resetting the seconds and minutes and restarting the timer.
	 */
	public void newGame()
	{
		listener.newGame();
		timer.restart();
	}
	
	/**
	 * Stops the timer
	 */
	public void stopGame()
	{
		timer.stop();
	}
	
	/**
	 * Starts the timer
	 */
	public void start()
	{
		timer.start();
	}
	
	/**
	 * Draws the timer using the form
	 * Time minutes:seconds
	 * 
	 * The timer is drawn using monospaced font at 18pt font
	 * 
	 * Rather than drawing the timer from the bottom-left corner, the top-left corner is used instead
	 * @param g The graphics to draw onto
	 * @param leftX The left coordinate
	 * @param topY The top coordinate
	 */
	public void drawTimer(Graphics g, int leftX, int topY)
	{
		g.setFont(new Font(Font.MONOSPACED,Font.PLAIN,18));
		g.setColor(Color.BLACK);
		if (Game.getQuit()){
			g.drawString("You gave up after " + this.toString() + " :(", leftX, topY + 18);
		}
		else if (Game.getSolutionState()){
			g.drawString("Success!!! Puzzle solved in " + this.toString() + " :)", leftX, topY + 18);
		}
		else {
			g.drawString("Time " + this.toString(), leftX, topY + 18);
		}
	}
	
	/**
	 * Listener which manages the time through receiving events from the timer
	 * Keeps track of the minutes and seconds
	 *
	 */
	private class TimeListener implements ActionListener
	{
		private int seconds;
		private int minutes;
		
		/**
		 * Creates the action listener and initializes the time to 0:00
		 */
		public TimeListener()
		{
			super();
			seconds = 0;
			minutes = 0;
		}
		
		/**
		 * Increments the time such that every second one second is increased.
		 * When 60 seconds has occurred, the minutes is incremented and seconds returns to 00
		 */
		public void actionPerformed(ActionEvent e)
		{
			if(seconds < 59)
				seconds++;
			else
			{
				minutes++;
				seconds = 0;
			}
		}
		
		/**
		 * Restarts the time by resetting to 0:00
		 */
		public void newGame()
		{
			seconds = 0;
			minutes = 0;
		}
	}
}


