package jaredbgreat.dldungeons.parser;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/

import java.util.Arrays;

/**
 * A custom alternative to Java's StringTokenizer class.  This allows for
 * the use of quotations and escape sequences.  Basically, its this makes
 * it possible to use white space inside strings.  Anything inside quotation
 * marks will be read in, processing escape sequences normally but without 
 * tokenizing the quoted text.  All the escape sequences found in Java are also
 * recognized, along wiht an \s sequences the become a single white space (an 
 * alternative to quoted strings).
 * 
 * @author JaredBGreat (Jared Blackburn)
 *
 */
public class Tokenizer {
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
	boolean keepQuotes = false; // this could be used for better control
	
	
	public Tokenizer(String input, String delims) {
		delim = new CharSet(delims);
		in = input;
		readTokens();
	}
	
	
	public Tokenizer(String input, String delims, boolean keepQuotes) {
		this.keepQuotes = keepQuotes;
		delim = new CharSet(delims);
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
		tokens = new String[(in.length() / 5) + 1];
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
		if(token >= tokens.length) {
			int newCapacity = Math.min(tokens.length * 2, tokens.length + 16);
			tokens = (String[])Arrays.copyOf(tokens, newCapacity);
		}
		tokens[token] = new String(scratchpad, 0, size);
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
		addToken();
		size = 0;
		if(keepQuotes) {
	        scratchpad[size++] = '\"';
		}
		do {
			nextChar();
			scratchpad[size++] = next;
		} while((position < in.length()) && (next != '\"'));
		if((!keepQuotes) && (next == '\"')) {
	        size--;
		}
		addToken();
		position++;
		size = 0;
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