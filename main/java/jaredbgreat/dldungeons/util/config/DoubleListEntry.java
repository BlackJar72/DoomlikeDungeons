package jaredbgreat.dldungeons.util.config;

import java.util.ArrayList;

public final class DoubleListEntry extends AbstractListEntry<Double> {

	public DoubleListEntry(String key) {
		super(key);
		// TODO Auto-generated constructor stub
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
		b2.append(DoubleEntry.D);
		b2.append(key);
		b2.append(' ');
		b2.append(buildDataString(b2.length()));
		b1.append(b2);
		b1.append(System.lineSeparator());		
		return b1.toString();	
	}

	
	@Override
	public String getTypeCode() {
		return DoubleEntry.D;
	}

	
	@Override
	protected Double parseString(String string) {
		return Double.parseDouble(string);
	}


	public double[] getAsArray() {
		double[] out = new double[value.size()];
		for(int i = 0; i < out.length; i++) {
			out[i] = value.get(i);
		}
		return out;
	}
	
	
	public void setValue(double[] val) {
		value = new ArrayList<>(val.length);
		for(double element : val) {
			value.add(element);
		}
	}
	
	
	public void setDefault(double[] val) {
		base  = new ArrayList<>(val.length);
		for(double element : val) {
			value.add(element);
		}
	}
	
	
	public void setDefaultValue(double[] val) {
		base  = new ArrayList<>(val.length);
		for(double element : val) {
			value.add(element);
		}
		value = base;
	}

}
