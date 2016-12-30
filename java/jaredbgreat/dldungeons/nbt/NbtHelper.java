package jaredbgreat.dldungeons.nbt;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license:
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import net.minecraft.item.ItemStack;

/**
 * This class should contain helper functions to deal with applying NBT 
 * tags to items.  Hopefully this will fix the potions, which were broken
 * when Minecraft 1.9 switched from coding potion effects as damage values 
 * to NBT, though I suspect it will be useful for other things as well.
 * 
 * Whether or not I might try to extend the use of this for other things 
 * is not yet determined as of starting this class (29 December 2016).
 * 
 * @author JaredBGreat (Jared Blackburn)
 */

public class NbtHelper {
	
	/**
	 * This will take a string and parse it into a tag and apply it.
	 * 
	 * I'm not yet sure if this is the best approach; this may very well
	 * change as I learn more about the use of NBT.
	 * 
	 * @param item
	 * @param tags
	 */
	public static void setNBT(ItemStack item, NbtTag tag) {
		// FIXME: This could probably be done more efficiently
		switch(tag.type) {
		case BYTE:
			item.getTagCompound().setByte(tag.name, Byte.parseByte(tag.data));
			break;
		case BYTE_ARRAY:
			// MAY be supported (but not yet)
			break;
		case COMPOUND:
			// NOT SUPPORTED (maybe never)
			break;
		case DOUBLE:
			item.getTagCompound().setDouble(tag.name, Double.parseDouble(tag.data));
			break;
		case END:
			// NOT SUPPORTED (maybe never)
			break;
		case FLOAT:
			item.getTagCompound().setFloat(tag.name, Float.parseFloat(tag.data));
			break;
		case INT:
			item.getTagCompound().setInteger(tag.name, Integer.parseInt(tag.data));
			break;
		case INT_ARRAY:
			// MAY be supported (but not yet)
			break;
		case LIST:
			// NOT SUPPORTED (maybe never)
			break;
		case LONG:
			item.getTagCompound().setLong(tag.name, Long.parseLong(tag.data));
			break;
		case SHORT:
			item.getTagCompound().setShort(tag.name, Short.parseShort(tag.data));
			break;
		case STRING:
			item.getTagCompound().setString(tag.name, tag.data);
			break;
		default:
			// TODO: Add error handling or message here; this should never happen!!!
			break;		
		}
	}

}
