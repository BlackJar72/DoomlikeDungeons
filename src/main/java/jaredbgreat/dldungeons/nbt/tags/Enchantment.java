package jaredbgreat.dldungeons.nbt.tags;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.CompoundNBT;

public class Enchantment extends ITag {
	private short id;
	private short lvl;

	protected Enchantment(String label, String name, String data) {
		super(label, name);
		id = Short.parseShort(name);
		lvl = Short.parseShort(data);
	}

	@Override
	public void write(CompoundNBT cmp) {
        if (!cmp.contains("ench")) {
            cmp.setTag("ench", new NBTList());
        }
        NBTList list = cmp.getList("ench", 10);
        CompoundNBT sub = new CompoundNBT();
        sub.setShort("id", id);
        sub.setShort("lvl", lvl);
        list.add(sub);
    }

	@Override
	public void write(NBTList in) {
        CompoundNBT sub = new CompoundNBT();
        sub.setShort("id", id);
        sub.setShort("lvl", lvl);
        in.add(sub);
    }

	@Override
	public NBTType getType() {
		return NBTType.COMPOUND; // What it really is
	}

}
