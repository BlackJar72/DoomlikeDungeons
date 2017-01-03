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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class Enchantment extends ITag {
	private short id;
	private short lvl;

	protected Enchantment(String label, String name, String data) {
		super(label, name);
		Tokenizer tokens = new Tokenizer(data, ",");
		id  = Short.parseShort(tokens.getToken(0));
		lvl = Short.parseShort(tokens.getToken(1));
	}

	@Override
	public void write(NBTTagCompound cmp) {
        if (!cmp.hasKey("ench", 9)) {
            cmp.setTag("ench", new NBTTagList());
        }
        NBTTagList list = cmp.getTagList("ench", 10);
        NBTTagCompound sub = new NBTTagCompound();
        sub.setShort("id", id);
        sub.setShort("lvl", lvl);
        list.appendTag(sub);
    }

	@Override
	public void write(NBTTagList in) {
        NBTTagCompound sub = new NBTTagCompound();
        sub.setShort("id", id);
        sub.setShort("lvl", lvl);
        in.appendTag(sub);
    }

	@Override
	public NBTType getType() {
		return NBTType.COMPOUND; // What it really is
	}

}
