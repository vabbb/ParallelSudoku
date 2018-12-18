/* cd ~/IdeaProjects/PSMC/parallelsudoku/out/artifacts/parallelsudoku_jar/
*  java -jar parallelsudoku.jar ../../../test1/test1_a.txt
* */

package com.vabbb.parallelsudoku;

import java.io.File;

public class Main {
	public static void main(String[] args) throws Exception {
		if (args.length != 1){
			printUsage();
		}
		Sudoku sudoku = new Sudoku(new File(args[0]));
		sudoku.prettyPrint();
	}

	private static void printUsage() {
		System.out.println("\nUsage:\n\t$ java -jar parallelsudoku.jar path/to/sudokufile.txt");
	}
}