package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.nbt.NBTType;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;


public class NBTuuid extends ITag {
	public final UUID   data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data; it will convert a text based 
	 * value to a UUID.
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTuuid(String label, String name, String data) {
		super(label, name);
		this.data  = UUID.fromString(data);
	}
	
	
	/**
	 * The constructor for use when a UUID is already available. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTuuid(String label, String name, UUID data) {
		super(label, name);
		this.data = data;
	}
	
	
	@Override
	public void write(NBTTagCompound cmp) {
		cmp.setUniqueId(name, data);
	}
	
	
	@Override
	public void write(NBTTagList cmp) {
		cmp.add(new NBTTagLong(data.getLeastSignificantBits()));
		cmp.add(new NBTTagLong(data.getMostSignificantBits()));
	}


	@Override
	public NBTType getType() {
		return NBTType.INT;
	}
}
