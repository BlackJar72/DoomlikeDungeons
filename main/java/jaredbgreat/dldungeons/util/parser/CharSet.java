package jaredbgreat.dldungeons.util.parser;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

/**
 * A set class (not based on Set) for characters stored as type char.
 * 
 * This is not intended to be thread safe.
 * 
 * Note that this only works with extended ASCII (values from 0 to 255).
 * 
 * As this uses 10 ints (8 for data, 2 for processing) it should use 40 
 * bytes if data,  This is more than for a short String or char[], but it 
 * should be faster and difference should be negligible in a game using 
 * a gigabyte by default. Then, the speed should also be negligible, 
 * especially at load time, but I wanted to try this anyway. 
 * 
 * @author JaredBGreat (Jared Blackburn)
 */
public class CharSet {
	private static final int BYTES = 256 / 32;
	private int[] data;
	
	// Space kept to avoid memory allocation / deallocation;
	// this is not thread safe but this should not be called
	// concurrently from multiple threads.
	private int bit;
	private int loc;
	
	/**
	 * Creates a new, empty CharSet.
	 */
	public CharSet() {
		data = new int[BYTES];
	}
	
	
	/**
	 * Creates a new char set contain the characters in the array.
	 * 
	 * @param in
	 */
	public CharSet(char[] in) {
		data = new int[BYTES];
		add(in);
	}
	
	
	/**
	 * Creates a new char set contain the characters in the String.
	 * 
	 * @param in
	 */
	public CharSet(String in) {
		data = new int[BYTES];
		add(in.toCharArray());
	}
	
	
	/**
	 * Adds the character to the CharSet.
	 * 
	 * @param in
	 */
	public void add(char in) {
		if(in > 255) return;
		bit = ((int) in) % 32;
		loc = ((int) in) / 32;
		if((data[loc] &  (1 << bit)) == 0) {
			data[loc] |= (1 << bit);	
		}
	}
	
	
	/**
	 * Removes the character from the charset.
	 * @param in
	 */
	public void remove(char in) {
		if(in > 255) return;
		bit = ((int) in) % 32;
		loc = ((int) in) / 32;
		if((data[loc] & (1 << bit)) != 0) {
			data[loc] &= ~(1 << bit);	
		}
	}
	
	
	/**
	 * Adds all the characters in the array to the CharSet.
	 * 
	 * @param in
	 */
	public void add(char[] in) {
		for(int i = 0; i < in.length; i++) {
			add(in[i]);
		}
	}
	
	
	/**
	 * Removes all the characters in the array from the CharSet.
	 * 
	 * @param in
	 */
	public void remove(char[] in) {
		for(int i = 0; i < in.length; i++) {
			remove(in[i]);
		}
	}
	
	
	/**
	 * Adds all the characters in the other CharSet to this CharSet.
	 * 
	 * @param in
	 */
	public void add(CharSet in) {
		for(int i = 0; i < BYTES; i++) {
			data[i] |= in.data[i];
		}
	}
	
	
	/**
	 * Removes all the characters in the other CharSet from this CharSet.
	 * 
	 * @param in
	 */
	public void remove(CharSet in) {
		for(int i = 0; i < BYTES; i++) {
			data[i] &= ~in.data[i];
		}
	}
	
	
	/**
	 * Return the union of this CharSet and the input.
	 * 
	 * @param in
	 * @return
	 */
	public CharSet union(CharSet in) {
		CharSet out = new CharSet();
		for(int i = 0; i < BYTES; i++) {
			out.data[i] = in.data[i] | data[i];
		}
		return out;
	}
	
	
	/**
	 * Return the intersection of this CharSet and the input.
	 * 
	 * @param in
	 * @return
	 */
	public CharSet intersection(CharSet in) {
		CharSet out = new CharSet();
		for(int i = 0; i < BYTES; i++) {
			out.data[i] = in.data[i] & data[i];
		}
		return out;
	}
	
	
	/**
	 * Return the complement of this CharSet.
	 * 
	 * @param in
	 * @return
	 */
	public CharSet complement() {
		CharSet out = new CharSet();
		for(int i = 0; i < data.length; i++) {
			out.data[i] = ~data[i];
		}
		return out;
	}
	
	
	/**
	 * Returns true if and only if the character in a member of the 
	 * CharSet.
	 * 
	 * @param in
	 * @return
	 */
	public boolean contains(char in) {
		if(in > 255) return false;
		bit = ((int) in) % 32;
		loc = ((int) in) / 32;
		return (data[loc] & (1 << bit)) != 0;
	}
	
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append('[').append(System.lineSeparator());
		for(int i = 0; i < 256; i++) {
			if(contains((char)i)) {
				b.append((char)i);
			}
		}
		b.append(System.lineSeparator()).append('[').append(System.lineSeparator());
		return b.toString();
	}
	
}
