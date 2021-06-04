package jaredbgreat.dldungeons.util.debug;	

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jaredbgreat.dldungeons.Info;

/**
 * A mostly unused wrapper for the Java logger, to make getting a logger easier.
 * 
 * @author Jared Blackburn
 */

public class SimpleLogging {
	private static Logger logger;
	private static SimpleLogging log;
	
	private SimpleLogging() {		
		logger = LogManager.getLogger(Info.CHANNEL);
	}
	
	
	public static SimpleLogging getInstance() {
		if(log == null) {
			log = new SimpleLogging();
		}
		return log;
	}
	
	
	public static void logError(String error) {
		if(log == null) {
			log = new SimpleLogging();
		}
		System.err.println(error);
		logger.error(error);
	}
	
	
	public static void logInfo(String info) {
		if(log == null) {
			log = new SimpleLogging();
		}
		//new Exception().printStackTrace();
		logger.info(info);
	}

}
