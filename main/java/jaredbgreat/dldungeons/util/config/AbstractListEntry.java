package jaredbgreat.dldungeons.util.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jaredbgreat.dldungeons.util.parser.Tokenizer;

public abstract class AbstractListEntry<T> extends AbstractConfigEntry<List<T>>{
	private static final String DELIM1 = ",\n\r";
	private static final String DELIM2 = ", ";
	private static final int WIDTH = 45;
	

	public AbstractListEntry(String key) {
		super(key);
	}

	
	@Override
	public void readIn(String string) {
		//try {
			value = new ArrayList<>();
			Tokenizer tokens = new Tokenizer(string, DELIM1);
			while(tokens.hasMoreTokens()) {
				value.add(parseString(tokens.nextToken().trim()));
			}
			good = true;
		//} catch(Exception e) {}
	}
	
	
	protected abstract T parseString(String string);
	
	
	protected String buildDataString(int offset) {		
		int len = offset;
		String current;
		StringBuilder spaceb  = new StringBuilder();
		for(int i = 0; i < offset; i++) {
			spaceb.append(" ");
		}
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if(value != null) {
			for(int i = 0; i < value.size(); i++) {
				current = value.get(i).toString();
				if(((len + current.length()) < WIDTH) || (len == offset)) {
					builder.append(current);
					len += current.length();
				} else {
					builder.append(System.lineSeparator());
					builder.append(spaceb);
					builder.append(current);
					len = offset + current.length();
				}
				if(i < (value.size() -1)) {
					builder.append(DELIM2);
				}
			}
		}
		builder.append(']');
		return builder.toString();
	}

	
	@Override
	public List<T> getValue() {
		return value;
	}
	
	
	public Set<T> getAsSet() {
		Set<T> out = new HashSet<>();
		for(T element : value) {
			out.add(element);
		}
		return out;
	}
	
	
	public void setValue(Set<T> val) {
		value = new ArrayList<>(val.size());
		val.forEach((element) -> value.add(element));
	}
	
	
	public void setValue(T[] val) {
		value = new ArrayList<>(val.length);
		for(T element : val) {
			value.add(element);
		}
	}
	
	
	public void setDefaultValue(T[] val) {
		base = new ArrayList<>(val.length);
		for(T element : val) {
			base.add(element);
		}
		value = base;
	}
	
	
	public void setDefaultValue(Set<T> val) {
		base = new ArrayList<>(val.size());
		val.forEach((element) -> base.add(element) );
		value = base;
	}
	
	
	@Override
	public boolean isGood() {
		return good;
	}

}
