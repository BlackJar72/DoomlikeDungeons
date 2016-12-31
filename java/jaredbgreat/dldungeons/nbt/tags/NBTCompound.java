package jaredbgreat.dldungeons.nbt.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.minecraft.nbt.NBTTagCompound;

public class NBTCompound implements ITag {
	public final String     label; // The label to uniquely identify the tag
	public final String     name;  // The name / label of the tag in NBT
	public final List<ITag> data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data.
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTCompound(String label, String name, String data) {
		this.label = label;
		this.name  = name;
		this.data  = new ArrayList<ITag>();
	}

	@Override
	public void write(NBTTagCompound cmp) {
		NBTTagCompound out = cmp.getCompoundTag(name);
		for(ITag child : data) {
			child.write(out);
		}
	}
	
	
	private void parseData(String in) {
		StringTokenizer tokens = new StringTokenizer(in, ",");
		while(tokens.hasMoreTokens()) {
			data.add(Tags.registry.get(tokens.nextToken()));
		}
	}
}
