/* cd ~/IdeaProjects/PSMC/parallelsudoku/out/artifacts/parallelsudoku_jar/
*  java -jar parallelsudoku.jar ../../../test1/test1_a.txt
* */

package com.vabbb.parallelsudoku;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class Main {
	public static void main(String[] args) throws Exception {
		if (args.length != 1)
			printUsage();

		Sudoku sudoku = new Sudoku(new File(args[0]));
		sudoku.prettyPrint();
		BigInteger searchsp = sudoku.computeSearchSpace();
		NumberFormat scientific_format = new DecimalFormat("0.######E0", DecimalFormatSymbols.getInstance(Locale.ROOT));
		String searchsp_string = scientific_format.format(searchsp);
		System.out.println("Spazio delle souluzioni: " + searchsp_string);
	}

	private static void printUsage() {
		System.out.println("\nUsage:\n\t$ java -jar parallelsudoku.jar path/to/sudokufile.txt");
	}
}