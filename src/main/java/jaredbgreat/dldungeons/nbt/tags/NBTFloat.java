package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.ListNBT;

public class NBTFloat extends ITag{
	public final float  data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data; it will convert a text based 
	 * value to a float.
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTFloat(String label, String name, String data) {
		super(label, name);
		this.data  = Float.parseFloat(data);
	}
	
	
	/**
	 * The constructor for use when a float array is already available. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTFloat(String label, String name, float data) {
		super(label, name);
		this.data = data;
	}
	
	
	@Override
	public void write(CompoundNBT cmp) {
		cmp.putFloat(name, data);
	}
	
	
	@Override
	public void write(ListNBT cmp) {
		cmp.add(new FloatNBT(data));
	}


	@Override
	public NBTType getType() {
		return NBTType.FLOAT;
	}
}
