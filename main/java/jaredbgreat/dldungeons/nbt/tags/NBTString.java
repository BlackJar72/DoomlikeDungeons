package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class NBTString extends ITag {
	public final String data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data.
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTString(String label, String name, String data) {
		super(label, name);
		this.data  = data;
	}
	
	
	@Override
	public void write(NBTTagCompound cmp) {
		cmp.setString(name, data);
	}
	
	
	@Override
	public void write(NBTTagList cmp) {
		cmp.appendTag(new NBTTagString(data));
	}


	@Override
	public NBTType getType() {
		return NBTType.STRING;
	}
}
