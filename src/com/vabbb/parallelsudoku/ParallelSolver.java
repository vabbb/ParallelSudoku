package com.vabbb.parallelsudoku;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

class ParallelSolver extends RecursiveTask<Integer> {
	private int i, j;
	private Sudoku soodoku;
	private BigInteger cutoff;
	private BigInteger searchSpace;

	ParallelSolver(Sudoku soodoku, BigInteger searchSpace, BigInteger cutoff) {
		this(0,0, soodoku, searchSpace, cutoff);
	}

	private ParallelSolver(int i, int j, Sudoku soodoku, BigInteger searchSpace, BigInteger cutoff) {
		this.i = i;
		this.j = j;
		this.soodoku = soodoku;
		this.cutoff = cutoff;
		this.searchSpace = searchSpace;
	}

	@Override
	protected Integer compute() {
		if (searchSpace.compareTo(cutoff) < 0)
		//if (soodoku.computeEmptyCells() < cutoff.intValue())
			return soodoku.seqSolver(i, j);
		int r = 0;

		List<ParallelSolver> tasks = new ArrayList<>();

		if (i == Sudoku.DIM) {
			i = 0;
			if (++j == Sudoku.DIM)
				return 1;
		}
		if (this.soodoku.getValue(i, j) != 0) {
			++i;
			return compute();
		}
		for (int val = 1; val <= Sudoku.DIM; ++val) {
			if (soodoku.isLegal(i, j, val)) {
				soodoku.setValue(i, j, val);
				ParallelSolver recursiveTask =
						new ParallelSolver (
								i + 1, j,
								cloneSudoku(soodoku),
								//BigInteger.ONE, cutoff
								soodoku.computeSearchSpace(), cutoff
						);
				tasks.add(recursiveTask);
			}
		}

		// Removing a task from the arraylist to run it on the main thread
		// shortens the wallclock time by ~10%
		if (tasks.size() > 0) {
			ParallelSolver threadHalving = tasks.remove(0);

			for (ParallelSolver t : tasks)
				t.fork();

			r += threadHalving.compute();

			for (ParallelSolver t : tasks)
				r += t.join();
		}
		return r;
	}

	private Sudoku cloneSudoku(Sudoku original) {
		int[][] r = new int[Sudoku.DIM][Sudoku.DIM];
		for (int i = 0; i < Sudoku.DIM; i++)
			for (int j = 0; j < Sudoku.DIM; j++)
				r[i][j] = original.getValue(i, j);
		return new Sudoku(r);
	}

}
