package jaredbgreat.dldungeons.util.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jaredbgreat.dldungeons.util.debug.Logging;
import jaredbgreat.dldungeons.util.parser.Tokenizer;

/**
 * This is a prototype of a better file reading system, and prototype/practice run 
 * for developing a more complex system to read configs in the old style.
 * 
 * 
 * @author Jared Blackburn
 *
 */
public class SimpleReader {
	private static final String DELIM   = "=";
	private static final String COMMENT = "#";
	private static final String EMPTY   = "";
	private static final String LIST_DELIM   = "({[,]}); \t\n\r";
	private final File source;
	private final HashMap<String, String> data;
	
	
	/**
	 * A this will create a reading of the File given, 
	 * where keys and values are separated by the equals 
	 * sign, and all data for a key-value pair must 
	 * be on on line in the file.
	 * 
	 * @param source
	 */
	public SimpleReader(File source) {
		this.source = source;
		data = new HashMap<>();
		readSource();
	}
		
	
	/**
	 * A this will create a reading of the File specified by, 
	 * the string where keys and values are separated by the  
	 * equals sign, and all data for a key-value pair must 
	 * be on on line in the file.
	 * 
	 * @param source
	 */
	public SimpleReader(String source) {
		this.source = new File(source);
		data = new HashMap<>();
		readSource();
	}
	
	
	/**
	 * A this will create a reading of the File given, 
	 * where keys and values are separated by the equals 
	 * sign.  If multiline is true then lines starting with 
	 * whitespace (' ' or tab) will treated as continuations 
	 * of the preceding line, adding their contents to the 
	 * value.  For blocks of text it is advised to use  
	 * quotation marks to preserve white space at the beginning 
	 * and end of lines.  For data intended to be interpreted as 
	 * and array or collection this is not needed unless the data 
	 * itself contains key-value pairs.
	 * 
	 * If multiline is false this will act just like the single 
	 * parameter constructor.
	 * 
	 * @param source
	 * @param multiline
	 */
	public SimpleReader(File source, boolean multiline) {
		this.source = source;
		data = new HashMap<>();
		if(multiline) {
			readSourceMulti();
		} else {
			readSource();
		}
	}	
	
	
	/**
	 * A this will create a reading of the File specified by, 
	 * the string where keys and values are separated by the equals 
	 * sign.  If multiline is true then lines starting with 
	 * whitespace (' ' or tab) will treated as continuations 
	 * of the preceding line, adding their contents to the 
	 * value.  For blocks of text it is advised to use  
	 * quotation marks to preserve white space at the beginning 
	 * and end of lines.  For data intended to be interpreted as 
	 * and array or collection this is not needed unless the data 
	 * itself contains key-value pairs.
	 * 
	 * If multiline is false this will act just like the single 
	 * parameter constructor.
	 * 
	 * @param source
	 * @param multiline
	 */
	public SimpleReader(String source, boolean multiline) {
		this.source = new File(source);
		data = new HashMap<>();
		if(multiline) {
			readSourceMulti();
		} else {
			readSource();
		}
	}
	
	
	/**
	 * Reads the file into a set of key-value pairs, while 
	 * skipping comments and empty lines.  All data must 
	 * be on the same line.
	 */
	private void readSource() {
		BufferedReader instream;
		if(source.exists() && source.isFile() && source.canRead()) {
			try {
				instream = new BufferedReader(new FileReader(source));
				String nextLine;
				Tokenizer tokenizer;
				while(instream.ready()) {
					nextLine = instream.readLine().trim();
					if(nextLine.isEmpty() || nextLine.startsWith(COMMENT)) continue;
					tokenizer = new Tokenizer(nextLine, DELIM);
					if(tokenizer.size() > 1) {
						data.put(tokenizer.nextToken().trim().toLowerCase(), tokenizer.nextToken());
					} else {
						data.put(tokenizer.nextToken().trim().toLowerCase(), EMPTY);
					}
				}
				instream.close();
			} catch (IOException e) {
				Logging.logError(e.getLocalizedMessage());
				e.printStackTrace();
			}
		} else {
			Logging.logError("FILE " + source.getPath() + " IS DOES NOT EXIST, IS READ ONLY, OR IS NOT A VALID FILE!!!");
		}
	}
	

	
	
