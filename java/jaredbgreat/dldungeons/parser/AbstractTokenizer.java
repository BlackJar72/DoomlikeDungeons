package jaredbgreat.dldungeons.parser;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/

import java.util.ArrayList;

import scala.actors.threadpool.Arrays;

public class AbstractTokenizer {
	private CharSet  delim;
	private String   input;
	private String[] tokens;
	private int  numTokens = 0;
	private int  token = 0;
	private int  position = 0;
	private int  size = 0;
	private char next;
	private char[] scratchpad;
	
	
	/**
	 * This will find the the next character, and process escape characters.
	 */
	protected void nextChar() {
		next = input.charAt(position);
		if(next == '\\') {
			readEscape();
		}
		position++;
		size++;
	}
	
	
	protected void growArray() {
		Arrays.copyOf(tokens, tokens.length + 16);
	}
	
	
	protected void readTokens() {
		boolean onTokens = false;
		position++;
		while(position < input.length()) {
			nextChar();
				if(delim.contains(next)) {
					if(onTokens) {
						addToken();
						onTokens = false;
					}
				}				
				else {
					onTokens = true;
					if(next == '"') {
						readQuote();
					} else if(next == '\\') {
						readEscape();
					} else {
						scratchpad[size] = next;
					}
				}
			}
		numTokens = token;
		Arrays.copyOf(tokens, token);
	}
	
	
	protected void addToken() {
		if(tokens.length < token) {
			Arrays.copyOf(tokens, tokens.length + 16);			
		}
		tokens[token] = new String(scratchpad, 0, size - 1);
		token++;		
	}
	
	
	/**
	 * This will read every thing from the until the basis character is 
	 * encountered again, ignoring all delimeters.  The basis should always 
	 * be the same character that started the quote section, which should 
	 * usually be either a single or double quote (" or '), with double 
	 * quotes preferred.
	 * 
	 * Note that escape sequences are not read literally. but still treated as
	 * escaped.
	 * 
	 * Also not that the quotation character itself in not included; to used 
	 * these are part of a string its required to include and escaped version.
	 * 
	 * @param basis 
	 */
	protected void readQuote() {
		position++;
		while((position < input.length()) && (next != '"')) {
			nextChar();
			scratchpad[size] = next;
		}
	}
	
	
	/** 
	 * This will change a two character escape sequence into the correct 
	 * chracter, allowing quotes (among other things) to be used.  Note 
	 * that this uses the standered backslash as and that an literal 
	 * backslash must thus be represented as a double backslash. 
	 */
	protected void readEscape() {
		position++;
		if(position >= input.length()) return;
		next = input.charAt(position);
		size++;
		switch(next) {
			// TODO: More complete set of escape characters
			case '"':
				scratchpad[size] = '"';
				break;
			case '\'':
				scratchpad[size] = '\'';
				break;
			case 'n':
				scratchpad[size] = '\n';
				break;
			case 'r':
				scratchpad[size] = '\r';
				break;
			case 't':
				scratchpad[size] = '\t';
				break;
			case 'f':
				scratchpad[size] = '\f';
				break;
			case 'b':
				scratchpad[size] = '\b';
				break;
			case '0':
				scratchpad[size] = '\0';
				break;
			case 's':
				scratchpad[size] = ' ';
				break;
			// Any character not otherwise defined will be preserved as-is;
			// this also automatically covers the use of double backslash.
			default:
				scratchpad[size] = next;
				break;
		}
		
	}
	

}
