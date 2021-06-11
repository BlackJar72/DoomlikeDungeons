package jaredbgreat.dldungeons.util.config;

@SuppressWarnings("rawtypes")
public abstract class AbstractConfigEntry<T> implements IConfigEntry<T>, Comparable<AbstractConfigEntry> {
	public static final String COMMENT = "# ";
	public static final String DEFAULT = "# [Default: ";
	public static final String MIN     = ", Minimum: ";
	public static final String MAX     = ", Maximum: ";
	protected final String key;
	protected String[] comment;
	
	
	public AbstractConfigEntry(String key) {
		this.key = key;
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
		if(o.getClass() == getClass()) {
			return key.equals(((AbstractConfigEntry)o).key);
		}
		return false;
	}


	@Override
	public int compareTo(AbstractConfigEntry o) {		
		return key.compareTo(o.key);
	}

}
