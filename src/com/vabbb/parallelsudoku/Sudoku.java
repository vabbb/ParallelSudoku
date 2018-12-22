package com.vabbb.parallelsudoku;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

class Sudoku {
	private final static int DIM = 9;
	private final static int BOXDIM = 3;
	final static int totalCells = DIM*DIM;

	private Cell[][] sudoku;

	Sudoku(File file) throws IOException {
		// Create matrix of cells
		this.sudoku = new Cell[DIM][DIM];
		for (int i = 0; i<DIM; i++)
			for (int j = 0; j<DIM; j++)
				this.sudoku[i][j] = new Cell(i, j);
		// Read the file
		BufferedReader br = new BufferedReader(new FileReader(file));
		int lines = 0;
		String str;
		List<String> lineList = new ArrayList<>();
		while ((str = br.readLine()) != null && str.length() != 0) {
			lines++;
			lineList.add(str);
		}

		// Write the file's content (lineList) in the sudoku cells
		for (int i = 0; i < lines; i++) {
			String currentLine = lineList.get(i);
			int idx = 0;
			for (int j = 0; j < currentLine.length(); j++)
				sudoku[i][j].setValue(currentLine.charAt(idx++));
		}
	}

	private boolean isLegal(int x, int y, int value) {
		int i, j;

		// Search row and column
		for (i = 0; i < DIM; i++)
			if (sudoku[x][i].getValue() == value || sudoku[i][y].getValue() == value){
				//System.out.printf("porcodio x%d y%d val%d\n",x,y,value);
				return false;
			}

		// Search the box
		int boxX0 = (x / BOXDIM) * BOXDIM;
		int boxY0 = (y / BOXDIM) * BOXDIM;
		for (i = 0; i < BOXDIM; i++)
			for (j = 0; j < BOXDIM; j++)
				if (sudoku[boxX0 + i][boxY0 + j].getValue() ==  value) {
					return false;
				}
		return true;
	}

	void prettyPrint(){
		int i, j;
		System.out.print("+-----------------------------+\n");
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
		System.out.print("+-----------------------------+\n");
	}

	BigInteger computeSearchSpace(){
		BigInteger r = BigInteger.valueOf(1);
		int i, j;
		for (i = 0; i < DIM; i++)
			for (j = 0; j < DIM; j++) {
				if (!this.sudoku[i][j].isFixed) {
					// computeCandidates returns the number of candidates for that cell.. How convenient!
					r = r.multiply(BigInteger.valueOf(computeCandidates(i, j)));
				}
			}
		return r;
	}

	private int computeCandidates(int x, int y) {
		int r = 0;
		for (int i = 1; i <= 9; i++) {
			if (isLegal(x, y, i)){
				sudoku[x][y].addCandidate(i);
				r++;
			}
		}
		return r;
	}

	int computeEmptyCells(){
		int i, j, r = 0;
		for (i = 0; i < DIM; i++)
			for (j = 0; j < DIM; j++)
				if (sudoku[i][j].getValue() == 0)
					r++;
		return r;
	}
}
