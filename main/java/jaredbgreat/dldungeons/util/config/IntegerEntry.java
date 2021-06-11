package jaredbgreat.dldungeons.util.config;

public final class IntegerEntry extends AbstractNumericEntry<Integer> {
	private static final String I = "I:";

	@Override
	public void readIn(String string) {
		value = Integer.parseInt(string);
	}
	
	
	public IntegerEntry(String key) {
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
		b.append(I);
		b.append('=');
		b.append(value);
		b.append(System.lineSeparator());
		return b.toString();
	}

}
