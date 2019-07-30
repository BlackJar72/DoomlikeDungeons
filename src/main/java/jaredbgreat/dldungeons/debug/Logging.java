package jaredbgreat.dldungeons.debug;

import org.apache.logging.log4j.core.Logger;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import jaredbgreat.dldungeons.DoomlikeDungeons;

/**
 * A mostly unused wrapper for the Java logger, to make getting a logger easier.
 * 
 * @author Jared Blackburn
 */

public class Logging {
	private static Logger logger;
	private static Logging log;
	
	private Logging() {
		logger = (Logger)DoomlikeDungeons.logger;
	}
	
	
	public static Logging getInstance() {
		if(log == null) {
			log = new Logging();
		}
		return log;
	}
	
	
	public static void logError(String error) {
		if(log == null) {
			log = new Logging();
		}
		System.err.println(error);
		logger.error(error);;
	}
	
	
	public static void logInfo(String info) {
		if(log == null) {
			log = new Logging();
		}
		logger.info(info);
	}

}
