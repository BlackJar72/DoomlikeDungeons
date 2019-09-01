package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;

public class NBTShort extends ITag {
	public final short  data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data; it will convert a text based 
	 * value to a short.
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTShort(String label, String name, String data) {
		super(label, name);
		this.data  = Short.parseShort(data);
	}
	
	
	/**
	 * The constructor for use when a short is already available. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTShort(String label, String name, short data) {
		super(label, name);
		this.data = data;
	}
	
	
	@Override
	public void write(NBTTagCompound cmp) {
		cmp.setShort(name, data);
	}
	
	
	@Override
	public void write(NBTTagList cmp) {
		cmp.appendTag(new NBTTagShort(data));
	}


	@Override
	public NBTType getType() {
		return NBTType.SHORT;
	}
}
