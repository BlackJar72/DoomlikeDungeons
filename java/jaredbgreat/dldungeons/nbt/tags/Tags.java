package jaredbgreat.dldungeons.nbt.tags;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/		

import jaredbgreat.dldungeons.nbt.NBTType;
import jaredbgreat.dldungeons.parser.Tokenizer;

import java.util.HashMap;

public final class Tags {
	static final HashMap<String,ITag> registry = new HashMap<String,ITag>();
	
	private Tags(){/*Do not instantiate this*/};
	
	public static ITag makeITag(Tokenizer tokens) {
		ITag out;
		NBTType type = NBTType.valueOf(tokens.getToken(1).toUpperCase());
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
		case LIST: // Not really supported, will be turned into a group
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
		case END: // Neither supported nor needed. 			
		default:
			out = null;
			break;		
		}
		registry.put(tokens.getToken(0), out);
		return out;
	};
	
	
	public static ITag getTag(String label) {
		return registry.get(label);
	}

}
