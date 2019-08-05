package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

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
	public void write(CompoundNBT cmp) {
		cmp.putString(name, data);
	}
	
	
	@Override
	public void write(ListNBT cmp) {
		cmp.add(new StringNBT(data));
	}


	@Override
	public NBTType getType() {
		return NBTType.STRING;
	}
}
