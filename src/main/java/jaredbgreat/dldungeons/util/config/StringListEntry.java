package jaredbgreat.dldungeons.util.config;

final class StringListEntry extends AbstractListEntry<String> {

	public StringListEntry(String key) {
		super(key);
	}

	
	@Override
	public String getConfigString() {
		StringBuilder b1 = new StringBuilder(System.lineSeparator());
		StringBuilder b2 = new StringBuilder();
		if(comment != null) {
			for(String c : comment) {
				b1.append(INDENT);
				b1.append(COMMENT);
				b1.append(c);
				b1.append(System.lineSeparator());
			}
		}
		if((base != null) && (base.toString().length() < 45)) {
			b1.append(INDENT);
			b1.append(DEFAULT);
			b1.append(base);
			b1.append(' ');
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
	
	
	protected String buildDataString(int offset) {		
		int len = offset;
		String current;
		StringBuilder spaceb  = new StringBuilder();
		for(int i = 0; i < offset; i++) {
			spaceb.append(" ");
		}
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if(value != null) {
			for(int i = 0; i < value.size(); i++) {
				current = value.get(i).toString();
				if(((len + current.length() + 2) < WIDTH) || (len == offset)) {
					builder.append('"');
					builder.append(current);
					builder.append('"');
					len += current.length() + 2;
				} else {
					builder.append(System.lineSeparator());
					builder.append(spaceb);
					builder.append('"');
					builder.append(current);
					builder.append('"');
					len = offset + current.length() + 2;
				}
				if(i < (value.size() -1)) {
					builder.append(DELIM2);
				}
			}
		}
		builder.append(']');
		return builder.toString();
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


	public String getAsSingleBlock() {
		StringBuilder builder = new StringBuilder();
		for(String line : value) {
			builder.append(line);
			builder.append(System.lineSeparator());
		}
		return builder.toString();
	}


	public String getAsSingleLine() {
		StringBuilder builder = new StringBuilder();
		for(String line : value) {
			builder.append(line);
			builder.append(' ');
		}
		return builder.toString();
	}

}
