package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;

public class NBTInt extends ITag {
	public final int    data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data; it will convert a text based 
	 * value to a int.
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTInt(String label, String name, String data) {
		super(label, name);
		this.data  = Integer.parseInt(data);
	}
	
	
	/**
	 * The constructor for use when an int is already available. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTInt(String label, String name, int data) {
		super(label, name);
		this.data = data;
	}
	
	
	@Override
	public void write(NBTTagCompound cmp) {
		cmp.setInteger(name, data);
	}
	
	
	@Override
	public void write(NBTTagList cmp) {
		cmp.appendTag(new NBTTagInt(data));
	}


	@Override
	public NBTType getType() {
		return NBTType.INT;
	}
}
