package jaredbgreat.dldungeons.util.config;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

class ConfigCategory implements Comparable<ConfigCategory> {
	private static final String COMMENT = "# ";
	private static final String BOUND   = "##################################################################";
	private static final String START   = " {";
	private static final String END     = "}";
	private static final String NLINE   = "\n\r";
	public final String name;
	@SuppressWarnings("rawtypes")
	final Map<String, AbstractConfigEntry> data;
	private String[] comment = {""};
	
	
	
	public ConfigCategory(String name) {
		this.name = name;
		data = new HashMap<>();
	}
	
	
	public ConfigCategory setComment(String ... lines) {
		if(lines.length == 1) {
			setCommentSingle(lines[0]);
		} else {
			comment = lines;
		}
		return this;
	}
	
	
	public ConfigCategory setCommentSingle(String text) {
		StringTokenizer lines = new StringTokenizer(text, NLINE);
		comment = new String[lines.countTokens()];
		for(int i = 0; i < comment.length; i++) {
			comment[i] = lines.nextToken();
		}
		return this;
	}
	
	
	@SuppressWarnings("rawtypes")
	public void add(AbstractConfigEntry entry) {
		if(!data.containsKey(entry.getKey())) {
			data.put(entry.getKey(), entry);
		}
		if(!entry.sameCategory(this)) {
			ConfigCategory other = entry.getCategory();
			entry.setCategory(this);
			if(other != null) {
				other.remove(entry);
			}
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	private void remove(AbstractConfigEntry entry) {
		data.remove(entry.getKey());
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
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void writeToFile(BufferedWriter outstream) throws IOException {
		ArrayList<AbstractConfigEntry> list = new ArrayList<>();
		data.forEach((key,value) -> list.add(value));
		Collections.sort(list);
		outstream.newLine();
		outstream.write(BOUND);
		outstream.newLine();
		outstream.write(COMMENT);
		outstream.write(name);
		outstream.newLine();
		outstream.newLine();
		for(int i = 0; i < comment.length; i++) {
			outstream.write(COMMENT);
			outstream.write(comment[i]);
			outstream.newLine();
		}
		outstream.write(BOUND);
		outstream.newLine();
		outstream.write(name);
		outstream.write(START);
		int size = list.size();
		for(int i = 0; i < size; i++) {
			outstream.write(list.get(i).getConfigString());
			outstream.newLine();
		}
		outstream.write(END);
		outstream.newLine();
	}
}