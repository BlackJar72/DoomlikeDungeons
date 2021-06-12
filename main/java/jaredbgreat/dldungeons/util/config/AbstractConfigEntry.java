package jaredbgreat.dldungeons.util.config;

@SuppressWarnings("rawtypes")
public abstract class AbstractConfigEntry<T> implements IConfigEntry<T>, Comparable<AbstractConfigEntry> {
	public static final String COMMENT = "# ";
	public static final String DEFAULT = "# [Default: ";
	public static final String MIN     = ", Minimum: ";
	public static final String MAX     = ", Maximum: ";
	public static final String INDENT  = "     ";
	protected final String key;
	protected String[] comment;
	protected T value, base;
	
	
	public AbstractConfigEntry(String key) {
		this.key = key;
	}
	
	
	public void setDefault() {
		value = base;
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

}
