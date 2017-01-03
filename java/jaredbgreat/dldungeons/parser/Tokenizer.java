package jaredbgreat.dldungeons.parser;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/

import java.util.HashSet;

import scala.actors.threadpool.Arrays;

public class Tokenizer {
	//private final HashSet<Character>  delim;
	private final CharSet  delim;
	private String[] tokens;
	private int  token = 0;
	
	// variable for processing
	private String in;
	private int position = 0;
	private int size = 0;
	private char next = 0;
	private char[] scratchpad;
	boolean onTokens = false;
	
	
	public Tokenizer(String input, String delims) {
		delim = new CharSet(delims);
		char[] fuck = delims.toCharArray();
		for(int i = 0; i < fuck.length; i++) {
			delim.add(fuck[i]);
		}
		in = input;
		readTokens();
	}
	
	
	/**
	 * This will find the the next character, and process escape characters.
	 */
	private void nextChar() {
		next = in.charAt(position);
		if(next == '\\') {
			readEscape();
		}
		position++;
	}
	
	
	/**
	 * Reads the line of text and turns it into tokens.
	 */
	private void readTokens() {
		tokens = new String[(in.length()) + 1];
		scratchpad = new char[in.length()];
		while(position < in.length()) {
			nextChar();
				if(delim.contains(next)) {
					if(onTokens) {
						addToken();
						onTokens = false;
						size = 0;
					}
				}				
				else {
					onTokens = true;
					if(next == '\"') {
						readQuote();
					} else {
						scratchpad[size] = next;
						size++;
					}
				}
			}
		if(onTokens) {
			addToken();
			onTokens = false;
			size = 0;
		}
		tokens = (String[])Arrays.copyOf(tokens, token);
		token = 0;
	}
	
	
	/**
	 * Adds a token that has been found.
	 */
	private void addToken() {
		if(tokens.length < token) {
			String[] tmp = (String[])Arrays.copyOf(tokens, token + 16); 
			tokens = tmp;
		}
		tokens[token] = new String(scratchpad, 0, size);
		System.err.println("New token was: \"" + tokens[token] + "\"");
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
	private void readQuote() {
		onTokens = true;
		nextChar();
		while((position < in.length()) && (next != '\"')) {
			scratchpad[size] = next;
			size++;
			nextChar();
		}
	}
	
	
	/** 
	 * This will change a two character escape sequence into the correct 
	 * chracter, allowing quotes (among other things) to be used.  Note 
	 * that this uses the standered backslash as and that an literal 
	 * backslash must thus be represented as a double backslash. 
	 */
	private void readEscape() {
		do {
			onTokens = true;
			position++;
			if(position >= in.length()) return;
			next = in.charAt(position);
			switch(next) {
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
			size++;
			position++;
			next = in.charAt(position);
		} while(next == '\\');
	}
	
	
	/**
	 * Returns the number of tokens.
	 * 
	 * @return number of tokens
	 */
	public int countTokens() {
		return tokens.length;
	}
	
	
	/**
	 * Returns whether or not there are more tokens
	 * 
	 * @return
	 */
	public boolean hasMoreTokens() {
		return token < tokens.length;
	}
	
	
	/**
	 * Returns the next available token.  If there are no more tokens it 
	 * will return null.
	 * 
	 * @return the next token
	 */
	public String nextToken() {
		if(hasMoreTokens()) {
			return tokens[token++];
		} else return null;
	}
	
	
	/**
	 * Resets the to the first token 
	 */
	public void reset() {
		token = 0;
	}
	
	
	/**
	 * Gets the token at the given index.  If an index is out of range this 
	 * will return null.
	 * 
	 * @param index
	 * @return the token at the give index
	 */
	public String getToken(int index) {
		if(index >= 0 && index < tokens.length) {
			return tokens[index];
		} else return null;
	}
}