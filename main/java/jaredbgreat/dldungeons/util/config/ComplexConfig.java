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
	
	
	/*---------------------------------------------------------------------------------*/
	/*           GETTING SIMPLE DATA (SINGLE DATA POINTS) FROM FILE                    */
	/*---------------------------------------------------------------------------------*/
	
	
	public void readSimpleEntry(String line) {
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
			default:
				Logging.logError(source + " contained invalid data type " + typeCode 
						+ " in line " + line + ".");
				entry = new StringEntry(line.substring(2, delimit).trim());
				break;
		}
		entry.readIn(line.substring(delimit + 1).trim());
		data.put(entry.key, entry);
		currentCat.add(entry);		
	}
	
	
	/*---------------------------------------------------------------------------------*/
	/*                   GETTING ARRAY/LIST DATA FROM FILE                             */
	/*---------------------------------------------------------------------------------*/
	
	
	
	
	
	/*---------------------------------------------------------------------------------*/
	/*                    RETRIEVING DATA FROM DATA STORE                              */
	/*---------------------------------------------------------------------------------*/
	
	
	

}
