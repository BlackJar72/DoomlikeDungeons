package jaredbgreat.dldungeons.nbt.tags;

import jaredbgreat.dldungeons.nbt.NBTType;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTFromJsonWrapper extends ITag {
	private NBTTagCompound wrapped;

	NBTFromJsonWrapper(String label, String data) {
		super(label, label);
		try {
			wrapped = JsonToNBT.getTagFromJson(data);
		} catch (NBTException e) {
			System.err.println("Exception reading json-nbt string: " + e.getMessage());
			wrapped = new NBTTagCompound();
		}
	}

	@Override
	public void write(NBTTagCompound cmp) {
		cmp.merge(wrapped);
	}

	@Override
	public void write(NBTTagList in) {
		in.appendTag(wrapped.copy());
	}

	@Override
	public NBTType getType() {
		return NBTType.COMPOUND;
	}
}
