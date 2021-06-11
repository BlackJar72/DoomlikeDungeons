package jaredbgreat.dldungeons.util.config;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigCategory implements Comparable<ConfigCategory> {
	private static final String COMMENT = "# ";
	private static final String BOUND = "##################################################################";
	private static final String START = " {";
	private static final String END = "}";
	public final String name;
	@SuppressWarnings("rawtypes")
	public final List<AbstractConfigEntry> data;
	
	
	public ConfigCategory(String name) {
		this.name = name;
		data = new ArrayList<>();
	}
	
	
	@SuppressWarnings("rawtypes")
	public void add(AbstractConfigEntry entry) {
		if(!data.contains(entry)) {
			data.add(entry);
		}
	}
	
	
	@Override
	public int compareTo(ConfigCategory o) {
		return name.compareTo(o.name);
	}
	
	
	@Override	
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override public boolean equals(Object o) {
		if(o instanceof ConfigCategory) {
			return name.equals(((ConfigCategory)o).name);
		}
		return false;
	}
	
	
	@SuppressWarnings("unchecked")
	public void sort() {
		Collections.sort(data);
	}
	
	
	public void writeToFile(BufferedWriter outstream) throws IOException {
		sort();
		outstream.newLine();
		outstream.write(BOUND);
		outstream.write(COMMENT);
		outstream.write(name);
		outstream.write(BOUND);
		outstream.write(name);
		outstream.write(START);
		int size = data.size();
		int last = size - 1;
		for(int i = 0; i < size; i++) {
			outstream.write(data.get(i).getConfigString());
			if(i == last) {
				outstream.write(END);
			}
			outstream.newLine();
		}
	}
}