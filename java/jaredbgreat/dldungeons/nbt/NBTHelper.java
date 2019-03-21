package jaredbgreat.dldungeons.nbt;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

import jaredbgreat.dldungeons.nbt.tags.ITag;
import jaredbgreat.dldungeons.nbt.tags.Tags;
import jaredbgreat.dldungeons.parser.Tokenizer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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

public class NBTHelper {
	
	/**
	 * This will take a an NBT tag (stored as an ITag object) and apply it
	 * to the item stack.
	 * 
	 * I'm not yet sure if this is the best approach; this may very well
	 * change as I learn more about the use of NBT.
	 * 
	 * @param item
	 * @param tags
	 */
	public static void setNbtTag(ItemStack item, ITag tag) {
		if(tag == null) {
			System.err.println("ERROR! null tag on item " + item.getItem().getUnlocalizedName());
		} else {
			if(!item.hasTagCompound()) {
				item.setTagCompound(new NBTTagCompound());
			}
			tag.write(item.getTagCompound());			
		}
	}
	
	
	/**
	 * This will take a string and parse it into a tag and apply it to the
	 * item stack.
	 * 
	 * @param item
	 * @param tags
	 */
	public static void setNbtTag(ItemStack item, String label) {
		Tags.getTag(label).write(item.getTagCompound());
	}
	
	
	/**
	 * Will take a tag label and return the corresponding NBT tag as an ITag
	 * object.
	 * 
	 * @param label
	 * @return
	 */
	public static ITag getTagFromLabel(String label) {
		return Tags.getTag(label);
	}
	
	
	/**
	 * This will parse a line into a relevant tag.
	 * 
	 * @param line
	 * @return
	 */
	public static ITag parseNBTLine(String line) {
		ITag out;
		Tokenizer tokens = new Tokenizer(line, " \t");
		return Tags.makeITag(line, tokens);
	}
	
	
	
}
