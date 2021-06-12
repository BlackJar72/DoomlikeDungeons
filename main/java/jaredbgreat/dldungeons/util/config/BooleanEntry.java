package jaredbgreat.dldungeons.util.config;

import java.util.ArrayList;

public final class BooleanEntry extends AbstractConfigEntry<Boolean> {
	static final String B = "B:";

	
	public BooleanEntry(String key) {
		super(key);
	}
	
	
	@Override
	public void readIn(String string) {
		value = Boolean.parseBoolean(string);
	}
	
	
	public void attachData(boolean base, String ... comment) {
		this.base = base;
		this.comment = comment;
	}
	
		
	
	@Override
	public String getConfigString() {
		StringBuilder b = new StringBuilder(System.lineSeparator());
		for(String c : comment) {
			b.append(INDENT);
			b.append(COMMENT);
			b.append(c);
			b.append(System.lineSeparator());			
		}
		if(base != null) {
			b.append(INDENT);
			b.append(DEFAULT);
			b.append(base);
			b.append(']');
			b.append(System.lineSeparator());
		}
		b.append(INDENT);
		b.append(key);
		b.append('=');
		b.append(value);
		b.append(System.lineSeparator());
		return b.toString();
	}



	@Override
	public Boolean getValue() {
		return value;
	}


	@Override
	public String getTypeCode() {
		return B;
	}
	
}
