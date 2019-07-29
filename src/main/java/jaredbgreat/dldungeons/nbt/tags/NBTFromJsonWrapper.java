package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 * 
 * This class added by Hubry at GitHub, January 2019
 */	

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTFromJsonWrapper extends ITag {
	private NBTTagCompound wrapped;

	NBTFromJsonWrapper(String label, String data) {
		super(label, label);
		try {
			wrapped = JsonToNBT.getTagFromJson(data);
		} catch (Exception e) {
			System.err.println("Exception reading json-nbt string: " + e.getMessage());
			wrapped = new NBTTagCompound();
		}
	}

	@Override
	public void write(NBTTagCompound cmp) {
		cmp.merge(wrapped);
	}

	@Override
	public void write(NBTTagList in) {
		in.add(wrapped.copy());
	}

	@Override
	public NBTType getType() {
		return NBTType.COMPOUND;
	}
}
