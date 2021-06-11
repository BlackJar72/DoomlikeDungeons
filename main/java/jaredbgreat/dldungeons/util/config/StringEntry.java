package jaredbgreat.dldungeons.util.config;

public final class StringEntry extends AbstractConfigEntry<String> {
	private static final String B = "B:";
	private String value, base;
	private String[] comment;
	
	
	public StringEntry(String key) {
		super(key);
	}
	
	
	public void attachData(String base, String ... comment) {
		this.base = base;
		this.comment = comment;
	}
	

	@Override
	public void readIn(String string) {
		value = string;
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
		return b.toString();
	}
	

	@Override
	public String getValue() {
		return value;
	}

}
