/*	Authors:	Group 18
 * 		-Micah Knerr
 * 		-Ryan Farrell
 * 	Class: COSC1320, Spring 2016
 * 	Project: Sudoku
 * 	Program: PuzzleInput.java
 * 	Description:
 * 		Reads a source file containing a sudoku puzzle
 * 		The input file is expected to be a series of numbers from 0 to 9 separated by whitespace
 * 		All spaces on the sudoku board must be represented by a number. Any blank spaces should receive a value of 0.
 * 
 * 		The class will randomly read from the available sources.
 * 		The constant UNIQUEPUZZLES should be adjusted to match the number of available puzzles in the directory.
 * 		The names of puzzles must follow the format SudokuPuzzle#.txt where # is the number puzzle starting with 1.
 * 		If 3 puzzles are available to be read in the directory, UNIQUEPUZZLES should have a value of 3 and the names should be as follows:
 * 			SudokuPuzzle1.txt
 * 			SudokuPuzzle2.txt
 * 			SudokuPuzzle3.txt
 * 
 * 		#### This is not true in the current version. If a file is not able to be read, a window informing that the file could not be found will appear with a large exit button.
 * 		Clicking the button will exit the program. Closing the window will exit as well.
 */

package sudokuGame;

import java.util.Random;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PuzzleInput {
	private static int[][] puzzle;				// 2D array of integers that holds the sudoku puzzle being used in the game
	private static int[][] puzzleSolution;		// 2D array of integers that holds the solution to the sudoku puzzle being used in the game
	public static final int UNIQUEPUZZLES = 9;	// integer that holds the number of unique puzzles currently available to play (must equal number of puzzle files in folder)
	private static int puzzleNum;				// integer that holds the puzzle # selected for the current game
	private static int nextRow;					// int nextRow used to recursively call backtrack() for the next square
	private static int nextCol;					// int nextCol used to recursively call backtrack() for the next square
	
	/**
	 * generatePuzzle() reads the puzzle from a random source file available in the directory.
	 * File names should follow the format given above.
	 * 
	 * @return: the sudoku puzzle read from the source file represented by integers
	 */
	public static int[][] generatePuzzle()
	{
		puzzle = new int[Sudoku.ROWS_AND_COLUMNS][Sudoku.ROWS_AND_COLUMNS];
		Random r = new Random();
		puzzleNum = r.nextInt(UNIQUEPUZZLES) + 1;
		String fileName = "SudokuPuzzle" + (puzzleNum) + ".txt";
		Scanner input = null;
		
		try
		{
			input = new Scanner(new FileInputStream(fileName));
		}
		catch (FileNotFoundException e)
		{
			System.err.println(e);
		}
		
		for(int row = 0; row < puzzle.length; row++)
			for(int col = 0; col < puzzle[row].length; col++)
				puzzle[row][col] = input.nextInt();
		input.close();
		
		// generate a solution to this puzzle
		puzzleSolution = new int[Sudoku.ROWS_AND_COLUMNS][Sudoku.ROWS_AND_COLUMNS];	// create new 2D integer array based on the size of the sudoku puzzle (default 9x9)
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 9; j++){
				puzzleSolution[i][j] = puzzle[i][j];	// start with the puzzleSolution looking like the input puzzle
			}
		}
		backtrack(0,0);	// recursive method used to solve the puzzle. When backtrack() stops running puzzleSolution will be filled with the puzzle solution.
		
		
		return puzzle;
	}
	
	/**
	 * getPuzzle() returns the last input sudoku puzzle as a 2D array of integers
	 */
	public static int[][] getPuzzle()
	{
		return puzzle;
	}
	
	/**
	 * getSolution() returns the solved version of the puzzle being used as a 2D array of integers
	 */
	public static int[][] getSolution()
	{
		return puzzleSolution;
	}
	
	/**
	 * backtrack() is used to recursively solve the input puzzle. it is called in getSolution()
	 * @param row
	 * @param col
	 * @return
	 * Inspiration/pseudocode for this method of solving a sudoku puzzle came from the wikipedia page on "Sudoku Solving Algorithms" 
	 * 		as well as Yiyuan Lee's May 1, 2014 blog post located at https://codemyroad.wordpress.com/2014/05/01/solving-sudoku-by-backtracking/
	 * Actual code by Ryan Farrell
	 */
	public static boolean backtrack(int row,int col){
		
		if (puzzle[row][col] == 0){		// if the cell is "empty" in the input puzzle we want to try values that solve the puzzle
			for (int x = 1; x < 10; x++){	// want to try values 1-9
				if (gridIsValid(row, col, x)){	// check if x is currently a valid option given the contents of the aligned row and column
					puzzleSolution[row][col] = x;	// if x is valid, put it in the solution puzzle
					nextCol = col + 1;
					nextRow = row;
					if (nextCol == 9) {	// if nextCol == 9, at end of grid and need to go to next row
						nextCol = 0;
						nextRow++;
					}
					if (nextRow == 9) { // if nextRow == 9, at end of puzzle, solved!
						return true;
					}
					if (backtrack(nextRow, nextCol)) {
						return true;	// next iteration (and thus every iteration after it, due to recursion) are true, return true
					}
					else{
						puzzleSolution[row][col] = 0;	// if bactrack(nextRow, nextCol) fails then reset the value in the current cell and try again
					}
				}
				else {
					continue; // if grid is not valid then continue to next value of x
				}
			}
		}
		else {	// else (there is a value in puzzle[row][col] other than 0) do not need to try new values, try next cell
			nextCol = col + 1;
			nextRow = row;
			if (nextCol == 9) { // if nextCol == 9, at end of grid and need to go to next row
				nextCol = 0;
				nextRow++;
			}
			if (nextRow == 9) { // if nextRow == 9, at end of puzzle, solved!
				return true;
			}
			if (backtrack(nextRow, nextCol)) {
				return true;	// next iteration (and thus every iteration after it, due to recursion) are true, return true
			}
		}
		return false;	// solution not found. backtrack.
	}

	
	/**
	 * gridIsValid() checks the values in the aligned row and column of the square being checked and returns true if no duplicates are found
	 * @param row: the row of the square checkValue is located in
	 * @param col: the column of the square checkValue is located in 
	 * @param checkValue: the value contained in the square being checked
	 * @return: true if no duplicates are found. False if a duplicate is found.
	 */
	public static boolean gridIsValid(int row, int col, int checkValue){
		int gRow = 0;	// gRow indicates the row coordinate of the first (top left) square of the local 3x3 grid
		int gCol = 0;  	// gCol indicates the column coordinate of the first (top left) square of the local 3x3 grid
		for(int i = 0; i < 9; i++){ // loop through the rows of the column aligned with the checkValue
			if (puzzleSolution[row][i] == checkValue)
				return false;
		}
		for(int j = 0; j < 9; j++){	// loop through the rows of the column aligned with the checkValue
			if (puzzleSolution[j][col] == checkValue)
				return false;
		}
		// following statements used to set up where the top-left cell of the 3x3 grid being tested is located
		if (row == 0 || row == 3 || row == 6)
			gRow = row;
		if (row == 1 || row == 4 || row == 7)
			gRow = row - 1;
		if (row == 2 || row == 5 || row == 8)
			gRow = row - 2;
		if (col == 0 || col == 3 || col == 6)
			gCol = col;
		if (col == 1 || col == 4 || col == 7)
			gCol = col -1;
		if (col == 2 || col == 5 || col == 8)
			gCol = col -2;
		// check the local 3x3 grid for duplicates of checkValue
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				if (puzzleSolution[gRow+i][gCol+j] == checkValue)
					return false;
			}
		}
		
		return true;	// return true if no duplicates were found in the aligned row and column
	}
}