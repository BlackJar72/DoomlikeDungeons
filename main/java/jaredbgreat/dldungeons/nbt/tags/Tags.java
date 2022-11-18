package jaredbgreat.dldungeons.nbt.tags;

import java.util.HashMap;
import java.util.Locale;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

import jaredbgreat.dldungeons.nbt.NBTType;
import jaredbgreat.dldungeons.util.parser.Tokenizer;

public final class Tags {
	static final HashMap<String,ITag> registry = new HashMap<>();
	
	private Tags(){/*Do not instantiate this*/}
	
	public static ITag makeITag(String line, Tokenizer tokens) {
		ITag out;
		NBTType type = NBTType.valueOf(tokens.getToken(1).toUpperCase(Locale.ROOT));
		switch(type) {
		case BOOLEAN:
			out = new NBTBoolean(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
		case BYTE:
			out = new NBTByte(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
			break;
		case BYTE_ARRAY:
			out = new NBTByteArray(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
			break;
		case COMPOUND:
			out = new NBTCompound(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
			break;
		case DOUBLE:
			out = new NBTDouble(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
			break;
		case FLOAT:
			out = new NBTFloat(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
			break;
		case LIST:
			out = new NBTList(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
			break;
		case GROUP:
			out = new NBTGroup(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
			break;
		case INT:
			out = new NBTInt(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
			break;
		case INT_ARRAY:
			out = new NBTIntArray(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
			break;
		case LONG:
			out = new NBTLong(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
			break;
		case SHORT:
			out = new NBTShort(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
			break;
		case STRING:
			out = new NBTString(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
			break;
		case UUID:
			out = new NBTuuid(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
			break;
		case ENCH:
			out = new Enchantment(tokens.getToken(0), tokens.getToken(2), tokens.getToken(3));
			break;
		case JSON:
			String[] s = line.split("\\s+", 3);
			if(s.length == 3) 
				out = new NBTFromJsonWrapper(tokens.getToken(0), s[2]);
			else out = null;
			break;
		case END: // Neither supported nor needed. 			
		default:
			out = null;
			break;		
		}
		if(out != null)
			registry.put(tokens.getToken(0), out);
		return out;
	}
	
	
	public static ITag getTag(String label) {
		return registry.get(label);
	}

}
