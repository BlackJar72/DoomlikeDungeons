package jaredbgreat.dldungeons.nbt.tags;

import java.util.ArrayList;
import java.util.StringTokenizer;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;

public class NBTIntArray extends ITag {
	public final int[]    data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data; it will convert a text based 
	 * value to a int array. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTIntArray(String label, String name, String data) {
		super(label, name);
		// The use of the array list is needed to process, but should not
		// effect performance as this should be done during loading, and 
		// never at run time.
		ArrayList<Integer> tmp = parseData(data);
		this.data = new int[tmp.size()];
		for(int i = 0; i < this.data.length; i++) {
			this.data[i] = tmp.get(i);
		}
	}
	
	
	/**
	 * The constructor for use when an int array is already available. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTIntArray(String label, String name, int[] data) {
		super(label, name);
		this.data = data;
	}
	
	
	@Override
	public void write(CompoundNBT cmp) {
		cmp.putIntArray(name, data);
	}
	
	
	@Override
	public void write(ListNBT cmp) {
		cmp.add(new IntArrayNBT(data));
	}
	
	
	/**
	 * This will parse the data from an array written as text into a
	 * (temporary) ArrayList of integers. 
	 * 
	 * @param in
	 * @return
	 */
	private ArrayList<Integer> parseData(String in) {
		StringTokenizer tokens = new StringTokenizer(in, "{,}");
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		while(tokens.hasMoreTokens()) {
			tmp.add(Integer.getInteger(tokens.nextToken()));
		}
		return tmp;
	}


	@Override
	public NBTType getType() {
		return NBTType.INT_ARRAY;
	}
}
