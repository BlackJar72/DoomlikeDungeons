package jaredbgreat.dldungeons.nbt.tags;

import jaredbgreat.dldungeons.nbt.NBTType;

import java.util.ArrayList;
import java.util.StringTokenizer;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTByteArray extends ITag {
	public final byte[]    data;  // The data carried by the tag in the NBT
	
	
	/**
	 * The constructor for use with text data; it will convert a text based 
	 * value to a int array. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTByteArray(String label, String name, String data) {
		super(label, name);
		// The use of the array list is needed to process, but should not
		// effect performance as this should be done during loading, and 
		// never at run time.
		ArrayList<Byte> tmp = parseData(data);
		this.data = new byte[tmp.size()];
		for(int i = 0; i < this.data.length; i++) {
			this.data[i] = tmp.get(i);
		}
	}
	
	
	/**
	 * The constructor for use when a byte array is already available. 
	 * 
	 * @param label
	 * @param name
	 * @param data
	 */
	NBTByteArray(String label, String name, byte[] data) {
		super(label, name);
		this.data = data;
	}
	
	
	@Override
	public void write(NBTTagCompound cmp) {
		cmp.setByteArray(name, data);
	}
	
	
	@Override
	public void write(NBTTagList cmp) {
		cmp.appendTag(new NBTTagByteArray(data));
	}
	
	
	/**
	 * This will parse the data from an array written as text into a
	 * (temporary) ArrayList of integers. 
	 * 
	 * @param in
	 * @return
	 */
	private ArrayList<Byte> parseData(String in) {
		StringTokenizer tokens = new StringTokenizer(in, "{,}");
		ArrayList<Byte> tmp = new ArrayList<Byte>();
		while(tokens.hasMoreTokens()) {
			tmp.add(Byte.parseByte(tokens.nextToken()));
		}
		return tmp;
	}


	@Override
	public NBTType getType() {
		return NBTType.BYTE_ARRAY;
	}
}
