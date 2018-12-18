package com.vabbb.parallelsudoku;

public class Main {
	public static void main(String[] args){
		if (args.length < 1 || args.length > 1){
			printUsage();
		}
		return;
	}

	private static void printUsage() {
		System.out.println("\nUsage:\n\t$ java -jar parallelsudoku.jar path/to/sudokufile.txt");
	}
}