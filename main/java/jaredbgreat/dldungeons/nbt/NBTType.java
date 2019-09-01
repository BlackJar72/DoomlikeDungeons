package jaredbgreat.dldungeons.nbt;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */			

/**
 * This is an enumeration of NBT tag types.  Note that is includes all possible
 * tags, in order, even if not used, so for consistency with the NBT format.
 * 
 * @author JaredBGreat (Jared Blackburn)
 */
public enum NBTType {
	
	// Actual types stored in the NBT format
	END,
	BYTE,
	SHORT,
	INT,
	LONG,
	FLOAT,
	DOUBLE,
	BYTE_ARRAY,
	STRING,
	LIST,
	COMPOUND,
	INT_ARRAY,
	
	// Types not part of the NBT format but which are stored in NBT files
	BOOLEAN,
	UUID, /*Not actually used by the mod, but included for completeness*/
	
	// Special categories that are not true NBT tags
	GROUP, /*Used to group several tags without creating a sub-compound*/
	ENCH,  /*Last ditch attempt to make this work*/
	JSON;  /*Wrapper for vanilla json strings*/
}
