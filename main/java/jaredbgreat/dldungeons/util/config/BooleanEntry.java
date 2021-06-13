package jaredbgreat.dldungeons.util.config;

final class BooleanEntry extends AbstractConfigEntry<Boolean> {
	static final String B = "B:";

	
	public BooleanEntry(String key) {
		super(key);
	}
	
	
	@Override
	public void readIn(String string) {
		try {
			value = Boolean.parseBoolean(string);
			good = true;
		} catch(Exception e) {
			value = false;
		}
	}
	
	
	public void attachData(boolean base, String ... comment) {
		this.base = base;
		if(comment.length == 1) {
			setCommentSingle(comment[0]);
		} else {
			this.comment = comment;
		}
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
		b.append(B);
		b.append(key);
		b.append('=');
		b.append(value);
		b.append(System.lineSeparator());
		return b.toString();
	}



	@Override
	public Boolean getValue() {
		return value;
	}


	@Override
	public String getTypeCode() {
		return B;
	}
	
}
