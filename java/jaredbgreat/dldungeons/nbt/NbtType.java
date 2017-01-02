package jaredbgreat.dldungeons.nbt;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/		

/**
 * This is an enumeration of NBT tag types.  Note that is includes all possible
 * tags, in order, even if not used, so for consistency with the NBT format.
 * 
 * @author JaredBGreat (Jared Blackburn)
 */
public enum NbtType {
	
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
	UUID /*Not actually used by the mod, but included for completeness*/;
}
