package jaredbgreat.dldungeons.util.config;

import java.util.StringTokenizer;

@SuppressWarnings("rawtypes")
abstract class AbstractConfigEntry<T> implements IConfigEntry<T>, Comparable<AbstractConfigEntry> {
	public static final String EMPTY   = "";
	public static final String COMMENT = "# ";
	public static final String DEFAULT = "# [Default: ";
	public static final String MIN     = ", Minimum: ";
	public static final String MAX     = ", Maximum: ";
	public static final String INDENT  = "     ";
	public static final String NLINE   = "\n\r";
	public static final String[] EMPTYA = new String[0];
	protected final String key;
	protected String[] comment;
	protected T value, base;
	protected Boolean good;
	protected ConfigCategory category;
	
	
	public AbstractConfigEntry(String key) {
		this.key = key;
		good = false;
	}
	
	
	public void setCategory(ConfigCategory category) {
		this.category = category;
	}
	
	
	public ConfigCategory getCategory() {
		return category;
	}
	
	
	public boolean sameCategory(ConfigCategory category) {
		return this.category == category;
	}
	
	
	public void makeDefault() {
		value = base;
	}
	
	
	public void setDefault(T def) {
		base = def;
	}
	
	
	public void setDefaultValue(T def) {
		value = base = def;
	}
	
	
	@Override
	public String getKey() {
		return key;
	}
	
	
	@Override
	public int hashCode() {
		return key.hashCode();
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof AbstractConfigEntry) {
			return key.equals(((AbstractConfigEntry)o).key);
		}
		return false;
	}


	@Override
	public int compareTo(AbstractConfigEntry o) {		
		return key.compareTo(o.key);
	}
	
	
	@Override
	public void setValue(T val) {
		value = val;
	}
	
	
	public AbstractConfigEntry<T> setComment(String ... lines) {
		if(lines.length == 1) {
			setCommentSingle(lines[0]);
		} else {
			comment = lines;
		}
		return this;
	}
	
	
	public AbstractConfigEntry<T> setCommentSingle(String text) {
		StringTokenizer lines = new StringTokenizer(text, NLINE);
		comment = new String[lines.countTokens()];
		for(int i = 0; i < comment.length; i++) {
			comment[i] = lines.nextToken();
		}
		return this;
	}
	
	
	@Override
	public boolean isGood() {
		return good;
	}

}
