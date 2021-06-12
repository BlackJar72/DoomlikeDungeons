package jaredbgreat.dldungeons.util.config;

public final class HexEntry extends AbstractNumericEntry<Long> {
	static final String H = "H:";
	
	
	public HexEntry(String key) {
		super(key);
		min = Long.MIN_VALUE;
		max = Long.MAX_VALUE;
	}
	

	@Override
	public void readIn(String string) {
		try {
			value = Long.parseLong(string, 16);
		} catch(Exception e) {
			value = 0L;
		}
	}
	
	
	public void attachData(long base, long min, long max) {
		super.attachData(base, min, max);
		base  = Math.min(max, Math.max(min, base));
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
		b.append(H);
		b.append(key);
		b.append('=');
		b.append(Long.toHexString(value));
		b.append(System.lineSeparator());
		return b.toString();
	}


	@Override
	public String getTypeCode() {
		return H;
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


	public boolean[] toBooleanArray(int number) {
		if((number < 0) || (number > 64)) {
			number = 64;
		}
		long bits = value;
		boolean[] out = new boolean[number];
		for(int i = 0; i < out.length; i++) {
			out[i] = ((bits & (1 << i)) != 0); 
		}
		return out;
	}


	public long fromBooleanArray(boolean[] array) {
		int num = Math.min(64, array.length);
		long out = 0;
		for(int i = 0; i < num; i++) {
			if(array[i]) {
				out |= (1 << i);
			}
		}
		out = value;
		return out;
	}

}
