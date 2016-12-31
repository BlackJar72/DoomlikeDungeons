package jaredbgreat.dldungeons.nbt;

public class NBTData {
	NbtType type;
	String name;
	String data;
		
	public NBTData(String type, String name, String data) {
		this.type = NbtType.valueOf(type.toUpperCase());
		this.name = name;
		this.data = data;
	}
}
