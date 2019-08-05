package jaredbgreat.dldungeons.nbt.tags;

import java.util.UUID;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;


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
	public void write(CompoundNBT cmp) {
		cmp.putUniqueId(name, data);
	}
	
	
	@Override
	public void write(ListNBT cmp) {
		cmp.add(new LongNBT(data.getLeastSignificantBits()));
		cmp.add(new LongNBT(data.getMostSignificantBits()));
	}


	@Override
	public NBTType getType() {
		return NBTType.INT;
	}
}