	/**
	 * Reads the file into a set of key-value pairs, while 
	 * skipping comments and empty lines.  This is used with 
	 * the multiline constructor option for reading files 
	 * that continue on multiple lines.
	 */
	private void readSourceMulti() {
		BufferedReader instream;
		if(source.exists() && source.isFile() && source.canRead()) {
			try {
				instream = new BufferedReader(new FileReader(source));
				String nextLine, trimmed, key = null;
				StringBuilder builder = null;
				Tokenizer tokenizer;
				while(instream.ready()) {
					nextLine = instream.readLine();
					trimmed = nextLine.trim();
					if(trimmed.isEmpty() || trimmed.startsWith(COMMENT)) continue;
					if((nextLine.charAt(0) == '\t') || (nextLine.charAt(0) == ' ')) {
						if((key != null) && (builder != null)) {
							tokenizer = new Tokenizer(trimmed, EMPTY);
							builder.append('\n');
							builder.append(tokenizer.nextToken());
						} else {
							Logging.logError("The line \"" + nextLine + "\" is misplaced and not tied to a key!");
						}
					} else {
						data.put(key, builder.toString());
						tokenizer = new Tokenizer(trimmed, DELIM);
						key = tokenizer.getToken(0).trim();
						if(tokenizer.size() > 1) {
							builder = new StringBuilder(tokenizer.getToken(1));
						} else {
							builder = new StringBuilder(EMPTY);
						}
					}
				}
				if((key != null) && (builder != null)) {
					data.put(key, builder.toString());
				}
				instream.close();
			} catch (IOException e) {
				Logging.logError(e.getLocalizedMessage());
				e.printStackTrace();
			}
		} else {
			Logging.logError("FILE " + source.getPath() + " IS DOES NOT EXIST, IS READ ONLY, OR IS NOT A VALID FILE!!!");
		}		
	}
	
	
	/*---------------------------------------------------------------------------------*/
	/*           GETTING SIMPLE DATA (SINGLE DATA POINTS)                              */
	/*---------------------------------------------------------------------------------*/
		
	
	/**
	 * Retrieve a string value mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return data.get(key.toLowerCase());
	}
	
	
	/**
	 * Retrieve an int value mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public int getInt(String key, int value) {
		try {
			value = Integer.parseInt(data.get(key.toLowerCase()));
		} catch(NumberFormatException e) {
			Logging.logError(e.getMessage() + System.lineSeparator() + key + " in file " + source.toPath() + 
					" has value of " + data.get(key.toLowerCase()) + " which is not an integer.");
		}			
		return value;
	}
	
	
	/**
	 * Retrieve a long value mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public long getInt(String key, long value) {
		try {
			value = Long.parseLong(data.get(key.toLowerCase()));
		} catch(NumberFormatException e) {
			Logging.logError(e.getMessage() + System.lineSeparator() + key + " in file " + source.toPath() + 
					" has value of " + data.get(key.toLowerCase()) + " which is not a (long) integer.");
		}			
		return value;
	}
	
	
	/**
	 * Retrieve a float value mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public float getFloat(String key, float value) {
		try {
			value = Float.parseFloat(data.get(key.toLowerCase()));
		} catch(NumberFormatException e) {
			Logging.logError(e.getMessage() + System.lineSeparator() + key + " in file " + source.toPath() + 
					" has value of " + data.get(key.toLowerCase()) + " which is not a number.");
		}			
		return value;
	}
	
	
	/**
	 * Retrieve a double value mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public double getDouble(String key, double value) {
		try {
			value = Double.parseDouble(data.get(key.toLowerCase()));
		} catch(NumberFormatException e) {
			Logging.logError(e.getMessage() + System.lineSeparator() + key + " in file " + source.toPath() + 
					" has value of " + data.get(key.toLowerCase()) + " which is not a number.");
		}			
		return value;
	}		
		
	
	/**
	 * Retrieve a boolean value mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean getBoolean(String key, boolean value) {
		try {
			value = Boolean.parseBoolean(data.get(key.toLowerCase()));
		} catch(NumberFormatException e) {
			Logging.logError(e.getMessage() + System.lineSeparator() + key + " in file " + source.toPath() + 
					" has value of " + data.get(key.toLowerCase()) + " which is not a boolean value.");
		}			
		return value;
	}
	
	
	/*---------------------------------------------------------------------------------*/
	/*                            GETTING ARRAY DATA                                   */
	/*---------------------------------------------------------------------------------*/
	
	
	/**
	 * Retrieve a String array mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * @param key
	 * @param value
	 * @return	 
	 */	
	public String[] getStringArray(String key) {
		String l = data.get(key.toLowerCase());
		Tokenizer t = new Tokenizer(l, LIST_DELIM);
		String[] out = new String[t.size()];
		for(int i = 0; i < out.length; i++) {
			out[i] = t.getToken(i);
		}
		return out;
	}
	
	
	/**
	 * Retrieve an int array mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * @param key
	 * @param value
	 * @return	 
	 */	
	public int[] getIntArray(String key) {
		String l = data.get(key.toLowerCase());
		Tokenizer t = new Tokenizer(l, LIST_DELIM);
		int[] out = new int[t.size()];
		for(int i = 0; i < out.length; i++) {
			try {
				out[i] = Integer.parseInt(t.getToken(i));
			} catch (NumberFormatException e){
				Logging.logError(t.getToken(i) + " in " + l + " from key " + key + " in file " 
						+ source.getPath() + " is not an integer.");
			}
		}
		return out;
	}
	
	
	/**
	 * Retrieve a long array mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * @param key
	 * @param value
	 * @return	 
	 */	
	public long[] getLongArray(String key) {
		String l = data.get(key.toLowerCase());
		Tokenizer t = new Tokenizer(l, LIST_DELIM);
		long[] out = new long[t.size()];
		for(int i = 0; i < out.length; i++) {
			try {
				out[i] = Long.parseLong(t.getToken(i));
			} catch (NumberFormatException e){
				Logging.logError(t.getToken(i) + " in " + l + " from key " + key + " in file " 
						+ source.getPath() + " is not an integer.");
			}
		}
		return out;
	}
	
	
	/**
	 * Retrieve a float array mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * @param key
	 * @param value
	 * @return	 
	 */	
	public float[] getFloatArray(String key) {
		String l = data.get(key.toLowerCase());
		Tokenizer t = new Tokenizer(l, LIST_DELIM);
		float[] out = new float[t.size()];
		for(int i = 0; i < out.length; i++) {
			try {
				out[i] = Float.parseFloat(t.getToken(i));
			} catch (NumberFormatException e){
				Logging.logError(t.getToken(i) + " in " + l + " from key " + key + " in file " 
						+ source.getPath() + " is not a number.");
			}
		}
		return out;
	}
	
	
	/**
	 * Retrieve a double array mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * @param key
	 * @param value
	 * @return	 
	 */	
	public double[] getDoubleArray(String key) {
		String l = data.get(key.toLowerCase());
		Tokenizer t = new Tokenizer(l, LIST_DELIM);
		double[] out = new double[t.size()];
		for(int i = 0; i < out.length; i++) {
			try {
				out[i] = Double.parseDouble(t.getToken(i));
			} catch (NumberFormatException e){
				Logging.logError(t.getToken(i) + " in " + l + " from key " + key + " in file " 
						+ source.getPath() + " is not a number.");
			}
		}
		return out;
	}
	
	
	/**
	 * Retrieve a boolean array mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * @param key
	 * @param value
	 * @return	 
	 */	
	public boolean[] getBooleanArray(String key) {
		String l = data.get(key.toLowerCase());
		Tokenizer t = new Tokenizer(l, LIST_DELIM);
		boolean[] out = new boolean[t.size()];
		for(int i = 0; i < out.length; i++) {
			try {
				out[i] = Boolean.parseBoolean(t.getToken(i));
			} catch (NumberFormatException e){
				Logging.logError(t.getToken(i) + " in " + l + " from key " + key + " in file " 
						+ source.getPath() + " is not boolean data.");
			}
		}
		return out;
	}
	
	
	/*---------------------------------------------------------------------------------*/
	/*                        GETTING COLLECTION DATA                                  */
	/*---------------------------------------------------------------------------------*/
	
	
	/**
	 * Retrieve a List of Strings mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * @param key
	 * @param value
	 * @return	 
	 */	
	public List<String> getStringList(String key) {
		String l = data.get(key.toLowerCase());
		Tokenizer t = new Tokenizer(l, LIST_DELIM);
		ArrayList<String> out = new ArrayList<>();
		for(int i = 0; i < t.size(); i++) {
			out.add(t.getToken(i));
		}
		return out;
	}
	
	
	/**
	 * Retrieve a Set of String mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * @param key
	 * @param value
	 * @return	 
	 */	
	public Set<String> getStringSet(String key) {
		String l = data.get(key.toLowerCase());
		Tokenizer t = new Tokenizer(l, LIST_DELIM);
		HashSet<String> out = new HashSet<>();
		for(int i = 0; i < t.size(); i++) {
			out.add(t.getToken(i));
		}
		return out;
	}
	
	
	/**
	 * Retrieve a Map of Strings to Strings mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * The data will need to be inclosed in quotes in the file to avoid 
	 * being separated during file reading.
	 * 
	 * @param key
	 * @param value
	 * @return	 
	 */	
	public Map<String, String> getStringMap(String key) {
		String l = data.get(key.toLowerCase());
		Tokenizer t1 = new Tokenizer(l, LIST_DELIM);
		HashMap<String, String> out = new HashMap<>();
		for(int i = 0; i < t1.size(); i++) {
			Tokenizer t2 = new Tokenizer(t1.getToken(i), DELIM);
			if(t2.size() > 1) {
				out.put(t2.getToken(0), t2.getToken(1));
			}
		}
		return out;
	}
	
	
	/**
	 * Retrieve a Map of Strings to Booleans mapped to the key.  This is 
	 * case insensitive.
	 * 
	 * The data will need to be inclosed in quotes in the file to avoid 
	 * being separated during file reading.
	 * 
	 * @param key
	 * @param value
	 * @return	 
	 */	
	public Map<String, Boolean> getBooleanMap(String key) {
		String l = data.get(key.toLowerCase());
		Tokenizer t1 = new Tokenizer(l, LIST_DELIM);
		HashMap<String, Boolean> out = new HashMap<>();
		for(int i = 0; i < t1.size(); i++) {
			Tokenizer t2 = new Tokenizer(t1.getToken(i), DELIM);
			if(t2.size() > 1) {
				out.put(t2.getToken(0), Boolean.parseBoolean(t2.getToken(1)));
			}
		}
		return out;
	}

}
