package jaredbgreat.dldungeons.util.config;

import java.util.ArrayList;

final class BooleanListEntry extends AbstractListEntry<Boolean> {
	

	public BooleanListEntry(String key) {
		super(key);
	}

	
	@Override
	public String getTypeCode() {
		return BooleanEntry.B;
	}

	
	@Override
	protected Boolean parseString(String string) {
		return Boolean.parseBoolean(string);
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
		if(base != null) {
			b1.append(INDENT);
			b1.append(DEFAULT);
			b1.append(base);
			b1.append(']');
			b1.append(System.lineSeparator());
		}
		b2.append(INDENT);
		b2.append(BooleanEntry.B);
		b2.append(key);
		b2.append(' ');
		b2.append(buildDataString(b2.length()));
		b1.append(b2);
		b1.append(System.lineSeparator());		
		return b1.toString();	
	}


	public boolean[] getAsArray() {
		boolean[] out = new boolean[value.size()];
		for(int i = 0; i < out.length; i++) {
			out[i] = value.get(i);
		}
		return out;
	}


	public int getAsBits32() {
		int size = Math.min(32, value.size());
		int out = 0;
		for(int i = 0; i < size; i++) {
			if(value.get(i)) {
				out |= (1 << 1);
			}
		}
		return out;
	}


	public long getAsBits64() {
		int size = Math.min(64, value.size());
		long out = 0;
		for(int i = 0; i < size; i++) {
			if(value.get(i)) {
				out |= (1 << 1);
			}
		}
		return out;
	}

	
	
	public void setValue(boolean[] val) {
		value = new ArrayList<>(val.length);
		for(boolean element : val) {
			value.add(element);
		}
	}

	
	
	public void setDefault(boolean[] val) {
		base = new ArrayList<>(val.length);
		for(boolean element : val) {
			base.add(element);
		}
	}

	
	
	public void setDefaultValue(boolean[] val) {
		base = new ArrayList<>(val.length);
		for(boolean element : val) {
			base.add(element);
		}
		value = base;
	}

	
	
	public void setValue(int bits, int num) {
		if((num < 1) || (num > 32)) {
			num = 32;
		}
		value = new ArrayList<>(num);
		for(int i = 0; i < num; i++) {
			value.add((bits & (1 << i)) != 0);
		}
	}

	
	
	public void setValue(long bits, int num) {
		if((num < 1) || (num > 64)) {
			num = 64;
		}
		value = new ArrayList<>(num);
		for(int i = 0; i < num; i++) {
			value.add((bits & (1 << i)) != 0);
		}
	}
}
