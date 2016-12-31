package jaredbgreat.dldungeons.parser;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/		

/**
 * A more advanced tokenizer to replace the use of Java's StringTokenizer class
 * and allow more advanced parsing of theme and loot files. 
 * 
 * @author JaredBGreat (Jared Blackburn)
 */
public class LineParser {
	private final ConfigLanguage lang;
	private String line; // The line to be parsed
	
	public enum ConfigLanguage {
		THEME,
		LOOT,
		NBT;
	}
	
	
	public LineParser(ConfigLanguage language, String delim) {
		lang = language;
	}
	

}
