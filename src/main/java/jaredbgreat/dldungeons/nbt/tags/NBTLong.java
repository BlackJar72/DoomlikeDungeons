package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;

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
	public void write(CompoundNBT cmp) {
		cmp.putLong(name, data);
	}
	
	
	@Override
	public void write(ListNBT cmp) {
		cmp.add(new LongNBT(data));
	}


	@Override
	public NBTType getType() {
		return NBTType.LONG;
	}
}
