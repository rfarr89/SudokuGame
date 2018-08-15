/*	Authors:	Group 18
 * 		-Micah Knerr
 * 		-Ryan Farrell
 * 	Class: COSC1320, Spring 2016
 * 	Project: Sudoku
 * 	Program: MenuBar.java
 * 	Description:
 * 		Class MenuBar creates a menu bar for a Sudoku game.
 * 		The menu bar has a Game menu which has the options for New Game, Clear Board, and Solve Board.
 */

package sudokuGame;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBar extends JMenuBar 
{	
	private static final long serialVersionUID = 3L;
	
	private JMenu gameMenu, settingMenu, sizeMenu;
	private JMenuItem gameNew, gameClear, gameSolve;
	private JCheckBoxMenuItem checkCorrectToggle;
	private JMenuItem sizeNone, sizeSmall, sizeMedium, sizeLarge;
	private Game game;
	
	/**
	 * MenuBar() creates the menu bar
	 * @param game: The instance of game to communicate to start a new game, clear the board, and solve the board
	 */
	public MenuBar(Game game)
	{
		super();
		this.game = game;
		createGameMenu();
		createSettingMenu();
	}
	
	/**
	 * toggleCheck() toggles the state of the checkCorrectToggle checkbox
	 */
	public void toggleCheck()
	{
		checkCorrectToggle.setSelected(!checkCorrectToggle.isSelected());
	}
	
	/**
	 * createGameMenu() creates the game menu and adds it to the menu bar
	 */
	private void createGameMenu()
	{
		gameMenu = new JMenu("Game");
		GameMenuListener listener = new GameMenuListener();
		
		gameNew = new JMenuItem("New Game");
		gameNew.setActionCommand("New");
		gameNew.addActionListener(listener);
		
		gameClear = new JMenuItem("Clear Board");
		gameClear.setActionCommand("Clear");
		gameClear.addActionListener(listener);
		
		gameSolve = new JMenuItem("Solve Board");
		gameSolve.setActionCommand("Solve");
		gameSolve.addActionListener(listener);
		
		gameMenu.add(gameNew);
		gameMenu.add(gameClear);
		gameMenu.add(gameSolve);
		
		add(gameMenu);
	}
	
	/**
	 * class gameMenuListener for the Game menu.
	 * Determines what action to take when a menu item is pressed.
	 */
	private class GameMenuListener implements ActionListener
	{
		/**
		 * actionPerformed() determines which method to call based off of the menu item pressed.
		 */
		public void actionPerformed(ActionEvent e)
		{
			String actionEvent = e.getActionCommand();
			if(actionEvent.equals("New"))
				game.newGame();
			else if(actionEvent.equals("Clear"))
				game.resetBoard();
			else if(actionEvent.equals("Solve"))
				game.solveBoard();
		}
	}
	
	/**
	 * createSettingMenu() creates the setting menu and adds it to the menu bar
	 */
	private void createSettingMenu()
	{
		settingMenu = new JMenu("Settings");
		
		checkCorrectToggle = new JCheckBoxMenuItem("Check Correct On Input");
		checkCorrectToggle.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					game.selectCheck();
				}
				else
				{
					game.deselectCheck();
				}
			}
		});
		
		settingMenu.add(checkCorrectToggle);
		
		createSizeMenu();
		
		add(settingMenu);
	}
	
	/**
	 * createSizeMenu() creates the size menu and adds it to the setting menu
	 */
	private void createSizeMenu()
	{
		sizeMenu = new JMenu("Line Thickness");
		SizeMenuListener listener = new SizeMenuListener();
		
		sizeNone = new JMenuItem("No Lines");
		sizeNone.setActionCommand("None");
		sizeNone.addActionListener(listener);
		
		sizeSmall = new JMenuItem("Small");
		sizeSmall.addActionListener(listener);
		
		sizeMedium = new JMenuItem("Medium");
		sizeMedium.addActionListener(listener);
		
		sizeLarge = new JMenuItem("Large");
		sizeLarge.addActionListener(listener);
		
		sizeMenu.add(sizeNone);
		sizeMenu.add(sizeSmall);
		sizeMenu.add(sizeMedium);
		sizeMenu.add(sizeLarge);		
		
		settingMenu.add(sizeMenu);
	}
	
	/**
	 * class sizeMenuListener for the size menu
	 * Determines what action to take when a menu item is pressed
	 * @author Micah_Tablet
	 */
	private class SizeMenuListener implements ActionListener
	{
		/**
		 * actionPerformed() determines which line width to set depending on which button was pressed.
		 * Available sizes are 0px, 3px, 5px, and 7px.
		 * The sizes correspond to none/no lines, small, medium, and large respectively
		 */
		public void actionPerformed(ActionEvent e)
		{
			String actionEvent = e.getActionCommand();
			if(actionEvent.equals("None"))
				Sudoku.LINE_WIDTH = 0;
			else if(actionEvent.equals("Small"))
				Sudoku.LINE_WIDTH = 3;
			else if(actionEvent.equals("Medium"))
				Sudoku.LINE_WIDTH = 5;
			else if(actionEvent.equals("Large"))
				Sudoku.LINE_WIDTH = 7;
		}
	}
}
