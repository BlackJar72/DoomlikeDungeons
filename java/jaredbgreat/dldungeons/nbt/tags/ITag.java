package jaredbgreat.dldungeons.nbt.tags;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/		

import net.minecraft.nbt.NBTTagCompound;

//FIXME: Should be an abstract class?  With the common data and some other
//common material included (such as a custome equals and hash functions)?

/**
 * This is the basis for creating trees of NBT data, including simple tags,
 * compounds, and sub compounds, of arbitrary depth, and allowing the same 
 * tags to be reused as either leaf or root (or technically both as a lone 
 * tag).  All potential nodes, i.e., one for each NBT data type, should use
 * this interface.
 * 
 * @author JaredBGreat (Jared Blackburn)
 */
public interface ITag {

	/**
	 * This will write the data to the NBT compound cmp, which may for some 
	 * types call this function on other tags.
	 * 
	 * @param cmp
	 */
	public void write(NBTTagCompound cmp);

}
