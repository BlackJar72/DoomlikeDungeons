package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

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
	public void write(CompoundNBT cmp) {
		cmp.putBoolean(name, data);
	}
	
	
	@Override
	public void write(ListNBT cmp) {
		byte out = 0;
		if(data) {
			out = 1;
		}
		ByteNBT sub = new ByteNBT(out);
		cmp.add(sub);
	}


	@Override
	public NBTType getType() {
		return NBTType.BOOLEAN;
	}
}
