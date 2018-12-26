package com.vabbb.parallelsudoku;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

class Sudoku {
	final static int DIM = 9;
	private final static int BOXDIM = 3;
	final static int totalCells = DIM * DIM;

	private Cell[][] sudoku;
	private List<String> sudokuString;

	private class Cell {
		int value;
		boolean fixed = false;

		void setValue(int value){
			this.value = value;
		}

		void setValue(char value){
			// ASCII magic!
			if('1' <= value && value <= '9') {
				this.fixed = true;
				this.value = Character.getNumericValue(value);
			} else this.value = 0;
		}

		boolean isFixed() { return fixed; }

		int getValue() { return this.value; }
	}

	private void constructorHelper(){
		// Create matrix of cells
		// ...and write the file's content (lineList) in the sudoku cells
		sudoku = new Cell[DIM][DIM];
		for (int i = 0; i < DIM; i++) {
			String currentLine = sudokuString.get(i);
			for (int j = 0; j < DIM; j++) {
				sudoku[i][j] = new Cell();
				setValue(i, j, currentLine.charAt(j));
			}
		}
	}
	Sudoku(File file) throws IOException {
		// Read the file
		BufferedReader br = new BufferedReader(new FileReader(file));
		String str;
		sudokuString = new ArrayList<>();
		while ((str = br.readLine()) != null && str.length() != 0)
			sudokuString.add(str);

		constructorHelper();
	}

	// I need this to make clones of the original sudoku that I read from the file
	Sudoku(List<String> ss) {
		sudokuString = ss;
		constructorHelper();
	}

	Sudoku(int[][] arr) {
		// Create matrix of cells and fill it with arr's values
		sudoku = new Cell[DIM][DIM];
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++) {
				sudoku[i][j] = new Cell();
				setValue(i, j, arr[i][j]);
			}
	}

	List<String> getSudokuString() {
		return sudokuString;
	}

	boolean isLegal(int x, int y, int value) {
		int i, j;

		// Search row and column
		for (i = 0; i < DIM; i++)
			if (getValue(x, i) == value || getValue(i, y) == value) {
				return false;
			}

		// Search the box
		int boxX0 = (x / BOXDIM) * BOXDIM;
		int boxY0 = (y / BOXDIM) * BOXDIM;
		for (i = 0; i < BOXDIM; i++)
			for (j = 0; j < BOXDIM; j++)
				if (getValue(boxX0 + i,boxY0 + j) == value) {
					return false;
				}
		return true;
	}

	void setValue(int i, int j, int value){
		sudoku[i][j].setValue(value);
	}

	private void setValue(int i, int j, char value){
		sudoku[i][j].setValue(value);
	}

	int getValue(int i, int j){
		return sudoku[i][j].getValue();
	}

	private void reset(int i, int j){
		sudoku[i][j].setValue(0);
	}

	private boolean isFixed(int i, int j){
		return sudoku[i][j].isFixed();
	}

	void prettyPrint() {
		System.out.println("+-----------------------------+");
		for (int i = 0; i < DIM; i++) {
			System.out.print("|");
			if (i % 3 == 0 && i > 0)
				System.out.print("---------+---------+---------|\n|");
			for (int j = 0; j < DIM; j++) {
				if (j % BOXDIM == 0 && j > 0)
					System.out.print("|");
				int val = getValue(i, j);
				if (val == 0)
					System.out.print(" . ");
				else System.out.printf(" %d ", val);
			}
			System.out.print("|\n");
		}
		System.out.println("+-----------------------------+\n");
	}

	BigInteger computeSearchSpace() {
		BigInteger r = BigInteger.ONE;
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				if (!isFixed(i, j))
		// computeCandidates returns the number of candidates for that cell
					r = r.multiply(computeCandidates(i, j));
		return r;
	}

	private BigInteger computeCandidates(int x, int y) {
		int r = 0;
		for (int i = 1; i <= 9; i++)
			if (isLegal(x, y, i))
				r++;
		return BigInteger.valueOf(r);
	}

	int computeEmptyCells() {
		int i, j, r = 0;
		for (i = 0; i < DIM; i++)
			for (j = 0; j < DIM; j++)
				if (getValue(i, j) == 0)
					r++;
		return r;
	}

	int seqSolver() {
		return seqSolver(0, 0);
	}

	// solver with backtracking algorithm
	int seqSolver(int i, int j){
		int r = 0;

		if (i == DIM) {				// if we got to the end of the column
			i = 0;				// go to the first cell of the column
			if (++j == DIM)			// and then to the next column; if next column is above the limit
				return 1;		// then we got a new solution
		}

		if(getValue(i, j) != 0)				// skip filled cells
			return seqSolver(i+1, j);		// recursive call to the next cell

		for(int val = 1; val <= 9; val++) {		// cycle through all possible values
			if(isLegal(i, j, val)){			// if val is legal
				setValue(i, j, val);		// then set val as the cell's value
				r += seqSolver(i+1, j);	// add additional solutions
			}
		}
		reset(i, j);		// and reset on backtrack

		return r;

	}
}
