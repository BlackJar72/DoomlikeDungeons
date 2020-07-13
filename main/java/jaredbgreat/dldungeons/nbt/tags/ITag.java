package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * This is the basis for creating trees of NBT data, including simple tags,
 * compounds, and sub compounds, of arbitrary depth, and allowing the same 
 * tags to be reused as either leaf or root (or technically both as a lone 
 * tag).  All potential nodes, i.e., one for each NBT data type, should use
 * this as base class.
 * 
 *  This started as an interface (hence the name), but was changed to an 
 *  abstract class when it was realized that a fair amount of code would
 *  otherwise be duplicated.
 * 
 * @author JaredBGreat (Jared Blackburn)
 */
public abstract class ITag {
	protected final String label; // The label to uniquely identify the tag
	protected final String name;  // The name / label of the tag in NBT

	
	protected ITag(String label, String name) {
		this.label = label;
		this.name = name;
	}
	
	
	/**
	 * This will write the data to the NBT compound cmp, which may for some 
	 * types call this function on other tags.
	 * 
	 * @param cmp
	 */
	public abstract void write(NBTTagCompound cmp);

	
	/**
	 * Writes NBT data and returns the tag for inclusion in a list.
	 * 
	 * @param in
	 * @return
	 */
	public abstract void write(NBTTagList in);
	
	/**
	 * This will return the NBTType of the tag.
	 */
	public abstract NBTType getType();
	
	
	@Override
	public int hashCode() {
		return label.hashCode();
	}
	
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof ITag)) return false;
		else {
			ITag o = (ITag)other;
			return o.label.equals(label);
		}
	}
}
