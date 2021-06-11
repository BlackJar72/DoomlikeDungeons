package jaredbgreat.dldungeons.util.config;

public final class FloatEntry extends AbstractNumericEntry<Float> {
	private static final String F = "F:";

	@Override
	public void readIn(String string) {
		value = Float.parseFloat(string);
	}
	
	
	public FloatEntry(String key) {
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
		b.append(F);
		b.append('=');
		b.append(value);
		b.append(System.lineSeparator());
		return b.toString();
	}
	
}
