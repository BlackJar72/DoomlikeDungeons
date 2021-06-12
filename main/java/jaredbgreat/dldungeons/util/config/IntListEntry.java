package jaredbgreat.dldungeons.util.config;

import java.util.ArrayList;

public final class IntListEntry extends AbstractListEntry<Integer> {

	public IntListEntry(String key) {
		super(key);
	}

	
	@Override
	public String getConfigString() {
		StringBuilder b1 = new StringBuilder(System.lineSeparator());
		StringBuilder b2 = new StringBuilder(System.lineSeparator());
		for(String c : comment) {
			b1.append(INDENT);
			b1.append(COMMENT);
			b1.append(c);
			b1.append(System.lineSeparator());			
		}
		if(base != null) {
			b1.append(INDENT);
			b1.append(DEFAULT);
			b1.append(base);
			b1.append(']');
			b1.append(System.lineSeparator());
		}
		b2.append(INDENT);
		b2.append(IntEntry.I);
		b2.append(key);
		b2.append('=');
		b2.append(buildDataString(b2.length()));
		b1.append(b2);
		b1.append(System.lineSeparator());		
		return b1.toString();	
	}

	
	@Override
	public String getTypeCode() {
		return IntEntry.I;
	}

	
	@Override
	protected Integer parseString(String string) {
		return Integer.parseInt(string);
	}


	public int[] getAsArray() {
		int[] out = new int[value.size()];
		for(int i = 0; i < out.length; i++) {
			out[i] = value.get(i);
		}
		return out;
	}
	
	
	public void setValue(int[] val) {
		value = new ArrayList<>(val.length);
		for(int element : val) {
			value.add(element);
		}
	}

}
