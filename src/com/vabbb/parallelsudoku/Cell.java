package com.vabbb.parallelsudoku;

import java.util.HashSet;

public class Cell {
	private HashSet<Integer> possibleValues = new HashSet<>();
	private int value;
	private int x;
	private int y;

	public Cell(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void setValue(int value){
		this.value = value;
	}

	public void setValue(char value){
		if('1' <= value && value <= '9')
			this.value = Character.getNumericValue(value);
		else this.value = 0;
	}

	public int getValue() {
		return this.value;
	}
}
