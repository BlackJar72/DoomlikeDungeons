package jaredbgreat.dldungeons.util.config;

public final class StringListEntry extends AbstractListEntry<String> {

	public StringListEntry(String key) {
		super(key);
	}

	
	@Override
	public String getConfigString() {
		StringBuilder b1 = new StringBuilder(System.lineSeparator());
		StringBuilder b2 = new StringBuilder(System.lineSeparator());
		if(comment != null) {
			for(String c : comment) {
				b1.append(INDENT);
				b1.append(COMMENT);
				b1.append(c);
				b1.append(System.lineSeparator());
			}
		}
		if(base != null) {
			b1.append(INDENT);
			b1.append(DEFAULT);
			b1.append(base);
			b1.append(']');
			b1.append(System.lineSeparator());
		}
		b2.append(INDENT);
		b2.append(StringEntry.S);
		b2.append(key);
		b2.append(' ');
		b2.append(buildDataString(b2.length()));
		b1.append(b2);
		b1.append(System.lineSeparator());		
		return b1.toString();	
	}

	
	@Override
	public String getTypeCode() {
		return StringEntry.S;
	}

	
	@Override
	protected String parseString(String string) {
		return string;
	}


	public String[] getAsArray() {
		String[] out = new String[value.size()];
		for(int i = 0; i < out.length; i++) {
			out[i] = value.get(i);
		}
		return out;
	}

}
