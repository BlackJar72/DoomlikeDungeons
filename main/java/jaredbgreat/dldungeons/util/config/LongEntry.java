package jaredbgreat.dldungeons.util.config;

public final class LongEntry extends AbstractNumericEntry<Long> {
	static final String L = "L:";
	
	
	public LongEntry(String key) {
		super(key);
		min = Long.MIN_VALUE;
		max = Long.MAX_VALUE;
	}
	

	@Override
	public void readIn(String string) {
		try {
			value = Long.parseLong(string);
		} catch(Exception e) {
			value = 0L;
		}
	}
	
	
	public void attachData(long base, long min, long max) {
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
			if(min > Long.MIN_VALUE) {
				b.append(MIN);
				b.append(min);
			}
			if(max < Long.MAX_VALUE) {
				b.append(MAX);
				b.append(max);
			}
			b.append(']');
			b.append(System.lineSeparator());
		}
		b.append(INDENT);
		b.append(L);
		b.append(key);
		b.append('=');
		b.append(value);
		b.append(System.lineSeparator());
		return b.toString();
	}


	@Override
	public String getTypeCode() {
		return L;
	}
	
	
	@Override
	public void setValue(Long val) {
		value = Math.min(max, Math.max(min, val));
	}
	
	
	public void setDefault(long def) {
		base = Math.min(max, Math.max(min, def));
	}
	
	
	public void setDefaultValue(long def) {
		value = base = Math.min(max, Math.max(min, def));
	}
	
}
