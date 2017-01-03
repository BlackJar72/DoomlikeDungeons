package jaredbgreat.dldungeons.nbt.tags;

import jaredbgreat.dldungeons.nbt.NBTType;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/

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
	public void write(NBTTagCompound cmp) {
		cmp.setUniqueId(name, data);
	}
	
	
	@Override
	public void write(NBTTagList cmp) {
		cmp.appendTag(new NBTTagLong(data.getLeastSignificantBits()));
		cmp.appendTag(new NBTTagLong(data.getMostSignificantBits()));
	}


	@Override
	public NBTType getType() {
		return NBTType.INT;
	}
}
