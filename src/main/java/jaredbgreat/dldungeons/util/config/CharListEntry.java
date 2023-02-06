package jaredbgreat.dldungeons.util.config;

import java.util.ArrayList;

final class CharListEntry extends AbstractListEntry<Character> {

	public CharListEntry(String key) {
		super(key);
	}

	
	@Override
	public String getConfigString() {
		StringBuilder b1 = new StringBuilder(System.lineSeparator());
		StringBuilder b2 = new StringBuilder(System.lineSeparator());
		if(comment != null) {
			for(String c : comment) {
				b1.append(INDENT);
				b1.append(COMMENT);
				b1.append(c);
				b1.append(System.lineSeparator());
			}
		}
		b2.append(INDENT);
		b2.append(CharEntry.C);
		b2.append(key);
		b2.append(' ');
		b2.append(buildDataString(b2.length()));
		b1.append(b2);
		b1.append(System.lineSeparator());		
		return b1.toString();	
	}

	
	@Override
	public String getTypeCode() {
		return CharEntry.C;
	}
	

	@Override
	protected Character parseString(String string) {
		return string.charAt(0);
	}
	
	
	public void setValue(char[] val) {
		value = new ArrayList<>(val.length);
		for(char element : val) {
			value.add(element);
		}
	}
	
	
	public void setDefault(char[] val) {
		base = new ArrayList<>(val.length);
		for(char element : val) {
			base.add(element);
		}
	}
	
	
	public void setDefaultValue(char[] val) {
		base = new ArrayList<>(val.length);
		for(char element : val) {
			base.add(element);
		}
		value = base;
	}

}
