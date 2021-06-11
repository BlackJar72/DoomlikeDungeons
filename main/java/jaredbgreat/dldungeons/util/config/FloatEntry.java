package jaredbgreat.dldungeons.util.config;

public class FloatEntry extends AbstractNumericEntry<Float> {
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
			b.append(INDENT);
			b.append(COMMENT);
			b.append(c);
			b.append(System.lineSeparator());			
		}
		if(base != null) {
			b.append(INDENT);
			b.append(DEFAULT);
			b.append(base);
			b.append(MIN);
			b.append(min);
			b.append(MAX);
			b.append(max);
			b.append(']');
			b.append(System.lineSeparator());
		}
		b.append(INDENT);
		b.append(key);
		b.append('=');
		b.append(value);
		b.append(System.lineSeparator());
		return b.toString();
	}


	@Override
	public String getTypeCode() {
		return F;
	}
	
}
