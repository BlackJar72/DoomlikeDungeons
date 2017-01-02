package jaredbgreat.dldungeons.nbt.tags;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/		

import net.minecraft.nbt.NBTTagCompound;

public class NBTByte extends ITag {
	public final byte   data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data; it will convert a text based 
	 * value to a byte.
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTByte(String label, String name, String data) {
		super(label, name);
		this.data  = Byte.parseByte(data);
	}
	
	
	/**
	 * The constructor for use when a byte is already available. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTByte(String label, String name, byte data) {
		super(label, name);
		this.data = data;
	}
	
	
	@Override
	public void write(NBTTagCompound cmp) {
		cmp.setByte(name, data);
	}
}
