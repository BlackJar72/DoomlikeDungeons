package jaredbgreat.dldungeons.nbt.tags;

import java.util.ArrayList;
import java.util.List;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

import jaredbgreat.dldungeons.nbt.NBTType;
import jaredbgreat.dldungeons.parser.Tokenizer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class NBTGroup extends ITag {
	public final List<ITag> data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data.
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTGroup(String label, String name, String data) {
		super(label, name);
		this.data  = new ArrayList<ITag>();
		parseData(data);
	}

	@Override
	public void write(CompoundNBT cmp) {
		for(ITag child : data) {
			child.write(cmp);
		}
	}
	
	
	@Override
	public void write(ListNBT cmp) {
		for(ITag child : data) {
			child.write(cmp);
		}
	}
	
	
	private void parseData(String in) {
		Tokenizer tokens = new Tokenizer(in, ",");
		while(tokens.hasMoreTokens()) {
			data.add(Tags.registry.get(tokens.nextToken()));
		}
	}


	@Override
	public NBTType getType() {
		return NBTType.GROUP;
	}
}
