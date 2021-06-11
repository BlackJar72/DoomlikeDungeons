package jaredbgreat.dldungeons.util.config;

public final class LongEntry extends AbstractNumericEntry<Long> {
	private static final String L = "L:";

	@Override
	public void readIn(String string) {
		value = Long.parseLong(string);
	}
	
	
	public LongEntry(String key) {
		super(key);
	}
		
	
	@Override
	public String getConfigString() {
		StringBuilder b = new StringBuilder(System.lineSeparator());
		for(String c : comment) {
			b.append(COMMENT);
			b.append(c);
			b.append(System.lineSeparator());			
		}
		if(base != null) {
			b.append(DEFAULT);
			b.append(base);
			b.append(MIN);
			b.append(min);
			b.append(MAX);
			b.append(max);
			b.append(']');
			b.append(System.lineSeparator());
		}
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
	
}
