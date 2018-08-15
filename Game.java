/*	Authors:	Group 18
 * 		-Micah Knerr
 * 		-Ryan Farrell
 * 	Class: COSC1320, Spring 2016
 * 	Project: Sudoku
 * 	Program: Game.java
 * 	Description:
 * 		Contains information pertaining to displaying and altering the state of the sudoku board
 * 		Also contains main() used to run the game
 */

package sudokuGame;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Game extends JPanel
{
	// main() used to run the Sudoku game
	public static void main(String[] args) {
		new Sudoku();
	}
	
	private static final long serialVersionUID = 2L;
	
	private int squareWidth;		// int holding the width of squares on the sudoku grid
	private int squareHeight;		// int holding the height of squares on the sudoku grid
	
	private int numberXOffset;		// int holding the number of X-offsets
	private int numberYOffset;		// int holding the number of Y-offsets
	
	private Square[][] board;		// Square[][] that holds the current sudoku puzzle displayed on the game board
	private int[][] solutionBoard;	// int[][] that holds the solution to the current puzzle
	private Time timer;				// timer for timing gameplay
	
	private static boolean checkCorrect, puzzleSolved, giveUp;	
	private int selectX, selectY;
	
	/**
	 * Game() creates the Sudoku board by creating the timer and starting a new game.
	 */
	public Game()
	{
		timer = new Time();	
		newGame();
		setOpaque(false);
		addMouseListener(new MouseSelect());
	}
	
	/** 
	 * paintComponent() Draws all board values onto the screen and the timer 
	 */
	public void paintComponent(Graphics g)
	{
		drawBoard(g);
		timer.drawTimer(g, 0, 0);
	}
	
	/**
	 * newGame() creates a board and initializes the selection and timer.
	 * The puzzle is received from PuzzleInput
	 */
	public void newGame()
	{
		board = new Square[Sudoku.ROWS_AND_COLUMNS][Sudoku.ROWS_AND_COLUMNS];
		solutionBoard = new int[Sudoku.ROWS_AND_COLUMNS][Sudoku.ROWS_AND_COLUMNS];
		fillBoard(0,0,PuzzleInput.generatePuzzle());
		Square.solveSudoku();
		solutionBoard = PuzzleInput.getSolution();	// save a copy of the solution for checking
		selectX = 0;
		selectY = 0;
		checkCorrect = false;
		puzzleSolved = false;
		giveUp = false;
		timer.newGame();
	}
	
	/**
	 * resetBoard() resets the board to its original state
	 */
	public void resetBoard()
	{
		fillBoard(0,0,PuzzleInput.getPuzzle());
		puzzleSolved = false;
		giveUp = false;
		timer.start(); //As long as you can clear the board from solved state, this needs to stay
	}
	
	/**
	 * solveBoard() uses the puzzle solution to fill the board.
	 */
	public void solveBoard()
	{
		fillBoard(0,0,PuzzleInput.getSolution());
		puzzleSolved = true;
		giveUp = true;
		timer.stopGame();
	}
	
	/**
	 * getSolutionState() returns the value of puzzleSolved. Used in Time.java to adjust the clock output upon solving the puzzle.
	 * @return
	 */
	public static boolean getSolutionState(){
		return puzzleSolved;
	}
	/**
	 * getQuit() is a getter method for giveUp
	 * @return: true if the user chooses to get the solution, false by default
	 */
	public static boolean getQuit(){
		return giveUp;
	}
	
	/**
	 * fillBoard() fills the board with values from the provided 2D integer array of the board
	 * row and col are expected to start at 0, however if another coordinate is given, the board will fill all spaces after the given coordinate.
	 * The board is filled by proceeding down the column of each row before moving to the next row.
	 * 
	 * @param row: row coordinate to start at
	 * @param col: column coordinate to start at
	 * @param board: 2D array of integers representing of the sudoku board
	 */
	private void fillBoard(int row, int col, int[][] board)
	{
		if(col >= Sudoku.ROWS_AND_COLUMNS)
			fillBoard(row+1,0,board);
		else if(row >= Sudoku.ROWS_AND_COLUMNS)
			return;
		
		else
		{
			if(board[row][col] != 0)
				this.board[row][col] = new Square(row,col,board[row][col],true,true);
			else
				this.board[row][col] = new Square(row,col,0,false,false);
			fillBoard(row,col+1,board);
		}
	}
	
	/**
	 * drawSelection() draws the selecting rectangle if the puzzle is not in its solved state
	 * 
	 * @param g: The graphics on which to draw
	 */
	private void drawSelection(Graphics g)
	{
		if(!puzzleSolved)
		{
			g.setColor(Color.BLUE);
			g.fillRect(Sudoku.BORDER_WIDTH + squareWidth * selectX, Sudoku.BORDER_WIDTH + squareHeight * selectY, squareWidth, Sudoku.LINE_WIDTH);
			g.fillRect(Sudoku.BORDER_WIDTH + squareWidth * selectX, Sudoku.BORDER_WIDTH + squareHeight * selectY, Sudoku.LINE_WIDTH, squareHeight);
			g.fillRect(Sudoku.BORDER_WIDTH + squareWidth * selectX, Sudoku.BORDER_WIDTH + squareHeight - Sudoku.LINE_WIDTH  + squareHeight * selectY, squareWidth, Sudoku.LINE_WIDTH);
			g.fillRect(Sudoku.BORDER_WIDTH + squareWidth - Sudoku.LINE_WIDTH + squareWidth * selectX, Sudoku.BORDER_WIDTH + squareHeight * selectY, Sudoku.LINE_WIDTH, squareHeight);
		}
	}
	
	/**
	 * drawBoard() draws the values of the board.
	 * If the value is 0, nothing is drawn
	 * 
	 * @param g: The graphics on which to draw
	 */
	public void drawBoard(Graphics g)
	{
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
		
		squareWidth = (getWidth() - 2 * Sudoku.BORDER_WIDTH) / Sudoku.ROWS_AND_COLUMNS;
		squareHeight = (getHeight() - 2 * Sudoku.BORDER_WIDTH) / Sudoku.ROWS_AND_COLUMNS;
		numberXOffset = squareWidth / 3;
		numberYOffset = squareHeight * 3 / 4;
		
		for(int row = 1; row <= Sudoku.ROWS_AND_COLUMNS; row++)
			for(int col = 1; col <= Sudoku.ROWS_AND_COLUMNS; col++)
			{
				String printValue;
				if(boardValue(row-1,col-1) == 0)
					printValue = "";
				else
					printValue = Integer.toString(boardValue(row-1,col-1));
				if(checkCorrect || puzzleSolved)
				{
					if(board[row-1][col-1].isOriginal())
						g.setColor(Color.BLACK);
					else if(board[row-1][col-1].isCorrect())
						g.setColor(Color.GREEN);
					else
						g.setColor(Color.RED);						
				}
				else{
					if(board[row-1][col-1].isOriginal())
						g.setColor(Color.BLACK);
					else
						g.setColor(Color.BLUE);
				}
				
				g.drawString(printValue, Sudoku.BORDER_WIDTH + numberXOffset + squareWidth * (col - 1), Sudoku.BORDER_WIDTH + numberYOffset + squareHeight * (row - 1));
			}
		// continually check to see if the puzzle has been solved. When a solution is reached, stop the timer and display a victory message.
		if (isSolved()){		// if the puzzle is solved...
			puzzleSolved = true;	// set puzzleSolved to true
			timer.stopGame();		// stop the game clock
		}
		// if puzzle is not solved, do nothing and keep playing

		drawSelection(g);	 // update selection square
	}
	
	/**
	 * boardValue() returns the value of the board at the given coordinate
	 * 
	 * @param row: row coordinate
	 * @param col: column coordinate
	 * @return: the value at that square
	 */
	public int boardValue(int row, int col)
	{
		return board[row][col].getValue();
	}
	
	/**
	 * moveUP() moves the selection up if it can.
	 */
	public void moveUp()
	{
		if(selectY > 0)
			selectY -= 1;
		else
			selectY = 0;
	}
	
	/**
	 * moveDown() moves the selection down if it can.
	 */
	public void moveDown()
	{
		if(selectY < Sudoku.ROWS_AND_COLUMNS - 1)
			selectY += 1;
		else
			selectY = Sudoku.ROWS_AND_COLUMNS - 1;
	}
	
	/**
	 * moveRight() moves the selection right if it can.
	 */
	public void moveRight()
	{
		if(selectX < Sudoku.ROWS_AND_COLUMNS - 1)
			selectX += 1;
		else
			selectX = Sudoku.ROWS_AND_COLUMNS - 1;
	}
	
	/**
	 * moveLeft() moves the selection left if it can.
	 */
	public void moveLeft()
	{
		if(selectX > 0)
			selectX -= 1;
		else
			selectX = 0;
	}
	
	/**
	 * changeValue() changes the value of the square at the selected space
	 * 
	 * @param value: The value to change to
	 */
	public void changeValue(int value)
	{
		if(value < 0 || value > Sudoku.ROWS_AND_COLUMNS)
		{
			System.err.println("Invalid value input");
			return;
		}
		//Order is reversed from intuitive x,y because x is related to columns while y is related to rows.
		//Since the board is set up as board[rows][columns], setting the value must use board[y][x]
		if(!board[selectY][selectX].isOriginal() && !board[selectY][selectX].isLocked())
			board[selectY][selectX].setValue(value);
	}
	
	/**
	 * selectCheck() sets the game to check whether input is correct
	 */
	public void selectCheck()
	{
		checkCorrect = true;	// "activate" checkCorrect
		
	}
	
	/**
	 * isSolved() is used to check if the puzzle has been solved or not. 
	 * @return false if a difference is found between the game board and the solution board. True if they are 100% identical.
	 */
	public boolean isSolved() {
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 9; j++){
				if (board[i][j].getValue() != solutionBoard[i][j]){
					return false;	// if a single difference is found, return false
				}
			}
		}
		return true; 	// if no differences are found, return true
	}
	
	/**
	 * deselectCheck() sets the game to not check whether input is correct
	 */
	public void deselectCheck()
	{
		checkCorrect = false;
	}
	
	/**
	 * class MouseSelect allows for the mouse to select a square when it clicks within its area
	 */
	private class MouseSelect implements MouseListener
	{
		/**
		 * mouseClicked() listens for when the mouse is clicked. When it is clicked the location is translated 
		 * to the square which it is trying to select. If the click occurs outside the board space nothing happens
		 */
		public void mouseClicked(MouseEvent e)
		{
			int boardWidth = getWidth() - 2 * Sudoku.BORDER_WIDTH;
			int boardHeight = getHeight() - 2 * Sudoku.BORDER_WIDTH;
			int mouseX = e.getX() - Sudoku.BORDER_WIDTH;
			int mouseY = e.getY() - Sudoku.BORDER_WIDTH;
			boolean withinX = mouseX > 0 && mouseX < boardWidth;
			boolean withinY = mouseY > 0 && mouseY < boardHeight;
			if(withinX && withinY)
			{
				selectX = mouseX / squareWidth;
				selectY = mouseY / squareWidth;
			}
			e.consume();
		}
		
		/**Stub for no action*/
		public void mousePressed(MouseEvent e){}
		/**Stub for no action*/
		public void mouseReleased(MouseEvent e){}
		/**Stub for no action*/
		public void mouseEntered(MouseEvent e){}
		/**Stub for no action*/
		public void mouseExited(MouseEvent e){}
	}
}