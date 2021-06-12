package jaredbgreat.dldungeons.util.config;

public abstract class AbstractNumericEntry<T extends Number> extends AbstractConfigEntry<T> {
	protected T min, max;
	

	public AbstractNumericEntry(String key) {
		super(key);
	}
	
	
	@Override
	public T getValue() {
		return value;
	}
	
	
	public void attachData(T base, T min, T max, String ... comment) {
		this.base = base;
		this.min  = min;
		this.max  = max;
		this.comment = comment;
	}

}
