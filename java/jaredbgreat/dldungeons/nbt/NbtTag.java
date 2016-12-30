package jaredbgreat.dldungeons.nbt;

public class NbtTag {
	NbtType type;
	String name;
	String data;
		
	public NbtTag(String type, String name, String data) {
		this.type = NbtType.valueOf(type.toUpperCase());
		this.name = name;
		this.data = data;
	}
}
