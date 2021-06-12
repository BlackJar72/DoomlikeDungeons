package jaredbgreat.dldungeons.util.config;

public final class IntEntry extends AbstractNumericEntry<Integer> {
	static final String I = "I:";
	
	
	public IntEntry(String key) {
		super(key);
		min = Integer.MIN_VALUE;
		max = Integer.MAX_VALUE;
	}
	

	@Override
	public void readIn(String string) {
		value = Integer.parseInt(string);
	}
	
	
	public void attachData(int base, int min, int max, String ... comment) {
		super.attachData(base, min, max, comment);
		value = Math.min(max, Math.max(min, value));
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
			if(min > Integer.MIN_VALUE) {
				b.append(MIN);
				b.append(min);
			}
			if(max < Integer.MAX_VALUE) {
				b.append(MAX);
				b.append(max);
			}
			b.append(']');
			b.append(System.lineSeparator());
		}
		b.append(INDENT);
		b.append(I);
		b.append(key);
		b.append('=');
		b.append(value);
		b.append(System.lineSeparator());
		return b.toString();
	}


	@Override
	public String getTypeCode() {
		return I;
	}
	
	
	@Override
	public void setValue(Integer val) {
		value = Math.min(max, Math.max(min, val));
	}

}
