package jaredbgreat.dldungeons.util.config;

public interface IConfigEntry<T> {	
	public void readIn(String string);
	public String getConfigString();
	public T getValue();
	public String getKey();
	public String getTypeCode();
	public void setValue(T val);
}
