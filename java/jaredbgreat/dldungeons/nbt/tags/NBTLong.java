package jaredbgreat.dldungeons.nbt.tags;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/		

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;

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
	public void write(NBTTagCompound cmp) {
		cmp.setLong(name, data);
	}
	
	
	@Override
	public void write(NBTTagList cmp) {
		cmp.appendTag(new NBTTagLong(data));
	}


	@Override
	public NBTType getType() {
		return NBTType.LONG;
	}
}
