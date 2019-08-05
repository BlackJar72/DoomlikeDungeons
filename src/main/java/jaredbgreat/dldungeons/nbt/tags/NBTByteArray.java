package jaredbgreat.dldungeons.nbt.tags;

import java.util.ArrayList;
import java.util.StringTokenizer;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class NBTByteArray extends ITag {
	public final byte[]    data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data; it will convert a text based 
	 * value to a int array. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTByteArray(String label, String name, String data) {
		super(label, name);
		// The use of the array list is needed to process, but should not
		// effect performance as this should be done during loading, and 
		// never at run time.
		ArrayList<Byte> tmp = parseData(data);
		this.data = new byte[tmp.size()];
		for(int i = 0; i < this.data.length; i++) {
			this.data[i] = tmp.get(i);
		}
	}
	
	
	/**
	 * The constructor for use when a byte array is already available. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTByteArray(String label, String name, byte[] data) {
		super(label, name);
		this.data = data;
	}
	
	
	@Override
	public void write(CompoundNBT cmp) {
		cmp.putByteArray(name, data);
	}
	
	
	@Override
	public void write(ListNBT cmp) {
		cmp.add(new ByteArrayNBT(data));
	}
	
	
	/**
	 * This will parse the data from an array written as text into a
	 * (temporary) ArrayList of integers. 
	 * 
	 * @param in
	 * @return
	 */
	private ArrayList<Byte> parseData(String in) {
		StringTokenizer tokens = new StringTokenizer(in, "{,}");
		ArrayList<Byte> tmp = new ArrayList<Byte>();
		while(tokens.hasMoreTokens()) {
			tmp.add(Byte.parseByte(tokens.nextToken()));
		}
		return tmp;
	}


	@Override
	public NBTType getType() {
		return NBTType.BYTE_ARRAY;
	}
}
