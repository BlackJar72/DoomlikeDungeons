package jaredbgreat.dldungeons.util.config;

public class FloatEntry extends AbstractNumericEntry<Float> {
	static final String F = "F:";
	
	
	public FloatEntry(String key) {
		super(key);
		min = Float.NEGATIVE_INFINITY;
		max = Float.POSITIVE_INFINITY;
	}
	

	@Override
	public void readIn(String string) {
		value = Float.parseFloat(string);
	}
	
	
	public void attachData(float base, float min, float max) {
		super.attachData(base, min, max);
		value = Math.min(max, Math.max(min, value));
	}
		
	
	@Override
	public String getConfigString() {
		StringBuilder b = new StringBuilder(System.lineSeparator());
		for(String c : comment) {
			b.append(INDENT);
			b.append(COMMENT);
			b.append(c);
			b.append(System.lineSeparator());			
		}
		if(base != null) {
			b.append(INDENT);
			b.append(DEFAULT);
			b.append(base);
			if(min > Float.NEGATIVE_INFINITY) {
				b.append(MIN);
				b.append(min);
			}
			if(max < Float.POSITIVE_INFINITY) {
				b.append(MAX);
				b.append(max);
			}
			b.append(']');
			b.append(System.lineSeparator());
		}
		b.append(INDENT);
		b.append(F);
		b.append(key);
		b.append('=');
		b.append(value);
		b.append(System.lineSeparator());
		return b.toString();
	}


	@Override
	public String getTypeCode() {
		return F;
	}
	
	
	@Override
	public void setValue(Float val) {
		value = Math.min(max, Math.max(min, val));
	}
	
	
	public void setDefault(float def) {
		base = Math.min(max, Math.max(min, def));
	}
	
	
	public void setDefaultValue(float def) {
		value = base = Math.min(max, Math.max(min, def));
	}
	
}
