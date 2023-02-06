package jaredbgreat.dldungeons.util.config;

final class StringEntry extends AbstractConfigEntry<String> {
	static final String S = "S:";
	
	
	public StringEntry(String key) {
		super(key);
	}
	
	
	public void attachData(String base, String ... comment) {
		this.base = base;
		if(comment.length == 1) {
			setCommentSingle(comment[0]);
		} else {
			this.comment = comment;
		}
	}
	

	@Override
	public void readIn(String string) {
		value = string;
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
		b.append(S);
		b.append(key);
		b.append('=');
		b.append(value);
		return b.toString();
	}
	

	@Override
	public String getValue() {
		return value;
	}


	@Override
	public String getTypeCode() {
		return S;
	}

}
