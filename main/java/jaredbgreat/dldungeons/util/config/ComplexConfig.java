package jaredbgreat.dldungeons.util.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import jaredbgreat.dldungeons.util.debug.DebugOut;
import jaredbgreat.dldungeons.util.debug.Logging;


@SuppressWarnings( {"rawtypes", "unused"} )
public final class ComplexConfig {
	private static final String EMPTY = "";
	private final File source;
	private final HashMap<String, AbstractConfigEntry> data;
	private final HashMap<String, ConfigCategory> cats;
	private boolean dirty;
	private ConfigCategory currentCat;
	
	
	/**
	 * This constructor sets the file to be read to whatever file is passed in as-is.
	 * 
	 * @param file
	 */
	public ComplexConfig(File file) {
		source = file;
		data = new HashMap<>();
		cats = new HashMap<>();
		dirty = false;
	}
	
	
	/**
	 * This constructor will set the file to be read to something under the users normal 
	 * config directory (folder in Windowspeak).  The string passed in is the file to 
	 * be read but may contain sub-directories separated by the system separator.
	 * 
	 * Basically it will place it in [Minecraft's directory]/config/[file].
	 * 
	 * @param file
	 */
	public ComplexConfig(String file) {
		source = new File(System.getProperty("user.dir") 
				+ File.separator + "config" + File.separator + file);
		data = new HashMap<>();
		cats = new HashMap<>();
		dirty = false;
	}
	
	
	private ConfigCategory getCategroy(String name) {
		ConfigCategory out = cats.get(name);
		if(out == null) {
			out = new ConfigCategory(name);
			cats.put(name, out);
		}
		return out;		
	}
	
	
	private void writeToFile(BufferedWriter outstream) throws IOException {
		ArrayList<ConfigCategory> list = new ArrayList<>();
		cats.forEach((key,value) -> list.add(value));
		Collections.sort(list);
		for(ConfigCategory cat : list) {
			cat.writeToFile(outstream);
			outstream.newLine();
		}
	}
	
	
	private void readIn() {
		if(source.exists() && source.isFile() && source.canRead()) {
			int delim;
			String nextline;
			try {
				BufferedReader instream = new BufferedReader(new FileReader(source));
				while(instream.ready()) {
					nextline = instream.readLine().trim();
					if(nextline.isEmpty() || (nextline.charAt(0) == '#')) continue;
					delim = nextline.indexOf('{');
					currentCat = getCategroy(nextline.substring(0, delim).trim());
					readCategory(instream);
				}
				instream.close();
			} catch (IOException e) {
				Logging.logError("Failed to read config file " + source);
				Logging.logThrowable(e);
				e.printStackTrace();
			}
		} else if(source.exists()){
			if(!source.isFile()) {
				Logging.logError(source + "exists but is not a normal file!");
			} 
			if(!source.canRead()) {
				Logging.logError("We don't have permission to read " + source + "!");
			}
		}
	}
	
	
	private void readCategory(BufferedReader instream) {
		String nextline;
		boolean more = true;
		int delim;
		try {
			while(more && instream.ready()) {
				nextline = instream.readLine().trim();
				if(nextline.isEmpty() || nextline.charAt(0) == '#') continue;
				delim = nextline.indexOf('}');
				more = delim < 0;
				if(!more) {
					nextline = nextline.substring(0, delim);
				}
				if(!nextline.isEmpty()) {
					readEntry(nextline, instream);
				}
			}
		} catch (IOException e) {
			Logging.logError("Could not read a line from file " + source + " correctly (or at all).");
			e.printStackTrace();
		}
	}
	
	
	private void readEntry(String line, BufferedReader instream) throws IOException {
		int del1 = line.indexOf('=');
		int del2 = line.indexOf('[');
		if((del1 > -1) && ((del1 < del2) || (del2 < 0))) {
			readSimpleEntry(line);
		} else if(del2 > -1) {
			readListEntry(line, instream);
		}
	}
	
	
	/*---------------------------------------------------------------------------------*/
	/*           GETTING SIMPLE DATA (SINGLE DATA POINTS) FROM FILE                    */
	/*---------------------------------------------------------------------------------*/
	
	
	private void readSimpleEntry(String line) {
		//The parameter String "line" should already be trimmed.
		if(currentCat == null) {
			currentCat = getCategroy(EMPTY);
		}
		int delimit = line.indexOf('=');
		char typeCode = line.charAt(0);
		AbstractConfigEntry entry;
		switch(typeCode) {
			case 'S':
				entry = new StringEntry(line.substring(2, delimit).trim());
				break;
			case 'B':
				entry = new BooleanEntry(line.substring(2, delimit).trim());
				break;
			case 'I':
				entry = new IntEntry(line.substring(2, delimit).trim());
				break;
			case 'L':
				entry = new LongEntry(line.substring(2, delimit).trim());
				break;
			case 'F':
				entry = new FloatEntry(line.substring(2, delimit).trim());
				break;
			case 'D':
				entry = new DoubleEntry(line.substring(2, delimit).trim());
				break;
			case 'H':
				entry = new HexEntry(line.substring(2, delimit).trim());
				break;
			default:
				Logging.logError(source + " contained invalid data type " + typeCode 
						+ " in line " + line + ".");
				entry = new StringEntry(line.substring(2, delimit).trim());
				break;
		}
		entry.readIn(line.substring(delimit + 1).trim());
		if(entry.isGood()) {
			data.put(entry.key, entry);
		} else {
			Logging.logError(line + " from file " + source + " failed to read properly.");
		}
		currentCat.add(entry);		
	}
	
	
	/*---------------------------------------------------------------------------------*/
	/*                   GETTING ARRAY/LIST DATA FROM FILE                             */
	/*---------------------------------------------------------------------------------*/
	
	
	private void readListEntry(String line, BufferedReader instream) throws IOException {
		//The parameter String "line" should already be trimmed.
		if(currentCat == null) {
			currentCat = getCategroy(EMPTY);
		}
		int delimit1 = line.indexOf('[');
		int delimit2 = line.indexOf(']');
		String dat;
		if(delimit2 > delimit1) {
			dat = line.substring(delimit1 + 1, delimit2);
		} else {
			dat = readListToString(line.substring(delimit1 + 1), instream, ']');
		}
		char typeCode = line.charAt(0);
		AbstractConfigEntry entry;
		switch(typeCode) {
			case 'S':
				entry = new StringListEntry(line.substring(2, delimit1).trim());
				break;
			case 'B':
				entry = new BooleanListEntry(line.substring(2, delimit1).trim());
				break;
			case 'I':
				entry = new IntListEntry(line.substring(2, delimit1).trim());
				break;
			case 'L':
				entry = new LongEntry(line.substring(2, delimit1).trim());
				break;
			case 'F':
				entry = new FloatEntry(line.substring(2, delimit1).trim());
				break;
			case 'D':
				entry = new DoubleEntry(line.substring(2, delimit1).trim());
				break;
			default:
				Logging.logError(source + " contained invalid data type " + typeCode 
						+ " in line " + line + ".");
				entry = new StringEntry(line.substring(2, delimit1).trim());
				break;
		}
		entry.readIn(dat);
		if(entry.isGood()) {
			data.put(entry.key, entry);
		} else {
			Logging.logError(line + " from file " + source + " failed to read properly.");
		}
		currentCat.add(entry);		
	}
	
	
	private String readListToString(String line, BufferedReader instream, char delim) throws IOException {
		String next;
		boolean more = true;
		int delimit2;
		StringBuilder builder = new StringBuilder(line);
		while(more && instream.ready()) {
			next = instream.readLine();
			delimit2 = next.indexOf(delim);
			more = delim < 0;
			if(more) {
				builder.append(next);
			} else if(delimit2 > 0){
				builder.append(next.substring(0, delimit2));
			}
		}
		builder.trimToSize();
		return builder.toString();
	}
	
	
	/*---------------------------------------------------------------------------------*/
	/*                 RETRIEVING DATA FROM DATA STORE & USER API                      */
	/*---------------------------------------------------------------------------------*/
	
	
	/**
	 * Running this will read in the existing file data.
	 * 
	 * This method will return the ComplexConfig it is called on for convenient chaining.
	 * 
	 * Typically this is done as:
	 * ComplexConfig myConfig = new ComplexConfig().open();
	 * 
	 * @return
	 */
	public ComplexConfig open() {
		readIn();
		return this;
	}
	
	
	/**
	 * A factory method that creates a complex config from the supplied file and opens it.
	 * 
	 * @param file
	 * @return
	 */
	public static ComplexConfig open(String file) {
		ComplexConfig reader = new ComplexConfig(file);
		reader.readIn();
		return reader;
	}
	
	
	/**
	 * A factory method that creates a complex config from the supplied file and opens it.
	 * 
	 * @param source
	 * @return
	 */
	public static ComplexConfig open(File source) {
		ComplexConfig reader = new ComplexConfig(source);
		reader.readIn();
		return reader;
	}
	
	
	public void update() {
		if(dirty) {
			if(!source.getParentFile().exists()) {
				source.getParentFile().mkdirs();
			}
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(source));
				writeToFile(writer);
				writer.close();
			} catch (IOException e) {
				Logging.logError("Failed to write file " + source + "!");
				e.printStackTrace();
			} 
		}
	}
	
	
	/**
	 * A synonym for update(), because it should be familiar to users of older Forge versions.
	 */
	public void save() {
		update();
	}
	
	
	/**
	 * This creates a new category described by the comment; if 
	 * the category already exists it set the comment to the one 
	 * given.  Technically this is not required to create a 
	 * category, as one will automatically be created with a data 
	 * variable is requested from it.
	 * 
	 * The comment may be divided into multiple Strings, one per line 
	 * as an alternative to adding escaped characters for line ending. 
	 * If (and only if) only one string is given it will be broken up 
	 * along escaped newline and return characters.
	 * 
	 * @param name
	 * @param comment
	 */
	public void createCategory(String name, String ... comment) {
		ConfigCategory newcat = cats.get(name);
		if(newcat == null) {
			newcat = new ConfigCategory(name);
			cats.put(name, newcat);
		}
		newcat.setComment(comment);
	}
	
	
	public int getInt(String name, String category, int defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		IntEntry entry;
		AbstractConfigEntry attempt = data.get(name);		
		if((attempt != null) && (attempt instanceof IntEntry)) {
			entry = (IntEntry)attempt;
		} else {
			entry = new IntEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public int getInt(String name, String category, int defaultValue, int min, int max, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		IntEntry entry = (IntEntry)data.get(name);
		if(entry ==  null) {
			entry = new IntEntry(name);
			entry.attachData(defaultValue, min, max);
			entry.makeDefault();
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public long getLong(String name, String category, long defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		LongEntry entry = (LongEntry)data.get(name);
		if(entry ==  null) {
			entry = new LongEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public long getInt(String name, String category, long defaultValue, long min, long max, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		LongEntry entry = (LongEntry)data.get(name);
		if(entry ==  null) {
			entry = new LongEntry(name);
			entry.attachData(defaultValue, min, max);
			entry.makeDefault();
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public float getFloat(String name, String category, float defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		FloatEntry entry = (FloatEntry)data.get(name);
		if(entry ==  null) {
			entry = new FloatEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public float getFloat(String name, String category, float defaultValue, float min, float max, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		FloatEntry entry = (FloatEntry)data.get(name);
		if(entry ==  null) {
			entry = new FloatEntry(name);
			entry.attachData(defaultValue, min, max);
			entry.makeDefault();
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public double getDouble(String name, String category, double defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		DoubleEntry entry = (DoubleEntry)data.get(name);
		if(entry ==  null) {
			entry = new DoubleEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public double getDouble(String name, String category, double defaultValue, double min, double max, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		DoubleEntry entry = (DoubleEntry)data.get(name);
		if(entry ==  null) {
			entry = new DoubleEntry(name);
			entry.attachData(defaultValue, min, max);
			entry.makeDefault();
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public boolean getBoolean(String name, String category, boolean defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		BooleanEntry entry = (BooleanEntry)data.get(name);
		if(entry ==  null) {
			entry = new BooleanEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public String getString(String name, String category, String defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		StringEntry entry = (StringEntry)data.get(name);
		if(entry ==  null) {
			entry = new StringEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public int getHexInt(String name, String category, int defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		HexEntry entry = (HexEntry)data.get(name);
		if(entry ==  null) {
			entry = new HexEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue().intValue();
	}
	
	
	public int getHexInt(String name, String category, int defaultValue, int min, int max, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		HexEntry entry = (HexEntry)data.get(name);
		if(entry ==  null) {
			entry = new HexEntry(name);
			entry.attachData(defaultValue, min, max);
			entry.makeDefault();
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue().intValue();
	}
	
	
	public long getHexLong(String name, String category, long defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		HexEntry entry = (HexEntry)data.get(name);
		if(entry ==  null) {
			entry = new HexEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public long getHexLong(String name, String category, long defaultValue, long min, long max, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		HexEntry entry = (HexEntry)data.get(name);
		if(entry ==  null) {
			entry = new HexEntry(name);
			entry.attachData(defaultValue, min, max);
			entry.makeDefault();
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	/*
	 * Integer Collections
	 */
	
	
	public List<Integer> getIntList(String name, String category, List<Integer> defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		IntListEntry entry = (IntListEntry)data.get(name);
		if(entry ==  null) {
			entry = new IntListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public int[] getIntArray(String name, String category, int[] defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		IntListEntry entry = (IntListEntry)data.get(name);
		if(entry ==  null) {
			entry = new IntListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getAsArray();
	}
	
	
	public Set<Integer> getIntSet(String name, String category, Set<Integer> defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		IntListEntry entry = (IntListEntry)data.get(name);
		if(entry ==  null) {
			entry = new IntListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getAsSet();
	}
	
	/*
	 * Long Collections
	 */
	
	
	public List<Long> getlongList(String name, String category, List<Long> defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		LongListEntry entry = (LongListEntry)data.get(name);
		if(entry ==  null) {
			entry = new LongListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public long[] getlongArray(String name, String category, long[] defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		LongListEntry entry = (LongListEntry)data.get(name);
		if(entry ==  null) {
			entry = new LongListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getAsArray();
	}
	
	
	public Set<Long> getlongSet(String name, String category, Set<Long> defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		LongListEntry entry = (LongListEntry)data.get(name);
		if(entry ==  null) {
			entry = new LongListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getAsSet();
	}
	
	/*
	 * Float Collections
	 */
	
	
	public List<Float> getFloatList(String name, String category, List<Float> defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		FloatListEntry entry = (FloatListEntry)data.get(name);
		if(entry ==  null) {
			entry = new FloatListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public float[] getFloatArray(String name, String category, float[] defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		FloatListEntry entry = (FloatListEntry)data.get(name);
		if(entry ==  null) {
			entry = new FloatListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getAsArray();
	}
	
	
	public Set<Float> getFSet(String name, String category, Set<Float> defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		FloatListEntry entry = (FloatListEntry)data.get(name);
		if(entry ==  null) {
			entry = new FloatListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getAsSet();
	}
	
	/*
	 * Double Collections
	 */
	
	
	public List<Double> getDoubleList(String name, String category, List<Double> defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		DoubleListEntry entry = (DoubleListEntry)data.get(name);
		if(entry ==  null) {
			entry = new DoubleListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public double[] getDoubleArray(String name, String category, double[] defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		DoubleListEntry entry = (DoubleListEntry)data.get(name);
		if(entry ==  null) {
			entry = new DoubleListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getAsArray();
	}
	
	
	public Set<Double> getDoubleSet(String name, String category, Set<Double> defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		DoubleListEntry entry = (DoubleListEntry)data.get(name);
		if(entry ==  null) {
			entry = new DoubleListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getAsSet();
	}
	
	/*
	 * String Collections
	 */
	
	
	public List<String> getStringList(String name, String category, List<String> defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		StringListEntry entry = (StringListEntry)data.get(name);
		if(entry ==  null) {
			entry = new StringListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public String[] getStringArray(String name, String category, String[] defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		StringListEntry entry = (StringListEntry)data.get(name);
		if(entry ==  null) {
			entry = new StringListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getAsArray();
	}
	
	
	public Set<String> getStringSet(String name, String category, Set<String> defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		StringListEntry entry = (StringListEntry)data.get(name);
		if(entry ==  null) {
			entry = new StringListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getAsSet();
	}
	
	/*
	 * Boolean Collections
	 */
	
	
	public List<Boolean> getBooleanList(String name, String category, List<Boolean> defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		BooleanListEntry entry = (BooleanListEntry)data.get(name);
		if(entry ==  null) {
			entry = new BooleanListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue();
	}
	
	
	public boolean[] getBooleanArray(String name, String category, boolean[] defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		BooleanListEntry entry = (BooleanListEntry)data.get(name);
		if(entry ==  null) {
			entry = new BooleanListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getAsArray();
	}
	
	
	public Set<Boolean> getBooleanSet(String name, String category, Set<Boolean> defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		BooleanListEntry entry = (BooleanListEntry)data.get(name);
		if(entry ==  null) {
			entry = new BooleanListEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getAsSet();
	}
	
	
	public boolean[] getBooleanArrayFromBits(String name, String category, long defaultValue, int num, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		HexEntry entry = (HexEntry)data.get(name);
		if(entry ==  null) {
			entry = new HexEntry(name);
			entry.setDefaultValue(defaultValue);
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.toBooleanArray(num);
	}
	
	
	public boolean[] getBooleanArrayFromBits(String name, String category, boolean[] defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		HexEntry entry = (HexEntry)data.get(name);
		if(entry ==  null) {
			entry = new HexEntry(name);
			entry.setDefaultValue(entry.fromBooleanArray(defaultValue));
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.toBooleanArray(defaultValue.length);
	}
	
	
	public int getIntFromBooleanArray(String name, String category, boolean[] defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		HexEntry entry = (HexEntry)data.get(name);
		if(entry ==  null) {
			entry = new HexEntry(name);
			entry.setDefaultValue(entry.fromBooleanArray(defaultValue));
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue().intValue();
	}
	
	
	public Long getLongFromBooleanArray(String name, String category, boolean[] defaultValue, String ... comment) {
		ConfigCategory cat = getCategroy(category);
		HexEntry entry = (HexEntry)data.get(name);
		if(entry ==  null) {
			entry = new HexEntry(name);
			entry.setDefaultValue(entry.fromBooleanArray(defaultValue));
			data.put(name, entry);
			dirty = true;
		}
		entry.setComment(comment);
		cat.add(entry);
		return entry.getValue().longValue();
	}
	
	
	

}
