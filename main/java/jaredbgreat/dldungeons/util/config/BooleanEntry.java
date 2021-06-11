package jaredbgreat.dldungeons.util.config;

public final class BooleanEntry extends AbstractConfigEntry<Boolean> {
	private static final String B = "B:";
	Boolean base;
	boolean value;

	
	public BooleanEntry(String key) {
		super(key);
	}
	
	
	@Override
	public void readIn(String string) {
		value = Boolean.parseBoolean(string);
	}
	
	
	public void attachData(boolean base, String ... comment) {
		this.base = base;
		this.comment = comment;
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
			b.append(']');
			b.append(System.lineSeparator());
		}
		b.append(B);
		b.append('=');
		b.append(value);
		b.append(System.lineSeparator());
		return b.toString();
	}



	@Override
	public Boolean getValue() {
		return value;
	}
	
}
