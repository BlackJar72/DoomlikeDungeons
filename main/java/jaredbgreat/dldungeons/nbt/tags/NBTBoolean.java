package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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
	
	
	@Override
	public void write(NBTTagList cmp) {
		byte out = 0;
		if(data) {
			out = 1;
		}
		NBTTagByte sub = new NBTTagByte(out);
		cmp.appendTag(sub);
	}


	@Override
	public NBTType getType() {
		return NBTType.BOOLEAN;
	}
}
