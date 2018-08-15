/*	Authors:	Group 18
 * 		-Micah Knerr
 * 		-Ryan Farrell
 * 	Class: COSC1320, Spring 2016
 * 	Project: Sudoku
 * 	Program: Square.java
 * 	Description:
 * 		Class which describes each individual square on the Sudoku board
 * 
 * 		Data members are:
 * 
 * 		Position, defined using two integers:
 * 		int Row- the row number of the square
 * 		int Column- the column number of the square
 * 
 * 		int Value: The value that the square holds and displays
 * 
 * 		boolean Correct: a boolean used to describe whether the value currently held is the correct answer
 * 		boolean Original: a boolean used to describe if the value was an original number from the input file or not.
 * 		If a square has false for this member, then its value can be changed through user input. Any original values will 
 * 			be marked as correct by giving the Correct boolean a value of true. 
 */

package sudokuGame;

public class Square
{
	private int row, col, value;
	private boolean isCorrect, original, lock;
	public static int[][] solvedSudoku;
	
	/**Creates the individual square
	 * 
	 * @param row The row of the square
	 * @param col The column of the square
	 * @param value The value of the square. If the square has no initial value, a 0 should be passed.
	 * @param correct If the value given is correct.
	 * @param original If the value given is from the source file. A blank or 0 should be given a value of false so the user can input the correct number.
	 */
	public Square(int row, int col, int value, boolean correct, boolean original)
	{
		this.row = row;
		this.col = col;
		this.value = value;
		this.original = original;
		this.lock = false;		// all squares start unlocked. Set lock to true when user inputs a correct answer
		if(original)
			this.isCorrect = true;
		else
			isCorrect = correct;
	}
	
	
	/**
	 * 
	 * @return the row of the square
	 */
	public int getRow()
	{
		return row;
	}
	
	/**
	 * 
	 * @return the column of the square
	 */
	public int getColumn()
	{
		return col;
	}
	
	/**
	 * 
	 * @return the value the square holds
	 */
	public int getValue()
	{
		return value;
	}
	
	/**
	 * 
	 * @return if the square's value is correct
	 */
	public boolean isCorrect()
	{
		return isCorrect;
	}
	
	/**
	 * 
	 * @return if the value is originally from the source file
	 */
	public boolean isOriginal()
	{
		return original;
	}
	
	/**Sets the correct boolean to true
	 * 
	 */
	public void setCorrect()
	{
		isCorrect = true;
	}
	
	/**
	 * isLocked() returns true if the square has been locked due to correct user entry
	 */
	public boolean isLocked()
	{
		return lock;
	}
	
	/**
	 * setLock() sets the value of boolean lock to true when called
	 */
	public void setLock()
	{
		lock = true;
	}
	
	/**Changes the value of the square if the square is not originally part of the source.
	 * 
	 * @param value the new value of the square
	 */
	public void setValue(int value)
	{
		if(!original && !lock)
			this.value = value;
		if(value == solvedSudoku[this.row][this.col]){
			this.isCorrect = true;
			this.lock = true;
		}
		else
			this.isCorrect = false;
	}
	
	public static void solveSudoku()
	{
		solvedSudoku = new int[Sudoku.ROWS_AND_COLUMNS][Sudoku.ROWS_AND_COLUMNS];
		solvedSudoku = PuzzleInput.getSolution();
	}
}
