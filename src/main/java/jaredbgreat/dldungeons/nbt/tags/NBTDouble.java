package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;

public class NBTDouble extends ITag {
	public final double data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data; it will convert a text based 
	 * value to a double.
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	public NBTDouble(String label, String name, String data) {
		super(label, name);
		this.data  = Double.parseDouble(data);
	}
	
	
	/**
	 * The constructor for use when a double is already available. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTDouble(String label, String name, double data) {
		super(label, name);
		this.data = data;
	}
	
	
	@Override
	public void write(CompoundNBT cmp) {
		cmp.putDouble(name, data);
	}
	
	
	@Override
	public void write(ListNBT cmp) {
		cmp.add(new DoubleNBT(data));
	}


	@Override
	public NBTType getType() {
		return NBTType.DOUBLE;
	}
}
