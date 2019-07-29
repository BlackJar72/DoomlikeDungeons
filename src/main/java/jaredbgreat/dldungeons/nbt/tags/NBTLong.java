package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;

public class NBTLong extends ITag {
	public final long   data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data; it will convert a text based 
	 * value to a long.
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTLong(String label, String name, String data) {
		super(label, name);
		this.data  = Long.parseLong(data);
	}
	
	
	/**
	 * The constructor for use when a long is already available. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTLong(String label, String name, long data) {
		super(label, name);
		this.data = data;
	}
	
	
	@Override
	public void write(NBTTagCompound cmp) {
		cmp.setLong(name, data);
	}
	
	
	@Override
	public void write(NBTTagList cmp) {
		cmp.add(new NBTTagLong(data));
	}


	@Override
	public NBTType getType() {
		return NBTType.LONG;
	}
}
