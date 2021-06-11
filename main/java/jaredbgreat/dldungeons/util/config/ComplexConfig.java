package jaredbgreat.dldungeons.util.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jaredbgreat.dldungeons.util.debug.Logging;


@SuppressWarnings( {"rawtypes", "unused"} )
public final class ComplexConfig {
	private static final String EMPTY = "";
	private final File source;
	private final HashMap<String, AbstractConfigEntry> data;
	private final HashMap<String, ConfigCategory> cats;
	private boolean dirty;
	private ConfigCategory currentCat;
	
	
	public ComplexConfig(File file) {
		source = file;
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
	
	
	/*---------------------------------------------------------------------------------*/
	/*           GETTING SIMPLE DATA (SINGLE DATA POINTS) FROM FILE                    */
	/*---------------------------------------------------------------------------------*/
	
	
	public void readSingleEntry(String line) {
		char typeCode = line.charAt(0);
		switch(typeCode) {
			case 'S':
				readString(line.substring(2));
				break;
			case 'B':
				readBoolean(line.substring(2));
				break;
			case 'I':
				readInt(line.substring(2));
				break;
			case 'L':
				readLong(line.substring(2));
				break;
			case 'F':
				readFloat(line.substring(2));
				break;
			case 'D':
				readDouble(line.substring(2));
				break;
			default:
				Logging.logError(source + " contained invalid data type " + typeCode 
						+ " in line " + line + ".");
				break;
		}		
	}
	
	
	/**
	 * This might be simpler, cleaner way to do it that a piles of 
	 * cut-n-paste methods.
	 * 
	 * @param line
	 * @param c
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void readSimpleEntry(final String line, Class clss) throws Exception {
		if(currentCat == null) {
			currentCat = getCategroy(EMPTY);
		}
		
		int delimit = line.indexOf('=');
		AbstractConfigEntry entry =
		        (AbstractConfigEntry)clss.getConstructor(new Class[]{String.class})
		        	.newInstance(line.substring(0, delimit).trim());
		entry.readIn(line.substring(delimit + 1).trim());
		data.put(entry.key, entry);
		currentCat.add(entry);		
	}
	
	
	private void readString(final String line) {
		if(currentCat == null) {
			currentCat = getCategroy(EMPTY);
		}
		int delimit = line.indexOf('=');
		StringEntry entry = new StringEntry(line.substring(0, delimit).trim());
		entry.readIn(line.substring(delimit + 1).trim());
		data.put(entry.key, entry);
		currentCat.add(entry);		
	}
	
	
	private void readInt(final String line) {
		if(currentCat == null) {
			currentCat = getCategroy(EMPTY);
		}
		int delimit = line.indexOf('=');
		IntegerEntry entry = new IntegerEntry(line.substring(0, delimit).trim());
		entry.readIn(line.substring(delimit + 1).trim());
		data.put(entry.key, entry);
		currentCat.add(entry);		
	}
	
	
	private void readLong(final String line) {
		if(currentCat == null) {
			currentCat = getCategroy(EMPTY);
		}
		int delimit = line.indexOf('=');
		LongEntry entry = new LongEntry(line.substring(0, delimit).trim());
		entry.readIn(line.substring(delimit + 1).trim());
		data.put(entry.key, entry);
		currentCat.add(entry);		
	}
	
	
	private void readFloat(final String line) {
		if(currentCat == null) {
			currentCat = getCategroy(EMPTY);
		}		
		int delimit = line.indexOf('=');
		FloatEntry entry = new FloatEntry(line.substring(0, delimit).trim());
		entry.readIn(line.substring(delimit + 1).trim());
		data.put(entry.key, entry);
		currentCat.add(entry);		
	}
	
	
	private void readDouble(final String line) {
		if(currentCat == null) {
			currentCat = getCategroy(EMPTY);
		}		
		int delimit = line.indexOf('=');
		DoubleEntry entry = new DoubleEntry(line.substring(0, delimit).trim());
		entry.readIn(line.substring(delimit + 1).trim());
		data.put(entry.key, entry);
		currentCat.add(entry);		
	}
	
	
	private void readBoolean(final String line) {
		if(currentCat == null) {
			currentCat = getCategroy(EMPTY);
		}		
		int delimit = line.indexOf('=');
		BooleanEntry entry = new BooleanEntry(line.substring(0, delimit).trim());
		entry.readIn(line.substring(delimit + 1).trim());
		data.put(entry.key, entry);
		currentCat.add(entry);		
	}
	
	
	/*---------------------------------------------------------------------------------*/
	/*                   GETTING ARRAY/LIST DATA FROM FILE                             */
	/*---------------------------------------------------------------------------------*/
	
	
	
	
	
	/*---------------------------------------------------------------------------------*/
	/*                    RETRIECING DATA FROM DATA STORE                              */
	/*---------------------------------------------------------------------------------*/

}
