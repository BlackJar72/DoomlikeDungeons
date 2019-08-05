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

public class NBTList extends ITag {
	private final List<ITag> data;  // The data carried by the tag in the NBT
	private final int type;         // Type of data
	
	
	/**
	 * The constructor for use with text data.
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTList(String label, String name, String data) {
		super(label, name);
		this.data  = new ArrayList<ITag>();
		parseData(data);
		type = this.data.get(0).getType().ordinal();
	}
	

	@Override
	public void write(CompoundNBT in) {
        if (!in.contains(name)) {
            in.put(name, new ListNBT());
        }	
        ListNBT list;
		for(ITag child : data) {
			list = in.getList(name, child.getType().ordinal());
			child.write(list);
		}
	}
	
	
	private void writeHelper(CompoundNBT in, ITag child) {
	}
	
	
	@Override
	public void write(ListNBT in) {
		for(ITag child : data) {
			child.write(in);
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
		return NBTType.LIST;
	}
}
