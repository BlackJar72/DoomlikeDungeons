package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class NBTByte extends ITag {
	public final byte   data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data; it will convert a text based 
	 * value to a byte.
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTByte(String label, String name, String data) {
		super(label, name);
		this.data  = Byte.parseByte(data);
	}
	
	
	/**
	 * The constructor for use when a byte is already available. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTByte(String label, String name, byte data) {
		super(label, name);
		this.data = data;
	}
	
	
	@Override
	public void write(CompoundNBT cmp) {
		cmp.putByte(name, data);
		
	}
	
	
	@Override
	public void write(ListNBT cmp) {
		cmp.add(new ByteNBT(data));
	}


	@Override
	public NBTType getType() {
		return NBTType.BYTE;
	}
}
