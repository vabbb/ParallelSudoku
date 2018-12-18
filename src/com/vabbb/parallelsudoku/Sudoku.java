package com.vabbb.parallelsudoku;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Sudoku {
	private final static int DIM = 9;

	private Cell[][] sudoku;

	Sudoku(File file) throws Exception {
		// Crea la matrice di celle
		this.sudoku = new Cell[DIM][DIM];
		for (int i = 0; i<DIM; i++)
			for (int j = 0; j<DIM; j++)
				this.sudoku[i][j] = new Cell(i, j);

		BufferedReader br = new BufferedReader(new FileReader(file));
		int lines = 0, columns = 0;
		String str;
		List<String> lineList = new ArrayList<>();
		while ((str = br.readLine()) != null && str.length() != 0) {
			lines++;
			columns = Math.max(columns, str.length()); // as it's not fixed
			lineList.add(str);
		}

		for (int i = 0; i < lines; i++) {
			String currentLine = lineList.get(i);
			int idx = 0;
			for (int j = 0; j < currentLine.length(); j++) {
				sudoku[i][j].setValue(currentLine.charAt(idx++));
			}
		}
	}

	void prettyPrint(){
		System.out.print("+-----------------------------+\n");
		for (int i = 0; i < DIM; i++) {
			System.out.print("|");
			if(i%3==0 && i>0)
				System.out.print("---------+---------+---------|\n|");
			for (int j = 0; j < DIM; j++) {
				if (j % 3 == 0 && j>0)
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
}
