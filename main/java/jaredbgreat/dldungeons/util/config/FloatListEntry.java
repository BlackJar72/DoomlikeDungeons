package jaredbgreat.dldungeons.util.config;

import java.util.ArrayList;

final class FloatListEntry extends AbstractListEntry<Float> {

	public FloatListEntry(String key) {
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
		if(base != null) {
			b1.append(INDENT);
			b1.append(DEFAULT);
			b1.append(base);
			b1.append(']');
			b1.append(System.lineSeparator());
		}
		b2.append(INDENT);
		b2.append(FloatEntry.F);
		b2.append(key);
		b2.append(' ');
		b2.append(buildDataString(b2.length()));
		b1.append(b2);
		b1.append(System.lineSeparator());		
		return b1.toString();	
	}

	
	@Override
	public String getTypeCode() {
		return FloatEntry.F;
	}

	
	@Override
	protected Float parseString(String string) {
		return Float.parseFloat(string);
	}


	public float[] getAsArray() {
		float[] out = new float[value.size()];
		for(int i = 0; i < out.length; i++) {
			out[i] = value.get(i);
		}
		return out;
	}
	
	
	public void setValue(float[] val) {
		value = new ArrayList<>(val.length);
		for(float element : val) {
			value.add(element);
		}
	}
	
	
	public void setDefault(float[] val) {
		base  = new ArrayList<>(val.length);
		for(float element : val) {
			value.add(element);
		}
	}
	
	
	public void setDefaultValue(float[] val) {
		base  = new ArrayList<>(val.length);
		for(float element : val) {
			value.add(element);
		}
		value = base;
	}

}
