package jaredbgreat.dldungeons.nbt.tags;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/		

import net.minecraft.nbt.NBTTagCompound;

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
}
