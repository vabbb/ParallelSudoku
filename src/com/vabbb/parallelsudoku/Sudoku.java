package com.vabbb.parallelsudoku;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

class Sudoku {
	private final static int DIM = 9;
	private final static int BOXDIM = 3;
	final static int totalCells = DIM*DIM;

	private Cell[][] sudoku;
	private List<String> sudokuString;

	Sudoku(File file) throws IOException {
		// Create matrix of cells
		this.sudoku = new Cell[DIM][DIM];
		for (int i = 0; i<DIM; i++)
			for (int j = 0; j<DIM; j++)
				this.sudoku[i][j] = new Cell(i, j);
		// Read the file
		BufferedReader br = new BufferedReader(new FileReader(file));
		String str;
		this.sudokuString = new ArrayList<>();
		while ((str = br.readLine()) != null && str.length() != 0) {
			this.sudokuString.add(str);
		}

		// Write the file's content (sudokuString) in the sudoku cells
		for (int i = 0; i < DIM; i++) {
			String currentLine = this.sudokuString.get(i);
			int idx = 0;
			for (int j = 0; j < currentLine.length(); j++)
				this.sudoku[i][j].setValue(currentLine.charAt(idx++));
		}
	}

	// I need this to make clones of the original sudoku that I read from the file
	Sudoku(List<String> sudokuString) {
		// Create matrix of cells
		this.sudoku = new Cell[DIM][DIM];
		for (int i = 0; i<DIM; i++)
			for (int j = 0; j<DIM; j++)
				this.sudoku[i][j] = new Cell(i, j);

		// Write the file's content (lineList) in the sudoku cells
		for (int i = 0; i < DIM; i++) {
			String currentLine = sudokuString.get(i);
			int idx = 0;
			for (int j = 0; j < currentLine.length(); j++)
				this.sudoku[i][j].setValue(currentLine.charAt(idx++));
		}
	}

	List<String> getSudokuString(){
		return this.sudokuString;
	}

	private boolean isLegal(int x, int y, int value) {
		int i, j;

		// Search row and column
		for (i = 0; i < DIM; i++)
			if (this.sudoku[x][i].getValue() == value || this.sudoku[i][y].getValue() == value){
				return false;
			}

		// Search the box
		int boxX0 = (x / BOXDIM) * BOXDIM;
		int boxY0 = (y / BOXDIM) * BOXDIM;
		for (i = 0; i < BOXDIM; i++)
			for (j = 0; j < BOXDIM; j++)
				if (this.sudoku[boxX0 + i][boxY0 + j].getValue() ==  value) {
					return false;
				}
		return true;
	}

	void prettyPrint(){
		int i, j;
		System.out.println("+-----------------------------+");
		for (i = 0; i < DIM; i++) {
			System.out.print("|");
			if(i % 3 == 0 && i > 0)
				System.out.print("---------+---------+---------|\n|");
			for (j = 0; j < DIM; j++) {
				if (j % BOXDIM == 0 && j > 0)
					System.out.print("|");
				int val = this.sudoku[i][j].getValue();
				if (val == 0)
					System.out.print(" . ");
				else System.out.printf(" %d ", val);
			}
			System.out.print("|\n");
		}
		System.out.println("+-----------------------------+");
	}

	BigInteger computeSearchSpace(){
		BigInteger r = BigInteger.valueOf(1);
		int i, j;
		for (i = 0; i < DIM; i++)
			for (j = 0; j < DIM; j++) {
				if (!this.sudoku[i][j].isFixed) {
					// computeCandidates returns the number of candidates for that cell
					// How convenient!
					r = r.multiply(BigInteger.valueOf(computeCandidates(i, j)));
				}
			}
		return r;
	}

	private int computeCandidates(int x, int y) {
		int r = 0;
		for (int i = 1; i <= 9; i++) {
			if (isLegal(x, y, i)){
				//this.sudoku[x][y].addCandidate(i);
				r++;
			}
		}
		return r;
	}

	int computeEmptyCells(){
		int i, j, r = 0;
		for (i = 0; i < DIM; i++)
			for (j = 0; j < DIM; j++)
				if (this.sudoku[i][j].getValue() == 0)
					r++;
		return r;
	}

	int seqSolve(){
		return seqSolve(0, 0, 0);
	}

	// solver with backtracking algorithm
	int seqSolve(int i, int j, int count) {
		if (i == DIM) {				// if we got to the end of the line
			i = 0;				// go to the first cell of the line
			if (++j == DIM)			// and then to the next line; if next line is above the limit
				return 1 + count;	// then we got a new solution
		}
		if (this.sudoku[i][j].getValue() != 0)		// skip fixed cells
			return seqSolve(i+1, j, count);	// recursive call to the next cell

		for (int val = 1; val <= DIM; ++val) {			// cycle through all possible values
			if (isLegal(i, j, val)) {			// if val is legal
				this.sudoku[i][j].setValue(val);	// then set val as the cell's value
				count = seqSolve(i+1,j, count);	// add additional solutions
			}
		}
							// if no legal value was found
		this.sudoku[i][j].setValue(0);		// then reset on backtrack

		return count;				// return what we got so far
	}

	int parSolve() {
		ParallelSolver parsolver = new ParallelSolver(0,0);
		return 1;
	}

	class ParallelSolver extends RecursiveTask<Integer> {
		private int x, y;

		ParallelSolver(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		protected Integer compute() {
			return null;
		}
	}

}


