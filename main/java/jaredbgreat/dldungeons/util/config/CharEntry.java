package jaredbgreat.dldungeons.util.config;

final class CharEntry extends AbstractConfigEntry<Character> {
	static final String C = "C:";

	public CharEntry(String key) {
		super(key);
	}

	
	@Override
	public void readIn(String string) {
		value = string.charAt(0);
		good = true;
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
			b.append(']');
			b.append(System.lineSeparator());
		}
		b.append(INDENT);
		b.append(C);
		b.append(key);
		b.append('=');
		b.append(value);
		b.append(System.lineSeparator());
		return b.toString();
	}

	
	@Override
	public Character getValue() {
		return value;
	}

	
	@Override
	public String getTypeCode() {
		return C;
	}

}
