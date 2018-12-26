/* cd ~/IdeaProjects/PSMC/parallelsudoku/out/artifacts/parallelsudoku_jar/
*  java -jar parallelsudoku.jar ../../../test1/test1_a.txt
* */

package com.vabbb.parallelsudoku;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ForkJoinPool;

public class Main {
	public static void main(String[] args) throws IOException {
		long startTime, endTime;
		String cutoff_string = "1E10";

		if (args.length == 2)
			cutoff_string = args[1];
		else if (args.length != 1) {
			printUsage();
			return;
		}

		BigInteger cutoff = new BigDecimal(cutoff_string).toBigInteger();

		Sudoku sudoku = new Sudoku(new File(args[0]));
		sudoku.prettyPrint();

		BigInteger searchsp = sudoku.computeSearchSpace();
		NumberFormat scientific_format =
				new DecimalFormat (
						"0.######E0",
						DecimalFormatSymbols.getInstance(Locale.ROOT)
				);
		String searchsp_string = scientific_format.format(searchsp);
		System.out.println("Spazio delle souluzioni: " + searchsp_string);

		int emptyCells = sudoku.computeEmptyCells();
		System.out.println("Celle vuote: " + emptyCells);
		// 100.0 makes every int into a float for this operation
		System.out.printf("Fattore di riempimento: %.1f%%\n\n",
				(Sudoku.totalCells - emptyCells)*100.0/(Sudoku.totalCells));

		// PARALLEL ALGORITHM
		System.out.println("+++ PARALLEL SOLVER");
		Sudoku parSudoku= new Sudoku(sudoku.getSudokuString());
		startTime = System.currentTimeMillis();
		int parSolutions = ForkJoinPool.commonPool().invoke (
				new ParallelSolver(parSudoku, searchsp, cutoff));
		endTime = System.currentTimeMillis();
		long parallelTime = endTime-startTime;
		System.out.printf("Wallclock time w/ parallel alg:  %8d ms\n", parallelTime);
		System.out.println("Possible solutions: " + parSolutions + "\n");

		// SEQUENTIAL ALGORITHM
		System.out.println("+++ SEQUENTIAL SOLVER");
		Sudoku seqSudoku= new Sudoku(sudoku.getSudokuString());
		startTime = System.currentTimeMillis();
		int seqSolutions = seqSudoku.seqSolver();
		endTime = System.currentTimeMillis();
		long sequentialTime = endTime-startTime;
		System.out.printf("Wallclock time w/ sequential alg:%8d ms\n", sequentialTime);
		System.out.println("Possible solutions: " + seqSolutions + "\n");

		// CALCULATE SPEEDUP
		System.out.print("Speedup: ");
		try {
			System.out.printf("%.6f\n",
					(double)sequentialTime/(double)parallelTime);
		} catch (ArithmeticException e){
			System.out.println("ArithmeticException - " + e);
		}
	}

	private static void printUsage() {
		System.out.println(
				"\nUtilizzo:\n\n$ java -jar parallelsudoku.jar " +
				"path/to/sudokufile.txt [cutoff in notazione scientifica]\n"
		);
	}

}
