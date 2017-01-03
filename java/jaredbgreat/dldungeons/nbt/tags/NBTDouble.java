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
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;

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
	public void write(NBTTagCompound cmp) {
		cmp.setDouble(name, data);
	}
	
	
	@Override
	public void write(NBTTagList cmp) {
		cmp.appendTag(new NBTTagDouble(data));
	}


	@Override
	public NBTType getType() {
		return NBTType.DOUBLE;
	}
}
