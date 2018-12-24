package com.vabbb.parallelsudoku;

import java.util.HashSet;
import java.util.Set;

class Cell {
	//private Set<Integer> candidates;
	boolean isFixed = false;
	private int value;
	private int x;
	private int y;

	Cell(int x, int y){
		this.x = x;
		this.y = y;
//		this.candidates = new HashSet<>();
	}

	void setValue(int value){
		this.value = value;
		this.isFixed = true;
	}

	void setValue(char value){
		// ASCII magic!
		if('1' <= value && value <= '9') {
			this.isFixed = true;
			this.value = Character.getNumericValue(value);
		} else this.value = 0;
	}

//	void addCandidate(int candidate){
//		this.candidates.add(candidate);
//	}
//	void removeCandidate(int candidate){
//		this.candidates.remove(candidate);
//	}
//	int howManyCandidates(){
//		if (this.isFixed) return 1;
//		return this.candidates.size();
//	}
	int getValue() {
		return this.value;
	}
	int getX() { return this.x; }
	int getY() { return this.y; }
}
