package jaredbgreat.dldungeons.nbt.tags;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/		

import net.minecraft.nbt.NBTTagCompound;

public class NBTBoolean extends ITag {
	public final boolean data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data; it will convert a text based 
	 * value to a boolean.
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTBoolean(String label, String name, String data) {
		super(label, name);
		this.data  = Boolean.parseBoolean(data);
	}
	
	
	/**
	 * The constructor for use when a boolean is already available. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTBoolean(String label, String name, boolean data) {
		super(label, name);
		this.data = data;
	}
	
	
	@Override
	public void write(NBTTagCompound cmp) {
		cmp.setBoolean(name, data);
	}
}
