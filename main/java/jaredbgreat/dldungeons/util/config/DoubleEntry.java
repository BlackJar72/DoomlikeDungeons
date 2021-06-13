package jaredbgreat.dldungeons.util.config;

final class DoubleEntry extends AbstractNumericEntry<Double> {
	static final String D = "D:";
	
	
	public DoubleEntry(String key) {
		super(key);
		min = Double.NEGATIVE_INFINITY;
		max = Double.POSITIVE_INFINITY;
	}

	
	@Override
	public void readIn(String string) {
		try {
			value = Double.parseDouble(string);
			good = true;
		} catch(Exception e) {
			value = Double.NaN;
		}
	}
	
	
	public void attachData(double base, double min, double max) {
		super.attachData(base, min, max);
		base   = Math.min(max, Math.max(min, base));
		value  = Math.min(max, Math.max(min, value));
	}
		
	
	@Override
	public String getConfigString() {
		StringBuilder b = new StringBuilder(System.lineSeparator());
		if(comment != null) { 
			for(String c : comment) {
				b.append(INDENT);
				b.append(COMMENT);
				b.append(c);
				b.append(System.lineSeparator());
			}
		}
		if(base != null) {
			b.append(INDENT);
			b.append(DEFAULT);
			b.append(base);
			if(min > Double.NEGATIVE_INFINITY) {
				b.append(MIN);
				b.append(min);
			}
			if(max < Double.POSITIVE_INFINITY) {
				b.append(MAX);
				b.append(max);
			}
			b.append(']');
			b.append(System.lineSeparator());
		}
		b.append(INDENT);
		b.append(D);
		b.append(key);
		b.append('=');
		b.append(value);
		b.append(System.lineSeparator());
		return b.toString();
	}


	@Override
	public String getTypeCode() {
		return D;
	}
	
	
	@Override
	public void setValue(Double val) {
		value = Math.min(max, Math.max(min, val));
	}
	
	
	public void setDefault(double def) {
		base = Math.min(max, Math.max(min, def));
	}
	
	
	public void setDefaultValue(double def) {
		value = base = Math.min(max, Math.max(min, def));
	}
	
}
