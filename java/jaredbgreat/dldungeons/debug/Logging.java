package jaredbgreat.dldungeons.debug;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import java.util.logging.Logger;

/**
 * A mostly unused wrapper for the Java logger, to make getting a logger easier.
 * 
 * @author Jared Blackburn
 */

public class Logging {
	private static Logger logger;
	private static Logging log;
	
	private Logging() {
		logger = Logger.getGlobal();
	}
	
	
	public static Logging getInstance() {
		if(log == null) {
			log = new Logging();
		}
		return log;
	}
	
	
	public static void LogError(String error) {
		if(log == null) {
			log = new Logging();
		}
		System.err.println(error);
		Logger logger = Logger.getGlobal();
		logger.severe(error);
	}
	
	
	public static void LogInfo(String info) {
		if(log == null) {
			log = new Logging();
		}
		Logger logger = Logger.getGlobal();
		logger.info(info);
	}

}
