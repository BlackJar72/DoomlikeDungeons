package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 * 
 * This class added by Hubry at GitHub, January 2019
 */	

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class NBTFromJsonWrapper extends ITag {
	private CompoundNBT wrapped;

	NBTFromJsonWrapper(String label, String data) {
		super(label, label);
		try {
			wrapped = JsonToNBT.getTagFromJson(data);
		} catch (Exception e) {
			System.err.println("Exception reading json-nbt string: " + e.getMessage());
			wrapped = new CompoundNBT();
		}
	}

	@Override
	public void write(CompoundNBT cmp) {
		cmp.merge(wrapped);
	}

	@Override
	public void write(ListNBT in) {
		in.add(wrapped.copy());
	}

	@Override
	public NBTType getType() {
		return NBTType.COMPOUND;
	}
}
